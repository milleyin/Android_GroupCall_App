package com.afmobi.palmchat.ui.activity.main;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.main.adapter.MyGridViewAdapter;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.cb.RefreshStateListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.RefreshQueueManager;
import com.afmobi.palmchat.ui.activity.main.helper.RefreshQueueManager.RefreshListener;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;

import de.greenrobot.event.EventBus;
//import com.umeng.analytics.MobclickAgent;

public class ChatFragment extends BaseFragment implements AfHttpResultListener,
        AfHttpSysListener, RefreshStateListener, RefreshListener {
    private   final String TAG = "ChatFragment";
    private ListView mListView;
//    private ListViewAddOn listViewAddOn = new ListViewAddOn();
    private MyGridViewAdapter adapterListView;
    private LooperThread looperThread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        EventBus.getDefault().register(this);// 注册
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RefreshQueueManager.getInstence().addListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_chat);
        initViews();
        return mMainView;
    }

    public void setListScrolltoTop() {
        if (mListView != null && mListView.getCount() > 0) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        RefreshQueueManager.getInstence().removeListener(this);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mAfCorePalmchat.AfRemoveHttpSysListener(this);
        looperThread.looper.quit();
//        ImageLoader.getInstance().cleanOtherImageCache();
//        PalmchatApp.getApplication().mMemoryBitmapCache.evictAll();
        EventBus.getDefault().unregister(this);//解注册
    }

    private void initViews() { 
        mAfCorePalmchat.AfAddHttpSysListener(this);
        mListView = (ListView) findViewById(R.id.listview);
     /*   View headerView = View.inflate(context, R.layout.header_chats, null);
        headerView.findViewById(R.id.rl_Contacts).setOnClickListener(this);
        mListView.addHeaderView(headerView);*/
        adapterListView = new MyGridViewAdapter(getActivity());
//        mListView.setOnScrollListener(new ImageOnScrollListener(mListView, listViewAddOn));
        mListView.setAdapter(adapterListView);
        adapterListView.setOnFreshListener(this);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapterListView.getCount() <= position)
                    return true;
                AppDialog appDialog = new AppDialog(context);
                String content = context.getResources().getString(R.string.delete_friend_dialog_content);
                final int positionTemp = position;
                appDialog.createConfirmDialog(context, content, new AppDialog.OnConfirmButtonDialogListener() {
                    @Override
                    public void onRightButtonClick() {
                        Message message = new Message();
                        message.what = LooperThread.DELETE_CHATS;
                        message.obj = positionTemp;
                        looperThread.handler.sendMessage(message);
                    }

                    @Override
                    public void onLeftButtonClick() {

                    }
                });
                appDialog.show();
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapterListView.getCount() <= position)
                    return;
                MainAfFriendInfo mainAfFriendInfo =  adapterListView.getItem(position);
                if(mainAfFriendInfo != null && mainAfFriendInfo.afMsgInfo != null){
                    if (MessagesUtils.isPrivateMessage(mainAfFriendInfo.afMsgInfo.type) || MessagesUtils.isSystemMessage(mainAfFriendInfo.afMsgInfo.type)) {
                        AfFriendInfo affFriendInfo = mainAfFriendInfo.afFriendInfo;
                        if (mainAfFriendInfo.afMsgInfo != null) {
//					纠正chats最近一条消息显示不准确问题
                            AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mainAfFriendInfo.afMsgInfo.getKey(), 0, 1);
                            if (null != recentDataArray && recentDataArray.length > 0) {
                                mainAfFriendInfo.afMsgInfo = recentDataArray[0];

                            }

                            mAfCorePalmchat.AfRecentMsgSetUnread(mainAfFriendInfo.afMsgInfo.getKey(), 0);
                            mainAfFriendInfo.afMsgInfo.unReadNum = 0;

                        }
                        if(affFriendInfo != null && affFriendInfo.afId != null) {
                            toChatting(affFriendInfo, affFriendInfo.afId, affFriendInfo.name);
                        }
                        CommonUtils.cancelNoticefacation(context.getApplicationContext());

                    } else if (MessagesUtils.isGroupChatMessage(mainAfFriendInfo.afMsgInfo.type)) {
                        if (mainAfFriendInfo.afMsgInfo != null) {
//						纠正chats最近一条消息显示不准确问题
                            AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mainAfFriendInfo.afMsgInfo.getKey(), 0, 1);
                            if (null != recentDataArray && recentDataArray.length > 0) {
                                mainAfFriendInfo.afMsgInfo = recentDataArray[0];
                                PalmchatLogUtils.println("--- ddd MyGridViewAdapter-- recentDataArray:" + recentDataArray[0].msg);
                            }

//					进入聊天界面，将此会话的未读数设为0
                            mAfCorePalmchat.AfRecentMsgSetUnread(mainAfFriendInfo.afMsgInfo.getKey(), 0);
                            mainAfFriendInfo.afMsgInfo.unReadNum = 0;
                            //【步骤】账号A、B互为好友； 账号A操作：创建一个群组 且B为群成员，创群成功后不修改群名称，直接在群组group1中发消息 10条--->账号B显示未读消息数10. 然后账号A修改该群名称， 再发一条消息 【结果】B账号的未读消息数变成1.（只显示修改群名称之后的未读消息数了）
                            CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).update(mainAfFriendInfo,false,true);
                        }
                        if(mainAfFriendInfo != null) {
                            toGroupChat(mainAfFriendInfo.afGrpInfo);
                        }

                        CommonUtils.cancelNoticefacation(context.getApplicationContext());
                    }
                }
            }
        });
    }

    private void toGroupChat(AfGrpProfileInfo groupListItem) {
        Bundle bundle = new Bundle();
        bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
        if (!"".equals(groupListItem.afid) || null != groupListItem.afid) {
            bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID,
                    groupListItem.afid);
        }
        if (!"".equals(groupListItem.name) || null != groupListItem.name) {
            bundle.putString(
                    GroupChatActivity.BundleKeys.ROOM_NAME,
                    groupListItem.name);
        }
        HelpManager.getInstance(context).jumpToPage(
                GroupChatActivity.class, bundle, false, 0,
                false);
    }

    private void toChatting(AfFriendInfo affFriendInfo, String afid, String name) {
        if(afid.startsWith(DefaultValueConstant._R) && !CommonUtils.isSystemAccount(affFriendInfo.afId)){
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
        } else {
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIST_T_P);

        }
        Intent intent = new Intent();
        if (afid != null && afid.startsWith(DefaultValueConstant._R)) {
            intent.setClass(context,AccountsChattingActivity.class);
        } else {
            intent.setClass(context,Chatting.class);
        }
        intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, affFriendInfo.alias);
        intent.putExtra(JsonConstant.KEY_FRIEND, affFriendInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void initData() {
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();

    }

    private static class List2Refresh implements Serializable {
        private static final long serialVersionUID = 1L;
        List<MainAfFriendInfo> rflist;
        int unreadNum;
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LooperThread.UPDATE_DATA:
                    List2Refresh refData = (List2Refresh) msg.obj;
                    if (refData != null && adapterListView != null) {
                        adapterListView.setAdapterData(refData.rflist);
                        adapterListView.notifyDataSetChanged();
                        RefreshQueueManager.getInstence().nextRefresh();
                        if (getActivity() != null && getActivity() instanceof MainTab) {
                            ((MainTab) getActivity()).setChatUnread(refData.unreadNum);
                        }
                    }
                    break;
                case LooperThread.CANCEL_NOTIFICATION:
                    if (getActivity() == null) {
                        return;
                    }
                    CommonUtils.cancelNoticefacation(getActivity().getApplicationContext());
                    break;
                case LooperThread.LOOPER_INIT_ORVER:
                    Handler handler = looperThread.handler;
                    if (null != handler) {
                        handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
                    }
                    break;
                case LooperThread.NEW_CONTACT:
//                    context.sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                       EventBus.getDefault().post(new RefreshChatsListEvent());
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 注册广播接收器
     */
 /*   private void registerBroadcastReceiver() {
        mMsgReceiver = new MsgReceiver();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(Constants.REFRESH_CHATS_ACTION);
        getActivity().registerReceiver(mMsgReceiver, filter2);
    }

    private void unRegisterBroadcastReceiver() {
        getActivity().unregisterReceiver(mMsgReceiver);
    }*/

    /**
     * 自定义BroadcastReceiver
     */
    /*class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.REFRESH_CHATS_ACTION.equals(intent.getAction())) {
                if (null != looperThread) {
                    Handler handler = looperThread.handler;
                    if (null != handler) {
                        handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
                    }
                }
            }
        }
    }*/
    public void onEventMainThread(RefreshChatsListEvent event){
        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }
    }


    @Override
    public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
        switch (msg) {
            case Consts.AF_SYS_MSG_INIT:
                if (null != looperThread) {
                    Handler handler = looperThread.handler;
                    if (null != handler) {
                        handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
                    }
                }
                break;
        }
        return false;
    }

    class LooperThread extends Thread {
        private static final int LOOPER_INIT_ORVER = 6999;
        private static final int UPDATE_DATA = 7000;
        private static final int CANCEL_NOTIFICATION = 7001;
        private static final int NEW_CONTACT = 7002;
        private static final int DELETE_CHATS = 7003;
        /**
         * 线程内部handler
         */
        Handler handler;

        /**
         * 线程内部Looper
         */
        Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            // 保持当前只有一条线程在执行查看数据操作
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case UPDATE_DATA:
                            int messageCount = 0;
                            List<MainAfFriendInfo> tempList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();
                            if (null != tempList) {
                                int size = tempList.size();
                                for (int i = 0; i < size; i++) {
                                    MainAfFriendInfo info = tempList.get(i);
                                    AfFriendInfo afFriendInfo = info.afFriendInfo;
                                    AfGrpProfileInfo afGrpInfo = info.afGrpInfo;
                                    if (null != info && null != afFriendInfo) {
                                        AfFriendInfo afFriendInfo1 = CacheManager.getInstance().searchAllFriendInfo(afFriendInfo.afId);
                                        if(afFriendInfo1 != null && !TextUtils.isEmpty(afFriendInfo1.name)) {
                                            info.afFriendInfo = afFriendInfo1;
                                        }
                                    }
                                    if (null != info && null != afGrpInfo) {
                                        info.afGrpInfo = CacheManager.getInstance().searchGrpProfileInfo(afGrpInfo.afid);
                                    }
                                    if (null != info && null != info.afMsgInfo) {
                                        int num = info.afMsgInfo.unReadNum;
                                        if (num > 0) {
                                            messageCount += num;
                                        }
                                    }
                                }
                                if (messageCount == 0) {
                                    mHandler.obtainMessage(LooperThread.CANCEL_NOTIFICATION).sendToTarget();
                                }
                            }
                            tempList.add(new MainAfFriendInfo());
                            Message mainMsg = new Message();
                            mainMsg.what = UPDATE_DATA;
//							mainMsg.obj = tempList;

                            List2Refresh refreshData = new List2Refresh();
                            refreshData.rflist = tempList;
                            refreshData.unreadNum = messageCount;
                            mainMsg.obj = refreshData;
                            mHandler.sendMessage(mainMsg);
                            break;
                        case NEW_CONTACT:
                            List<AfFriendInfo> friendList = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).getList();
                            for (AfFriendInfo afFriend : friendList) {
                                if (afFriend.is_new_contact) {
                                    MessagesUtils.addMsg2Chats(mAfCorePalmchat, afFriend.afId, MessagesUtils.ADD_CHATS_PHONE_BOOK_IMPORT);
                                    mAfCorePalmchat.AfDbProfileUpdateNewContact(afFriend.afId, false);
                                }
                            }
                            mHandler.obtainMessage(NEW_CONTACT).sendToTarget();
                            break;
                        case DELETE_CHATS:
                            int positionTemp = (int) msg.obj;
                            MainAfFriendInfo mainAfFriendInfo =  adapterListView.getItem(positionTemp);
                            if(mainAfFriendInfo != null && mainAfFriendInfo.afMsgInfo != null) {
                                MessagesUtils.removeMsg(mainAfFriendInfo.afMsgInfo, true, true);
                                AfPalmchat mAfCorePalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;
                                if (MessagesUtils.isPrivateMessage(mainAfFriendInfo.afMsgInfo.type) || MessagesUtils.isSystemMessage(mainAfFriendInfo.afMsgInfo.type)) {
                                    mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mainAfFriendInfo.afMsgInfo.getKey());
                                } else if (MessagesUtils.isGroupChatMessage(mainAfFriendInfo.afMsgInfo.type)) {
                                    mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mainAfFriendInfo.afMsgInfo.getKey());
                                }
                            }

                            onRefreshState();
                            break;
                        default:
                            break;
                    }
                }
            };
            mHandler.sendEmptyMessage(LOOPER_INIT_ORVER);
            Looper.loop();
        }
    }

    @Override
    public void startRefresh() {
        // TODO Auto-generated method stub

        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
     /*   if (v.getId() == R.id.rl_Contacts) {
            startActivity(new Intent(getActivity(), ContactsActivity.class));
        }*/
    }

    @Override
    public void onRefreshState() {
        // TODO Auto-generated method stub
        if (null != looperThread) {
            Handler handler = looperThread.handler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.UPDATE_DATA);
            }
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub

    }

}
