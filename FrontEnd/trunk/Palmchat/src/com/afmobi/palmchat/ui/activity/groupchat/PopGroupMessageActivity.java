package com.afmobi.palmchat.ui.activity.groupchat;

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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.PalmchatApp.PopMessageReceiver;
import com.afmobi.palmchat.broadcasts.HomeBroadcast;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
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
import com.afmobi.palmchat.ui.activity.chats.model.EmotionModel;
import com.afmobi.palmchat.ui.activity.groupchat.adapter.PopGroupMessageAdapter;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
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
//import com.afmobi.palmchat.util.image.ImageLoader;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfAttachImageInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpNotifyMsg;
import com.core.AfGrpProfileInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfImageReqInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;

import de.greenrobot.event.EventBus;

import static com.afmobi.palmchat.ui.activity.main.constant.Constants.SYSTEM_DIALOG_REASON_HOME_KEY;
import static com.afmobi.palmchat.ui.activity.main.constant.Constants.SYSTEM_DIALOG_REASON_KEY;
//import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author heguiming modify 2013-12-13
 */
public class PopGroupMessageActivity extends BaseActivity implements OnClickListener,
		AfHttpResultListener, AfHttpProgressListener, OnEditorActionListener,
		PopMessageReceiver, OnItemClick,OnItemLongClick {
	
	private static final String TAG = PopGroupMessageActivity.class.getCanonicalName();
	
	public final static int ACTION_INSERT = 0;
	private final static int EDIT_TEXT_CHANGE = 1;
	private PopGroupMessageAdapter adapterListView;
	private ListView vListView;
	private ArrayList<AfMessageInfo> listChats = new ArrayList<AfMessageInfo>();
	private ArrayList<AfMessageInfo> listVoiceChats = new ArrayList<AfMessageInfo>();

	private EmojjView emojjView;
	private CutstomEditText vEditTextContent;
	private ImageView vButtonSend, vButtonBackKeyboard,vButtonKeyboard;
	private AfPalmchat mAfCorePalmchat;
	private String mFriendAfid;
	private boolean isForward;
	private ImageView vImageEmotion;
	private View vImageViewVoice;
	private View viewUnTalk,viewFrameToast;
	private View viewOptionsLayout, viewChattingVoiceLayout;
	private Button vButtonTalk;
	private boolean sent;
	private MediaRecorder mRecorder;
	private long recordStart;
	private long recordEnd;
	private int recordTime;
	private TextView vTextViewRecordTime;
	private TextView mCancelVoice;
	private RelativeLayout edit_group_name;
	private Button et_exit;
	private EditText et_group_name;

	/* emotion */
	private String cameraFilename;
	private File f;
	private LinearLayout chatting_emoji_layout;
	private List<EmotionModel> optionsData = new ArrayList<EmotionModel>();
	public String[] optionsString;
	int optionsInt[] = { R.drawable.chatting_camara_icon_selector,
			R.drawable.chatting_gallary_icon_selector,
			R.drawable.chatting_voice_icon_selector, 
			R.drawable.chatting_name_card_icon_selector};
	private String mVoiceName;
	private static final int CAMERA = 1;
	public static final int TITLE = 7;
	private static final int MSG_TOO_SHORT = -2;

	private static final int NOTICEFY = 0;
//	private String mSmallFilename;
	private final int ALBUM_REQUEST = 3; //add by zhh 发送请求到相册页面
	byte[] data;
	ChattingMenuDialog widget;

	RippleView mRippleView;
	private ImageView mVoiceImageEmotion, mVoiceImageViewMore;
	private ProgressBar mVoiceProgressBar, mVoiceProgressBar2;
	
	private boolean isCanSave=false;

	private int mOffset = 0;
	private int mCount = 1;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
	private boolean mIsFirst = true;
	private boolean mIsNoticefy = true;
	private boolean isInResume = false;

//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	private static final int RECORDING_TIMER = 9000;
	private boolean isOnClick = false;
	private final static int AT_LEAST_LENGTH = 50;
	
	private boolean mIsRecording;

	ScreenObserver mScreenObserver;
	private HomeBroadcast mHomeBroadcast;
	private Handler mainHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(vListView != null){
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			switch (msg.what) {
			case RECORDING_TIMER:
				long recordCurTime = System.currentTimeMillis();
				int voiceProgress = (int)((recordCurTime - recordStart) / 1000);
				if (voiceProgress <= RequestConstant.RECORD_VOICE_TIME) {
					mVoiceProgressBar.setProgress(voiceProgress);
					mVoiceProgressBar2.setProgress(voiceProgress);
				}
				vTextViewRecordTime.setText(CommonUtils.diffTime(recordCurTime, recordStart));
				if (recordStart > 0 && recordCurTime - recordStart >= RequestConstant.RECORD_VOICE_TIME * 1000 && !sent) {
					stopRecording();
					stopRecordingTimeTask();
					if(CommonUtils.getSdcardSize()){
						ToastManager.getInstance().show(context, R.string.memory_is_full);
						break;
					}
					sent = true;
					mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
				}
				break;
			case MessagesUtils.MSG_VOICE:
			{
				PalmchatLogUtils.println("GroupChatActivity  mVoiceName " + mVoiceName);
				sendVoice(mVoiceName);
				break;
			}
			case MessagesUtils.SEND_VOICE:
			{
				//send voice
				AfMessageInfo msgInfoVoice = (AfMessageInfo)msg.obj;
				AfAttachVoiceInfo afAttachVoiceInfo = (AfAttachVoiceInfo) msgInfoVoice.attach;
				final int voiceLength = afAttachVoiceInfo.voice_len;
				PalmchatLogUtils.println("GroupChatActivity  voiceLength  " + voiceLength);
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_VOICE);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);
				
//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_VOICE);
//				MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);
				
				int code = mAfCorePalmchat.AfHttpSendVoice(mFriendAfid, System.currentTimeMillis(), data, data.length, voiceLength, msgInfoVoice._id, PopGroupMessageActivity.this, PopGroupMessageActivity.this);
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
			case MessagesUtils.MSG_PICTURE:
			{
				String largeFilename = (String) msg.obj;//压缩后大图全路径
//				byte[] bytes = FileUtils.readBytes(f.getAbsolutePath());
//				byte [] bytesPic = BitmapUtils.bitmapToBytes(Chatting.this,f);
				File largeFile = new File(largeFilename);
				int largeFileSize = 0;
				if(largeFile != null){
					largeFileSize = (int) (largeFile.length() /1024);
				}
				
				File f = new File(largeFilename);
				File f2 = FileUtils.copyToImg(f.getAbsolutePath());
				if (f2 != null) {
					f = f2;
				}
				String smallFileName = smallImage(f,MessagesUtils.PICTURE);
				
				File smaleFile = new File(RequestConstant.IMAGE_CACHE+smallFileName);
				int smallFileSize = 0;
				if(smaleFile != null){
					smallFileSize = (int) (smaleFile.length() / 1024);
				}
				
				initMedia(largeFilename ,largeFileSize , smallFileName ,smallFileSize);


				break;
			}
			
			case MessagesUtils.EDIT_TEXT_CHANGE:
			{
				int len = (Integer)msg.obj;
				if(len > DefaultValueConstant.LENGTH_0){
					vButtonSend.setVisibility(View.VISIBLE);
					vImageViewVoice.setVisibility(View.GONE);
				}else{
					vButtonSend.setVisibility(View.GONE);
					vImageViewVoice.setVisibility(View.VISIBLE);
				}
				break;
			}
			
			case MessagesUtils.MSG_TEXT://send text
			{
				AfMessageInfo msgInfoText = (AfMessageInfo)msg.obj;
				send(msgInfoText.msg, msgInfoText);
//				vEditTextContent.setText("");
				PalmchatLogUtils.println("GroupChatActivity  msgInfoText._id  "+msgInfoText._id);
				break;
			}	
			case MessagesUtils.MSG_EMOTION:
			{
				AfMessageInfo afMessageInfo = (AfMessageInfo)msg.obj;
				
//				listChats.add(afMessageInfo);
				CommonUtils.getRealList(listChats,afMessageInfo);
				setAdapter(afMessageInfo);
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_EMOTION);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);
				
//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_EMOTION);
//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_TEXT);
//				MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);
				
				int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.fromAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_EMOTION, afMessageInfo._id, PopGroupMessageActivity.this);
				break;
			}

			case MessagesUtils.MSG_TOO_SHORT:
			{
				ToastManager.getInstance().show(context, R.string.message_too_short);
				break;
			}
			
			case MessagesUtils.MSG_VOICE_RESEND://resend voice
			{
				
				break;
			}
			
			case MessagesUtils.MSG_RESEND_CARD:
			{
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
//				listChats.add(afMessageInfo);
				CommonUtils.getRealList(listChats, afMessageInfo);
				setAdapter(afMessageInfo);
				PalmchatLogUtils.println("GroupChatActivity  afMessageInfo._id  " + afMessageInfo._id);
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_CARD);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);
				
//				MobclickAgent.onEvent(context, ReadyConfigXML.S_M_CARD);
//				MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);
				
				int code = mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.fromAfId, System.currentTimeMillis(), afMessageInfo.msg, Consts.MSG_CMMD_RECOMMEND_CARD, afMessageInfo._id, PopGroupMessageActivity.this);
				break;
			}
			
			case MessagesUtils.SEND_IMAGE:
			{
				AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
				if(mFriendAfid.equals(afMessageInfo.fromAfId)){
					AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
					AfImageReqInfo param = afAttachImageInfo.upload_info;
	//				listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats,afMessageInfo);
					setAdapter(afMessageInfo);
					
					// heguiming 2013-12-04
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_PIC);
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);
					
