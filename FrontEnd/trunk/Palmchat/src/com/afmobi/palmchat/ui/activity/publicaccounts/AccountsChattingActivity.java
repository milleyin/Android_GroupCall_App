package com.afmobi.palmchat.ui.activity.publicaccounts;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.PalmchatApp.MessageReadReceiver;
import com.afmobi.palmchat.PalmchatApp.MessageReceiver;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.LaunchActivity;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomListActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.friends.LocalSearchActivity;
import com.afmobi.palmchat.ui.activity.friends.PublicAccountListActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.adapter.AccountsChattingAdapter;
import com.afmobi.palmchat.ui.customview.CutstomEditText;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.StartTimer;
import com.afmobi.palmchat.util.TipHelper;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfAttachPAMsgInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfOnlineStatusInfo;
import com.core.AfPalmchat;
import com.core.AfPublicAccountResp;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.greenrobot.event.EventBus;

public class AccountsChattingActivity extends BaseActivity  implements OnClickListener,IXListViewListener,MessageReceiver,MessageReadReceiver,
			AfHttpResultListener,PalmchatApp.ColseSoftKeyBoardLister,OnEditorActionListener {
    /**
     * tag
     */
    private final String TAG = AccountsChattingActivity.class.getSimpleName();
    /**
     * 标题
     */
    private String mTitleName;
    /**
     * 对方ID
     */
    private String mAccountsAfid;
    /**
     * 输入框
     */
    private CutstomEditText mCETxt_Input;
    /**
     * 中间件类
     */
    private AfPalmchat mAfCorePalmchat;
    /**
     * 公共账号ID
     */
    private String mOfficialAccountAfid;
    /**
     * 聊天列表
     */
    private XListView mXListView;
    /**
     * 发送按钮
     */
    private ImageView mIv_SendMsg;
    /**
     * 标题
     */
    private TextView mTV_TitleName;
    /**
     * 提示内容
     */
    private View viewFrameToast;
    /**
     * 第三方消息名
     */
    private TextView mTv_OtherMessageName;
    /**
     * 第三方消息来时的图标
     */
    private View mTv_OtherMessageColon;
    /***/
    private TextView mTv_OtherMessageToast;
    /**
     * 未读消息布局
     */
    private View mV_Unread;
    /**
     * 未读消息textview
     */
    private TextView mTV_Unread;
    /**
     * 未读消息提示数
     */
    private int mUnreadTipsCount;
    /**
     * 底部布局
     */
    private RelativeLayout mRl_bottom_layout;
    /**
     * 消息列表适配器
     */
    private AccountsChattingAdapter mAccountsChattingAdapter;
    /**
     * 消息列表
     */
    private ArrayList<AfMessageInfo> mAfMessageInfos = new ArrayList<AfMessageInfo>();
    /**
     * 有通知
     */
    private boolean mIsNoticefy = true;
    /**
     * 滚动条是否滑到底部
     */
    private boolean mIsBottom = false;
    /**
     * 获取数据偏移量
     */
    private int mOffset = 0;
    /**
     * 每次取得数据条数
     */
    private int mCount = 20;
    private int mPageId;
    /**
     * 发送已读
     */
    private boolean toSendRead = false;
    /***/
    private boolean mIsFirst = true;
    /***/
    private boolean pop = false;
    /**
     * 是否正在刷新
     */
    private boolean isRefreshing = false;
    /***/
    private boolean isInResume = false;
    /*below for already read sign code*/
    private final static int READ_INTERVAL_TIME = 10000;
    /**
     * 上一次震动时间
     **/
    private long lastSoundTime;
    private int _end_index;
    /**
     * 格式化时间
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
    private Handler mChattingAccountsHandler = new Handler();
    /**
     * 标识公共帐号查看历史消息
     */
    private boolean mIsViewHistory;
    /**
     * 好友类
     */
    AfFriendInfo afFriend; //add by zhh

    /**
     * 消息类
     */
    AfMessageInfo afMessageInfo;
    /**
     * 跳转profile
     */
    private ImageView mIvAccountProfile;

    /**
     * 记录已从服务器端获取的历史详细信息的个数，达到mAllCount时，即显示到列表上
     */
    private int mloadCount;
    /**
     * 每次从服务器端获取的历史记录总数
     */
    private int mAllCount;
    /**
     * 每次取得历史数据条数
     */
    private int mHistoryCount = 6;

    /**
     * 是否显示进入profile入口
     */
//    private boolean misShowProfileImg;
    //没有数据情况下提示布局
    private LinearLayout mLinFollowingNoDataArea;
    private boolean isLoadingMore_mListview;
    ArrayList<AfAttachPAMsgInfo> mAfMessageInfosLoadMore;
    private int mRequestCode = 1000;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int code = -2;
            if (toSendRead) {
                toSendRead = false;
                code = mAfCorePalmchat.AfHttpSendMsg(mAccountsAfid, System.currentTimeMillis(), null, Consts.MSG_CMMD_READ, Consts.REQ_CODE_READ, AccountsChattingActivity.this);
            }
            PalmchatLogUtils.println("mChattingAccountsHandler  code  " + code);
            mChattingAccountsHandler.postDelayed(this, READ_INTERVAL_TIME);
        }
    };
    /**
     * Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LooperThread.INIT_FINISH://初始化结束后去读取最近的消息
                    getMsgData(true);
                    break;
                case LooperThread.GETMSG_FROM_DB:
                    List<AfMessageInfo> recentData =(List<AfMessageInfo>) msg.obj;
                    boolean isInit=msg.arg1==LooperThread.LOADTYPE_NEW?true:false;
                    if (isInit) { //初始进入此页面时初始化数据
                        mAfMessageInfos.clear();
                    } else {
                        stopRefresh();
                    }
                    bindData(recentData);
                    if (isInit) {
                        getData();
                    }
                   break;
                //发送文本信息
                case MessagesUtils.MSG_TEXT: {
                    AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
                    send(msgInfoText.msg, msgInfoText);

                    break;
                }

                case MessagesUtils.MSG_SET_STATUS: {
                    PalmchatLogUtils.println("---www : MSG_SET_STATUS");
//                sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                    break;
                }

                case MessagesUtils.EDIT_TEXT_CHANGE: {
                    break;
                }

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAfMessageInfosLoadMore = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_chatting_officialaccounts);


        //if is from forward msg
        Intent intent = getIntent();
        mIsViewHistory = intent.getBooleanExtra(JsonConstant.VIEW_HISTORY, false);
        isInResume = intent.getBooleanExtra(JsonConstant.KEY_FLAG, false);
        //isForward = intent.getBooleanExtra(JsonConstant.KEY_FORWARD, false);
        mAccountsAfid = intent.getStringExtra(JsonConstant.KEY_FROM_UUID);
        afFriend = (AfFriendInfo) intent.getSerializableExtra(JsonConstant.KEY_FRIEND); //add by zhh 为了避免外部列表有消息而点击进入则为空
        mRl_bottom_layout = (RelativeLayout) findViewById(R.id.officialchatting_bottom_layout_id);
        //标题
        mTV_TitleName = (TextView) findViewById(R.id.title_text);
        mXListView = (XListView) findViewById(R.id.officialchatting_listview);
        //设置listview是否显示底部加载数据提示
        mXListView.setOnScrollListener(new ImageOnScrollListener(mXListView ));
        if(mIsViewHistory) {
            mXListView.setPullLoadEnable(true);
            mRl_bottom_layout.setVisibility(View.GONE);
        }
        mXListView.setXListViewListener(this);


        mCETxt_Input = (CutstomEditText) findViewById(R.id.officialchatting_message_edit);
        mCETxt_Input.addTextChangedListener(new LimitTextWatcher(mCETxt_Input, ReplaceConstant.MAX_SIZE, mHandler, MessagesUtils.EDIT_TEXT_CHANGE));
        mCETxt_Input.setMaxLength(ReplaceConstant.MAX_SIZE);
        mCETxt_Input.setOnEditorActionListener(this);
        mCETxt_Input.setOnClickListener(this);
        mCETxt_Input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                PalmchatLogUtils.println("Chatting  onFocusChange  " + hasFocus);
                if (hasFocus) {
                    mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    mXListView.setSelection(mAccountsChattingAdapter.getCount());
                }
            }
        });


        //发送按钮
        mIv_SendMsg = (ImageView) findViewById(R.id.officialchatting_send_button);
        //
        viewFrameToast = findViewById(R.id.officialchatting_toast_frame);
        mTv_OtherMessageName = (TextView) findViewById(R.id.textview_name);
        mTv_OtherMessageColon = (TextView) findViewById(R.id.textview_colon);
        mTv_OtherMessageToast = (TextView) findViewById(R.id.textview_message);
        //未读消息
        mV_Unread = findViewById(R.id.officialchatting_unread);
        mV_Unread.setOnClickListener(this);
        //
        mTV_Unread = (TextView) findViewById(R.id.textView_unread);
        mTV_Unread.setOnClickListener(this);
        //返回按钮
        findViewById(R.id.back_button).setOnClickListener(this);
        mIv_SendMsg.setOnClickListener(this);

        mIvAccountProfile = (ImageView) findViewById(R.id.op2);
//        if(misShowProfileImg) {
//            mIvAccountProfile.setVisibility(View.VISIBLE);
//        }
        if(mAccountsAfid != null && !mAccountsAfid.toLowerCase().equals(RequestConstant.PALMCHAT_ID) && afFriend.unfollow_opr == false)
        {
            mIvAccountProfile.setVisibility(View.VISIBLE);
        }
        if(mIsViewHistory){
            mIvAccountProfile.setVisibility(View.GONE);
        }
        mLinFollowingNoDataArea = (LinearLayout)findViewById(R.id.lin_following_no_data_area);
    }

    @Override
    public void init() {
        //初始化中件间
        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        Intent intent = getIntent();
        if(intent!=null){
            mTitleName = getIntent().getStringExtra(JsonConstant.KEY_FROM_NAME);
        }
        if(!mIsViewHistory) {
            if (!TextUtils.isEmpty(mTitleName)) {
                mTV_TitleName.setText(mTitleName);
            }
        }else{
                mTV_TitleName.setText(R.string.view_history);
        }
        mAccountsChattingAdapter = new AccountsChattingAdapter(this, mAfMessageInfos, mXListView, mAccountsAfid,mIsViewHistory);
        mXListView.setAdapter(mAccountsChattingAdapter);
        PalmchatApp.getApplication().addMessageReceiver(this);
        PalmchatApp.getApplication().setMessageReadReceiver(this);
        PalmchatApp.getApplication().setColseSoftKeyBoardListe(this);

        AfFriendInfo friendInfo = CacheManager.getInstance().searchAllFriendInfo(mAccountsAfid);
        if (friendInfo == null) {
            friendInfo = afFriend;
        }
        mIvAccountProfile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toPublicAccountDetail(afFriend);
            }
        });
        if (mIsViewHistory) {//查询历史消息
            loadData();
        } else {
            //初始化列表数据
           //new GetDataTask(true).execute();
            looperThread = new LooperThread();//用于一步读取历史记录
            looperThread.setName(TAG);
            looperThread.start();
        }

    }

    private void loadData() {
        mPageId = (int) System.currentTimeMillis() + new Random(10000).nextInt();
        mAfCorePalmchat.AfHttpPublicAccountGetHistory(mAccountsAfid, mPageId, mOffset, mHistoryCount, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mIsViewHistory) {
            getData();
            String msgContent = mAfCorePalmchat.AfDbGetMsgExtra(mAccountsAfid);
            mCETxt_Input.setText(EmojiParser.getInstance(context).parse(msgContent));
            CharSequence text = mCETxt_Input.getText();
            if (text.length() > 0) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }

            if (!CommonUtils.isEmpty(mAccountsAfid) && !mAccountsAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
                mChattingAccountsHandler.postDelayed(runnable, READ_INTERVAL_TIME);
            }

            if (CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().containsKey(mAccountsAfid)) {
                pop = true;
                getMsgData(false);//new GetDataTask(false).execute();
                CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().clear();
            }
        }
        else if(mAccountsChattingAdapter.getCount() == 0){
            showRefresh();
        }
        CommonUtils.cancelNoticefacation(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        PalmchatLogUtils.println(TAG + "onActivityResult--->" + requestCode + "--intent--" + intent);
        if (resultCode == RESULT_OK) {
            boolean isFollowed = intent.getBooleanExtra(DefaultValueConstant.ISFOLLOWED, false);
            if (!isFollowed) {
                doBack();
            }
        }


    }

    /**
     * 跳转到公共帐号详情页面
     *
     * @param afFriendInfo
     */
    private void toPublicAccountDetail(
            final AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(this, PublicAccountDetailsActivity.class);
        intent.putExtra("Info", afFriendInfo);
        startActivityForResult(intent,mRequestCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsFirst = true;
        mOffset = 0;
        int dbInt = mAfCorePalmchat.AfDbSetMsgExtra(mAccountsAfid, getEditTextContent());
        PalmchatLogUtils.println("mFriendAfid  " + mAccountsAfid + "  onPause  dbInt " + dbInt + "  getEditTextContent  " + getEditTextContent());


        VoiceManager.getInstance().completion();
        mChattingAccountsHandler.removeCallbacks(runnable);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            //返回按钮
            case R.id.back_button:
                doBack();
                break;

            //发送按钮
            case R.id.officialchatting_send_button:
                sendTextOrEmotion();
                break;
            //点击未读消息时
            case R.id.textView_unread:
                doViewUnread();
                break;

            case R.id.officialchatting_message_edit:
                doEditClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        if(!mIsViewHistory) {
            if (!isRefreshing) {
                isRefreshing = true;
                mOffset = mAfMessageInfos.size();
                mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                getMsgData(false);//new GetDataTask(false).execute();
            }
        }
    }

    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub
        if(mIsViewHistory) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                if (!isLoadingMore_mListview) {
                    mloadCount = 0;
                    mAfMessageInfosLoadMore.clear();
                    isLoadingMore_mListview = true;
                    mOffset = mAfMessageInfos.size();
                    loadData();
                }
            } else {
                ToastManager.getInstance().show(this, R.string.network_unavailable);
                isLoadingMore_mListview = false;
                mXListView.stopLoadMore();
            }
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        PalmchatLogUtils.println("code " + code + "  flag  " + flag + "  result  " + result + "  user_data  " + user_data);
        if (Consts.REQ_CODE_SUCCESS == code) {
            switch (flag) {
                case Consts.REQ_MSG_SEND:
                    doReqMsgSend(user_data, result, code, flag);
                    break;
                case Consts.REQ_PUBLIC_ACCOUNT_HISTORY:
                    if (result != null) {
                        AfPublicAccountResp afPublicAccountResp = (AfPublicAccountResp) result;
                        Byte k = 0;
                        boolean isMessageAndImg = false;
                        if (afPublicAccountResp != null && afPublicAccountResp.historylist != null && afPublicAccountResp.historylist.length > 0) {
                            mAllCount = afPublicAccountResp.historylist.length;
                            for (AfAttachPAMsgInfo afAttachPAMsgInfo : afPublicAccountResp.historylist) {
                                //获取图文信息时，通过id更新
                                afAttachPAMsgInfo._id = k++;
                                mAfMessageInfosLoadMore.add(afAttachPAMsgInfo);
                            }
                            for(AfAttachPAMsgInfo afAttachPAMsgInfo : mAfMessageInfosLoadMore) {
                                if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_PA_URL) {
                                    isMessageAndImg = true;
                                    //获取图文信息
                                    mAfCorePalmchat.AfHttpHttpPublicAccountGetDetail(afAttachPAMsgInfo.url, afAttachPAMsgInfo._id, this);
                                }
                                else{
                                    mloadCount ++;
                                }
                            }
                            if(!isMessageAndImg){ //如果没有图文信息，则立即显示
                                setLoadMore();
                            }
                            if(mAccountsChattingAdapter.getCount() > 0) {
                                showNoData(false);
                            }
                        }
                        else {
                            //ToastManager.getInstance().show(this,R.string.no_data);
                            mXListView.setPullLoadEnable(false);
                            isLoadingMore_mListview = false;
                            stopRefresh();
                            showNoData(mAccountsChattingAdapter.getCount() <= 0);
                        }
                    }
                    break;
                case Consts.REQ_FLAG_POLLING_PA_GETDETAIL:
                    if(result != null){
                        int id = (int) user_data;
                        AfAttachPAMsgInfo afAttachPAMsgInfoTemp = (AfAttachPAMsgInfo) result;
                        if(afAttachPAMsgInfoTemp != null && mAfMessageInfosLoadMore != null){
                            for(AfAttachPAMsgInfo afAttachPAMsgInfo : mAfMessageInfosLoadMore){
                                if(afAttachPAMsgInfo._id == id){//根据Id更新图文信息
                                    afAttachPAMsgInfo.url = afAttachPAMsgInfoTemp.url;
                                    afAttachPAMsgInfo.afid = afAttachPAMsgInfoTemp.afid;
                                    afAttachPAMsgInfo.author = afAttachPAMsgInfoTemp.author;
                                    afAttachPAMsgInfo.content = afAttachPAMsgInfoTemp.content;
                                    afAttachPAMsgInfo.imgurl = afAttachPAMsgInfoTemp.imgurl;
                                    afAttachPAMsgInfo.title = afAttachPAMsgInfoTemp.title;
                                    afAttachPAMsgInfo.time_int64 = afAttachPAMsgInfoTemp.time_int64;
                                }
                            }
                        }
                        mloadCount ++ ;
                        if(mloadCount == mAllCount)//如果此加载的图文信息已经获取完，则显示到列表中
                        {
                            setLoadMore();
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            switch (flag) {
                case Consts.REQ_FLAG_POLLING_PA_GETDETAIL:
                    mloadCount++;
                    if(mloadCount == mAllCount)
                    {
                        setLoadMore();
                    }
                    break;
                default:
                    break;
            }
            stopRefresh();
            showNoData(mAccountsChattingAdapter.getCount() <= 0);
            Consts.getInstance().showToast(context, code, flag, http_code);
        }
    }

    /**
     * 设置加载的信息
     */
    private void setLoadMore(){
        for(AfAttachPAMsgInfo afAttachPAMsgInfo : mAfMessageInfosLoadMore) {
            if (afAttachPAMsgInfo.msgtype == AfMessageInfo.MESSAGE_PA_URL && TextUtils.isEmpty(afAttachPAMsgInfo.title)) {//过虑掉解析错误的
                continue;
            }
            afMessageInfo = new AfMessageInfo();
            afMessageInfo.fromAfId = mAccountsAfid;
            afMessageInfo.status = 30;
            afMessageInfo.type = afAttachPAMsgInfo.msgtype;
            afMessageInfo.attach = afAttachPAMsgInfo;
            afMessageInfo.msg = afAttachPAMsgInfo.content;
            afMessageInfo.client_time = afAttachPAMsgInfo.time_int64;
            mAfMessageInfos.add(afMessageInfo);
        }
        mAfMessageInfosLoadMore.clear();
        isLoadingMore_mListview = false;
        stopRefresh();
        mAccountsChattingAdapter.notifyDataSetChanged();
    }

    /**
     * 当前页面接收到消息的处理
     */
    @Override
    public void handleMessageFromServer(final AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        Log.d(TAG, "===:" + "handleMessageFromServer");
        PalmchatLogUtils.println("Chatting " + afMessageInfo);
        if (MessagesUtils.isGroupSystemMessage(afMessageInfo.type)) {
            PalmchatLogUtils.println("Chatting isGroupSystemMessage return" + afMessageInfo.msg);
            return;
        }

        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
        if (mAccountsAfid.equals(afMessageInfo.fromAfId) && (MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type)) && !mIsViewHistory) {//当前聊天人发过来的信息
            setUnReadCount();
            if (System.currentTimeMillis() - afMessageInfo.server_time * 1000 < 10 * 1000 * 60) {
                if (PalmchatApp.getApplication().getOnlineStatusInfoMap().containsKey(mAccountsAfid)) {
                    AfOnlineStatusInfo afOnlineStatusInfo = PalmchatApp.getApplication().getOnlineStatusInfoMap().get(mAccountsAfid);
                    afOnlineStatusInfo.status = AfOnlineStatusInfo.AFMOBI_ONLINESTAUS_INFO_ONLINE;
                    afOnlineStatusInfo.ts = System.currentTimeMillis();
                    PalmchatApp.getApplication().setOnlineStatusInfoMap(mAccountsAfid, afOnlineStatusInfo);
                }
            }
            if (AccountsChattingActivity.class.getName().equals(CommonUtils.getCurrentActivity(this))) {
                new Thread(new Runnable() {
                    public void run() {
                        mAfCorePalmchat.AfRecentMsgSetUnread(afMessageInfo.getKey(), 0);
                        afMessageInfo.unReadNum = 0;
                        int status = MessagesUtils.insertMsg(afMessageInfo, false, true);
                        PalmchatLogUtils.println("handleMessage  status  " + status);
                    }
                }).start();
            }
            if (msgType == AfMessageInfo.MESSAGE_CARD) {
                getProfileInfo(afMessageInfo);
            } else {
                //插入到消息列表
                CommonUtils.getRealList(mAfMessageInfos, afMessageInfo);
                //震动
                showVibrate();
                setAdapter(null);
            }
            toSendRead = true;
            handleMessageForReadFromServer(new AfMessageInfo(afMessageInfo.fromAfId));
        } else {
            //不是当前页面的朋友发过来的消息，并在聊天界面显示提醒信息
            //private chat or group chat or palmchat team
            if (MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isGroupChatMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type)) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    mTv_OtherMessageToast.setBackgroundDrawable(null);
                }else{
                    mTv_OtherMessageToast.setBackground(null);
                }
                switch (msgType) {
                    case AfMessageInfo.MESSAGE_TEXT:
                    case AfMessageInfo.MESSAGE_EMOTIONS:
                    case AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT:
                    case AfMessageInfo.MESSAGE_PA_URL:
                        String msg = afMessageInfo.msg;
                        final CharSequence content = EmojiParser.getInstance(context).parse(msg);
                        mTv_OtherMessageToast.setText(content);
                        break;

                    case AfMessageInfo.MESSAGE_FLOWER:
                        String showMsg = getString(R.string.sent_flower_msg).replace("XXXX", afMessageInfo.msg);
                        mTv_OtherMessageToast.setText(showMsg);
                        break;

                    case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
                        mTv_OtherMessageToast.setText(getString(R.string.msg_custom_emoticons));
                        break;
                    case AfMessageInfo.MESSAGE_VOICE:
                        mTv_OtherMessageToast.setText(getString(R.string.msg_voice));
                        break;
                    case AfMessageInfo.MESSAGE_IMAGE:
                        mTv_OtherMessageToast.setText("");
                        mTv_OtherMessageToast.setBackgroundResource(R.drawable.pop_default);
                        break;
                    case AfMessageInfo.MESSAGE_CARD:
                        mTv_OtherMessageToast.setText(R.string.name_card);
                        break;
                    case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
                        AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) afMessageInfo.attach;
                        if (afAttachPAMsgInfo != null) {
                            mTv_OtherMessageToast.setText(afAttachPAMsgInfo.content);
                        }
                        break;
                    case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                        mTv_OtherMessageToast.setText(R.string.msg_share_broadcast);
                        break;
                    case AfMessageInfo.MESSAGE_FRIEND_REQ:
                    case AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS:
                        mTv_OtherMessageToast.setText(CommonUtils.getFriendRequestContent(PalmchatApp.getApplication().getApplicationContext(), afMessageInfo));
                }
                final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.fromAfId);
                PalmchatLogUtils.println("handleMessage  afFriendInfo  " + afFriendInfo);
                if (afFriendInfo != null) {
                    String displayName = "";
                    if (afFriendInfo.afId != null) {
                        displayName = TextUtils.isEmpty(afFriendInfo.name) ? afFriendInfo.afId.replace("a", "") : afFriendInfo.name;
                    }
                    mTv_OtherMessageName.setText(displayName);
                    mTv_OtherMessageColon.setVisibility(View.VISIBLE);
                    StartTimer.timerHandler.post(StartTimer.startTimer(afMessageInfo, viewFrameToast));
                    viewFrameToast.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (MessagesUtils.isPrivateMessage(afMessageInfo.type)) {
                                toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
                            } else if (MessagesUtils.isGroupChatMessage(afMessageInfo.type)) {
                                toGroupChatting(afMessageInfo.toAfId);
                            } else if (MessagesUtils.isSystemMessage(afMessageInfo.type)) {
                                toAccountsChat(afFriendInfo);
                            }
                        }
                    });
                }
            } else {
                PalmchatLogUtils.println("Chatroom message");
            }
        }
    }

    @Override
    public void handleMessageForReadFromServer(AfMessageInfo afReadInfo) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("handleMessageForReadFromServer  afReadInfo  " + afReadInfo);
        if (mAccountsAfid.equals(afReadInfo.fromAfId)) {
            for (AfMessageInfo afMessageInfo : mAfMessageInfos) {
                int status = afMessageInfo.status;
                if (!MessagesUtils.isReceivedMessage(status) && status == AfMessageInfo.MESSAGE_SENT) {
                    afMessageInfo.status = AfMessageInfo.MESSAGE_SENT_AND_READ;
                }
            }
            setAdapter(null);
        }
    }

    @Override
    public void onColseSoftKeyBoard() {
        // TODO Auto-generated method stub
        CommonUtils.closeSoftKeyBoard(mCETxt_Input);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("Chatting  onEditorAction  actionId  " + actionId);
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                sendTextOrEmotion();
                break;
            case EditorInfo.IME_NULL:
                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
                    EditText edit = (EditText) v;
                    String str = edit.getText().toString() + "\n";
                    v.setText(str);

                    if (str.length() >= ReplaceConstant.MAX_SIZE) {
                        edit.setSelection(ReplaceConstant.MAX_SIZE);//将光标移至文字末�?
                    } else {
                        edit.setSelection(str.length());//将光标移至文字末�?
                    }

                }
                break;
        }

        return true;
    }

    @Override
    protected void onDestroy() {

        PalmchatApp.getApplication().removeMessageReceiver(this);
        PalmchatApp.getApplication().setMessageReadReceiver(null);
         PalmchatApp.getApplication().setColseSoftKeyBoardListe(null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        PalmchatLogUtils.i("hj", "Chatting Ondestroy");
    }

    /**
     * 点击未读消息
     */
    private void doViewUnread() {
        hideUnreadTips();
        mAccountsChattingAdapter.notifyDataSetChanged();
        mXListView.setSelection(mAccountsChattingAdapter.getCount());
        mIsBottom = true;
    }

    /**
     * 点击输入框操作
     */
    private void doEditClick() {
        mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mXListView.setSelection(mAccountsChattingAdapter.getCount());
    }


    private void doReqMsgSend(Object user_data, Object result, int flag, int code) {
        int msg_id;
        if (user_data instanceof Integer) {
            msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
            if (msg_id > DefaultValueConstant.LENGTH_0) {
                String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;

                sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT_AND_READ, fid);

            } else if (msg_id == Consts.REQ_CODE_READ) {
                PalmchatLogUtils.println("case Consts.REQ_CODE_READ code " + code + "  flag  " + flag);
            } else {
                //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
            }
        }
    }

    /**
     * 处理返回
     */
    private void doBack() {

        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }

        finish();
        PalmchatApp.getApplication().mMemoryCache.evictAll();
        Stack<Activity> activityStack = MyActivityManager.getActivityStack();
        int acSize = activityStack.size();
        if (acSize > 0) {
            for (int i = acSize - 1; i >= 0; i--) {
                Activity act = activityStack.get(i);
                if (!(act instanceof MainTab || act instanceof LaunchActivity || act instanceof ProfileActivity
                        || act instanceof MyProfileActivity || act instanceof ChattingRoomListActivity || act instanceof PublicAccountListActivity
                        || act instanceof PublicAccountDetailsActivity || act instanceof LocalSearchActivity
                        || act instanceof PalmcallActivity)) {
                    act.finish();
                }
            }
        }
    }

    /**
     * 获取输入框内容
     *
     * @return String
     */
    private String getEditTextContent() {
        return mCETxt_Input.getText().toString();
    }

    /**
     * 设置输入框默认内容
     *
     * @param content
     */
    private void setEditTextContent(String content) {
        if (null != content) {
            mCETxt_Input.setText(content);
        }
    }

    /**
     * 获取朋友profile
     *
     * @param afMessageInfo
     */
    public void getProfileInfo(AfMessageInfo afMessageInfo) {
        mAfCorePalmchat.AfHttpGetInfo(new String[]{afMessageInfo.msg}, Consts.REQ_GET_INFO, null, afMessageInfo, this);
    }

    /**
     * 震动
     */
    private void showVibrate() {
        // TODO Auto-generated method stub
        if (PalmchatApp.getApplication().getSettingMode().isVibratio()) {
            if ((System.currentTimeMillis() - lastSoundTime) > 4000 || lastSoundTime == 0) {
                TipHelper.vibrate(context, 200L);
            }
            lastSoundTime = System.currentTimeMillis();
            PalmchatLogUtils.println("handleMessageFromServer  end  lastSoundTime  " + lastSoundTime);
        }
    }

    /**
     * 设置消息未读数
     */
    private void setUnReadCount() {
        if (!mIsBottom) {
            mUnreadTipsCount++;
            mV_Unread.setVisibility(View.VISIBLE);

            String _tipsCount = null;
            if (mUnreadTipsCount > 99) {
                _tipsCount = "99+";
            } else {
                _tipsCount = String.valueOf(mUnreadTipsCount);
            }
            mTV_Unread.setText(_tipsCount);
        }
    }

    /**
     * 发送文本或者表情
     */
    private void sendTextOrEmotion() {
        String content = getEditTextContent();
        if (content.length() > 0) {
            //是表情
            if (EmojiParser.getInstance(PalmchatApp.getApplication()).hasEmotions(content)) {
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);
            }
            PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_SENDMSG);
            setEditTextContent("");
            sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_INSERT, null, content);
        }
    }

    /**
     * 发送文本或语音消息并更新本地数据库和缓存信息
     *
     * @param msgType         消息类型
     * @param fileName        文件名
     * @param fileSize        文件大小
     * @param length          文件长度
     * @param handler         handler
     * @param handler_MsgType handler类型
     * @param action          action （插入或重发）
     * @param afMessageInfo   消息对象
     * @param content         消息内容
     */
    private void sendMessageInfoForText(final int msgType, final String fileName, final int fileSize,
                                        final int length, final Handler handler, final int handler_MsgType, final int action,
                                        final AfMessageInfo afMessageInfo, final String content) {

        final AfMessageInfo messageInfo = new AfMessageInfo();//新增
        new Thread(new Runnable() {

            public void run() {
                switch (msgType) {
                    //文本消息
                    case AfMessageInfo.MESSAGE_TEXT:
                        if (action == MessagesUtils.ACTION_INSERT) {//新消息并插入本地数据库
                            messageInfo.client_time = System.currentTimeMillis();
                            messageInfo.toAfId = mAccountsAfid;
                            messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_TEXT;
                            messageInfo.msg = content;
                            messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                            messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);

                        } else if (action == MessagesUtils.ACTION_UPDATE) {//重发此消息并更新本地数据库
                            afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                            PalmchatLogUtils.println("---msg id:" + afMessageInfo._id);
                            mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
                            PalmchatLogUtils.println("---msg id: AfDbMsgUpdate " + afMessageInfo._id);
                        }
                        break;
                    default:
                        break;
                }

                PalmchatLogUtils.println("Chatting  sendMessageInfoForText  afMessageInfo  " + afMessageInfo);
                if (action == MessagesUtils.ACTION_INSERT) {
                    messageInfo.action = action;
                    handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
                    //向缓存中插入一条数据
                    MessagesUtils.insertMsg(messageInfo, true, true);
                } else if (action == MessagesUtils.ACTION_UPDATE) {
                    afMessageInfo.action = action;
                    handler.obtainMessage(handler_MsgType, afMessageInfo).sendToTarget();
                    //更新当前消息的状态
                    MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);

                }
            }

        }).start();
    }

    /**
     * 设置适配器
     *
     * @param afMessageInfo 消息对象
     */
    private void setAdapter(AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        if (null == mAccountsChattingAdapter) {
            mAccountsChattingAdapter = new AccountsChattingAdapter(this, mAfMessageInfos, mXListView, mOfficialAccountAfid,mIsViewHistory);
            mXListView.setAdapter(mAccountsChattingAdapter);
        } else {
            if (afMessageInfo != null && MessagesUtils.ACTION_UPDATE == afMessageInfo.action) {
                PalmchatLogUtils.println("setAdapter  afMessageInfo.action ACTION_UPDATE");
                mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            } else if (afMessageInfo != null) {
                PalmchatLogUtils.println("setAdapter  afMessageInfo.action " + afMessageInfo.action);
                mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }
            mAccountsChattingAdapter.notifyDataSetChanged();

            if ((mIsNoticefy && 0 < mAccountsChattingAdapter.getCount()) || mIsBottom) {//若在底部，则不显示最新未读消息
                PalmchatLogUtils.println("Chatting  setAdapter");
                mXListView.setSelection(mAccountsChattingAdapter.getCount());
                mIsNoticefy = false;
                hideUnreadTips();
            } else {
                if (mUnreadTipsCount > 0) {
                    mV_Unread.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * 隐藏未读消息提示
     */
    private void hideUnreadTips() {
        if (mV_Unread.getVisibility() == View.VISIBLE) {
            mV_Unread.setVisibility(View.GONE);
            mUnreadTipsCount = 0;
        }
    }

    /**
     * 发送消息
     *
     * @param content
     * @param msgInfo
     */
    private void send(String content, AfMessageInfo msgInfo) {

        CommonUtils.getRealList(mAfMessageInfos, msgInfo);

        setAdapter(msgInfo);

        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);
        int code = -1;
        if (msgInfo.type == (AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_STORE_EMOTIONS)) {//应用商店表情
            code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.toAfId, System.currentTimeMillis(),
                    content, Consts.MSG_CMMD_STORE_EMOTION, msgInfo._id, this);
        } else {//普通消息
            code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.toAfId, System.currentTimeMillis(),
                    content, Consts.MSG_CMMD_NORMAL, msgInfo._id, this);
        }
        sendRequestError(msgInfo, code, Consts.SEND_MSG);
    }

    /**
     * delete 指定的消息
     *
     * @param afMessageInfo
     */
    public void delete(final AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            public void run() {
                mAfCorePalmchat.AfDbMsgRmove(afMessageInfo);
                if (MessagesUtils.isLastMsg(afMessageInfo)) {
                    AfMessageInfo lastMsg = mAccountsChattingAdapter.getLastMsg();
                    if (lastMsg != null) {
                        //					 CacheManager.getInstance().getRecentMsgCacheSortList(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(lastMsg,true,true);
                        MessagesUtils.insertMsg(lastMsg, true, true);
                    } else {
                        AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mAccountsAfid, 0, 1);
                        if (recentDataArray == null || recentDataArray.length <= 0) {
                            MessagesUtils.removeMsg(afMessageInfo, true, true);
                        }
                    }

                }
            }
        }).start();

    }

    /**
     * 清除处理,
     *
     * @param msgInfo
     * @param handle
     * @param requestFlag
     */
    private void sendRequestError(AfMessageInfo msgInfo, int handle, int requestFlag) {
        if (Consts.AF_HTTP_HANDLE_INVALID == handle) {
            switch (requestFlag) {
                case Consts.SEND_MSG:
                    sendMsgFailure(Consts.REQ_MSG_SEND, Consts.REQ_CODE_UNNETWORK, msgInfo._id);
                    break;
                default:
                    break;
            }
        }
        PalmchatLogUtils.println("sendRequestError  handle==" + handle + "==msg==" + msgInfo.msg + "==requestFlag==" + requestFlag);
    }

    /**
     * send message error
     *
     * @param flag
     * @param code
     * @param user_data
     * @return
     */
    private boolean sendMsgFailure(int flag, int code, Object user_data) {
        if (user_data instanceof String) {
            PalmchatLogUtils.println("friend req else code " + code + "  flag  " + flag);
            return false;
        }
        int msg_id;
        msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
        if (msg_id > DefaultValueConstant.LENGTH_0) {
            sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
            return false;
        } else if (msg_id == Consts.REQ_CODE_READ) {
            PalmchatLogUtils.println("case Consts.REQ_CODE_READ sendMsgFailure  code " + code + "  flag  " + flag);
            return true;
        } else {
            //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
            ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
            PalmchatLogUtils.println("else Consts.REQ_MSG_SEND else code " + code + "  flag  " + flag);
            return false;
        }
    }

    /**
     * 更新状态
     *
     * @param msg_id
     * @param status
     * @param fid
     */
    void sendTextOrVoice(int msg_id, int status, String fid) {
        updateStatus(msg_id, status, fid);
    }

    /**
     * forward msg
     * 消息转发
     */
    private void forwardMsg() {

        final AfMessageInfo forwardMsgInfo = CacheManager.getInstance().getForwardMsg();
        if (forwardMsgInfo != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    AfMessageInfo afMessageInfo = new AfMessageInfo();
                    final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & forwardMsgInfo.type;
                    switch (msgType) {
                        case AfMessageInfo.MESSAGE_TEXT:
                            afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_TEXT;
                            afMessageInfo.msg = forwardMsgInfo.msg;
                            break;

                        default:
                            break;

                    }
                    afMessageInfo.fid = forwardMsgInfo.fid;
                    afMessageInfo.client_time = System.currentTimeMillis();//afMessageInfo 更改??
                    afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                    afMessageInfo.toAfId = mAccountsAfid;
                    afMessageInfo.fromAfId = null;
                    afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);//向数据库插入一条数据
                    //设置此消息的状态
                    mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afMessageInfo._id, 0, afMessageInfo.fid, Consts.AFMOBI_PARAM_FID);
                    //向缓存中插入一条数据
                    MessagesUtils.insertMsg(afMessageInfo, true, true);
                    //在mainHandler中实现发送转发消息
                    mHandler.obtainMessage(MessagesUtils.MSG_FORWARD, afMessageInfo).sendToTarget();
                    CacheManager.getInstance().setForwardMsg(null);//add by wxl 
                }
            }).start();

        }

    }

    /**
     * 更新消息状态(发送成功、失败、已读)
     *
     * @param msgId
     * @param status
     * @param fid
     */
    public void updateStatus(final int msgId, final int status, final String fid) {

        int index = ByteUtils.indexOf(mAccountsChattingAdapter.getLists(), msgId);
        PalmchatLogUtils.println("index  " + index + "  msgId  " + msgId + "  status  " + status);
        if (index != -1 && index < mAccountsChattingAdapter.getCount()) {
            AfMessageInfo afMessageInfo = mAccountsChattingAdapter.getItem(index);
            afMessageInfo.status = status;
            afMessageInfo.fid = fid;
            PalmchatLogUtils.println("afMessageInof.fid:" + fid + "   afMessageInfo.msgId  " + afMessageInfo._id + "  afMessageInfo.status  " + afMessageInfo.status);
            mAccountsChattingAdapter.notifyDataSetChanged();
        }
        new StatusThead(msgId, status, fid).start();
    }

    /**
     * 从本地数据库中获取最新的聊天的记录
     *
     * @return 返回消息集合
     */
    private List<AfMessageInfo> getRecentData() {
        AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mAccountsAfid, mOffset, mCount);
        if (recentDataArray == null || recentDataArray.length == 0) {
            return new ArrayList<AfMessageInfo>();
        }
        List<AfMessageInfo> recentDataList = Arrays.asList(recentDataArray);
        return recentDataList;
    }

    /**
     * 此方法把主要是把最新的消息集合添加到ListView中并显示，
     *
     * @param result
     */
    private void bindData(List<AfMessageInfo> result) {
        int size = result.size();
        int listSize = mAfMessageInfos.size();

        if (size > 0) {
            for (int i = 0; i < result.size(); i++) {
                AfMessageInfo afMessageInfo = result.get(i);
                CommonUtils.getRealList(mAfMessageInfos, afMessageInfo);
            }
            if (!mIsFirst) {
                mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                if (pop) {
                    mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    pop = false;
                }
                mAccountsChattingAdapter.notifyDataSetChanged();
                PalmchatLogUtils.println("bindData  new  adapterListView.getCount()  " + mAccountsChattingAdapter.getCount() + " new mAfMessageInfos.size()  " + mAfMessageInfos.size());
                mXListView.setSelection(mAfMessageInfos.size() - listSize);
            } else {
                mIsFirst = false;
                setAdapter(null);
            }
        } else {
            if (!mIsFirst) {
                ToastManager.getInstance().show(context, R.string.already_top);
            }
            mIsFirst = false;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (isInResume) {
            new Thread(new Runnable() {
                public void run() {
                    AfMessageInfo afMessageInfo = mAccountsChattingAdapter.getLastMsg();
                    if (afMessageInfo != null) {
                        mAfCorePalmchat.AfRecentMsgSetUnread(afMessageInfo.getKey(), 0);
                        MessagesUtils.setUnreadMsg(afMessageInfo.getKey(), 0);
                    }
                }
            }).start();
        }
        isInResume = true;
    }

    /**
     * 停止刷新、停止加载
     */
    private void stopRefresh() {
        // TODO Auto-generated method stub
        isRefreshing = false;
        if(mIsViewHistory && mIsFirst){
            mXListView.setPullRefreshEnable(false);
        }
        mXListView.stopRefresh();
        mXListView.stopLoadMore();
        mXListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
    }

    /**
     * 重发文本或表情
     *
     * @param afMessageInfo
     */
    public void resendTextOrEmotion(AfMessageInfo afMessageInfo) {
        sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_UPDATE, afMessageInfo, afMessageInfo.msg);
    }

    /**
     * 进入聊天页面
     *
     * @param infos
     * @param afid
     * @param name
     */
    private void toChatting(AfFriendInfo infos, String afid, String name) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("toChatting");
        Intent intent = new Intent(context, Chatting.class);
        intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
        intent.putExtra(JsonConstant.KEY_FRIEND, infos);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        context.startActivity(intent);
        finish();
    }

    /**
     * 跳转群聊
     *
     * @param groupId
     */
    void toGroupChatting(String groupId) {
        AfGrpProfileInfo groupListItem = null;
        groupListItem = CacheManager.getInstance().searchGrpProfileInfo(groupId);
        if (groupListItem != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
            if (groupListItem != null && null != groupListItem.afid && !"".equals(groupListItem.afid)) {
                bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID, groupListItem.afid);
            }
            if (groupListItem != null && null != groupListItem.name && !"".equals(groupListItem.name)) {
                bundle.putString(GroupChatActivity.BundleKeys.ROOM_NAME, groupListItem.name);
            }
            bundle.putBoolean(JsonConstant.KEY_FLAG, true);
            HelpManager.getInstance(context).jumpToPage(GroupChatActivity.class, bundle, false, 0, false);
        }
        finish();
    }

    /**
     * 其他公共账号来消息
     */
    private void toAccountsChat(AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(context, AccountsChattingActivity.class);
        intent.putExtra(JsonConstant.VIEW_HISTORY, false);
        intent.putExtra(JsonConstant.KEY_FROM_UUID, afFriendInfo.afId);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, afFriendInfo.name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, afFriendInfo.alias);
        intent.putExtra(JsonConstant.KEY_FRIEND, afFriendInfo);
        startActivity(intent);
        finish();
    }

    /**
     * 更新消息发送状态 线程类
     */
    public class StatusThead extends Thread {
        private int msgId;
        private int status;
        private String fid;

        public StatusThead(int msgId, int status, String fid) {
            this.msgId = msgId;
            this.status = status;
            this.fid = fid;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            int _id = DefaultValueConstant.LENGTH_0;

            if (fid != DefaultValueConstant.MSG_INVALID_FID) {
                _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

            } else {
                _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
            }

            //update recent chats msg status 
            AfMessageInfo msg = mAfCorePalmchat.AfDbMsgGet(msgId);

            if (msg == null) {

                PalmchatLogUtils.println("Chatting AfDbMsgGet msg:" + msg);
                return;
            }

            PalmchatLogUtils.println("---www : StatusThead isLast msg " + msgId + " msg status: " + status);
            MessagesUtils.setRecentMsgStatus(msg, status);

            mHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();

            PalmchatLogUtils.println("update status msg_id " + _id);
        }
    }

    /**
     * 异步加载缓存到数据库中的聊天信息
     */
    /*private class GetDataTask extends AsyncTask<Void, Void, List<AfMessageInfo>> {

        //是否为初始化数据，true为初始化数据，false为下拉刷新数据
        boolean isInit;

        public GetDataTask(boolean isInit) {
            this.isInit = isInit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AfMessageInfo> doInBackground(Void... params) {
            // Simulates a background job.
            List<AfMessageInfo> recentData = getRecentData();
            try {
                if (isInit) {
                    if (recentData.size() >= 17) {//为了进入chatting流畅，此处根据情况使此线程停留一段时间
                        Thread.sleep(140);
                    } else {
                        Thread.sleep(60);
                    }
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return recentData;
        }

        @Override
        protected void onPostExecute(List<AfMessageInfo> result) {
//            mListItems.addFirst("Added after refresh...");
            if (isInit) { //初始进入此页面时初始化数据
                mAfMessageInfos.clear();
                //消息转发       
            } else {
                stopRefresh();
            }
            bindData(result);
            if (isInit) {
                getData();
            }
            super.onPostExecute(result);
        }
    }*/

    /**
     * 滑动监听类
     */
    public class ImageOnScrollListener implements OnScrollListener {

        @SuppressWarnings("rawtypes")
        private final Class[] INT_CLASS = {int.class};



        private ListView listView;

        public ImageOnScrollListener(ListView listView ) {
            super();
            this.listView = listView;

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

          /*  if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {

                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
//	             judge if filiing is finish
                if (scrollState == SCROLL_STATE_FLING) {
                    new Thread(getWatcherRunnable()).start();
                }
            } else {
                addOn.setSlipState(false);
                mAccountsChattingAdapter.notifyDataSetChanged();
            }*/

            if (OnScrollListener.SCROLL_STATE_FLING == scrollState || OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState || OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                mXListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                CommonUtils.closeSoftKeyBoard(mCETxt_Input);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                PalmchatLogUtils.println("Chatting  onScroll  Top  true");
            }
            PalmchatLogUtils.println("Chatting  visibleItemCount  " + visibleItemCount
                    + "firstVisibleItem  " + firstVisibleItem
                    + "totalItemCount  " + totalItemCount
                    + "  vListView.getLastVisiblePosition()  " + mXListView.getLastVisiblePosition()
            );

            if (visibleItemCount + firstVisibleItem >= totalItemCount - 1) {
                PalmchatLogUtils.println("Chatting  onScroll  Bottom  true");
                hideUnreadTips();
                mIsBottom = true;
            } else {
                PalmchatLogUtils.println("Chatting  onScroll  Bottom  false");
                mIsBottom = false;
            }
            _end_index = firstVisibleItem + visibleItemCount;
            if (_end_index >= totalItemCount) {
                _end_index = totalItemCount - 1;
            }
        }

      /*  private Runnable getWatcherRunnable() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int currFirstPos = listView.getFirstVisiblePosition();
                    int oldFirstPos;
                    do {
                        oldFirstPos = currFirstPos;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            continue; // sleep is interrupted, just try again.
                        }
                        currFirstPos = listView.getFirstVisiblePosition();
                    } while (currFirstPos != oldFirstPos);

                    // Set scroll state to IDLE
                    Handler handler = listView.getHandler();
                    if (handler != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // LogUtil.i(TAG, "Set scroll state to IDLE");
                                    Method method = AbsListView.class.getDeclaredMethod("reportScrollStateChange", INT_CLASS);
                                    method.setAccessible(true);
                                    method.invoke(listView, SCROLL_STATE_IDLE);
                                } catch (Exception e) {
                                    PalmchatLogUtils.e(TAG, "Failed to change scroll state" + e.toString());
                                }
                            }
                        });
                    }
                }
            };
            return runnable;
        }*/

    }

    public void showRefresh() {
        int px = AppUtils.dpToPx(this, 60);
        mXListView.performRefresh(px);
    }
    /**
     * 判断是否显示提示控件
     * @param bool
     */
    private void showNoData(boolean bool){
        if(mIsViewHistory) {
            if (bool) {//判断是否有数据显示提示语
                mLinFollowingNoDataArea.setVisibility(View.VISIBLE);
            } else {
                mLinFollowingNoDataArea.setVisibility(View.GONE);
            }
        }
    }


    private LooperThread looperThread;
    /**
     * 异步读取数据库
     * @param isInit
     */
    private void getMsgData(boolean isInit){
        if (looperThread != null && looperThread.handler != null) {
            looperThread.handler.obtainMessage(LooperThread.GETMSG_FROM_DB, isInit?LooperThread.LOADTYPE_NEW:LooperThread.LOADTYPE_LOADMORE).sendToTarget();
        }
    }

    /**
     * 用于异步读历史记录
     */
    class LooperThread extends Thread {

        // 更新缓存数据
        private static final int GETMSG_FROM_DB = 9992;//从数据库读取聊天记录
        private static final int INIT_FINISH=9993;//初始化结束 可以加载聊天记录了
        private static final int LOADTYPE_NEW=1,LOADTYPE_LOADMORE=0;//是读取最新的聊天记录 还是loadMore
        Handler handler;
        Looper looper;
        private boolean isInit;
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            // 保持当前只有一条线程在执行查看数据操作
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if (!isInit) {
                        isInit = true;
                    }
                    switch (msg.what) {
                        case GETMSG_FROM_DB://从数据库获取消息记录
                            int loadType=(Integer)msg.obj;
                            List<AfMessageInfo> recentData =getRecentData();
                            mHandler.obtainMessage(GETMSG_FROM_DB,loadType,0,recentData).sendToTarget();
                            break;
                    }

                }
            };
            if (!isInit) {
                mHandler.sendEmptyMessage(INIT_FINISH);
            }
            Looper.loop();

        }
    }
}