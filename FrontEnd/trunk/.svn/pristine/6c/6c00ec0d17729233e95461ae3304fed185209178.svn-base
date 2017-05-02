package com.afmobi.palmchat.ui.activity.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.ShareBroadcastRefreshChatting;
import com.afmobi.palmchat.eventbusmodel.ShowUpdateVersionEvent;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MenuAdapter;
import com.afmobi.palmchat.ui.activity.friends.ContactsFriendsFragment;
import com.afmobi.palmchat.ui.activity.friends.LocalSearchActivity;
import com.afmobi.palmchat.ui.activity.friends.RecentlyFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.UnderlinePageIndicator;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
import com.core.AfResponseComm.AfChapterInfo;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by hj on 2015/12/16.
 */
public class ChatsContactsActivity extends BaseFragmentActivity implements AfHttpResultListener,View.OnClickListener,FragmentSendFriendCallBack {
    private ViewPager viewpager;
    public ArrayList<Fragment> listFragments;
    private RecentlyFragment mChatFragment ; //
    private ContactsFriendsFragment mContactsFriendsFragment;
    private View layout_message;
    private View layout_friends;
    private TextView mtxtMessages,mtxtContacts;
    private ImageView rightImageview;
    public static final int CONTENT_FRAGMENT_MESSAGE = 0;
    public static final int CONTENT_FRAGMENT_FRIENDS = 1;
    public boolean isShareTag = false;
    public String mShareId;
    public String mTagName;
    public String mTagUrl;
    public String mContent;
    public String senderName;
    public String senderHeaderUrl;
    public int mShareTagPostNum;
    public AfChapterInfo afChapterInfo;
    /**
     * 发送广播的用户ID
     */
    public String mBroadcastAfid;
    private final int LOCALSEARCH_RESULT_CODE = 1001;
    /**
     * 中间件类
     */
    private AfPalmchat mAfCorePalmchat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_contacts);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            afChapterInfo = (AfChapterInfo) bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
        }

        isShareTag = intent.getBooleanExtra(JsonConstant.KEY_IS_SHARE_TAG, false);
        mShareId = intent.getStringExtra(JsonConstant.KEY_SHARE_ID);
        mTagName = intent.getStringExtra(JsonConstant.KEY_TAG_NAME);
        mTagUrl = intent.getStringExtra(JsonConstant.KEY_TAG_URL);
        mContent = intent.getStringExtra(JsonConstant.KEY_BROADCAST_CONTENT);
        mShareTagPostNum = intent.getIntExtra(JsonConstant.KEY_SHARE_TAG_POST_NUM, 0);
        mBroadcastAfid = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_AFID);
        senderName = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_NAME);
        senderHeaderUrl = intent.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_HEADER_URL);
        //初始化中件间
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView)findViewById(R.id.title_text)).setText(R.string.send_to_friend_contact_title);
        layout_message = findViewById(R.id.tab_message);
        layout_friends = findViewById(R.id.tab_contact);

        mtxtMessages = (TextView)findViewById(R.id.txt_messages);
        mtxtContacts = (TextView)findViewById(R.id.txt_contacts);
        layout_message.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_MESSAGE));
        layout_friends.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_FRIENDS));
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        rightImageview = (ImageView) findViewById(R.id.op2);
        rightImageview.setVisibility(View.VISIBLE);
        rightImageview.setBackgroundResource(R.drawable.selector_right_search);
        rightImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsContactsActivity.this, LocalSearchActivity.class);
                intent.putExtra("isSendFriend", true);
                intent.putExtra(JsonConstant.KEY_SHARE_ID, mShareId);
                intent.putExtra(JsonConstant.KEY_IS_SHARE_TAG, isShareTag);
                intent.putExtra(JsonConstant.KEY_BROADCAST_CONTENT, mContent);
                intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_AFID, mBroadcastAfid);
                intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_NAME, senderName);
                intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_HEADER_URL, senderHeaderUrl);
                intent.putExtra(JsonConstant.KEY_TAG_URL, mTagUrl);
                intent.putExtra(JsonConstant.KEY_TAG_NAME, mTagName);
                intent.putExtra(JsonConstant.KEY_SHARE_TAG_POST_NUM, mShareTagPostNum);
                if(afChapterInfo != null) {
                    intent.putExtra(JsonConstant.KEY_BC_AFCHAPTERINFO,afChapterInfo);
                }
                startActivityForResult(intent,LOCALSEARCH_RESULT_CODE);
            }
        });
        init();
    }
    private void init() {
        listFragments = new ArrayList<Fragment>();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowPhoneContacts",false);
        mChatFragment= new RecentlyFragment();
        mChatFragment.setArguments(bundle);
        mContactsFriendsFragment = new ContactsFriendsFragment();
        mContactsFriendsFragment.setArguments(bundle);
        listFragments.add(mChatFragment);
        listFragments.add(mContactsFriendsFragment);


        MenuAdapter adapter  = new MenuAdapter(getSupportFragmentManager(), listFragments);
        viewpager.setAdapter(adapter);

        viewpager.setOffscreenPageLimit(3);
        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        indicator.setFades(false);
        indicator.setSelectedColor(getResources().getColor(R.color.log_blue));
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                showSelectedOption(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewpager.setCurrentItem(index);
            showSelectedOption(index);

        }
    }

    final private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessagesUtils.MSG_SET_STATUS: {
                    PalmchatLogUtils.println("---www : MSG_SET_STATUS");
//                    sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                    break;
                }
            }
        }
    };

    /**
     * 切换到哪个tab页
     * @param pos
     */
    private void showSelectedOption(int pos) {
        // TODO Auto-generated method stub
        switch (pos) {
            case CONTENT_FRAGMENT_MESSAGE:
                layout_message.setSelected(true);
                layout_friends.setSelected(false);
                mtxtMessages.setTextColor(getResources().getColor(R.color.chats_17a5ef));
                mtxtContacts.setTextColor(getResources().getColor(R.color.text_level_2));
                mChatFragment.refreshData();
                break;
            case CONTENT_FRAGMENT_FRIENDS :
                layout_message.setSelected(false);
                layout_friends.setSelected(true);
                mtxtContacts.setTextColor(getResources().getColor(R.color.chats_17a5ef));
                mtxtMessages.setTextColor(getResources().getColor(R.color.text_level_2));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        PalmchatLogUtils.println("ChattingRoomMainAct onStart");
    }

    @Override
    protected void onResumeFragments() {
        // TODO Auto-generated method stub
        super.onResumeFragments();
        PalmchatLogUtils.println("ChattingRoomMainAct onResumeFragments");
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        PalmchatLogUtils.println("ChattingRoomMainAct onStop");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        PalmchatLogUtils.println("ChattingRoomMainAct onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    @Override
    public void fragmentCallBack(final boolean isPrivate, Object object) {
        AfFriendInfo friendInfo = null;
        AfGrpProfileInfo grpProfileInfo = null;
        AppDialog appDialog = new AppDialog(this);
        String showStr = null;
        if (isPrivate) {
            friendInfo = (AfFriendInfo) object;
            showStr = CommonUtils.replace("XX", friendInfo.name + "", getResources().getString(R.string.send_to_friend_toast));
        } else {
            grpProfileInfo = (AfGrpProfileInfo) object;
            showStr = CommonUtils.replace("XX", grpProfileInfo.name + "", getResources().getString(R.string.send_to_friend_toast));
        }
        final AfFriendInfo friendInfoTemp = friendInfo;
        final AfGrpProfileInfo grpProfileInfoTemp = grpProfileInfo;

        appDialog.createConfirmDialog(this, showStr, new AppDialog.OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                AfAttachPAMsgInfo afAttachPAMsgInfo = new AfAttachPAMsgInfo();
                AfMessageInfo messageInfo = null;
                String strId = null;
                afAttachPAMsgInfo.mid = mShareId;
                afAttachPAMsgInfo.afid = mBroadcastAfid;
                if (isPrivate) {
                    strId = friendInfoTemp.afId;
                    // insertBroadcastTextToDb(isPrivate,friendInfoTemp);
                } else {
                    strId = grpProfileInfoTemp.afid;
                    // insertBroadcastTextToDb(isPrivate,grpProfileInfoTemp);
                }
                //mTagUrl = "http://www.qq.com";
                if (!TextUtils.isEmpty(mTagUrl)) {
                    if (!mTagUrl.startsWith(JsonConstant.HTTP_HEAD)) {//拼接url的
                        mTagUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerInfo()[4] + mTagUrl;
                    }
                    afAttachPAMsgInfo.imgurl = mTagUrl;
                } else {
                    afAttachPAMsgInfo.imgurl = "";
                }
                showProgressDialog(R.string.please_wait);
                if (isShareTag) {
                    afAttachPAMsgInfo.postnum = mShareTagPostNum;
                    afAttachPAMsgInfo.content = mTagName;
                    afAttachPAMsgInfo.msgtype = AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG;
                    messageInfo = getMessageInfo(afAttachPAMsgInfo, strId, isPrivate);
                    mAfCorePalmchat.AfHttpAfBCShareTags(strId, System.currentTimeMillis(), mTagName, mTagUrl, messageInfo, ChatsContactsActivity.this);
                } else {
                    afAttachPAMsgInfo.content = mContent;
                    afAttachPAMsgInfo.msgtype = AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG;
                    afAttachPAMsgInfo.local_img_path = senderHeaderUrl;
                    afAttachPAMsgInfo.name = senderName;
                    messageInfo = getMessageInfo(afAttachPAMsgInfo, strId, isPrivate);
                    mAfCorePalmchat.AfHttpAfBCShareBroadCast(strId, System.currentTimeMillis(), mShareId, messageInfo, ChatsContactsActivity.this);
                }

            }

            @Override
            public void onLeftButtonClick() {
                finish();
            }
        });
        appDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        appDialog.show();
    }

    private AfMessageInfo getMessageInfo(AfAttachPAMsgInfo afAttachPAMsgInfo,String mFriendAfid,boolean isPrivate){
        AfMessageInfo messageInfo = new AfMessageInfo();//新增
        afAttachPAMsgInfo._id = mAfCorePalmchat.AfDbAttachPAMsgInsert(afAttachPAMsgInfo);

        messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
        messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
        messageInfo.attach = afAttachPAMsgInfo;
        messageInfo.attach_id = afAttachPAMsgInfo._id;
        if(afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG) {
            messageInfo.msg = afAttachPAMsgInfo.content;
        }
        if(isPrivate) {
            messageInfo.toAfId = mFriendAfid;
            messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | afAttachPAMsgInfo.msgtype;
            messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);
        }
        else{
            messageInfo.fromAfId = mFriendAfid;
            messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | afAttachPAMsgInfo.msgtype;
            messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);
        }
        messageInfo.action = MessagesUtils.ACTION_INSERT;
        //handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
        new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_SENTING, messageInfo.fid,messageInfo.type).start();
        return messageInfo;
    }

    /**
     * 更新消息发送状态 线程类
     */
    public class StatusThead extends Thread {
        private int msgId;
        private int status;
        private String fid;
        private int msgType;
        public StatusThead(int msgId, int status, String fid,int type) {
            this.msgId = msgId;
            this.status = status;
            this.fid = fid;
            msgType = type;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            int _id = DefaultValueConstant.LENGTH_0;
            AfMessageInfo msg = null;
//			update recent chats msg status
            if(MessagesUtils.isPrivateMessage(msgType)) {
                if (fid != DefaultValueConstant.MSG_INVALID_FID) {
                    _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

                } else {
                    _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
                }
                msg = mAfCorePalmchat.AfDbMsgGet(msgId);
            }
            else if(MessagesUtils.isGroupChatMessage(msgType)){
                if (fid != DefaultValueConstant.MSG_INVALID_FID) {
                    _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

                } else {
                    _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
                }
                msg = mAfCorePalmchat.AfDbGrpMsgGet(msgId);
            }
            if (msg == null) {
                PalmchatLogUtils.println("Chatting AfDbMsgGet msg:" + msg);
                return;
            }
            PalmchatLogUtils.println("---www : StatusThead isLast msg " + msgId + " msg status: " + status);
            MessagesUtils.setRecentMsgStatus(msg, status);
            mainHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();
            PalmchatLogUtils.println("update status msg_id " + _id);
            EventBus.getDefault().post(new ShareBroadcastRefreshChatting());
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        AfMessageInfo messageInfo = null;
        if(user_data != null && user_data instanceof AfMessageInfo){
            messageInfo = (AfMessageInfo)user_data;
        }
        Intent intent = new Intent();
        dismissAllDialog();
        if (Consts.REQ_CODE_SUCCESS == code) {
            //向缓存中插入一条数据
            MessagesUtils.insertMsg(messageInfo, true, true);
            AfResponseComm.AfTagShareTagOrBCResp afResponseComm = (AfResponseComm.AfTagShareTagOrBCResp) result;
            if(messageInfo != null) {
                if(afResponseComm != null){
                    AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) messageInfo.attach;
                    if(afAttachPAMsgInfo != null){
                        afAttachPAMsgInfo.postnum = afResponseComm.post_number;
                        MessagesUtils.updateShareTagMsgPostNumber(messageInfo,afAttachPAMsgInfo._id);
                    }
                }
                new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_SENT, messageInfo.fid,messageInfo.type).start();
            }
            intent.putExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS,true);
        }
        else{
        	intent.putExtra(JsonConstant.KEY_SHARE_OR_SEND_FRIENDS_ERROR_CODE,code);
            if(code == Consts.REQ_CODE_202) {
                MessagesUtils.deleteMessageFromDb(this,messageInfo);
                intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.the_broadcast_doesnt_exist));
                if(afChapterInfo != null){
                    sendUpdate_delect_BroadcastList(afChapterInfo);
                }
            }
            else if (code == Consts.REQ_CODE_UNNETWORK || code == Consts.REQ_CODE_CANCEL) {
                //向缓存中插入一条数据
                MessagesUtils.insertMsg(messageInfo, true, true);
                intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.network_unavailable));
            }
            else if(code == Consts.REQ_CODE_TAG_ILLEGAL){
                //intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, false);
            }
            if(messageInfo != null) {
                new StatusThead(messageInfo._id, AfMessageInfo.MESSAGE_UNSENT, messageInfo.fid,messageInfo.type).start();
            }
            intent.putExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
        }
        setResult(RESULT_OK,intent);
        finish();
    }

    /**
     * 发广播通知 各个页面这个广播被删除了
     *
     * @param afChapterInfo
     */
    private void sendUpdate_delect_BroadcastList(AfChapterInfo afChapterInfo) {
        afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
        EventBus.getDefault().post(afChapterInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOCALSEARCH_RESULT_CODE) {
            if (data != null) {
                boolean isSucess = data.getBooleanExtra("isSucess", false);
                boolean isCancel = data.getBooleanExtra("isCancel", false);
                String boradTips = data.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                closeActivity(isSucess, isCancel, boradTips);
            }
        }
    }

    public void closeActivity(boolean isSuccess,boolean isCancel,String brdTips){
        if(isCancel){
            finish();
        }
        else {
            Intent intent = new Intent();
            intent.putExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, isSuccess);
            if(!TextUtils.isEmpty(brdTips)) {
                intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, brdTips);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}