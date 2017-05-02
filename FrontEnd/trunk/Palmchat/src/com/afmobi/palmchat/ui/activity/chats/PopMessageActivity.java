package com.afmobi.palmchat.ui.activity.chats;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.PalmchatApp.MessageReadReceiver;
import com.afmobi.palmchat.PalmchatApp.PopMessageReceiver;
import com.afmobi.palmchat.broadcasts.HomeBroadcast;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.broadcasts.ScreenObserver;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.adapter.EmotionAdapter;
import com.afmobi.palmchat.ui.activity.chats.adapter.PopMessageAdapter;
import com.afmobi.palmchat.ui.activity.chats.model.EmotionModel;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.ChattingMenuDialog;
import com.afmobi.palmchat.ui.customview.CutstomEditText;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.ui.customview.RippleView;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.TipHelper;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfAttachImageInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfFriendInfo;
import com.core.AfImageReqInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

import de.greenrobot.event.EventBus;

public class PopMessageActivity extends BaseActivity implements OnClickListener,
        AfHttpResultListener, AfHttpProgressListener, OnEditorActionListener,
        PopMessageReceiver, IXListViewListener, OnItemClick, OnItemLongClick, MessageReadReceiver {

    private static final String TAG = PopMessageActivity.class.getCanonicalName();
    private PopMessageAdapter adapterListView;
    private ListView vListView;
    private ArrayList<AfMessageInfo> listChats = new ArrayList<AfMessageInfo>();
    private ArrayList<AfMessageInfo> listVoiceChats = new ArrayList<AfMessageInfo>();

    RippleView mRippleView;
    private ImageView mVoiceImageEmotion;
    private ProgressBar mVoiceProgressBar, mVoiceProgressBar2;
    private EmojjView emojjView;
    private CutstomEditText vEditTextContent;
    private ImageView vButtonBackKeyboard;
    private ImageView vButtonSend;
    private AfPalmchat mAfCorePalmchat;
    private String mFriendAfid;
    private boolean isForward;
    private ImageView vImageEmotion, vButtonKeyboard;
    private View vImageViewVoice;
    private View viewUnTalk;
    private View viewOptionsLayout, viewChattingVoiceLayout;
    private Button vButtonTalk;
    private boolean sent;
    private MediaRecorder mRecorder;
    private long recordStart;
    private long recordEnd;
    private int recordTime;
    private TextView vTextViewRecordTime;
    private TextView mCancelVoice;

    private GridView chattingEmotion;
    private String cameraFilename;
    private File f;
    private LinearLayout chatting_emoji_layout;
    private List<EmotionModel> optionsData = new ArrayList<EmotionModel>();
    public String[] optionsString;
    private boolean isBottom = false;
    int optionsInt[] = {R.drawable.chatting_camara_icon_selector,
            R.drawable.chatting_gallary_icon_selector,
            R.drawable.chatting_voice_icon_selector,
            R.drawable.chatting_name_card_icon_selector,
    };
    private String mVoiceName;

    //	private String mSmallFilename;
    private Intent intent;
    byte[] data;
    ChattingMenuDialog widget;
//	private Action actions[];

    //	AfFriendInfo afFriend;
    private int mOffset = 0;
    private int mCount = 1;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
    private boolean mIsFirst = true, mIsNoticefy = true;
    private boolean isRefreshing = false;

    private boolean isInResume = false;

//    private ListViewAddOn listViewAddOn = new ListViewAddOn();
    private static final int RECORDING_TIMER = 9000;

    private boolean isOnClick = false;

    private boolean isPublicMember = false;
    private boolean isGreet = false; //

//	pop head layout

    private ImageView mPopHeadExit;
    private TextView mPopHeadName, mPopHeadId;
    private ImageView mPopHeadPhoto;

    private final static int AT_LEAST_LENGTH = 50;
    private final int ALBUM_REQUEST = 3;

    private boolean mIsRecording;

    ScreenObserver mScreenObserver;
    private HomeBroadcast mHomeBroadcast;
    final private Handler mainHandler = new Handler() {
        public void handleMessage(Message msg) {
            vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            switch (msg.what) {
                case RECORDING_TIMER:
                    long recordCurTime = System.currentTimeMillis();
                    int voiceProgress = (int) ((recordCurTime - recordStart) / 1000);
                    if (voiceProgress <= RequestConstant.RECORD_VOICE_TIME) {
                        mVoiceProgressBar.setProgress(voiceProgress);
                        mVoiceProgressBar2.setProgress(voiceProgress);
                    }
                    vTextViewRecordTime.setText(CommonUtils.diffTime(recordCurTime, recordStart));
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
                    }
                    break;

                case MessagesUtils.MSG_VOICE: {
                    sendVoice(mVoiceName);
                    break;
                }
                case MessagesUtils.SEND_VOICE: {

                    AfMessageInfo msgInfoVoice = (AfMessageInfo) msg.obj;
                    AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) msgInfoVoice.attach;
                    final int voiceLength = afAttachVoiceInfo.voice_len;
                    PalmchatLogUtils.println("Chatting  voiceLength  " + voiceLength);

                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_VOICE);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//                    MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.S_M_VOICE);
//                    MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.P_NUM);

                    int code = mAfCorePalmchat.AfHttpSendVoice(mFriendAfid, System.currentTimeMillis(), data, data.length, voiceLength, msgInfoVoice._id, PopMessageActivity.this, PopMessageActivity.this);
                    sendRequestError(msgInfoVoice, code, Consts.SEND_VOICE);
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

                case MessagesUtils.MSG_CARD://send card
                {
                    AfFriendInfo afFriendInfo = (AfFriendInfo) msg.obj;
                    sendCard(afFriendInfo);
                    break;
                }

                case MessagesUtils.MSG_TEXT://send text
                {
                    AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
                    send(msgInfoText.msg, msgInfoText);
//				vEditTextContent.setText("");
                    PalmchatLogUtils.println("msgInfoText._id  " + msgInfoText._id);
                    break;
                }
                case MessagesUtils.MSG_EMOTION: {
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;

//				listChats.add(afMessageInfo);
                    CommonUtils.getRealList(listChats, afMessageInfo);
                    setAdapter(afMessageInfo);

                    // heguiming 2013-12-04
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//                    MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.S_M_EMOTION);
//                    MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.S_M_TEXT);
//                    MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.P_NUM);

                    int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.toAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_EMOTION, afMessageInfo._id, PopMessageActivity.this);
                    sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
                    break;
                }

                case MessagesUtils.MSG_CAMERA_NOTIFY: {
                    AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                    AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                    updateStautsForImage(afMessageInfo._id, afMessageInfo.status, afMessageInfo.fid, afAttachImageInfo.progress);
                    break;
                }

                case MessagesUtils.MSG_SET_STATUS: {
                    PalmchatLogUtils.println("---www : MSG_SET_STATUS");
//			notify recent chats refresh
//                    sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
                    EventBus.getDefault().post(new RefreshChatsListEvent());
                    break;
                }
                case MessagesUtils.MSG_TOO_SHORT://显示录音太短
                {
                    ToastManager.getInstance().show(context, R.string.message_too_short);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mScreenObserver = new ScreenObserver(this, new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {

            }

            @Override
            public void onUserPresent() {//主要解决的问题：若解锁成功后，又弹出消息窗，则关闭此消息窗
                PalmchatLogUtils.i("hj", "PopMessageActivity BroadcastReceiverdoubleScreen");
                if(PalmchatApp.getApplication().isDoubleClickScreen) {
                    PalmchatApp.getApplication().isDoubleClickScreen = false;
                    clearData();
                }
            }
        });
        mHomeBroadcast = new HomeBroadcast(this, new HomeBroadcast.HomeStateListener() {
            @Override
            public void OnPressHome() {
                PalmchatApp.getApplication().isDoubleClickScreen = false;
                clearData();
            }
        });
        CacheManager.getInstance().getPopMessageManager().addPopMessageActivity(this);
//        PalmchatApp.getApplication().getHandlerMap().put(PopMessageActivity.class.getCanonicalName(), mainHandler);
        super.onCreate(savedInstanceState);
        PalmchatLogUtils.println("--rrr PopMessageActivity onCreate");
    }


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

                afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);
                afMessageInfo.attach = afFriendInfo;
                PalmchatLogUtils.println("afMessageInfo._id " + afMessageInfo._id);
                mainHandler.obtainMessage(MessagesUtils.MSG_RESEND_CARD, afMessageInfo).sendToTarget();
                addToCache(afMessageInfo);
