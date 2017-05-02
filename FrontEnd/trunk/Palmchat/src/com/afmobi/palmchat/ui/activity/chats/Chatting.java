package com.afmobi.palmchat.ui.activity.chats;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipException;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseChattingActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.PalmchatApp.MessageReadReceiver;
import com.afmobi.palmchat.PalmchatApp.MessageReceiver;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.CloseChatingEvent;
import com.afmobi.palmchat.eventbusmodel.EventRefreshFriendList;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.ui.activity.friends.LocalSearchActivity;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.customview.RippleBackground;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.broadcasts.ScreenObserver;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.eventbusmodel.ChatImageDownloadedEvent;
import com.afmobi.palmchat.eventbusmodel.EmoticonDownloadEvent;
import com.afmobi.palmchat.eventbusmodel.SendImageEvent;
import com.afmobi.palmchat.eventbusmodel.ShareBroadcastRefreshChatting;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.LaunchActivity;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.adapter.ChattingAdapter;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomListActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.friends.FriendsActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity.StatusThead;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.AppDialog.OnInputDialogListener;
import com.afmobi.palmchat.ui.customview.ChattingMenuDialog;
import com.afmobi.palmchat.ui.customview.CutstomEditText;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.FaceFooterView;
import com.afmobi.palmchat.ui.customview.FlowerAniView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.ui.customview.RippleView;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.StartTimer;
import com.afmobi.palmchat.util.TipHelper;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.ZipFileUtils;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfAttachImageInfo;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfDatingInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfImageReqInfo;
import com.core.AfMessageInfo;
import com.core.AfOnlineStatusInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.greenrobot.event.EventBus;


public class Chatting extends BaseChattingActivity implements OnClickListener,
        AfHttpResultListener, AfHttpProgressListener, OnEditorActionListener,
        MessageReceiver, IXListViewListener, OnItemClick, OnItemLongClick, MessageReadReceiver, PalmchatApp.ColseSoftKeyBoardLister {

    /**
     * 分享图片
     */
    public static final int SEND_SHARE_IMAGE = 8937284;
    private static final String TAG = Chatting.class.getCanonicalName();
    public final static String FRIEND = "FRIEND";
    public final static String RECOMMEND = "RECOMMEND";
    public final static String TT = "TT";
    /**
     * chatting适配器
     */
    private ChattingAdapter adapterListView;
    private XListView vListView;
    private ArrayList<AfMessageInfo> listChats = new ArrayList<AfMessageInfo>();
    private ArrayList<AfMessageInfo> listVoiceChats = new ArrayList<AfMessageInfo>();

    /**
     * 未读消息提示数
     */
    private int unreadTipsCount;

    /**
     * 输入框
     */
    private CutstomEditText vEditTextContent;
    private ImageView vButtonBackKeyboard, vButtonKeyboard, vButtonLeftKeyboard;

    private ImageView vButtonSend;

    private String mFriendName, mAlias;
    /**
     * 标识是否转发
     */
    private boolean isForward;
    /**
     * 标识公共帐号查看历史消息
     */
    private boolean isViewHistory;
    private ImageView vImageEmotion, vImageViewBack, vImageViewMore;
    private View vImageViewVoice;
    private View viewUnTalk, viewFrameToast;
    private View viewOptionsLayout, viewChattingVoiceLayout;
    private Button vButtonTalk;
    private ImageView vImageViewRight, mImageViewAddGroup;
    /**
     * 是否发送
     */
    private boolean sent;
    private TextView vTextViewOtherMessageToast, vTextViewOtherMessageColon, vTextViewOtherMessageName, mTextViewOnline, mTextViewTitle;
    private MediaRecorder mRecorder;
    /**
     * 录音开始时间
     */
    private long recordStart;
    /**
     * 录音结束时间
     */
    private long recordEnd;
    /**
     * 语音时长
     */
    private int recordTime;
    private TextView vTextViewRecordTime;
    private TextView mCancelVoice;

    private LinearLayout chattingOptions;
    private LinearLayout img_picture, img_camera, img_voice, img_name_card, img_video, img_background;
    private ImageView img_picture_p, img_camera_c, img_voice_v, img_name_card_n, img_video_v, img_background_v;
    private String cameraFilename;
    private File imageFile;
    private LinearLayout chatting_emoji_layout;

    /**
     * 滚动条是否滑到底部
     */
    private boolean isBottom = false;
    /**
     * 声音文件名称
     */
    private String mVoiceName;
    /**
     * 转发图片路径
     */
    private String forward_imagePath;
    private Intent intent;
    byte[] data;
    ChattingMenuDialog widget;
    /**
     * 好友类
     */
    AfFriendInfo afFriend;

    /**
     * 获取数据偏移量
     */
    private int mOffset = 0;

    /**
     * 每次取得数据条数
     */
    private int mCount = 20;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
    private boolean mIsFirst = true, mIsNoticefy = true;
    /**
     * 是否正在刷新
     */
    private boolean isRefreshing = false;

    private boolean isInResume = false;

//    private ListViewAddOn listViewAddOn = new ListViewAddOn();
    private static final int RECORDING_TIMER = 9000;
    private static final int ONLINE_STATUS = 90001;
    public static final int ACTION_LONG_CLICK = 5;
//    public static final int FINISH = 8000;

    private static final int BACK_FROM_SET_CHAT_BG = 223333;

    private boolean isPublicMember = false;

    private KeyboardListenRelativeLayout chat_root;

    private final static int AT_LEAST_LENGTH = 60;

    RippleView mRippleView;
    /**水波纹*/
    private RippleBackground mRippleViewEffect;
    private ImageView mVoiceImageEmotion, mVoiceImageViewMore;
    private ProgressBar mVoiceProgressBar, mVoiceProgressBar2;

    private final static int STORE_FACE_DOWNLOAD =8001;
    private final int ALBUM_REQUEST = 3; //add by zhh 发送请求到相册页面
    /**
     * 是否正在录音
     */
    private boolean mIsRecording;

	/**好友是否在线*/
	private boolean isOnline;
	/**是否从ProfileActivity跳转过来.当然如果要判断多个界面的话应该加多类型判断*/
	private boolean mIsFromProfile;

    private ScreenObserver mScreenObserver;

    private LooperThread looperThread;

    boolean isFollow;
    //广播
    private final String Broadcast = "com.palmcall.hideradiobroadcas";

    final private Handler mainHandler = new Handler() {
        public void handleMessage(Message msg) {
            vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            switch (msg.what) {
            case LooperThread.INIT_FINISH://初始化结束后去读取最近的消息
            		getMsgData(true);
				break; 
            	case LooperThread.GETMSG_FROM_DB:
            		List<AfMessageInfo> recentData =(List<AfMessageInfo>) msg.obj;
            		boolean isInit=msg.arg1==LooperThread.LOADTYPE_NEW?true:false;
            		if (isInit) { //初始进入此页面时初始化数据
                        listChats.clear();
                        if (isForward) {
                            forwardMsg();
                        }
                        for (AfMessageInfo afMessageInfo : recentData) {
                            int status = afMessageInfo.status;
                            if (MessagesUtils.isReceivedMessage(status)) {
                                toSendRead = true;
                                break;
                            }
                        }
                        if (toSendRead) {
                            //发送已读消息标识到服务商
                            sendAlreadyRead();
                        }
                    } else {
                        stopRefresh();
                    }
                    bindData(recentData);
                    if (isInit) {
                        getData();
                    } 
            		break;
                case 0://更新动态表情下载
                    int size = CacheManager.getInstance().getGifImageUtilInstance().getListFolders().size();
                    PalmchatLogUtils.println("case 0: size:" + size);
                    Intent it = (Intent) msg.obj;
                    if (it != null) {
                        String item_id = it.getStringExtra("itemId");
                        PalmchatLogUtils.println("case 0: item_id:" + item_id);
                        if (CacheManager.getInstance().getItemid_update().containsKey(item_id)) {
                            CacheManager.getInstance().getItemid_update().remove(item_id);
                        }
                        if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(item_id)) {
                            CacheManager.getInstance().getStoreDownloadingMap().remove(item_id);
                        }
                        //发布广播
                        EventBus.getDefault().post(new EmoticonDownloadEvent(item_id, 100, true, false, JsonConstant.BROADCAST_STORE_FRAGMENT_MARK, 0));
                    }
                    faceFooterView.showDownlaodStoreFace();
                    faceFooterView.showNewSymbolOrNot();
                    break;
                case SEND_SHARE_IMAGE://从图库内分享图片
                    if (isShareImage) {
                        dismissAllDialog();
                        setResult(ForwardSelectActivity.SHARE_IMAGE);
                        finish();
                    } else {
                        setResult(ForwardSelectActivity.SHARE_IMAGE);
                    }
                    break;
                /*case FINISH:
                    finish();
                    break;*/
//				正在录音时，刷新录音时间。
                case RECORDING_TIMER:
                    long recordCurTime = System.currentTimeMillis();

                    int voiceProgress = (int) ((recordCurTime - recordStart) / 1000);
                    if (voiceProgress <= RequestConstant.RECORD_VOICE_TIME) {//录音时间小于指定的时间时，则显示录音进度
                        mVoiceProgressBar.setProgress(voiceProgress);
                        mVoiceProgressBar2.setProgress(voiceProgress);
                    }

                    vTextViewRecordTime.setText(CommonUtils.diffTime(recordCurTime, recordStart));//设置录音时长
                    if (recordStart > 0 && recordCurTime - recordStart >= RequestConstant.RECORD_VOICE_TIME * 1000 && !sent) {
                        stopRecording();
                        stopRecordingTimeTask();
                        if (CommonUtils.getSdcardSize()) {
                            ToastManager.getInstance().show(context, R.string.memory_is_full);
                            break;
                        }
                        PalmchatLogUtils.e(TAG, "----handleMessage----MessagesUtils.MSG_VOICE");

                        sent = true;
                        mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                        mCancelVoice.setVisibility(View.GONE);
                        vButtonTalk.setSelected(false);
                    }
                    break;

//				发送语音
                case MessagesUtils.MSG_VOICE: {
                    sendVoice(mVoiceName);
                    break;
                }
                case MessagesUtils.SEND_VOICE://发送语音
                {
                    AfMessageInfo msgInfoVoice = (AfMessageInfo) msg.obj;
                    //把语音添加到消息集合中
                    CommonUtils.getRealList(listChats, msgInfoVoice);
                    //更新适配器
                    setAdapter(msgInfoVoice);
                    AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) msgInfoVoice.attach;
                    final int voiceLength = afAttachVoiceInfo.voice_len;
                    PalmchatLogUtils.println("Chatting  voiceLength  " + voiceLength);

                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_VOICE);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_VOICE);
//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.P_NUM);

                    int code = mAfCorePalmchat.AfHttpSendVoice(mFriendAfid, System.currentTimeMillis(), data, data.length, voiceLength, msgInfoVoice._id, Chatting.this, Chatting.this);
                    sendRequestError(msgInfoVoice, code, Consts.SEND_VOICE);
                    break;
                }

//			image
                case MessagesUtils.MSG_IMAGE: {
                    String largeFilename = (String) msg.obj;//原大图全路径
                    if (null != looperThread.handler) {
                        looperThread.handler.obtainMessage(LooperThread.SEND_IMAGE_LOOPER, largeFilename).sendToTarget();
                    }
                    break;
                }

//			camera 
                case MessagesUtils.MSG_PICTURE://处理通过相机获取到的图片
                {

                    String largeFilename = (String) msg.obj;//after compress big picture totally path
                    File largeFile = new File(largeFilename);
                    int largeFileSize = 0;
                    if (largeFile != null) {
                        largeFileSize = (int) (largeFile.length() / 1024);
                    }

                    //File f = new File(largeFilename);
                    File f2 = FileUtils.copyToImg(largeFile.getAbsolutePath());
                    if (f2 != null) {
                        largeFile = f2;
                    }
                    String smallFileName = smallImage(largeFile, MessagesUtils.PICTURE);

                    File smaleFile = new File(RequestConstant.IMAGE_CACHE + smallFileName);
                    int smallFileSize = 0;
                    if (smaleFile != null) {
                        smallFileSize = (int) (smaleFile.length() / 1024);
                    }

                    initMedia(largeFilename, largeFileSize, smallFileName, smallFileSize);

                    break;
                }

                case MessagesUtils.EDIT_TEXT_CHANGE: {
                    if (!CommonUtils.isEmpty(mFriendAfid) && mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
                        return;
                    }
                    int len = (Integer) msg.obj;
                    if (len > DefaultValueConstant.LENGTH_0) {
                        vButtonSend.setVisibility(View.VISIBLE);
                        vImageViewVoice.setVisibility(View.GONE);
                    } else {
                        vButtonSend.setVisibility(View.GONE);
                        vImageViewVoice.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case MessagesUtils.MSG_CARD://发送朋友名片
                {
                    AfFriendInfo afFriendInfo = (AfFriendInfo) msg.obj;
                    sendCard(afFriendInfo);
                    break;
                }

                case MessagesUtils.MSG_TEXT://发送文本信息
                {
                    AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
                    send(msgInfoText.msg, msgInfoText);
//				vEditTextContent.setText("");
                    PalmchatLogUtils.println("msgInfoText._id  " + msgInfoText._id);
                    break;
                }

                case MessagesUtils.MSG_GIF://发送GIf
                {
                    AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
                    send(msgInfoText.msg, msgInfoText);
//				vEditTextContent.setText("");
                    PalmchatLogUtils.println("MSG_GIF msgInfoText._id  " + msgInfoText._id);
                    break;
                }

                case MessagesUtils.MSG_EMOTION://发送表情
                {
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;

//				listChats.add(afMessageInfo);
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    setAdapter(afMessageInfo);

                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_EMOTION);
//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_TEXT);
//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.P_NUM);

                    int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.toAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_EMOTION, afMessageInfo._id, Chatting.this);
                    sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
                    break;
                }

//			转发消息
                case MessagesUtils.MSG_FORWARD://转发消息
                {
                    AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
                    forwardRequest(msgInfoText);
                    PalmchatLogUtils.println("MessagesUtils.MSG_FORWARD  " + msgInfoText.fid);
                    break;
                }

                case MessagesUtils.MSG_TOO_SHORT://显示录音太短
                {
                    ToastManager.getInstance().show(context, R.string.message_too_short);
                    break;
                }

                case MessagesUtils.MSG_RESEND_CARD: {
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
//				listChats.add(afMessageInfo);
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    setAdapter(afMessageInfo);
                    PalmchatLogUtils.println("afMessageInfo._id  " + afMessageInfo._id);

                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_CARD);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_CARD);
//                    MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.P_NUM);

                    int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.toAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_RECOMMEND_CARD, afMessageInfo._id, Chatting.this);
                    sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
                    break;
                }

                case MessagesUtils.SEND_IMAGE://发送图片
                {
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;

                    PalmchatLogUtils.println("mFriendAfid:" + mFriendAfid + "   afmsg.toAfid:" + mFriendAfid + "   " + Chatting.this);

                    if (mFriendAfid.equals(afMessageInfo.toAfId)) {
                        AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                        AfImageReqInfo param = afAttachImageInfo.upload_info;
                        //				listChats.add(afMessageInfo);
                        CommonUtils.getRealList(listChats, afMessageInfo);//添加到消息集合中
                        setAdapter(afMessageInfo);

                        // heguiming 2013-12-04
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_PIC);
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//                        MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_PIC);
//                        MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.P_NUM);

                        int code = mAfCorePalmchat.AfHttpSendImage(param, afMessageInfo, Chatting.this, Chatting.this);
                        sendRequestError(afMessageInfo, code, Consts.SEND_IMAGE);
                    }
                    break;
                }
                case MessagesUtils.FAIL_TO_LOAD_PICTURE://下载图片失败提示
                {
                    ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
                    break;
                }
                case MessagesUtils.MSG_CAMERA_NOTIFY: {//拍照之后的发送广播
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                    AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                    updateImageStautsByBroadcast(afMessageInfo._id, afMessageInfo.status, afMessageInfo.fid, afAttachImageInfo.progress);
                    break;
                }

                case MessagesUtils.MSG_SET_STATUS: {
                    PalmchatLogUtils.println("---www : MSG_SET_STATUS");
//                    sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                    break;
                }
                case Constants.DB_DELETE: {
                    ToastManager.getInstance().show(Chatting.this, getString(R.string.add_friend_req));
                    dismissProgressDialog();

                    String my_afid = CacheManager.getInstance().getMyProfile().afId;
                    SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setFriendReqTime(my_afid, mFriendAfid);
                    sentLayout();
                }
                break;
                case Constants.TO_REFRESH_FRIEND_LIST://刷新朋友列表
                {
                   /* Intent intent = new Intent();
                    intent.setAction(Constants.REFRESH_FRIEND_LIST_ACTION);
                    context.sendBroadcast(intent);*/
                    EventBus.getDefault().post(new EventRefreshFriendList());
                 /*   Intent intent2 = new Intent();
                    intent2.setAction(Constants.REFRESH_CHATS_ACTION);
                    context.sendBroadcast(intent2);*/
                    EventBus.getDefault().post(new RefreshChatsListEvent());

                    dismissProgressDialog();

                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    setAdapter(afMessageInfo);
                    final AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(mFriendAfid);
                    if (afF != null) {
//					vImageViewRight.setBackgroundResource(R.drawable.t_contact);
                        vImageViewRight.setBackgroundResource(R.drawable.t_profile);
                        String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
                                afF.name, context.getString(R.string.frame_toast_friend_req_success));
                        ToastManager.getInstance().AddFriendsShow(Chatting.this, aa);
                        mAfCorePalmchat.AfRecentMsgSetUnread(afMessageInfo.getKey(), 0);
                        MessagesUtils.setUnreadMsg(afMessageInfo.getKey(), 0);
                    }
                    mImageViewAddGroup.setVisibility(View.VISIBLE);
                    sentLayout();
                    break;
                }
//			动态表情
                case STORE_FACE_DOWNLOAD: {
                    Params params = (Params) msg.obj;
                    String myAfid = CacheManager.getInstance().getMyProfile().afId;
                    if (!TextUtils.isEmpty(myAfid)) {
                        Intent intent2 = new Intent();
                        intent2.putExtra("itemId", params.itemId);
                        intent2.putExtra("is_face_change", params.is_face_change);
//					EmojiParser.getInstance(context).readGif(context, myAfid, handler, intent);
                        if (!TextUtils.isEmpty(params.gifFolderPath)) {
                            CacheManager.getInstance().getGifImageUtilInstance().putDownLoadFolder(context, params.gifFolderPath, mainHandler, intent2);
                        }

                    }
                    break;
                }


                case MessagesUtils.MSG_ERROR_OCCURRED:
                    ToastManager.getInstance().show(context, R.string.error_occurred);
                    break;

//				送花消息
                case LooperThread.CHECK_FLOWER_MSG:
                    FlowerAnimParam param = (FlowerAnimParam) msg.obj;
                    int count = param.mCount;
                    if (param.mIsShow && count > 0) {
                        ShowSendFlowerAnim(String.valueOf(count));
                    }
                    break;
                case ONLINE_STATUS://设置好友在线状态
                    AfOnlineStatusInfo onlineStatusInfo = (AfOnlineStatusInfo) msg.obj;
					PalmchatApp.getApplication().setOnlineStatusInfoMap(mFriendAfid, onlineStatusInfo);
                    if (onlineStatusInfo.status == AfOnlineStatusInfo.AFMOBI_ONLINESTAUS_INFO_OFFLINE) {
                        String dateTime = dateFormat(onlineStatusInfo.datetime);
                        if(!TextUtils.isEmpty(dateTime)) {
                            mTextViewOnline.setText(PalmchatApp.getApplication().getString(R.string.frd_last_seen).replace("XXXX", dateTime));
                            mTextViewOnline.setVisibility(View.VISIBLE);
                        }
                        else{
                            mTextViewOnline.setText("");
                            mTextViewOnline.setVisibility(View.GONE);
                        }
                    } else if (onlineStatusInfo.status == AfOnlineStatusInfo.AFMOBI_ONLINESTAUS_INFO_NOACCOUNT) {
                        mTextViewOnline.setVisibility(View.GONE);
                    } else {
                        mTextViewOnline.setText(R.string.online);
                        mTextViewOnline.setVisibility(View.VISIBLE);
						isOnline = true;
                    }
                    break;
                case MessagesUtils.MSG_SHARE_BROADCAST_OR_TAG:
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                    setAdapter(afMessageInfo);
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 隐藏加好友和好友请求布局
     */
    private void sentLayout() {
        // TODO Auto-generated method stub
        r_stranger_add.setVisibility(View.GONE);
        r_frd_add.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mIsForworded = savedInstanceState.getBoolean("isforword");
        }
        //shareImage();
        //添加当前窗体的Handle添加到公共键值对内，以便在其它页面调用
