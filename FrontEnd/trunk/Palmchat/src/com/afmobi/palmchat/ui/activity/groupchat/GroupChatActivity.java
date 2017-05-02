package com.afmobi.palmchat.ui.activity.groupchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseChattingActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.PalmchatApp.MessageReceiver;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.ui.activity.friends.LocalSearchActivity;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
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
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.LaunchActivity;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.ActivityBackgroundChange;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.ForwardSelectActivity;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.chats.SelectListActivity; 
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.groupchat.adapter.GroupChattingAdapter;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.ChattingMenuDialog;
import com.afmobi.palmchat.ui.customview.CutstomEditText;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.FaceFooterView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout;
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
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.StartTimer;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.TipHelper;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.ZipFileUtils;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfAttachImageInfo;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpNotifyMsg;
import com.core.AfGrpProfileInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfImageReqInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import de.greenrobot.event.EventBus;

/**
 * @author heguiming modify 2013-12-13
 */
@SuppressLint("NewApi")
public class GroupChatActivity extends BaseChattingActivity implements OnClickListener, AfHttpResultListener, AfHttpProgressListener
, OnEditorActionListener, MessageReceiver, IXListViewListener, OnItemClick, OnItemLongClick {
	private static final String TAG = GroupChatActivity.class.getCanonicalName();
	private static final int BACK_FROM_SET_CHAT_BG = 223334;
	public final static int ACTION_INSERT = 0;
	public final static int ACTION_UPDATE = 1;
	private final static int EDIT_TEXT_CHANGE = 1;
	private GroupChattingAdapter adapterListView;
	private XListView vListView;
	private ArrayList<AfMessageInfo> listChats = new ArrayList<AfMessageInfo>();
	private ArrayList<AfMessageInfo> listVoiceChats = new ArrayList<AfMessageInfo>();
	private int unreadTipsCount;
	private CutstomEditText vEditTextContent;
	private ImageView vButtonBackKeyboard, vButtonKeyboard, vButtonLeftKeyboard;
	private ImageView vButtonSend;

	private boolean isForward;
	private ImageView vImageEmotion, vImageViewBack, vImageViewMore;
	private View vImageViewVoice;
	private View viewUnTalk, viewFrameToast;
	private View viewOptionsLayout, viewChattingVoiceLayout;
	private Button vButtonTalk;
	private ImageView vImageViewRight;
	private boolean sent;
	private MediaRecorder mRecorder;
	private long recordStart;
	private long recordEnd;
	private int recordTime;
	private TextView vTextViewRecordTime;
	private TextView mCancelVoice;
	private View edit_group_name;
	private ImageView et_exit;
	private EditText et_group_name;

	/* emotion */
	private LinearLayout chattingOptions;
	private LinearLayout img_picture, img_camera, img_voice, img_name_card, img_video, img_background;
	private ImageView img_picture_p, img_camera_c, img_voice_v, img_name_card_n, img_video_v, img_background_v;

	private String cameraFilename;
	private File f;
	private LinearLayout chatting_emoji_layout;
	private String mVoiceName;
	private static final int CAMERA = 1;
	public static final int TITLE = 7;
	private static final int MSG_TOO_SHORT = -2;
	private static final int NOTICEFY = 123212;
	byte[] data;
	ChattingMenuDialog widget;
	private TextView vTextViewOtherMessageToast, vTextViewOtherMessageColon, vTextViewOtherMessageName;
	private boolean isCreate = false;
	private boolean isNameChanged = false;
	private boolean isCanSave = false;
	private int mOffset = 0;
	private int mCount = 20;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
	private boolean mIsFirst = true;
	private boolean mIsNoticefy = true;
	private boolean isRefreshing = false;
	private boolean isInResume = false;
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	public static final int FINISH = 8000;
	private static final int RECORDING_TIMER = 9000;

	private final static int AT_LEAST_LENGTH = 60;
	RippleView mRippleView;
	private ImageView mVoiceImageEmotion, mVoiceImageViewMore;
	private ProgressBar mVoiceProgressBar, mVoiceProgressBar2;
	private final static int STORE_FACE_DOWNLOAD = FINISH + 1;

	private final int ALBUM_REQUEST = 3; // add by zhh 发送请求到相册页面
	private boolean mIsRecording;
	/**
	 * 从私聊页面创建的群
	 */
	private boolean mIsSingleChat;
	private LooperThread looperThread;
	private String groupID;
	private int status = Consts.AFMOBI_GRP_STATUS_ACTIVE;
	private TextView title_text;
	private Bundle bundle = null;
	private boolean isBottom = false;
	private KeyboardListenRelativeLayout chat_root;
	private boolean back_to_default = false;
	private String forward_imagePath;
	private View view_unread;
	private TextView textview_unread;
	private String groupId;
	private String roomName;
	private int gversion;
	/**
	 * 是否从popMessageActivity进入此页面
	 */
	private boolean isFromPop;

	private ScreenObserver mScreenObserver;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 埋点 友盟统计
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_G);
		if(savedInstanceState != null) {
			mIsForworded = savedInstanceState.getBoolean("isforword");
		}
/*		*//* 从分享或者转发图片进入此页面 *//*
		shareImage();*/
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_G);
		// 创建消息队列
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
		super.onCreate(savedInstanceState);
		// 根据机型隐藏导航条
		if (systemBarConfig.hasNavigtionBar()) {
			if (PalmchatApp.getOsInfo().getUa().contains("HTC")// ||
					// PalmchatApp.getOsInfo().getUa().contains("MI")
					|| PalmchatApp.getOsInfo().getBrand().contains("Meizu")) {// 过滤htc,小米
				// meizu
				chat_root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				tintManager.setNavigationBarTintEnabled(false);
			}
		}
		PalmchatLogUtils.println("--cccddd group oncreate " + this);
	}

	@Override
	public void findViews() {
		// EventBus注册
		EventBus.getDefault().register(this);
		/* 设置声音模式 */
		setListenMode();
		bundle = getIntent().getExtras();
		if (bundle != null) {
			/* group status 正在群聊页面/退出群聊页面 */
			status = bundle.getInt(JsonConstant.KEY_STATUS, Consts.AFMOBI_GRP_STATUS_ACTIVE);
			/* 是否从分享图片进入 */
			isShareImage = bundle.getBoolean(JsonConstant.KEY_SHARE_IMG, false);
			/* 分享图片路径 */
			shareImageUri = (ArrayList<String>) bundle.getSerializable(JsonConstant.KEY_SHARE_IMG_URI);

			/* 群ID */
			groupID = bundle.containsKey(BundleKeys.ROOM_ID) ? bundle.getString(BundleKeys.ROOM_ID) : null;
			mFriendAfid = groupID;
			groupId = groupID;
			/* 群名 */
			roomName = bundle.getString(GroupChatActivity.BundleKeys.ROOM_NAME);
			if (bundle.containsKey("singlechat")) {
				mIsSingleChat = bundle.getBoolean("singlechat");
			}
			/* 是否创建群 */
			isCreate = bundle.getBoolean("is_create", false);
			isInResume = bundle.getBoolean(JsonConstant.KEY_FLAG);
			/* 获取图片转发路径 */
			forward_imagePath = bundle.getString(JsonConstant.KEY_BC_FORWARD_IMAGEPAHT);
			isFromPop = bundle.getBoolean(JsonConstant.KEY_FROM_POP_MESSAGE_TO_CHATTING, false);
			if(isFromPop){
				if(!CommonUtils.isScreenLocked(PalmchatApp.getApplication().getApplicationContext())){
					PalmchatApp.getApplication().isDoubleClickScreen = false;
				}
				mScreenObserver = new ScreenObserver(this, new ScreenObserver.ScreenStateListener() {
					@Override
					public void onScreenOn() { //亮屏
						PalmchatLogUtils.i("hj","GroupChat BroadcastReceiveronScreenOn");
						PalmchatApp.getApplication().isDoubleClickScreen = false;
					}

					@Override
					public void onScreenOff() {//灭屏
						PalmchatLogUtils.i("hj","GroupChat BroadcastReceiveronScreenOff");
						PalmchatApp.getApplication().isDoubleClickScreen = false;
					}

					@Override
					public void onUserPresent() {//解锁成功
						PalmchatLogUtils.i("hj","GroupChat BroadcastReceiveronUserPresent");
						PalmchatApp.getApplication().isDoubleClickScreen = false;
					}
				});
				PalmchatLogUtils.i("hj", "ChattingdoubleScreen");
			}
		}
		/* 关闭当前页面时，是否退至默认页面 */
		back_to_default = getIntent().getBooleanExtra(JsonConstant.KEY_BACK_TO_DEFAULT, false);

		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		setContentView(R.layout.activity_groupchatting);
		PalmchatLogUtils.println("GroupChatActivity  groupID  " + groupID);
		chat_root = (KeyboardListenRelativeLayout) findViewById(R.id.chat_root);
		vListView = (XListView) findViewById(R.id.listview);
		vListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

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
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Top  true");
				}
				PalmchatLogUtils.println("GroupChatActivity  visibleItemCount  " + visibleItemCount + "firstVisibleItem  " + firstVisibleItem + "totalItemCount  " + totalItemCount + "  vListView.getLastVisiblePosition()  " + vListView.getLastVisiblePosition());

				if (visibleItemCount + firstVisibleItem >= totalItemCount - 1) {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Bottom  true");
					hideUnreadTips();
					isBottom = true;
				} else {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Bottom  false");
					isBottom = false;
				}
			}
		});
		vListView.setPullLoadEnable(true);
		vListView.setXListViewListener(this);
		mCancelVoice = (TextView) findViewById(R.id.cancel_voice_note);
		vTextViewRecordTime = (TextView) findViewById(R.id.text_show);
		vEditTextContent = (CutstomEditText) findViewById(R.id.chatting_message_edit);
		vEditTextContent.setOnClickListener(this);
		vEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					closeEmotions();
				}
			}
		});

		edit_group_name = findViewById(R.id.r_et);
		et_group_name = (EditText) findViewById(R.id.et_group_name);
		et_group_name.setOnClickListener(this);
		et_group_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					//emojjView.getViewRoot().setVisibility(View.GONE);
					closeEmotions();
				}
			}
		});
		et_group_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Editable editable = et_group_name.getText();
				int len = editable.length();
				if (len > 0) {
					et_exit.setImageResource(R.drawable.button_enter);
					isCanSave = true;
				} else {
					et_exit.setImageResource(R.drawable.button_exit);
					isCanSave = false;
				}
			}

		});

		if (isCreate) {//如果是新创建的群，可以在此页面显示修改群名
			edit_group_name.setVisibility(View.VISIBLE);
		} else {
			edit_group_name.setVisibility(View.GONE);
		}

		et_exit = (ImageView) findViewById(R.id.et_exit);
		et_exit.setOnClickListener(this);
		vEditTextContent.addTextChangedListener(new LimitTextWatcher(vEditTextContent, ReplaceConstant.MAX_SIZE, mainHandler, EDIT_TEXT_CHANGE));
		vEditTextContent.setMaxLength(ReplaceConstant.MAX_SIZE);
		vEditTextContent.setOnEditorActionListener(this);
		vEditTextContent.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
					PalmchatLogUtils.e(TAG, "GroupChatActivity  keyCode==KEYCODE_ENTER==" + keyCode);
				}
				return false;
			}
		});
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(roomName);
		vImageViewRight = (ImageView) findViewById(R.id.op2);
		vImageViewRight.setVisibility(View.VISIBLE);
		vImageViewRight.setBackgroundResource(R.drawable.group_detail);
		vImageViewRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("come_page", "multichat_page");
				b.putString("room_id", groupID);
				b.putString("room_name", roomName);
				b.putBoolean("isForward",isForward);
				jumpToPage(EditGroupActivity.class, b, true, TITLE, false);

			}
		});

		vTextViewOtherMessageName = (TextView) findViewById(R.id.textview_name);
		vTextViewOtherMessageColon = (TextView) findViewById(R.id.textview_colon);
		vTextViewOtherMessageToast = (TextView) findViewById(R.id.textview_message);

		mVoiceImageEmotion = (ImageView) findViewById(R.id.image_emotion2);
		mVoiceImageEmotion.setOnClickListener(this);
		mVoiceImageViewMore = (ImageView) findViewById(R.id.chatting_operate_two2);
		mVoiceImageViewMore.setOnClickListener(this);
		mVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_progress);
		mVoiceProgressBar2 = (ProgressBar) findViewById(R.id.voice_progress2);
		mRippleView = (RippleView) findViewById(R.id.ripple);

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
		vImageViewMore = (ImageView) findViewById(R.id.chatting_operate_two);
		vImageViewMore.setOnClickListener(this);
		vButtonTalk = (Button) findViewById(R.id.talk_button);
		vButtonTalk.setOnTouchListener(new HoldTalkListener());
		vImageEmotion = (ImageView) findViewById(R.id.image_emotion);
		vImageEmotion.setOnClickListener(this);
		chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);

		emojjView = new EmojjView();
		emojjView.setView(GroupChatActivity.this);
		/* 底部弹出工具对话框 */
		mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//emojjView = new EmojjView(GroupChatActivity.this);
				emojjView.initViews();
				emojjView.select(EmojiParser.SUN);
				chatting_emoji_layout.addView(emojjView.getViewRoot());
				emojjView.getViewRoot().setVisibility(View.GONE);
				faceFooterView = new FaceFooterView(GroupChatActivity.this, emojjView);
				faceFooterView.addGifFooter();
				emojjView.setFaceFooterView(faceFooterView);
				boolean showVoice = CacheManager.getInstance().getShowChattingVoice(mFriendAfid);
				if (showVoice && !isCreate && !isFromPop) {
					vImageViewMore.performClick();
				}
				else if(isFromPop){
					vEditTextContent.requestFocus();
				}
			}
		}, 200);
		viewOptionsLayout = findViewById(R.id.chatting_options_layout);
		viewChattingVoiceLayout = findViewById(R.id.chatting_voice_layout);
		chattingOptions = (LinearLayout) findViewById(R.id.chatting_item_option);
		img_picture = (LinearLayout) findViewById(R.id.img_picture);
		img_camera = (LinearLayout) findViewById(R.id.img_camera);
		img_voice = (LinearLayout) findViewById(R.id.img_voice);
		img_name_card = (LinearLayout) findViewById(R.id.img_name_card);
		img_video = (LinearLayout) findViewById(R.id.img_video);
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

		view_unread = findViewById(R.id.view_unread);
		view_unread.setOnClickListener(this);
		textview_unread = (TextView) findViewById(R.id.textView_unread);
		chattingOptions.setVisibility(View.GONE);
		vImageViewBack = (ImageView) findViewById(R.id.back_button);
		vImageViewBack.setOnClickListener(this);
		if (status == Consts.AFMOBI_GRP_STATUS_EXIT) {
			vImageViewRight.setBackgroundResource(R.drawable.group_enable);
			vImageViewRight.setClickable(false);
		}
		/* 判断内存是否已满 */
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
		/* 设置聊天窗口背景 */
		setChatBg();

	}

	@Override
	public void init() {
		adapterListView = new GroupChattingAdapter(this, listChats, vListView );
		adapterListView.setOnItemClick(this);
		vListView.setAdapter(adapterListView);
		// if is from forward msg
		isForward = getIntent().getBooleanExtra(JsonConstant.KEY_FORWARD, false);
		/* 初始化数据，从服务器获取最近20条聊天记录 */
		getMsgData(true);
//		new GetDataTask(true).execute();//OnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		/* 从分享或者转发图片进入此页面 */
		looperThread.handler.sendEmptyMessageDelayed(LooperThread.SHARE_IMAGE,150);
		//shareImage();
		PalmchatApp.getApplication().addMessageReceiver(this);
	}

	/** 获取聊天语音聊表
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
	  /**
	   * 异步读取数据库
	   * @param isInit
	   */
	    private void getMsgData(boolean isInit){
	    	if (looperThread != null && looperThread.handler != null) {
	            looperThread.handler.obtainMessage(LooperThread.GETMSG_FROM_DB, isInit?LooperThread.LOADTYPE_NEW:LooperThread.LOADTYPE_LOADMORE).sendToTarget();
	        }
	    }
	private Handler mainHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (vListView != null) {
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			switch (msg.what) {
			  case LooperThread.INIT_FINISH://初始化结束后去读取最近的消息
          		getMsgData(true);
				break; 
			  case LooperThread.GETMSG_FROM_DB:
          		List<AfMessageInfo> recentData =(List<AfMessageInfo>) msg.obj;
          		boolean isInit=msg.arg1==LooperThread.LOADTYPE_NEW?true:false;
          		if (isInit) { // 第一次进入时，先清除列表
    				listChats.clear();
    						// 转发 
    				if (isForward) {
    					forwardMsg();
    				}
    			} else {
    				stopRefresh();
    			}
    			bindData(recentData);
    			if(isInit){
    				getData();
    			}
          		break;
			case Chatting.SEND_SHARE_IMAGE://从图库内分享图片
				if (isShareImage) {
					dismissAllDialog();
					setResult(ForwardSelectActivity.SHARE_IMAGE);
					finish();
				}
				break;
			case RECORDING_TIMER://正在录音时，刷新录音时间。
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
					sent = true;
					mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
					mCancelVoice.setVisibility(View.GONE);
					vButtonTalk.setSelected(false);
				}
				break;
			case MessagesUtils.MSG_VOICE: {//发送语音
				PalmchatLogUtils.println("GroupChatActivity  mVoiceName " + mVoiceName);
				sendVoice(mVoiceName);
				break;
			}
			case MessagesUtils.SEND_VOICE: {//发送语音
				AfMessageInfo msgInfoVoice = (AfMessageInfo) msg.obj;
				// listChats.add(msgInfoVoice);
				//把语音添加到消息集合中
				CommonUtils.getRealList(listChats, msgInfoVoice);
				//更新适配器
				setAdapter(msgInfoVoice);
				AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) msgInfoVoice.attach;
				final int voiceLength = afAttachVoiceInfo.voice_len;
				PalmchatLogUtils.println("GroupChatActivity  voiceLength  " + voiceLength);

				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_VOICE);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);