//				CacheManager.getInstance()
            }
        }).start();

    }

    public void sendVoice(final String voiceName) {
        if (!CommonUtils.isEmpty(voiceName)) {

            PalmchatLogUtils.e(TAG, "----sendVoice----");

            new Thread(new Runnable() {
                public void run() {
                    data = FileUtils.readBytes(RequestConstant.VOICE_CACHE + voiceName);
                    if (recordTime < 2) {
                        mainHandler.sendEmptyMessage(MessagesUtils.MSG_TOO_SHORT);
                    } else {
                        if (data == null || data.length <= AT_LEAST_LENGTH) {
                            mainHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
                            return;
                        }

                        sendMessageInfoForText(AfMessageInfo.MESSAGE_VOICE, voiceName, data.length, recordTime, mainHandler, MessagesUtils.SEND_VOICE, MessagesUtils.ACTION_INSERT, null, null);

                        back();
                    }
                }
            }).start();
        } else {
            ToastManager.getInstance().show(context, getString(R.string.sdcard_unmounted));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return event.getAction() == MotionEvent.ACTION_OUTSIDE;
    }


    private WakeLock mWakeLock;

    @Override
    public void findViews() {
        if (PalmchatApp.getApplication().mColseSoftKeyBoardListe != null) {
            PalmchatLogUtils.println("PopMessage close SoftKeyBoard");
            PalmchatApp.getApplication().mColseSoftKeyBoardListe.onColseSoftKeyBoard();
        }
        intent = getIntent();
        mFriendAfid = intent.getStringExtra(JsonConstant.KEY_FROM_UUID);
        AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(mFriendAfid);

        if (afFriendInfo == null) {
            PalmchatLogUtils.println(TAG + " afFriendInfo == null " + mFriendAfid);
            finish();
            return;
        }

        setListenMode();

        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;

        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.TOP;

        onWindowAttributesChanged(localLayoutParams);

        getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
                | LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_popmessage);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                "com.afmobi.palmchat");

        mPopHeadExit = (ImageView) findViewById(R.id.pophead_exit);
        mPopHeadExit.setOnClickListener(this);

        mPopHeadPhoto = (ImageView) findViewById(R.id.itemimage);

        mPopHeadName = (TextView) findViewById(R.id.pophead_name);
        mPopHeadId = (TextView) findViewById(R.id.pophead_id);

        vButtonKeyboard = (ImageView) findViewById(R.id.btn_keyboard);
        vButtonKeyboard.setOnClickListener(this);
        isGreet = intent.getBooleanExtra(JsonConstant.KEY_GREET, false);

        String friendName = afFriendInfo.name;

        if (!TextUtils.isEmpty(afFriendInfo.alias)) {
            friendName = afFriendInfo.alias;
        }

        if (StringUtil.isNullOrEmpty(friendName)) {
            friendName = afFriendInfo.afId.replace("a", "");
        }

        mPopHeadName.setText(friendName);


//		palmchat team
        if (afFriendInfo.afId.startsWith(DefaultValueConstant._R)) {
            mPopHeadId.setText(DefaultValueConstant.EMPTY);

        } else {
            mPopHeadId.setText("Palm ID:" + afFriendInfo.afId.replace("a", ""));

        }