//        PalmchatApp.getApplication().getHandlerMap().put(Chatting.class.getCanonicalName(), mainHandler);
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onColseSoftKeyBoard() {
        PalmchatLogUtils.println("Chatting Setting close SoftKeyBoard");
        CommonUtils.closeSoftKeyBoard(vEditTextContent);
    }

    /**
     * 获取新收到的所有的语音，可用于连续播放
     *
     * @return
     */
    public ArrayList<AfMessageInfo> getListVoiceChats() {
        listVoiceChats.clear();
        for (AfMessageInfo msgInfo : listChats) {
            final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & msgInfo.type;
            int status = msgInfo.status;
            if (MessagesUtils.isReceivedMessage(status) && msgType == AfMessageInfo.MESSAGE_VOICE) {
                listVoiceChats.add(msgInfo);
            }
        }
        return listVoiceChats;
    }

    class LooperThread extends Thread {

        // 更新缓存数据
        private static final int SEND_IMAGE_LOOPER = 9999;
        private static final int CHECK_FLOWER_MSG = 9991;
        private static final int GETMSG_FROM_DB = 9992;//从数据库读取聊天记录
        private static final int INIT_FINISH=9993;//初始化结束 可以加载聊天记录了
        private static final int SHARE_IMAGE = 9994;
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
                    		mainHandler.obtainMessage(GETMSG_FROM_DB,loadType,0,recentData).sendToTarget();
                    		 
                    	break;
                        case SEND_IMAGE_LOOPER:

                            String largeFilename = (String) msg.obj;//原大图全路径
                            File f = new File(largeFilename);
                            File f2 = FileUtils.copyToImg(f.getAbsolutePath());
                            if (f2 != null) {
                                f = f2;
                            }
                            String cameraFilename = f2.getAbsolutePath();//imageFile.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                            String smallFileName = smallImage(f, MessagesUtils.PICTURE);
                            cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                            cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);

                            if (CommonUtils.isEmpty(cameraFilename)) {
                                mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
                                return;
                            }

                            largeFilename = cameraFilename;

                            File largeFile = new File(largeFilename);
                            int largeFileSize = 0;
                            if (largeFile != null) {
                                largeFileSize = (int) (largeFile.length() / 1024);
                            }
                            File smaleFile = new File(RequestConstant.IMAGE_CACHE + smallFileName);
                            int smallFileSize = 0;
                            if (smaleFile != null) {
                                smallFileSize = (int) (smaleFile.length() / 1024);
                            }
                            PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_SENDIMG);
                            initMedia(largeFilename, largeFileSize, smallFileName, smallFileSize);
                            break;


                        case CHECK_FLOWER_MSG:
                            int flowerCount = 0;
                            boolean isShow = false;

//						find if have send flower msg, if have send flower msg and it's unread, show send flower anim 
                            List<AfMessageInfo> listTemp = null;

//						 mOffset = 0, mCount = 20
                            AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mFriendAfid, 0, 20);
                            if (recentDataArray == null || recentDataArray.length == 0) {
                                listTemp = new ArrayList<AfMessageInfo>();
                            } else {
                                listTemp = Arrays.asList(recentDataArray);
                            }
//						 计算  收到花的总数，判断是否显示送花动画
                            for (AfMessageInfo afMessageInfo : listTemp) {
                                if (MessagesUtils.isReceivedMessage(afMessageInfo.status)
                                        && MessagesUtils.isFlowerMsg(afMessageInfo.type)
                                        && afMessageInfo.status == AfMessageInfo.MESSAGE_UNREAD) {
                                    isShow = true;
                                    int count;
                                    try {
                                        count = Integer.parseInt(afMessageInfo.msg);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                        count = 1;
                                    }

                                    flowerCount += count;
                                    afMessageInfo.status = AfMessageInfo.MESSAGE_READ;
                                    mAfCorePalmchat.AfDbMsgSetStatus(afMessageInfo._id, afMessageInfo.status);

                                }
                            }
                            FlowerAnimParam flowerAnim = new FlowerAnimParam();
                            flowerAnim.mIsShow = isShow;
                            flowerAnim.mCount = flowerCount;
                            mainHandler.obtainMessage(CHECK_FLOWER_MSG, flowerAnim).sendToTarget();
                            break;
                        case SHARE_IMAGE:
                            shareImage();
                            break;
                    }

                }
            };
         /*   if (!isInit) {
                handler.obtainMessage(LooperThread.GETMSG_FROM_DB, LooperThread.LOADTYPE_NEW).sendToTarget();
			} */
            Looper.loop();

        }
    }

    /**
     * 送花动画类
     */
    private class FlowerAnimParam {
        //是否显示
        public boolean mIsShow;
        //花的数量
        public int mCount;
    }

    /**
     * 发送朋友名片
     *
     * @param afFriendInfo 朋友profile
     */
    void sendCard(final AfFriendInfo afFriendInfo) {
        new Thread(new Runnable() {
            public void run() {
                AfMessageInfo afMessageInfo = new AfMessageInfo();
                afMessageInfo.msg = afFriendInfo.afId;
                afMessageInfo.client_time = System.currentTimeMillis();
//				afMessageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
                afMessageInfo.toAfId = mFriendAfid;
                afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_CARD;
                afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

                //播入本地数据库一条数据
                afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);
                afMessageInfo.attach = afFriendInfo;
                PalmchatLogUtils.println("afMessageInfo._id " + afMessageInfo._id);
                mainHandler.obtainMessage(MessagesUtils.MSG_RESEND_CARD, afMessageInfo).sendToTarget();
                //向缓存中插入一条数据
                MessagesUtils.insertMsg(afMessageInfo, true, true);
//				CacheManager.getInstance()
            }
        }).start();

    }


    /**
     * 重新发送名片
     *
     * @param afMessageInfo 朋友profile
     */
    public void resendCard(final AfMessageInfo afMessageInfo) {
        new Thread(new Runnable() {
            public void run() {
//				afMessageInfo.client_time = System.currentTimeMillis();
                afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                //更新数据库
                mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
                MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
                mainHandler.obtainMessage(MessagesUtils.MSG_RESEND_CARD, afMessageInfo).sendToTarget();
            }
        }).start();
    }

    /**
     * 重新发送分享的广播
     *
     * @param afMessageInfo 朋友profile
     */
    public void resendShareBroadcast(final AfMessageInfo afMessageInfo,final boolean isShareTag) {
        new Thread(new Runnable() {
            public void run() {
                AfAttachPAMsgInfo msgInfo = (AfAttachPAMsgInfo) afMessageInfo.attach;
                if (msgInfo != null) {
                    afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                    //更新数据库
                    mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
                    MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    int code;
                    if (isShareTag) {
                        code = mAfCorePalmchat.AfHttpAfBCShareTags(afMessageInfo.toAfId, System.currentTimeMillis(), msgInfo.content, msgInfo.imgurl, afMessageInfo._id, Chatting.this);
                    } else {
                        code = mAfCorePalmchat.AfHttpAfBCShareBroadCast(afMessageInfo.toAfId, System.currentTimeMillis(), msgInfo.mid, afMessageInfo._id, Chatting.this);
                    }
                    sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
                    mainHandler.obtainMessage(MessagesUtils.MSG_SHARE_BROADCAST_OR_TAG, afMessageInfo).sendToTarget();
                }
            }
        }).start();
    }

    /**
     * 发送语音
     *
     * @param voiceName
     */
    public void sendVoice(final String voiceName) {
        mHandler2.removeCallbacksAndMessages(null);
        if (!CommonUtils.isEmpty(voiceName)) {
            long end = System.currentTimeMillis();
            final long time = Math.min((int) ((end - mTimeStart) / 1000), RequestConstant.RECORD_VOICE_TIME_BROADCAST);
            PalmchatLogUtils.e(TAG, "----sendVoice---- time" + time);

            sent = false;
            PalmchatLogUtils.println("sendVoice sent " + sent);

            new Thread(new Runnable() {
                public void run() {
                    data = FileUtils.readBytes(RequestConstant.VOICE_CACHE + voiceName);


                    if (time < 2) {
                        mainHandler.sendEmptyMessage(MessagesUtils.MSG_TOO_SHORT);
                    } else {
                        if (data == null || data.length <= AT_LEAST_LENGTH) {
                            mainHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
                            return;
                        }
                        //发送文本或语音消息并更新本地数据库和缓存信息
                        sendMessageInfoForText(AfMessageInfo.MESSAGE_VOICE, voiceName, data.length, recordTime, mainHandler, MessagesUtils.SEND_VOICE, MessagesUtils.ACTION_INSERT, null, null);
                    }
                }
            }).start();
        } else {
            ToastManager.getInstance().show(context, getString(R.string.sdcard_unmounted));
        }

    }

    private ArrayList<String> existList = new ArrayList<String>();

    private boolean isShareImage;
    private ArrayList<String> shareImageUri;
    private boolean mDirectChatting = false;

    private int _start_index;
    private int _end_index;

    private RelativeLayout r_stranger_add, r_frd_add;
    private ImageView stranger_head_icon;
    private TextView stranger_for_add, frd_accept, frd_inorge;
    private View view_unread;
    private TextView textview_unread;
    private View stranger_for_adding, frd_accepting;
    private ImageView frd_head_icon;
    private boolean back_to_default = false;
    private FlowerAniView mFlowerAniView;
    /**
     * 是否从popMessageActivity进入此页面
     */
    private boolean isFromPop;

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        PalmchatLogUtils.i("ChattingHash","findviews:" + this.hashCode()+"");
        EventBus.getDefault().register(this);
        //设置语音模式
        setListenMode();
        //初始化中件间
        setContentView(R.layout.activity_chatting);
        chat_root = (KeyboardListenRelativeLayout) findViewById(R.id.chat_root);
        intent = getIntent();
        //好友afid
        mFriendAfid = intent.getStringExtra(JsonConstant.KEY_FROM_UUID);
        //好友姓名
        mFriendName = intent.getStringExtra(JsonConstant.KEY_FROM_NAME);
        //是否为分享图片
        isShareImage = intent.getBooleanExtra(JsonConstant.KEY_SHARE_IMG, false);
        //分享图片地址
        shareImageUri = (ArrayList<String>) intent.getSerializableExtra(JsonConstant.KEY_SHARE_IMG_URI);
        //转发图片地址
        forward_imagePath = intent.getStringExtra(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT);

        mDirectChatting = intent.getBooleanExtra(JsonConstant.KeyDirectChat, false);

        isFromPop = intent.getBooleanExtra(JsonConstant.KEY_FROM_POP_MESSAGE_TO_CHATTING, false);
        if(isFromPop){
            if(!CommonUtils.isScreenLocked(PalmchatApp.getApplication().getApplicationContext())){
                PalmchatApp.getApplication().isDoubleClickScreen = false;
            }
            mScreenObserver = new ScreenObserver(this, new ScreenObserver.ScreenStateListener() {
                @Override
                public void onScreenOn() { //亮屏
                    PalmchatLogUtils.i("hj","Chattting BroadcastonScreenOn");
                    PalmchatApp.getApplication().isDoubleClickScreen = false;
                }

                @Override
                public void onScreenOff() {//灭屏
                    PalmchatLogUtils.i("hj","Chattting BroadcastonScreenOff");
                    PalmchatApp.getApplication().isDoubleClickScreen = false;
                }

                @Override
                public void onUserPresent() {//解锁成功
                    PalmchatLogUtils.i("hj","Chattting BroadcastonUserPresent");
                    PalmchatApp.getApplication().isDoubleClickScreen = false;
                }
            });
            PalmchatLogUtils.i("hj", "ChattingdoubleScreen");
        }

        back_to_default = getIntent().getBooleanExtra(JsonConstant.KEY_BACK_TO_DEFAULT, false);
        PalmchatLogUtils.println("mFriendAfid: 111" + mFriendAfid + "   " + this);

        mAlias = intent.getStringExtra(JsonConstant.KEY_FROM_ALIAS);
        mIsFromProfile = intent.getBooleanExtra(JsonConstant.KEY_FROM_PROFILE, false);
        //若别名不为空，则显示别名
        if (!TextUtils.isEmpty(mAlias)) {
            mFriendName = mAlias;
        }
        isInResume = intent.getBooleanExtra(JsonConstant.KEY_FLAG, false);
        afFriend = (AfFriendInfo) intent.getSerializableExtra(JsonConstant.KEY_FRIEND);
        vListView = (XListView) findViewById(R.id.listview);
//		vListView.setOnItemClickListener(this);
        //设置listview是否显示底部加载数据提示
        vListView.setPullLoadEnable(true);
        vListView.setXListViewListener(this);

        r_stranger_add = (RelativeLayout) findViewById(R.id.r_stranger_add);
        r_frd_add = (RelativeLayout) findViewById(R.id.r_frd_add);
        stranger_head_icon = (ImageView) findViewById(R.id.stranger_head_icon);
        stranger_for_add = (TextView) findViewById(R.id.stranger_for_add);
        frd_head_icon = (ImageView) findViewById(R.id.frd_head_icon);
        frd_accept = (TextView) findViewById(R.id.frd_accept);
        frd_inorge = (TextView) findViewById(R.id.frd_inorge);
        stranger_head_icon.setOnClickListener(this);
        stranger_for_add.setOnClickListener(this);
        frd_head_icon.setOnClickListener(this);
        frd_accept.setOnClickListener(this);
        frd_inorge.setOnClickListener(this);

        stranger_for_adding = findViewById(R.id.pb_stranger_for_add);
        frd_accepting = findViewById(R.id.r_frd_adding);

        mFlowerAniView = (FlowerAniView) findViewById(R.id.view_flower_ani);

        mCancelVoice = (TextView) findViewById(R.id.cancel_voice_note);
        vTextViewRecordTime = (TextView) findViewById(R.id.text_show);
        vEditTextContent = (CutstomEditText) findViewById(R.id.chatting_message_edit);
        vEditTextContent.setOnClickListener(this);
        vEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                PalmchatLogUtils.println("Chatting  onFocusChange  " + hasFocus);
                if (hasFocus) {
                    closeEmotions();
                }
            }
        });
        //限制输入框长度
        vEditTextContent.addTextChangedListener(new LimitTextWatcher(vEditTextContent, ReplaceConstant.MAX_SIZE, mainHandler, MessagesUtils.EDIT_TEXT_CHANGE));
        vEditTextContent.setMaxLength(ReplaceConstant.MAX_SIZE);

        vEditTextContent.setOnEditorActionListener(this);
        vTextViewOtherMessageName = (TextView) findViewById(R.id.textview_name);
        mTextViewOnline = (TextView) findViewById(R.id.title_offOnline);
        vTextViewOtherMessageColon = (TextView) findViewById(R.id.textview_colon);
        vTextViewOtherMessageToast = (TextView) findViewById(R.id.textview_message);

        mTextViewTitle = (TextView) findViewById(R.id.title_text);
        mTextViewTitle.setOnClickListener(this);

        vImageViewRight = (ImageView) findViewById(R.id.op2);
        vImageViewRight.setVisibility(View.VISIBLE);

        vImageViewRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AfFriendInfo afFriendInfo = afFriend;
                toProfile(afFriendInfo);
            }
        });

        mVoiceImageEmotion = (ImageView) findViewById(R.id.image_emotion2);
        mVoiceImageEmotion.setOnClickListener(this);
        mVoiceImageViewMore = (ImageView) findViewById(R.id.chatting_operate_two2);
        mVoiceImageViewMore.setOnClickListener(this);

        mVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_progress);
        mVoiceProgressBar2 = (ProgressBar) findViewById(R.id.voice_progress2);

        vButtonSend = (ImageView) findViewById(R.id.chatting_send_button);
        vButtonSend.setOnClickListener(this);
        vButtonBackKeyboard = (ImageView) findViewById(R.id.btn_backkeyboard);
        vButtonBackKeyboard.setOnClickListener(this);
        vButtonKeyboard = (ImageView) findViewById(R.id.btn_keyboard);
        vButtonKeyboard.setOnClickListener(this);
        vButtonLeftKeyboard = (ImageView) findViewById(R.id.btn_leftkeyboard);
        vButtonLeftKeyboard.setOnClickListener(this);
        vImageViewVoice = findViewById(R.id.chatting_operate_one);
        vImageViewVoice.setOnClickListener(this);
        viewUnTalk = findViewById(R.id.un_talk);
        viewFrameToast = findViewById(R.id.view_frame);
        view_unread = findViewById(R.id.view_unread);
        textview_unread = (TextView) findViewById(R.id.textView_unread);
        vImageViewMore = (ImageView) findViewById(R.id.chatting_operate_two);
        vImageViewMore.setOnClickListener(this);
        vButtonTalk = (Button) findViewById(R.id.talk_button);
        vButtonTalk.setOnTouchListener(new HoldTalkListener());
        vImageEmotion = (ImageView) findViewById(R.id.image_emotion);
        vImageEmotion.setOnClickListener(this);
        mImageViewAddGroup = (ImageView) findViewById(R.id.op1);
        mImageViewAddGroup.setBackgroundResource(R.drawable.add_group_btn);
        mImageViewAddGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                ArrayList<String> existList = new ArrayList<String>();
                existList.add(mFriendAfid);
                b.putStringArrayList("afId_list", existList);
                b.putInt("owner_num", existList.size());
                b.putString("come_page", "singlechat_page");
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(context, FriendsActivity.class);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        emojjView = new EmojjView();
        emojjView.setView(Chatting.this);
        //由于表情框以及获取用户profile比较耗时，所以延迟加载这些内容，以达到进入chatting比较流畅
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //初始化表情框
                //emojjView = new EmojjView();
                emojjView.initViews();
                //选择默认表情
                emojjView.select(EmojiParser.SUN);
                //把表情框加入到chatting_emoji_layout布局中
                chatting_emoji_layout.addView(emojjView.getViewRoot());
                emojjView.getViewRoot().setVisibility(View.GONE);
                //初始化表情底部
                faceFooterView = new FaceFooterView(Chatting.this, emojjView);
                faceFooterView.addGifFooter();
                emojjView.setFaceFooterView(faceFooterView);
                //判断接收到的消息是否为公众帐号发送
                if (mFriendAfid != null && !mFriendAfid.startsWith(DefaultValueConstant._R)) {
                    //若不是公众帐号，则判断是否进入聊天页面直接显示语音布局
                    boolean showVoice = CacheManager.getInstance().getShowChattingVoice(mFriendAfid);
                    if (showVoice && !isFromPop) {//显示语音布局
                        vImageViewMore.performClick();
                    }
                    else if(isFromPop){
                        vEditTextContent.requestFocus();
                    }
                }
                final MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(mFriendAfid);
                if (aff != null) {
                    boolean flag = MessagesUtils.isFriendReqMessage(aff.afMsgInfo.type);
                    if (flag) {//显示好友请求布局
                        r_stranger_add.setVisibility(View.GONE);
                        r_frd_add.setVisibility(View.VISIBLE);
                        set_AcceptFriend_clickable(mFriendAfid, Consts.HTTP_CODE_UNKNOWN);

                    }
                }

                final AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(mFriendAfid);
                if (afF != null) {//若是好友，则隐藏加好友布局和好友请求布局
                    vImageViewRight.setBackgroundResource(R.drawable.t_profile);
                    r_stranger_add.setVisibility(View.GONE);
                    r_frd_add.setVisibility(View.GONE);
                    if (!mFriendAfid.startsWith(DefaultValueConstant._R)) {
                        mImageViewAddGroup.setVisibility(View.VISIBLE);
                    }
                } else if (r_frd_add.getVisibility() == View.GONE) {
                    vImageViewRight.setBackgroundResource(R.drawable.t_profile);
                    String my_afid = CacheManager.getInstance().getMyProfile().afId;
                    if (!mFriendAfid.startsWith(DefaultValueConstant._R)) {
                        if (CommonUtils.isOverOneDay(context, my_afid, mFriendAfid)) {//判断是否超过一天
                            r_stranger_add.setVisibility(View.VISIBLE);
                        } else {
                            r_stranger_add.setVisibility(View.GONE);
                        }
                    }
                    r_frd_add.setVisibility(View.GONE);
                    set_addFriend_clickable(mFriendAfid, Consts.HTTP_CODE_UNKNOWN);
                }
                if (!mFriendAfid.startsWith(DefaultValueConstant._R)) {
                    if (afF == null) {//not my friend
                        String my_afid = CacheManager.getInstance().getMyProfile().afId;
                        if (r_frd_add.getVisibility() == View.GONE && r_stranger_add.getVisibility() == View.GONE && CommonUtils.isOverOneDay(context, my_afid, mFriendAfid)) {
                            r_stranger_add.setVisibility(View.VISIBLE);
                            set_addFriend_clickable(mFriendAfid, Consts.HTTP_CODE_UNKNOWN);
                        }
                    }
                }
                AfFriendInfo friendInfo = CacheManager.getInstance().searchAllFriendInfo(mFriendAfid);
                if (friendInfo == null) {
                    friendInfo = afFriend;
                }
                if (friendInfo != null) {
                    //调UIL的显示头像方法
                    ImageManager.getInstance().DisplayAvatarImage(stranger_head_icon, friendInfo.getServerUrl(), friendInfo.getAfidFromHead()
                            , Consts.AF_HEAD_MIDDLE, friendInfo.sex, friendInfo.getSerialFromHead(), null);
                    //调UIL的显示头像方法
                    ImageManager.getInstance().DisplayAvatarImage(frd_head_icon, friendInfo.getServerUrl(), friendInfo.getAfidFromHead()
                            , Consts.AF_HEAD_MIDDLE, friendInfo.sex, friendInfo.getSerialFromHead(), null);
                }
            }
        }, 200);
        viewOptionsLayout = findViewById(R.id.chatting_options_layout);
        viewChattingVoiceLayout = findViewById(R.id.chatting_voice_layout);
        chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