//					MobclickAgent.onEvent(context, ReadyConfigXML.S_M_PIC);
//					MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);
					
					int code =  mAfCorePalmchat.AfHttpSendImage(param, afMessageInfo, PopGroupMessageActivity.this, PopGroupMessageActivity.this);
				}
				break;
			}
			case EditGroupActivity.GROUP_NAME_CHANGED: {
				edit_group_name.setVisibility(View.GONE);
				Toast.makeText(context, R.string.setting_name_success,
						Toast.LENGTH_SHORT).show();
				
				break;
			}
			case EditGroupActivity.GROUP_SET_NAME_FAIL: {
				Toast.makeText(context, R.string.editmygroup_name_change_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case MessagesUtils.FAIL_TO_LOAD_PICTURE:
			{
				ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
				break;
			}
			case NOTICEFY:
			{
				AfMessageInfo afMessageInfo = (AfMessageInfo)msg.obj;
				handlePopMessageFromServer(afMessageInfo);
			}
				break;
			case MessagesUtils.MSG_CAMERA_NOTIFY:{
				AfMessageInfo afMessageInfo =  (AfMessageInfo) msg.obj;
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				updateImageStautsByBroadcast(afMessageInfo._id,afMessageInfo.status, afMessageInfo.fid, afAttachImageInfo.progress);
				break;
			}
			
			case MessagesUtils.MSG_SET_STATUS:{
				PalmchatLogUtils.println("---www : grp MSG_SET_STATUS");
//				notify recent chats refresh
//					sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
				EventBus.getDefault().post(new RefreshChatsListEvent());
				break;
			}
			
			
			case MessagesUtils.MSG_ERROR_OCCURRED:
				ToastManager.getInstance().show(context, R.string.error_occurred);
				break;
			
			default:
				break;
			}
		}

	};

	private LooperThread looperThread;
	  
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
				PalmchatLogUtils.i("hj", "PopGroupMessageActivity BroadcastReceiverdoubleScreen");
				if(PalmchatApp.getApplication().isDoubleClickScreen) {
					PalmchatApp.getApplication().isDoubleClickScreen = false;
					clearData();
				}
			}
		});
		mHomeBroadcast = new HomeBroadcast(this, new HomeBroadcast.HomeStateListener() {
			@Override
			public void OnPressHome() {
				back();
			}
		});
		CacheManager.getInstance().getPopMessageManager().addPopMessageActivity(this);

		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_G);
		
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_G);
		
		looperThread = new LooperThread();
		looperThread.setName(TAG);
		looperThread.start();
		
		super.onCreate(savedInstanceState);
		PalmchatLogUtils.println("--rrr1 PopGroupMessageActivity onCreate");
	}
	

	class LooperThread extends Thread {
		
		// update cache data
		private static final int SEND_IMAGE_LOOPER = 9991;
		
		Handler handler;
		Looper looper;
		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			//single thread model
			handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SEND_IMAGE_LOOPER:
						
						String largeFilename = (String) msg.obj;// big image path
						File f = new File(largeFilename);
						File f2 = FileUtils.copyToImg(f.getAbsolutePath());
						if (f2 != null) {
							f = f2;
						}
						String cameraFilename = f2.getAbsolutePath();//f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
						String smallFileName = smallImage(f,MessagesUtils.PICTURE);
						cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
						cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
						
						if(CommonUtils.isEmpty(cameraFilename)){
							mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
							return;
						}
						
						largeFilename = cameraFilename;
						
						File largeFile = new File(largeFilename);
						int largeFileSize = 0;
						if(largeFile != null){
							largeFileSize = (int) (largeFile.length() /1024);
						}
						File smaleFile = new File(RequestConstant.IMAGE_CACHE + smallFileName);
						int smallFileSize = 0;
						if(smaleFile != null){
							smallFileSize = (int) (smaleFile.length() / 1024);
						}
						
						initMedia(largeFilename ,largeFileSize , smallFileName ,smallFileSize);
						break; 
						
					}
					
				}
			};
			
			Looper.loop();
			
		}
		
		}

	/** 发送语音
	 * @param voiceName 音频文件名称
	 */
	public void sendVoice(final String voiceName) {
		if (!CommonUtils.isEmpty(voiceName)) {
			new Thread(new Runnable() {
				public void run() {
					data = FileUtils.readBytes(RequestConstant.VOICE_CACHE
							+ voiceName);
					if(data == null){ //判断文件是否存在
						PalmchatLogUtils.println("sendVoice  data: null");
						return;
					}
					if (recordTime < 2) { //判断录音时间是否太短
						mainHandler.sendEmptyMessage(MSG_TOO_SHORT);
					} else {
						
						if(data == null || data.length <= AT_LEAST_LENGTH) {
							mainHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
							return;
						}

						insertAndSendMessageInfoForText(AfMessageInfo.MESSAGE_VOICE,
								voiceName, data.length, recordTime,
								mainHandler, MessagesUtils.SEND_VOICE, ACTION_INSERT, null,null);
						back();//返回并关闭此Pop窗口
					}
				}
			}).start();
		} else {
			ToastManager.getInstance().show(context,
					getString(R.string.sdcard_unmounted));
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		  if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
			  
			  return true;
		  }
		
		return false;
	}

	private String groupID;

	private boolean isBottom = false;
	
//	pop head layout
	
	private ImageView mPopHeadExit;
	private TextView mPopHeadName, mPopHeadId;
	
	private WakeLock mWakeLock;
	
	@Override
	public void findViews() {
		//设置语音模式
		setListenMode();
		Intent intent = getIntent();
		groupID = intent.getStringExtra(BundleKeys.ROOM_ID);
//		mCount = intent.getIntExtra(JsonConstant.KEY_POP_MSG_COUNT, 1);
		//初始化群组对象
		AfGrpProfileInfo search = new AfGrpProfileInfo();
		search.afid = groupID;
		
		AfGrpProfileInfo grp = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).search(search, false, true);
			if(grp==null){//如果不存在此群组，则关闭
				clearData();
				return ;
			}
			roomName = grp.name;
		
			mFriendAfid = groupID;
			groupId = groupID;
			
			mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat; 
		
		WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
		localLayoutParams.gravity = Gravity.TOP;
		onWindowAttributesChanged(localLayoutParams);
		getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);//设置窗体外的点击事件

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //设置锁屏时显示此窗体
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON //设置启动此窗体时点亮屏幕
				| WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER //设置显示桌面
				| LayoutParams.FLAG_DISMISS_KEYGUARD);//设置不经过解锁直接显示此窗体
		setContentView(R.layout.activity_pop_group_message);
		PalmchatLogUtils.println("GroupChatActivity  groupID  "+groupID);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
	                | PowerManager.ACQUIRE_CAUSES_WAKEUP //如果有请求锁时，强制打开screen和keyboard light
					| PowerManager.ON_AFTER_RELEASE, //如释放销时，重新设置timer
	                "com.afmobi.palmchat");
		 
		mPopHeadExit = (ImageView) findViewById(R.id.pophead_exit);
		mPopHeadExit.setOnClickListener(this);
		
		findViewById(R.id.itemimage).setVisibility(View.GONE);
		
		findViewById(R.id.group_heads).setVisibility(View.VISIBLE);
		
		ImageView grpHead1 = (ImageView) findViewById(R.id.group_head_1);
		ImageView grpHead2 = (ImageView) findViewById(R.id.group_head_2);
		ImageView grpHead3 = (ImageView) findViewById(R.id.group_head_3);
//		ImageView grpHead4 = (ImageView) findViewById(R.id.group_head_4);
		
		ImageView[] imageviews = {grpHead1, grpHead2, grpHead3};
		setGroupAvatar(imageviews, grp);//设置群组头像
		
		mPopHeadName = (TextView) findViewById(R.id.pophead_name);
		mPopHeadId = (TextView) findViewById(R.id.pophead_id);
		
		mPopHeadName.setText(grp.name);
		mPopHeadId.setText(DefaultValueConstant.EMPTY);

		vButtonKeyboard = (ImageView)findViewById(R.id.btn_keyboard);
		vButtonKeyboard.setOnClickListener(this);
		vListView = (ListView) findViewById(R.id.listview);
		vListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {


				if (mIsRecording) {
					return;
				}

				if (OnScrollListener.SCROLL_STATE_FLING == scrollState || OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState || OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
					vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);//禁止ListView跳转模式
					CommonUtils.closeSoftKeyBoard(vEditTextContent);//关闭软键盘
					disableViews();
				}
				PalmchatLogUtils.println("scrollState  " + scrollState);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Top  true");
				}
				PalmchatLogUtils.println("GroupChatActivity  visibleItemCount  " + visibleItemCount
								+ "firstVisibleItem  " + firstVisibleItem
								+ "totalItemCount  " + totalItemCount
								+ "  vListView.getLastVisiblePosition()  " + vListView.getLastVisiblePosition()
				);

				if (visibleItemCount + firstVisibleItem >= totalItemCount - 1) {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Bottom  true");
					isBottom = true;
				} else {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Bottom  false");
					isBottom = false;
				}
			}
		});
	
		mCancelVoice = (TextView)findViewById(R.id.cancel_voice_note);
		vTextViewRecordTime = (TextView) findViewById(R.id.text_show);
		vEditTextContent = (CutstomEditText) findViewById(R.id.chatting_message_edit);
		vEditTextContent.setOnClickListener(this);
		vEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					//输入框获取焦点时，关闭表情输入框
					closeEmotions();
				}
			}
		});

		edit_group_name = (RelativeLayout) findViewById(R.id.r_et);
		et_group_name = (EditText) findViewById(R.id.et_group_name);
		et_group_name.setOnClickListener(this);
		et_group_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					emojjView.getViewRoot().setVisibility(View.GONE);
				}
			}
		});
		et_group_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				Editable editable = et_group_name.getText();
				int len = editable.length();
				if (len > 0) {
					et_exit.setBackgroundResource(R.drawable.button_enter);
					isCanSave = true;
				} else {
					et_exit.setBackgroundResource(R.drawable.button_exit);
					isCanSave = false;
				}
			}

		});
		edit_group_name.setVisibility(View.GONE);
		et_exit = (Button) findViewById(R.id.et_exit);
		et_exit.setOnClickListener(this);
		//设置输入框最大字符串长度
		vEditTextContent.addTextChangedListener(new LimitTextWatcher(
				vEditTextContent, ReplaceConstant.MAX_SIZE, mainHandler,
				EDIT_TEXT_CHANGE));
		vEditTextContent.setMaxLength(ReplaceConstant.MAX_SIZE);
		vEditTextContent.setOnEditorActionListener(this);
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
		vImageViewVoice = findViewById(R.id.chatting_operate_one);
		vImageViewVoice.setOnClickListener(this);
		viewUnTalk = findViewById(R.id.un_talk);
		viewFrameToast = findViewById(R.id.view_frame);
		vButtonTalk = (Button) findViewById(R.id.talk_button);
		vButtonTalk.setOnTouchListener(new HoldTalkListener());
		vImageEmotion = (ImageView) findViewById(R.id.image_emotion);
		vImageEmotion.setOnClickListener(this);
		emojjView = new EmojjView(this);
		emojjView.select(EmojiParser.SUN);
		viewOptionsLayout = findViewById(R.id.chatting_options_layout);
		viewChattingVoiceLayout = findViewById(R.id.chatting_voice_layout);
		chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
		chatting_emoji_layout.addView(emojjView.getViewRoot());
		// chatting_emoji_layout.addView(viewChattingEmotion);
		emojjView.getViewRoot().setVisibility(View.GONE);
		//设置表情背景
		viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
		