//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_VOICE);
//				MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);

				int code = mAfCorePalmchat.AfHttpSendVoice(mFriendAfid, System.currentTimeMillis(), data, data.length, voiceLength, msgInfoVoice._id, GroupChatActivity.this, GroupChatActivity.this);
				sendRequestError(msgInfoVoice, code, Consts.SEND_VOICE);
				break;
			}

			// image
			case MessagesUtils.MSG_IMAGE: {
				String largeFilename = (String) msg.obj;// 原大图全路径
				if (null != looperThread.handler) {
					looperThread.handler.obtainMessage(LooperThread.SEND_IMAGE_LOOPER, largeFilename).sendToTarget();
				}
				break;
			}

			case MessagesUtils.MSG_PICTURE: { // 分享或转发图片
				String largeFilename = (String) msg.obj;// 压缩后大图全路径

				File largeFile = new File(largeFilename);
				int largeFileSize = 0;
				if (largeFile != null) {
					largeFileSize = (int) (largeFile.length() / 1024);
				}

				File f = new File(largeFilename);
				File f2 = FileUtils.copyToImg(f.getAbsolutePath());
				if (f2 != null) {
					f = f2;
				}
				String smallFileName = smallImage(f, MessagesUtils.PICTURE);

				File smaleFile = new File(RequestConstant.IMAGE_CACHE + smallFileName);
				int smallFileSize = 0;
				if (smaleFile != null) {
					smallFileSize = (int) (smaleFile.length() / 1024);
				}

				initMedia(largeFilename, largeFileSize, smallFileName, smallFileSize);

				break;
			}

			case MessagesUtils.EDIT_TEXT_CHANGE: {
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

			case MessagesUtils.MSG_CARD:// send card
			{
				AfFriendInfo afFriendInfo = (AfFriendInfo) msg.obj;
				sendCard(afFriendInfo);
				break;
			}

			case MessagesUtils.MSG_TEXT:// send text
			{
				AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
				send(msgInfoText.msg, msgInfoText);
				// vEditTextContent.setText("");
				PalmchatLogUtils.println("GroupChatActivity  msgInfoText._id  " + msgInfoText._id);
				break;
			}

			case MessagesUtils.MSG_GIF:// send text
			{
				AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
				send(msgInfoText.msg, msgInfoText);
				// vEditTextContent.setText("");
				PalmchatLogUtils.println("MSG_GIF msgInfoText._id  " + msgInfoText._id);
				break;
			}

			case MessagesUtils.MSG_EMOTION: {
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;

				// listChats.add(afMessageInfo);
				CommonUtils.getRealList(listChats, afMessageInfo);
				setAdapter(afMessageInfo);

				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);

//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_EMOTION);
//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_TEXT);
//				MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);

				int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.fromAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_EMOTION, afMessageInfo._id, GroupChatActivity.this);
				sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
				break;
			}

			case MessagesUtils.MSG_FORWARD: {//转发消息
				AfMessageInfo msgInfoText = (AfMessageInfo) msg.obj;
				forwardRequest(msgInfoText);
				PalmchatLogUtils.println("MessagesUtils.MSG_FORWARD  " + msgInfoText.fid);
				break;
			}

			case MessagesUtils.MSG_TOO_SHORT: {//显示录音太短
				ToastManager.getInstance().show(context, R.string.message_too_short);
				break;
			}

			case MessagesUtils.MSG_VOICE_RESEND:// resend voice
			{

				break;
			}

			case MessagesUtils.MSG_RESEND_CARD: {
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
				// listChats.add(afMessageInfo);
				CommonUtils.getRealList(listChats, afMessageInfo);
				setAdapter(afMessageInfo);
				PalmchatLogUtils.println("GroupChatActivity  afMessageInfo._id  " + afMessageInfo._id);

				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_CARD);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);

//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_CARD);
//				MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);

				int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.fromAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_RECOMMEND_CARD, afMessageInfo._id, GroupChatActivity.this);
				sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
				break;
			}

			case MessagesUtils.SEND_IMAGE: {//发送图片
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
				if (mFriendAfid.equals(afMessageInfo.fromAfId)) {
					AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
					AfImageReqInfo param = afAttachImageInfo.upload_info;
					// listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					setAdapter(afMessageInfo);

					// heguiming 2013-12-04
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_PIC);
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);