//		chattingOptions = createOptionsGridView(4);
        chattingOptions = (LinearLayout) findViewById(R.id.chatting_item_option);
        img_picture = (LinearLayout) findViewById(R.id.img_picture);
        img_camera = (LinearLayout) findViewById(R.id.img_camera);
        img_voice = (LinearLayout) findViewById(R.id.img_voice);
        img_name_card = (LinearLayout) findViewById(R.id.img_name_card);
        img_video = (LinearLayout) findViewById(R.id.img_video);
        img_video.setVisibility(View.INVISIBLE);
        img_background = (LinearLayout) findViewById(R.id.img_background);
        img_picture.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_voice.setOnClickListener(this);
        img_name_card.setOnClickListener(this);
        img_video.setOnClickListener(this);
        img_background.setOnClickListener(this);
        img_picture_p = (ImageView) findViewById(R.id.img_picture_p);
        img_camera_c = (ImageView) findViewById(R.id.img_camera_c);
        img_voice_v = (ImageView) findViewById(R.id.img_voice_v);
        img_name_card_n = (ImageView) findViewById(R.id.img_name_card_n);
        img_video_v = (ImageView) findViewById(R.id.img_video_v);
        img_background_v = (ImageView) findViewById(R.id.img_background_v);
        img_picture_p.setOnClickListener(this);
        img_camera_c.setOnClickListener(this);
        img_voice_v.setOnClickListener(this);
        img_name_card_n.setOnClickListener(this);
        img_video_v.setOnClickListener(this);
        img_background_v.setOnClickListener(this);
        /*add by zhh*/
        view_unread.setOnClickListener(this);

        mRippleView = (RippleView) findViewById(R.id.ripple);
        mRippleViewEffect=(RippleBackground)findViewById(R.id.chat_recordcall_effect);
        chattingOptions.setVisibility(View.GONE);
        vImageViewBack = (ImageView) findViewById(R.id.back_button);
        vImageViewBack.setOnClickListener(this);
        if (!CommonUtils.isEmpty(mFriendAfid) && mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team Palmchat team 不显示more、表情按钮和语音按键
            isPublicMember = true;//palmchat team
            vImageViewRight.setVisibility(View.GONE);
            vButtonSend.setVisibility(View.VISIBLE);
            vImageViewVoice.setVisibility(View.GONE);
            vImageViewMore.setVisibility(View.GONE);
            vImageEmotion.setVisibility(View.GONE);
            r_stranger_add.setVisibility(View.GONE);
            r_frd_add.setVisibility(View.GONE);
        }

        if (CommonUtils.getSdcardSize()) {
            ToastManager.getInstance().show(context, R.string.memory_is_full);
        }
        vEditTextContent.setOnTouchListener(new View.OnTouchListener() {
            float actionx = 0,actiony = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    // 当按下的时候
                    case (MotionEvent.ACTION_DOWN):
                        actionx = event.getX();
                        actiony = event.getY();
                        break;
                    // 当按上的时候
                    case (MotionEvent.ACTION_UP):
                        if(Math.abs(actionx - event.getX()) > 10d || Math.abs(actiony - event.getY()) > 10d){
                            closeEmotions();
                        }
                        break;
                }
                return false;
            }
        });
        //设置聊天背景
        setChatBg();
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
                        case AfMessageInfo.MESSAGE_CARD:
                            afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_CARD;
                            afMessageInfo.msg = forwardMsgInfo.msg;
                            afMessageInfo.attach = forwardMsgInfo.attach;
                            break;
                        case AfMessageInfo.MESSAGE_IMAGE:
                            afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_IMAGE;

                            final AfAttachImageInfo forwardAttachImageInfo = (AfAttachImageInfo) forwardMsgInfo.attach;
                            AfAttachImageInfo afAttachImageInfo = new AfAttachImageInfo();
                            PalmchatLogUtils.println("Chatting forwardMsg forwardAttachImageInfo:" + forwardAttachImageInfo);
                            if (forwardAttachImageInfo != null) {

                                afAttachImageInfo.large_file_name = forwardAttachImageInfo.large_file_name;
                                afAttachImageInfo.large_file_size = forwardAttachImageInfo.large_file_size;
                                afAttachImageInfo.small_file_name = forwardAttachImageInfo.small_file_name;
                                afAttachImageInfo.small_file_size = forwardAttachImageInfo.small_file_size;

                            }
                            AfImageReqInfo param = new AfImageReqInfo();

                            param.file_name = afAttachImageInfo.small_file_name + System.currentTimeMillis();
                            param.path = afAttachImageInfo.large_file_name;
                            param.recv_afid = mFriendAfid;
                            param.send_msg = mFriendAfid;
                            //
                            param._id = mAfCorePalmchat.AfDbImageReqInsert(param);
                            afAttachImageInfo.upload_id = param._id;
                            afAttachImageInfo.upload_info = param;

                            afAttachImageInfo._id = mAfCorePalmchat.AfDbAttachImageInsert(afAttachImageInfo);
                            afMessageInfo.attach = afAttachImageInfo;
                            afMessageInfo.attach_id = afAttachImageInfo._id;
                            break;

                        default:
                            break;

                    }
                    afMessageInfo.fid = forwardMsgInfo.fid;
                    afMessageInfo.client_time = System.currentTimeMillis();//afMessageInfo 更改??
                    afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                    afMessageInfo.toAfId = mFriendAfid;
                    afMessageInfo.fromAfId = null;
                    afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);//向数据库插入一条数据
                    //设置此消息的状态
                    mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afMessageInfo._id, 0, afMessageInfo.fid, Consts.AFMOBI_PARAM_FID);
                    //向缓存中插入一条数据
                    MessagesUtils.insertMsg(afMessageInfo, true, true);
                    //在mainHandler中实现发送转发消息
                    mainHandler.obtainMessage(MessagesUtils.MSG_FORWARD, afMessageInfo).sendToTarget();
                    CacheManager.getInstance().setForwardMsg(null);//add by wxl 
                }
            }).start();

        }

    }

    /**
     * 设置语音模式
     */
    private void setListenMode() {
        int listenMode = SharePreferenceService.getInstance(PalmchatApp.getApplication().getApplicationContext()).getListenMode();
        PalmchatLogUtils.println("Chatting  time setListenMode " + dateFormat.format(new Date()) + "  listenMode  " + listenMode);
        if (listenMode == AudioManager.MODE_IN_CALL) {//听筒模式
            onItemClick(ChattingMenuDialog.ACTION_HANDSET_MODE);
        } else {//扬声器模式
            onItemClick(ChattingMenuDialog.ACTION_SPEAKER_MODE);
        }
    }


    /**
     * 跳转到profile界面
     *
     * @param afFriendInfo 朋友profile
     */
    private void toProfile(final AfFriendInfo afFriendInfo) {
        //gtf 2014-11-16 
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CT_T_PF);
//        MobclickAgent.onEvent(context, ReadyConfigXML.CT_T_PF);

        Intent intent = new Intent(context, ProfileActivity.class);
        AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
        intent.putExtra(JsonConstant.KEY_PROFILE, info);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, info.afId);
        intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
        intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.CHAT_TO_PROFILE);
        startActivityForResult(intent, 1001);
    }

    /**
     * 隐藏相应的布局
     */
    private void disableViews() {
        vTextViewRecordTime.setVisibility(View.GONE);
        if(emojjView != null) {
            emojjView.getViewRoot().setVisibility(View.GONE);
        }
        chattingOptions.setVisibility(View.GONE);
        vButtonKeyboard.setVisibility(View.GONE);
        vButtonLeftKeyboard.setVisibility(View.GONE);
        if (!(!CommonUtils.isEmpty(mFriendAfid) &&
                mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS))) {//Palmchat team 不显示more和表情按钮
            vImageEmotion.setVisibility(View.VISIBLE);
            vImageViewMore.setVisibility(View.VISIBLE);
        }
        if (viewChattingVoiceLayout.getVisibility() == View.VISIBLE) {
            viewOptionsLayout.setVisibility(View.VISIBLE);
            viewChattingVoiceLayout.setVisibility(View.GONE);
        }

    }

    /**
     * 显示录音布局
     */
    private void showTalkView() {
        if (viewUnTalk.getVisibility() != View.VISIBLE) {
            viewUnTalk.setVisibility(View.VISIBLE);
            vButtonTalk.setVisibility(View.GONE);
//            vImageViewVoice.setBackgroundResource(R.drawable.chatting_setmode_voice_btn_focused);
        }
    }


    @Override
    public void init() {
        //设置好友名称
        mTextViewTitle.setText(mFriendName);
        //初始化ListView适配器
        adapterListView = new ChattingAdapter(this, listChats, vListView,   mFriendAfid);
        adapterListView.setOnItemClick(this);
        vListView.setOnScrollListener(new ImageOnScrollListener(vListView ));
        vListView.setAdapter(adapterListView);
        //		if is from forward msg
        isForward = intent.getBooleanExtra(JsonConstant.KEY_FORWARD, false);
        setAdapter(null); 

//		view history can't send msg
        isViewHistory = intent.getBooleanExtra(JsonConstant.VIEW_HISTORY, false);
        if (isViewHistory) {
            viewOptionsLayout.setVisibility(View.GONE);
        }

        PalmchatApp.getApplication().addMessageReceiver(this);
        PalmchatApp.getApplication().setMessageReadReceiver(this);
        PalmchatApp.getApplication().setColseSoftKeyBoardListe(this);
        isFollow = CommonUtils.isFollow(mFriendAfid);
        getMsgData(true);
        	/* 从分享或者转发图片进入此页面 */
        looperThread.handler.sendEmptyMessageDelayed(LooperThread.SHARE_IMAGE,150);
    }

    /**
     * 设置用户在线状态
     */
    private void setOnlineStatus(){
        if (!mFriendAfid.startsWith(DefaultValueConstant._R)) {//非公众帐号
            if (PalmchatApp.getApplication().getOnlineStatusInfoMap().containsKey(mFriendAfid)) {
                AfOnlineStatusInfo afOnlineStatusInfo = PalmchatApp.getApplication().getOnlineStatusInfoMap().get(mFriendAfid);
                if (afOnlineStatusInfo != null) {
                    long nowTime = System.currentTimeMillis();
                    mTextViewOnline.setVisibility(View.VISIBLE);
                    if (afOnlineStatusInfo.status == AfOnlineStatusInfo.AFMOBI_ONLINESTAUS_INFO_ONLINE) {
                        mTextViewOnline.setText(R.string.online);
                        isOnline = true;
                    } else {
                        String dateTime = dateFormat(afOnlineStatusInfo.datetime);
                        if(!TextUtils.isEmpty(dateTime)) {
                            mTextViewOnline.setText(PalmchatApp.getApplication().getString(R.string.frd_last_seen).replace("XXXX", dateFormat(afOnlineStatusInfo.datetime)));
                        }
                        else{
                            mTextViewOnline.setText("");
                        }
                    }
                    if (nowTime - afOnlineStatusInfo.ts > 10 * 1000 * 60) {
                        getOnlineStatusInfo();
                    }
                }
            } else {
                getOnlineStatusInfo();
            }
        }
    }

    /**
     * 获取用户在线状态信息
     */
    private void getOnlineStatusInfo() {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAfCorePalmchat.AfHttpGetOnlineStatusNew(mFriendAfid, null, Chatting.this);
            }
        }, 1500);
    }


    /**
     * 发送已读消息标识到服务商
     */
    private void sendAlreadyRead() {
        if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
            toSendRead = false;
            int code = mAfCorePalmchat.AfHttpSendMsg(mFriendAfid, System.currentTimeMillis(), null, Consts.MSG_CMMD_READ, Consts.REQ_CODE_READ, this);
            PalmchatLogUtils.println("sendAlreadyRead  code  " + code);
        }
    }


    /**
     * 从本地数据库中获取最新的聊天的记录
     *
     * @return 返回消息集合
     */
    private List<AfMessageInfo> getRecentData() {
        PalmchatLogUtils.println("mOffset  " + mOffset + "  mCount  " + mCount);
        AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mFriendAfid, mOffset, mCount);
        if (recentDataArray == null || recentDataArray.length == 0) {
            return new ArrayList<AfMessageInfo>();
        }
        List<AfMessageInfo> recentDataList = Arrays.asList(recentDataArray);
        return recentDataList;
    }


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
     * 分享照片
     */
    private void shareImage() {
        if (isShareImage && null != shareImageUri) {
            showProgressDialog(R.string.Sending);
/*            new Thread(new Runnable() {
                @Override
                public void run() {*/
					for (String path : shareImageUri) { 
						if (!TextUtils.isEmpty(path)) {
							imageFile = new File(path); 
							if ((null != imageFile) && (!imageFile.exists())) {
								mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
							} else {
								File f2 = FileUtils.copyToImg(imageFile.getAbsolutePath());
								if (f2 != null) {
									imageFile = f2;
								}
								cameraFilename = f2.getAbsolutePath();// imageFile.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
								smallImage(imageFile, MessagesUtils.PICTURE);
								cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
								cameraFilename = BitmapUtils.imageCompressionAndSave(imageFile.getAbsolutePath(), cameraFilename);
								if (CommonUtils.isEmpty(cameraFilename)) {
									mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
									return;
								}
								Log.i("shareImage", "isShareImage : " + isShareImage + "shareImageUri: " + shareImageUri);
								mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
							}
						}else{
							mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
							
						}
					}
/*				}
            }).start();*/
        } else if (!TextUtils.isEmpty(forward_imagePath)) {
            if(!mIsForworded) {
                imageFile = new File(forward_imagePath);
                File f2 = FileUtils.copyToImg(imageFile.getAbsolutePath());
                if (f2 != null) {
                    imageFile = f2;
                }
                cameraFilename = f2.getAbsolutePath();//imageFile.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                smallImage(imageFile, MessagesUtils.PICTURE);
                cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                cameraFilename = BitmapUtils.imageCompressionAndSave(imageFile.getAbsolutePath(), cameraFilename);
                if (CommonUtils.isEmpty(cameraFilename)) {
                    mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
                    return;
                }
                Log.i("shareImage", "forward_imagePath : " + forward_imagePath);
                mIsForworded = true;
                mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
            }
        }
    }


    /**
     * 获取输入框内容
     *
     * @return
     */
    private String getEditTextContent() {
        return vEditTextContent.getText().toString();
    }

    /**
     * 设置适配器
     *
     * @param afMessageInfo 消息对象
     */
    private void setAdapter(AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        if (adapterListView == null) {
            adapterListView = new ChattingAdapter(this, listChats, vListView,  mFriendAfid);
            vListView.setAdapter(adapterListView);
        } else {
            if (afMessageInfo != null && MessagesUtils.ACTION_UPDATE == afMessageInfo.action) {
                PalmchatLogUtils.println("setAdapter  afMessageInfo.action ACTION_UPDATE");
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            } else if (afMessageInfo != null) {
                PalmchatLogUtils.println("setAdapter  afMessageInfo.action " + afMessageInfo.action);
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }
            adapterListView.notifyDataSetChanged();
            if ((mIsNoticefy && 0 < adapterListView.getCount()) || isBottom) {//若在底部，则不显示最新未读消息
                PalmchatLogUtils.println("Chatting  setAdapter");
                vListView.setSelection(adapterListView.getCount());
                mIsNoticefy = false;
                hideUnreadTips();
            } else {
                if (unreadTipsCount > 0) {
                    view_unread.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 隐藏未读消息提示
     */
    private void hideUnreadTips() {
        if (view_unread.getVisibility() == View.VISIBLE) {
            view_unread.setVisibility(View.GONE);
            unreadTipsCount = 0;
        }
    }


    /**
     * 异步加载缓存到数据库中的聊天信息
     */
   /* private class GetDataTask extends AsyncTask<Void, Void, List<AfMessageInfo>> {

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
            PalmchatLogUtils.i("chatingGetChatting","count:"+ recentData.size());
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
                listChats.clear();
                if (isForward) {
                    forwardMsg();
                }
                for (AfMessageInfo afMessageInfo : result) {
                    int status = afMessageInfo.status;
                    if (MessagesUtils.isReceivedMessage(status)) {
                        toSendRead = true;
                        break;
                    }
                }
                if (toSendRead) {
                    //发送已读消息标识到服务商
                    sendAlreadyRead();
                }
            } else {
                stopRefresh();
            }
            bindData(result);
            if (isInit) {
                getData();
            } 
        }


    }*/


    /**
     * 此方法把主要是把最新的消息集合添加到ListView中并显示，
     *
     * @param result
     */
    private void bindData(List<AfMessageInfo> result) {
        int size = result.size();
        int listSize = listChats.size();
        PalmchatLogUtils.i("chatingGetChatting","bindData new addSize  " + size + " old listChats.size()  " + listSize + " old adapterListView.getCount()  " + adapterListView.getCount());
        if (size > 0) {
            for (int i = 0; i < result.size(); i++) {
                AfMessageInfo afMessageInfo = result.get(i);
                CommonUtils.getRealList(listChats, afMessageInfo);
                setVoiceQueue(afMessageInfo);
            }
            if (!mIsFirst) {
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                if (pop) {
                    vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    pop = false;
                }
                adapterListView.notifyDataSetChanged();
                PalmchatLogUtils.println("bindData  new  adapterListView.getCount()  " + adapterListView.getCount() + " new listChats.size()  " + listChats.size());
                vListView.setSelection(listChats.size() - listSize);
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
//    	stopRefresh();
    }


    /**
     * 把语音添加到消息集合中
     *
     * @param afMessageInfo
     */
    private void setVoiceQueue(AfMessageInfo afMessageInfo) {
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
        int status = afMessageInfo.status;
        if (MessagesUtils.isReceivedMessage(status) && msgType == AfMessageInfo.MESSAGE_VOICE) {//from others chat and voice type
            CommonUtils.getRealList(listVoiceChats, afMessageInfo);
        }
    }


    /**
     * 停止刷新、停止加载
     */
    private void stopRefresh() {
        // TODO Auto-generated method stub
        isRefreshing = false;

        vListView.stopRefresh();
        vListView.stopLoadMore();
        vListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
    }

    private float mDownX;
    private float mDownY;

    private long mCurrentClickTime;
    private static final long LONG_PRESS_TIME = 1500;

    /**
     * 发送语音按钮监听类。控制发送语音手势（发送语音，语音取消等）
     *
     * @author afmobi
     */
    class HoldTalkListener implements OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.talk_button) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下操作
                        if (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime > LONG_PRESS_TIME) {
                            PalmchatLogUtils.println("HoldTalkListener sent " + sent);
                            if (sent) {
                                ToastManager.getInstance().show(Chatting.this, R.string.chat_ready);
                                return false;
                            }
                            //以下按下时的获取x,y坐标
                            mDownX = event.getRawX();
                            mDownY = event.getRawY();
                            PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_DOWN----");
                            if (!CommonUtils.isHasSDCard()) {
                                ToastManager.getInstance().show(Chatting.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
                                return false;
                            }
                            mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
                            if (recordStart > 0) {//判断是否已录音
                                PalmchatLogUtils.e(TAG, "----recordStart----" + recordStart);
                                //发送录音消息
                                mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                                //停止定时器
                                stopRecordingTimeTask();
                                setMode(SharePreferenceService.getInstance(PalmchatApp.getApplication().getApplicationContext()).getListenMode());
                                vButtonTalk.setSelected(false);
                                //停止录音
                                stopRecording();
                                sent = true;
                                return false;
                            }
                            PalmchatLogUtils.e(TAG, "----startRecording----" + recordStart);
                            mVoiceName = startRecording();
//					start record exception
                            if (mVoiceName == null) {
                                return false;
                            }
                            //设置杨声器模式
                            setMode(AudioManager.MODE_NORMAL);
                            if (VoiceManager.getInstance().isPlaying()) {
                                VoiceManager.getInstance().pause();
                            }
                            PalmchatLogUtils.println("mVoiceName " + mVoiceName);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_MOVE----");
//					 already stop recording
                        if (recordStart <= 0) {
                            break;
                        }
                        float offsetX1 = Math.abs(event.getRawX() - mDownX);
                        float offsetY1 = Math.abs(event.getRawY() - mDownY);
                        if (offsetX1 < ImageUtil.dip2px(context, 85) && offsetY1 < ImageUtil.dip2px(context, 85)) {
//                            mCancelVoice.setText(R.string.empty);
//                            mCancelVoice.setVisibility(View.GONE);

                            if (mCancelVoice.getText().equals(getString(R.string.cancel_voice))) {
                                mCancelVoice.setText(R.string.empty);
                            } else
                            {
                                mCancelVoice.setText(R.string.push_up_to_cancel);
                            }
                            vButtonTalk.setSelected(false);
                            mVoiceProgressBar.setVisibility(View.VISIBLE);
                            mVoiceProgressBar2.setVisibility(View.GONE);
                            vTextViewRecordTime.setTextColor(getResources().getColor(R.color.log_blue));
//					show cancel voice text
                        } else {//显示取消提示
                            mCancelVoice.setText(R.string.cancel_voice);
                            mCancelVoice.setVisibility(View.VISIBLE);
                            vButtonTalk.setSelected(true);
                            mVoiceProgressBar.setVisibility(View.GONE);
                            mVoiceProgressBar2.setVisibility(View.VISIBLE);
                            vTextViewRecordTime.setTextColor(getResources().getColor(R.color.color_voice_red));
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----");
                        if (/*vTextViewRecordTime != null && vTextViewRecordTime.isShown() &&*/ !sent) {
                            PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
                            if (CommonUtils.getSdcardSize()) {
                                ToastManager.getInstance().show(context, R.string.memory_is_full);
                            } else {
                                float offsetX = Math.abs(event.getRawX() - mDownX);
                                float offsetY = Math.abs(event.getRawY() - mDownY);
                                if (offsetX < ImageUtil.dip2px(context, 85) && offsetY < ImageUtil.dip2px(context, 85)) {//当手释放时，如果在此番外范围内，则直接发送语音
                                    PalmchatLogUtils.println("---- send voice----");
                                    if (recordStart > 0) {
                                        mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                                        sent = true;
                                    }
                                } else {
                                    ToastManager.getInstance().show(context, R.string.cancelled);
                                    mHandler2.removeCallbacksAndMessages(null);
                                }
                            }
//							mRecordPrepareBlock
                            vTextViewRecordTime.setVisibility(View.GONE);
                            stopRecording();
                            stopRecordingTimeTask();
                        }
                        setMode(SharePreferenceService.getInstance(PalmchatApp.getApplication().getApplicationContext()).getListenMode());
//					show cancelled text
                        if (mCancelVoice.getVisibility() == View.VISIBLE) {
//                            mCancelVoice.setText(R.string.cancelled);
                            mainHandler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
//                                    mCancelVoice.setVisibility(View.GONE);
//                                    if (!mCancelVoice.getText().equals(getString( R.string.call_Voice_Tips))) {
//                                        mCancelVoice.setVisibility(View.GONE);
//                                    }

                                    mCancelVoice.setVisibility(View.VISIBLE);
//                                    if (mCancelVoice.getText().equals( getString( R.string.empty))) {
//                                        vTextViewRecordTime.setText("0.0s");
//                                    }
                                    if (!mCancelVoice.getText().equals(getString( R.string.call_Voice_Tips))) {
//                                        mTextCancel.setVisibility(View.GONE);
                                        mCancelVoice.setText(R.string.call_Voice_Tips);
                                    }

                                }
                            }, 1000);


                        }
                        vButtonTalk.setSelected(false);
                        break;
                }
            }
            return false;
        }
    }

    private Timer recordingTimer = new Timer();
    private TimerTask recordingTimerTask;

    private void startRecordingTimeTask() {
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }
        recordingTimerTask = new TimerTask() {
            @Override
            public void run() {
                mainHandler.sendEmptyMessage(RECORDING_TIMER);
            }
        };
        recordingTimer.schedule(recordingTimerTask, 0, 100);
    }

    private void stopRecordingTimeTask() {
        if (null != recordingTimerTask) {
            recordingTimerTask.cancel();
        }
    }

    /**
     * 关闭表情
     */
    public void closeEmotions() {
        // TODO Auto-generated method stub
        vButtonKeyboard.setVisibility(View.GONE);
        if (mIsRecording) {
            vTextViewRecordTime.setVisibility(View.VISIBLE);
        } else {
            vTextViewRecordTime.setVisibility(View.GONE);
        }
        if(emojjView != null) {
            emojjView.getViewRoot().setVisibility(View.GONE);
        }
        chattingOptions.setVisibility(View.GONE);
        vButtonLeftKeyboard.setVisibility(View.GONE);
        if (!(!CommonUtils.isEmpty(mFriendAfid) &&
                mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS))) {//Palmchat team 不显示more和表情按钮
            vImageEmotion.setVisibility(View.VISIBLE);
            vImageViewMore.setVisibility(View.VISIBLE);
        }
        PalmchatLogUtils.println("Chatting  closeEmotions  " + adapterListView.getCount());
        vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        vListView.setSelection(adapterListView.getCount());
    }

    Handler mHandler2 = new Handler();
    private long mTimeStart;

    /**
     * 开始录音
     *
     * @return
     */
    private String startRecording() {
        String voiceName = null;
        if (RequestConstant.checkSDCard()) {
//			String dir = mAfCorePalmchat.AfResGetDir(AfMessageInfo.MESSAGE_VOICE);
            voiceName = mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_VOICE);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            mRecorder.setOutputFile(RequestConstant.VOICE_CACHE + voiceName);//ClippingSounds.saveTalkSoundsFileNames();
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOnInfoListener(new OnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    // TODO Auto-generated method stub
                    mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                    stopRecordingTimeTask();
                    setMode(SharePreferenceService.getInstance(PalmchatApp.getApplication().getApplicationContext()).getListenMode());
                    vButtonTalk.setSelected(false);
                    stopRecording();
                    sent = true;
                }
            });

            try {
                mRecorder.prepare();
                mRecorder.start();
                recordStart = System.currentTimeMillis();
                mTimeStart = System.currentTimeMillis();
            } catch (Exception e) {
                PalmchatLogUtils.e("startRecording", "prepare() failed" + e.getMessage());
                e.printStackTrace();

//				vTextViewRecordTime.setVisibility(View.GONE);
                return null;
            }

            final String recordName = voiceName;

            mHandler2.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    long time1 = System.currentTimeMillis();
                    byte[] recordData = FileUtils.readBytes(RequestConstant.VOICE_CACHE + recordName);
                    long time2 = System.currentTimeMillis();

                    if (recordData != null) {
                        PalmchatLogUtils.println("--ddd chatting recordData length " + recordData.length);
                    } else {
                        PalmchatLogUtils.println("--ddd chatting recordData null ");
                    }
                    PalmchatLogUtils.println("--ddd chatting readBytes time  " + (time2 - time1));

