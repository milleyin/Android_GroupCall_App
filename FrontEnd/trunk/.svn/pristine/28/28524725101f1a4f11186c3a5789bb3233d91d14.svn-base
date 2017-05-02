package com.afmobi.palmchat.ui.activity.friends;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.EventRefreshFriendList;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.friends.adapter.PublicAccountListAdapter;
import com.afmobi.palmchat.ui.activity.invitefriends.PublicAccountsFragmentActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;

import de.greenrobot.event.EventBus;
//import com.umeng.analytics.MobclickAgent;

public class PublicAccountListActivity extends BaseActivity implements AfHttpSysListener, AfHttpResultListener, OnClickListener {
    private static final String TAG = PublicAccountListActivity.class.getCanonicalName();
    private LooperThread looperThread;
    private ListView mListView;
    private AfPalmchat mAfCorePalmchat;
    private PublicAccountListAdapter mAdapter;
    private ImageView filterImage;
    private View mViewNoPublicAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        mAfCorePalmchat.AfAddHttpSysListener(this);
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
//        MobclickAgent.onPageStart(TAG);
//        MobclickAgent.onResume(this);
        updatePublicAccountList();
    }


    @Override
    protected void onPause() {

        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mAfCorePalmchat.AfRemoveHttpSysListener(this);
        EventBus.getDefault().unregister(this);
        looperThread.looper.quit();
    }


    public void onEventMainThread(EventRefreshFriendList event) {
        updatePublicAccountList();
    }

    /**
     * ͨ��handlerˢ���б�
     */
    private void updatePublicAccountList() {

        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.obtainMessage(LooperThread.UPDATE_PUBLIC_ACCOUNT_LIST).sendToTarget();
            }
        }


    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_public_account_list);
        mViewNoPublicAccount = findViewById(R.id.no_public_account_layout);
        mListView = (ListView) findViewById(R.id.public_account_listview);
        ((TextView) findViewById(R.id.title_text)).setText(R.string.home_left_item_public_account);
        findViewById(R.id.back_button).setOnClickListener(this);
        List<AfFriendInfo> publicAccountList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).getList();
        mAdapter = new PublicAccountListAdapter(context, publicAccountList);
        mListView.setAdapter(mAdapter);
        if (publicAccountList.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mViewNoPublicAccount.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mViewNoPublicAccount.setVisibility(View.VISIBLE);
        }
        /**
         * �����ת���������
         */
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
//                MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_P_PBL);
                final AfFriendInfo afFriendInfo = mAdapter.getItem(position);
                toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
            }
        });

        /**
         * �����Ƿ�ȡ���ע
         */
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                final AfFriendInfo afFriendInfo = mAdapter.getItem(position);
                AppDialog appDialog = new AppDialog(context);
                String msg = context.getResources().getString(R.string.delete_public_account);
                appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {
                    @Override
                    public void onRightButtonClick() {
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.UNFOL_PBL);
//                        MobclickAgent.onEvent(context, ReadyConfigXML.UNFOL_PBL);
                        AfPalmchat mAfCorePalmchat = ((PalmchatApp) context
                                .getApplicationContext()).mAfCorePalmchat;
                        mAfCorePalmchat.AfHttpFriendOpr(null, afFriendInfo.afId, Consts.HTTP_ACTION_D, Consts.FRIENDS_PUBLIC_ACCOUNT,
                                Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT, null, afFriendInfo, PublicAccountListActivity.this);
                        showProgressDialog(R.string.delete);
                    }

                    @Override
                    public void onLeftButtonClick() {

                    }
                });
                appDialog.show();
                return true;
            }
        });
        filterImage = (ImageView) findViewById(R.id.op3);
        filterImage.setVisibility(View.VISIBLE);
        filterImage.setBackgroundResource(R.drawable.btn_chat_more);
        filterImage.setOnClickListener(this);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    /**
     * �������
     *
     * @param infos
     * @param afid
     * @param name
     */
    private void toChatting(AfFriendInfo infos, String afid, String name) {
        Intent intent = new Intent(this, AccountsChattingActivity.class);
        intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
        intent.putExtra(JsonConstant.KEY_BACK_TO_DEFAULT, true);
        intent.putExtra(JsonConstant.KEY_FRIEND, infos);
        startActivity(intent);
    }


    class LooperThread extends Thread {
        private static final int UPDATE_PUBLIC_ACCOUNT_LIST = 8004;
        Handler handler;
        Looper looper;

        @SuppressLint("HandlerLeak")
        @Override
        public void run() {
            Looper.prepare();//������Ϣ����
            looper = Looper.myLooper();//��ȡ��ǰ���߳�
            handler = new Handler() {
                @SuppressLint("DefaultLocale")
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case UPDATE_PUBLIC_ACCOUNT_LIST:
                            ArrayList<AfFriendInfo> realList = new ArrayList<AfFriendInfo>();
                            List<AfFriendInfo> publicAccountList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).getList();
                            for (AfFriendInfo afFriend : publicAccountList) {
                                if (afFriend.type == Consts.AFMOBI_FRIEND_TYPE_PFF) {
                                    realList.add(afFriend);
                                }
                            }
                            mHandler.obtainMessage(UPDATE_PUBLIC_ACCOUNT_LIST, realList).sendToTarget();
                            break;
                    }
                }

            };
            Looper.loop();//������Ϣѭ��
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LooperThread.UPDATE_PUBLIC_ACCOUNT_LIST:
                    List<AfFriendInfo> publicAccountList = (List<AfFriendInfo>) msg.obj;
                    if (mAdapter != null) {
                        mAdapter.setList(publicAccountList);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (publicAccountList.size() > 0) {
                        mListView.setVisibility(View.VISIBLE);
                        mViewNoPublicAccount.setVisibility(View.GONE);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mViewNoPublicAccount.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    /**
     * ɾ���ѹ�ע�Ĺ��ں�
     *
     * @param mAfCorePalmchat
     * @param afId
     */
    public void clearPrivateChatHistory(final AfPalmchat mAfCorePalmchat, final String afId) {
        new Thread(new Runnable() {
            public void run() {
                final AfMessageInfo[] recentDataArray = mAfCorePalmchat
                        .AfDbRecentMsgGetRecord(
                                AfMessageInfo.MESSAGE_TYPE_MASK_PRIV,
                                afId, 0, Integer.MAX_VALUE);
                if (null != recentDataArray && recentDataArray.length > 0) {
                    for (AfMessageInfo messageInfo : recentDataArray) {
                        mAfCorePalmchat.AfDbMsgRmove(messageInfo);
                        MessagesUtils.removeMsg(messageInfo, true, true);
                    }
                    mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afId);
//                    context.sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                }
            }

        }).start();

    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub

        dismissProgressDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                // 	delete public account
                case Consts.REQ_FRIEND_LIST:
                    final AfFriendInfo friendInfo = (AfFriendInfo) user_data;
                    CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).remove(friendInfo, true, true);
                    AfPalmchat mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
                    clearPrivateChatHistory(mAfCorePalmchat, friendInfo.afId);
                    List<AfFriendInfo> publicAccountList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).getList();
                    if (publicAccountList.size() > 0) {
                        mListView.setVisibility(View.VISIBLE);
                        mViewNoPublicAccount.setVisibility(View.GONE);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mViewNoPublicAccount.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setList(publicAccountList);
                    mAdapter.notifyDataSetChanged();
                    ToastManager.getInstance().show(context, getString(R.string.success));
                    break;
            }
        } else {
            Consts.getInstance().showToast(context, code, flag, http_code);
        }
    }

    @Override
    public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
        // TODO Auto-generated method stub
        switch (msg) {
            case Consts.AF_SYS_MSG_INIT:
            case Consts.AF_SYS_MSG_UPDATE_FRIEND:
                updatePublicAccountList();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.op3:
                startActivity(new Intent(this, PublicAccountsFragmentActivity.class));
                break;
        }
    }

}