//        if (!CommonUtils.showHeadImage(afFriendInfo.afId, mPopHeadPhoto, afFriendInfo.sex)) {
            //调UIL的显示头像方法, 替换原来的AvatarImageInfo.
            ImageManager.getInstance().DisplayAvatarImage(mPopHeadPhoto, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);
        //        }


        isInResume = intent.getBooleanExtra(JsonConstant.KEY_FLAG, false);
        vListView = (ListView) findViewById(R.id.listview);
        vListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

                if (mIsRecording) {
                    return;
                }

                if (OnScrollListener.SCROLL_STATE_FLING == scrollState || OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState || OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                    vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                    CommonUtils.closeSoftKeyBoard(vEditTextContent);
                    disableViews();
                }
                PalmchatLogUtils.println("scrollState  " + scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
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
                    isBottom = true;
                } else {
                    PalmchatLogUtils.println("Chatting  onScroll  Bottom  false");
                    isBottom = false;
                }
            }
        });

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
        vEditTextContent.addTextChangedListener(new LimitTextWatcher(vEditTextContent, ReplaceConstant.MAX_SIZE, mainHandler, MessagesUtils.EDIT_TEXT_CHANGE));
        vEditTextContent.setMaxLength(ReplaceConstant.MAX_SIZE);
        vEditTextContent.setOnEditorActionListener(this);
        vEditTextContent.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                PalmchatLogUtils.println("Chatting  setOnKeyListener  keyCode  " + keyCode);
                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
                    PalmchatLogUtils.e(TAG, "keyCode==KEYCODE_ENTER==" + keyCode);
                }
                return false;
            }
        });
        mVoiceImageEmotion = (ImageView) findViewById(R.id.image_emotion2);
        mVoiceImageEmotion.setOnClickListener(this);

        mVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_progress);
        mVoiceProgressBar2 = (ProgressBar) findViewById(R.id.voice_progress2);
        mRippleView = (RippleView) findViewById(R.id.ripple);
        vButtonSend = (ImageView) findViewById(R.id.chatting_send_button);
        vButtonSend.setOnClickListener(this);
        vButtonBackKeyboard = (ImageView) findViewById(R.id.btn_backkeyboard);
        vButtonBackKeyboard.setOnClickListener(this);
        vImageViewVoice = findViewById(R.id.chatting_operate_one);
        vImageViewVoice.setOnClickListener(this);
        viewUnTalk = findViewById(R.id.un_talk);
        vButtonTalk = (Button) findViewById(R.id.talk_button);
        vButtonTalk.setOnTouchListener(new HoldTalkListener());
        vImageEmotion = (ImageView) findViewById(R.id.image_emotion);
        vImageEmotion.setOnClickListener(this);
        emojjView = new EmojjView(this);
        emojjView.select(EmojiParser.SUN);
        viewOptionsLayout = findViewById(R.id.chatting_options_layout);
        viewChattingVoiceLayout = findViewById(R.id.chatting_voice_layout);
        chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
        chattingEmotion = createGridView(8);
        chatting_emoji_layout.addView(emojjView.getViewRoot());
//		chatting_emoji_layout.addView(viewChattingEmotion);
        emojjView.getViewRoot().setVisibility(View.GONE);
        setEmojiBg();


        if (!CommonUtils.isEmpty(mFriendAfid) && mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
            isPublicMember = true;//palmchat team
            vImageEmotion.setVisibility(View.GONE);
        }