//                    if (recordData == null || recordData.length <= 0) {
//                        stopRecording();
//                        stopRecordingTimeTask();
//                    } else {
//						显示录音波纹
                        mRippleView.setVisibility(View.VISIBLE);
                        mRippleView.setOriginRadius(vButtonTalk.getWidth() / 2);
                        mRippleView.startRipple();
                        mRippleViewEffect.startRippleAnimation();

                        vButtonBackKeyboard.setClickable(false);
                        mVoiceImageEmotion.setClickable(false);
                        mVoiceImageViewMore.setClickable(false);
                        mCancelVoice.setVisibility(View.VISIBLE);
                        mCancelVoice.setText(R.string.push_up_to_cancel);

                        mIsRecording = true;
                        adapterListView.setIsRecording(mIsRecording);

                        createVoiceDialog();
                        startRecordingTimeTask();

                        AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
                        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


//                    }
                }
            }, 200);
        }
        return voiceName;
    }

    /**
     * 停止录音
     */
    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
                PalmchatLogUtils.e("stopRecording", "stopRecording() failed" + e.getMessage());
            } finally {
                mRecorder = null;
                recordEnd = System.currentTimeMillis();
                recordTime = Math.min((int) ((recordEnd - recordStart) / 1000), RequestConstant.RECORD_VOICE_TIME);
                recordStart = 0;
                recordEnd = 0;
                vTextViewRecordTime.setVisibility(View.GONE);
                vTextViewRecordTime.setText("0s");
                mRippleView.setVisibility(View.GONE);
                mRippleView.stopRipple();
                mRippleViewEffect.stopRippleAnimation();

                vButtonBackKeyboard.setClickable(true);
                mVoiceImageEmotion.setClickable(true);
                mVoiceImageViewMore.setClickable(true);

                mIsRecording = false;
                adapterListView.setIsRecording(mIsRecording);

                mVoiceProgressBar.setVisibility(View.VISIBLE);
                mVoiceProgressBar2.setVisibility(View.GONE);
                mVoiceProgressBar.setProgress(0);
                mVoiceProgressBar2.setProgress(0);


                AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
                am.abandonAudioFocus(null);


            }
        }
    }

    /**
     * 显示录音时间
     */
    private void createVoiceDialog() {
        vTextViewRecordTime.setVisibility(View.VISIBLE);
        vTextViewRecordTime.setText("0s");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU://显示菜单
                if (mFriendAfid != null && !mFriendAfid.startsWith(DefaultValueConstant._R)) {
                    int listenMode = SharePreferenceService.getInstance(PalmchatApp.getApplication().getApplicationContext()).getListenMode();
                    addAction(listenMode, mFriendAfid);
                    widget.show();
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                vButtonKeyboard.setVisibility(View.GONE);
                vButtonLeftKeyboard.setVisibility(View.GONE);
                if (!(!CommonUtils.isEmpty(mFriendAfid) &&
                        mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS))) {//Palmchat team 不显示more和表情按钮
                    vImageEmotion.setVisibility(View.VISIBLE);
                    vImageViewMore.setVisibility(View.VISIBLE);
                }
                if (emojjView != null && emojjView.getViewRoot().getVisibility() != View.GONE) {
                    emojjView.getViewRoot().setVisibility(View.GONE);
                    return false;
                } else if (chattingOptions.getVisibility() != View.GONE) {
                    chattingOptions.setVisibility(View.GONE);
                    return false;
                } else if (viewChattingVoiceLayout.getVisibility() == View.VISIBLE) {
                    viewOptionsLayout.setVisibility(View.VISIBLE);
                    viewChattingVoiceLayout.setVisibility(View.GONE);
                    return false;
                } else {
                    back();
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 刷新聊天列表
     * @param event
     */
    public void onEventMainThread(ShareBroadcastRefreshChatting event) {
        if(null != event){
            adapterListView.notifyDataSetChanged();
        }
    }
    public void onEventMainThread(CloseChatingEvent event) {
        finish();
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
    private void sendMessageInfoForText(final int msgType, final String fileName,
                                        final int fileSize, final int length, final Handler handler, final int handler_MsgType, final int action, final AfMessageInfo afMessageInfo, final String content) {
        PalmchatLogUtils.println("sendMessageInfoForText " + fileName + "  mVoiceName  " + mVoiceName);
        final AfMessageInfo messageInfo = new AfMessageInfo();//新增
        new Thread(new Runnable() {
            public void run() {
                switch (msgType) {
                    case AfMessageInfo.MESSAGE_TEXT://文本消息
                        if (action == MessagesUtils.ACTION_INSERT) {//新消息并插入本地数据库
                            messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
                            messageInfo.toAfId = mFriendAfid;
                            messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_TEXT;
                            messageInfo.msg = content;
                            messageInfo.status = AfMessageInfo.MESSAGE_SENTING;

                            messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);

                        } else if (action == MessagesUtils.ACTION_UPDATE) {//重发此消息并更新本地数据库
//						afMessageInfo.client_time = System.currentTimeMillis();//afMessageInfo 更改�?
                            afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

                            PalmchatLogUtils.println("---msg id:" + afMessageInfo._id);
                            mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
                            PalmchatLogUtils.println("---msg id: AfDbMsgUpdate " + afMessageInfo._id);
                        }
                        break;
                    case AfMessageInfo.MESSAGE_VOICE://语音消息
                        if (action == MessagesUtils.ACTION_INSERT) {//新消息并插入本地数据库
                            AfAttachVoiceInfo afAttachVoiceInfo = new AfAttachVoiceInfo();
                            afAttachVoiceInfo.file_name = fileName;
                            afAttachVoiceInfo.file_size = fileSize;
                            afAttachVoiceInfo.voice_len = length;
                            afAttachVoiceInfo._id = mAfCorePalmchat.AfDbAttachVoiceInsert(afAttachVoiceInfo);
                            PalmchatLogUtils.println("Chatting  sendMessageInfoForText " + fileName + "  mVoiceName  " + mVoiceName + "  voice_length  " + length);

                            messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
                            messageInfo.toAfId = mFriendAfid;
                            messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_VOICE;
                            messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                            messageInfo.attach = afAttachVoiceInfo;
                            messageInfo.attach_id = afAttachVoiceInfo._id;
                            messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);
                        } else if (action == MessagesUtils.ACTION_UPDATE) {//重发此消息并更新本地数据库
//						afMessageInfo.client_time = System.currentTimeMillis();
                            afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                            mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
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
//					addToCache(afMessageInfo);
                    //更新当前消息的状态
                    MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);

                }
            }

        }).start();
    }

    /**
     * 输入框内显示表情
     *
     * @param id   表情资源Id
     * @param data
     */
    public void setFace(int id, String data) {
        Drawable drawable;
        if (id != 0 && !CommonUtils.isDeleteIcon(id, vEditTextContent)) {
            drawable = getResources().getDrawable(id);
            drawable.setBounds(0, 0, CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
            ImageSpan span = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(data);
            spannableString.setSpan(span, 0, data.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            Log.e("ttt->", spannableString.length() + "->" + vEditTextContent.length());
            int length = spannableString.length() + vEditTextContent.length();
            if (length < DefaultValueConstant.MAX_SIZE) {
                int index = vEditTextContent.getSelectionStart();
                Editable editable = vEditTextContent.getText();
                editable.insert(index, spannableString);
            }
        }
    }

    /**
     * 删除输入框内的表情
     */
    public void emojj_del() {
        CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.chatting_send_button://send button
                sendTextOrEmotion();
                break;
            case R.id.chatting_operate_one://change voice button
            case R.id.btn_backkeyboard:
                toShowVoice();
                break;
            case R.id.btn_keyboard:
                vButtonKeyboard.setVisibility(View.GONE);
                emojjView.getViewRoot().setVisibility(View.GONE);
                chattingOptions.setVisibility(View.GONE);
                if (!(!CommonUtils.isEmpty(mFriendAfid) &&
                        mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS))) {//Palmchat team 不显示more和表情按钮
                    vImageEmotion.setVisibility(View.VISIBLE);
                    vImageViewMore.setVisibility(View.VISIBLE);
                }
                vButtonLeftKeyboard.setVisibility(View.GONE);
                showTalkView();
                vListView.setSelection(adapterListView.getCount());
                PalmchatLogUtils.i("chatsCount","btnkey:" + Integer.valueOf(adapterListView.getCount()));
                vEditTextContent.requestFocus();
                CommonUtils.showSoftKeyBoard(vEditTextContent);
                break;
            case R.id.image_emotion://emotion button
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                emojjView.getViewRoot().setVisibility(View.VISIBLE);
                vButtonKeyboard.setVisibility(View.VISIBLE);
                vImageEmotion.setVisibility(View.GONE);
                chattingOptions.setVisibility(View.GONE);
                vImageViewMore.setVisibility(View.VISIBLE);
                vButtonLeftKeyboard.setVisibility(View.GONE);
//			emojjView.getViewRoot().setFocusableInTouchMode(true);
                showTalkView();
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vListView.setSelection(adapterListView.getCount());
                    }
                },20);

                PalmchatLogUtils.i("chatsCount","image_emotion:" + Integer.valueOf(adapterListView.getCount()));
                break;


            case R.id.image_emotion2:
                toShowVoice();
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                emojjView.getViewRoot().setVisibility(View.VISIBLE);
                vButtonKeyboard.setVisibility(View.VISIBLE);
                vImageEmotion.setVisibility(View.GONE);
                chattingOptions.setVisibility(View.GONE);
                vImageViewMore.setVisibility(View.VISIBLE);
                vButtonLeftKeyboard.setVisibility(View.GONE);
//			emojjView.getViewRoot().setFocusableInTouchMode(true);
                showTalkView();
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                vListView.setSelection(adapterListView.getCount());
                break;

            case R.id.chatting_operate_two:// + button(more)
                vButtonKeyboard.setVisibility(View.GONE);
                vImageEmotion.setVisibility(View.VISIBLE);
                vImageViewMore.setVisibility(View.GONE);
                vButtonLeftKeyboard.setVisibility(View.VISIBLE);
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                mainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //防止同时显示多媒体菜单和软件盘
                        CommonUtils.closeSoftKeyBoard(vEditTextContent);
                        chattingOptions.setVisibility(View.VISIBLE);
                    }
                }, 200);

                emojjView.getViewRoot().setVisibility(View.GONE);
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                vListView.setSelection(adapterListView.getCount());
                break;

            case R.id.chatting_operate_two2:
                vButtonKeyboard.setVisibility(View.GONE);
                vImageEmotion.setVisibility(View.VISIBLE);
                toShowVoice();
                vImageViewMore.setVisibility(View.GONE);
                vButtonLeftKeyboard.setVisibility(View.VISIBLE);
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                chattingOptions.setVisibility(View.VISIBLE);
                emojjView.getViewRoot().setVisibility(View.GONE);
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                vListView.setSelection(adapterListView.getCount());

                break;
            case R.id.btn_leftkeyboard:
                chattingOptions.setVisibility(View.GONE);
                vImageViewMore.setVisibility(View.VISIBLE);
                vButtonLeftKeyboard.setVisibility(View.GONE);
                vButtonKeyboard.setVisibility(View.GONE);
                vImageEmotion.setVisibility(View.VISIBLE);
                //toShowVoice();
                emojjView.getViewRoot().setVisibility(View.GONE);
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                vListView.setSelection(adapterListView.getCount());
                vEditTextContent.requestFocus();
                CommonUtils.showSoftKeyBoard(vEditTextContent);
                break;

            case R.id.chatting_message_edit://onclick on eidttext
                closeEmotions();
                break;
            case R.id.back_button:
                //5.1 改为直接返回
                boolean hasKeyBoard = chat_root.hasKeyboard();
                if (hasKeyBoard) {
                    CommonUtils.closeSoftKeyBoard(vEditTextContent);
//				return;
                }
                back();
                break;
            case R.id.stranger_for_add:
                //gtf 2014-11-16
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CT_ADD_SUCC);
//                MobclickAgent.onEvent(context, ReadyConfigXML.CT_ADD_SUCC);

                String name = CacheManager.getInstance().getMyProfile().name;
                if (!TextUtils.isEmpty(name)) {
                    boolean addFriend_clickable = update_addFriend_clickable(mFriendAfid, Consts.HTTP_CODE_UNKNOWN);
                    if (addFriend_clickable) {
                        mAfCorePalmchat.AfHttpSendMsg(mFriendAfid, System.currentTimeMillis(), getString(R.string.want_to_be_friend).replace(DefaultValueConstant.TARGET_NAME, name), Consts.MSG_CMMD_FRD_REQ, String.valueOf(Consts.MSG_CMMD_FRD_REQ), Chatting.this);
                    }
                }
                if (!TextUtils.isEmpty(mFriendAfid) && !CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix)) {
                    // 判断是否已经关注过了，关注后不执行AddFollw();
                    if (!isFollow && !CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix) || isFollow && CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix)) {
                        AddFollw(mFriendAfid);
                    }
                }
                break;
            case R.id.stranger_head_icon:
            case R.id.frd_head_icon: {
                AfFriendInfo afFriendInfo = afFriend;
                if (afFriendInfo != null) {
                    toProfile(afFriendInfo);
                } else {
                    final AfFriendInfo friendInfo = CacheManager.getInstance().searchAllFriendInfo(mFriendAfid);
                    if (friendInfo != null) {
                        toProfile(friendInfo);
                    } else {
                        toProfileForAfid(mFriendAfid);
                    }
                }
                break;
            }
            case R.id.frd_accept: {
                //gtf 2014-11-16
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.NOTI_PLUS);
//                MobclickAgent.onEventEnd(context, ReadyConfigXML.NOTI_PLUS);
                boolean acceptFriend_clickable = update_AcceptFriend_clickable(mFriendAfid, Consts.HTTP_CODE_UNKNOWN);
                if (acceptFriend_clickable) {
//				mAfCorePalmchat.AfHttpSendMsg(mFriendAfid,System.currentTimeMillis(), null,Consts.MSG_CMMD_AGREE_FRD_REQ, mFriendAfid,Chatting.this);
                    mAfCorePalmchat.AfHttpSendMsg(mFriendAfid, System.currentTimeMillis(), null, Consts.MSG_CMMD_AGREE_FRD_REQ, String.valueOf(Consts.MSG_CMMD_AGREE_FRD_REQ), Chatting.this);
                    r_frd_add.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(mFriendAfid) && !CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix)) {
                    // 判断是否已经关注过了，关注后不执行AddFollw();
                    if (!isFollow && !CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix) || isFollow && CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix)) {
                        AddFollw(mFriendAfid);
                    }
                }