//					MobclickAgent.onEvent(context, ReadyConfigXML.S_M_PIC);
//					MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);

					int code = mAfCorePalmchat.AfHttpSendImage(param, afMessageInfo, GroupChatActivity.this, GroupChatActivity.this);
					sendRequestError(afMessageInfo, code, Consts.SEND_IMAGE);
					PalmchatLogUtils.i("GoupChatSendImage","SEND_IMAGE Handle");
				}
				else{
					PalmchatLogUtils.i("GoupChatSendImage","FromAfid:"+afMessageInfo.fromAfId+",mFriendAfid"+mFriendAfid);
				}
				break;
			}
			case EditGroupActivity.GROUP_NAME_CHANGED: {//更新群名称提示
				isNameChanged = true;
				edit_group_name.setVisibility(View.GONE);
				Toast.makeText(context, R.string.setting_name_success, Toast.LENGTH_SHORT).show();

				break;
			}
			case EditGroupActivity.GROUP_SET_NAME_FAIL: {//更新群名失败
				Toast.makeText(context, R.string.editmygroup_name_change_fail, Toast.LENGTH_SHORT).show();
				break;
			}
			case MessagesUtils.FAIL_TO_LOAD_PICTURE: {//下载图片失败提示
				ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
				break;
			}
			case NOTICEFY: {
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
				handleMessageFromServer(afMessageInfo);
			}
				break;
			case MessagesUtils.MSG_CAMERA_NOTIFY: {//拍照之后的发送广播
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				updateStatusForImage(afMessageInfo._id, afMessageInfo.status, afMessageInfo.fid, afAttachImageInfo.progress);
				break;
			}

			case MessagesUtils.MSG_SET_STATUS: {
				PalmchatLogUtils.println("---www : grp MSG_SET_STATUS");
				// notify recent chats refresh
//				sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
				EventBus.getDefault().post(new RefreshChatsListEvent());
				break;
			}
			case STORE_FACE_DOWNLOAD: {
				Params params = (Params) msg.obj;
				String myAfid = CacheManager.getInstance().getMyProfile().afId;
				if (!TextUtils.isEmpty(myAfid)) {
					Intent intent2 = new Intent();
					intent2.putExtra("itemId", params.itemId);
					intent2.putExtra("is_face_change", params.is_face_change);
					// EmojiParser.getInstance(context).readGif(context, myAfid,
					// handler, intent);
					if (!TextUtils.isEmpty(params.gifFolderPath)) {
						CacheManager.getInstance().getGifImageUtilInstance().putDownLoadFolder(context, params.gifFolderPath, mainHandler, intent2);
					}

				}
				break;
			}
			case MessagesUtils.MSG_SHARE_BROADCAST_OR_TAG:
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
				if(afMessageInfo != null){
					setAdapter(afMessageInfo);
				}
				break;
			case MessagesUtils.MSG_ERROR_OCCURRED:
				ToastManager.getInstance().show(context, R.string.error_occurred);
				break;

			default:
				break;
			}
		}

	};

	class LooperThread extends Thread {

		// 更新缓存数据
		private static final int SEND_IMAGE_LOOPER = 9991;
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

						String largeFilename = (String) msg.obj;// 原大图全路径
						File f = new File(largeFilename);
						File f2 = FileUtils.copyToImg(f.getAbsolutePath());
						if (f2 != null) {
							f = f2;
						}
						String cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
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
						case SHARE_IMAGE:
							shareImage();
							break;
					}

				}
			};
		/*	if (!isInit) {
				handler.obtainMessage(LooperThread.GETMSG_FROM_DB, LooperThread.LOADTYPE_NEW).sendToTarget();
			} */
			Looper.loop();

		}

	}

	/** 发送朋友名片
	 * @param afFriendInfo
	 */
	void sendCard(AfFriendInfo afFriendInfo) {
		AfMessageInfo afMessageInfo = new AfMessageInfo();
		afMessageInfo.msg = afFriendInfo.afId;
		afMessageInfo.client_time = System.currentTimeMillis();
		afMessageInfo.fromAfId = mFriendAfid;
		afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_CARD;
		afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

		afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
		afMessageInfo.attach = afFriendInfo;
		PalmchatLogUtils.println("GroupChatActivity  afMessageInfo._id " + afMessageInfo._id);
		mainHandler.obtainMessage(MessagesUtils.MSG_RESEND_CARD, afMessageInfo).sendToTarget();
		addToCache(afMessageInfo);
	}

	/** 重新发送朋友名片
	 * @param afMessageInfo
	 */
	public void resendCard(final AfMessageInfo afMessageInfo) {
		new Thread(new Runnable() {
			public void run() {
				afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
				mAfCorePalmchat.AfDbGrpMsgUpdate(afMessageInfo);
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
					mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
					MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
					CommonUtils.getRealList(listChats, afMessageInfo);
					int code;
					if (isShareTag) {
						code = mAfCorePalmchat.AfHttpAfBCShareTags(afMessageInfo.fromAfId, System.currentTimeMillis(), msgInfo.content, msgInfo.imgurl, afMessageInfo._id, GroupChatActivity.this);
					} else {
						code = mAfCorePalmchat.AfHttpAfBCShareBroadCast(afMessageInfo.fromAfId, System.currentTimeMillis(), msgInfo.mid, afMessageInfo._id, GroupChatActivity.this);
					}
					sendRequestError(afMessageInfo, code, Consts.SEND_MSG);
					mainHandler.obtainMessage(MessagesUtils.MSG_SHARE_BROADCAST_OR_TAG, afMessageInfo).sendToTarget();
				}
			}
		}).start();
	}

	/** 发送语音
	 * @param voiceName
	 */
	public void sendVoice(final String voiceName) {
		mHandler2.removeCallbacksAndMessages(null);
		if (!CommonUtils.isEmpty(voiceName)) {
			long end = System.currentTimeMillis();
			final long time = Math.min((int) ((end - mTimeStart) / 1000), RequestConstant.RECORD_VOICE_TIME_BROADCAST);
			PalmchatLogUtils.e(TAG, "----sendVoice---- time " + time);
			new Thread(new Runnable() {
				public void run() {
					data = FileUtils.readBytes(RequestConstant.VOICE_CACHE + voiceName);
					if (time < 2) {
						mainHandler.sendEmptyMessage(MSG_TOO_SHORT);
					} else {
						if (data == null || data.length <= AT_LEAST_LENGTH) {
							mainHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
							return;
						}
						sendMessageInfoForText(AfMessageInfo.MESSAGE_VOICE, voiceName, data.length, recordTime, mainHandler, MessagesUtils.SEND_VOICE, ACTION_INSERT, null, null);
					}
				}
			}).start();
		} else {
			ToastManager.getInstance().show(context, getString(R.string.sdcard_unmounted));
		}

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
						afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_TEXT;
						afMessageInfo.msg = forwardMsgInfo.msg;
						break;
					case AfMessageInfo.MESSAGE_CARD:
						afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_CARD;
						afMessageInfo.msg = forwardMsgInfo.msg;
						afMessageInfo.attach = forwardMsgInfo.attach;
						break;
					case AfMessageInfo.MESSAGE_IMAGE:
						afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_IMAGE;

						final AfAttachImageInfo forwardAttachImageInfo = (AfAttachImageInfo) forwardMsgInfo.attach;
						AfAttachImageInfo afAttachImageInfo = new AfAttachImageInfo();
						PalmchatLogUtils.println("GroupChatActivity forwardMsg forwardAttachImageInfo:" + forwardAttachImageInfo);
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
					afMessageInfo.client_time = System.currentTimeMillis();// afMessageInfo
																			// 更改??
					afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
					afMessageInfo.toAfId = null;
					afMessageInfo.fromAfId = mFriendAfid;
					afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
					mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, afMessageInfo._id, 0, afMessageInfo.fid, Consts.AFMOBI_PARAM_FID);
					addToCache(afMessageInfo);
					mainHandler.obtainMessage(MessagesUtils.MSG_FORWARD, afMessageInfo).sendToTarget();
					CacheManager.getInstance().setForwardMsg(null);// add by wxl
				}
			}).start();

		}

	}

	/**
	 * 设置系统声音模式
	 */
	private void setListenMode() {
		int listenMode = SharePreferenceService.getInstance(GroupChatActivity.this).getListenMode();
		PalmchatLogUtils.println("GroupChatActivity  time setListenMode " + dateFormat.format(new Date()) + "  listenMode  " + listenMode);
		if (listenMode == AudioManager.MODE_IN_CALL) { // 通话模式
			onItemClick(ChattingMenuDialog.ACTION_HANDSET_MODE);
		} else {// 普通模式
			onItemClick(ChattingMenuDialog.ACTION_SPEAKER_MODE);
		}
	}

	/**
	 * 设置View隐藏或显示
	 */
	private void disableViews() {
		vTextViewRecordTime.setVisibility(View.GONE);
		emojjView.getViewRoot().setVisibility(View.GONE);
		chattingOptions.setVisibility(View.GONE);
		vButtonKeyboard.setVisibility(View.GONE);
		vButtonLeftKeyboard.setVisibility(View.GONE);
		vImageEmotion.setVisibility(View.VISIBLE);
		vImageViewMore.setVisibility(View.VISIBLE);
		if (viewChattingVoiceLayout.getVisibility() == View.VISIBLE) {
			viewOptionsLayout.setVisibility(View.VISIBLE);
			viewChattingVoiceLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 切换语音与正常聊天模式
	 */
	private void changeMode() {
		if (viewUnTalk.getVisibility() != View.VISIBLE) {
			viewUnTalk.setVisibility(View.VISIBLE);
			vButtonTalk.setVisibility(View.GONE);
//			vImageViewVoice.setBackgroundResource(R.drawable.chatting_setmode_voice_btn_focused);
		}
	}

	/**
	 * 分享或转发图片处理
	 */
	private void shareImage() {

		PalmchatLogUtils.e(TAG, "----shareImage:" + isShareImage + "--shareImageUri--" + shareImageUri);

		if (isShareImage && null != shareImageUri) {
			showProgressDialog(R.string.Sending);
		/*	new Thread(new Runnable() {
				@Override
				public void run() {*/
					for (String path : shareImageUri) { 
					 	if (!TextUtils.isEmpty(path)) {
							f = new File(path); 
							if ((null != f) && (!f.exists())) {
								mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
							} else {
								File f2 = FileUtils.copyToImg(f.getAbsolutePath());
								if (f2 != null) {
									f = f2;
								}
								cameraFilename = f2.getAbsolutePath();// imageFile.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
								smallImage(f, MessagesUtils.PICTURE);
								cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
								cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
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
			/*	}
			}).start();*/
		} else if (!TextUtils.isEmpty(forward_imagePath)) {
			if(!mIsForworded) {
				f = new File(forward_imagePath);
				File f2 = FileUtils.copyToImg(f.getAbsolutePath());
				if (f2 != null) {
					f = f2;
				}
				cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
				smallImage(f, MessagesUtils.PICTURE);
				cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
				cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
				if (CommonUtils.isEmpty(cameraFilename)) {
					mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
					return;
				}
				mIsForworded = true;
				mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
			}
		}
	}
	
 
	private boolean isShareImage;
	private ArrayList<String> shareImageUri;

	/**
	 * 显示或隐藏录音布局
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
		closeEmotions();
	}

	private String getEditTextContent() {
		return vEditTextContent.getText().toString();
	}

	/** 设置ListView的适配器
	 * @param afMessageInfo
	 */
	private void setAdapter(AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		if (adapterListView == null) {
			adapterListView = new GroupChattingAdapter(this, listChats, vListView );
			vListView.setAdapter(adapterListView);
		} else {
			// adapterListView.setList(listChats);
			if (afMessageInfo != null && MessagesUtils.ACTION_UPDATE == afMessageInfo.action) {
				PalmchatLogUtils.println("Group  setAdapter  afMessageInfo.action ACTION_UPDATE");
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
			} else if (afMessageInfo != null) {
				PalmchatLogUtils.println("Group  setAdapter  afMessageInfo.action " + afMessageInfo.action);
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			adapterListView.notifyDataSetChanged();
			// vListView.setSelection(adapterListView.getCount());
			if ((mIsNoticefy && 0 < adapterListView.getCount()) || isBottom) {
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
	 * 设置未读消息
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

	/**
	 * 隐藏未读数的提示
	 */
	private void hideUnreadTips() {
		if (view_unread.getVisibility() == View.VISIBLE) {
			view_unread.setVisibility(View.GONE);
			unreadTipsCount = 0;
		}
	}

	private AfGrpProfileInfo groupInfo;
	// private AfGrpItemInfo mGrpItemInfo;
	private String group_list;

	private boolean pop = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		MobclickAgent.onPageStart("GroupChatActivity");
//		MobclickAgent.onResume(this);
		mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				faceFooterView.refreshFaceFootView();
				faceFooterView.showNewSymbolOrNot();
				if(faceFooterView.mSelectItemIndex != -1){
					emojjView.setUnSelected();
				}
			}
		}, 300);

		groupInfo = CacheManager.getInstance().searchGrpProfileInfo(groupID);
		if (groupInfo != null) {
			if (isCreate) {
				groupInfo.name = roomName;
			} else {

				title_text.setText(groupInfo.name);
				edit_group_name.setVisibility(View.GONE);
			}
		}
		//getData();
		CommonUtils.cancelNoticefacation(getApplicationContext());

		String msgContent = mAfCorePalmchat.AfDbGetMsgExtra(mFriendAfid);
		PalmchatLogUtils.println("mFriendAfid  " + mFriendAfid + "  group  msgContent  " + msgContent);

		vEditTextContent.setText(EmojiParser.getInstance(context).parse(msgContent));
		CharSequence text = vEditTextContent.getText();
		if (text.length() > 0) {// 当界面返回时 光标选择到末尾
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
		if (CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().containsKey(mFriendAfid)) {
			pop = true;
//			new GetDataTask(false).execute();
			getMsgData(false);
			CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().clear();
		}

	}

	/**
	 * 获取本地聊天数据
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

	private float mDownX;
	private float mDownY;

	private long mCurrentClickTime;
	private static final long LONG_PRESS_TIME = 1500;

	class HoldTalkListener implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.talk_button) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下操作
					if (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime > LONG_PRESS_TIME) {
						// 以下按下时的获取x,y坐标
						mDownX = event.getRawX();
						mDownY = event.getRawY();
						if (!CommonUtils.isHasSDCard()) {
							ToastManager.getInstance().show(GroupChatActivity.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
							return false;
						}
						sent = false;
						mCurrentClickTime = Calendar.getInstance().getTimeInMillis();

						if (recordStart > 0) {// 判断是否已录音
							// 发送录音消息
							mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
							// 停止定时器
							stopRecordingTimeTask();
							setMode(SharePreferenceService.getInstance(GroupChatActivity.this).getListenMode());
							vButtonTalk.setSelected(false);
							// 停止录音
							stopRecording();
							sent = true;
							return false;
						}

						mVoiceName = startRecording();
						if (mVoiceName == null) {
							return false;
						}

						// 设置杨声器模式
						setMode(AudioManager.MODE_NORMAL);
						if (VoiceManager.getInstance().isPlaying()) {
							VoiceManager.getInstance().pause();
						}
						PalmchatLogUtils.println("GroupChatActivity  mVoiceName " + mVoiceName);
					}
					break;
				case MotionEvent.ACTION_MOVE:

					// already stop recording
					if (recordStart <= 0) {
						break;
					}

					float offsetX1 = Math.abs(event.getRawX() - mDownX);
					float offsetY1 = Math.abs(event.getRawY() - mDownY);

					if (offsetX1 < ImageUtil.dip2px(context, 85) && offsetY1 < ImageUtil.dip2px(context, 85)) {

						mCancelVoice.setText(R.string.empty);
						mCancelVoice.setVisibility(View.GONE);
						vButtonTalk.setSelected(false);

						mVoiceProgressBar.setVisibility(View.VISIBLE);
						mVoiceProgressBar2.setVisibility(View.GONE);

						vTextViewRecordTime.setTextColor(getResources().getColor(R.color.log_blue));

						// show cancel voice text
					} else {// 显示取消提示
						mCancelVoice.setText(R.string.cancel_voice);
						mCancelVoice.setVisibility(View.VISIBLE);
						vButtonTalk.setSelected(true);

						mVoiceProgressBar.setVisibility(View.GONE);
						mVoiceProgressBar2.setVisibility(View.VISIBLE);

						vTextViewRecordTime.setTextColor(getResources().getColor(R.color.color_voice_red));
					}

					break;
				case MotionEvent.ACTION_UP:
					if (/*
						 * vTextViewRecordTime != null &&
						 * vTextViewRecordTime.isShown() &&
						 */!sent || (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime <= LONG_PRESS_TIME)) {
						if (CommonUtils.getSdcardSize()) {
							ToastManager.getInstance().show(context, R.string.memory_is_full);
						} else {
							float offsetX = Math.abs(event.getRawX() - mDownX);
							float offsetY = Math.abs(event.getRawY() - mDownY);

							if (offsetX < ImageUtil.dip2px(context, 85) && offsetY < ImageUtil.dip2px(context, 85)) {// 当手释放时，如果在此番外范围内，则直接发送语音
								PalmchatLogUtils.println("---- send voice----");

								if (recordStart > 0) {
									mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);

								}
							} else {
								mHandler2.removeCallbacksAndMessages(null);
							}
						}
						vTextViewRecordTime.setVisibility(View.GONE);
						stopRecording();
						stopRecordingTimeTask();
					}
					setMode(SharePreferenceService.getInstance(GroupChatActivity.this).getListenMode());

					// show cancelled text
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

	/**
	 * 关闭表情布局
	 */
	public void closeEmotions() {
		// TODO Auto-generated method stub
		vButtonKeyboard.setVisibility(View.GONE);
		vImageEmotion.setVisibility(View.VISIBLE);
		if (mIsRecording) {
			vTextViewRecordTime.setVisibility(View.VISIBLE);
		} else {
			vTextViewRecordTime.setVisibility(View.GONE);
		}
		emojjView.getViewRoot().setVisibility(View.GONE);
		chattingOptions.setVisibility(View.GONE);
		vImageViewMore.setVisibility(View.VISIBLE);
		vButtonLeftKeyboard.setVisibility(View.GONE);
		vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		vListView.setSelection(adapterListView.getCount());
	}

	private long mTimeStart;
	Handler mHandler2 = new Handler();

	/**
	 * 开始录音
	 *
	 * @return
	 */
	private String startRecording() {
		String voiceName = null;

		if (RequestConstant.checkSDCard()) {
			// String dir =
			// mAfCorePalmchat.AfResGetDir(AfMessageInfo.MESSAGE_VOICE);
			voiceName = mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_VOICE);
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			mRecorder.setOutputFile(RequestConstant.VOICE_CACHE + voiceName);// ClippingSounds.saveTalkSoundsFileNames();
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mRecorder.setOnInfoListener(new OnInfoListener() {

				@Override
				public void onInfo(MediaRecorder mr, int what, int extra) {
					// TODO Auto-generated method stub
					mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
					stopRecordingTimeTask();
					setMode(SharePreferenceService.getInstance(GroupChatActivity.this).getListenMode());
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

				// vTextViewRecordTime.setVisibility(View.GONE);
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
						PalmchatLogUtils.println("--ddd groupchat recordData length " + recordData.length);
					} else {
						PalmchatLogUtils.println("--ddd groupchat recordData null ");
					}
					PalmchatLogUtils.println("--ddd groupchat readBytes time  " + (time2 - time1));

//					if (recordData == null || recordData.length <= 0) {
//						stopRecording();
//						stopRecordingTimeTask();
//					} else {
						//显示录音波纹
						mRippleView.setVisibility(View.VISIBLE);
						mRippleView.setOriginRadius(vButtonTalk.getWidth() / 2);
						mRippleView.startRipple();

						vButtonBackKeyboard.setClickable(false);
						mVoiceImageEmotion.setClickable(false);
						mVoiceImageViewMore.setClickable(false);

						mIsRecording = true;
						adapterListView.setIsRecording(mIsRecording);

						createVoiceDialog();
						startRecordingTimeTask();

						AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
						am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

//					}
				}
			}, 200);

		} else {
			// ToastManager.getInstance().show(this, R.string.sdcard_unmounted);
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
		case KeyEvent.KEYCODE_MENU:
			if (mFriendAfid != null && !mFriendAfid.startsWith(DefaultValueConstant._R)) {
				int listenMode = SharePreferenceService.getInstance(GroupChatActivity.this).getListenMode();
				addAction(listenMode, mFriendAfid);
				widget.show();
			}
			return true;
		case KeyEvent.KEYCODE_BACK:
			vButtonKeyboard.setVisibility(View.GONE);
			vButtonLeftKeyboard.setVisibility(View.GONE);
			vImageEmotion.setVisibility(View.VISIBLE);
			vImageViewMore.setVisibility(View.VISIBLE);
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
			/*	if (isCreate && !isNameChanged) {
					int req_flag = Consts.REQ_GRP_MODIFY;
					CallGrpOpr(null, roomName, groupID, req_flag, false);
				} else {
					isCreate = false;
				}*/
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
	 * @param msgType
	 *            消息类型
	 * @param fileName
	 *            文件名
	 * @param fileSize
	 *            文件大小
	 * @param length
	 *            文件长度
	 * @param handler
	 *            handler
	 * @param handler_MsgType
	 *            handler类型
	 * @param action
	 *            action （插入或重发）
	 * @param afMessageInfo
	 *            消息对象
	 * @param content
	 *            消息内容
	 */
	private void sendMessageInfoForText(final int msgType, final String fileName, final int fileSize, final int length, final Handler handler, final int handler_MsgType, final int action, final AfMessageInfo afMessageInfo, final String content) {
		PalmchatLogUtils.println("GroupChatActivity  sendMessageInfoForText " + fileName + "  mVoiceName  " + mVoiceName);
		final AfMessageInfo messageInfo = new AfMessageInfo();// 新增
		new Thread(new Runnable() {
			public void run() {
				switch (msgType) {
				case AfMessageInfo.MESSAGE_TEXT:// 文本消息
					if (action == MessagesUtils.ACTION_INSERT) {// 新消息并插入本地数据库
						messageInfo.client_time = System.currentTimeMillis();
						messageInfo.fromAfId = mFriendAfid;
						// messageInfo.toAfId =
						// CacheManager.getInstance().getMyProfile().afId;
						messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_TEXT;
						messageInfo.msg = content;
						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;

						messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);
					} else if (action == MessagesUtils.ACTION_UPDATE) {// 重发此消息并更新本地数据库
					// afMessageInfo.client_time =
					// System.currentTimeMillis();//afMessageInfo 更改�?
						afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						mAfCorePalmchat.AfDbGrpMsgUpdate(afMessageInfo);
					}
					break;
				case AfMessageInfo.MESSAGE_VOICE:// 语音消息
					if (action == MessagesUtils.ACTION_INSERT) {// 新消息并插入本地数据库
						AfAttachVoiceInfo afAttachVoiceInfo = new AfAttachVoiceInfo();
						afAttachVoiceInfo.file_name = fileName;
						afAttachVoiceInfo.file_size = fileSize;
						afAttachVoiceInfo.voice_len = length;
						afAttachVoiceInfo._id = mAfCorePalmchat.AfDbAttachVoiceInsert(afAttachVoiceInfo);
						PalmchatLogUtils.println("GroupChatActivity  sendMessageInfoForText " + fileName + "  mVoiceName  " + mVoiceName + "  voice_length  " + length);

						messageInfo.client_time = System.currentTimeMillis();
						messageInfo.fromAfId = mFriendAfid;
						// messageInfo.toAfId =
						// CacheManager.getInstance().getMyProfile().afId;
						messageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_VOICE;
						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						messageInfo.attach = afAttachVoiceInfo;
						messageInfo.attach_id = afAttachVoiceInfo._id;
						messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);
					} else if (action == MessagesUtils.ACTION_UPDATE) {// 重发此消息并更新本地数据库
					// afMessageInfo.client_time = System.currentTimeMillis();
						afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						mAfCorePalmchat.AfDbGrpMsgUpdate(afMessageInfo);
					}
					break;
				default:
					break;
				}
				PalmchatLogUtils.println("GroupChatActivity  sendMessageInfoForText  afMessageInfo  " + afMessageInfo);
				if (action == MessagesUtils.ACTION_INSERT) {
					messageInfo.action = action;
					handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
					// 向缓存中插入一条数据
					addToCache(messageInfo);
				} else if (action == MessagesUtils.ACTION_UPDATE) {
					afMessageInfo.action = action;
					handler.obtainMessage(handler_MsgType, afMessageInfo).sendToTarget();
					// //更新当前消息的状态
					MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
				}
			}

		}).start();

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
		switch (v.getId()) {
		case R.id.chatting_send_button:// send button
			sendTextOrEmotion();
			break;
		case R.id.chatting_operate_one:// change voice button
		case R.id.btn_backkeyboard:
			toShowVoice();
			break;
		case R.id.btn_keyboard:
			vButtonKeyboard.setVisibility(View.GONE);
			vImageEmotion.setVisibility(View.VISIBLE);
			emojjView.getViewRoot().setVisibility(View.GONE);
			chattingOptions.setVisibility(View.GONE);
			vImageViewMore.setVisibility(View.VISIBLE);
			vButtonLeftKeyboard.setVisibility(View.GONE);
			changeMode();
			vListView.setSelection(adapterListView.getCount());
			vEditTextContent.requestFocus();
			CommonUtils.showSoftKeyBoard(vEditTextContent);
			break;
		case R.id.image_emotion:// emotion button
			CommonUtils.closeSoftKeyBoard(vEditTextContent);
			emojjView.getViewRoot().setVisibility(View.VISIBLE);
			vButtonKeyboard.setVisibility(View.VISIBLE);
			vImageEmotion.setVisibility(View.GONE);
			chattingOptions.setVisibility(View.GONE);
			vImageViewMore.setVisibility(View.VISIBLE);
			vButtonLeftKeyboard.setVisibility(View.GONE);
			// emojjView.getViewRoot().setFocusableInTouchMode(true);
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
			vButtonKeyboard.setVisibility(View.VISIBLE);
			vImageEmotion.setVisibility(View.GONE);
			chattingOptions.setVisibility(View.GONE);
			vImageViewMore.setVisibility(View.VISIBLE);
			vButtonLeftKeyboard.setVisibility(View.GONE);
			// emojjView.getViewRoot().setFocusableInTouchMode(true);
			changeMode();
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
			// toShowVoice();
			emojjView.getViewRoot().setVisibility(View.GONE);
			vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			vListView.setSelection(adapterListView.getCount());
			vEditTextContent.requestFocus();
			CommonUtils.showSoftKeyBoard(vEditTextContent);
			break;
		case R.id.chatting_message_edit:// onclick on eidttext
			closeEmotions();
			break;
		case R.id.back_button:
			boolean hasKeyBoard = chat_root.hasKeyboard();
			if (hasKeyBoard) {
				CommonUtils.closeSoftKeyBoard(vEditTextContent);

			}
			back();
			break;
		case R.id.et_exit:
			if (isCanSave) {
				roomName = et_group_name.getText().toString();
				if (TextUtils.isEmpty(roomName)) {
					ToastManager.getInstance().show(context, R.string.public_group_name_not_empty);
				} else {
					roomName = roomName.trim();
					if (TextUtils.isEmpty(roomName)) {
						ToastManager.getInstance().show(context, R.string.public_group_name_not_empty);
					} else {
						title_text.setText(roomName);
						int req_flag = Consts.REQ_GRP_MODIFY;

						showProgressDialog(R.string.please_wait);
						CallGrpOpr(null, roomName, groupID, req_flag, true);
					}
				}

			} else {
				edit_group_name.setVisibility(View.GONE);
			}

			break;
		case R.id.et_group_name:
			//emojjView.getViewRoot().setVisibility(View.GONE);
			closeEmotions();
			if (viewChattingVoiceLayout.getVisibility() == View.VISIBLE) {
				viewOptionsLayout.setVisibility(View.VISIBLE);
				viewChattingVoiceLayout.setVisibility(View.GONE);
				vEditTextContent.requestFocus();
				CacheManager.getInstance().putShowChattingVoice(mFriendAfid, false);
			}
			break;
		case R.id.img_picture:
		case R.id.img_picture_p: {
			if (CommonUtils.getSdcardSize()) {
				ToastManager.getInstance().show(context, R.string.memory_is_full);
				break;
			}

			// modify by zhh
			Intent mIntent = new Intent(this, PreviewImageActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt("size", DefaultValueConstant.MAX_SELECTED);
			mIntent.putExtras(mBundle);
			startActivityForResult(mIntent, ALBUM_REQUEST);
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
			SharePreferenceService.getInstance(GroupChatActivity.this).savaFilename(cameraFilename);
			f = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
			Uri u = Uri.fromFile(f);
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			try {
				startActivityForResult(intent, MessagesUtils.CAMERA);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
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
			Intent intent = new Intent(GroupChatActivity.this, SelectListActivity.class);
			intent.putExtra(JsonConstant.KEY_AFID, mFriendAfid);
			startActivityForResult(intent, 3);
			break;
		}
		case R.id.img_video:
		case R.id.img_video_v: {
			PalmchatLogUtils.println("group img_video");
			break;
		}
		case R.id.img_background:
		case R.id.img_background_v: {
			PalmchatLogUtils.println("group img_background");
			Intent intent = new Intent(GroupChatActivity.this, ActivityBackgroundChange.class);
			intent.putExtra(JsonConstant.KEY_FROM_UUID, mFriendAfid);
			startActivityForResult(intent, BACK_FROM_SET_CHAT_BG);
			break;
		}
		case R.id.view_unread:
			hideUnreadTips();
			adapterListView.notifyDataSetChanged();
			vListView.setSelection(adapterListView.getCount());
			isBottom = true;
			break;
		default:
			break;
		}
	}

	private void CallGrpOpr(String member, String groupName, String groupId, int req_flag, boolean isShowToast) {
		if (groupInfo != null) {
			List<AfGrpProfileItemInfo> lists = groupInfo.members;
			String afid[] = new String[lists.size()];
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < lists.size(); i++) {
				afid[i] = lists.get(i).afid;
				buffer = buffer.append(afid[i]).append(",");
				// String members = "a1902418,a1568854,a1675034,a2452792";
			}
			String members = buffer.substring(0, buffer.lastIndexOf(","));
			mAfCorePalmchat.AfHttpGrpOpr(members, groupName, groupId, req_flag, isShowToast, this);
		} else {
			finish();
		}
	}


	/**
	 * 退出此activity
	 */
	private void back() {
		if (VoiceManager.getInstance().isPlaying()) {
			VoiceManager.getInstance().pause();
		}

		adapterListView.exit();

		if (isForward) { // mIsSingleChat从私聊页面创建群，退出时，进入chats页面
			if(TextUtils.isEmpty(forward_imagePath) || mIsSingleChat) {
				Intent intent = new Intent(GroupChatActivity.this, MainTab.class);
				intent.putExtra(JsonConstant.KEY_TO_MAIN, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else {
				finish();
			}
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
					if (!(act instanceof MainTab || act instanceof LaunchActivity || act instanceof ProfileActivity || act instanceof MyProfileActivity  || act instanceof LocalSearchActivity
							|| act instanceof PalmcallActivity)) {
						act.finish();
					}
				}
			}
		}
	}

	/**
	 * 发送文本或表情
	 */
	// send textOrEmotion
	private void sendTextOrEmotion() {
		String content = getEditTextContent();
		if (content.length() > 0) {
			vEditTextContent.setText("");
			PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_SENDMSG);

			sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_INSERT, null, content);
		}
	}

	/** 重发文本或表情
	 * @param afMessageInfo
	 */
	public void resendTextOrEmotion(AfMessageInfo afMessageInfo) {
		sendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_UPDATE, afMessageInfo, null);
	}

	/**
	 * 发送消息
	 *
	 * @param content
	 * @param msgInfo
	 */
	private void send(String content, AfMessageInfo msgInfo) {
		// listChats.add(msgInfo);
		CommonUtils.getRealList(listChats, msgInfo);
		setAdapter(msgInfo);

		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);