//		pop msg hide some ui
        vButtonSend.setVisibility(View.VISIBLE);
        vImageViewVoice.setVisibility(View.GONE);

        if (isGreet) {//say hi
            setEditTextContent(getString(R.string.hi));
            sendTextOrEmotion();
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
    }

    private void setEmojiBg() {
        viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
    }

    /**
     * forward msg
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
                    afMessageInfo.client_time = System.currentTimeMillis();
                    afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
                    afMessageInfo.toAfId = mFriendAfid;
                    afMessageInfo.fromAfId = null;
                    afMessageInfo._id = mAfCorePalmchat.AfDbMsgInsert(afMessageInfo);
                    mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, afMessageInfo._id, 0, afMessageInfo.fid, Consts.AFMOBI_PARAM_FID);
                    addToCache(afMessageInfo);
                    mainHandler.obtainMessage(MessagesUtils.MSG_FORWARD, afMessageInfo).sendToTarget();

                }
            }).start();

        }

    }

    private void setListenMode() {
        int listenMode = SharePreferenceService.getInstance(PopMessageActivity.this).getListenMode();
        PalmchatLogUtils.println("Chatting  time setListenMode " + dateFormat.format(new Date()) + "  listenMode  " + listenMode);
        if (listenMode == AudioManager.MODE_IN_CALL) {
            onItemClick(ChattingMenuDialog.ACTION_HANDSET_MODE);
        } else {
            onItemClick(ChattingMenuDialog.ACTION_SPEAKER_MODE);
        }
    }

    private void disableViews() {
        vTextViewRecordTime.setVisibility(View.GONE);
        emojjView.getViewRoot().setVisibility(View.GONE);
        setEmojiBg();
        vButtonKeyboard.setVisibility(View.GONE);
        if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
        	vImageEmotion.setVisibility(View.VISIBLE);
        }        
        if (viewChattingVoiceLayout.getVisibility() == View.VISIBLE) {
            viewOptionsLayout.setVisibility(View.VISIBLE);
            viewChattingVoiceLayout.setVisibility(View.GONE);
        }
    }

    private GridView createOptionsGridView(int number) {
        final GridView view = new GridView(this);
        view.setNumColumns(number);
//		view.setBackgroundResource(R.drawable.profile_photobg);
        view.setHorizontalSpacing(0);
        view.setVerticalSpacing(0);
        view.setSelector(android.R.color.transparent);
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        view.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.tab_bg);
        return view;
    }


    private GridView createGridView(int number) {
        final GridView view = new GridView(this);
        view.setNumColumns(number);
        view.setHorizontalSpacing(0);
        view.setVerticalSpacing(0);
        view.setPadding(10, 0, 10, 0);
        view.setBackgroundColor(Color.rgb(219, 225, 238));
        view.setSelector(android.R.color.transparent);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        view.setGravity(Gravity.CENTER);
        return view;
    }


    private void changeMode() {
        if (viewUnTalk.getVisibility() != View.VISIBLE) {
            viewUnTalk.setVisibility(View.VISIBLE);
            vButtonTalk.setVisibility(View.GONE);
//            vImageViewVoice.setBackgroundResource(R.drawable.chatting_setmode_voice_btn_focused);
        }
    }

    private void toChatting(AfFriendInfo affFriendInfo, String afid, String name) {

        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIST_T_P);
//        MobclickAgent.onEvent(context, ReadyConfigXML.LIST_T_P);

        Intent intent = new Intent(context, Chatting.class);
        intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
        intent.putExtra(JsonConstant.KEY_FROM_POP_MESSAGE_TO_CHATTING, true);
        intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
        intent.putExtra(JsonConstant.KEY_FROM_ALIAS, affFriendInfo.alias);
        intent.putExtra(JsonConstant.KEY_FRIEND, affFriendInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        clearData();
    }

    @Override
    public void init() {

        adapterListView = new PopMessageAdapter(context, listChats, vListView );
        adapterListView.setOnItemClick(this);
//		vListView.setOnScrollListener(new ImageOnScrollListener(vListView, listViewAddOn));
        PalmchatLogUtils.println("pop init listView:" + vListView);
        if (vListView != null) {

            vListView.setAdapter(adapterListView);

            setAdapter(null);

            vListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    if (mIsRecording) {
                        return;
                    }

                    PalmchatLogUtils.println("--ccz onItemClick");
                    final AfMessageInfo afMsg = adapterListView.getItem(position);
                    AfFriendInfo afF = CacheManager.getInstance().searchAllFriendInfo(afMsg.fromAfId);
                    if (afF != null) {

                        toChatting(afF, afMsg.fromAfId, afF.name);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                mAfCorePalmchat.AfRecentMsgSetUnread(afMsg.fromAfId, 0);
                                MessagesUtils.setUnreadMsg(afMsg.fromAfId, 0);
                            }
                        }).start();

                    }

                }
            });
            vListView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        doubleClick();
                    }
                    return true;
                }
            });
        }

        optionsData.clear();

        initData();

//		if is from forward msg
        isForward = intent.getBooleanExtra(JsonConstant.KEY_FORWARD, false);
        if (isForward) {
            forwardMsg();
        }
        app.setPopMessageReceiver(this);
        if(!CommonUtils.isScreenLocked(PalmchatApp.getApplication().getApplicationContext())){//主要解决的问题：若解锁成功后，又弹出消息窗，则关闭此消息窗
            PalmchatLogUtils.i("hj","Pop is not locked");
            clearData();
        }
    }


    private void sendAlreadyRead() {
        if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
            toSendRead = false;
            mAfCorePalmchat.AfHttpSendMsg(mFriendAfid, System.currentTimeMillis(), null, Consts.MSG_CMMD_READ, Consts.REQ_CODE_READ, this);
        }
    }


    private List<AfMessageInfo> getRecentData() {

        mCount = CacheManager.getInstance().getPopMessageManager().getMsgCount(mFriendAfid);
        PalmchatLogUtils.println("mOffset  " + mOffset + "  mCount  " + mCount);
        if(mAfCorePalmchat == null) {//未知原因
            mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        }
        AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, mFriendAfid, mOffset, mCount);
        if (recentDataArray == null || recentDataArray.length == 0) {
            return new ArrayList<AfMessageInfo>();
        }
        List<AfMessageInfo> recentDataList = Arrays.asList(recentDataArray);
        return recentDataList;
    }


    private void initData() {
        // TODO Auto-generated method stub
        optionsString = getResources().getStringArray(R.array.chatting_options);
        for (int i = 0; i < optionsString.length; i++) {
            EmotionModel map = new EmotionModel();
            map.setResId(optionsInt[i]);
            map.setText(optionsString[i]);
            optionsData.add(map);
        }
        loadData();
    }


    private String getEditTextContent() {
        return vEditTextContent.getText().toString();
    }

    private void setEditTextContent(String text) {
        vEditTextContent.setText(text);
    }

    private void setAdapter(AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        if (adapterListView == null) {
            adapterListView = new PopMessageAdapter(context, listChats, vListView );
//			vListView.setOnScrollListener(new ImageOnScrollListener(vListView, listViewAddOn));
            vListView.setAdapter(adapterListView);
        } else {
//			adapterListView.setList(listChats);
            if (afMessageInfo != null && MessagesUtils.ACTION_UPDATE == afMessageInfo.action) {
                PalmchatLogUtils.println("setAdapter  afMessageInfo.action ACTION_UPDATE");
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            } else if (afMessageInfo != null) {
                PalmchatLogUtils.println("setAdapter  afMessageInfo.action " + afMessageInfo.action);
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }
            adapterListView.notifyDataSetChanged();
            if ((mIsNoticefy && 0 < adapterListView.getCount()) || isBottom) {
                PalmchatLogUtils.println("Chatting  setAdapter");
                vListView.setSelection(adapterListView.getCount());
                mIsNoticefy = false;
            }
        }
    }


    private class GetDataTask extends AsyncTask<Void, Void, List<AfMessageInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<AfMessageInfo> doInBackground(Void... params) {
            List<AfMessageInfo> recentData = getRecentData();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return recentData;
        }

        @Override
        protected void onPostExecute(List<AfMessageInfo> result) {
            bindData(result);
            stopRefresh();
            super.onPostExecute(result);
        }


    }


    private void bindData(List<AfMessageInfo> result) {
        int size = result.size();
        int listSize = listChats.size();
        PalmchatLogUtils.println("bindData new addSize  " + size + " old listChats.size()  " + listSize + " old adapterListView.getCount()  " + adapterListView.getCount());
        if (size > 0) {
//    		listChats.addAll(0,result);
            for (int i = 0; i < result.size(); i++) {
                AfMessageInfo afMessageInfo = result.get(i);
                CommonUtils.getRealList(listChats, afMessageInfo);
                setVoiceQueue(afMessageInfo);
            }
            if (!mIsFirst) {
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
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


    private void setVoiceQueue(AfMessageInfo afMessageInfo) {
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
        int status = afMessageInfo.status;
        if (MessagesUtils.isReceivedMessage(status) && msgType == AfMessageInfo.MESSAGE_VOICE) {//from others chat and voice type
            CommonUtils.getRealList(listVoiceChats, afMessageInfo);
        }
    }


    private void stopRefresh() {
        // TODO Auto-generated method stub

    }


    private final Timer timer = new Timer();
    private TimerTask task;
    Handler timerhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情
            super.handleMessage(msg);
        }
    };

    private float mDownX;
    private float mDownY;

    private long mCurrentClickTime;
    private static final long LONG_PRESS_TIME = 500;

    class HoldTalkListener implements OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.talk_button) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下操作
                        //以下按下时的获取x,y坐标
                        mDownX = event.getRawX();
                        mDownY = event.getRawY();

                        PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_DOWN----");

                        if (!CommonUtils.isHasSDCard()) {
                            ToastManager.getInstance().show(PopMessageActivity.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
                            return false;
                        }
                        sent = false;
                        mCurrentClickTime = Calendar.getInstance().getTimeInMillis();

                        if (recordStart > 0) {//判断是否已录音
                            //发送录音消息
                            mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                            //停止定时器
                            stopRecordingTimeTask();
                            setMode(SharePreferenceService.getInstance(PopMessageActivity.this).getListenMode());
                            vButtonTalk.setSelected(false);
                            //停止录音
                            stopRecording();
                            sent = true;
                            return false;
                        }

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
                        //显示录音时间
                        createVoiceDialog();
                        PalmchatLogUtils.println("mVoiceName " + mVoiceName);
                        startRecordingTimeTask();
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        float offsetX1 = Math.abs(event.getRawX() - mDownX);
                        float offsetY1 = Math.abs(event.getRawY() - mDownY);

                        if (offsetX1 < ImageUtil.dip2px(context, 50) && offsetY1 < ImageUtil.dip2px(context, 50)) {
                            mCancelVoice.setText(R.string.empty);
                            mCancelVoice.setVisibility(View.GONE);
                            vButtonTalk.setSelected(false);
                            mVoiceProgressBar.setVisibility(View.VISIBLE);
                            mVoiceProgressBar2.setVisibility(View.GONE);
                            vTextViewRecordTime.setTextColor(getResources().getColor(R.color.log_blue));
//					show cancel voice text
                        } else { //显示取消提示
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

                        if (vTextViewRecordTime != null && vTextViewRecordTime.isShown() && !sent || (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime <= LONG_PRESS_TIME)) {
                            PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
                            if (CommonUtils.getSdcardSize()) {
                                ToastManager.getInstance().show(context, R.string.memory_is_full);
                            } else {

                                float offsetX = Math.abs(event.getRawX() - mDownX);
                                float offsetY = Math.abs(event.getRawY() - mDownY);

                                if (offsetX < ImageUtil.dip2px(context, 50) && offsetY < ImageUtil.dip2px(context, 50)) {//当手释放时，如果在此番外范围内，则直接发送语音
                                    PalmchatLogUtils.println("---- send voice----");
                                    if (recordStart > 0) {
                                        mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
                                    }
                                }
                            }
                            vTextViewRecordTime.setVisibility(View.GONE);
                            stopRecording();
                            stopRecordingTimeTask();
                        }
                        setMode(SharePreferenceService.getInstance(PopMessageActivity.this).getListenMode());

//					show cancelled text
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

    public void closeEmotions() {
        // TODO Auto-generated method stub
        vButtonKeyboard.setVisibility(View.GONE);
        if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
        	vImageEmotion.setVisibility(View.VISIBLE);
        }
        vTextViewRecordTime.setVisibility(View.GONE);
        emojjView.getViewRoot().setVisibility(View.GONE);
        setEmojiBg();
        PalmchatLogUtils.println("Chatting  closeEmotions  " + adapterListView.getCount());
        vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        vListView.setSelection(adapterListView.getCount());
    }

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
                    setMode(SharePreferenceService.getInstance(PopMessageActivity.this).getListenMode());
                    vButtonTalk.setSelected(false);
                    stopRecording();
                    sent = true;

                }
            });
            try {
                mRecorder.prepare();
                mRecorder.start();
                recordStart = System.currentTimeMillis();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Message message = new Message();
                        message.what = 1;
                        timerhandler.sendMessage(message);
                    }
                };
                timer.schedule(task, 0, 30000);
            } catch (Exception e) {
                PalmchatLogUtils.e("startRecording", "prepare() failed" + e.getMessage());
                e.printStackTrace();
                return null;
            }

            mRippleView.setVisibility(View.VISIBLE);
            mRippleView.setOriginRadius(vButtonTalk.getWidth() / 2);
            mRippleView.startRipple();

            vButtonBackKeyboard.setClickable(false);
            mVoiceImageEmotion.setClickable(false);
            mIsRecording = true;

        }
        return voiceName;
    }

    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mRecorder = null;
                recordEnd = System.currentTimeMillis();
                recordTime = Math.min((int) ((recordEnd - recordStart) / 1000), RequestConstant.RECORD_VOICE_TIME_BROADCAST);
                recordStart = 0;
                recordEnd = 0;
                vTextViewRecordTime.setVisibility(View.GONE);
                vTextViewRecordTime.setText("0s");
                mRippleView.setVisibility(View.GONE);
                mRippleView.stopRipple();

                vButtonBackKeyboard.setClickable(true);
                mVoiceImageEmotion.setClickable(true);

                mIsRecording = false;

                mVoiceProgressBar.setVisibility(View.VISIBLE);
                mVoiceProgressBar2.setVisibility(View.GONE);
                mVoiceProgressBar.setProgress(0);
                mVoiceProgressBar2.setProgress(0);
            }
        }
    }

    private void createVoiceDialog() {
        vTextViewRecordTime.setVisibility(View.VISIBLE);
        vTextViewRecordTime.setText("0s");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
/*            case KeyEvent.KEYCODE_MENU:
                if (mFriendAfid != null && !mFriendAfid.startsWith(DefaultValueConstant._R)) {
                    int listenMode = SharePreferenceService.getInstance(PopMessageActivity.this).getListenMode();
                    addAction(listenMode, mFriendAfid);
                    widget.show();
                }
                return true;*/
            case KeyEvent.KEYCODE_BACK:
                vButtonKeyboard.setVisibility(View.GONE);
                if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
                	vImageEmotion.setVisibility(View.VISIBLE);
                }
                if (emojjView.getViewRoot().getVisibility() != View.GONE) {
                    emojjView.getViewRoot().setVisibility(View.GONE);
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
                    addToCache(messageInfo);
                } else if (action == MessagesUtils.ACTION_UPDATE) {
                    afMessageInfo.action = action;
                    handler.obtainMessage(handler_MsgType, afMessageInfo).sendToTarget();
//					addToCache(afMessageInfo);
                    //更新当前消息的状态
                    MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);

                }
            }

        }).start();