//			showProgressDialog(R.string.Sending);
                break;
            }
            case R.id.frd_inorge: {
                final MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(mFriendAfid);
                if (aff != null) {
                    sentLayout();
                    new Thread(new Runnable() {
                        public void run() {
                            MessagesUtils.removeFreqMsg(aff.afMsgInfo, true, true);
                            String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, aff.afFriendInfo.name, getString(R.string.want_to_be_friend_ignored));
                            aff.afMsgInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS;
                            aff.afMsgInfo.msg = aa;
                            //生成一条好友请求成功消息 插入数据库中。
                            int _id = mAfCorePalmchat.AfDbMsgInsert(aff.afMsgInfo);

                            aff.afMsgInfo._id = _id;
                            //向内存内插入新消息，后面两个参数表示是否向数据库或内存内插入消息
                            MessagesUtils.insertMsg(aff.afMsgInfo, false, true);
                        }
                    }).start();
                }
                break;
            }
            case R.id.img_picture:
            case R.id.img_picture_p: {
                if (CommonUtils.getSdcardSize()) {
                    ToastManager.getInstance().show(context, R.string.memory_is_full);
                    break;
                }

//			modify by zhh
                Intent mIntent = new Intent(this, PreviewImageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("size", DefaultValueConstant.MAX_SELECTED);
                mIntent.putExtras(mBundle);
                startActivityForResult(mIntent, ALBUM_REQUEST);
//			new PreviewImageDialog(Chatting.this, mainHandler,DefaultValueConstant.MAX_SELECTED).show();
                break;
            }
            case R.id.img_camera:
            case R.id.img_camera_c: {
                if (CommonUtils.getSdcardSize()) {
                    ToastManager.getInstance().show(context, R.string.memory_is_full);
                    break;
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraFilename = ClippingPicture.getCurrentFilename();
                Log.e("camera->", cameraFilename);
                SharePreferenceService.getInstance(Chatting.this).savaFilename(cameraFilename);
                imageFile = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
                Uri u = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                try {
                    startActivityForResult(intent, MessagesUtils.CAMERA);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
//				e.printStackTrace();
                    ToastManager.getInstance().show(context, R.string.no_camera);
                }
                break;
            }
            case R.id.img_voice:
            case R.id.img_voice_v: {
                toShowVoice();
                break;
            }
            case R.id.img_name_card:
            case R.id.img_name_card_n: {
                intent = new Intent(Chatting.this, SelectListActivity.class);
                intent.putExtra(JsonConstant.KEY_AFID, mFriendAfid);
                startActivityForResult(intent, 3);
                break;
            }
            case R.id.img_video:
            case R.id.img_video_v: {
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_T_SDG);
//                MobclickAgent.onEvent(context, ReadyConfigXML.PF_T_SDG);
                sendGift(getString(R.string.send_gift), 0, 0);
                break;
            }
            case R.id.img_background:
            case R.id.img_background_v: {
                //gtf 2.15-5-21
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CLI_GBD);
//                MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.CLI_GBD);

                PalmchatLogUtils.println("img_background");
                Intent intent = new Intent(Chatting.this, ActivityBackgroundChange.class);
                intent.putExtra(JsonConstant.KEY_FROM_UUID, mFriendAfid);
                startActivityForResult(intent, BACK_FROM_SET_CHAT_BG);
                break;
            }
            case R.id.view_unread:   //点击未读消息时  zhh
                hideUnreadTips();
                adapterListView.notifyDataSetChanged();
                vListView.setSelection(adapterListView.getCount());
                isBottom = true;
                break;
            case R.id.title_text:
                getOnlineStatusInfo();
                break;
            default:
                break;
        }
    }

    /**
     * 送花给好友
     *
     * @param title
     * @param user_data
     * @param number
     */
    private void sendGift(String title, final int user_data, final int number) {
        // TODO Auto-generated method stub
        closeEmotions();
        final AppDialog appDialog = new AppDialog(this);
        appDialog.createSendGiftDialog(this, title, null, number, new OnInputDialogListener() {
            @Override
            public void onRightButtonClick(String inputStr) {
                if (!TextUtils.isEmpty(inputStr)) {
                    int num = Integer.parseInt(inputStr);
                    int allFlower = CacheManager.getInstance().getMyProfile().dating.wealth_flower;
                    int left = allFlower - num;
                    PalmchatLogUtils.println("createSendGiftDialog  left  " + left);
                    if (num > 0) {
                        appDialog.dismiss();
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PNSD_FL);
//                        MobclickAgent.onEvent(context, ReadyConfigXML.PNSD_FL);
                        mAfCorePalmchat.AfHttpSendGift(mFriendAfid, num, System.currentTimeMillis(), user_data, Chatting.this);
                    } else {
                        ToastManager.getInstance().show(context, R.string.input_numer_flowers);
                    }
                } else {
                    ToastManager.getInstance().show(context, R.string.input_numer_flowers);
                }
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }

    /**
     * 跳转到好友profile
     *
     * @param afid
     */
    private void toProfileForAfid(final String afid) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.CHAT_TO_PROFILE);
        startActivityForResult(intent,1001);
    }


    /**
     * 显示录音布局
     */
    private void toShowVoice() {

        if (CommonUtils.getSdcardSize()) {
            ToastManager.getInstance().show(context, R.string.memory_is_full);
            return;
        }
        if (viewChattingVoiceLayout.getVisibility() == View.VISIBLE) {
            viewOptionsLayout.setVisibility(View.VISIBLE);
            viewChattingVoiceLayout.setVisibility(View.GONE);
            vEditTextContent.requestFocus();
            CommonUtils.showSoftKeyBoard(vEditTextContent);

            CacheManager.getInstance().putShowChattingVoice(mFriendAfid, false);

        } else {
            CommonUtils.closeSoftKeyBoard(vEditTextContent);
            viewOptionsLayout.setVisibility(View.GONE);
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewChattingVoiceLayout.setVisibility(View.VISIBLE);
                }
            }, 200);
            CacheManager.getInstance().putShowChattingVoice(mFriendAfid, true);
        }
        mCancelVoice.setText(R.string.call_Voice_Tips);
        mCancelVoice.setVisibility(View.VISIBLE);
        closeEmotions();
    }


    /**
     * 退出此activity
     */
    private void back() {
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }
//        adapterListView.exit();

        if (isForward) {
            if(TextUtils.isEmpty(forward_imagePath)) {
                Intent intent = new Intent(Chatting.this, MainTab.class);
                intent.putExtra(JsonConstant.KEY_TO_MAIN, true);
                startActivity(intent);
            }
            else{
                finish();
            }
        } else {
            if (mDirectChatting && TextUtils.isEmpty(forward_imagePath)) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(Process.myPid());
            } else {
                finish();
                PalmchatApp.getApplication().mMemoryCache.evictAll();

                if (back_to_default) {
                    return;
                }

                Stack<Activity> activityStack = MyActivityManager.getActivityStack();
                int acSize = activityStack.size();
                if (acSize > 0) {
                    for (int i = acSize - 1; i >= 0; i--) {
                        Activity act = activityStack.get(i);
                        if (!(act instanceof MainTab || act instanceof LaunchActivity || act instanceof ProfileActivity
                                || act instanceof MyProfileActivity || act instanceof ChattingRoomListActivity || act instanceof LocalSearchActivity
                                || act instanceof PalmcallActivity)) {
                            act.finish();
                        }
                    }
                }
            }
        }
    }

    /**
     * 发送文本或表情
     */
    private void sendTextOrEmotion() {
        String content = getEditTextContent();
        if (content.length() > 0) {
            if (EmojiParser.getInstance(PalmchatApp.getApplication())
                    .hasEmotions(content)) {
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);
//                MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_EMOTION);
            }
            PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_SENDMSG);
            vEditTextContent.setText("");
            sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_INSERT, null, content);
          /*  mFlowerAniView.startPlayPredict(null,testPoints , testPoints[testPointsWinIndex]) ;
            testPointsWinIndex++;
            if(testPointsWinIndex>5){
            	testPointsWinIndex=0;
            }*/
        }
    }
