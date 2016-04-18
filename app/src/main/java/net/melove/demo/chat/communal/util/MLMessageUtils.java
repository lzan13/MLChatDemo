package net.melove.demo.chat.communal.util;

import android.content.Context;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.PathUtil;

import net.melove.demo.chat.R;
import net.melove.demo.chat.application.MLConstants;

/**
 * Created by lzan13 on 2016/3/22.
 * 消息处理工具类，主要做谢谢EMMessage对象的处理
 */
public class MLMessageUtils {

    /**
     * 发送一条撤回消息的透传，这里需要和接收方协商定义，通过一个透传，并加上扩展去实现消息的撤回
     *
     * @param message  需要撤回的消息
     * @param callBack 发送消息的回调，通知调用方发送撤回消息的结果
     */
    public static void sendRecallMessage(EMMessage message, final EMCallBack callBack) {
        boolean result = false;
        // 获取当前时间，用来判断后边撤回消息的时间点是否合法，这个判断不需要在接收方做，
        // 因为如果接收方之前不在线，很久之后才收到消息，将导致撤回失败
        long currTime = MLDate.getCurrentMillisecond();
        long msgTime = message.getMsgTime();
        // 判断当前消息的时间是否已经超过了限制时间，如果超过，则不可撤回消息
        if (currTime < msgTime || (currTime - msgTime > MLConstants.ML_TIME_RECALL)) {
            callBack.onError(MLConstants.ML_ERROR_I_RECALL_TIME, MLConstants.ML_ERROR_S_RECALL_TIME);
            return;
        }
        // 获取消息 id，作为撤回消息的参数
        String msgId = message.getMsgId();
        // 创建一个CMD 类型的消息，将需要撤回的消息通过这条CMD消息发送给对方
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        // 判断下消息类型，如果是群聊就设置为群聊
        if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            cmdMessage.setChatType(EMMessage.ChatType.GroupChat);
        }
        // 设置消息接收者
        cmdMessage.setReceipt(message.getTo());
        // 创建CMD 消息的消息体 并设置 action 为 recall
        String action = MLConstants.ML_ATTR_RECALL;
        EMCmdMessageBody body = new EMCmdMessageBody(action);
        cmdMessage.addBody(body);
        // 设置消息的扩展为要撤回的 msgId
        cmdMessage.setAttribute(MLConstants.ML_ATTR_MSG_ID, msgId);
        // 确认无误，开始发送撤回消息的透传
        cmdMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void onError(int i, String s) {
                callBack.onError(i, s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
        // 准备工作完毕，发送消息
        EMClient.getInstance().chatManager().sendMessage(cmdMessage);
        EMClient.getInstance().chatManager().getConversation(message.getTo()).removeMessage(cmdMessage.getMsgId());
    }

    /**
     * 收到撤回消息，这里需要和发送方协商定义，通过一个透传，并加上扩展去实现消息的撤回
     *
     * @param cmdMessage 收到的透传消息，包含需要撤回的消息的 msgId
     * @return 返回撤回结果是否成功
     */
    public static boolean receiveRecallMessage(Context context, EMMessage cmdMessage) {
        boolean result = false;
        // 从cmd扩展中获取要撤回消息的id
        String msgId = cmdMessage.getStringAttribute(MLConstants.ML_ATTR_MSG_ID, null);
        if (msgId == null) {
            MLLog.d("recall - 3 %s", msgId);
            return result;
        }
        // 根据得到的msgId 去本地查找这条消息，如果本地已经没有这条消息了，就不用撤回
        EMMessage oldMessage = EMClient.getInstance().chatManager().getMessage(msgId);
        if (oldMessage == null) {
            MLLog.d("recall - 3 message is null %s", msgId);
            return result;
        }

        // 这里根据是否是群组聊天来判断会话的chatId
        String chatId = oldMessage.getChatType() == EMMessage.ChatType.Chat ? oldMessage.getFrom() : oldMessage.getTo();
        /**
         * 创建一条接收方的消息，因为最新版SDK不支持setType，所以其他类型消息无法更新为TXT类型，
         * 这里只能新建消息，并且设置消息类型为TXT，
         */
        // 更改要撤销的消息的内容，替换为消息已经撤销的提示内容，因为自己是接收方，所以创建一条接收的消息
        EMMessage recallMessage = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        EMTextMessageBody body = new EMTextMessageBody(String.format(context.getString(R.string.ml_hint_msg_recall_by_user), oldMessage.getUserName()));
        recallMessage.addBody(body);
        // 设置发送方和接收方
        recallMessage.setFrom(oldMessage.getFrom());
        if (oldMessage.getChatType() == EMMessage.ChatType.Chat) {
            recallMessage.setReceipt(EMClient.getInstance().getCurrentUser());
        } else {
            recallMessage.setReceipt(chatId);
        }
        // 设置新消息的 msgId为撤销消息的 msgId
        recallMessage.setMsgId(msgId);
        // 设置新消息的 msgTime 为撤销消息的 mstTime
        recallMessage.setMsgTime(oldMessage.getMsgTime());
        // 设置扩展为撤回消息类型，是为了区分消息的显示
        recallMessage.setAttribute(MLConstants.ML_ATTR_RECALL, true);
        // 删除旧消息
        EMClient.getInstance().chatManager().getConversation(chatId).removeMessage(msgId);
        // 保存消息到本地
        EMClient.getInstance().chatManager().saveMessage(recallMessage);
        // 设置此条消息为已读
        EMClient.getInstance().chatManager().getConversation(chatId).markMessageAsRead(recallMessage.getMsgId());
        return result;
    }

    /**
     * 获取图片消息的缩略图本地保存的路径
     *
     * @param fullSizePath 缩略图的原始路径
     * @return 返回本地路径
     */
    public static String getThumbImagePath(String fullSizePath) {
        String thumbImageName = MLCrypto.cryptoStr2SHA1(fullSizePath);
        String path = PathUtil.getInstance().getHistoryPath() + "/" + "thumb_" + thumbImageName;
        return path;
    }
}