//		return messageInfo;
    }

    private void addToCache(final AfMessageInfo messageInfo) {
        MessagesUtils.insertMsg(messageInfo, true, true);
    }

    /**
     * 输入框内显示表情
     *
     * @param id
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
            Log.e("ttt->", spannableString.length() + "->" + vEditTextContent.length());
            int length = spannableString.length() + vEditTextContent.length();
            if (length < DefaultValueConstant.MAX_SIZE) {
                int index = vEditTextContent.getSelectionStart();
                Editable editable = vEditTextContent.getText();
                editable.insert(index, spannableString);
            }
        }
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
                if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
                	vImageEmotion.setVisibility(View.VISIBLE);
                }
                emojjView.getViewRoot().setVisibility(View.GONE);
                //vImageViewMore.setVisibility(View.VISIBLE);
                changeMode();
                vListView.setSelection(adapterListView.getCount());
                vEditTextContent.requestFocus();
                CommonUtils.showSoftKeyBoard(vEditTextContent);
                break;
            case R.id.image_emotion://emotion button
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                emojjView.getViewRoot().setVisibility(View.VISIBLE);
                vButtonKeyboard.setVisibility(View.VISIBLE);
                vImageEmotion.setVisibility(View.GONE);
                setEmojiBg();
//			emojjView.getViewRoot().setFocusableInTouchMode(true);
                changeMode();
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vListView.setSelection(adapterListView.getCount());
                    }
                },20);
                break;

            case R.id.image_emotion2:
                toShowVoice();
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                emojjView.getViewRoot().setVisibility(View.VISIBLE);
                setEmojiBg();
                vButtonKeyboard.setVisibility(View.VISIBLE);
                vImageEmotion.setVisibility(View.GONE);
//			emojjView.getViewRoot().setFocusableInTouchMode(true);
                changeMode();
                vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                vListView.setSelection(adapterListView.getCount());
                break;

            case R.id.chatting_message_edit://onclick on eidttext
                closeEmotions();
                break;
            case R.id.pophead_exit:
                back();
                break;
            default:
                break;
        }
    }


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
        } else {
            viewOptionsLayout.setVisibility(View.GONE);
            viewChattingVoiceLayout.setVisibility(View.VISIBLE);
            CommonUtils.closeSoftKeyBoard(vEditTextContent);
        }
        closeEmotions();
    }


    private void back() {
        InputMethodManager imm = (InputMethodManager) PalmchatApp.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(vEditTextContent.getWindowToken(), 0);
        clearData();

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startActivity(startMain);
    }

    private void clearData() {
        isOnClick = false;
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }

        finish();
        PopMessageActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        CacheManager.getInstance().getPopMessageManager().clearCount();
        app.setPopMessageReceiver(null);
        CacheManager.getInstance().getPopMessageManager().setmFirstPopMsg(null);

        CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().clear();
        CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().put(mFriendAfid, true);
    }

    //send textOrEmotion
    private void sendTextOrEmotion() {

        String content = getEditTextContent();

        if (content.length() > 0) {

            if (EmojiParser.getInstance(PalmchatApp.getApplication())
                    .hasEmotions(content)) {
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);

//                MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.S_M_EMOTION);

            }

            resetEditTextContent();
            sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_INSERT, null, content);

            back();

        }
    }


    private void resetEditTextContent() {
        // TODO Auto-generated method stub
        vEditTextContent.setText("");
    }


    public void resendTextOrEmotion(AfMessageInfo afMessageInfo) {
        sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_UPDATE, afMessageInfo, afMessageInfo.msg);
    }

    private void send(String content, AfMessageInfo msgInfo) {
        // heguiming 2013-12-04
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);

//        MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.S_M_TEXT);
//        MobclickAgent.onEvent(PopMessageActivity.this, ReadyConfigXML.P_NUM);

        int code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.toAfId, System.currentTimeMillis(), content, Consts.MSG_CMMD_NORMAL, msgInfo._id, this);
        sendRequestError(msgInfo, code, Consts.SEND_MSG);
    }

    private void forwardRequest(AfMessageInfo msgInfo) {

        if (msgInfo.fid != null) {

            CommonUtils.getRealList(listChats, msgInfo);
            setAdapter(msgInfo);
            int code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.getKey(), System.currentTimeMillis(), msgInfo.fid, Consts.MSG_CMMD_FORWARD, msgInfo, PopMessageActivity.this);
            sendRequestError(msgInfo, code, Consts.SEND_MSG);

        } else {
            forwardFailure(msgInfo);
        }

    }

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


    public void updateStatus(final int msgId, final int status, final String fid) {
        int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
        for (AfMessageInfo af : adapterListView.getLists()) {
            PalmchatLogUtils.println("af.id  " + af._id);
        }
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
     * zhh 发送完图片后，更新列表消息发送状态
     *
     * @param msgId
     * @param status
     * @param fid
     * @param progress
     */
    private void updateStautsForImage(final int msgId, int status, String fid, int progress) {
        int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
        if (msgId != -1 && index == -1) {
            AfMessageInfo msg = CacheManager.getInstance().getAfMessageInfo();
            if (msg != null && mFriendAfid.equals(msg.toAfId)) {
                if (msg != null && msgId == msg._id) {
                    PalmchatLogUtils.println("Chatting  updateImageStautsByBroadcast  ");
                    CommonUtils.getRealList(adapterListView.getLists(), msg);
                    CacheManager.getInstance().setAfMessageInfo(null);

                    updateStautsForImage(msgId, status, fid, progress);
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
        int key = 0;
        if (Consts.REQ_CODE_SUCCESS == code) {
            switch (flag) {
                case Consts.REQ_MSG_SEND:
                    msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
                    PalmchatLogUtils.println("onresult msg_id  " + msg_id);
                    if (msg_id > DefaultValueConstant.LENGTH_0) {
                        String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
                        if (!isPublicMember) {
                            updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT, fid);
                        } else {
                            updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT_AND_READ, fid);
                        }
                    } else if (msg_id == Consts.REQ_CODE_READ) {
                        PalmchatLogUtils.println("case Consts.REQ_CODE_READ code " + code + "  flag  " + flag);
                    } else {
                        //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                        ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                        PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
                    }
                    break;
                case Consts.REQ_VOICE_SEND:
                    msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
                    if (msg_id > DefaultValueConstant.LENGTH_0) {
                        updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT, DefaultValueConstant.MSG_INVALID_FID);
                    } else {
                        //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                        ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                        PalmchatLogUtils.println("case Consts.REQ_VOICE_SEND code " + code + "  flag  " + flag);
                    }
                    break;
                case Consts.REQ_MEDIA_UPLOAD://media upload
                {
                    AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
                    if (afMessageInfo != null) {
                        final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                        if (afAttachImageInfo != null) {
                            String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
                            updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid, afAttachImageInfo);
                            updateStautsForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, afMessageInfo.fid, afAttachImageInfo.progress);
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
                    break;
                }
                case Consts.REQ_GET_INFO://get profile(info) success
                {
                    AfProfileInfo afProfileInfo = (AfProfileInfo) result;
                    AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
                    if (afProfileInfo != null && afMessageInfo != null) {
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
                        updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT, ((AfMessageInfo) user_data).fid);
                    } else {
                        //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
                        ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
                        PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
                    }

                    break;
                }

                default:
                    break;
            }
        } else {
//			dismissProgressDialog();
            boolean isRead = false;
            switch (flag) {
                case Consts.REQ_MSG_SEND:
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
                case Consts.REQ_GET_INFO://get profile(info) failure
                {

                    break;
                }

//			forward msg
                case Consts.REQ_MSG_FORWARD: {

                    if (code != Consts.REQ_CODE_UNNETWORK) {

                        if (user_data != null && user_data instanceof AfMessageInfo) {
                            forwardFailure((AfMessageInfo) user_data);
                        }
                    }

                    String user_data2 = String.valueOf(((AfMessageInfo) user_data)._id);
                    sendMsgFailure(flag, code, user_data2);

                    break;
                }
                default:
                    break;
            }
            if (Consts.REQ_CODE_MEDIA_SESSTION_TIMEOUT != code && !isRead) {
                if (CommonUtils.isChatting(this)) {
                    Consts.getInstance().showToast(context, code, flag, http_code);
                }
            }
        }
    }


    /**
     * send image error
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
            updateStautsForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
            int handle = mAfCorePalmchat.AfHttpSendImage(afAttachImageInfo.upload_info, afMessageInfo, this, this);
            sendRequestError(afMessageInfo, handle, Consts.SEND_IMAGE);
        } else if (afImageReqInfo != null && afMessageInfo != null) {
            AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
            if (afAttachImageInfo != null) {
                AfImageReqInfo imageReqInfo = afAttachImageInfo.upload_info;
                afImageReqInfo._id = imageReqInfo._id;
                afAttachImageInfo.upload_info = afImageReqInfo;

                updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo);
                updateStautsForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);

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
            updateStautsForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);

            new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
        }
    }


    /**
     * send voice error
     */
    private void sendVoiceFailure(int flag, int code, Object user_data) {
        int msg_id;
        msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
        if (msg_id > DefaultValueConstant.LENGTH_0) {
            updateStatus(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
        } else {
            //updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
            ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
            PalmchatLogUtils.println("else Consts.REQ_VOICE_SEND else code " + code + "  flag  " + flag);
        }
    }


    /**
     * send message error
     */
    private boolean sendMsgFailure(int flag, int code, Object user_data) {
        int msg_id;
        msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
        if (msg_id > DefaultValueConstant.LENGTH_0) {
            updateStatus(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
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


    void updateStatusForCamera(int sendImage, AfMessageInfo afMessageInfo) {
        System.out.println("MessagesUtils.CAMERA_MESSAGE.equals(action)");
//					modify by wk
        ArrayList<AfMessageInfo> tmpList = CacheManager.getInstance().getmImgSendList();
        for (AfMessageInfo msg : tmpList) {
            mainHandler.obtainMessage(MessagesUtils.SEND_IMAGE, msg).sendToTarget();
        }
        CacheManager.getInstance().clearmImgSendList();


    }


    @Override
    public void AfOnProgress(int httpHandle, int flag, final int progress, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("ywp: AfhttpHandleOnProgress httpHandle =" + httpHandle + " flag=" + flag + " progress=" + progress + " user_data = " + user_data);
        switch (flag) {
            case Consts.REQ_MEDIA_UPLOAD:
                final AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
                if (afMessageInfo != null) {
                    final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
                    afAttachImageInfo.progress = progress;
                    if (progress == 100) {
                        //updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT,afAttachImageInfo);
                        PalmchatLogUtils.println("100 progress  " + progress);
                    } else {
                        PalmchatLogUtils.println("progress  " + progress);
                        updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo);
                        updateStautsForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo.progress);
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            mAfCorePalmchat.AfDbAttachImageUpdateProgress(afAttachImageInfo._id, afAttachImageInfo.progress);
                        }
                    }).start();
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PalmchatLogUtils.println("onActivityResult--->>" + requestCode + "  data  " + data);
        emojjView.getViewRoot().setVisibility(View.GONE);
        setEmojiBg();

        if (resultCode == 0) {
            return;
        }
        if (requestCode == MessagesUtils.CAMERA) {
            PalmchatLogUtils.println("rotation==" + getWindowManager().getDefaultDisplay().getRotation());
            try {
                if (cameraFilename == null) {
                    cameraFilename = SharePreferenceService.getInstance(this).getFilename();
                    f = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
                } else {
                    SharePreferenceService.getInstance(this).clearFilename();
                }
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
        } else if (requestCode == MessagesUtils.PICTURE) {
            if (data != null) {
                String path = "";
                Cursor cursor = null;
                Uri originalUri = data.getData();
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
                f = new File(path);
                File f2 = FileUtils.copyToImg(f.getAbsolutePath());
                if (f2 != null) {
                    f = f2;
                }
                cameraFilename = f2.getAbsolutePath();//f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                smallImage(f, MessagesUtils.PICTURE);
                cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
                if (CommonUtils.isEmpty(cameraFilename)) {
                    mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
                    return;
                }

                PalmchatLogUtils.e("cameraFilename=", cameraFilename);
//					modify by wk
//					new PreviewImageDialog(Chatting.this, path, mainHandler, cameraFilename).show();

                mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
            }

        } else if (resultCode == 10) {
            // 好友名片
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
//					afMessageInfo.status = AfMessageInfo.MESSAGE_READ;
//					adapterListView.notifyDataSetChanged();
                    adapterListView.getLists().set(index, afMessageInfo);
                }
            }
        } 

		
		/* add by zhh 从相册页面返回 */
        if (requestCode == ALBUM_REQUEST && resultCode == Activity.RESULT_OK) { // 相册页面拍照返回
            PalmchatLogUtils.println("rotation==" + getWindowManager().getDefaultDisplay().getRotation());
            if (data != null) {
                File f;
                String cameraFilename = data.getStringExtra("cameraFilename");
                try {
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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
                opts.inSampleSize = 4;
            } else if (len > 10 * 100 * 100) {
                opts.inSampleSize = 2;
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

        if (bitmap.getWidth() > bitmap.getHeight()) {
            newBitmap = ImageUtil.zoomBitmap(bitmap, CommonUtils.dip2px(this, 96), CommonUtils.dip2px(this, 80));
        } else {
            newBitmap = ImageUtil.zoomBitmap(bitmap, CommonUtils.dip2px(this, 80), CommonUtils.dip2px(this, 96));
        }
        String smallFilename = ClippingPicture.saveTalkBitmap(newBitmap, mAfCorePalmchat);
        if (newBitmap != null && !newBitmap.isRecycled()) {
            newBitmap.recycle();
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return smallFilename;
    }


    private long lastSoundTime;

    @Override
    public void handlePopMessageFromServer(final AfMessageInfo afMessageInfo) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("Chatting " + afMessageInfo);
        if (MessagesUtils.isGroupSystemMessage(afMessageInfo.type)) {
            PalmchatLogUtils.println("Chatting isGroupSystemMessage return" + afMessageInfo.msg);
            return;
        }

        PalmchatLogUtils.println("--rrr PopChatActvity handleMessageFromServer id:" + afMessageInfo.getKey() + "  msg:" + afMessageInfo.msg);

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }

        mWakeLock.release();

        setVoiceQueue(afMessageInfo);
        final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
        if (mFriendAfid.equals(afMessageInfo.fromAfId) && (MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type))) {
            if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ) {
//				friendRequest(afMessageInfo);
            } else {
                PalmchatLogUtils.println("handleMessageFromServer  begin  System.currentTimeMillis() - lastSoundTime  " + (System.currentTimeMillis() - lastSoundTime));
                if (PopMessageActivity.class.getName().equals(CommonUtils.getCurrentActivity(this))) {
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
                    CommonUtils.getRealList(listChats, afMessageInfo);
//						showVibrate();
                    setAdapter(null);
                }
                toSendRead = true;
                handleMessageForReadFromServer(new AfMessageInfo(afMessageInfo.fromAfId));
            }

            PalmchatLogUtils.println("--www111 :" + this + " list size " + listChats.size() + "  msg " + afMessageInfo.msg);

        } else {

            PalmchatLogUtils.println("--rrr handleMessage mFriendAfid  != afMessageInfo.toAfId mFriendAfid:" + mFriendAfid + " afMsgId:" +
                    afMessageInfo.getKey());

        }
    }


    private void showVibrate() {
        // TODO Auto-generated method stub
        if (app.getSettingMode().isVibratio()) {
            if ((System.currentTimeMillis() - lastSoundTime) > 4000 || lastSoundTime == 0) {
                TipHelper.vibrate(context, 200L);
            }
            lastSoundTime = System.currentTimeMillis();
            PalmchatLogUtils.println("handleMessageFromServer  end  lastSoundTime  " + lastSoundTime);
        }
    }

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
                addToCache(afMessageInfo);
            }
        }).start();

    }

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
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }
        chattingHandler.removeCallbacks(runnable);
        super.onStop();
    }


    @Override
    protected void onDestroy() {

//        PalmchatApp.getApplication().getHandlerMap().remove(PopMessageActivity.class.getCanonicalName());

        super.onDestroy();
        if(mScreenObserver != null) {//停止监听广播
            mScreenObserver.stopScreenStateUpdate();
        }
        if(mHomeBroadcast != null){
            mHomeBroadcast.unRegisterBroadcastReceiver();
        }
        //SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setIsDoubleScreen(CacheManager.getInstance().getMyProfile().afId,false);
    }


    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        if (!isRefreshing) {
            isRefreshing = true;
            mOffset = listChats.size();
            new GetDataTask().execute();
        }
    }


    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub

    }


    /**
     * delete
     */
    public void delete(final AfMessageInfo afMessageInfo) {
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
            if (toSendRead) {
                toSendRead = false;
                mAfCorePalmchat.AfHttpSendMsg(mFriendAfid, System.currentTimeMillis(), null, Consts.MSG_CMMD_READ, Consts.REQ_CODE_READ, PopMessageActivity.this);
            }
            chattingHandler.postDelayed(this, READ_INTERVAL_TIME);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("Chatting");
//        MobclickAgent.onResume(this);

        getData();
        CommonUtils.cancelNoticefacation(getApplicationContext());

        String msgContent = mAfCorePalmchat.AfDbGetMsgExtra(mFriendAfid);
        PalmchatLogUtils.println("mFriendAfid  " + mFriendAfid + "  msgContent  " + msgContent);
//		setEditTextContent(msgContent);

        CharSequence text = EmojiParser.getInstance(context).parse(msgContent);
        if (vEditTextContent != null) {
            vEditTextContent.setText(text);
        }

        if (!CommonUtils.isEmpty(mFriendAfid) && !mFriendAfid.startsWith(RequestConstant.SERVICE_FRIENDS)) {//Palmchat team
            chattingHandler.postDelayed(runnable, READ_INTERVAL_TIME);
        }

    }


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

    private void loadData() {
        listChats.clear();
        List<AfMessageInfo> listTemp = getRecentData();
        for (AfMessageInfo afMessageInfo : listTemp) {
            int status = afMessageInfo.status;
            if (MessagesUtils.isReceivedMessage(status)) {
                toSendRead = true;
                break;
            }
        }
        if (toSendRead) {
            sendAlreadyRead();
        }
        bindData(listTemp);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        MobclickAgent.onPageEnd("Chatting"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//        MobclickAgent.onPause(this);
        mIsFirst = true;
        mOffset = 0;
        int dbInt = mAfCorePalmchat.AfDbSetMsgExtra(mFriendAfid, getEditTextContent());
        PalmchatLogUtils.println("mFriendAfid  " + mFriendAfid + "  onPause  dbInt " + dbInt + "  getEditTextContent  " + getEditTextContent());

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
                    ToastManager.getInstance().show(PopMessageActivity.this, getString(R.string.switch_speaker_mode));
                }
                SharePreferenceService.getInstance(PopMessageActivity.this).setListenMode(AudioManager.MODE_NORMAL);
                break;
            case ChattingMenuDialog.ACTION_HANDSET_MODE:
                AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
                audio.setSpeakerphoneOn(false);
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                audio.setMode(AudioManager.MODE_IN_CALL);
                if (!mIsFirst) {
                    ToastManager.getInstance().show(PopMessageActivity.this, getString(R.string.switch_handset_mode));
                }
                SharePreferenceService.getInstance(PopMessageActivity.this).setListenMode(AudioManager.MODE_IN_CALL);
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
        // TODO Auto-generated method stub
        String str = getEditTextContent();
        str = str + "@" + name + ":";
        setEditTextContent(str);

        vEditTextContent.requestFocus();
        if (!TextUtils.isEmpty(str)) {
            vEditTextContent.setSelection(str.length());
        }
        CommonUtils.showSoftKeyBoard(vEditTextContent);
    }


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
     * 删除表情
     */
    public void emojj_del() {
        // TODO Auto-generated method stub
        CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);

    }

    /**
     * 双击事件、多击事件
     */
    //存储时间的数组
    long[] mHits = new long[2];

    public void doubleClick() {
        // 双击事件响应
        //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        //双击事件的时间间隔500ms
        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
            PalmchatLogUtils.i("pophj", "test doubleClick");
            AfFriendInfo afF = CacheManager.getInstance().searchAllFriendInfo(mFriendAfid);
            if (afF != null) {
                PalmchatApp.getApplication().isDoubleClickScreen = true;
                toChatting(afF, mFriendAfid, afF.name);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mAfCorePalmchat.AfRecentMsgSetUnread(mFriendAfid, 0);
                        MessagesUtils.setUnreadMsg(mFriendAfid, 0);
                    }
                }).start();
            }
        }
    }
}
