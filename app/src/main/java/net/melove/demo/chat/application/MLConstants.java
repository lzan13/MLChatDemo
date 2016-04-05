package net.melove.demo.chat.application;

/**
 * Class ${FILE_NAME}
 * <p/>
 * Created by lzan13 on 2015/9/10 22:04.
 */
public class MLConstants {

    public static final String ML_GCM_NUMBER = "163141467698";

    // 集成小米推送需要的 appid 和 appkey
    public static final String ML_MI_APP_ID = "2882303761517430984";
    public static final String ML_MI_APP_KEY = "5191743065984";

    // 设置消息中 msgId 扩展的 key
    public static final String ML_ATTR_MSG_ID = "ml_msg_id";

    // 设置自己扩展的 key
    public static final String ML_ATTR_BURN = "ml_burn";
    public static final String ML_ATTR_RECALL = "ml_recall";
    public static final String ML_ATTR_TYPE = "ml_type";
    public static final String ML_ATTR_AT = "ml_at";
    public static final String ML_ATTR_TOP = "ml_top";
    public static final String ML_ATTR_LAST_TIME = "ml_list_time";

    /**
     * 自定义一些错误码，表示一些固定的错误
     */
    // 撤回消息错误码，超过时间限制
    public static final int ML_ERROR_I_RECALL_TIME = 5001;
    // 撤回消息错误文字描述
    public static final String ML_ERROR_S_RECALL_TIME = "ml_max_time";


    // 界面跳转传递 username/groupid 参数的 key
    public static final String ML_EXTRA_CHAT_ID = "ml_chat_id";
    public static final String ML_EXTRA_USER_LOGIN_OTHER_DIVERS = "ml_user_login_another_devices";
    public static final String ML_EXTRA_USER_REMOVED = "ml_remove";

    // 自定义广播的 action
    public static final String ML_ACTION_INVITED = "ml_action_invited";
    public static final String ML_ACTION_CONTACT = "ml_action_contact";
    public static final String ML_ACTION_MESSAGE = "ml_action_message";


    // SharedPreference 保存内容的 key
    public static final String ML_SHARED_USERNAME = "ml_username";
    public static final String ML_SHARED_PASSWORD = "ml_password";

    public static final int ML_TIME_RECALL = 300000;


    /**
     * 当界面跳转需要返回结果时，定义跳转请求码
     */
    public static final int ML_REQUEST_CODE_PHOTO = 0x01;
    public static final int ML_REQUEST_CODE_GALLERY = 0x02;
    public static final int ML_REQUEST_CODE_VIDEO = 0x03;
    public static final int ML_REQUEST_CODE_FILE = 0x04;
    public static final int ML_REQUEST_CODE_LOCATION = 0x05;
    public static final int ML_REQUEST_CODE_GIFT = 0x06;
    public static final int ML_REQUEST_CODE_CONTACTS = 0x07;

    /**
     * 长按菜单回调的动作
     */
    public static final int ML_MSG_ACTION_COPY = 0X00;
    public static final int ML_MSG_ACTION_FORWARD = 0X01;
    public static final int ML_MSG_ACTION_DELETE = 0X02;
    public static final int ML_MSG_ACTION_RECALL = 0X03;

    /**
     * 聊天消息类型
     * 首先是SDK支持的正常的消息类型
     */
    public static final int MSG_TYPE_TEXT_SEND = 0;
    public static final int MSG_TYPE_TEXT_RECEIVED = 1;
    public static final int MSG_TYPE_IMAGE_SEND = 2;
    public static final int MSG_TYPE_IMAGE_RECEIVED = 3;
    public static final int MSG_TYPE_FILE_SEND = 4;
    public static final int MSG_TYPE_FILE_RECEIVED = 5;

    /**
     * 系统级消息类型
     */
    // 撤回类型消息
    public static final int MSG_TYPE_SYS_RECALL = 10;

}
