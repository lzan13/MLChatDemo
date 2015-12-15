package net.melove.demo.chat.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

import net.melove.demo.chat.R;
import net.melove.demo.chat.activity.MLChatActivity;
import net.melove.demo.chat.activity.MLMainActivity;
import net.melove.demo.chat.adapter.MLConversationAdapter;
import net.melove.demo.chat.application.MLConstants;
import net.melove.demo.chat.entity.MLConversationEntity;
import net.melove.demo.chat.widget.MLToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 单聊会话列表界面Fragment
 */
public class MLConversationsFragment extends MLBaseFragment {

    private Activity mActivity;
    private List<MLConversationEntity> mConversationList;
    private String[] mMenus = null;
    private ListView mListView;


    public static MLConversationsFragment newInstance() {
        MLConversationsFragment fragment = new MLConversationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MLConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getParentFragment().getActivity();
        getArguments().getString("");
        init();
        initConversationListView();
    }

    private void init() {
        mMenus = new String[]{
                mActivity.getResources().getString(R.string.ml_menu_conversation_top),
                mActivity.getResources().getString(R.string.ml_menu_conversation_clear),
                mActivity.getResources().getString(R.string.ml_menu_conversation_delete)
        };

    }

    private void initConversationListView() {
        MLConversationEntity temp = null;

        Map<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        mConversationList = new ArrayList<MLConversationEntity>();
        for (EMConversation conversation : conversations.values()) {
            temp = new MLConversationEntity(conversation);
            mConversationList.add(temp);
        }

        MLConversationAdapter mAdapter = new MLConversationAdapter(mActivity, mConversationList);

        mListView = (ListView) getView().findViewById(R.id.ml_listview_conversation);
        mListView.setAdapter(mAdapter);
        // 设置列表项点击监听
        setItemClickListener();

        // 设置列表项长按监听
        setItemLongClickListener();
        // 设置长按弹出上下文菜单，和 onContextItemSelected 配套使用
//        setContextMenuListener();

    }

    private void setContextMenuListener() {
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "Delete");
                menu.add(0, 1, 0, "Top");
                menu.add(0, 2, 0, "Other");
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:

                break;
            case 1:
                break;
            case 2:

                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * ListView 控件点击监听
     */
    private void setItemClickListener() {
        // ListView 的点击监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mActivity, MLChatActivity.class);
                intent.putExtra(MLConstants.ML_C_USERNAME, mConversationList.get(position).getUsername());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, ((MLMainActivity) mActivity).getToolbar(), "toolbar");
                ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
            }
        });
    }

    /**
     * ListView 列表项的长按监听
     */
    private void setItemLongClickListener() {
        // ListView 的长按监听
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                MLToast.makeToast("选择长按操作" + position).show();
                new AlertDialog.Builder(mActivity)
                        .setTitle(R.string.ml_title_dialog_conversation)
                        .setItems(mMenus, null)
                        .show();

                return true;
            }
        });
    }
}