//		pop msg hide some ui
		vButtonSend.setVisibility(View.VISIBLE);
		vImageViewVoice.setVisibility(View.GONE);

		vEditTextContent.setOnTouchListener(new View.OnTouchListener() {
			float actionx = 0, actiony = 0;

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
						if (Math.abs(actionx - event.getX()) > 10d || Math.abs(actiony - event.getY()) > 10d) {
							closeEmotions();
						}
						break;
				}
				return false;
			}
		});
	}


	/** 设置群组头像
	 * @param imageviews 群组头像ImageView数组
	 * @param infos
	 */
private void setGroupAvatar(ImageView[] imageviews, AfGrpProfileInfo infos) {

	//设置默认头像
	imageviews[0].setBackgroundResource(R.drawable.head_male2);
	imageviews[1].setBackgroundResource(R.drawable.head_male2);
	imageviews[2].setBackgroundResource(R.drawable.head_male2);
		
		ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
		imageViews.add(imageviews[0]);
		imageViews.add(imageviews[1]);
		imageViews.add(imageviews[2]);
		//从服务器下载三名群组成员头像做为群组头像
//		ImageLoader.getInstance().displayImage(imageViews, infos );
	ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
	}

	/**
	 * 设置声音模式
	 */
	private void setListenMode() {
		int listenMode = SharePreferenceService.getInstance(PopGroupMessageActivity.this).getListenMode();
		PalmchatLogUtils.println("GroupChatActivity  time setListenMode " + dateFormat.format(new Date()) + "  listenMode  " + listenMode);
		if(listenMode == AudioManager.MODE_IN_CALL){ //按着录音
			onItemClick(ChattingMenuDialog.ACTION_HANDSET_MODE);
		}else{
			onItemClick(ChattingMenuDialog.ACTION_SPEAKER_MODE);//录音模式
		}
	}

	/**
	 * 隐藏相应组件
	 */
	private void disableViews() {
		//隐藏录音计时控件
		vTextViewRecordTime.setVisibility(View.GONE);
		//隐藏表情
		emojjView.getViewRoot().setVisibility(View.GONE);
		viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
		//隐藏键盘按钮
		vButtonKeyboard.setVisibility(View.GONE);
		//显示表情按钮
		vImageEmotion.setVisibility(View.VISIBLE);
		if(viewChattingVoiceLayout.getVisibility() == View.VISIBLE){//隐藏语音布局
			viewOptionsLayout.setVisibility(View.VISIBLE);
			viewChattingVoiceLayout.setVisibility(View.GONE);
		}
	}


	/** 创建选项GridView
	 * @param number 列数
	 * @return
	 */
	private GridView createOptionsGridView(int number) {
		final GridView view = new GridView(this);
		view.setNumColumns(number);
//		view.setBackgroundResource(R.drawable.profile_photobg);
		view.setHorizontalSpacing(0);
		view.setVerticalSpacing(0);
		view.setSelector(android.R.color.transparent);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		view.setBackgroundResource(R.drawable.tab_bg);
		return view;
	}


	/**
	 * 如果当前显示录音界面，则隐藏
	 */
	private void changeMode() {
		if (viewUnTalk.getVisibility() != View.VISIBLE) {
			viewUnTalk.setVisibility(View.VISIBLE);
			vButtonTalk.setVisibility(View.GONE);
//			vImageViewVoice.setBackgroundResource(R.drawable.chatting_setmode_voice_btn_focused);
		}
	}

	private String groupId;
	private String roomName;
	
	private int gversion;

	@Override
	public void init() {
		adapterListView = new PopGroupMessageAdapter(context, listChats,vListView );
		adapterListView.setOnItemClick(this);
		optionsData.clear();
		setAdapter(null);
		if (vListView != null) {
			vListView.setAdapter(adapterListView);
			vListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub

					if (mIsRecording) {
						return;
					}

					final AfMessageInfo afMsg = adapterListView.getItem(position);
					//点击当前记录直接进入群组聊天界面
					toGroupChatting(afMsg.toAfId);

					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//把未读消息数设置为0
							mAfCorePalmchat.AfRecentMsgSetUnread(afMsg.toAfId, 0);
							//把缓存内的未读消息数清空
							MessagesUtils.setUnreadMsg(afMsg.toAfId, 0);
						}
					}).start();
				}
			});
			vListView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					//双击进入聊天
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						doubleClick();
					}
					return true;
				}
			});
		}
		//加载最近的聊天记录
		loadData();
		PalmchatLogUtils.println("--rrr initData");
		
		app.setPopMessageReceiver(this);
		if(!CommonUtils.isScreenLocked(PalmchatApp.getApplication().getApplicationContext())){ //主要解决的问题：若解锁成功后，又弹出消息窗，则关闭此消息窗
			PalmchatLogUtils.i("hj","PopGroup is not locked");
			clearData();
		}
	}


	/**
	 * 加载最近的聊天记录
	 */
	private void loadData() {
		listChats.clear();
		List<AfMessageInfo> listTemp = getRecentData();
		//绑定数据到listView
		bindData(listTemp);
	}


	/**
	 * 显示录音界面
	 */
	private void toShowVoice() {
		//关闭软键盘
		CommonUtils.closeSoftKeyBoard(vEditTextContent);
		if(CommonUtils.getSdcardSize()){ //判断SD卡剩余空间
			ToastManager.getInstance().show(context, R.string.memory_is_full);
			return;
		}
		if(viewChattingVoiceLayout.getVisibility() == View.VISIBLE){
			viewOptionsLayout.setVisibility(View.VISIBLE);
			viewChattingVoiceLayout.setVisibility(View.GONE);
		} else {
			viewOptionsLayout.setVisibility(View.GONE);
			viewChattingVoiceLayout.setVisibility(View.VISIBLE);
		}
		//关闭表情
		closeEmotions();
	}


	/** 获取输入框内容
	 * @return
	 */
	private String getEditTextContent() {
		return vEditTextContent.getText().toString();
	}

	/** 设置输入框内容
	 * @param text
	 */
	private void setEditTextContent(String text){
		vEditTextContent.setText(text);
	}


	/** 设置ListView数据适配器
	 * @param afMessageInfo
	 */
	private void setAdapter(AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		if (adapterListView == null) {
			adapterListView = new PopGroupMessageAdapter(context, listChats,vListView );

//			vListView.setOnScrollListener(new ImageOnScrollListener(vListView, listViewAddOn));
			vListView.setAdapter(adapterListView);
		} else {
			if(afMessageInfo != null){
				PalmchatLogUtils.println("Group  setAdapter  afMessageInfo.action "+afMessageInfo.action);
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			adapterListView.notifyDataSetChanged();
//			vListView.setSelection(adapterListView.getCount());
			if ((mIsNoticefy && 0 < adapterListView.getCount()) || isBottom) {
				vListView.setSelection(adapterListView.getCount());
				mIsNoticefy = false;
			}
		}
	}

	private AfGrpProfileInfo groupInfo;
	// private AfGrpItemInfo mGrpItemInfo;
	private String group_list;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
//		MobclickAgent.onPageStart("GroupChatActivity");
//	    MobclickAgent.onResume(this);

		getData();
		//取消消息通知
		CommonUtils.cancelNoticefacation(getApplicationContext());

		//获取上次未发出的聊天内容
		String msgContent = mAfCorePalmchat.AfDbGetMsgExtra(mFriendAfid);
		PalmchatLogUtils.println("mFriendAfid  "+mFriendAfid+"  group  msgContent  "+msgContent);
//		setEditTextContent(msgContent);
		CharSequence text = EmojiParser.getInstance(context).parse(msgContent);
		if(vEditTextContent != null){
			//设置上次未发出的聊天内容
			vEditTextContent.setText(text);
		}
	}

	/**
	 * 获取最新聊天记录
	 */
	private void getData() {
		if(isInResume){
			new Thread(new Runnable() {
				public void run() {
					AfMessageInfo afMessageInfo = adapterListView.getLastMsg();
					if(afMessageInfo != null){
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
	private static final long LONG_PRESS_TIME = 500;

	/**
	 * 此类主要实现录音的点击事件
	 */
	class HoldTalkListener implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.talk_button) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://按下操作
					//以下按下时的获取x,y坐标
					mDownX = event.getRawX();
					mDownY = event.getRawY();
					if(!CommonUtils.isHasSDCard()){
						ToastManager.getInstance().show(PopGroupMessageActivity.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
						return false;
					}
					
					sent = false;
					mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
					
					if (recordStart > 0) {//判断是否已录音
						//发送录音消息
						mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
						//停止定时器
						stopRecordingTimeTask();
						setMode(SharePreferenceService.getInstance(PopGroupMessageActivity.this).getListenMode());
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
					PalmchatLogUtils.println("GroupChatActivity  mVoiceName "+mVoiceName);
					startRecordingTimeTask();
					break;
				case MotionEvent.ACTION_MOVE://移动
//					 already stop recording
					if (recordStart <= 0) {
						break;
					}
					
					float offsetX1 = Math.abs(event.getRawX() - mDownX);
					float offsetY1 = Math.abs(event.getRawY() - mDownY);
					
					if(offsetX1 < ImageUtil.dip2px(context, 50)  && offsetY1 < ImageUtil.dip2px(context, 50)) {
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
					if (vTextViewRecordTime != null && vTextViewRecordTime.isShown() && !sent || (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime <= LONG_PRESS_TIME)) {
						if(CommonUtils.getSdcardSize()){
							ToastManager.getInstance().show(context, R.string.memory_is_full);
						}else{
							float offsetX = Math.abs(event.getRawX() - mDownX);
							float offsetY = Math.abs(event.getRawY() - mDownY);
							
							if(offsetX < ImageUtil.dip2px(context, 50) && offsetY < ImageUtil.dip2px(context, 50)) { //当手释放时，如果在此番外范围内，则直接发送语音
									PalmchatLogUtils.println("---- send voice----");
									if (recordStart > 0) {
									mainHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);//
							}
							}
						}
						vTextViewRecordTime.setVisibility(View.GONE);
						stopRecording();
						stopRecordingTimeTask();
					}
					setMode(SharePreferenceService.getInstance(PopGroupMessageActivity.this).getListenMode());
//					show cancelled text
					if(mCancelVoice.getVisibility() == View.VISIBLE) {
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

	/**
	 * 开始录音
	 */
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

	/**
	 * 停止录音
	 */
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
		vImageEmotion.setVisibility(View.VISIBLE);
		vTextViewRecordTime.setVisibility(View.GONE);
		emojjView.getViewRoot().setVisibility(View.GONE);
		viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
		vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		vListView.setSelection(adapterListView.getCount());
	}

	/** 开始录音
	 * @return 返回文件名
	 */
	private String startRecording() {
		String voiceName = null;
		
		if (RequestConstant.checkSDCard()) {
			// String dir =
			// mAfCorePalmchat.AfResGetDir(AfMessageInfo.MESSAGE_VOICE);
			voiceName = mAfCorePalmchat
					.AfResGenerateFileName(AfMessageInfo.MESSAGE_VOICE);
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
					setMode(SharePreferenceService.getInstance(PopGroupMessageActivity.this).getListenMode());
					vButtonTalk.setSelected(false);
					stopRecording();
					sent = true;
				
				}
			});
			try {
				mRecorder.prepare();
				mRecorder.start();
				recordStart = System.currentTimeMillis();
				
			} catch (Exception e) {
				PalmchatLogUtils.e("startRecording", "prepare() failed"+e.getMessage());
				e.printStackTrace();
//				vTextViewRecordTime.setVisibility(View.GONE);
				return null;
			}
			
			mRippleView.setVisibility(View.VISIBLE);
			mRippleView.setOriginRadius(vButtonTalk.getWidth() / 2);
			mRippleView.startRipple();
			
			vButtonBackKeyboard.setClickable(false);
			mVoiceImageEmotion.setClickable(false);
			mVoiceImageViewMore.setClickable(false);
			
			mIsRecording = true;
			
		} else {
//			ToastManager.getInstance().show(this, R.string.sdcard_unmounted);
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
				recordTime = Math.min((int) ((recordEnd - recordStart) / 1000),
						RequestConstant.RECORD_VOICE_TIME);
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
				
				mVoiceProgressBar.setVisibility(View.VISIBLE);
				mVoiceProgressBar2.setVisibility(View.GONE);
				mVoiceProgressBar.setProgress(0);
				mVoiceProgressBar2.setProgress(0);
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
/*		case KeyEvent.KEYCODE_MENU:
			if (mFriendAfid != null && !mFriendAfid.startsWith(DefaultValueConstant._R)) {
				int listenMode = SharePreferenceService.getInstance(PopGroupMessageActivity.this).getListenMode();
				addAction(listenMode, mFriendAfid);
				widget.show();
			}
			return true;*/
		case KeyEvent.KEYCODE_BACK:
			vButtonKeyboard.setVisibility(View.GONE);
			vImageEmotion.setVisibility(View.VISIBLE);
			if(emojjView.getViewRoot().getVisibility() != View.GONE){
				emojjView.getViewRoot().setVisibility(View.GONE);
				return false;
			}
			else{
				
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

	/** 把文本或语音消息保存到数据库并发送数据
	 * @param msgType 消息类型
	 * @param fileName 语音文件名
	 * @param fileSize 文件大小
	 * @param length
	 * @param handler
	 * @param handler_MsgType
	 * @param action
	 * @param afMessageInfo
	 * @param content
	 */
	private void insertAndSendMessageInfoForText(final int msgType,
			final String fileName, final int fileSize, final int length,
			final Handler handler, final int handler_MsgType, final int action,
			final AfMessageInfo afMessageInfo,final String content) {
		PalmchatLogUtils.println("GroupChatActivity  insertAndSendMessageInfoForText "+fileName+"  mVoiceName  "+mVoiceName);
		final AfMessageInfo messageInfo = new AfMessageInfo();//新增
		new Thread(new Runnable() {
			public void run() {
				switch (msgType) {
				case AfMessageInfo.MESSAGE_TEXT://文本消息
					if(action == MessagesUtils.ACTION_INSERT){
						messageInfo.client_time = System.currentTimeMillis();
						messageInfo.fromAfId = mFriendAfid;
//						messageInfo.toAfId = CacheManager.getInstance().getMyProfile().afId;
						messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_TEXT;
						messageInfo.msg = content;
						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						//把此消息保存到本地数据库
						messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);
					}
					break;
				case AfMessageInfo.MESSAGE_VOICE:
					if(action == MessagesUtils.ACTION_INSERT){
						AfAttachVoiceInfo afAttachVoiceInfo = new AfAttachVoiceInfo();
						afAttachVoiceInfo.file_name =  fileName;
						afAttachVoiceInfo.file_size = fileSize;
						afAttachVoiceInfo.voice_len = length;
						afAttachVoiceInfo._id = mAfCorePalmchat.AfDbAttachVoiceInsert(afAttachVoiceInfo);//把语音相关信息保存到数据库
						PalmchatLogUtils.println("GroupChatActivity  insertAndSendMessageInfoForText "+fileName+"  mVoiceName  "+mVoiceName +"  voice_length  "+length);
						
						messageInfo.client_time = System.currentTimeMillis();
						messageInfo.fromAfId = mFriendAfid;
//						messageInfo.toAfId = CacheManager.getInstance().getMyProfile().afId;
						messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_VOICE;
						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						messageInfo.attach = afAttachVoiceInfo;
						messageInfo.attach_id = afAttachVoiceInfo._id;
						messageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(messageInfo);//把语音信息保存到数据库
					}
					break;
				default:
					break;
				}
				PalmchatLogUtils.println("GroupChatActivity  insertAndSendMessageInfoForText  afMessageInfo  "+afMessageInfo);
				if(action == MessagesUtils.ACTION_INSERT){
					messageInfo.action = action;
					//根据条件发送消息
					handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
					//把消息保存到缓存中
					MessagesUtils.insertMsg(messageInfo, true, true);
				}
			}

		}).start();
		
	}

	/**
	 * @param id
	 * @param data
	 */
	public void setFace(int id, String data) {
		Drawable drawable;
		if (id != 0 && !CommonUtils.isDeleteIcon(id,vEditTextContent)) {
			drawable = getResources().getDrawable(id);
			drawable.setBounds(0, 0, CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
			ImageSpan span = new ImageSpan(drawable);
			SpannableString spannableString = new SpannableString(data);
			spannableString.setSpan(span, 0, data.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			Log.e("ttt->",
					spannableString.length() + "->" + vEditTextContent.length());
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
		case R.id.chatting_send_button://send button
			sendTextOrEmotion();
			break;
		case R.id.chatting_operate_one://change voice button
		case R.id.btn_backkeyboard:
			toShowVoice();
			break;
			case R.id.btn_keyboard:
				vButtonKeyboard.setVisibility(View.GONE);
				vImageEmotion.setVisibility(View.VISIBLE);
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
			viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
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
			
		case R.id.image_emotion2://录音按钮
			toShowVoice();
			CommonUtils.closeSoftKeyBoard(vEditTextContent);
			emojjView.getViewRoot().setVisibility(View.VISIBLE);
			viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
			vButtonKeyboard.setVisibility(View.VISIBLE);
			vImageEmotion.setVisibility(View.GONE);
//			emojjView.getViewRoot().setFocusableInTouchMode(true);
			changeMode();
			vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			vListView.setSelection(adapterListView.getCount());
			break;
			
		case R.id.chatting_operate_two2:
			toShowVoice();
			CommonUtils.closeSoftKeyBoard(vEditTextContent);
			emojjView.getViewRoot().setVisibility(View.GONE);
			viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
			vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			vListView.setSelection(adapterListView.getCount());
			
			break;
			
		case R.id.chatting_message_edit://onclick on eidttext
			closeEmotions();
			break;
		case R.id.pophead_exit:
		
			back();
			break;
	

		case R.id.et_exit:
			if(isCanSave){
				roomName = et_group_name.getText().toString();
				mPopHeadName.setText(roomName);
				int req_flag = Consts.REQ_GRP_MODIFY;
				
				showProgressDialog(R.string.please_wait);
				CallGrpOpr(null, roomName, groupID, req_flag,true);
			}else{
				edit_group_name.setVisibility(View.GONE);
			}
	
			break;
		case R.id.et_group_name:
			emojjView.getViewRoot().setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/** 修改群组名称
	 * @param member
	 * @param groupName
	 * @param groupId
	 * @param req_flag
	 * @param isShowToast
	 */
	private void CallGrpOpr(String member, String groupName, String groupId,
			int req_flag,boolean isShowToast) {
		List<AfGrpProfileItemInfo> lists = groupInfo.members;
		String afid[] = new String[lists.size()];
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < lists.size(); i++) {
			afid[i] = lists.get(i).afid;
			buffer = buffer.append(afid[i]).append(",");
			// String members = "a1902418,a1568854,a1675034,a2452792";
		}
		String members = buffer.substring(0, buffer.lastIndexOf(","));
		mAfCorePalmchat.AfHttpGrpOpr(members, groupName, groupId, req_flag,
				isShowToast, this);
	}

	/**
	 * 退出当前窗体
	 */
	private void back() {
		//隐藏键盘
		InputMethodManager imm = (InputMethodManager) PalmchatApp.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(vEditTextContent.getWindowToken(), 0);
		clearData();
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startActivity(startMain);
		
	}

	/**
	 * 关闭此窗体前清除一些数据
	 */
	private void clearData() {
		if (VoiceManager.getInstance().isPlaying()) { //暂停录音
			VoiceManager.getInstance().pause();
		}
		
		finish();
		//页面切换动画
		PopGroupMessageActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		//清空缓存
		CacheManager.getInstance().getPopMessageManager().clearCount();
		//把接收消息接口设置为null
		app.setPopMessageReceiver(null);

		CacheManager.getInstance().getPopMessageManager().setmFirstPopMsg(null);
		
		CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().clear();
		CacheManager.getInstance().getPopMessageManager().getmExitPopMsg().put(mFriendAfid, true);
	}


	/**
	 * 发送文本表情，由于是锁屏弹出窗口，所以发送之后即关闭窗口
	 */
	private void sendTextOrEmotion() {
		String content = getEditTextContent();
		if(content.length() > 0) {
			vEditTextContent.setText("");
			insertAndSendMessageInfoForText(AfMessageInfo.MESSAGE_TEXT, null, 0, 0, mainHandler, MessagesUtils.MSG_TEXT, MessagesUtils.ACTION_INSERT, null,content);
			back();//关闭窗口
		}
	}

	/** 发送消息
	 * @param content 消息内容
	 * @param msgInfo 消息对象
	 */
	private void send(String content, AfMessageInfo msgInfo) {
		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.G_NUM);
		
//		MobclickAgent.onEvent(context, ReadyConfigXML.S_M_TEXT);
//		MobclickAgent.onEvent(context, ReadyConfigXML.G_NUM);

		//发送消息
		mAfCorePalmchat.AfHttpSendMsg(msgInfo.fromAfId, System.currentTimeMillis(), content, Consts.MSG_CMMD_NORMAL, msgInfo._id, this);
	}


	/** 更新消息状态(比如更新listView列表数据、以及更新数据库的状态)
	 * @param msgId
	 * @param status
	 * @param fid
	 */
	public void updateStatus(final int msgId, final int status, final String fid) {
		int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
		for (AfMessageInfo af : adapterListView.getLists()) {
			PalmchatLogUtils.println("GroupChatActivity  af.id  "+af._id);
		}
		PalmchatLogUtils.println("GroupChatActivity  index  " + index + "  msgId  " + msgId + "  status  " + status);
		if (index != -1 && index < adapterListView.getCount()) {
			AfMessageInfo afMessageInfo = adapterListView.getItem(index);
			afMessageInfo.status = status;
			afMessageInfo.fid = fid;
			PalmchatLogUtils.println("GroupChatActivity  afMessageInfo.msgId  "+afMessageInfo._id+"  afMessageInfo.status  "+afMessageInfo.status);
			adapterListView.notifyDataSetChanged();
		}
		new StatusThead(msgId, status, fid).start();
		
	}


	/**
	 * 更新数据库中的消息状态，由于操作数据库需要时间，所以在另外一个线程中操作
	 */
	public class StatusThead extends Thread {
		private int msgId;
		private int status;
		private String fid;
		public StatusThead(int msgId, int status, String fid){
			this.msgId = msgId;
			this.status = status;
			this.fid = fid;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
		
			int _id = DefaultValueConstant.LENGTH_0;
			
			if(fid != DefaultValueConstant.MSG_INVALID_FID) {
				_id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS_AND_FID);
				
			} else {
			    _id = mAfCorePalmchat.AfDbMsgSetStatusOrFid(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, msgId, status, fid, Consts.AFMOBI_PARAM_STATUS);
			}
			
//			update recent chats msg status 
			AfMessageInfo msg = mAfCorePalmchat.AfDbGrpMsgGet(msgId);
			if(msg == null) {
				
				PalmchatLogUtils.println("GroupChatActivity AfDbGrpMsgGet msg:"+msg);
				return;
			}
			
				PalmchatLogUtils.println("---www : grp StatusThead isLast msg "+ msgId+ " msg status: "+status);
				MessagesUtils.setRecentMsgStatus(msg, status);

				mainHandler.obtainMessage(MessagesUtils.MSG_SET_STATUS).sendToTarget();
			
			PalmchatLogUtils.println("update status msg_id "+ _id);
			
//			 _id = mAfCorePalmchat.AfDbGrpMsgSetStatus(msgId, status);
		}
	}

	/** 通过广播更新图片状态
	 * @param msgId
	 * @param status
	 * @param fid
	 * @param progress
	 */
	public void updateImageStautsByBroadcast(final int msgId, int status, String fid, int progress){
		int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
		if(msgId != -1 && index == -1){
			AfMessageInfo msg = CacheManager.getInstance().getAfMessageInfo();
			if(msg != null && mFriendAfid.equals(msg.fromAfId)){
				if(msg != null && msgId == msg._id){
					PalmchatLogUtils.println("GroupChatActivity  updateImageStautsByBroadcast  ");
					CommonUtils.getRealList(adapterListView.getLists(), msg);
					CacheManager.getInstance().setAfMessageInfo(null);
					
					updateImageStautsByBroadcast(msgId, status, fid, progress);
				}else{
					new Thread(new Runnable() {
						public void run() {
							PalmchatLogUtils.println("GroupChatActivity  Thread  updateImageStautsByBroadcast  ");
							AfMessageInfo msg = mAfCorePalmchat.AfDbMsgGet(msgId);
							if(msg != null){
								CommonUtils.getRealList(adapterListView.getLists(), msg);
								mainHandler.obtainMessage(MessagesUtils.MSG_CAMERA_NOTIFY,msg).sendToTarget();
							}
						}
					}).start();
				}
			}
		}else if (index != -1 && index < adapterListView.getCount()) {
			AfMessageInfo afMessageInfo = adapterListView.getItem(index);
			if(afMessageInfo != null && mFriendAfid.equals(afMessageInfo.fromAfId)){
				PalmchatLogUtils.println("GroupChatActivity  updateImageStatusByBroadcast  afMessageInfo  "+afMessageInfo);
				afMessageInfo.status = status;
				afMessageInfo.fid = fid;
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				afAttachImageInfo.progress = progress;
				adapterListView.notifyDataSetChanged();
			}
		}
	}


	/** 设置普通消息发送失败
	 * @param flag
	 * @param code
	 * @param user_data
	 */
	private void sendMsgFailure(int flag, int code, Object user_data) {
		int msg_id;
		msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
		if(msg_id > DefaultValueConstant.LENGTH_0){
			//发送广播
			sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID);
//					updateStatus(msg_id , AfMessageInfo.MESSAGE_UNSENT);
		}else{
			//updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
			ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
			PalmchatLogUtils.println("GroupChatActivity  else Consts.REQ_MSG_SEND else code " + code + "  flag  " + flag);
		}
	}


	/** 发送文字消息发送失败
	 * @param msg_id 消息ID
	 * @param status 消息状态
	 * @param fid
	 */
	void sendBroadcastForTextOrVoice(int msg_id,int status, String fid){
		Intent intent = new Intent(MessagesUtils.GROUP_TEXT_MESSAGE);
		intent.putExtra(JsonConstant.KEY_MESSAGE_ID, msg_id);
		intent.putExtra(JsonConstant.KEY_STATUS, status);
		intent.putExtra(JsonConstant.KEY_MSG_FID, fid);
		sendBroadcast(intent);
		if(!CommonUtils.isGroupChatActivity(this)){
			updateStatus(msg_id, status, fid);
		}
	}

	void sendBroadcastForImage(int msg_id,int status, String fid, int progress){
		Intent intent = new Intent(MessagesUtils.GROUP_IMAGE_MESSAGE);
		intent.putExtra(JsonConstant.KEY_MESSAGE_ID,msg_id);
		intent.putExtra(JsonConstant.KEY_STATUS, status);
		intent.putExtra(JsonConstant.KEY_PROGRESS, progress);
		intent.putExtra(JsonConstant.KEY_MSG_FID, fid);
		PalmchatLogUtils.println("GroupChatActivity  msg_id  "+msg_id+"  status  "+status+"  progress  "+progress);
		sendBroadcast(intent);
		if(!CommonUtils.isGroupChatActivity(this)){
			updateImageStautsByBroadcast(msg_id, status, fid, progress);
		}
	}


	/**send image error*/
	private void sendImageFailure(int code, Object result, Object user_data) {
		final AfImageReqInfo afImageReqInfo = (AfImageReqInfo) result;
		AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
		if(Consts.REQ_CODE_MEDIA_SESSTION_TIMEOUT == code){
			AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
			AfImageReqInfo imageReqInfo = afAttachImageInfo.upload_info;
			imageReqInfo.server_dir = null;
			imageReqInfo.server_name = null;
			afAttachImageInfo.upload_info = imageReqInfo;
			afAttachImageInfo.progress = 0;
			sendBroadcastForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
			mAfCorePalmchat.AfHttpSendImage(afAttachImageInfo.upload_info, afMessageInfo, this, this);
		}else if(afImageReqInfo != null && afMessageInfo != null){
			AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
			if(afAttachImageInfo != null){
				AfImageReqInfo imageReqInfo = afAttachImageInfo.upload_info;
				afImageReqInfo._id = imageReqInfo._id;
				afAttachImageInfo.upload_info = afImageReqInfo;

				sendBroadcastForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
//						updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, afAttachImageInfo);
				new Thread(new Runnable() {
					public void run() {
						PalmchatLogUtils.println("GroupChatActivity  afImageReqInfo._id  "+afImageReqInfo._id);
						mAfCorePalmchat.AfDbImageReqUPdate(afImageReqInfo);//mAfCorePalmchat.AfDbImageReqUPdate(afAttachImageInfo.upload_info);
					}
				}).start();
				new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
			}else{
				PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  afAttachImageInfo  "+afAttachImageInfo);
			}
		}else{
			AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
			sendBroadcastForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID, afAttachImageInfo.progress);
			new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_UNSENT, DefaultValueConstant.MSG_INVALID_FID).start();
		}
	}


	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("GroupChatActivity  group  code " + code + "  flag  " + flag + "  result  " + result + "  user_data  " + user_data);
		int msg_id;
		AfGrpProfileInfo mProgfileInfo = null;
		if (Consts.REQ_CODE_SUCCESS == code) {
			ableClick();
			switch (flag) {
			case Consts.REQ_MSG_SEND:
				msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
				PalmchatLogUtils.println("GroupChatActivity  onresult msg_id  "+msg_id);
				if(msg_id > DefaultValueConstant.LENGTH_0){
					String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
					sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, fid);
					PalmchatLogUtils.println("grp result fid:"+fid);
//					updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT);
				}else{
					//updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("GroupChatActivity  case Consts.REQ_MSG_SEND code "+code+"  flag  "+flag);
				}
				break;
			case Consts.REQ_VOICE_SEND:
				msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
				if(msg_id > DefaultValueConstant.LENGTH_0){
					sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, DefaultValueConstant.MSG_INVALID_FID);
//					updateStatus(msg_id, AfMessageInfo.MESSAGE_SENT);
				}else{
					//updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("GroupChatActivity  case Consts.REQ_VOICE_SEND code "+code+"  flag  "+flag);
				}
				break;
			case Consts.REQ_MEDIA_INIT://media init
			{
				
				break;
			}
			case Consts.REQ_MEDIA_UPLOAD://media upload
			{
				AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
				if(afMessageInfo != null){
					final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
					if(afAttachImageInfo != null){
						String fid = (result != null && result instanceof String) ? result.toString() : DefaultValueConstant.MSG_INVALID_FID;
						sendBroadcastForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid, afAttachImageInfo.progress);
//						updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, afAttachImageInfo);
						new StatusThead(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT, fid).start();//mAfCorePalmchat.AfDbMsgSetStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT);
						new Thread(new Runnable() {
							public void run() {
								PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  success  "+afAttachImageInfo.upload_id);
								mAfCorePalmchat.AfDbImageReqRmove(afAttachImageInfo.upload_id);
							}
						}).start();
					}else{
						PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  afAttachImageInfo  "+afAttachImageInfo);
					}
				}else{
					PalmchatLogUtils.println("GroupChatActivity  Consts.REQ_MEDIA_UPLOAD  afMessageInfo  "+afMessageInfo);
				}
				break;
			}
			case Consts.REQ_GET_INFO://get profile(info) success
			{
				AfProfileInfo afProfileInfo = (AfProfileInfo)result;
				AfMessageInfo afMessageInfo = (AfMessageInfo)user_data;
				if(afProfileInfo != null && afMessageInfo != null){
					AfFriendInfo afFriendInfo = AfProfileInfo.friendToProfile(afProfileInfo);
					afMessageInfo.attach = afFriendInfo;
//					listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats,afMessageInfo);
					showVibrate();
					setAdapter(null);
				}else{
					//get profile info failure
					PalmchatLogUtils.println("GroupChatActivity AfOnResult get profile info failure");
				}
				break;
			}
			

//			forward msg
			case Consts.REQ_MSG_FORWARD:
			{
				PalmchatLogUtils.println("REQ_MSG_FORWARD success grp");
//				update send msg ui
				msg_id = user_data != null ? ((AfMessageInfo)user_data)._id : DefaultValueConstant.LENGTH_0;
				PalmchatLogUtils.println("onresult msg_id  "+msg_id);
				if(msg_id > DefaultValueConstant.LENGTH_0){
					sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT, ((AfMessageInfo)user_data).fid);
				}else{
					//updateStatus(msgInfo._id, AfMessageInfo.MESSAGE_UNSENT);
					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code "+code+"  flag  "+flag);
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
					if(afGrpProfileInfo != null){
						mProgfileInfo.tips = afGrpProfileInfo.tips;
					}
					CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).update(mProgfileInfo, true, true);
					//mAfCorePalmchat.AfDbGrpProfileUpdateName(groupId,roomName);
				}
				boolean isShowToast = (Boolean) user_data;
				if (!StringUtil.isEmpty(groupId, true)) {
					if(isShowToast){
						mainHandler.sendMessage(mainHandler.obtainMessage(
								EditGroupActivity.GROUP_NAME_CHANGED, groupId));
					}
				} else {
					if(isShowToast){
						mainHandler.sendMessage(mainHandler.obtainMessage(
								EditGroupActivity.GROUP_SET_NAME_FAIL, groupId));
					}
				}
				new Thread(new Runnable() {
					public void run() {
						AfGrpNotifyMsg afGrpNotifyMsg;
						final int modify_type = AfGrpNotifyMsg.MODIFY_TYPE_NAME;
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(roomName, groupId, 
								CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), null, 
								null, null, gversion, AfMessageInfo.MESSAGE_GRP_MODIFY , modify_type);
						
						AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg,PalmchatApp.getApplication());
						afMessageInfo.client_time = System.currentTimeMillis();
						int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
						afMessageInfo._id = msg_id;
						MessagesUtils.insertMsg(afMessageInfo, true, true);
						mainHandler.obtainMessage(NOTICEFY,afMessageInfo).sendToTarget();
						
					}
				}).start();
				break;
			default:
				break;
			}
		}else{
//			dismissProgressDialog();
			switch (flag) {
			case Consts.REQ_MSG_SEND:
				sendMsgFailure(flag, code, user_data);
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
			
			case Consts.REQ_GRP_MODIFY:
			{
				dismissProgressDialog();
				break;
			}
			default:
				break;
			}
			if(Consts.REQ_CODE_YOU_HAVE_BEEN_REMOVED == code){
			
				new Thread(new Runnable() {
					public void run() {
						AfGrpNotifyMsg afGrpNotifyMsg;
						final int modify_type = AfGrpNotifyMsg.BEEN_REMOVED;
						afGrpNotifyMsg = AfGrpNotifyMsg.getAfGrpNotifyMsg(roomName, groupId, 
								CacheManager.getInstance().getMyProfile().afId, getString(R.string.group_you), null, 
								null, null, gversion, AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED , modify_type);
						
						AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfoForYou(afGrpNotifyMsg,PalmchatApp.getApplication());
						afMessageInfo.client_time = System.currentTimeMillis();
						int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
						afMessageInfo._id = msg_id;
						MessagesUtils.insertMsg(afMessageInfo, true, true);
						mAfCorePalmchat.AfDbGrpProfileSetStatus(groupID, Consts.AFMOBI_GRP_STATUS_EXIT);
						mainHandler.obtainMessage(NOTICEFY,afMessageInfo).sendToTarget();
					}
				}).start();
//				ToastManager.getInstance().show(context, context.getString(R.string.you_have_been_removed));
			}else if(Consts.REQ_CODE_GROUNP_NOT_EXISTS == code){
				
				ToastManager.getInstance().show(context, context.getString(R.string.group_exist));
			}else if(Consts.REQ_CODE_MEDIA_SESSTION_TIMEOUT != code){
				 if(CommonUtils.isGroupChatActivity(this)){
						Consts.getInstance().showToast(context, code, flag,http_code);
				 }
			}
		}
	}

	
	private void ableClick() {
		
		new Thread(new Runnable() {
			public void run() {
				AfGrpProfileInfo  afGrpProfileInfo = CacheManager.getInstance().searchGrpProfileInfo(groupID);
				if(afGrpProfileInfo != null){
					afGrpProfileInfo.status = Consts.AFMOBI_GRP_STATUS_ACTIVE;
					CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(afGrpProfileInfo, true, true);
				}
			}
		}).start();
	}
	

	@Override
	public void AfOnProgress(int httpHandle, int flag, int progress,
			Object user_data) {
		PalmchatLogUtils.println("GroupChatActivity  ywp: AfhttpHandleOnProgress httpHandle =" + httpHandle +  " flag=" + flag + " progress=" + progress + " user_data = " + user_data);
		switch (flag) {
		case Consts.REQ_MEDIA_UPLOAD:
			final AfMessageInfo afMessageInfo = (AfMessageInfo)user_data;
			if(afMessageInfo != null){
				final AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				afAttachImageInfo.progress = progress;
				if(progress == 100){
					//updateImageStatus(afMessageInfo._id, AfMessageInfo.MESSAGE_SENT,afAttachImageInfo);
					PalmchatLogUtils.println("GroupChatActivity  100 progress  "+progress);
				}else{
					PalmchatLogUtils.println("GroupChatActivity  progress  " + progress);
					sendBroadcastForImage(afMessageInfo._id, AfMessageInfo.MESSAGE_SENTING, afMessageInfo.fid, afAttachImageInfo.progress);
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
		PalmchatLogUtils.println("GroupChatActivity  onActivityResult--->>" + requestCode+"  data  "+data);
		emojjView.getViewRoot().setVisibility(View.GONE);
		viewOptionsLayout.setBackgroundResource(R.drawable.btn_bg_profile);
		
		PalmchatLogUtils.e(TAG, "GroupChatActivity----data:" + data);
		PalmchatLogUtils.e(TAG, "GroupChatActivity----requestCode:" + requestCode);
		PalmchatLogUtils.e(TAG, "GroupChatActivity----resultCode:" + resultCode);
		ArrayList<AfMessageInfo> list = CacheManager.getInstance().getAfMessageInfoList();
		if(null != list && 0 < list.size()){
			for (int i = 0; i < list.size(); i++) {
    			CommonUtils.getRealList(listChats,list.get(i));
			}
			CacheManager.getInstance().getAfMessageInfoList().clear();
			setAdapter(null);
		}
		if (requestCode == TITLE) {
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				boolean isQuit = bundle.getBoolean(JsonConstant.KEY_QUIT_GROUP);
				if(isQuit){//exit group
					clearData();
					return;
				}
				String roomName = bundle.getString("ROOMNAME");
				boolean clearGroupMsgSuccess = bundle.getBoolean("clearGroupMsgSuccess");
				PalmchatLogUtils.e(TAG, "GroupChatActivity----clearGroupMsgSuccess:" + clearGroupMsgSuccess);
				mPopHeadName.setText(roomName);
				if(clearGroupMsgSuccess){
					listChats.clear();
					setAdapter(null);
				}
			}
		}
		if (requestCode == CAMERA) {
			PalmchatLogUtils.println("GroupChatActivity  rotation=="
					+ getWindowManager().getDefaultDisplay().getRotation());
			try {
				if (cameraFilename == null) {
					cameraFilename = SharePreferenceService.getInstance(this).getFilename();
					f = new File(RequestConstant.CAMERA_CACHE, cameraFilename);
				} else {
					SharePreferenceService.getInstance(this).clearFilename();
				}
				cameraFilename = f.getAbsolutePath();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
				if (f != null && cameraFilename != null) {
					
					String tempStr = smallImage(f,MessagesUtils.CAMERA);
					if(tempStr == null || TextUtils.isEmpty(tempStr)){
						return;
					}
					cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
					cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
					PalmchatLogUtils.e(TAG, "GroupChatActivity----cameraFilename:" + cameraFilename);
					if(CommonUtils.isEmpty(cameraFilename)){
						mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
						//	mainHandler.obtainMessage(MessagesUtils.MSG_ERROR_TIP, null).sendToTarget();
						//ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
						return;
					}
					PalmchatLogUtils.println("GroupChatActivity  cameraFilename  "+cameraFilename);
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
				if(DefaultValueConstant.FILEMANAGER.equals(originalUri.getScheme())){
					path = originalUri.getEncodedPath();
				}else{
					cursor = getContentResolver().query(originalUri, null,null, null, null);
					if(cursor == null || !cursor.moveToFirst()){
//					ToastManager.getInstance().show(context, );
						PalmchatLogUtils.println("GroupChatActivity  onActivityResult  cursor  "+cursor+"  cursor.moveToFirst()  false");
						return;
					}
					String authority = originalUri.getAuthority();
					
					if(!CommonUtils.isEmpty(authority) && authority.contains(DefaultValueConstant.FILEMANAGER)){
						path = cursor.getString(0);
					}else{
						path = cursor.getString(1);
					}
				}
					PalmchatLogUtils.e("path=", "GroupChatActivity  "+path); // 这个就是我们想要的原图的路径
					if(cursor != null){
						cursor.close();
					}
					f = new File(path);
					File f2 = FileUtils.copyToImg(f.getAbsolutePath());
					if (f2 != null) {
						f = f2;
					}
					cameraFilename = f2.getAbsolutePath();//f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
					smallImage(f,MessagesUtils.PICTURE);
					cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
					cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
					if(CommonUtils.isEmpty(cameraFilename)){
						mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
						//mainHandler.obtainMessage(MessagesUtils.MSG_ERROR_TIP, null).sendToTarget();
						return;
					}
					PalmchatLogUtils.e("cameraFilename=", "GroupChatActivity  "+cameraFilename);
					mainHandler.obtainMessage(MessagesUtils.MSG_PICTURE, cameraFilename).sendToTarget();
			}

		} else if (resultCode == 10) {

			if(data == null){
				return;
			}
			Bundle bundle = data.getExtras();
			if(bundle != null){
//				String tt = bundle.getString(Chatting.TT);
//				if(RECOMMEND.equals(tt)){
					AfFriendInfo afFriendInfo = (AfFriendInfo) bundle.getSerializable(JsonConstant.KEY_FRIEND);
					PalmchatLogUtils.println("GroupChatActivity  afFriendInfo  "+afFriendInfo);
					mainHandler.obtainMessage(MessagesUtils.MSG_CARD, afFriendInfo).sendToTarget();
//				}
			}
		} else if (resultCode == 11) {

			if(data == null)return;
			Bundle bundle = data.getExtras();
			if(bundle != null){
				int msg_id = bundle.getInt(JsonConstant.KEY_ID, -1);
				boolean flag  = bundle.getBoolean(JsonConstant.KEY_FLAG ,false);
				String url = bundle.getString(JsonConstant.KEY_URL);
				int large_file_size = bundle.getInt(JsonConstant.KEY_FILESIZE, -1);
				String large_file_name = bundle.getString(JsonConstant.KEY_FILENAME);
				String small_file_name = bundle.getString(JsonConstant.KEY_LOCAL_IMG_PATH);
				int index = ByteUtils.indexOf(adapterListView.getLists(), msg_id);
//				PalmchatLogUtils.i(TAG, "找到第几�?-> " + index + "->" + talkList.getSelectedItemPosition(),true);
				if (index != -1 && index < adapterListView.getCount()) {
					AfMessageInfo afMessageInfo = adapterListView.getItem(index);
					PalmchatLogUtils.println("GroupChatActivity  updateImageStatus  afMessageInfo  "+afMessageInfo);
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
						
						String tempStr = smallImage(f,MessagesUtils.CAMERA);
						if(tempStr == null || TextUtils.isEmpty(tempStr)){
							return;
						}
						cameraFilename = RequestConstant.IMAGE_CACHE + mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
						cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
						PalmchatLogUtils.e(TAG, "GroupChatActivity----cameraFilename:" + cameraFilename);
						if(CommonUtils.isEmpty(cameraFilename)){
							mainHandler.sendEmptyMessage(MessagesUtils.FAIL_TO_LOAD_PICTURE);
							//	mainHandler.obtainMessage(MessagesUtils.MSG_ERROR_TIP, null).sendToTarget();
							//ToastManager.getInstance().show(context, R.string.fail_to_load_picture);
							return;
						}
						PalmchatLogUtils.println("GroupChatActivity  cameraFilename  "+cameraFilename);
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
					Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
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
		String smallFilename = ClippingPicture.saveTalkBitmap(bitmap,
				mAfCorePalmchat);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return smallFilename;
	}

	private void initMedia(final String largeFileName , final int largeFileSize , final String smallFilename,final int smallFileSize) {
		new Thread(new Runnable() {
			public void run() {
				
				AfMessageInfo afMessageInfo = getMessageInfoForImage(largeFileName,largeFileSize,smallFilename,smallFileSize);
//				modify by wk
				CacheManager.getInstance().addmImgSendList(afMessageInfo);
                //把消息保存到缓存中
				MessagesUtils.insertMsg(afMessageInfo, true, true);
			}  
		}).start();
		
	}
	

	private AfMessageInfo getMessageInfoForImage(String largeFileName,int largeFileSize,String smallFileName,int smallFizeSize) {
		// TODO Auto-generated method stub RequestConstant.IMAGE_CACHE;
		long begin = System.currentTimeMillis();
		PalmchatLogUtils.println("GroupChatActivity  getMessageInfoForImage begin "+begin);
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
		PalmchatLogUtils.println("GroupChatActivity  param._id  "+param._id);
		
		afAttachImageInfo._id = mAfCorePalmchat.AfDbAttachImageInsert(afAttachImageInfo);
		
		
		AfMessageInfo afMessageInfo = new AfMessageInfo();
		afMessageInfo.attach = afAttachImageInfo;
		afMessageInfo.attach_id = afAttachImageInfo._id;
		afMessageInfo.client_time = System.currentTimeMillis();
		afMessageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_IMAGE;
		afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
		afMessageInfo.fromAfId = mFriendAfid;
		afMessageInfo.attach = afAttachImageInfo;
		
		afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
		//afAttachImageInfo.upload_info = new AfImageReqInfo();
		PalmchatLogUtils.println("GroupChatActivity  getMessageInfoForImage end "+(System.currentTimeMillis()-begin));
		return afMessageInfo;
	}

	private long lastSoundTime;
	
	@Override
	public void handlePopMessageFromServer(final AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("--rrr PopGroupChatActvity handleMessageFromServer id:"+afMessageInfo.getKey()+"  msg:"+afMessageInfo.msg);
		
		if(!mWakeLock.isHeld()) {
			mWakeLock.acquire();
		}
		mWakeLock.release();
		
		setVoiceQueue(afMessageInfo);
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		if(mFriendAfid.equals(afMessageInfo.toAfId)){
			if(msgType == AfMessageInfo.MESSAGE_FRIEND_REQ || msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS){
//				friendRequest(afMessageInfo);
			}else{
				int type = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
				switch (type) {
				case AfMessageInfo.MESSAGE_GRP_CREATE:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_CREATE "+afMessageInfo.toAfId);
					break;
				case AfMessageInfo.MESSAGE_GRP_ADD_MEMBER:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_ADD_MEMBER "+afMessageInfo.toAfId);
//					listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats,afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_FRIEND_REQ:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_FRIEND_REQ "+afMessageInfo.toAfId);
					break;
				case AfMessageInfo.MESSAGE_GRP_MODIFY:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_MODIFY "+afMessageInfo.toAfId);
					mPopHeadName.setText(afMessageInfo.name);
//					listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats,afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER:
				case AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_REMOVE_MEMBER OR MESSAGE_GRP_HAVE_BEEN_REMOVED"+afMessageInfo.toAfId);
					CommonUtils.getRealList(listChats,afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_DROP:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_DROP "+afMessageInfo.toAfId);
//					listChats.add(afMessageInfo);
					CommonUtils.getRealList(listChats,afMessageInfo);
					setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_GRP_DESTROY:
					PalmchatLogUtils.println("GroupChatActivity  MESSAGE_GRP_DESTROY "+afMessageInfo.toAfId);
					CommonUtils.getRealList(listChats,afMessageInfo);
					setAdapter(null);
					break;
				default :
					PalmchatLogUtils.println("handleMessageFromServer  begin  System.currentTimeMillis() - lastSoundTime  "+(System.currentTimeMillis() - lastSoundTime));
					if(PopGroupMessageActivity.class.getName().equals(CommonUtils.getCurrentActivity(this))) {
					new Thread(new Runnable() {
						public void run() {
							mAfCorePalmchat.AfRecentMsgSetUnread(afMessageInfo.getKey(), 0);
							afMessageInfo.unReadNum = 0;
							int status = MessagesUtils.insertMsg(afMessageInfo, false, true);
							PalmchatLogUtils.println("handleMessage  status  "+status);
						}
					}).start();
					
					}
					
					if(msgType == AfMessageInfo.MESSAGE_CARD){
						getProfileInfo(afMessageInfo);
					}else{
						CommonUtils.getRealList(listChats,afMessageInfo);
//						showVibrate();
						setAdapter(null);
					}
					break;
				}
			}
			
			PalmchatLogUtils.println("--www111 :group  "+ this + " list size "+ listChats.size() + "  msg " + afMessageInfo.msg);
			 
		} else {
			
			PalmchatLogUtils.println("--rrr group handleMessage mFriendAfid  != afMessageInfo.toAfId mFriendAfid:" + mFriendAfid + " afMsgId:" +
					afMessageInfo.getKey());
			
		}
	
	}

	/**
	 * 来消息时，使手机震动
	 */
	private void showVibrate() {
		if(app.getSettingMode().isVibratio()){
			if((System.currentTimeMillis() - lastSoundTime) > 4000 ||  lastSoundTime == 0) {
				TipHelper.vibrate(context, 200L);
			}
			lastSoundTime = System.currentTimeMillis();
			PalmchatLogUtils.println("GroupChatActivity  handleMessageFromServer  end  lastSoundTime  "+lastSoundTime);
		}
	}

	/** 获取好友profile
	 * @param afMessageInfo
	 */
	public void getProfileInfo(AfMessageInfo afMessageInfo) {
		mAfCorePalmchat.AfHttpGetInfo(new String[]{afMessageInfo.msg},
				Consts.REQ_GET_INFO, null, afMessageInfo, this);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEND:
			sendTextOrEmotion();
			break;
		case EditorInfo.IME_NULL:
			if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
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


	/** 发送默认表情
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
				afMessageInfo.fromAfId = mFriendAfid;
//				afMessageInfo.toAfId = mFriendAfid;
				afMessageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_GRP | AfMessageInfo.MESSAGE_EMOTIONS;
				afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
				
				afMessageInfo._id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
				mainHandler.obtainMessage(MessagesUtils.MSG_EMOTION,afMessageInfo).sendToTarget();
				//把消息保存到缓存中
				MessagesUtils.insertMsg(afMessageInfo, true, true);
			}
		}).start();
	}

	/** 设置录音模式
	 * @param mode
	 */
	public void setMode(int mode) {
		if (mode == AudioManager.MODE_IN_CALL) { //听筒模式
			AudioManager audio = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			audio.setSpeakerphoneOn(false);
			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
			audio.setMode(AudioManager.MODE_IN_CALL);
		} else { //扬声器模式
			AudioManager audioManager = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setMode(AudioManager.MODE_NORMAL);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
		}
	}

	@Override
	protected void onStop() {
		if (VoiceManager.getInstance().isPlaying()) {
			VoiceManager.getInstance().pause();
		}
		super.onStop();
	}


	public final class BundleKeys {
		public static final String ROOM_ID = "room_id";

		public static final String ROOM_NAME = "room_name";

		public static final String MEMBER_COUNT = "member_count";
	}


	/** 加载客户端最近的聊天记录
	 * @return 返回最近的聊天记录集合
	 */
	private List<AfMessageInfo> getRecentData() {
		
		mCount = CacheManager.getInstance().getPopMessageManager().getMsgCount(mFriendAfid);
		PalmchatLogUtils.println("GroupChatActivity  mOffset  " + mOffset + "  mCount  " + mCount);
		AfMessageInfo[] recentDataArray = null;
		 //从数据库获取最近的聊天记录
		if(mAfCorePalmchat != null) {
			recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mFriendAfid, mOffset, mCount);
		}
		 if(recentDataArray == null || recentDataArray.length == 0){
			return new  ArrayList<AfMessageInfo>();
		 }
		 List<AfMessageInfo> recentDataList = Arrays.asList(recentDataArray);
		 return recentDataList;
	}

	/** 绑定数据
	 * @param result
	 */
	private void bindData(List<AfMessageInfo> result) {
		int size = result.size();
		int listSize = listChats.size();
    	if(size > 0){
    		for (int i = 0; i < result.size(); i++) {
    			AfMessageInfo afMessageInfo = result.get(i);
				//把当前聊天记录插入到listChats集合中
    			CommonUtils.getRealList(listChats,afMessageInfo);
    			setVoiceQueue(afMessageInfo);
    			
    			PalmchatLogUtils.println("--eee bindData:" + afMessageInfo.msg);
			}
    		if(!mIsFirst){
    			vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
    			adapterListView.notifyDataSetChanged();
    			vListView.setSelection(listChats.size()-listSize);
    		}else{
    			mIsFirst = false;
    			setAdapter(null); 
    		}
    	}else{
    		if(!mIsFirst){
    			ToastManager.getInstance().show(context, R.string.already_top);
    		}
    		mIsFirst = false;
    	}
	}

	/** 设置语音列表顺序
	 * @param afMessageInfo
	 */
    private void setVoiceQueue(AfMessageInfo afMessageInfo) {
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		int status = afMessageInfo.status;
		if(MessagesUtils.isReceivedMessage(status) && msgType == AfMessageInfo.MESSAGE_VOICE){//from others chat and voice type
			CommonUtils.getRealList(listVoiceChats, afMessageInfo);//排序语音集合
		}
	}


	/** 删除信息
	 * @param afMessageInfo
	 */
	public void delete(final AfMessageInfo afMessageInfo) {
		new Thread(new Runnable() {
			public void run() {
				//从数据库中删除此信息
				mAfCorePalmchat.AfDbGrpMsgRmove(afMessageInfo);
				if(MessagesUtils.isLastMsg(afMessageInfo)) {
					 AfMessageInfo lastMsg = adapterListView.getLastMsg();
					 if(lastMsg != null){
						 MessagesUtils.insertMsg( lastMsg, true, true);
					 }else{
						 AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, mFriendAfid, 0, 1);
						 if(recentDataArray == null || recentDataArray.length <= 0){
							 MessagesUtils.removeMsg( afMessageInfo, true, true);
						 }
					 }
					 
				}
			}
		}).start();
		
	}


	/** 跳转到群组聊天页面
	 * @param groupId 群组ID
	 */
	void toGroupChatting(String groupId){
		//获取当前群组信息
		AfGrpProfileInfo groupListItem = CacheManager.getInstance().searchGrpProfileInfo(groupId);
		Bundle bundle = new Bundle();
		bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
		if (groupListItem != null && !"".equals(groupListItem.afid)
				|| null != groupListItem.afid) {
			bundle.putString(
					PopGroupMessageActivity.BundleKeys.ROOM_ID,
					groupListItem.afid);
		}
		if (groupListItem != null && !"".equals(groupListItem.name)
				|| null != groupListItem.name) {
			bundle.putString(
					PopGroupMessageActivity.BundleKeys.ROOM_NAME,
					groupListItem.name);
		}
		bundle.putBoolean(JsonConstant.KEY_FROM_POP_MESSAGE_TO_CHATTING, true);
		bundle.putBoolean(JsonConstant.KEY_FLAG, true);
		HelpManager.getInstance(context).jumpToPage(
				GroupChatActivity.class, bundle, false, 0,
				false);
		
		clearData();
	}
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	
//    	MobclickAgent.onPageEnd("GroupChatActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//        MobclickAgent.onPause(this);
    	
    	mIsFirst = true;
    	mOffset = 0;
    	int dbInt = mAfCorePalmchat.AfDbSetMsgExtra(mFriendAfid, getEditTextContent());
		PalmchatLogUtils.println("mFriendAfid  " + mFriendAfid + " group  onPause  dbInt " + dbInt + "  getEditTextContent  " + getEditTextContent());
    }

	@Override
	protected void onDestroy() {
		PalmchatLogUtils.println("--rrr1 PopGroupMessageActivity onDestroy");
		looperThread.looper.quit();
		
		super.onDestroy();
		if(mScreenObserver != null) {//停止监听广播
			mScreenObserver.stopScreenStateUpdate();
		}
		if(mHomeBroadcast != null){
			mHomeBroadcast.unRegisterBroadcastReceiver();
		}
	}

	@Override
	public void onItemClick(int item) {
		PalmchatLogUtils.println("GroupChatting onItemClick " + item);
		if(widget != null){
			widget.cancel();
		}
		switch (item) {
		case ChattingMenuDialog.ACTION_SPEAKER_MODE://杨声器模式
			AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setMode(AudioManager.MODE_NORMAL);//设置普通模式
			setVolumeControlStream(AudioManager.STREAM_MUSIC);  //设置媒体流
			if(!mIsFirst){
				ToastManager.getInstance().show(PopGroupMessageActivity.this, getString(R.string.switch_speaker_mode));
			}
			SharePreferenceService.getInstance(PopGroupMessageActivity.this).setListenMode(AudioManager.MODE_NORMAL);
			break;
		case ChattingMenuDialog.ACTION_HANDSET_MODE://听筒模式
			AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
			audio.setSpeakerphoneOn(false); //关闭免提
			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);  //通话模式
            audio.setMode(AudioManager.MODE_IN_CALL);  //设置听筒模式
            if(!mIsFirst){
            	ToastManager.getInstance().show(PopGroupMessageActivity.this, getString(R.string.switch_handset_mode));
            }
            SharePreferenceService.getInstance(PopGroupMessageActivity.this).setListenMode(AudioManager.MODE_IN_CALL);
			break;
		default:
			break;
		}
	}
	

	@Override
	public void onItemClick(int position, String name,String afid,byte sex) {
		// TODO Auto-generated method stub
		String str = getEditTextContent();
		str = str + "@"+name+":";
		setEditTextContent(str);
		
		vEditTextContent.requestFocus();
		if(!TextUtils.isEmpty(str)){
			vEditTextContent.setSelection(str.length());
		}
		CommonUtils.showSoftKeyBoard(vEditTextContent);
	}

	/**
	 * 删除表情
	 */
	public void emojj_del() {
		// TODO Auto-generated method stub
		CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
		
	}

	//存储时间的数组
	long[] mHits = new long[2];
	/**
	 * 双击事件、多击事件
	 */
		public void doubleClick() {
			// 双击事件响应
			//实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
			System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
			mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			//双击事件的时间间隔500ms
			if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
				PalmchatLogUtils.i("pophj", "test doubleClick");
				PalmchatApp.getApplication().isDoubleClickScreen = true;
				toGroupChatting(mFriendAfid);
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