//		MobclickAgent.onEvent(context, ReadyConfigXML.S_M_TEXT);
//		MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);
		int code = -1;
		if (msgInfo.type == (AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_STORE_EMOTIONS)) {
			code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.fromAfId, System.currentTimeMillis(), content, Consts.MSG_CMMD_STORE_EMOTION, msgInfo._id, this);
		} else {
			code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.fromAfId, System.currentTimeMillis(), content, Consts.MSG_CMMD_NORMAL, msgInfo._id, this);
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
			int code = mAfCorePalmchat.AfHttpSendMsg(msgInfo.fromAfId, System.currentTimeMillis(), msgInfo.fid, Consts.MSG_CMMD_FORWARD, msgInfo, GroupChatActivity.this);
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
			mAfCorePalmchat.AfDbGrpMsgRmove(msgInfo);
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
			mAfCorePalmchat.AfDbGrpMsgRmove(msgInfo);
			mainHandler.obtainMessage(MessagesUtils.MSG_CARD, afFriendInfo).sendToTarget();
			break;
		}

		mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgInfo._id, 0, null, Consts.AFMOBI_PARAM_FID);

	}

	/**
	 * 转发失败后重新发送
	 *
	 * @param msgInfo
	 */
	private void forwardFailureUpdate(AfMessageInfo msgInfo) {

		PalmchatLogUtils.println("forwardFailureUpdate");

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
		mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgInfo._id, 0, null, Consts.AFMOBI_PARAM_FID);
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
		PalmchatLogUtils.println("GroupChatActivity sendRequestError  handle==" + handle + "==msg==" + msgInfo.msg + "==requestFlag==" + requestFlag);
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
		for (AfMessageInfo af : adapterListView.getLists()) {
			PalmchatLogUtils.println("GroupChatActivity  af.id  " + af._id);
		}
		PalmchatLogUtils.println("GroupChatActivity  index  " + index + "  msgId  " + msgId + "  status  " + status);
		if (index != -1 && index < adapterListView.getCount()) {
			AfMessageInfo afMessageInfo = adapterListView.getItem(index);
			afMessageInfo.status = status;
			afMessageInfo.fid = fid;
			PalmchatLogUtils.println("GroupChatActivity  afMessageInfo.msgId  " + afMessageInfo._id + "  afMessageInfo.status  " + afMessageInfo.status);
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
				_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);

			} else {
				_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
			}

			// update recent chats msg status
			AfMessageInfo msg = mAfCorePalmchat.AfDbGrpMsgGet(msgId);
			if (msg == null) {

				PalmchatLogUtils.println("GroupChatActivity AfDbGrpMsgGet msg:" + msg);
				return;
			}

			PalmchatLogUtils.println("---www : grp StatusThead isLast msg " + msgId + " msg status: " + status);
			MessagesUtils.setRecentMsgStatus(msg, status);

			mainHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();

			PalmchatLogUtils.println("update status msg_id " + _id);

			// _id = mAfCorePalmchat.AfDbGrpMsgSetStatus(msgId, status);
		}
	}

	/**
	 * 发送图片消息后，更新列表消息发送状态
	 * 
	 * @param msgId
	 * @param status
	 * @param fid
	 * @param afAttachImageInfo
	 */
	public void updateImageStatus(int msgId, int status, String fid, AfAttachImageInfo afAttachImageInfo) {

		int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
		// PalmchatLogUtils.i(TAG, "找到第几�?-> " + index + "->" +
		// talkList.getSelectedItemPosition(),true);
		if (index != -1 && index < adapterListView.getCount()) {
			AfMessageInfo afMessageInfo = adapterListView.getItem(index);
			PalmchatLogUtils.println("GroupChatActivity  updateImageStatus  afMessageInfo  " + afMessageInfo);
			afMessageInfo.status = status;
			afMessageInfo.attach = afAttachImageInfo;
			afMessageInfo.fid = fid;
		}

	}

	/**
	 * 发送图片消息后，更新消息发送状态
	 * 
	 * @param msgId
	 * @param status
	 * @param fid
	 * @param progress
	 */
	private void updateStatusForImage(final int msgId, int status, String fid, int progress) {
		int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
		if (msgId != -1 && index == -1) {
			AfMessageInfo msg = CacheManager.getInstance().getAfMessageInfo();
			if (msg != null && mFriendAfid.equals(msg.fromAfId)) {
				if (msg != null && msgId == msg._id) {
					PalmchatLogUtils.println("GroupChatActivity  updateImageStautsByBroadcast  ");
					CommonUtils.getRealList(adapterListView.getLists(), msg);
					CacheManager.getInstance().setAfMessageInfo(null);

					updateStatusForImage(msgId, status, fid, progress);
				} else {
					new Thread(new Runnable() {
						public void run() {
							PalmchatLogUtils.println("GroupChatActivity  Thread  updateImageStautsByBroadcast  ");
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
			if (afMessageInfo != null && mFriendAfid.equals(afMessageInfo.fromAfId)) {
				PalmchatLogUtils.println("GroupChatActivity  updateImageStatusByBroadcast  afMessageInfo  " + afMessageInfo);
				afMessageInfo.status = status;
				afMessageInfo.fid = fid;
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				afAttachImageInfo.progress = progress;
				adapterListView.notifyDataSetChanged();
			}
		}
	}

	/**
	 * send message error
	 */
	private void sendMsgFailure(int flag, int code, Object user_data) {
		int msg_id;
		msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
		if (msg_id > DefaultValueConstant.LENGTH_0) {
			updateStatusForSendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);

		} else {

			ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
			PalmchatLogUtils.println("GroupChatActivity  else Consts.REQ_MSG_SEND else code " + code + "  flag  " + flag);
		}
	}

	/**
	 * zhh 发送完语音或者文本消息后，更新消息发送状态
	 * 
	 * @param msg_id
	 * @param status
	 * @param fid
	 */
	private void updateStatusForSendTextOrVoice(int msg_id, int status, String fid) {
		updateStatus(msg_id, status, fid);
	}

	/**
	 * send voice error
	 */
	private void sendVoiceFailure(int flag, int code, Object user_data) {
		int msg_id;
		msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
		if (msg_id > DefaultValueConstant.LENGTH_0) {

			updateStatusForSendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
		} else {
			ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
			PalmchatLogUtils.println("GroupChatActivity  else Consts.REQ_VOICE_SEND else code " + code + "  flag  " + flag);
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
			updateStatusForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
			int handle = mAfCorePalmchat.AfHttpSendImage(afAttachImageInfo.upload_info, afMessageInfo, this, this);
			sendRequestError(afMessageInfo, handle, Consts.SEND_IMAGE);
		} else if (afImageReqInfo != null && afMessageInfo != null) {
			AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
			if (afAttachImageInfo != null) {
				AfImageReqInfo imageReqInfo = afAttachImageInfo.upload_info;
				afImageReqInfo._id = imageReqInfo._id;
				afAttachImageInfo.upload_info = afImageReqInfo;

				updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo);
				updateStatusForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);

				new Thread(new Runnable() {
					public void run() {
						PalmchatLogUtils.println("GroupChatActivity  afImageReqInfo._id  " + afImageReqInfo._id);
						mAfCorePalmchat.AfDbImageReqUPdate(afImageReqInfo);// mAfCorePalmchat.AfDbImageReqUPdate(afAttachImageInfo.upload_info);
					}
				}).start();
				new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
			} else {
				PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  afAttachImageInfo  " + afAttachImageInfo);
			}
		} else {
			AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
			updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo);
			updateStatusForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
			new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("GroupChatActivity  group  code " + code + "  flag  " + flag + "  result  " + result + "  user_data  " + user_data);
		int msg_id;

		AfGrpProfileInfo mProgfileInfo = null;
		if (Consts.REQ_CODE_SUCCESS == code) {
			ableClick();
			switch (flag) {
			case Consts.REQ_MSG_SEND:
				msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
				PalmchatLogUtils.println("GroupChatActivity  onresult msg_id  " + msg_id);
				if (msg_id > DefaultValueConstant.LENGTH_0) {
					String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
					updateStatusForSendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, fid);
					PalmchatLogUtils.println("grp result fid:" + fid);

				} else {

					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("GroupChatActivity  case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
				}
				break;
			case Consts.REQ_VOICE_SEND:
				msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
				if (msg_id > DefaultValueConstant.LENGTH_0) {
					updateStatusForSendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, DefaultValueConstant.MSG_INVALID_FID);

				} else {
					// updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("GroupChatActivity  case Consts.REQ_VOICE_SEND code " + code + "  flag  " + flag);
				}
				break;
			case Consts.REQ_MEDIA_INIT:// media init
			{

				break;
			}
			case Consts.REQ_MEDIA_UPLOAD:// media upload
			{
				AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
				SendImageEvent event=new SendImageEvent(afMessageInfo,0,true,result);
				EventBus.getDefault().post(event);
				 //这里只所以要搞EventBus事件 是因为 当在一个聊天里发了一张图没发完的时候 再退出  再进 百分比不会动了， 是因为已经不是同一个对象了			
				uploadMediaCallBack(event); //这里要再调一次 是因为当前可能没有开群聊或则私聊界面 所以这个时候也必须处理
				break;
			}
			case Consts.REQ_GET_INFO:// get profile(info) success
			{
				AfProfileInfo afProfileInfo = (AfProfileInfo) result;
				AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
				if (afProfileInfo != null && afMessageInfo != null) {
					CacheManager.getInstance().saveOrUpdateFriendProfile(afProfileInfo);
					AfFriendInfo afFriendInfo = AfProfileInfo.friendToProfile(afProfileInfo);
					afMessageInfo.attach = afFriendInfo;
					// listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					showVibrate();
					setAdapter(null);

				} else {
					// get profile info failure
					PalmchatLogUtils.println("GroupChatActivity AfOnResult get profile info failure");
				}
				break;
			}

			// forward msg
			case Consts.REQ_MSG_FORWARD: {
				PalmchatLogUtils.println("REQ_MSG_FORWARD success grp");
				// update send msg ui
				msg_id = user_data != null ? ((AfMessageInfo) user_data)._id : DefaultValueConstant.LENGTH_0;
				PalmchatLogUtils.println("onresult msg_id  " + msg_id);
				if (msg_id > DefaultValueConstant.LENGTH_0) {
					updateStatusForSendTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, ((AfMessageInfo) user_data).fid);

				} else {

					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code " + code + "  flag  " + flag);
				}

				break;
			}

			case Consts.REQ_GRP_MODIFY:
				dismissProgressDialog();
				mProgfileInfo = (AfGrpProfileInfo) result;
				AfGrpProfileInfo afGrpProfileInfo = CacheManager.getInstance().searchGrpProfileInfo(groupId);
				if (null != mProgfileInfo) {
					gversion = mProgfileInfo.version;
					groupId = mProgfileInfo.afid;
					mProgfileInfo.name = roomName;// groupname
					if (afGrpProfileInfo != null) {
						mProgfileInfo.tips = afGrpProfileInfo.tips;
					}
					CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).update(mProgfileInfo, true, true);
					// mAfCorePalmchat.AfDbGrpProfileUpdateName(groupId,roomName);
				}
				boolean isShowToast = (Boolean) user_data;
				if (!StringUtil.isEmpty(groupId, true)) {
					if (isShowToast) {
						mainHandler.sendMessage(mainHandler.obtainMessage(EditGroupActivity.GROUP_NAME_CHANGED, groupId));
					}
				} else {
					if (isShowToast) {
						mainHandler.sendMessage(mainHandler.obtainMessage(EditGroupActivity.GROUP_SET_NAME_FAIL, groupId));
					}
				}
				new Thread(new Runnable() {
					public void run() {
						AfGrpNotifyMsg afGrpNotifyMsg;
						final int modify_type = AfGrpNotifyMsg.MODIFY_TYPE_NAME;
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(roomName, groupId, CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), null, null, null, gversion, AfMessageInfo.MESSAGE_GRP_MODIFY, modify_type);

						AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg, PalmchatApp.getApplication());
						afMessageInfo.client_time = System.currentTimeMillis();
						int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
						afMessageInfo._id = msg_id;
						MessagesUtils.insertMsg(afMessageInfo, true, true);
						mainHandler.obtainMessage(NOTICEFY, afMessageInfo).sendToTarget();

					}
				}).start();
				break;
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
			case Consts.REQ_BC_SHARE_BROADCAST:
			case Consts.REQ_BC_SHARE_TAG:
				msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
				if (msg_id > DefaultValueConstant.LENGTH_0) {
					AfResponseComm.AfTagShareTagOrBCResp afResponseComm = (AfResponseComm.AfTagShareTagOrBCResp) result;
					AfMessageInfo msg = mAfCorePalmchat.AfDbGrpMsgGet(msg_id);
					if (afResponseComm != null) {
						AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) msg.attach;
						MessagesUtils.updateShareTagMsgPostNumber(msg,afAttachPAMsgInfo._id);
					}
					updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT, DefaultValueConstant.MSG_INVALID_FID);
				}
				break;
			default:
				break;
			}
		} else {
			// dismissProgressDialog();
			switch (flag) {
			case Consts.REQ_MSG_SEND:
				sendMsgFailure(flag, code, user_data);
				break;
			case Consts.REQ_VOICE_SEND:
				sendVoiceFailure(flag, code, user_data);
				break;
			case Consts.REQ_MEDIA_INIT:// media init
			case Consts.REQ_MEDIA_UPLOAD:// media upload
			{
				sendImageFailure(code, result, user_data);
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

			// forward msg
			case Consts.REQ_MSG_FORWARD: {

				if (code != Consts.REQ_CODE_UNNETWORK) {

					if (user_data != null && user_data instanceof AfMessageInfo) {
						forwardFailureUpdate((AfMessageInfo) user_data);
						return;
					}
				}

				String user_data2 = String.valueOf(((AfMessageInfo) user_data)._id);
				sendMsgFailure(flag, code, user_data2);

				break;
			}

			case Consts.REQ_GRP_MODIFY: {
				dismissProgressDialog();
				break;
			}
			case Consts.REQ_BC_SHARE_TAG:
			case Consts.REQ_BC_SHARE_BROADCAST:
				sendMsgFailure(flag, code, user_data);
				break;
			default:
				break;
			}
			if (Consts.REQ_CODE_YOU_HAVE_BEEN_REMOVED == code) {
				vImageViewRight.setBackgroundResource(R.drawable.group_enable);
				vImageViewRight.setClickable(false);
				new Thread(new Runnable() {
					public void run() {
						AfGrpNotifyMsg afGrpNotifyMsg;
						final int modify_type = AfGrpNotifyMsg.BEEN_REMOVED;
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(roomName, groupId, CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), null, null, null, gversion, AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED, modify_type);

						AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg, PalmchatApp.getApplication());
						afMessageInfo.client_time = System.currentTimeMillis();
						int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
						afMessageInfo._id = msg_id;
						MessagesUtils.insertMsg(afMessageInfo, true, true);
						mAfCorePalmchat.AfDbGrpProfileSetStatus(groupID, Consts.AFMOBI_GRP_STATUS_EXIT);

						AfGrpProfileInfo grpInfo4 = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);

						if (grpInfo4 != null) {
							grpInfo4.version = afGrpNotifyMsg.gver;
							grpInfo4.members = CommonUtils.getRealList(grpInfo4.members, afGrpNotifyMsg.users_afid, afGrpNotifyMsg.users_name, Consts.ACTION_REMOVE);
							afMessageInfo.afidList = afGrpNotifyMsg.users_afid;

							grpInfo4.status = Consts.AFMOBI_GRP_STATUS_EXIT;
							mAfCorePalmchat.AfDbGrpProfileSetStatus(grpInfo4.afid, grpInfo4.status);
							afMessageInfo.status = grpInfo4.status;
							CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(grpInfo4, true, true);

						}
						mainHandler.obtainMessage(NOTICEFY, afMessageInfo).sendToTarget();
					}
				}).start();
				// ToastManager.getInstance().show(context,
				// context.getString(R.string.you_have_been_removed));
			} else if (Consts.REQ_CODE_GROUNP_NOT_EXISTS == code) {
				vImageViewRight.setBackgroundResource(R.drawable.group_enable);
				vImageViewRight.setClickable(false);
				ToastManager.getInstance().show(context, context.getString(R.string.group_exist));
			} else if (Consts.REQ_CODE_MEDIA_SESSTION_TIMEOUT != code) {
				if (CommonUtils.isGroupChatActivity(this)) {
					Consts.getInstance().showToast(context, code, flag, http_code);
				}
			}
		}
	}

	/**
	 * 设置布局内的按钮可点击
	 */
	private void ableClick() {
		vImageViewRight = (ImageView) findViewById(R.id.op2);
		vImageViewRight.setVisibility(View.VISIBLE);
		vImageViewRight.setBackgroundResource(R.drawable.group_detail);
		vImageViewRight.setClickable(true);

		new Thread(new Runnable() {
			public void run() {
				AfGrpProfileInfo afGrpProfileInfo = CacheManager.getInstance().searchGrpProfileInfo(groupID);
				if (afGrpProfileInfo != null) {
					afGrpProfileInfo.status = Consts.AFMOBI_GRP_STATUS_ACTIVE;
					CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(afGrpProfileInfo, true, true);
				}
			}
		}).start();
	}

	/**
	 * 将消息加入缓存
	 * 
	 * @param messageInfo
	 */
	private void addToCache(final AfMessageInfo messageInfo) {
		MessagesUtils.insertMsg(messageInfo, true, true);
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
						updateStatusForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid, afAttachImageInfo.progress);
						new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid).start();
						new Thread(new Runnable() {
							public void run() {
								PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  success  " + afAttachImageInfo.upload_id);
								mAfCorePalmchat.AfDbImageReqRmove(afAttachImageInfo.upload_id);
							}
						}).start();
					} else {
						PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  afAttachImageInfo  " + afAttachImageInfo);
					}
				} else {
					PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  afMessageInfo  " + afMessageInfo);
				}
		 }else{
			if (afMessageInfo != null) {
				final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				afAttachImageInfo.progress = event.getProgress();
				if (event.getProgress() == 100) {
					// updateImageStatus(afMessageInfo._id,
					// AfMessageInfo.MESSAGE_SENT,afAttachImageInfo);
					PalmchatLogUtils.println("GroupChatActivity  100 progress  " + event.getProgress());
				} else {
					PalmchatLogUtils.println("GroupChatActivity  progress  " + event.getProgress());
					updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo);
					updateStatusForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo.progress);
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
	public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {
		PalmchatLogUtils.println("GroupChatActivity  ywp: AfhttpHandleOnProgress httpHandle =" + httpHandle + " flag=" + flag + " progress=" + progress + " user_data = " + user_data);
		switch (flag) {
		case Consts.REQ_MEDIA_UPLOAD: 
			final AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
			SendImageEvent event=new SendImageEvent(afMessageInfo,progress,false,null);
			EventBus.getDefault().post(event);
			 //wxl20160419 这里只所以要搞EventBus事件 是因为 当在一个聊天里发了一张图没发完的时候 再退出  再进 百分比不会动了， 是因为已经不是同一个对象了			
			uploadMediaCallBack(event); //wxl20160419这里要再调一次 是因为当前可能没有开群聊或则私聊界面 所以这个时候也必须处理
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		PalmchatLogUtils.println("GroupChatActivity  onActivityResult--->>" + requestCode + "  data  " + data);
		emojjView.getViewRoot().setVisibility(View.GONE);
		chattingOptions.setVisibility(View.GONE);
		vButtonLeftKeyboard.setVisibility(View.GONE);
		vButtonKeyboard.setVisibility(View.GONE);
		vImageEmotion.setVisibility(View.VISIBLE);
		vImageViewMore.setVisibility(View.VISIBLE);

		PalmchatLogUtils.e(TAG, "GroupChatActivity----data:" + data);
		PalmchatLogUtils.e(TAG, "GroupChatActivity----requestCode:" + requestCode);
		PalmchatLogUtils.e(TAG, "GroupChatActivity----resultCode:" + resultCode);

		if (requestCode == BACK_FROM_SET_CHAT_BG) {
			setChatBg();
		}
		else if(requestCode == IntentConstant.REQUEST_CODE_CHATTING){
			if(adapterListView != null){
				adapterListView.notifyDataSetChanged();
			}
		}

		ArrayList<AfMessageInfo> list = CacheManager.getInstance().getAfMessageInfoList();
		if (null != list && 0 < list.size()) {
			for (int i = 0; i < list.size(); i++) {
				CommonUtils.getRealList(listChats, list.get(i));
			}
			CacheManager.getInstance().getAfMessageInfoList().clear();
			setAdapter(null);
		}

		if (requestCode == 0) {
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				isCreate = bundle.getBoolean("isCreate");
			}

		}
		if (requestCode == TITLE) {
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				boolean isQuit = bundle.getBoolean(JsonConstant.KEY_QUIT_GROUP);
				if (isQuit) {// exit group
					finish();
					return;
				}
				String roomName = bundle.getString("ROOMNAME");
				boolean clearGroupMsgSuccess = bundle.getBoolean("clearGroupMsgSuccess");
				PalmchatLogUtils.e(TAG, "GroupChatActivity----clearGroupMsgSuccess:" + clearGroupMsgSuccess);
				title_text.setText(roomName);
				boolean isEditGroupName = bundle.getBoolean("isEditGroupName");
				if(isEditGroupName) {
					isCreate = false;
					edit_group_name.setVisibility(View.GONE);
				}
				if (clearGroupMsgSuccess) {
					listChats.clear();
					setAdapter(null);
				}

			}
		}
		if (requestCode == CAMERA) {
			PalmchatLogUtils.println("GroupChatActivity  rotation==" + getWindowManager().getDefaultDisplay().getRotation());
			try {
				if (cameraFilename == null) {
					cameraFilename = SharePreferenceService.getInstance(this).getFilename();
					f = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
				} else {
					SharePreferenceService.getInstance(this).clearFilename();
				}
				cameraFilename = f.getAbsolutePath();// /mnt/sdcard/DCIM/Camera/1374678138536.jpg
				if (f != null && cameraFilename != null) {

					String tempStr = smallImage(f, MessagesUtils.CAMERA);
					if (tempStr == null || TextUtils.isEmpty(tempStr)) {
						return;
					}
					cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
					cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
					PalmchatLogUtils.e(TAG, "GroupChatActivity----cameraFilename:" + cameraFilename);
					if (CommonUtils.isEmpty(cameraFilename)) {
						mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
						// mainHandler.obtainMessage(MessagesUtils.MSG_ERROR_TIP,
						// null).sendToTarget();
						// ToastManager.getInstance().show(context,
						// R.string.fail_to_load_picture);
						return;
					}
					PalmchatLogUtils.println("GroupChatActivity  cameraFilename  " + cameraFilename);
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
						// ToastManager.getInstance().show(context, );
						PalmchatLogUtils.println("GroupChatActivity  onActivityResult  cursor  " + cursor + "  cursor.moveToFirst()  false");
						return;
					}
					String authority = originalUri.getAuthority();

					if (!CommonUtils.isEmpty(authority) && authority.contains(DefaultValueConstant.FILEMANAGER)) {
						path = cursor.getString(0);
					} else {
						path = cursor.getString(1);
					}
				}
				PalmchatLogUtils.e("path=", "GroupChatActivity  " + path); // 这个就是我们想要的原图的路径
				if (cursor != null) {
					cursor.close();
				}
				f = new File(path);
				File f2 = FileUtils.copyToImg(f.getAbsolutePath());
				if (f2 != null) {
					f = f2;
				}
				cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
				smallImage(f, MessagesUtils.PICTURE);
				cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
				cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
				if (CommonUtils.isEmpty(cameraFilename)) {
					mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
					// mainHandler.obtainMessage(MessagesUtils.MSG_ERROR_TIP,
					// null).sendToTarget();
					return;
				}
				PalmchatLogUtils.e("cameraFilename=", "GroupChatActivity  " + cameraFilename);
				mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
			}

		} else if (resultCode == 10) {
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				AfFriendInfo afFriendInfo = (AfFriendInfo) bundle.getSerializable(JsonConstant.KEY_FRIEND);
				PalmchatLogUtils.println("GroupChatActivity  afFriendInfo  " + afFriendInfo);
				mainHandler.obtainMessage(MessagesUtils.MSG_CARD, afFriendInfo).sendToTarget();
				// }
			}
		} else if (resultCode == 11) {
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				int msg_id = bundle.getInt(JsonConstant.KEY_ID, -1);
				boolean flag = bundle.getBoolean(JsonConstant.KEY_FLAG, false);
				String url = bundle.getString(JsonConstant.KEY_URL);
				int large_file_size = bundle.getInt(JsonConstant.KEY_FILESIZE, -1);
				String large_file_name = bundle.getString(JsonConstant.KEY_FILENAME);
				String small_file_name = bundle.getString(JsonConstant.KEY_LOCAL_IMG_PATH);
				int index = ByteUtils.indexOf(adapterListView.getLists(), msg_id);
				// PalmchatLogUtils.i(TAG, "找到第几�?-> " + index + "->" +
				// talkList.getSelectedItemPosition(),true);
				if (index != -1 && index < adapterListView.getCount()) {
					AfMessageInfo afMessageInfo = adapterListView.getItem(index);
					PalmchatLogUtils.println("GroupChatActivity  updateImageStatus  afMessageInfo  " + afMessageInfo);
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

		/* add by zhh 从相册页面返回 */
		if (requestCode == ALBUM_REQUEST && resultCode == Activity.RESULT_OK) { // 相册页面拍照返回
			if (data != null) {
				try {
					File f;
					String cameraFilename = data.getStringExtra("cameraFilename");
					if (cameraFilename == null) {
						cameraFilename = SharePreferenceService.getInstance(this).getFilename();

					} else {
						SharePreferenceService.getInstance(this).clearFilename();
					}
					f = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
					cameraFilename = f.getAbsolutePath();// /mnt/sdcard/DCIM/Camera/1374678138536.jpg
					if (f != null && cameraFilename != null) {

						String tempStr = smallImage(f, MessagesUtils.CAMERA);
						if (tempStr == null || TextUtils.isEmpty(tempStr)) {
							return;
						}
						cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
						cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
						PalmchatLogUtils.e(TAG, "GroupChatActivity----cameraFilename:" + cameraFilename);
						if (CommonUtils.isEmpty(cameraFilename)) {
							mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
							return;
						}
						PalmchatLogUtils.println("GroupChatActivity  cameraFilename  " + cameraFilename);
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
					/** �?要进行旋�? */
					Matrix matrix = new Matrix();
					matrix.setRotate(degree);
					Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					if (null != newbmp) {
						try {
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
						} catch (Exception e) {
							e.printStackTrace();
							return "";
						}

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
				// modify by wk
				// CacheManager.getInstance().setAfMessageInfo(afMessageInfo);
				CacheManager.getInstance().addmImgSendList(afMessageInfo);
				updateStatusForCamera(afMessageInfo);
				/*将消息加入本地缓存*/
				addToCache(afMessageInfo);

				mainHandler.sendEmptyMessageDelayed(Chatting.SEND_SHARE_IMAGE, 1000);

			}
		}).start();

	}

	/**
	 * zhh 拍照或者分享图片时，发送图片更新界面发送状态
	 * 
	 * @param afMessageInfo
	 */
	private void updateStatusForCamera(AfMessageInfo afMessageInfo) {
		ArrayList<AfMessageInfo> tmpList = CacheManager.getInstance().getmImgSendList();
		for (AfMessageInfo msg : tmpList) {
			mainHandler.obtainMessage(MessagesUtils.SEND_IMAGE, msg).sendToTarget();
		}
		CacheManager.getInstance().clearmImgSendList();
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
		PalmchatLogUtils.println("GroupChatActivity  getMessageInfoForImage begin " + begin);
		AfAttachImageInfo afAttachImageInfo = new AfAttachImageInfo();
		afAttachImageInfo.large_file_name = largeFileName;
		afAttachImageInfo.large_file_size = largeFileSize;
		afAttachImageInfo.small_file_name = smallFileName;
		afAttachImageInfo.small_file_size = smallFizeSize;
		//
		AfImageReqInfo param = new AfImageReqInfo();

		param.file_name = afAttachImageInfo.small_file_name;
		param.path = afAttachImageInfo.large_file_name;
		param.recv_afid = mFriendAfid;
		param.send_msg = mFriendAfid;
		//
		param._id = mAfCorePalmchat.AfDbImageReqInsert(param);
		afAttachImageInfo.upload_id = param._id;
		afAttachImageInfo.upload_info = param;
		PalmchatLogUtils.println("GroupChatActivity  param._id  " + param._id);

		afAttachImageInfo._id = mAfCorePalmchat.AfDbAttachImageInsert(afAttachImageInfo);

		AfMessageInfo afMessageInfo = new AfMessageInfo();
		afMessageInfo.attach = afAttachImageInfo;
		afMessageInfo.attach_id = afAttachImageInfo._id;
		afMessageInfo.client_time = System.currentTimeMillis();
		afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_IMAGE;
		afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
		afMessageInfo.fromAfId = mFriendAfid;
		afMessageInfo.attach = afAttachImageInfo;

		afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
		// afAttachImageInfo.upload_info = new AfImageReqInfo();
		PalmchatLogUtils.println("GroupChatActivity  getMessageInfoForImage end " + (System.currentTimeMillis() - begin));
		return afMessageInfo;
	}

	private long lastSoundTime;

	/**
	 * 当前页面接收到消息的处理
	 */
	@Override
	public void handleMessageFromServer(final AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("GroupChatActvity " + afMessageInfo);
		setVoiceQueue(afMessageInfo);
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		if (mFriendAfid.equals(afMessageInfo.toAfId)) {
			setUnReadCount();
			if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ || msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS) {
				friendRequest(afMessageInfo);
			} else {
				int type = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
				switch (type) {
				case AfMessageInfo.MESSAGE_GRP_CREATE:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_CREATE " + afMessageInfo.toAfId);
					break;
				case AfMessageInfo.MESSAGE_GRP_ADD_MEMBER:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_ADD_MEMBER " + afMessageInfo.toAfId);
					// listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					ableClick();// 更换可点击的查看的按钮图片
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_FRIEND_REQ:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_FRIEND_REQ " + afMessageInfo.toAfId);
					break;
				case AfMessageInfo.MESSAGE_GRP_MODIFY:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_MODIFY " + afMessageInfo.toAfId);
					title_text.setText(afMessageInfo.name);
					// listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER:
				case AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_REMOVE_MEMBER OR MESSAGE_GRP_HAVE_BEEN_REMOVED" + afMessageInfo.toAfId);
					// listChats.add(afMessageInfo);
					enableClick(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_DROP:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_DROP " + afMessageInfo.toAfId);
					// listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_DESTROY:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_DESTROY " + afMessageInfo.toAfId);
					// listChats.add(afMessageInfo);
					enableClick(afMessageInfo);
					CommonUtils.getRealList(listChats, afMessageInfo);
					setAdapter(null);
					break;
				default:
					PalmchatLogUtils.println("handleMessageFromServer  begin  System.currentTimeMillis() - lastSoundTime  " + (System.currentTimeMillis() - lastSoundTime));
					if (GroupChatActivity.class.getName().equals(CommonUtils.getCurrentActivity(this))) {
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
						mAfCorePalmchat.AfHttpGetInfo(new String[]{afMessageInfo.msg}, Consts.REQ_GET_INFO, null, afMessageInfo, this);
					} else {
						// listChats.add(afMessageInfo);
						CommonUtils.getRealList(listChats, afMessageInfo);
						setAdapter(null);
						if(afMessageInfo.toAfId != null && mAfCorePalmchat.AfDbSettingGetTips(afMessageInfo.toAfId)) {
							showVibrate();
						}
					}
					break;
				}
			}

		} else {
			if (MessagesUtils.isGroupSystemMessage(afMessageInfo.type)) {
				PalmchatLogUtils.println("GroupChatActivity isGroupSystemMessage return " + afMessageInfo.msg);
				return;
			}

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
					vTextViewOtherMessageToast.setText(R.string.voice);
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
					if(afAttachPAMsgInfo != null) {
						vTextViewOtherMessageToast.setText(afAttachPAMsgInfo.content);
					}
					break;
				case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
					vTextViewOtherMessageToast.setText(R.string.msg_share_broadcast);
					break;
				case AfMessageInfo.MESSAGE_FRIEND_REQ:
				case AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS:
					vTextViewOtherMessageToast.setText(CommonUtils.getFriendRequestContent(PalmchatApp.getApplication().getApplicationContext(),afMessageInfo));
					/*friendRequest(afMessageInfo);
					return;*/
				default:
					String msgs = afMessageInfo.msg;
					final CharSequence contents = EmojiParser.getInstance(context).parse(msgs);
					vTextViewOtherMessageToast.setText(contents);
				}
				final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.fromAfId);
				PalmchatLogUtils.println("GroupChatActivity  handleMessage  afFriendInfo  " + afFriendInfo);
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
								// refreshView(afMessageInfo);
								if(afFriendInfo != null) {
									toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
								}
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
				PalmchatLogUtils.println("GroupChatActivity Chatroom message");
			}
		}
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

	private void enableClick(final AfMessageInfo afMessageInfo) {
		if (afMessageInfo.status == Consts.AFMOBI_GRP_STATUS_EXIT) {
			//
			vImageViewRight.setBackgroundResource(R.drawable.group_enable);
			vImageViewRight.setClickable(false);
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
		intent.putExtra(JsonConstant.KEY_FRIEND, infos);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		context.startActivity(intent);
		finish();
	}

	/**
	 * 震动
	 */
	private void showVibrate() {
		if (PalmchatApp.getApplication().getSettingMode().isVibratio()) {
			if ((System.currentTimeMillis() - lastSoundTime) > 4000 || lastSoundTime == 0) {
				TipHelper.vibrate(context, 200L);
			}
			lastSoundTime = System.currentTimeMillis();
			PalmchatLogUtils.println("GroupChatActivity  handleMessageFromServer  end  lastSoundTime  " + lastSoundTime);
		}
	}

	/**
	 * 处理好友请求的消息
	 *
	 * @param afMessageInfo
	 */ void friendRequest(final AfMessageInfo afMessageInfo) {
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ) {
			AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				vTextViewOtherMessageToast.setBackgroundDrawable(null);
			}else{
				vTextViewOtherMessageToast.setBackground(null);
			}
			String showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, afFriendInfo.name, getString(R.string.want_to_be_friend_ignored));
			vTextViewOtherMessageColon.setVisibility(View.GONE);
			vTextViewOtherMessageToast.setText(showStr);
			StartTimer.timerHandler.post(StartTimer.startTimer(afMessageInfo, viewFrameToast));
			viewFrameToast.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
				  finish();
				 }
			});
		} else if (msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS) {
			AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				vTextViewOtherMessageToast.setBackgroundDrawable(null);
			}else{
				vTextViewOtherMessageToast.setBackground(null);
			}
			String showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, afFriendInfo.name, getString(R.string.frame_toast_friend_req_success));
			vTextViewOtherMessageToast.setText(showStr);
			vTextViewOtherMessageColon.setVisibility(View.GONE);
			StartTimer.timerHandler.post(StartTimer.startTimer(afMessageInfo, viewFrameToast));
			viewFrameToast.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
				  finish();
			 	}
			});
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
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
				// afMessageInfo.attach = afAttachVoiceInfo;
				afMessageInfo.client_time = System.currentTimeMillis();
				afMessageInfo.fromAfId = mFriendAfid;
				// afMessageInfo.toAfId = mFriendAfid;
				afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_EMOTIONS;
				afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

				afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
				mainHandler.obtainMessage(MessagesUtils.MSG_EMOTION, afMessageInfo).sendToTarget();
				addToCache(afMessageInfo);
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
				// afMessageInfo.client_time = System.currentTimeMillis();
				afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
				mAfCorePalmchat.AfDbGrpMsgUpdate(afMessageInfo);
				MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
				mainHandler.obtainMessage(MessagesUtils.MSG_EMOTION, afMessageInfo).sendToTarget();
			}
		}).start();
	}

	/** 重新语音
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
						mainHandler.sendEmptyMessage(MSG_TOO_SHORT);
					} else {
						if (data == null || data.length <= AT_LEAST_LENGTH) {
							mainHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
							return;
						}
						sendMessageInfoForText(AfMessageInfo.MESSAGE_VOICE, voiceName, data.length, recordTimeTemp, mainHandler, MessagesUtils.SEND_VOICE, ACTION_UPDATE, afMessageInfo, null);
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
	 * 清除聊天记录
	 */
	public void clearMessage() {
		AppDialog appDialog = new AppDialog(context);
		String msg = context.getResources().getString(R.string.clear_chat_history);
		appDialog.createClearDialog(context, msg, new OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				final AfMessageInfo afMessageInfo = adapterListView.getLastMsg();
				adapterListView.getLists().clear();
				setAdapter(null);
				new Thread(new Runnable() {
					public void run() {
						PalmchatLogUtils.println("GroupChatActivity  clearMessage  afMessageInfo  " + afMessageInfo);
						// MessagesUtils.removeMsg( afMessageInfo, true, true);
						// mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_GRP,
						// mFriendAfid);
						AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mFriendAfid, 0, Integer.MAX_VALUE);
						if (null != recentDataArray && recentDataArray.length > 0) {
							for (AfMessageInfo messageInfo : recentDataArray) {
								mAfCorePalmchat.AfDbGrpMsgRmove(messageInfo);
								// 清除本地聊天记录
								MessagesUtils.removeMsg(messageInfo, true, true);
							}
							mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mFriendAfid);
						}
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

	public final class BundleKeys {
		public static final String ROOM_ID = "room_id";

		public static final String ROOM_NAME = "room_name";

		public static final String MEMBER_COUNT = "member_count";

	}

	private boolean isShowDialog = false;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
		if (!isRefreshing) {
			isRefreshing = true;
			mOffset = listChats.size();
			vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
//			new GetDataTask(false).execute();
			getMsgData(false);
		}
	}

	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取最近20条聊天信息
	 * 
	 * @return
	 */
	private List<AfMessageInfo> getRecentData() {
		PalmchatLogUtils.println("GroupChatActivity  mOffset  " + mOffset + "  mCount  " + mCount);
		AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mFriendAfid, mOffset, mCount);
		if (recentDataArray == null || recentDataArray.length == 0) {
			return new ArrayList<AfMessageInfo>();
		}
		List<AfMessageInfo> recentDataList = Arrays.asList(recentDataArray);
		return recentDataList;
	}

	/**
	 * 异步加载缓存到数据库中的聊天信息
	 */
	/*private class GetDataTask extends AsyncTask<Void, Void, List<AfMessageInfo>> {

		// 是否为初始化数据，true为初始化数据，false为下拉刷新数据
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
					if (recentData.size() >= 17) {
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
			// mListItems.addFirst("Added after refresh...");
			super.onPostExecute(result);
			if (isInit) { // 第一次进入时，先清除列表
				listChats.clear();
						 转发 
				if (isForward) {
					forwardMsg();
				}
			} else {
				stopRefresh();
			}
			bindData(result);
			if(isInit){
				getData();
			}
 
		}

	} 

	/**
	 * 将数据显示在列表上
	 * 
	 * @param result
	 */
	private void bindData(List<AfMessageInfo> result) {
		int size = result.size();
		int listSize = listChats.size();
		if (size > 0) {
			for (int i = 0; i < result.size(); i++) {
				AfMessageInfo afMessageInfo = result.get(i);
				/* 将消息add或者set在列表集合中 */
				CommonUtils.getRealList(listChats, afMessageInfo);
				/* 获取语音信息 */
				setVoiceQueue(afMessageInfo);
			}
			if (!mIsFirst) {
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
				if (pop) {
					vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					pop = false;
				}
				adapterListView.notifyDataSetChanged();
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
	}

	/**
	 * 获取语音列表数据
	 * 
	 * @param afMessageInfo
	 */
	private void setVoiceQueue(AfMessageInfo afMessageInfo) {
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		int status = afMessageInfo.status;
		if (MessagesUtils.isReceivedMessage(status) && msgType == AfMessageInfo.MESSAGE_VOICE) {// from
																								// others
																								// chat
																								// and
																								// voice
																								// type
			CommonUtils.getRealList(listVoiceChats, afMessageInfo);
		}
	}

	/**
	 * delete
	 */
	public void delete(final AfMessageInfo afMessageInfo) {
		new Thread(new Runnable() {
			public void run() {
				mAfCorePalmchat.AfDbGrpMsgRmove(afMessageInfo);
				if (MessagesUtils.isLastMsg(afMessageInfo)) {
					AfMessageInfo lastMsg = adapterListView.getLastMsg();
					if (lastMsg != null) {
						MessagesUtils.insertMsg(lastMsg, true, true);
					} else {
						AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mFriendAfid, 0, 1);
						if (recentDataArray == null || recentDataArray.length <= 0) {
							MessagesUtils.removeMsg(afMessageInfo, true, true);
						}
					}

				}
			}
		}).start();

	}

	void toGroupChatting(String groupId) {
		AfGrpProfileInfo groupListItem = null;
		groupListItem = CacheManager.getInstance().searchGrpProfileInfo(groupId);
		Bundle bundle = new Bundle();
		bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
		if (groupListItem != null && !"".equals(groupListItem.afid) || null != groupListItem.afid) {
			bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID, groupListItem.afid);
		}
		if (groupListItem != null && !"".equals(groupListItem.name) || null != groupListItem.name) {
			bundle.putString(GroupChatActivity.BundleKeys.ROOM_NAME, groupListItem.name);
		}
		bundle.putBoolean(JsonConstant.KEY_FLAG, true);
		HelpManager.getInstance(context).jumpToPage(GroupChatActivity.class, bundle, false, 0, false);
	}

	private void stopRefresh() {
		// TODO Auto-generated method stub
		isRefreshing = false;
		vListView.stopRefresh();
		vListView.stopLoadMore();
		vListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

//		MobclickAgent.onPageEnd("GroupChatActivity"); // 保证 onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息
//		MobclickAgent.onPause(this);

		mIsFirst = true;
		mOffset = 0;

		int dbInt = mAfCorePalmchat.AfDbSetMsgExtra(mFriendAfid, getEditTextContent());
		PalmchatLogUtils.println("mFriendAfid  " + mFriendAfid + " group  onPause  dbInt " + dbInt + "  getEditTextContent  " + getEditTextContent());

		// is recording, so send voice
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
				}

				stopRecordingTimeTask();
			}
			setMode(SharePreferenceService.getInstance(GroupChatActivity.this).getListenMode());

			// show cancelled text
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

	}

	/** 重发图片
	 * @param afMessageInfo
	 */
	public void resendImage(final AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
		if (afAttachImageInfo != null) {
			// afMessageInfo.client_time = System.currentTimeMillis();
			afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
			// listChats.add(afMessageInfo);
			CommonUtils.getRealList(listChats, afMessageInfo);
			setAdapter(afMessageInfo);
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

				mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, afMessageInfo._id, 0, null, Consts.AFMOBI_PARAM_FID);

			}
			new Thread(new Runnable() {
				public void run() {
					mAfCorePalmchat.AfDbImageReqUPdate(afAttachImageInfo.upload_info);
					// addToCache(afMessageInfo);
					MessagesUtils.setRecentMsgStatus(afMessageInfo, afMessageInfo.status);
				}
			}).start();
			new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid).start();
			int handle = mAfCorePalmchat.AfHttpSendImage(afAttachImageInfo.upload_info, afMessageInfo, this, this);
			sendRequestError(afMessageInfo, handle, Consts.SEND_IMAGE);
		} else {
			PalmchatLogUtils.println("GroupChatActivity  resendImage " + afMessageInfo);
		}
	}

	@Override
	protected void onDestroy() {
		PalmchatLogUtils.e(TAG, "----GroupChatActivity----onDestroy----");
		// EventBus反注册
		EventBus.getDefault().unregister(this);
		PalmchatApp.getApplication().removeMessageReceiver(this);
		looperThread.looper.quit();
		super.onDestroy();

		PalmchatLogUtils.i("hj", "GroupChat Ondestroy");
		if(mScreenObserver != null) {
			mScreenObserver.stopScreenStateUpdate();
		}
	}

	@Override
	public void onItemClick(int item) {
		PalmchatLogUtils.println("GroupChatting onItemClick " + item);
		// 隐藏聊天页面对话框
		if (widget != null) {
			widget.cancel();
		}
		switch (item) {
		case ChattingMenuDialog.ACTION_SPEAKER_MODE: // 普通模式
			AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setMode(AudioManager.MODE_NORMAL);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			if (!mIsFirst) {
				ToastManager.getInstance().show(GroupChatActivity.this, getString(R.string.switch_speaker_mode));
			}
			SharePreferenceService.getInstance(GroupChatActivity.this).setListenMode(AudioManager.MODE_NORMAL);
			break;
		case ChattingMenuDialog.ACTION_HANDSET_MODE: // 通话模式
			AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
			audio.setSpeakerphoneOn(false);
			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
			audio.setMode(AudioManager.MODE_IN_CALL);
			if (!mIsFirst) {
				ToastManager.getInstance().show(GroupChatActivity.this, getString(R.string.switch_handset_mode));
			}
			SharePreferenceService.getInstance(GroupChatActivity.this).setListenMode(AudioManager.MODE_IN_CALL);
			break;
		case ChattingMenuDialog.ACTION_CLEAR_DATA:
			clearMessage();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(int position, String name, String afid, byte sex) {
		// TODO Auto-generated method stub
		closeEmotions();
		String str = getEditTextContent();
		str = str + "@" + name + ":";
		if (str.length() <= ReplaceConstant.MAX_SIZE) {
			vEditTextContent.setText(EmojiParser.getInstance(getApplicationContext()).parse(str));

			vEditTextContent.requestFocus();
			if (!TextUtils.isEmpty(str)) {
				vEditTextContent.setSelection(str.length());
			}
			disableViews();
			CommonUtils.showSoftKeyBoard(vEditTextContent);
		}
	}

	/** 发送gif图片
	 * @param text
	 * @param i
	 * @param j
	 * @param b
	 */
	public void sendGifImage(final String text) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				AfMessageInfo afMessageInfo = new AfMessageInfo();
				afMessageInfo.msg = text;
				// afMessageInfo.attach = afAttachVoiceInfo;
				afMessageInfo.client_time = System.currentTimeMillis();
				// afMessageInfo.fromAfId =
				// CacheManager.getInstance().getMyProfile().afId;
				afMessageInfo.fromAfId = mFriendAfid;
				afMessageInfo.type = AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_STORE_EMOTIONS;
				afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;

				afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
				mainHandler.obtainMessage(MessagesUtils.MSG_GIF, afMessageInfo).sendToTarget();
				addToCache(afMessageInfo);
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
			mAfCorePalmchat.AfDbGrpMsgUpdate(afMessageInfo);
			mainHandler.obtainMessage(MessagesUtils.MSG_GIF, afMessageInfo).sendToTarget();

			PalmchatLogUtils.println("resendGifImage---msg id:" + afMessageInfo._id);

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

	public void emojj_del() {
		CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
	}
}