//    private int []testPoints=new int[]{100,500,1000,2000,10000,600000};
//    private static int testPointsWinIndex;
    /**
     * 重发文本或表情
     *
     * @param afMessageInfo
     */
    public void resendTextOrEmotion(AfMessageInfo afMessageInfo) {
        sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_UPDATE, afMessageInfo, afMessageInfo.msg);
    }

    /**
     * 发送消息
     *
     * @param content
     * @param msgInfo
     */
    private void send(String content, AfMessageInfo msgInfo) {
//		listChats.add(msgInfo);
        CommonUtils.getRealList(listChats, msgInfo);
        setAdapter(msgInfo);

        // heguiming 2013-12-04
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//        MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_TEXT);
//        MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.P_NUM);
        int code = -1;
        if (msgInfo.type == (AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_STORE_EMOTIONS)) {//应用商店表情
            code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.toAfId, System.currentTimeMillis(), content, Consts.MSG_CMMD_STORE_EMOTION, msgInfo._id, this);
        } else {//普通消息
            code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.toAfId, System.currentTimeMillis(), content, Consts.MSG_CMMD_NORMAL, msgInfo._id, this);
        }
        sendRequestError(msgInfo, code, Consts.SEND_MSG);
    }

    /**
     * 发送转发消息
     *
     * @param msgInfo
     */
    private void forwardRequest(AfMessageInfo msgInfo) {

        if (msgInfo.fid != null) {

            CommonUtils.getRealList(listChats, msgInfo);
            setAdapter(msgInfo);
            int code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.getKey(), System.currentTimeMillis(), msgInfo.fid, Consts.MSG_CMMD_FORWARD, msgInfo, Chatting.this);
            sendRequestError(msgInfo, code, Consts.SEND_MSG);

        } else {
            forwardFailure(msgInfo);
        }

    }

    /**
     * 转发失败
     *
     * @param msgInfo
     */
    private void forwardFailure(AfMessageInfo msgInfo) {

        PalmchatLogUtils.println("forwardFailure");


        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & msgInfo.type;
        switch (msgType) {
            case AfMessageInfo.MESSAGE_TEXT:
                mainHandler.obtainMessage(MessagesUtils.MSG_TEXT, msgInfo).sendToTarget();
                break;

            case AfMessageInfo.MESSAGE_IMAGE:
                mAfCorePalmchat.AfDbMsgRmove(msgInfo);
                AfAttachImageInfo afAttach = (AfAttachImageInfo) msgInfo.attach;
                String largeFileName = afAttach.large_file_name;
                mainHandler.obtainMessage(MessagesUtils.MSG_IMAGE, largeFileName).sendToTarget();
                break;

            case AfMessageInfo.MESSAGE_CARD:
                AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(msgInfo.msg);
                if (afFriendInfo == null) {
                    PalmchatLogUtils.println("AfMessageInfo.MESSAGE_CARD affriend = null");
                    return;
                }
                mAfCorePalmchat.AfDbMsgRmove(msgInfo);
                mainHandler.obtainMessage(MessagesUtils.MSG_CARD, afFriendInfo).sendToTarget();
                break;


        }

        mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgInfo._id, 0, null, Consts.AFMOBI_PARAM_FID);

    }


    /**
     * 转发失败后重新发送
     *
     * @param msgInfo
     */
    private void forwardFailureResend(AfMessageInfo msgInfo) {

        PalmchatLogUtils.println("forwardFailureResend");


        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & msgInfo.type;
        switch (msgType) {
            case AfMessageInfo.MESSAGE_TEXT:
                resendTextOrEmotion(msgInfo);
                break;

            case AfMessageInfo.MESSAGE_IMAGE:
                resendImage(msgInfo);

                break;

            case AfMessageInfo.MESSAGE_CARD:
                resendCard(msgInfo);
                break;

        }
        mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, msgInfo._id, 0, null, Consts.AFMOBI_PARAM_FID);
    }


    /**
     * 请求加好友失败
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
                case Consts.SEND_VOICE:
                    sendVoiceFailure(Consts.REQ_VOICE_SEND, Consts.REQ_CODE_UNNETWORK, msgInfo._id);
                    break;
                case Consts.SEND_IMAGE:
                    sendImageFailure(Consts.REQ_CODE_UNNETWORK, null, msgInfo);
                    break;

                default:
                    break;
            }

        }
        PalmchatLogUtils.println("sendRequestError  handle==" + handle + "==msg==" + msgInfo.msg + "==requestFlag==" + requestFlag);
    }


    /**
     * 更新消息状态(发送成功、失败、已读)
     *
     * @param msgId
     * @param status
     * @param fid
     */
    public void updateStatus(final int msgId, final int status, final String fid) {

        int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
        PalmchatLogUtils.println("index  " + index + "  msgId  " + msgId + "  status  " + status);
        if (index != -1 && index < adapterListView.getCount()) {
            AfMessageInfo afMessageInfo = adapterListView.getItem(index);
            afMessageInfo.status = status;
            afMessageInfo.fid = fid;
            PalmchatLogUtils.println("afMessageInof.fid:" + fid + "   afMessageInfo.msgId  " + afMessageInfo._id + "  afMessageInfo.status  " + afMessageInfo.status);
            adapterListView.notifyDataSetChanged();
        }
        new StatusThead(msgId, status, fid).start();

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

//			update recent chats msg status 
            AfMessageInfo msg = mAfCorePalmchat.AfDbMsgGet(msgId);


            if (msg == null) {

                PalmchatLogUtils.println("Chatting AfDbMsgGet msg:" + msg);
                return;
            }

            PalmchatLogUtils.println("---www : StatusThead isLast msg " + msgId + " msg status: " + status);
            MessagesUtils.setRecentMsgStatus(msg, status);

            mainHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();


            PalmchatLogUtils.println("update status msg_id " + _id);
        }
    }


    /**
     * 更新图片消息状态
     *
     * @param msgId
     * @param status
     * @param fid
     * @param afAttachImageInfo
     */
    public void updateImageStatus(int msgId, int status, String fid, AfAttachImageInfo afAttachImageInfo) {
        int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
//		PalmchatLogUtils.i(TAG, "找到第几�?-> " + index + "->" + talkList.getSelectedItemPosition(),true);
        if (index != -1 && index < adapterListView.getCount()) {
            AfMessageInfo afMessageInfo = adapterListView.getItem(index);
            PalmchatLogUtils.println("updateImageStatus  afMessageInfo  " + afMessageInfo);
            afMessageInfo.status = status;
            afMessageInfo.attach = afAttachImageInfo;
            afMessageInfo.fid = fid;
        }

    }


    /**
     * 通过广播更新发送的图片状态
     *
     * @param msgId
     * @param status
     * @param fid
     * @param progress
     */
    public void updateImageStautsByBroadcast(final int msgId, int status, String fid, int progress) {
        int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
        if (msgId != -1 && index == -1) {
            AfMessageInfo msg = CacheManager.getInstance().getAfMessageInfo();
            if (msg != null && mFriendAfid.equals(msg.toAfId)) {
                if (msg != null && msgId == msg._id) {
                    PalmchatLogUtils.println("Chatting  updateImageStautsByBroadcast  ");
                    CommonUtils.getRealList(adapterListView.getLists(), msg);
                    CacheManager.getInstance().setAfMessageInfo(null);

                    updateImageStautsByBroadcast(msgId, status, fid, progress);
                } else {
                    new Thread(new Runnable() {
                        public void run() {
                            PalmchatLogUtils.println("Chatting  Thread  updateImageStautsByBroadcast  ");
                            AfMessageInfo msg = mAfCorePalmchat.AfDbMsgGet(msgId);
                            if (msg != null) {
                                CommonUtils.getRealList(adapterListView.getLists(), msg);
                                mainHandler.obtainMessage(MessagesUtils.MSG_CAMERA_NOTIFY, msg).sendToTarget();
                            }
                        }
                    }).start();
                }
            }
        } else if (index != -1 && index < adapterListView.getCount()) {
            AfMessageInfo afMessageInfo = adapterListView.getItem(index);
            if (afMessageInfo != null && mFriendAfid.equals(afMessageInfo.toAfId)) {
                PalmchatLogUtils.println("updateImageStatusByBroadcast  afMessageInfo  " + afMessageInfo);
                afMessageInfo.status = status;
                afMessageInfo.fid = fid;
                AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                afAttachImageInfo.progress = progress;
                adapterListView.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("code " + code + "  flag  " + flag + "  result  " + result + "  user_data  " + user_data);

        int msg_id;
        if (Consts.REQ_CODE_SUCCESS == code) {
            Log.d(TAG, "====:" + flag);
            switch (flag) {
                case Consts.REQ_MSG_SEND:
//				friend req or accept frd req msg
//				好友请求 ， 同意好友请求 
                    if (user_data instanceof String) {
                        final String cmd = (String) user_data;
                        int req = Integer.parseInt(cmd);
                        final String afid = mFriendAfid;
                        AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;

//					 MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(afid);
//					 send friend request
                        if (req == Consts.MSG_CMMD_FRD_REQ) {
                            update_addFriend_clickable(afid, code);
                            mainHandler.obtainMessage(Constants.DB_DELETE).sendToTarget();
                            AfMessageInfo afMessageInfo = MessagesUtils.addMsg2Chats(core, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
                            CommonUtils.getRealList(listChats, afMessageInfo);
                            setAdapter(afMessageInfo);
                            sendFriendReq(code, afid, req);

//                  accept friend request
                        } else {
//						core.AfHttpFriendOpr("all", afid,Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE,(byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, afid, this);
                            core.AfHttpFriendOpr("all", afid, Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE, Consts.AFMOBI_FRIEND_TYPE_FF, null, req, this);
                        }

//					私聊类型消息发送结果 回调
                    } else if (user_data instanceof Integer) {
                        msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
                        PalmchatLogUtils.println("onresult msg_id  " + msg_id);
                        if (msg_id > DefaultValueConstant.LENGTH_0) {
                            String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
                            if (!isPublicMember) {
                                sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, fid);
                            } else {
                                sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT_AND_READ, fid);
                            }
                        } else if (msg_id == Consts.REQ_CODE_READ) {
                            PalmchatLogUtils.println("case Consts.REQ_CODE_READ code " + code + "  flag  " + flag);
                        } else {
                            //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                            ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                            PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
                        }
                    }

                    break;
                case Consts.REQ_SEND_GIFT:
                    int userData = (Integer) user_data;
                    if (userData == 0) {
                        AfDatingInfo[] afDatingInfos = (AfDatingInfo[]) result;
                        PalmchatLogUtils.println("afDatingInfos  " + afDatingInfos);
                        if (afDatingInfos != null && afDatingInfos.length > 1) {

                            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PFSD_FL_SUCC);
//                            MobclickAgent.onEvent(context, ReadyConfigXML.PFSD_FL_SUCC);

                            AfDatingInfo myDatingInfo = afDatingInfos[0];
                            AfDatingInfo otherDatingInfo = afDatingInfos[1];

							/*	PalmchatLogUtils.println("myDatingInfo  "+myDatingInfo);
                                PalmchatLogUtils.println("otherDatingInfo  "+otherDatingInfo);*/
                            ToastManager.getInstance().show(context, R.string.success);
                            playFlowersAnimationImmediately(null);
                            AfProfileInfo myProfileInfo = CacheManager.getInstance().getMyProfile();
                            myProfileInfo.dating = myDatingInfo;
                        } else {
                            ToastManager.getInstance().show(context, R.string.failure);
                        }
                    }
                    break;
//				发送语音消息
                case Consts.REQ_VOICE_SEND:
                    msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
                    if (msg_id > DefaultValueConstant.LENGTH_0) {
                        sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, DefaultValueConstant.MSG_INVALID_FID);
                    } else {
                        //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                        ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                        PalmchatLogUtils.println("case Consts.REQ_VOICE_SEND code " + code + "  flag  " + flag);
                    }
                    break;
                case Consts.REQ_MEDIA_INIT://media init
                {

                    break;
                }
//			上传图片附件
                case Consts.REQ_MEDIA_UPLOAD://media upload
                {
                	AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
                	SendImageEvent event=new SendImageEvent(afMessageInfo,0,true,result);
    				EventBus.getDefault().post(event);
    				 //这里只所以要搞EventBus事件 是因为 当在一个聊天里发了一张图没发完的时候 再退出  再进 百分比不会动了， 是因为已经不是同一个对象了			
    				uploadMediaCallBack(event); //这里要再调一次 是因为当前可能没有开群聊或则私聊界面 所以这个时候也必须处理
                    
                    break;
                }
                case Consts.REQ_GET_INFO://get profile(info) success
                {
                    AfProfileInfo afProfileInfo = (AfProfileInfo) result;

                    AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
                    if (afProfileInfo != null && afMessageInfo != null) {
                        CacheManager.getInstance().saveOrUpdateFriendProfile(afProfileInfo);
                        AfFriendInfo afFriendInfo = AfProfileInfo.friendToProfile(afProfileInfo);
                        afMessageInfo.attach = afFriendInfo;
//					listChats.add(afMessageInfo);
                        CommonUtils.getRealList(listChats, afMessageInfo);
                        showVibrate();
                        setAdapter(null);

                    } else {
                        //get profile info failure
                    }
                    break;
                }

//			forward msg
                case Consts.REQ_MSG_FORWARD: {
                    PalmchatLogUtils.println("REQ_MSG_FORWARD success");
//				update send msg ui
                    msg_id = user_data != null ? ((AfMessageInfo) user_data)._id : DefaultValueConstant.LENGTH_0;
                    PalmchatLogUtils.println("onresult msg_id  " + msg_id);
                    if (msg_id > DefaultValueConstant.LENGTH_0) {
                        sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, ((AfMessageInfo) user_data).fid);
                    } else {
                        //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                        ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                        PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
                    }

                    break;
                }
                //同意好友请求
                case Consts.REQ_FRIEND_LIST: {
//				 String afid = (String) user_data;
                    String afid = mFriendAfid;
                    int req = (Integer) user_data;
                    AfPalmchat core = PalmchatApp.getApplication().mAfCorePalmchat;
                    if (req == Consts.MSG_CMMD_AGREE_FRD_REQ) {
                        MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(afid);
                        //update_AcceptFriend_clickable(afid, Consts.HTTP_CODE_UNKNOWN);
                        PalmchatLogUtils.i("ChattingHash","result: " + this.hashCode()+"========" + r_frd_add.hashCode());
                        Activity activity = MyActivityManager.getScreenManager().getCurrentActivity();//获取当前活动的控件，不然的话关闭的只是上次的控件，当前的没有关闭
                        if(activity != null && !activity.equals(this)){
                            Chatting chatting = (Chatting) activity;
                            if(chatting != null){
                                chatting.setAddButton();
                            }
                        }else{
                            r_frd_add.setVisibility(View.GONE);
                        }
                        new_friend_insert(core, aff,true);
                    } else {
                        update_addFriend_clickable(afid, code);
                        mainHandler.sendMessage(mainHandler.obtainMessage(Constants.DB_DELETE));
                        AfPalmchat afPalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
                        AfMessageInfo afMessageInfo = MessagesUtils.addMsg2Chats(afPalmchat, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
                        CommonUtils.getRealList(listChats, afMessageInfo);
                        setAdapter(afMessageInfo);
                    }

                    sendFriendReq(code, afid, req);

                }
                break;
//				下载动态表情
                case Consts.REQ_STORE_DOWNLOAD: {
                    final String item_id = (String) user_data;
                    new Thread() {
                        public void run() {

                            String savePath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, item_id)).append(item_id).append(Constants.STORE_DOWNLOAD_TMP).toString();
                            File tmpfile = new File(savePath);

                            String gifFolderPath = null;
                            if (tmpfile.exists()) {
                                String zipPath = new StringBuffer(CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, item_id)).append(item_id).append(Constants.STORE_DOWNLOAD_COMPLETE).toString();
                                File zipFile = new File(zipPath);
                                tmpfile.renameTo(zipFile);

                                gifFolderPath = CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, item_id);
                                try {
                                    if (zipFile.exists()) {
                                        ZipFileUtils.getInstance().upZipFile(zipFile, gifFolderPath);
                                        FileUtils.renameTo(gifFolderPath + "/" + CacheManager.getInstance().getScreenString() + "/");
                                    }

                                } catch (ZipException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    zipFile.delete();
                                }
                            }

//						mAfCorePalmchat.AfDBPaystoreProdinfoInsert(item_id, small_url, tv_face_name.getText().toString().trim(), "", 100, afcoin, System.currentTimeMillis(),ver_code);
                            if (item_id != null && CacheManager.getInstance().getItemid_update().containsKey(item_id)) {
                                AfProdProfile afProdProfile = CacheManager.getInstance().getItemid_update().get(item_id);
                                int ver_code = afProdProfile.ver_code;
                                mAfCorePalmchat.AfDBPaystoreProdinfoUpdate(item_id, ver_code, System.currentTimeMillis());
                            }

                            Params params = new Params();
                            params.itemId = item_id;
                            params.gifFolderPath = gifFolderPath;
                            mainHandler.obtainMessage(STORE_FACE_DOWNLOAD, params).sendToTarget();

                        }
                    }.start();
                    break;
                }
                //获取朋友在线状态
                case Consts.REQ_GET_ONLINE_STATUS_NEW: {
                    AfOnlineStatusInfo afOnlineStatusInfo = (AfOnlineStatusInfo) result;
                    if (afOnlineStatusInfo != null) {
                        AfOnlineStatusInfo onlineStatusInfo = new AfOnlineStatusInfo();
                        onlineStatusInfo.status = afOnlineStatusInfo.status;
                        onlineStatusInfo.ts = System.currentTimeMillis();
                        onlineStatusInfo.datetime = afOnlineStatusInfo.datetime;
                        mainHandler.obtainMessage(ONLINE_STATUS, onlineStatusInfo).sendToTarget();
                    }
                }
                case Consts.REQ_BC_SHARE_BROADCAST:
                case Consts.REQ_BC_SHARE_TAG:
                    msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
                    if (msg_id > DefaultValueConstant.LENGTH_0) {
                        AfResponseComm.AfTagShareTagOrBCResp afResponseComm = (AfResponseComm.AfTagShareTagOrBCResp) result;
                        AfMessageInfo msg = mAfCorePalmchat.AfDbMsgGet(msg_id);
                        if (afResponseComm != null) {
                            AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) msg.attach;
                            MessagesUtils.updateShareTagMsgPostNumber(msg,afAttachPAMsgInfo._id);
                        }
                        sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, DefaultValueConstant.MSG_INVALID_FID);
                    }
                    break;
                case Consts.REQ_FLAG_FOLLOW_LIST: // 关注 或取消关注
                    boolean isFollow = (Boolean) user_data;
                    if (isFollow) {// 取消关注一个人
                        MessagesUtils.onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afFriend);
                    } else {// 关注一个人
                        MessagesUtils.onAddFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afFriend);
                        //MessagesUtils.addMsg2Chats(mAfPalmchat, afid, MessagesUtils.ADD_CHATS_FOLLOW);
                        ToastManager.getInstance().show(context, R.string.followed);
                    }
                    Update_IsFollow();
                    break;
                default:
                    break;
            }

        } else {
            PalmchatLogUtils.i("ChattingHash","result: " + this.hashCode()+"");
//			dismissProgressDialog();
            PalmchatLogUtils.e("Chatting AfOnResult", "code " + code + "  flag  " + flag + "  result  " + result + "  user_data  " + user_data);
            boolean isRead = false;
            switch (flag) {
                case Consts.REQ_MSG_SEND:

                    if (user_data instanceof String) {

                        final String cmd = (String) user_data;
                        int req = Integer.parseInt(cmd);
                        final String afid = mFriendAfid;
                        /*update_AcceptFriend_clickable(mFriendAfid, code);*/
                        /*update_addFriend_clickable(mFriendAfid, code);*/

                        sendFriendReq(code, afid, req);
                    }
                    isRead = sendMsgFailure(flag, code, user_data);
                    break;
                case Consts.REQ_VOICE_SEND:
                    sendVoiceFailure(flag, code, user_data);
                    break;
                case Consts.REQ_MEDIA_INIT://media init 
                case Consts.REQ_MEDIA_UPLOAD://media upload
                {
                    sendImageFailure(code, result, user_data);
                    break;
                }
                case Consts.REQ_BC_SHARE_TAG:
                case Consts.REQ_BC_SHARE_BROADCAST:
                    sendMsgFailure(flag, code, user_data);
                    break;
                case Consts.REQ_GET_INFO://get profile(info) failure
                {

                    break;
                }

//			forward msg
                case Consts.REQ_MSG_FORWARD: {

                    if (code != Consts.REQ_CODE_UNNETWORK) {

                        if (user_data != null && user_data instanceof AfMessageInfo) {
                            forwardFailureResend((AfMessageInfo) user_data);
                            return;
                        }
                    }

                    String user_data2 = String.valueOf(((AfMessageInfo) user_data)._id);
                    sendMsgFailure(flag, code, user_data2);

                    break;
                }
                case Consts.REQ_STORE_DOWNLOAD_PRE:
                case Consts.REQ_STORE_DOWNLOAD: {
                    String item_id = (String) user_data;
                    PalmchatLogUtils.println("REQ_STORE_DOWNLOAD item_id:" + item_id);
                    if (CacheManager.getInstance().getStoreDownloadingMap().containsKey(item_id)) {
                        CacheManager.getInstance().getStoreDownloadingMap().remove(item_id);
                    }

                    setShowOrNot(item_id);
                    break;
                }

                case Consts.REQ_FRIEND_LIST:
                    String afid = mFriendAfid;
                    int req = (Integer) user_data;

//				String afid = (String) user_data;
//				final MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(afid);
                    if (req == Consts.MSG_CMMD_AGREE_FRD_REQ) {
                        update_AcceptFriend_clickable(afid, Consts.HTTP_CODE_UNKNOWN);
                    } else {
                        update_addFriend_clickable(afid, code);
                    }

                    sendFriendReq(code, afid, req);

                    break;
                case Consts.REQ_SEND_GIFT:
                    Consts.getInstance().showToast(context, code, flag, http_code);
                    break;
                case Consts.REQ_FLAG_FOLLOW_LIST:
                    boolean isFollow = (Boolean) user_data;
                    String msg = "";
                    if (isFollow) {
                        msg = getString(R.string.unfollow_failure).replace("XXXX", afFriend.name);
                    } else {
                        msg = getString(R.string.follow_failure).replace("XXXX", afFriend.name);
                    }
                    ToastManager.getInstance().show(context, msg);
                    Update_IsFollow();
                    break;
                default:
                    break;
            }
            if (Consts.REQ_CODE_MEDIA_SESSTION_TIMEOUT != code && !isRead) {
                if (CommonUtils.isChatting(this)) {
                    dismissProgressDialog();
                    Consts.getInstance().showToast(context, code, flag, http_code);
                }
            }
        }
    }

    /**
     * 发送好友
     *
     * @param code
     * @param afid
     * @param flagReq
     */
    private void sendFriendReq(int code, String afid, int flagReq) {
        if (mFriendAfid != null && mFriendAfid.equals(afid)) {
            if (code == Consts.REQ_CODE_SUCCESS) {
                sentLayout();
            } else {
                if (flagReq == Consts.MSG_CMMD_AGREE_FRD_REQ) {
                    set_AcceptFriend_clickable(afid, code);
                } else {
                    set_addFriend_clickable(afid, code);
                }
            }

        }
    }

    /**
     * 添加好友成功处理
     *
     * @param core
     * @param aff
     * @param isFriendRequest true好友请求加好友
     */
    private void new_friend_insert(final AfPalmchat core, final MainAfFriendInfo aff,final boolean isFriendRequest) {

        Log.d(TAG, "===:" + "new_friend_insert");

        if (aff == null || aff.afFriendInfo == null || aff.afFriendInfo.afId == null) {
            PalmchatLogUtils.e(TAG, "aff==null||aff.afFriendInfo==null ");
            return;
        }

        String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
                aff.afFriendInfo.name, context.getString(R.string.frame_toast_friend_req_success));
        aff.afMsgInfo.msg = aa;
        new Thread() {
            public void run() {
                // heguiming 2013-12-04
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PF_ADD_SUCC);
//                MobclickAgent.onEvent(context, ReadyConfigXML.PF_ADD_SUCC);

                MessagesUtils.onAddFriendSuc(aff.afFriendInfo);
                //添加好友成功。删除好友请求,刷新好友列表
                PalmchatLogUtils.e("WXL", "removeFreqMsg add");
                MessagesUtils.removeFreqMsg(aff.afMsgInfo, true, true);
                aff.afMsgInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS;

                if(isFriendRequest) {
                    //生成一条好友请求成功消息 插入数据库中。
                    int _id = core.AfDbMsgInsert(aff.afMsgInfo);

                    aff.afMsgInfo._id = _id;
                    aff.afMsgInfo.unReadNum = 0;
                    //向内存内插入新消息，后面两个参数表示是否向数据库或内存内插入消息
                    MessagesUtils.insertMsg(aff.afMsgInfo, false, true);
                   /* aff.afMsgInfo.unReadNum = 0;
                    CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).update(aff, false, true);*/
                }
                mainHandler.obtainMessage(Constants.TO_REFRESH_FRIEND_LIST, aff.afMsgInfo).sendToTarget();
            }

        }.start();

    }


    /**
     * send image error
     *
     * @param code
     * @param result
     * @param user_data
     */
    private void sendImageFailure(int code, Object result, Object user_data) {
        final AfImageReqInfo afImageReqInfo = (AfImageReqInfo) result;
        AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
        if (Consts.REQ_CODE_MEDIA_SESSTION_TIMEOUT == code) {
            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
            AfImageReqInfo imageReqInfo = afAttachImageInfo.upload_info;
            imageReqInfo.server_dir = null;
            imageReqInfo.server_name = null;
            afAttachImageInfo.upload_info = imageReqInfo;
            afAttachImageInfo.progress = 0;
            updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo);
            sendImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
            int handle = mAfCorePalmchat.AfHttpSendImage(afAttachImageInfo.upload_info, afMessageInfo, this, this);
            sendRequestError(afMessageInfo, handle, Consts.SEND_IMAGE);
        } else if (afImageReqInfo != null && afMessageInfo != null) {
            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
            if (afAttachImageInfo != null) {
                AfImageReqInfo imageReqInfo = afAttachImageInfo.upload_info;
                afImageReqInfo._id = imageReqInfo._id;
                afAttachImageInfo.upload_info = afImageReqInfo;

                updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo);
                sendImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
//						updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, afAttachImageInfo);
                new Thread(new Runnable() {
                    public void run() {
                        PalmchatLogUtils.println("afImageReqInfo._id  " + afImageReqInfo._id);
                        mAfCorePalmchat.AfDbImageReqUPdate(afImageReqInfo);//mAfCorePalmchat.AfDbImageReqUPdate(afAttachImageInfo.upload_info);
                    }
                }).start();
                new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
            } else {
                PalmchatLogUtils.println("Consts.REQ_MEDIA_UPLOAD  afAttachImageInfo  " + afAttachImageInfo);
            }
        } else {
            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
            updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo);
            sendImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
            new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
        }
    }


    /**
     * send voice error
     *
     * @param flag
     * @param code
     * @param user_data
     */
    private void sendVoiceFailure(int flag, int code, Object user_data) {
        int msg_id;
        msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
        if (msg_id > DefaultValueConstant.LENGTH_0) {
            sendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
        } else {
            //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
            ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
            PalmchatLogUtils.println("else Consts.REQ_VOICE_SEND else code " + code + "  flag  " + flag);
        }
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
     * 录音之后，通知发送语音
     *
     * @param msg_id
     * @param status
     * @param fid
     */
    void sendTextOrVoice(int msg_id, int status, String fid) {
        updateStatus(msg_id, status, fid);

    }


    /**
     * 从图库选择图片后，发送图片
     *
     * @param msg_id
     * @param status
     * @param fid
     * @param progress
     */
    void sendImage(int msg_id, int status, String fid, int progress) {
        updateImageStautsByBroadcast(msg_id, status, fid, progress);

    }

    /**
     * 通过相机照相之后，发送图片
     */
    void sendCamera() {
        System.out.println("MessagesUtils.CAMERA_MESSAGE.equals(action)");
        ArrayList<AfMessageInfo> tmpList = CacheManager.getInstance().getmImgSendList();
        for (AfMessageInfo msg : tmpList) {
            mainHandler.obtainMessage(MessagesUtils.SEND_IMAGE, msg).sendToTarget();
        }
        CacheManager.getInstance().clearmImgSendList();
    }

    /**wxl20160419
	 * 这里只所以要搞EventBus事件 是因为 当在一个聊天里发了一张图没发完的时候 再退出  再进 百分比不会动了， 是因为已经不是同一个对象了
	 * @param event
	 */
	public void onEventMainThread(SendImageEvent event) {
		uploadMediaCallBack(event); 
	}
		/**wxl20160419
		 * 处理上传图片的返回
		 * @param event
		 */
		private void uploadMediaCallBack( SendImageEvent event ){	
			AfMessageInfo afMessageInfo = event.getAfMessageInfo();
		 if(event.isFinished()){
			  if (afMessageInfo != null) {
                 final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                 if (afAttachImageInfo != null) {
                     String fid = (event.getResult() != null && event.getResult() instanceof String) ? event.getResult().toString() : DefaultValueConstant.MSG_INVALID_FID;
                     updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid, afAttachImageInfo);
                     sendImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, afMessageInfo.fid, afAttachImageInfo.progress);
                     new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid).start();
                     new Thread(new Runnable() {
                         public void run() {
                             PalmchatLogUtils.println("Consts.REQ_MEDIA_UPLOAD  success  " + afAttachImageInfo.upload_id);
                             mAfCorePalmchat.AfDbImageReqRmove(afAttachImageInfo.upload_id);
                         }
                     }).start();
                 } else {
                     PalmchatLogUtils.println("Consts.REQ_MEDIA_UPLOAD  afAttachImageInfo  " + afAttachImageInfo);
                 }
             } else {
                 PalmchatLogUtils.println("Consts.REQ_MEDIA_UPLOAD  afMessageInfo  " + afMessageInfo);
             }
		 }else{
			 if (afMessageInfo != null) {
                 final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                 afAttachImageInfo.progress = event.getProgress();
                 if (event.getProgress() == 100) {
                     //updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT,afAttachImageInfo);
                     PalmchatLogUtils.println("100 progress  " + event.getProgress());
                 } else {
                     PalmchatLogUtils.println("progress  " + event.getProgress());
                     updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo);
                     sendImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo.progress);
                 }

                 new Thread(new Runnable() {
                     public void run() {
                         mAfCorePalmchat.AfDbAttachImageUpdateProgress(afAttachImageInfo._id, afAttachImageInfo.progress);
                     }
                 }).start();
             }
		 } 
	}
    @Override
    public void AfOnProgress(int httpHandle, int flag, final int progress, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("ywp: AfhttpHandleOnProgress httpHandle =" + httpHandle + " flag=" + flag + " progress=" + progress + " user_data = " + user_data);
        switch (flag) {
//		更新图片上传进度
            case Consts.REQ_MEDIA_UPLOAD:
            	final AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
            	SendImageEvent event=new SendImageEvent(afMessageInfo,progress,false,null);
    			EventBus.getDefault().post(event);
    			 //wxl20160419这里只所以要搞EventBus事件 是因为 当在一个聊天里发了一张图没发完的时候 再退出  再进 百分比不会动了， 是因为已经不是同一个对象了			
    			uploadMediaCallBack(event); //wxl20160419这里要再调一次 是因为当前可能没有开群聊或则私聊界面 所以这个时候也必须处理
                break;
//			更新动态表情下载进度
            case Consts.REQ_STORE_DOWNLOAD:
                String itemId = (String) user_data;
//			the same item, same progress, no need to refresh  
                if (CacheManager.getInstance().getStoreDownloadingMap().get(itemId) == progress) {
                    return;
                }
                CacheManager.getInstance().getStoreDownloadingMap().put(itemId, progress);
                //EventBus.getDefault().post(new EmoticonDownloadEvent(itemId, progress, false,false,JsonConstant.BROADCAST_STORE_FRAGMENT_MARK,0));
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PalmchatLogUtils.println("onActivityResult--->>" + requestCode + "  data  " + data);
       
        emojjView.getViewRoot().setVisibility(View.GONE);
        chattingOptions.setVisibility(View.GONE);
        vButtonLeftKeyboard.setVisibility(View.GONE);
        vButtonKeyboard.setVisibility(View.GONE);
        vImageEmotion.setVisibility(View.VISIBLE);
        vImageViewMore.setVisibility(View.VISIBLE);
//		refresh say hi, back from profile
        if (requestCode == 1001) {
        	AfFriendInfo afF = CacheManager.getInstance().searchAllFriendInfo(mFriendAfid);
        	if(null!=afF) {
                if (null != CacheManager.getInstance().getFriendsCacheSortListEx(
                        Consts.AFMOBI_FRIEND_TYPE_BF).search(afF, false, true)) {
                    if (mIsFromProfile) {
                        MyActivityManager.getScreenManager().popActivity(ProfileActivity.class);
                    }
                    finish();
                }
                //若别名不为空，则显示别名
                if (!TextUtils.isEmpty(afF.alias)) {
                    mFriendName = afF.alias;
                    mTextViewTitle.setText(mFriendName);
                }
            }
            /*else{ //现在没有Say hi了，不需要刷新chatting了
                new GetDataTask(false).execute();
        	}
*/

        } else if (requestCode == BACK_FROM_SET_CHAT_BG) {
            setChatBg();
            String background_name = SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getBackgroundForAfid(CacheManager.getInstance().getMyProfile().afId, mFriendAfid);
            if ("background0".equals(background_name)) {
                //gtf 2.15-5-21
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SEL_DEFAU);
//                MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.SEL_DEFAU);
            } else {
                //gtf 2.15-5-21
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SEL_NO_DEFAU);
//                MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.SEL_NO_DEFAU);
            }
        }
        else if(requestCode == IntentConstant.REQUEST_CODE_CHATTING){
            if(adapterListView != null){
                adapterListView.notifyDataSetChanged();
            }
        }

        if (resultCode == 0) {
            return;
        }
        if (requestCode == MessagesUtils.CAMERA) {
            PalmchatLogUtils.println("rotation==" + getWindowManager().getDefaultDisplay().getRotation());
            try {
                if (cameraFilename == null) {
                    cameraFilename = SharePreferenceService.getInstance(this).getFilename();
                    imageFile = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
                } else {
                    SharePreferenceService.getInstance(this).clearFilename();
                }
                cameraFilename = imageFile.getAbsolutePath();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                if (imageFile != null && cameraFilename != null) {

                    String tempStr = smallImage(imageFile, MessagesUtils.CAMERA);
                    if (tempStr == null || TextUtils.isEmpty(tempStr)) {
                        return;
                    }
                    cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                    cameraFilename = BitmapUtils.imageCompressionAndSave(imageFile.getAbsolutePath(), cameraFilename);
                    PalmchatLogUtils.e(TAG, "----cameraFilename:" + cameraFilename);
                    if (CommonUtils.isEmpty(cameraFilename)) {
                        mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
                        return;
                    }
                    PalmchatLogUtils.println("cameraFilename  " + cameraFilename);
                    mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == MessagesUtils.PICTURE) {
            if (data != null) {
                String path = "";
                Cursor cursor = null;
                Uri originalUri = data.getData();
                if (originalUri == null) {
                    return;
                }
                if (DefaultValueConstant.FILEMANAGER.equals(originalUri.getScheme())) {
                    path = originalUri.getEncodedPath();
                } else {
                    cursor = getContentResolver().query(originalUri, null, null, null, null);
                    if (cursor == null || !cursor.moveToFirst()) {
//					ToastManager.getInstance().show(context, );
                        PalmchatLogUtils.println("onActivityResult  cursor  " + cursor + "  cursor.moveToFirst()  false");
                        return;
                    }
                    String authority = originalUri.getAuthority();

                    if (!CommonUtils.isEmpty(authority) && authority.contains(DefaultValueConstant.FILEMANAGER)) {
                        path = cursor.getString(0);
                    } else {
                        path = cursor.getString(1);
                    }
                }
                PalmchatLogUtils.e("path=", path); // 这个就是我们想要的原图的路径
                if (cursor != null) {
                    cursor.close();
                }
                imageFile = new File(path);
                File f2 = FileUtils.copyToImg(imageFile.getAbsolutePath());
                if (f2 != null) {
                    imageFile = f2;
                }
                cameraFilename = f2.getAbsolutePath();//imageFile.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                smallImage(imageFile, MessagesUtils.PICTURE);
                cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                cameraFilename = BitmapUtils.imageCompressionAndSave(imageFile.getAbsolutePath(), cameraFilename);
                if (CommonUtils.isEmpty(cameraFilename)) {
                    mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
                    return;
                }

                PalmchatLogUtils.e("cameraFilename=", cameraFilename);
                mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
            }

        } else if (resultCode == 10) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            if (bundle != null) {
//				String tt = bundle.getString(Chatting.TT);
//				if(RECOMMEND.equals(tt)){
                AfFriendInfo afFriendInfo = (AfFriendInfo) bundle.getSerializable(JsonConstant.KEY_FRIEND);
                PalmchatLogUtils.println("afFriendInfo  " + afFriendInfo);
                mainHandler.obtainMessage(MessagesUtils.MSG_CARD, afFriendInfo).sendToTarget();
//				}
            }
        } else if (resultCode == 11) {
            if (data == null) return;
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                int msg_id = bundle.getInt(JsonConstant.KEY_ID, -1);
                boolean flag = bundle.getBoolean(JsonConstant.KEY_FLAG, false);
                String url = bundle.getString(JsonConstant.KEY_URL);
                int large_file_size = bundle.getInt(JsonConstant.KEY_FILESIZE, -1);
                String large_file_name = bundle.getString(JsonConstant.KEY_FILENAME);
                String small_file_name = bundle.getString(JsonConstant.KEY_LOCAL_IMG_PATH);
                int index = ByteUtils.indexOf(adapterListView.getLists(), msg_id);
                if (index != -1 && index < adapterListView.getCount()) {
                    AfMessageInfo afMessageInfo = adapterListView.getItem(index);
                    PalmchatLogUtils.println("updateImageStatus  afMessageInfo  " + afMessageInfo);
                    AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                    afAttachImageInfo.url = url;
                    afAttachImageInfo.large_file_size = large_file_size;
                    afAttachImageInfo.large_file_name = large_file_name;
                    afAttachImageInfo.small_file_name = small_file_name;
                    afMessageInfo.attach = afAttachImageInfo;
                    adapterListView.getLists().set(index, afMessageInfo);
                }
            }
        } 

		/*add by zhh 从相册页面返回*/
        if (requestCode == ALBUM_REQUEST && resultCode == Activity.RESULT_OK) { // 相册页面拍照返回
            if (data != null) {
                File f;
                try {
                    String cameraFilename = data.getStringExtra("cameraFilename");
                    if (cameraFilename == null) {
                        cameraFilename = SharePreferenceService.getInstance(this).getFilename();
                    } else {
                        SharePreferenceService.getInstance(this).clearFilename();
                    }
                    f = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
                    cameraFilename = f.getAbsolutePath();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                    if (f != null && cameraFilename != null) {

                        String tempStr = smallImage(f, MessagesUtils.CAMERA);
                        if (tempStr == null || TextUtils.isEmpty(tempStr)) {
                            return;
                        }
                        cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                        cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
                        PalmchatLogUtils.e(TAG, "----cameraFilename:" + cameraFilename);
                        if (CommonUtils.isEmpty(cameraFilename)) {
                            mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
                            return;
                        }
                        PalmchatLogUtils.println("cameraFilename  " + cameraFilename);
                        mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == ALBUM_REQUEST && resultCode == MessagesUtils.PICTURE) { // 相册选择照片返回
            if (data != null) {
                List<String> photoLs = data.getStringArrayListExtra("photoLs");
                for (String imgPath : photoLs) {
                    if (null != looperThread.handler) {
                        looperThread.handler.obtainMessage(LooperThread.SEND_IMAGE_LOOPER, imgPath).sendToTarget();
                    }
                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 生成缩略图并返回路径
     *
     * @param f
     * @param type
     * @return
     */
    private String smallImage(File f, int type) {
        FileInputStream is = null;
        Bitmap bitmap = null;

        try {
            int len = 0;
            BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            is = new FileInputStream(f.getAbsolutePath());
            len = is.available();
            if (len > 100 * 100 * 100) {
                opts.inSampleSize = 4;//取得的缩略图长和宽都是原来的1/4，图片大小就为原始的1/16
            } else if (len > 10 * 100 * 100) {
                opts.inSampleSize = 2;//取得的缩略图长和宽都是原来的1/2，图片大小就为原始的1/4
            }
            bitmap = BitmapFactory.decodeStream(is, null, opts);
            is.close();
            opts = null;
            if (null != bitmap) {
                /** 旋转图片 */
                int degree = BitmapUtils.readBitmapDegree(f.getAbsolutePath());
                if (0 != degree) {
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degree);
                    Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    if (null != newbmp) {
                        BufferedOutputStream bos;
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                        bos = new BufferedOutputStream(new FileOutputStream(f.getAbsolutePath()));
                        newbmp.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                        bos.flush();
                        bos.close();
                        bitmap = newbmp;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (null == bitmap) {
            return "";
        }
        Bitmap newBitmap = null;
        newBitmap = ImageUtil.zoomBitmapEx2(bitmap, 0);
        String smallFilename = ClippingPicture.saveTalkBitmap(newBitmap, mAfCorePalmchat);
        if (newBitmap != null && !newBitmap.isRecycled()) {
            newBitmap.recycle();
        }
        return smallFilename;
    }


    /**
     * 初始化初始化
     *
     * @param largeFileName 大图片名称
     * @param largeFileSize 大图片大小
     * @param smallFilename 小图片名称
     * @param smallFileSize 小图片大小
     */
    private void initMedia(final String largeFileName, final int largeFileSize, final String smallFilename, final int smallFileSize) {
        new Thread(new Runnable() {
            public void run() {
                AfMessageInfo afMessageInfo = getMessageInfoForImage(largeFileName, largeFileSize, smallFilename, smallFileSize);
                //添加到缓存
                CacheManager.getInstance().addmImgSendList(afMessageInfo);
                //发送广播
                sendCamera();
                //向缓存中插入一条数据
                MessagesUtils.insertMsg(afMessageInfo, true, true);
                //延迟发送
                mainHandler.sendEmptyMessageDelayed(SEND_SHARE_IMAGE, 1000);
            }
        }).start();

    }


    /**
     * 生成消息类对象
     *
     * @param largeFileName 大图片名称
     * @param largeFileSize 大图片大小
     * @param smallFileName 小图片名称
     * @param smallFizeSize 小图片大小
     * @return
     */
    private AfMessageInfo getMessageInfoForImage(String largeFileName, int largeFileSize, String smallFileName, int smallFizeSize) {
        // TODO Auto-generated method stub RequestConstant.IMAGE_CACHE;
        long begin = System.currentTimeMillis();
        PalmchatLogUtils.println("getMessageInfoForImage begin " + begin);
        AfAttachImageInfo afAttachImageInfo = new AfAttachImageInfo();
        afAttachImageInfo.large_file_name = largeFileName;
        afAttachImageInfo.large_file_size = largeFileSize;
        afAttachImageInfo.small_file_name = smallFileName;
        afAttachImageInfo.small_file_size = smallFizeSize;
        //
        AfImageReqInfo param = new AfImageReqInfo();

        param.file_name = afAttachImageInfo.small_file_name + System.currentTimeMillis();
        param.path = afAttachImageInfo.large_file_name;
        param.recv_afid = mFriendAfid;
        param.send_msg = mFriendAfid;
        //
        param._id = mAfCorePalmchat.AfDbImageReqInsert(param);
        afAttachImageInfo.upload_id = param._id;
        afAttachImageInfo.upload_info = param;
        PalmchatLogUtils.println("param._id  " + param._id);

        afAttachImageInfo._id = mAfCorePalmchat.AfDbAttachImageInsert(afAttachImageInfo);


        AfMessageInfo afMessageInfo = new AfMessageInfo();
        afMessageInfo.attach = afAttachImageInfo;
        afMessageInfo.attach_id = afAttachImageInfo._id;
        afMessageInfo.client_time = System.currentTimeMillis();
        afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_IMAGE;
        afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
        afMessageInfo.toAfId = mFriendAfid;
        afMessageInfo.attach = afAttachImageInfo;

        afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);
        //afAttachImageInfo.upload_info = new AfImageReqInfo();
        PalmchatLogUtils.println("getMessageInfoForImage end " + (System.currentTimeMillis() - begin));
        return afMessageInfo;
    }

    /**
     * 设置消息未读数
     */
    private void setUnReadCount() {
        if (!isBottom) {
            unreadTipsCount++;
            view_unread.setVisibility(View.VISIBLE);

            String _tipsCount = null;
            if (unreadTipsCount > 99) {
                _tipsCount = "99+";
            } else {
                _tipsCount = String.valueOf(unreadTipsCount);
            }
            textview_unread.setText(_tipsCount);
        }
    }

    private long lastSoundTime;

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
        //设置语音消息队列顺序
        setVoiceQueue(afMessageInfo);
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
        if (mFriendAfid.equals(afMessageInfo.fromAfId) && (MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type))) {//当前聊天人发过来的信息
            setUnReadCount();
            if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ || msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS) {//
                friendRequest(afMessageInfo);
            } else {
                String a = DateUtil.getFormatDateTime(afMessageInfo.server_time * 1000, "HH:mm:ss");
				if(!isOnline){
					if(System.currentTimeMillis() - afMessageInfo.server_time * 1000 < 10 * 1000 * 60){
						mTextViewOnline.setText(R.string.online);
						mTextViewOnline.setVisibility(View.VISIBLE);
						if (PalmchatApp.getApplication().getOnlineStatusInfoMap().containsKey(mFriendAfid)) {
							AfOnlineStatusInfo afOnlineStatusInfo = PalmchatApp.getApplication().getOnlineStatusInfoMap().get(mFriendAfid);
							afOnlineStatusInfo.status = AfOnlineStatusInfo.AFMOBI_ONLINESTAUS_INFO_ONLINE;
							afOnlineStatusInfo.ts = System.currentTimeMillis();
							PalmchatApp.getApplication().setOnlineStatusInfoMap(mFriendAfid,afOnlineStatusInfo);
							isOnline = true;
						}
					}
				}
                PalmchatLogUtils.println("handleMessageFromServer  begin  System.currentTimeMillis() - lastSoundTime  " + (System.currentTimeMillis() - lastSoundTime));
                if (Chatting.class.getName().equals(CommonUtils.getCurrentActivity(this))) {
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
//						listChats.add(afMessageInfo);
                    //插入到消息列表
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    //震动
                    showVibrate();
                    setAdapter(null);
                }
                toSendRead = true;
                handleMessageForReadFromServer(new AfMessageInfo(afMessageInfo.fromAfId));

                if (MessagesUtils.isFlowerMsg(afMessageInfo.type) && Chatting.class.getName().equals(CommonUtils.getCurrentActivity(this))) {
                    afMessageInfo.status = AfMessageInfo.MESSAGE_READ;
                    mAfCorePalmchat.AfDbMsgSetStatus(afMessageInfo._id, afMessageInfo.status);
                    ShowSendFlowerAnim(afMessageInfo.msg);

                }

            }


        } else {
            //不是当前页面的朋友发过来的消息，并在聊天界面显示提醒信息
            //private chat or group chat or palmchat team
            if (MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isGroupChatMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type)) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    vTextViewOtherMessageToast.setBackgroundDrawable(null);
                }else{
                    vTextViewOtherMessageToast.setBackground(null);
                }
                switch (msgType) {
                    case AfMessageInfo.MESSAGE_TEXT:
                    case AfMessageInfo.MESSAGE_EMOTIONS:
                    case AfMessageInfo.MESSAGE_PUBLIC_ACCOUNT:
                    case AfMessageInfo.MESSAGE_PA_URL:
                        String msg = afMessageInfo.msg;
                        final CharSequence content = EmojiParser.getInstance(context).parse(msg);
                        vTextViewOtherMessageToast.setText(content);
                        break;

                    case AfMessageInfo.MESSAGE_FLOWER:
                        String showMsg = getString(R.string.sent_flower_msg).replace("XXXX", afMessageInfo.msg);
                        vTextViewOtherMessageToast.setText(showMsg);
                        break;

                    case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
                        vTextViewOtherMessageToast.setText(getString(R.string.msg_custom_emoticons));
                        break;
                    case AfMessageInfo.MESSAGE_VOICE:
                        vTextViewOtherMessageToast.setText(getString(R.string.msg_voice));
                        break;
                    case AfMessageInfo.MESSAGE_IMAGE:
                        vTextViewOtherMessageToast.setText("");
                        vTextViewOtherMessageToast.setBackgroundResource(R.drawable.pop_default);
                        break;
                    case AfMessageInfo.MESSAGE_CARD:
                        vTextViewOtherMessageToast.setText(R.string.name_card);
                        break;
                    case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
                        AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) afMessageInfo.attach;
                        if (afAttachPAMsgInfo != null) {
                            vTextViewOtherMessageToast.setText(afAttachPAMsgInfo.content);
                        }
                        break;
                    case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
                        vTextViewOtherMessageToast.setText(R.string.msg_share_broadcast);
                        break;
                    case AfMessageInfo.MESSAGE_FRIEND_REQ:
                    case AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS:
                        vTextViewOtherMessageToast.setText(CommonUtils.getFriendRequestContent(PalmchatApp.getApplication().getApplicationContext(),afMessageInfo));
                    default:
                        String msgs = afMessageInfo.msg;
                        final CharSequence contents = EmojiParser.getInstance(context).parse(msgs);
                        vTextViewOtherMessageToast.setText(contents);
                }
                final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.fromAfId);
                PalmchatLogUtils.println("handleMessage  afFriendInfo  " + afFriendInfo);
                if (afFriendInfo != null) {
                    String displayName = "";
                    if (afFriendInfo.afId != null) {
                        displayName = TextUtils.isEmpty(afFriendInfo.name) ? afFriendInfo.afId.replace("a", "") : afFriendInfo.name;
                    }
                    vTextViewOtherMessageName.setText(displayName);
                    vTextViewOtherMessageColon.setVisibility(View.VISIBLE);
                    StartTimer.timerHandler.post(StartTimer.startTimer(afMessageInfo, viewFrameToast));
                    viewFrameToast.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (MessagesUtils.isPrivateMessage(afMessageInfo.type)) {
//								refreshView(afMessageInfo);
                                toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
                            }
                            else if(MessagesUtils.isSystemMessage(afMessageInfo.type)){
                                finish();
                                toAccountsChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
                            }
                            else if (MessagesUtils.isGroupChatMessage(afMessageInfo.type)) {
                                finish();
                                toGroupChatting(afMessageInfo.toAfId);
                            }
                        }
                    });
                }

            } else {
                PalmchatLogUtils.println("Chatroom message");
            }
        }
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
    private void toAccountsChatting(AfFriendInfo infos, String afid, String name) {
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_P_PBL);

        Intent intent = new Intent(context, AccountsChattingActivity.class);
        intent.putExtra(JsonConstant.VIEW_HISTORY, false);
        intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
        intent.putExtra(JsonConstant.KEY_FRIEND, infos);
        context.startActivity(intent);
    }

    /**
     * 处理好友请求的消息
     *
     * @param afMessageInfo
     */
    private void friendRequest(final AfMessageInfo afMessageInfo) {
        Log.d(TAG, "===:" + "friendRequest");
        if (afMessageInfo != null && afMessageInfo.fromAfId != null && afMessageInfo.fromAfId.equals(mFriendAfid)) {

            final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
            if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ) {//好友请求消息，显示是否接受好友请求的消息
                r_stranger_add.setVisibility(View.GONE);
                r_frd_add.setVisibility(View.VISIBLE);

            } else if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS) {//接受好友的请求并服务端添加成功
                r_stranger_add.setVisibility(View.GONE);
                r_frd_add.setVisibility(View.GONE);
                mImageViewAddGroup.setVisibility(View.VISIBLE);

                final MainAfFriendInfo aff = MainAfFriendInfo.getFreqInfoFromAfID(mFriendAfid);
                if (aff != null) {
                    new_friend_insert(mAfCorePalmchat, aff,false);
                }
            }
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
                    //获取光标位置
                    int index = edit.getSelectionStart();
                    String str = edit.getText().insert(index,"\n").toString();
                    CharSequence text = EmojiParser.getInstance(this).parse(str);
                    v.setText(text);

                    if (str.length() >= ReplaceConstant.MAX_SIZE) {
                        edit.setSelection(ReplaceConstant.MAX_SIZE);//将光标移至文字末�?
                    } else {
                        edit.setSelection(index + 1);//移到光标处
                    }

                }
                break;
        }

        return true;
    }


    /**
     * 发送默认图片
     *
     * @param text
     * @param i
     * @param j
     * @param b
     */
    public void sendDefaultImage(final String text, int i, int j, boolean b) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            public void run() {
                AfMessageInfo afMessageInfo = new AfMessageInfo();
                afMessageInfo.msg = text;
//		afMessageInfo.attach = afAttachVoiceInfo;
                afMessageInfo.client_time = System.currentTimeMillis();
//				afMessageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
                afMessageInfo.toAfId = mFriendAfid;
                afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_EMOTIONS;
                afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

                afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);
                mainHandler.obtainMessage(MessagesUtils.MSG_EMOTION, afMessageInfo).sendToTarget();
                //向缓存中插入一条数据
                MessagesUtils.insertMsg(afMessageInfo, true, true);
            }
        }).start();

    }


    /**
     * 重发默认图片
     *
     * @param afMessageInfo
     */
    public void resendDefaultImage(final AfMessageInfo afMessageInfo) {
        new Thread(new Runnable() {
            public void run() {
//				afMessageInfo.client_time = System.currentTimeMillis();
                afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
                MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
                mainHandler.obtainMessage(MessagesUtils.MSG_EMOTION, afMessageInfo).sendToTarget();
            }
        }).start();
    }


    /**
     * 重发语音
     *
     * @param afMessageInfo
     */
    public void resendVoice(final AfMessageInfo afMessageInfo) {
        AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) afMessageInfo.attach;
        final String voiceName = afAttachVoiceInfo.file_name;
        final int recordTimeTemp = afAttachVoiceInfo.voice_len;
        if (!CommonUtils.isEmpty(voiceName)) {
            new Thread(new Runnable() {
                public void run() {
                    data = FileUtils.readBytes(RequestConstant.VOICE_CACHE + voiceName);
                    if (recordTimeTemp <= 0) {
                        mainHandler.sendEmptyMessage(MessagesUtils.MSG_TOO_SHORT);
                    } else {
                        if (data == null || data.length <= AT_LEAST_LENGTH) {
                            mainHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
                            return;
                        }
                        sendMessageInfoForText(AfMessageInfo.MESSAGE_VOICE, voiceName, data.length, recordTimeTemp, mainHandler, MessagesUtils.SEND_VOICE, MessagesUtils.ACTION_UPDATE, afMessageInfo, null);
                    }
                }
            }).start();
        } else {
            ToastManager.getInstance().show(context, getString(R.string.sdcard_unmounted));
        }
    }


    private void addAction(int mode, String target) {
        widget = new ChattingMenuDialog(this, mode);
        widget.setItemClick(this);
    }

    /**
     * 清空聊天信息
     */
    public void clearMessage() {
        AppDialog appDialog = new AppDialog(context);
        String msg = context.getResources().getString(R.string.clear_chat_history);
        appDialog.createClearDialog(context, msg, new OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                final AfMessageInfo afMessageInfo = adapterListView.getLastMsg();
                adapterListView.getLists().clear();
                setAdapter(afMessageInfo);
                new Thread(new Runnable() {
                    public void run() {
                        PalmchatLogUtils.println("clearMessage  afMessageInfo  " + afMessageInfo);
                        MessagesUtils.removeMsg(afMessageInfo, true, true);
                        mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mFriendAfid);
                    }
                }).start();
            }

            @Override
            public void onLeftButtonClick() {

            }
        });
        appDialog.show();
    }


    /**
     * 设置媒体模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        if (mode == AudioManager.MODE_IN_CALL) {
            AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audio.setSpeakerphoneOn(false);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            audio.setMode(AudioManager.MODE_IN_CALL);
        } else {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * 重发图片
     *
     * @param afMessageInfo
     */
    public void resendImage(final AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
        if (afAttachImageInfo != null) {
//			afMessageInfo.client_time = System.currentTimeMillis();
            afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
//			listChats.add(afMessageInfo);
            CommonUtils.getRealList(listChats, afMessageInfo);
            setAdapter(afMessageInfo);


//			forward msg failed, upload_info == null
            if (afAttachImageInfo.upload_info == null) {
                PalmchatLogUtils.println("afAttachImageInfo.upload_info == null");

                AfImageReqInfo param = new AfImageReqInfo();

                param.file_name = afAttachImageInfo.small_file_name + System.currentTimeMillis();
                param.path = afAttachImageInfo.large_file_name;
                param.recv_afid = mFriendAfid;
                param.send_msg = mFriendAfid;
                //
                param._id = mAfCorePalmchat.AfDbImageReqInsert(param);
                afAttachImageInfo.upload_id = param._id;
                afAttachImageInfo.upload_info = param;
                PalmchatLogUtils.println("param._id  " + param._id);

                PalmchatLogUtils.i("WXL", "param.server_dir =" + param.server_dir + "  param.server_name=" + param.server_name);

                mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afMessageInfo._id, 0, null, Consts.AFMOBI_PARAM_FID);
            }
            new Thread(new Runnable() {
                public void run() {
                    mAfCorePalmchat.AfDbImageReqUPdate(afAttachImageInfo.upload_info);
//					addToCache(afMessageInfo);
                    MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
                }
            }).start();
            new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid).start();

            // heguiming 2013-12-04
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_PIC);
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//            MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.S_M_PIC);
//            MobclickAgent.onEvent(Chatting.this, ReadyConfigXML.P_NUM);

            int handle = mAfCorePalmchat.AfHttpSendImage(afAttachImageInfo.upload_info, afMessageInfo, this, this);
            sendRequestError(afMessageInfo, handle, Consts.SEND_IMAGE);
        } else {
            PalmchatLogUtils.println("resendImage " + afMessageInfo);
        }
    }

    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
    @Override
    protected void onDestroy() {
        PalmchatApp.getApplication().removeMessageReceiver(this);
        PalmchatApp.getApplication().setMessageReadReceiver(null);
//        PalmchatApp.getApplication().getHandlerMap().remove(Chatting.class.getCanonicalName());
        if (null != looperThread && null != looperThread.looper) {
            looperThread.looper.quit();
        }
        if (mFlowerAniView != null) {
            mFlowerAniView.stopPlayFlowers();
        }
        PalmchatApp.getApplication().setColseSoftKeyBoardListe(null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if(mScreenObserver != null) {
            mScreenObserver.stopScreenStateUpdate();
        }
        PalmchatLogUtils.i("hj","Chatting Ondestroy");
    }


    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        if (!isRefreshing) {
            isRefreshing = true;
            mOffset = listChats.size();
            vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            getMsgData(false);//  new GetDataTask(false).execute();
        }
    }


    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub

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
                    AfMessageInfo lastMsg = adapterListView.getLastMsg();
                    if (lastMsg != null) {
                        //					 CacheManager.getInstance().getRecentMsgCacheSortList(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(lastMsg,true,true);
                        MessagesUtils.insertMsg(lastMsg, true, true);
                    } else {
                        AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mFriendAfid, 0, 1);
                        if (recentDataArray == null || recentDataArray.length <= 0) {
                            MessagesUtils.removeMsg(afMessageInfo, true, true);
                        }
                    }

                }
            }
        }).start();

    }

    /*below for already read sign code*/
    private final static int READ_INTERVAL_TIME = 10000;
    private boolean toSendRead = false;
    Handler chattingHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int code = -2;
            if (toSendRead) {
                toSendRead = false;
                code = mAfCorePalmchat.AfHttpSendMsg(mFriendAfid, System.currentTimeMillis(), null, Consts.MSG_CMMD_READ, Consts.REQ_CODE_READ, Chatting.this);
            }
            PalmchatLogUtils.println("chattingHandler  code  " + code);
            chattingHandler.postDelayed(this, READ_INTERVAL_TIME);
        }
    };

    private boolean pop = false;


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        /*MobclickAgent.onPageStart("Chatting");
        MobclickAgent.onResume(this);*/

        getData();
        CommonUtils.cancelNoticefacation(getApplicationContext());

        String msgContent = mAfCorePalmchat.AfDbGetMsgExtra(mFriendAfid);
        vEditTextContent.setText(EmojiParser.getInstance(context).parse(msgContent));
        CharSequence text = vEditTextContent.getText();
        if (text.length() > 0) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }

        if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
            chattingHandler.postDelayed(runnable, READ_INTERVAL_TIME);
        }
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(faceFooterView != null) {
                    faceFooterView.refreshFaceFootView();
                    faceFooterView.showNewSymbolOrNot();
                    if(faceFooterView.mSelectItemIndex != -1){
                        emojjView.setUnSelected();
                    }
                }
            }
        }, 300);

        if (CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().containsKey(mFriendAfid)) {
            pop = true;
            getMsgData(false);//new GetDataTask(false).execute();
            CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().clear();
        }

        if (looperThread != null && looperThread.handler != null) {
            looperThread.handler.obtainMessage(LooperThread.CHECK_FLOWER_MSG).sendToTarget();
        }


        if (systemBarConfig.hasNavigtionBar()) {
            if (PalmchatApp.getOsInfo().getUa().contains("HTC") //|| PalmchatApp.getOsInfo().getUa().contains("MI")
                    || PalmchatApp.getOsInfo().getBrand().contains("Meizu")) {//过滤htc,小米 meizu
                chat_root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                tintManager.setNavigationBarTintEnabled(false);
            }
        }
        //设置用户在线状态
        setOnlineStatus();
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (isInResume) {
            new Thread(new Runnable() {
                public void run() {
                    AfMessageInfo afMessageInfo = adapterListView.getLastMsg();
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
     * 显示发送花的动画
     *
     * @param flowerCount
     */
    public void ShowSendFlowerAnim(final String flowerCount) {
    	if(mainHandler!=null){
	        mainHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                // TODO Auto-generated method stub
	                PalmchatLogUtils.println("show send flower animation");
	//					PalmchatApp.getApplication().getSoundManager().playNotification();
	                playFlowersAnimationImmediately(flowerCount);
	            }
	        }, 1000);
    	}


    }

    /**
     * show Flowers admiation immediately .must be call by in UIThread
     *
     * @param flowerCount if null mean not show  flowers number string in animation
     */
    private void playFlowersAnimationImmediately(String flowerCount) {
        if (mFlowerAniView.isRunning()) {
            mFlowerAniView.stopPlayFlowers();
        }
        mFlowerAniView.startPlayFlowers(flowerCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("Chatting"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//        MobclickAgent.onPause(this);
        CacheManager.getInstance().getClickableMap().put(mFriendAfid + CacheManager.accept_friend_suffix, false);
        mIsFirst = true;
        mOffset = 0;
        int dbInt = mAfCorePalmchat.AfDbSetMsgExtra(mFriendAfid, getEditTextContent());
        PalmchatLogUtils.println("mFriendAfid  " + mFriendAfid + "  onPause  dbInt " + dbInt + "  getEditTextContent  " + getEditTextContent());


//		is recording, so send voice
//		正在录音时灭屏， 将语音发送出去
        if (recordStart > 0) {
            if (vTextViewRecordTime != null && vTextViewRecordTime.isShown() && !sent) {
                vTextViewRecordTime.setVisibility(View.GONE);
                stopRecording();
                PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
                if (CommonUtils.getSdcardSize()) {
                    ToastManager.getInstance().show(context, R.string.memory_is_full);
                } else {
                    PalmchatLogUtils.println("---- send voice----");
                    mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);

                    sent = true;
                }

                stopRecordingTimeTask();
            }
            setMode(SharePreferenceService.getInstance(PalmchatApp.getApplication().getApplicationContext()).getListenMode());

//		show cancelled text
            if (mCancelVoice.getVisibility() == View.VISIBLE) {
                mCancelVoice.setText(R.string.cancelled);
                mainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mCancelVoice.setVisibility(View.GONE);
                    }
                }, 1000);

            }
        }
        VoiceManager.getInstance().completion();
        chattingHandler.removeCallbacks(runnable);


    }

    /**
     * 跳转私群聊
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
    }

    @Override
    public void onItemClick(int item) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("Chatting onItemClick " + item);
        if (widget != null) {
            widget.cancel();
        }
        switch (item) {
            case ChattingMenuDialog.ACTION_SPEAKER_MODE:
                AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                if (!mIsFirst) {
                    ToastManager.getInstance().show(Chatting.this, getString(R.string.switch_speaker_mode));
                }
                SharePreferenceService.getInstance(Chatting.this).setListenMode(AudioManager.MODE_NORMAL);
                break;
            case ChattingMenuDialog.ACTION_HANDSET_MODE:
                AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
                audio.setSpeakerphoneOn(false);
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                audio.setMode(AudioManager.MODE_IN_CALL);
                if (!mIsFirst) {
                    ToastManager.getInstance().show(Chatting.this, getString(R.string.switch_handset_mode));
                }
                SharePreferenceService.getInstance(Chatting.this).setListenMode(AudioManager.MODE_IN_CALL);
                break;
            case ChattingMenuDialog.ACTION_CLEAR_DATA:
                clearMessage();
                break;
            case ChattingMenuDialog.ACTION_CANCEL:

                break;

            default:
                break;
        }
    }


    @Override
    public void onItemClick(int position, String name, String afid, byte sex) {
        closeEmotions();
        String str = getEditTextContent();
        str = str + "@" + name + ":";
        vEditTextContent.setText(EmojiParser.getInstance(getApplicationContext()).parse(str));

        vEditTextContent.requestFocus();
        if (!TextUtils.isEmpty(str)) {
            vEditTextContent.setSelection(str.length());
        }
        CommonUtils.showSoftKeyBoard(vEditTextContent);
    }

    /**
     * 收到已读类型消息，将发送出去的消息状态设为已读。
     */
    @Override
    public void handleMessageForReadFromServer(AfMessageInfo afReadInfo) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("handleMessageForReadFromServer  afReadInfo  " + afReadInfo);
        if (mFriendAfid.equals(afReadInfo.fromAfId)) {
            for (AfMessageInfo afMessageInfo : listChats) {
                int status = afMessageInfo.status;
                if (!MessagesUtils.isReceivedMessage(status) && status == AfMessageInfo.MESSAGE_SENT) {
                    afMessageInfo.status = AfMessageInfo.MESSAGE_SENT_AND_READ;
                }
            }
            setAdapter(null);
        }
    }

    /**
     * 一个聊天图片 查看大图后 把大图替换掉消图 做为清晰的预览图时进入该事件
     * @param event
     */
    public void onEventMainThread(ChatImageDownloadedEvent event) {
    	if(adapterListView!=null){
    		adapterListView.notifyDataSetChanged();
    	}
    }



    /**
     * 发送gif图片
     *
     * @param text
     * @param i
     * @param j
     * @param b
     */
    public void sendGifImage(final String text, int i, int j, boolean b) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            public void run() {
                AfMessageInfo afMessageInfo = new AfMessageInfo();
                afMessageInfo.msg = text;
//		afMessageInfo.attach = afAttachVoiceInfo;
                afMessageInfo.client_time = System.currentTimeMillis();
//				afMessageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
                afMessageInfo.toAfId = mFriendAfid;
                afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_STORE_EMOTIONS;
                afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

                afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);
                mainHandler.obtainMessage(MessagesUtils.MSG_GIF, afMessageInfo).sendToTarget();
                //向缓存中插入一条数据
                MessagesUtils.insertMsg(afMessageInfo, true, true);
            }
        }).start();

    }

    /**
     * 重发gif图片
     *
     * @param afMessageInfo
     */
    public void resendGifImage(AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        if (afMessageInfo != null) {
            afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
            mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
            mainHandler.obtainMessage(MessagesUtils.MSG_GIF, afMessageInfo).sendToTarget();

            PalmchatLogUtils.println("resendGifImage---msg id:" + afMessageInfo._id);

        }

    }

    public class ImageOnScrollListener implements OnScrollListener {

        @SuppressWarnings("rawtypes")
        private final Class[] INT_CLASS = {int.class};

        private static final String TAG = "ImageOnScrollListener";



        private ListView listView;

        public ImageOnScrollListener(ListView listView ) {
            super();
            this.listView = listView;

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mIsRecording) {
                return;
            }

            if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {

                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
//	             judge if filiing is finish
                /*if (scrollState == SCROLL_STATE_FLING) {
                    new Thread(getWatcherRunnable()).start();
                }*/
            } else {

                adapterListView.notifyDataSetChanged();
            }

            if (OnScrollListener.SCROLL_STATE_FLING == scrollState || OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState || OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                disableViews();
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
                            + "  vListView.getLastVisiblePosition()  " + vListView.getLastVisiblePosition()
            );

            if (visibleItemCount + firstVisibleItem >= totalItemCount - 1) {
                PalmchatLogUtils.println("Chatting  onScroll  Bottom  true");
                hideUnreadTips();
                isBottom = true;
            } else {
                PalmchatLogUtils.println("Chatting  onScroll  Bottom  false");
                isBottom = false;
            }
            _start_index = firstVisibleItem;
            _end_index = firstVisibleItem + visibleItemCount;
            if (_end_index >= totalItemCount) {
                _end_index = totalItemCount - 1;
            }
        }

        private Runnable getWatcherRunnable() {
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
        }
    }

    /**
     * 更新好友按钮状态
     *
     * @param afid
     * @param code
     * @return
     */
    public boolean update_addFriend_clickable(String afid, int code) {
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.add_friend_suffix)) {
            if (CacheManager.getInstance().getClickableMap().get(afid + CacheManager.add_friend_suffix)) {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.add_friend_suffix, false);
            } else {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.add_friend_suffix, true);
            }
            set_addFriend_clickable(afid, code);
            return CacheManager.getInstance().getClickableMap().get(afid + CacheManager.add_friend_suffix);
        }
        return true;
    }

    /**
     * 设置加好友按钮的点击状态
     *
     * @param afid
     * @param code
     */
    public void set_addFriend_clickable(String afid, int code) {
        PalmchatLogUtils.e("set_addFriend_clickable", code + "");
        if (!CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.add_friend_suffix)) {
            CacheManager.getInstance().getClickableMap().put(afid + CacheManager.add_friend_suffix, false);
        }
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.add_friend_suffix)) {
            if (!CacheManager.getInstance().getClickableMap().get(afid + CacheManager.add_friend_suffix)) {
                if (code != Consts.REQ_CODE_SUCCESS) {
                    stranger_for_add.setVisibility(View.VISIBLE);
                    stranger_for_adding.setVisibility(View.GONE);
                }
                stranger_for_add.setClickable(true);
                stranger_for_add.setEnabled(true);
                PalmchatLogUtils.e("img_gift.setClickable", "lin_tab_follow.setClickable = true");
            } else {
                stranger_for_add.setVisibility(View.INVISIBLE);
                stranger_for_adding.setVisibility(View.VISIBLE);
                stranger_for_add.setClickable(false);
                stranger_for_add.setEnabled(false);
                PalmchatLogUtils.e("img_gift.setClickable", "lin_tab_follow.setClickable = false");
            }
        }
    }

    public boolean update_AcceptFriend_clickable(String afid, int code) {
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.accept_friend_suffix)) {
            if (CacheManager.getInstance().getClickableMap().get(afid + CacheManager.accept_friend_suffix)) {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.accept_friend_suffix, false);
            } else {
                CacheManager.getInstance().getClickableMap().put(afid + CacheManager.accept_friend_suffix, true);
            }
            set_AcceptFriend_clickable(afid, code);
            return CacheManager.getInstance().getClickableMap().get(afid + CacheManager.accept_friend_suffix);
        }
        return true;
    }

    /**
     * 设置同意好友请求按钮的点击状态
     *
     * @param afid
     * @param code
     */
    public void set_AcceptFriend_clickable(String afid, int code) {
        PalmchatLogUtils.e("set_AcceptFriend_clickable", code + "");
        if (!CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.accept_friend_suffix)) {
            CacheManager.getInstance().getClickableMap().put(afid + CacheManager.accept_friend_suffix, false);
        }
        if (CacheManager.getInstance().getClickableMap().containsKey(afid + CacheManager.accept_friend_suffix)) {
            if (!CacheManager.getInstance().getClickableMap().get(afid + CacheManager.accept_friend_suffix)) {

                if (code != Consts.REQ_CODE_SUCCESS) {
                    frd_accept.setVisibility(View.VISIBLE);
                    frd_accepting.setVisibility(View.GONE);
                }
                frd_accept.setClickable(true);
                frd_accept.setEnabled(true);
                PalmchatLogUtils.e("img_gift.setClickable", "lin_tab_follow.setClickable = true");
            } else {
                frd_accept.setVisibility(View.VISIBLE);
                PalmchatLogUtils.e("img_gift.setClickable", "lin_tab_follow.setClickable = false");
            }
        }
    }

    private String download_file_save_path = "";

    /**
     * 下载store表情
     *
     * @param mItemId
     */
    public void downloadFace(String mItemId) {
        if (!CacheManager.getInstance().getStoreDownloadingMap().containsKey(mItemId)) {
            CacheManager.getInstance().getStoreDownloadingMap().put(mItemId, 0);
            download_file_save_path = CommonUtils.getStoreFaceFolderDownLoadSdcardSavePath(CacheManager.getInstance().getMyProfile().afId, mItemId);
            download_file_save_path += mItemId + Constants.STORE_DOWNLOAD_TMP;
            mAfCorePalmchat.AfHttpStoreDownload(null, mItemId, CacheManager.getInstance().getScreenType(), download_file_save_path, false, mItemId, this, this);
        }

        setShowOrNot(mItemId);
    }

    /** 格式化时间
     * @param date 时间
     * @return 今天的时间转化为hh:mm 其它时间转化为d/m/y
     */
    public static String dateFormat(String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date dateTime = null;
           // Date dateTimea = DateUtils.isToday()
            if(date.indexOf("/")>0) {
                 dateTime = df.parse(date);
            }
            else{
                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            }
            long times = dateTime.getTime();
            if(DateUtils.isToday(times)){
                df = new SimpleDateFormat("HH:mm:ss");
            }
            else{
                df = new SimpleDateFormat("dd/MM/yyyy");
            }
            return df.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /** 判断是否点击关注按钮
     * @param afid
     */
    public void AddFollw(final String afid) {
        if (!isFollow) {
            Update_IsFollow();
            mAfCorePalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_MASTRE, afid, Consts.HTTP_ACTION_A, isFollow, Chatting.this);
            MessagesUtils.addStranger2Db(afFriend);
        }
    }

    public void Update_IsFollow() {
        if (CacheManager.getInstance().getClickableMap().containsKey(mFriendAfid + CacheManager.follow_suffix)) {
            if (CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix)) {
                CacheManager.getInstance().getClickableMap().put(mFriendAfid + CacheManager.follow_suffix, false);
            } else {
                CacheManager.getInstance().getClickableMap().put(mFriendAfid + CacheManager.follow_suffix, true);
            }
            isFollow = CommonUtils.isFollow(mFriendAfid);
        }
    }

   public void setAddButton() {
       r_frd_add.setVisibility(View.GONE);
   }

}