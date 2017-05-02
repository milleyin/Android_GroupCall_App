package com.afmobi.palmchat.ui.activity.social;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.eventbusmodel.SwitchFragmentEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.MyProfileDetailActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.AppFunctionTips_pop_Utils;
import com.afmobi.palmchat.ui.customview.CutstomPopwindowEditText;
import com.afmobi.palmchat.ui.customview.CutstomPopwindowEditText.OnPopItemClickListener;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;
import com.afmobi.palmchat.ui.customview.MyScrollView;
import com.afmobi.palmchat.ui.customview.RectImageView;
import com.afmobi.palmchat.ui.customview.RippleBackground;
import com.afmobi.palmchat.ui.customview.RippleView;
import com.afmobi.palmchat.ui.customview.ScrollViewListener;
import com.afmobi.palmchat.ui.customview.XCFlowLayout;
import com.afmobi.palmchat.ui.customview.switchbutton.SwitchButton;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.VoiceManager.OnCompletionListener;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.AfResponseComm.AfTagGetDefaultTagsResp;
import com.core.AfResponseComm.AfTagGetLangPackageResp;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.facebook.AccessToken;
import com.facebook.AccessToken.AccessTokenRefreshCallback;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * Send Broadcast Message af
 * 
 * @author heguiming
 * 
 */
public class ActivitySendBroadcastMessage extends BaseActivity implements OnClickListener, ScrollViewListener, PalmchatApp.ColseSoftKeyBoardLister, AfHttpResultListener {
	private static final int MAX_SELECTED = 9;
	private static final int MAX_SELECTED_ADD = MAX_SELECTED + 1;
	private AfPalmchat mAfCorePalmchat;
	private CutstomPopwindowEditText mMessageEdit;
	private ScrollView mSV_EditText;
	private TextView mSizeTxt;
	private EmojjView emojjView;
	private LinearLayout chatting_emoji_layout;
	private RelativeLayout chatting_options_layout;
	private ImageView vImageVoice;
	/**分享facebook开关*/
	private SwitchButton mSBt_Facebook;
	/**是否分享facebook图标*/
	private ImageView mIv_Facebook;
	/**分享facebook提示文字*/
	private TextView mTv_Facebook;
	private Button vImageSend;
	private RectImageView vImageEmotion, vImageKeyboard;
	KeyboardListenRelativeLayout relativeLayout;
	private View mSendVoiceView;
	private TextView mTitleTxt;
	private TextView mVoiceTime;
	private ImageView mImgDecibel;
	private Button mTalkBtn;
	private TextView mTextCancel;
	private ArrayList<AfMFileInfo> picturePathList = new ArrayList<AfMFileInfo>();
	/** 排布规则 */
	private String picture_rule_layout;
	private MyScrollView scrollview;
	private LooperThread looperThread;
	/** 录音界面 */
	private RippleView mRippleView;
	private ProgressBar mVoiceProgressBar, mVoiceProgressBar2;
	boolean is_cliek_send = false;
	public static final int SHOW_VIOICE_FUNCTIONTIPS = 222;
	/**水波纹*/
	private RippleBackground mRippleViewEffect;



	private String mCurTagname;
	/* zhh 文本框中输入的内容 用于控制改变文本框颜色后重新赋值不会造成死循环 */
	private String edtCurText = "";
	private int color_tag_normal;
	private int broadcast_type;
	private boolean mFacebookShareInit = false;
	/** 默认的6个 热门tag,因为这个列表是整个运行期间只取一次,下次启动要重新取, */
	private ArrayList<String> mTagsShowList = new ArrayList<String>();
	/** default tag */
	private static ArrayList<String> mDefaultNetList = new ArrayList<String>();
	private static HashMap<String, Boolean> mDefaultTagMap = new HashMap<String, Boolean>();
	/* 存储文本输入框中改变之前的文字 */
	private String beforeStr = "";
	private ArrayAdapter<String> adapterTagSearch;
	private ArrayList<String> mDictTagSearch = new ArrayList<String>();
	/** send voice */
	private MediaRecorder mRecorder;
	private long recordStart;
	private long recordEnd;
	private int recordTime;
	private boolean sent;
	private String mVoiceName;
	private final static int AT_LEAST_LENGTH = 60;
	private boolean isClick = false;
	private final int BASE = 600;
	private static final int RECORDING_TIMER = 9001;
	private EditListener editListener;
	/** 回调facebooksdk */
	private CallbackManager mCallbackManager;
	private LinearLayout mWhiteSpaceLayout;
	private boolean isVoiceRecordFirstShow;// true表示 是从外部点语音入口进来的。 用来处理没录音的时候返回
											// 直接关闭
	/** 流式布局 */
	private XCFlowLayout mXCFlowLayout;
	private static final int SHOWPOPWINDOW = 1;
	private static final int HIDEPOPWINDOW = 2;


	@Override
	public void findViews() {

		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_SD_BCM);

		setContentView(R.layout.activity_send_broadcast_messages);
		mXCFlowLayout = (XCFlowLayout) findViewById(R.id.flow_tags);
		looperThread = new LooperThread();
		looperThread.start();

		if (editListener == null) {
			editListener = new EditListener();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		Intent intent = getIntent();
		String messageText = null;
		int selectPosition = 0;
		if (null != intent) {
			ArrayList<AfMFileInfo> plist = (ArrayList<AfMFileInfo>) intent.getSerializableExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST);
			if (plist != null) {
				picturePathList = plist;
			}
			mCurTagname = intent.getStringExtra(JsonConstant.KEY_SENDBROADCAST_TAGNAME);
			picture_rule_layout = intent.getStringExtra(JsonConstant.KEY_SENDBROADCAST_PICRULE);
			messageText = intent.getStringExtra(JsonConstant.KEY_SENDBROADCAST_MESSAGE);
			selectPosition = intent.getIntExtra(JsonConstant.KEY_SELECT_POSITION,0);
			broadcast_type = intent.getByteExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, Consts.BR_TYPE_IMAGE_TEXT);

		}
		PalmchatApp.getApplication().setFacebookShareClose(true);
		PalmchatApp.getApplication().setColseSoftKeyBoardListe(this);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.broadcast_messages);

		mRelativeLayoutVoice = (RelativeLayout) findViewById(R.id.r_1);
		mRippleView = (RippleView) findViewById(R.id.ripple);
		mRippleViewEffect=(RippleBackground)findViewById(R.id.recordcall_effect);
		mVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_progress);
		mVoiceProgressBar2 = (ProgressBar) findViewById(R.id.voice_progress2);

		mSizeTxt = (TextView) findViewById(R.id.txt_size);

		findViewById(R.id.back_button).setOnClickListener(this);
		vImageSend = (Button) findViewById(R.id.btn_post);
		vImageSend.setVisibility(View.VISIBLE);
		vImageSend.setText(R.string.send_broadcast_post);
		vImageSend.setOnClickListener(this);

		mImgDecibel = (ImageView) findViewById(R.id.img_decibel);
		mWhiteSpaceLayout = (LinearLayout) findViewById(R.id.white_space_layout);
		mWhiteSpaceLayout.setOnClickListener(this);
		mSV_EditText = (ScrollView)findViewById(R.id.editView_scroll);
		mMessageEdit = (CutstomPopwindowEditText) findViewById(R.id.message_edit);
		mMessageEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mMessageEdit.setAsDropDownView(mSV_EditText);
		
		mMessageEdit.setOnPopItemClickListener(new OnPopItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (position > mDictTagSearch.size()) {
					return;
				}
				int index = mMessageEdit.getSelectionStart();
				String msgTxt = mMessageEdit.getText().toString();
				String insertText = mDictTagSearch.get(position).toString() + " ";
				if (null != msgTxt && (msgTxt.length() > 0)) {
					String msgPre = msgTxt.substring(0, index);
					int indexpre = msgPre.lastIndexOf("#");
					mMessageEdit.getText().replace( indexpre,index, insertText);
				} else {
					mMessageEdit.setText(insertText);
					mMessageEdit.setSelection(insertText.length());
				}
				mMessageEdit.hideInfoTip();
			}

		});

		mMessageEdit.setOnClickListener(this);
		setListener(mMessageEdit);

		if (null == mTagsShowList || (mTagsShowList.size() < 1)) {
			mTagsShowList.add(0, DefaultValueConstant.LABEL);
		}

		if (mDefaultNetList == null || mDefaultNetList.size() < 1) {
			mAfCorePalmchat.AfHttpAfBCGetDefaultTags(this);
		} else {
			mTagsShowList.addAll(mDefaultNetList);
			getFlowTagFromDict();			
		}

		setFlowlayout();
		mMessageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				PalmchatLogUtils.println("Chatting  onFocusChange  " + hasFocus);
				if (hasFocus) {
					closeEmotion();
				}
			}
		});

		mMessageEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 if (!CacheManager.getInstance().getEditTextDelete()) {
					setEdittextListener(s);
					CacheManager.getInstance().setEditTextDelete(false);
				} else {
					CacheManager.getInstance().setEditTextDelete(false);
				} 

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				 String str = beforeStr = s.toString().replace(DefaultValueConstant.CR, DefaultValueConstant.BLANK);
				if (str != null) {
					String string = str;
					editListener.edittext_string = string;
					editListener.edittext_length = s.length();
				} else {
					editListener.edittext_length = 0;
				} 

			}

			@Override
			public void afterTextChanged(Editable s) {
				String strText = s.toString().replace(DefaultValueConstant.CR, DefaultValueConstant.BLANK);
				if (!edtCurText.equals(strText)) { // 当文本内容发生改变时才去设置tag颜色
					edtCurText = strText;
					parseEdtText(strText);
 					doWithPopwidow();
				}
			}
		});
		emojjView = new EmojjView(this);
		emojjView.select(EmojiParser.SUN);
		vImageEmotion = (RectImageView) findViewById(R.id.image_emotion);
		vImageKeyboard = (RectImageView) findViewById(R.id.image_keyboard);
		vImageVoice = (ImageView) findViewById(R.id.image_voice);
		if (broadcast_type == Consts.BR_TYPE_VOICE_TEXT) {
			vImageVoice.setVisibility(View.VISIBLE);
			vImageVoice.setOnClickListener(this);
		} else {
			vImageVoice.setVisibility(View.INVISIBLE);
		}
		vImageEmotion.setOnClickListener(this);
		vImageKeyboard.setOnClickListener(this);

		chatting_options_layout = (RelativeLayout) findViewById(R.id.chatting_options_layout);
		chatting_options_layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
		chatting_emoji_layout.addView(emojjView.getViewRoot());

		scrollview = (MyScrollView) findViewById(R.id.scrollview);

		scrollview.setScrollViewListener(this);
		relativeLayout = (KeyboardListenRelativeLayout) findViewById(R.id.keyboardRelativeLayout);
		relativeLayout.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

			public void onKeyboardStateChanged(int state) {
				switch (state) {
				case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE:// keyboard
																		// hide
					if (!isClick && mSendVoiceView.getVisibility() != View.VISIBLE) {
						chatting_options_layout.setVisibility(View.INVISIBLE);
					}
					isClick = false;
					break;
				case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW:// keyboard
																		// show
					closeEmotion();
					isClick = false;
					if (!isClick && mSendVoiceView.getVisibility() != View.VISIBLE) {
						chatting_options_layout.setVisibility(View.VISIBLE);
					}

					break;
				default:
					break;
				}
				vImageEmotion.setVisibility(View.VISIBLE);
				vImageKeyboard.setVisibility(View.GONE);
			}

		});

		mSendVoiceView = findViewById(R.id.sendvoice_layout);
		mVoiceTime = (TextView) findViewById(R.id.voice_time);
		mVoiceTime.setText("0.0s");
		mTalkBtn = (Button) findViewById(R.id.talk_button1);
		mTalkBtn.setOnTouchListener(new HoldTalkListener());
		mTextCancel = (TextView) findViewById(R.id.tv_cancel);
		mTextCancel.setVisibility(View.GONE);



		mSBt_Facebook = (SwitchButton) findViewById(R.id.switch_facebook);
		mSBt_Facebook.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean opened) {
				// TODO Auto-generated method stub
				if ((!CommonUtils.hasPublishPermission())&&opened) {
					createFacebookShareDialog();
					return;
				}
				PalmchatApp.getApplication().setFacebookShareClose(!opened);
				if(opened){
					mIv_Facebook.setBackgroundResource(R.drawable.btn_share_to_facebook_p);
					mTv_Facebook.setTextColor(getResources().getColor(R.color.sharefb_text_color));
				} else {
					mIv_Facebook.setBackgroundResource(R.drawable.btn_share_to_facebook_n);
					mTv_Facebook.setTextColor(getResources().getColor(R.color.text_level_2));
				}
				PalmchatLogUtils.i(TAG, "--wx----open---"+opened);
			}
			
		});
		mIv_Facebook = (ImageView)findViewById(R.id.image_facebook);
		mTv_Facebook = (TextView)findViewById(R.id.shareFacebook);

		if (broadcast_type == Consts.BR_TYPE_VOICE_TEXT) {
			vImageVoice.performClick();
			isVoiceRecordFirstShow = true;
		}
		mMessageEdit.requestFocus();
		color_tag_normal = getResources().getColor(R.color.log_blue);

		if (!TextUtils.isEmpty(messageText)) {
			isNeedParseAllText=true;
			mMessageEdit.setText(messageText);
			
		} else if (!TextUtils.isEmpty(mCurTagname)) {
			isNeedParseAllText=true;
			mMessageEdit.setText(" "+mCurTagname + " ");
			mMessageEdit.setSelection(0);
			
		}
		String message = mMessageEdit.getText().toString();
		if (!TextUtils.isEmpty(message)&&(!TextUtils.isEmpty(messageText))) {
			mMessageEdit.setSelection(selectPosition);
		}

	}
	private boolean isNeedParseAllText;//如果是初始化就带文字的 那就要全局匹配一次 罪恶的根源在于公司部分6.0的手机 用setText会打不出字来的情况
	private void getFlowTagFromDict(){
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (null != looperThread.handler) {
					looperThread.handler.sendEmptyMessage(LooperThread.DEAL_TAGS_DICT);
				}
			}
		},DefaultValueConstant.DELAYTIME_LOADTICT);
	}
	private void setFlowlayout() {
		if (null != mXCFlowLayout) {
			mXCFlowLayout.removeAllViews();
		}
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// lp.leftMargin =
		// getResources().getDimensionPixelSize(R.dimen.flowview_marginleft);
		lp.rightMargin = getResources().getDimensionPixelSize(R.dimen.flowview_marginright);
		lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.flowview_marginbottom);
		int maxWidth = getResources().getDimensionPixelSize(R.dimen.flowview_marginwidth);
		int minWidth = getResources().getDimensionPixelSize(R.dimen.flowview_minwidth);
		if (null != mTagsShowList) {
			for (int i = 0; i < mTagsShowList.size(); i++) {
				TextView view = new TextView(this);
				view.setText(mTagsShowList.get(i));
				view.setTextColor(getResources().getColorStateList(R.drawable.selector_tag_text));
				view.setTextSize(12);
				view.setGravity(Gravity.CENTER);
				view.setOnClickListener(new MyOnClickListener(i));
				view.setBackgroundResource(R.drawable.selector_tag_btn);
				view.setMaxWidth(maxWidth);
				view.setMinWidth(minWidth);
				view.setSingleLine(true);
				view.setEllipsize(TextUtils.TruncateAt.END);
				mXCFlowLayout.addView(view, lp);
			}
		}
	}

	@Override
	public void onColseSoftKeyBoard() {
		CommonUtils.closeSoftKeyBoard(mMessageEdit);
	}

	/**
	 * 
	 */
	private void selectPopTagsFromDict(Object obj) {
		String tagName = "";
		if (null != obj) {
			try {
				tagName = (String) obj;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Message msg = Message.obtain();
		msg.what = LooperThread.DEAL_POPWINDOW_TAGSDICT;

		ArrayList<String> dictTagsResult = new ArrayList<String>();
		AfResponseComm.AfTagGetLangPackageResp adtag = mAfCorePalmchat.AfDbBCLangPackageListSearch(tagName, 0, DefaultValueConstant.SEARCHTAGSFROMDICT);
		if (adtag != null) {
			ArrayList<AfResponseComm.AfTagLangPackageItem> list = adtag.db_list;
			if (null != list) {
				mDictTagSearch.clear();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).tag_name != null && list.get(i).tag_name.startsWith("#")) {
						String abc = list.get(i).tag_name;
						PalmchatLogUtils.i("SENDBORAD", "abc:" + abc);
						dictTagsResult.add(abc);
					}
				}
				msg.obj = dictTagsResult;
				msg.arg1 = SHOWPOPWINDOW;

			}
		} else {
			msg.arg1 = HIDEPOPWINDOW;
		}
		mHandler.sendMessage(msg);
	}

	/**
	 * 处理输入tag联想的问题
	 */
	private void doWithPopwidow() {
		int position = mMessageEdit.getSelectionStart();

		String msg = mMessageEdit.getText().toString().replace(DefaultValueConstant.CR, DefaultValueConstant.BLANK);
		position = mMessageEdit.getSelectionStart();
		String message = msg.substring(0, position);
		int _tagIndex = message.lastIndexOf("#");
		Message msgg = Message.obtain();
		if (_tagIndex >= 0) {
     		String	tagName = message.substring(_tagIndex);
			/* 解析tag */
			Pattern patternTag = Pattern.compile(DefaultValueConstant.SEARCHTAGCONDITION); // 判断是否是tag的表达式
			Matcher matcherTag = patternTag.matcher(tagName);
			//#后面有字母才匹配
			if (matcherTag.matches()) {
				if ((null != looperThread) && (null != looperThread.handler)) {
					msgg.what = LooperThread.DEAL_POPWINDOW_TAGSDICT;
					msgg.obj = tagName;
					looperThread.handler.sendMessage(msgg);
				}
			} else {
				mMessageEdit.hideInfoTip();
			}
		} else {
			mMessageEdit.hideInfoTip();
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK ==keyCode ){
			if(mMessageEdit.isPopShow()){
				mMessageEdit.hideInfoTip();
				return true;
			} else {
				return super.onKeyUp(keyCode, event);
			}

		} else{			
			return super.onKeyUp(keyCode, event);
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			addTag(mTagsShowList.get(index));
		}
	}

	class LooperThread extends Thread {
		private static final int DEAL_TAGS_DICT = 9998;
		private static final int DEAL_POPWINDOW_TAGSDICT = 9997;
		Handler handler;
		Looper looper;

		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case DEAL_TAGS_DICT: {
						getTagsFromDict();
						break;
					}
					case DEAL_POPWINDOW_TAGSDICT: {
						selectPopTagsFromDict(msg.obj);
						break;
					}
					default:
						break;
					}
				}
			};
			Looper.loop();
		}
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case MessagesUtils.MSG_VOICE: {
				recordVoiceFinish(mVoiceName);
				break;
			}
			case RECORDING_TIMER:
				long recordCurTime = System.currentTimeMillis();
				int voiceProgress = (int) ((recordCurTime - recordStart) / 1000);
				if (voiceProgress <= RequestConstant.RECORD_VOICE_TIME_BROADCAST) {
					mVoiceProgressBar.setProgress(voiceProgress);
					mVoiceProgressBar2.setProgress(voiceProgress);
				}
				mVoiceTime.setText(CommonUtils.diffTime(recordCurTime, recordStart));
				refreshVoiceAmplitude();
				if (recordStart > 0 && recordCurTime - recordStart >= RequestConstant.RECORD_VOICE_TIME_BROADCAST * 1000 && !sent) {
					stopRecordingTimeTask();
					stopRecording();
					if (CommonUtils.getSdcardSize()) {
						ToastManager.getInstance().show(context, R.string.memory_is_full);
						break;
					}
					PalmchatLogUtils.e(TAG, "----handleMessage----MessagesUtils.MSG_VOICE");

					sent = true;

					mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);

					setMode(SharePreferenceService.getInstance(ActivitySendBroadcastMessage.this).getListenMode());

					mVoiceTime.setVisibility(View.INVISIBLE);

					mTimer.postDelayed(new Runnable() {

						@Override
						public void run() {
							mTextCancel.setVisibility(View.GONE);
						}
					}, 1000);

					mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
				}
				break;

			case MessagesUtils.MSG_TOO_SHORT:
				ToastManager.getInstance().show(context, R.string.message_too_short);
				stopRecording();
				stopRecordingTimeTask();
				break;

			case MessagesUtils.SEND_VOICE:
				isVoiceRecordFirstShow = false;
				picturePathList.clear();
				AfMFileInfo params = new AfMFileInfo();
				params.local_img_path = (String) msg.obj;
				params.type = Consts.BR_TYPE_VOICE_TEXT;
				params.url_type = Consts.URL_TYPE_VOICE;
				params.recordTime = recordTime;
				picturePathList.add(params);
				setBtnClickable(true, false, false, false);
				setVoice();
				onBack();

				PalmchatLogUtils.e(TAG, "MessagesUtils.SEND_VOICE  -- size " + picturePathList.size() + "  recordTime " + recordTime);

				break;

			case MessagesUtils.MSG_ERROR_OCCURRED:
				ToastManager.getInstance().show(context, R.string.error_occurred);
				break;

			// facebook成功处理ui
			case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
				//PalmchatApp.getApplication().setFacebookShareClose(false);
				ToastManager.getInstance().show(ActivitySendBroadcastMessage.this, R.string.facebook_login_success_tip);
				mSBt_Facebook.setChecked(true);
				break;
			}
				// facebook失败处理UI
			case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
				//PalmchatApp.getApplication().setFacebookShareClose(true);
				mSBt_Facebook.setChecked(false);
				ToastManager.getInstance().show(ActivitySendBroadcastMessage.this, R.string.facebook_login_failed_tip);
				break;
			}
			
			case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
				ToastManager.getInstance().show(ActivitySendBroadcastMessage.this, R.string.facebook_login_cancel_tip);
				break;
			}
			case LooperThread.DEAL_TAGS_DICT: {
				if (null != msg.obj) {
					try {
						@SuppressWarnings("unchecked")
						ArrayList<String> tagsList = (ArrayList<String>) msg.obj;
						if (null != tagsList) {
							mTagsShowList.addAll(tagsList);
							if (mTagsShowList != null) {
								setFlowlayout();
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case LooperThread.DEAL_POPWINDOW_TAGSDICT: {
				if (SHOWPOPWINDOW == msg.arg1) {
					mDictTagSearch.clear();
					if (null != msg.obj) {
						try {
							mDictTagSearch = (ArrayList<String>) msg.obj;
							mMessageEdit.updateData(mDictTagSearch);	
							
							if(View.VISIBLE==chatting_options_layout.getVisibility()){
								//表情输入栏
								int chatting_options=(int)chatting_options_layout.getY();
								//pop坐标
								int PopY = (int)mSV_EditText.getY()+mSV_EditText.getHeight()+mMessageEdit.getPopYoffset();
								//超出输入栏 大小
								int diff =PopY+mMessageEdit.getPopdefaultHeight()-chatting_options;
								if(diff>0){
									int height = mMessageEdit.getPopdefaultHeight()-diff;
									mMessageEdit.setPopHeight(height);
								} else {
									mMessageEdit.setPopDefaultHeight();
								}
							} else{
								mMessageEdit.setPopDefaultHeight();
							}
							mMessageEdit.showInfoTip();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} else if (HIDEPOPWINDOW == msg.arg1) {
					mDictTagSearch.clear();
					mMessageEdit.hideInfoTip();
				}

				break;
			}
			default:
				break;
			}

		};
	};

	private Handler mTimer = new Handler();

	private void refreshVoiceAmplitude() {

		if (mRecorder != null) {
			int maxAmplitude = mRecorder.getMaxAmplitude();
			PalmchatLogUtils.println("--wwww refreshVoiceAmplitude maxAmplitude " + maxAmplitude);
			int ratio = maxAmplitude / BASE;
			int decibel = 0;
			if (ratio > 1) {
				decibel = (int) (20 * Math.log(ratio));
			}

			decibel = decibel / 4;
			PalmchatLogUtils.println("--wwww refreshVoiceAmplitude decibel " + decibel);

			if (decibel < 5) {
				mImgDecibel.setBackgroundResource(Constants.voice_anims[0]);
			} else if (decibel > 5 && decibel < 7) {
				mImgDecibel.setBackgroundResource(Constants.voice_anims[1]);
			} else if (decibel > 7 && decibel < 10) {
				mImgDecibel.setBackgroundResource(Constants.voice_anims[2]);
			} else if (decibel > 10 && decibel < 13) {
				mImgDecibel.setBackgroundResource(Constants.voice_anims[3]);
			} else if (decibel > 13 && decibel < 15) {
				mImgDecibel.setBackgroundResource(Constants.voice_anims[4]);
			} else if (decibel > 15) {
				mImgDecibel.setBackgroundResource(Constants.voice_anims[5]);
			}

		}
	}

	private void recordVoiceFinish(final String voiceName) {
		mHandler2.removeCallbacksAndMessages(null);

		if (!CommonUtils.isEmpty(voiceName)) {
			long end = System.currentTimeMillis();
			final long time = Math.min((int) ((end - mTimeStart) / 1000), RequestConstant.RECORD_VOICE_TIME_BROADCAST);

			PalmchatLogUtils.e(TAG, "----sendVoice---- time " + time);

			new Thread(new Runnable() {
				public void run() {
					byte[] data = FileUtils.readBytes(RequestConstant.VOICE_CACHE + voiceName);

					if (time < 2) {
						mHandler.sendEmptyMessage(MessagesUtils.MSG_TOO_SHORT);
					} else {
						if (data == null || data.length <= AT_LEAST_LENGTH) {
							mHandler.sendEmptyMessage(MessagesUtils.MSG_ERROR_OCCURRED);
							return;
						}

						mHandler.obtainMessage(MessagesUtils.SEND_VOICE, RequestConstant.VOICE_CACHE + voiceName).sendToTarget();
					}
				}
			}).start();
		} else {
			ToastManager.getInstance().show(context, getString(R.string.sdcard_unmounted));
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if ((!mFacebookShareInit) && CommonUtils.hasPublishPermission()) {
			mSBt_Facebook.setChecked(true);
			//PalmchatApp.getApplication().setFacebookShareClose(false);
			mFacebookShareInit = true;
		} else if (!CommonUtils.hasPublishPermission()) {
			mSBt_Facebook.setChecked(false);
			//PalmchatApp.getApplication().setFacebookShareClose(true);
			mFacebookShareInit = false;
		}
		if (null != mMessageEdit) {
			CommonUtils.forceSetKeyBoard(mMessageEdit, "close");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
		// MobclickAgent.onPageEnd(TAG);
		// MobclickAgent.onPause(this);
		if (picturePathList.size() > 0) {
			AfMFileInfo tmp = picturePathList.get(0);
			if (Consts.BR_TYPE_VOICE_TEXT == tmp.type || Consts.URL_TYPE_VOICE == tmp.url_type) {
				tmp.voicePlaying = false;

			}
		}

		VoiceManager.getInstance().completion();
		if (recordStart > 0) {

			if (!sent) {
				stopRecordingTimeTask();
				stopRecording();
				PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
				if (CommonUtils.getSdcardSize()) {
					ToastManager.getInstance().show(context, R.string.memory_is_full);
				} else {

					PalmchatLogUtils.println("---- send voice----");
					mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
				}

				setMode(SharePreferenceService.getInstance(ActivitySendBroadcastMessage.this).getListenMode());

				mVoiceTime.setVisibility(View.INVISIBLE);

				mTimer.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mTextCancel.setVisibility(View.GONE);
					}
				}, 1000);

				mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));

				onBack();

			}

		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	private void closeEmotion() {
		chatting_emoji_layout.setVisibility(View.GONE);
	}

	private View voiceView;
	private RelativeLayout mRelativeLayoutVoice;
	private ImageView playIcon;
	private ImageView playAnim;
	private TextView playTime;
	private View deleteView;
	private AnimationDrawable animDrawable;

	void setVoice() {
		if (picturePathList.size() > 0) {
			final AfMFileInfo info = picturePathList.get(0);
			if (Consts.URL_TYPE_VOICE == info.url_type) {
				mRelativeLayoutVoice.setVisibility(View.VISIBLE);
				voiceView = findViewById(R.id.lin_play_icon_to_voice);
				// viewHolder.playBtn = convertView.findViewById(R.id.r_1);
				playIcon = (ImageView) findViewById(R.id.play_icon);

				playAnim = (ImageView) findViewById(R.id.play_icon_to_voice_anim);
				playTime = (TextView) findViewById(R.id.play_time_to_voice);
				deleteView = findViewById(R.id.click_remove);
				voiceView.setVisibility(View.VISIBLE);
				playTime.setText(info.recordTime + "s");

				mRelativeLayoutVoice.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!CommonUtils.isHasSDCard()) {
							ToastManager.getInstance().show(ActivitySendBroadcastMessage.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
							return;
						}

						boolean isPlaying = VoiceManager.getInstance().isPlaying();
						if (isPlaying) {
							VoiceManager.getInstance().pause();
							info.voicePlaying = false;
							return;
						}

						info.voicePlaying = true;

						VoiceManager.getInstance().setView(playAnim);
						VoiceManager.getInstance().setPlayIcon(playIcon);
						VoiceManager.getInstance().play(info.local_img_path, new OnCompletionListener() {

							@Override
							public Object onGetContext() {
								return ActivitySendBroadcastMessage.this;
							}

							@Override
							public void onCompletion() {
								info.voicePlaying = false;
							}

							@Override
							public void onError() {

							}
						});

						if (info.voicePlaying) {
							animDrawable = ((AnimationDrawable) getResources().getDrawable(R.anim.playing_from_voice_frame));
							if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
								playAnim.setBackgroundDrawable(animDrawable);
							}else {
								playAnim.setBackground(animDrawable);
							}
							if (animDrawable != null && !animDrawable.isRunning()) {
								animDrawable.setOneShot(false);
								animDrawable.start();
							}
							playIcon.setBackgroundResource(R.drawable.voice_pause_icon);
						}
					}
				});

				deleteView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (VoiceManager.getInstance().isPlaying()) {
							VoiceManager.getInstance().pause();

							info.voicePlaying = false;
							// notifyDataSetChanged();
						}
						picturePathList.clear();
						setVoice();
						setBtnClickable(true, true, true, true);
					}
				});

			}
		} else {
			voiceView.setVisibility(View.GONE);
		}

	}

	/**
	 * 添加表情
	 * 
	 * @param id
	 * @param data
	 */
	public void setFace(int id, String data) {
		int length = data.length() + mMessageEdit.length();
		if (length <= Constants.BROADCAST_TEXTMAXCOUNT) {
			/* 获取之前文本框中内容 */
//			String mStr = mMessageEdit.getText().toString();
			/* 获取光标所在位置 */
			int _sIndex = mMessageEdit.getSelectionStart();
			/*String text = mStr.substring(0, _sIndex) + data + mStr.substring(_sIndex);
			mMessageEdit.setText(text);
			mMessageEdit.setSelection(_sIndex + data.length());*/
			mMessageEdit.getText().insert(_sIndex, data);
		}

	}

	/**
	 * 添加一个tag
	 * 
	 * @param tag
	 */
	public void addTag(String tag) {
		int length = tag.length() + mMessageEdit.length();
		if (length <= Constants.BROADCAST_TEXTMAXCOUNT) {
			/* 获取之前文本框中内容 */
			String mStr = mMessageEdit.getText().toString();
			/* 获取光标所在位置 */
			int _sIndex = mMessageEdit.getSelectionStart();
			String text = "";
			/* 插入标签 */
			if (tag.equals("#")) {
				//text = mStr.substring(0, _sIndex) + tag + mStr.substring(_sIndex);
				mMessageEdit.requestFocus();
				CommonUtils.showSoftKeyBoard(mMessageEdit);
				/*mMessageEdit.setText(text);
				mMessageEdit.setSelection(_sIndex + tag.length());*/
				mMessageEdit.getText().insert(_sIndex,  tag  );
			} else {
				/*text =  mStr.substring(0, _sIndex) +  tag + " " + mStr.substring(_sIndex);
				mMessageEdit.setText(text);
				mMessageEdit.setSelection(_sIndex + tag.length() + 1);*/
				mMessageEdit.getText().insert(_sIndex,  tag + " ");
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			boolean hasKeyBoard = relativeLayout.hasKeyboard();
			if (hasKeyBoard) {
				CommonUtils.closeSoftKeyBoard(mMessageEdit);
				return;
			}
			onBack();
			break;
		// case R.id.send_btn:
		case R.id.btn_post:
			if (check_completProfile(this)) {
				sendMessage();
			}
			break;
		case R.id.message_edit:
			closeEmotion();
			mHandler.postDelayed( new Runnable() {
				public void run() {
					vImageEmotion.setVisibility(View.VISIBLE);
					vImageKeyboard.setVisibility(View.GONE);
				}
			},400);
			break;
		case R.id.image_keyboard:
			closeEmotion();
			isClick = false;
			mHandler.postDelayed(new Runnable() {
				public void run() {
					vImageEmotion.setVisibility(View.VISIBLE);
					vImageKeyboard.setVisibility(View.GONE);
				}
			}, 400);
			mMessageEdit.requestFocus();
			CommonUtils.showSoftKeyBoard(mMessageEdit);
			break;
		case R.id.image_emotion:
			if (!isClick) {
				mHandler.postDelayed(new Runnable() {// 这里加delay是为了键盘切换为表情时
														// 能延后弹出
					public void run() {
						showEmotion();
						vImageEmotion.setVisibility(View.GONE);
						vImageKeyboard.setVisibility(View.VISIBLE);
					}
				}, 400);
			}
			isClick = true;
			CommonUtils.closeSoftKeyBoard(mMessageEdit);
			break;
		case R.id.image_voice:
			showSendVoice();
			break;
		case R.id.white_space_layout:
			/** 关闭键盘 */
			CommonUtils.closeSoftKeyBoard(mMessageEdit);
			break;

		default:
			break;
		}
	}

	/**
	 * 发广播前检查是否有region
	 * 
	 * @param context
	 * @return
	 */
	private boolean check_completProfile(final Context context) {
		AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();
		if (TextUtils.isEmpty(afProfileInfo.region) || afProfileInfo.region.equals(DefaultValueConstant.OTHERS)) {
			AppDialog appDialog = new AppDialog(context);
			String msg = context.getResources().getString(R.string.region_null_tips);
			appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {

				@Override
				public void onRightButtonClick() {
					Intent intent = new Intent(context, MyProfileDetailActivity.class);
					Bundle data = new Bundle();
					data.putInt(JsonConstant.KEY_FORM_CITY_BC, MyProfileDetailActivity.FROM_CITY_BC);
					intent.putExtras(data);
					context.startActivity(intent);
				}

				@Override
				public void onLeftButtonClick() {// wxl 2016 0229修改，但不去改Profile时
					sendMessage();
				}
			});
			appDialog.show();
		} else {
			return true;
		}
		return false;
	}



	@Override
	public void onBackPressed() {

		onBack();

	}

	private void showEmotion() {
		chatting_emoji_layout.setVisibility(View.VISIBLE);
	}

	/**
	 * 从文本中获取合法的tags
	 * 
	 * @param message
	 * @return
	 */
	public static String getTags(String message) {
		String tag_string = null;
		Pattern patternTags = Pattern.compile(EmojiParser.regTags); // 判断是否是tag的表达式
		Matcher matcherTags = patternTags.matcher(message);

		ArrayList<String> arrlist = new ArrayList<String>();
		while (matcherTags.find()) { // 匹配所有合法tags
			try {
				tag_string = matcherTags.group();
				if (!arrlist.contains(tag_string)) {
					arrlist.add(tag_string);
				}
			} catch (Exception e) {
				break;
			}

			if (arrlist.size() >= EmojiParser.MAXTAGSCOUNT) {
				break;
			}
		}
		String tags = "";
		for (int i = 0; i < arrlist.size(); i++) {
			tags += arrlist.get(i);
			if (i < arrlist.size() - 1) {
				tags += ",";
			}
		}
		if (arrlist.isEmpty()) {
			tags = null;
		}
		return tags;
	}

	/**
	 * 发送广播
	 */
	@SuppressLint("DefaultLocale")
	private void sendMessage() {
		if (!is_cliek_send) {
			is_cliek_send = true;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					CommonUtils.closeSoftKeyBoard(mMessageEdit);
				}
			}, 100);

			String message = mMessageEdit.getText().toString();

			if (!TextUtils.isEmpty(message)) {
				message = message.trim();
			}
			String tag_string = getTags(message);// 获取tags

			int index = picturePathList.size();
			if (!TextUtils.isEmpty(message)) {
				if (index > 0) {
					AfMFileInfo params = picturePathList.get(0);
					ArrayList<AfMFileInfo> listPath = new ArrayList<AfMFileInfo>();
					for (AfMFileInfo tmp : picturePathList) {
						if (!tmp.is_add) {
							listPath.add(tmp);
						}
					}
					if (listPath.size() > 0) {
						if (Consts.URL_TYPE_IMG == params.url_type) {
							BroadcastUtil.getInstance().sendBroadcastPictureAndText(context, message, listPath, Consts.BC_PURVIEW_ALL, tag_string, picture_rule_layout);
						} else if (Consts.URL_TYPE_VOICE == params.url_type) {
							BroadcastUtil.getInstance().sendBroadcastVoiceAndText(context, message, listPath, Consts.BC_PURVIEW_ALL, tag_string);
						}
					}
				} else {// 纯文字
					BroadcastUtil.getInstance().sendBroadcastMsg(context, message, Consts.BC_PURVIEW_ALL, tag_string);
				 }

				doSendBroadFinish();
			} else {
				if (index > 0) {
					AfMFileInfo params = picturePathList.get(0);
					ArrayList<AfMFileInfo> listPath = new ArrayList<AfMFileInfo>();
					for (AfMFileInfo tmp : picturePathList) {
						if (!tmp.is_add) {
							listPath.add(tmp);
						}
					}
					if (listPath.size() > 0) {
						if (Consts.URL_TYPE_IMG == params.url_type) {
							BroadcastUtil.getInstance().sendBroadcastPictureAndText(context, message, listPath, Consts.BC_PURVIEW_ALL, tag_string, picture_rule_layout);
						} else if (Consts.URL_TYPE_VOICE == params.url_type) {
							BroadcastUtil.getInstance().sendBroadcastVoiceAndText(context, message, listPath, Consts.BC_PURVIEW_ALL, tag_string);
						}
						 	doSendBroadFinish();
					} else {
						is_cliek_send = false;
						if (broadcast_type == Consts.BR_TYPE_VOICE_TEXT) {
							showToast(R.string.please_input_message_or_record_voice);
						}else{
							showToast(R.string.please_input_message_or_select_picture);
						}
					}
				} else {
					is_cliek_send = false;
					if (broadcast_type == Consts.BR_TYPE_VOICE_TEXT) {
						showToast(R.string.please_input_message_or_record_voice);
					}else{
						showToast(R.string.please_input_message_or_select_picture);
					}
				}
			}
		}
	}

	/**
	 * 发送成功返回
	 */
	private void doSendBroadFinish(){
		SwitchFragmentEvent switchFragmentEvent = new SwitchFragmentEvent();

		if(TextUtils.isEmpty(mCurTagname)){
			switchFragmentEvent.setFromType(IntentConstant.REQUEST_CODE_TAGPAGE);
		}
		EventBus.getDefault().post(switchFragmentEvent);
		finish();
	}

	private void showSendVoice() {
		CommonUtils.closeSoftKeyBoard(mMessageEdit);
		mSendVoiceView.setVisibility(View.VISIBLE);
		scrollview.setVisibility(View.GONE);
		mMessageEdit.setVisibility(View.GONE);
		chatting_options_layout.setVisibility(View.INVISIBLE);
		mTitleTxt.setText(R.string.send_voice);
		mTextCancel.setText(R.string.call_Voice_Tips);
		mTextCancel.setVisibility(View.VISIBLE);
		vImageSend.setVisibility(View.GONE);
	}

	private void onBack() {
		if (isVoiceRecordFirstShow) {// 如果是从语音入口进来 直接录音 这时点返回 直接关闭 不进编辑文本
			finish();
			return;
		}
		if (mSendVoiceView.getVisibility() == View.VISIBLE) {
			scrollview.setVisibility(View.VISIBLE);
			mMessageEdit.setVisibility(View.VISIBLE);
			mMessageEdit.requestFocus();
			if (recordStart > 0 && !sent) {

				mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
				stopRecordingTimeTask();
				setMode(SharePreferenceService.getInstance(ActivitySendBroadcastMessage.this).getListenMode());
				mTalkBtn.setSelected(false);
				stopRecording();
				sent = true;

			} else {
				mTitleTxt.setText(R.string.broadcast_messages);
				vImageSend.setVisibility(View.VISIBLE);
				mSendVoiceView.setVisibility(View.GONE);

			}

		} else if (chatting_emoji_layout != null && chatting_emoji_layout.getVisibility() == View.VISIBLE) {
			chatting_emoji_layout.setVisibility(View.GONE);
		} else {
			String message = mMessageEdit.getText().toString();
			AfMFileInfo afMessageInfo = null;
			if (picturePathList.size() > 0) {
				afMessageInfo = picturePathList.get(0);
			}

			if (broadcast_type == Consts.BR_TYPE_IMAGE || broadcast_type == Consts.BR_TYPE_IMAGE_TEXT) {// 如果是图片广播那么要返回到图片编辑界面
				// 返回到编辑图片 并带入基本参数
				Intent intent = new Intent();
				intent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICLIST, picturePathList);
				intent.putExtra(JsonConstant.KEY_SENDBROADCAST_PICRULE, picture_rule_layout);
				if (!TextUtils.isEmpty(message)) {// 返回的时候带入用户编辑的文本数据，作为保存
					intent.putExtra(JsonConstant.KEY_SENDBROADCAST_MESSAGE, message);
					intent.putExtra(JsonConstant.KEY_SELECT_POSITION,mMessageEdit.getSelectionStart());
				}
				intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, Consts.BR_TYPE_IMAGE_TEXT);
				intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TAGNAME, mCurTagname);
				intent.setClass(ActivitySendBroadcastMessage.this, EditBroadcastPictureActivity.class);
				startActivity(intent);
				finish();// 这里不能调doFinish 因为图片还要被恢复 不能删除
			} else if (!TextUtils.isEmpty(message) || afMessageInfo != null) {// 如果有文本
																				// 或
																				// 语音
																				// 提示是否放弃
				AppDialog appDialog = new AppDialog(this);
				appDialog.createConfirmDialog(this, null, getString(R.string.exit_dialog_content), null, getString(R.string.exit), new OnConfirmButtonDialogListener() {
					@Override
					public void onRightButtonClick() {
						doFinishIfNotSend();
					}

					@Override
					public void onLeftButtonClick() {

					}
				});
				appDialog.show();
			} else {// 如果无图片 无文字 无语音 直接就返回了
				doFinishIfNotSend();
			}
		}
	}

	/**
	 * 如果是取消发送的
	 * zhh 关闭当前页面
	 */
	private void doFinishIfNotSend() {
		finish();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (VoiceManager.getInstance().isPlaying()) {
					VoiceManager.getInstance().pause();
				}
				// 当取消发送广播 删除编辑过的图片和缩图。 （如果发送的 就不能删除，发送的时候上传数据要用）
				Iterator<AfMFileInfo> sListIterator = picturePathList.iterator();
				while (sListIterator.hasNext()) {
					AfMFileInfo mediaParams = sListIterator.next();
					String picture_path = mediaParams.local_img_path;
					String thumb_path = mediaParams.local_thumb_path;
					if (!TextUtils.isEmpty(picture_path)) {
						FileUtils.fileDelete(picture_path);
					}
					if (!TextUtils.isEmpty(thumb_path)) {
						FileUtils.fileDelete(thumb_path);
					}
					sListIterator.remove();
				}
			}
		}, 100);
	}

	@Override
	protected void onDestroy() {
		looperThread.looper.quit();
		is_cliek_send = false;
		PalmchatApp.getApplication().setColseSoftKeyBoardListe(null);

		super.onDestroy();
	}

	@Override
	public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
		/*
		 * if (Math.abs(y-oldy) > 0) { closeEmotion();
		 * CommonUtils.closeSoftKeyBoard(mMessageEdit); }
		 */
	}

	private long mTimeStart;

	/**
	 * send voice
	 * 
	 */
	Handler mHandler2 = new Handler();

	private String startRecording() {
		String voiceName = null;
		if (RequestConstant.checkSDCard()) {
			voiceName = mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_VOICE);
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// mRecorder.setAudioSamplingRate(16000);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mRecorder.setOutputFile(RequestConstant.VOICE_CACHE + voiceName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mRecorder.setOnInfoListener(new OnInfoListener() {

				@Override
				public void onInfo(MediaRecorder mr, int what, int extra) {
					PalmchatLogUtils.println("mRecorder onInfo what " + what + " extra " + extra);

					mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
					stopRecordingTimeTask();
					setMode(SharePreferenceService.getInstance(ActivitySendBroadcastMessage.this).getListenMode());
					mTalkBtn.setSelected(false);
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
				e.printStackTrace();
				PalmchatLogUtils.e("startRecording", "prepare() failed" + e.getMessage());

				return null;
			}

			final String recordName = voiceName;

			mHandler2.postDelayed(new Runnable() {

				@Override
				public void run() {
					long time1 = System.currentTimeMillis();
					byte[] recordData = FileUtils.readBytes(RequestConstant.VOICE_CACHE + recordName);
					long time2 = System.currentTimeMillis();

					if (recordData != null) {
						PalmchatLogUtils.println("--ddd ActivitySendBroadcastMessage recordData length " + recordData.length);
					} else {
						PalmchatLogUtils.println("--ddd ActivitySendBroadcastMessage recordData null ");
					}
					PalmchatLogUtils.println("--ddd ActivitySendBroadcastMessage readBytes time  " + (time2 - time1));
					mRippleView.setVisibility(View.VISIBLE);
					mRippleView.setOriginRadius(mTalkBtn.getWidth() / 2);
					mRippleView.startRipple();
					mRippleViewEffect.startRippleAnimation();

					mTextCancel.setVisibility(View.VISIBLE);
					mTextCancel.setText(R.string.push_up_to_cancel);
					mVoiceTime.setVisibility(View.VISIBLE);
					mVoiceTime.setText("0.0s");
					startRecordingTimeTask();

					AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
					am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
				}
			}, 50);

		} else {
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
				PalmchatLogUtils.e("stopRecording", "stopRecording() failed" + e.getMessage());
			} finally {
				mRecorder = null;
				recordEnd = System.currentTimeMillis();
				recordTime = Math.min((int) ((recordEnd - recordStart) / 1000), RequestConstant.RECORD_VOICE_TIME_BROADCAST);
				recordStart = 0;
				recordEnd = 0;
				mVoiceTime.setVisibility(View.GONE);
				mVoiceTime.setText("0s");
				mRippleView.setVisibility(View.GONE);
				mRippleView.stopRipple();
				mRippleViewEffect.stopRippleAnimation();
				mVoiceProgressBar.setVisibility(View.VISIBLE);
				mVoiceProgressBar2.setVisibility(View.GONE);
				mVoiceProgressBar.setProgress(0);
				mVoiceProgressBar2.setProgress(0);

				AudioManager am = (AudioManager) PalmchatApp.getApplication().getSystemService(Context.AUDIO_SERVICE);
				am.abandonAudioFocus(null);

			}
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
				mHandler.sendEmptyMessage(RECORDING_TIMER);
			}
		};
		recordingTimer.schedule(recordingTimerTask, 0, 100);
	}

	private void stopRecordingTimeTask() {
		if (null != recordingTimerTask) {
			recordingTimerTask.cancel();
		}
		mImgDecibel.setBackgroundResource(Constants.voice_anims[0]);
	}

	private float mDownX;
	private float mDownY;

	private long mCurrentClickTime;
	private static final long LONG_PRESS_TIME = 500;

	class HoldTalkListener implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.talk_button1) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (CommonUtils.isFastClick()) {
						break;
					}
					mDownX = event.getX();
					mDownY = event.getY();

					PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_DOWN----");

					if (!CommonUtils.isHasSDCard()) {
						ToastManager.getInstance().show(ActivitySendBroadcastMessage.this, R.string.without_sdcard_cannot_play_voice_and_so_on);
						return false;
					}
					sent = false;
					mCurrentClickTime = Calendar.getInstance().getTimeInMillis();

					if (recordStart > 0) {
						mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);
						stopRecordingTimeTask();
						setMode(SharePreferenceService.getInstance(ActivitySendBroadcastMessage.this).getListenMode());
						mTalkBtn.setSelected(false);
						stopRecording();
						sent = true;
						return false;
					}

					mVoiceName = startRecording();

					if (mVoiceName == null) {
						return false;
					}
					setMode(AudioManager.MODE_NORMAL);
					if (VoiceManager.getInstance().isPlaying()) {
						VoiceManager.getInstance().pause();
					}
					mTimer.removeCallbacksAndMessages(null);

					PalmchatLogUtils.println("mVoiceName " + mVoiceName);
					break;
				case MotionEvent.ACTION_MOVE:

					if (recordStart <= 0) {
						break;
					}

					float offsetX1 = Math.abs(event.getX() - mDownX);
					float offsetY1 = Math.abs(event.getY() - mDownY);

					if (offsetX1 < ImageUtil.dip2px(context, 85) && offsetY1 < ImageUtil.dip2px(context, 85)) {
						if (mTextCancel.getText().equals(getString(R.string.cancel_voice))) {
							mTextCancel.setText(R.string.empty);
						} else
						{
							mTextCancel.setText(R.string.push_up_to_cancel);
						}
//						mTextCancel.setVisibility(View.GONE);
						mVoiceProgressBar.setVisibility(View.VISIBLE);
						mVoiceProgressBar2.setVisibility(View.GONE);
						mTalkBtn.setSelected(false);

						mVoiceTime.setTextColor(getResources().getColor(R.color.log_blue));

						mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
					} else {
						mTextCancel.setText(R.string.cancel_voice);
						mTextCancel.setVisibility(View.VISIBLE);
						mTalkBtn.setSelected(true);
						mVoiceProgressBar.setVisibility(View.GONE);
						mVoiceProgressBar2.setVisibility(View.VISIBLE);
						mVoiceTime.setTextColor(getResources().getColor(R.color.color_voice_red));
						mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_red));

					}

					break;

				case MotionEvent.ACTION_UP:

					PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----");

					if (mVoiceTime != null && mVoiceTime.isShown() && !sent || (Calendar.getInstance().getTimeInMillis() - mCurrentClickTime <= LONG_PRESS_TIME)) {

						PalmchatLogUtils.e(TAG, "----MotionEvent.ACTION_UP----MessagesUtils.MSG_VOICE");
						if (CommonUtils.getSdcardSize()) {
							ToastManager.getInstance().show(context, R.string.memory_is_full);
						} else {

							float offsetX = Math.abs(event.getX() - mDownX);
							float offsetY = Math.abs(event.getY() - mDownY);

							if (offsetX < mTalkBtn.getWidth() / 2 && offsetY < mTalkBtn.getHeight() / 2) {
								if (recordStart > 0) {
									mHandler.sendEmptyMessage(MessagesUtils.MSG_VOICE);

								}
							} else {
								mTextCancel.setText(R.string.empty);
								ToastManager.getInstance().show(context, R.string.cancelled);
								mHandler2.removeCallbacksAndMessages(null);
							}
						}

						stopRecording();
						stopRecordingTimeTask();

						setMode(SharePreferenceService.getInstance(ActivitySendBroadcastMessage.this).getListenMode());

						mVoiceTime.setVisibility(View.INVISIBLE);
						mTimer.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mVoiceTime.setVisibility(View.VISIBLE);
								if (mTextCancel.getText().equals(getString( R.string.empty))) {
									mVoiceTime.setText("0.0s");
								}
								if (!mTextCancel.getText().equals(getString( R.string.call_Voice_Tips))) {
//                                        mTextCancel.setVisibility(View.GONE);
									mTextCancel.setText(R.string.call_Voice_Tips);
								}
//								mTextCancel.setVisibility(View.GONE);
							}
						}, 1000);

						mSendVoiceView.setBackgroundColor(PalmchatApp.getApplication().getResources().getColor(R.color.brdcast_voice_blue));
						mTalkBtn.setSelected(false);
						// show cancelled text

					}
					break;
				}
			}
			return false;
		}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
		if (resultCode == SHOW_VIOICE_FUNCTIONTIPS) {
			if (data != null) {
				if (vImageVoice.isClickable()) {
					AppFunctionTips_pop_Utils.getInit().showFunctionTips_pop(ActivitySendBroadcastMessage.this, vImageVoice, R.string.send_bc_voice_functiontips, R.drawable.bg_functiontips_voice);
				}
			}

		}
	}


	private void setEdittextListener(CharSequence s) {
		int length = s.length();
		if (editListener.edittext_length > length) {
			isDeleteSymbol();
		} else if (editListener.edittext_length < length) {

		}

	}

	private boolean isDeleteSymbol() {
		int  selectIndex=mMessageEdit.getSelectionStart();
		if (editListener.edittext_string != null && editListener.edittext_string.length() >= 2&&selectIndex>0&&selectIndex+1<=editListener.edittext_string.toString().length()) {
			String str = editListener.edittext_string.toString().substring(0, selectIndex +1);
			String tempStr =  str.substring(str.length() - 1);
			if ("]".equals(tempStr)) {
				int sIndex=str.lastIndexOf("[");
				int eIndex=str.lastIndexOf("]") + 1;
				String ttt = str.substring(sIndex, eIndex);
				if (!TextUtils.isEmpty(ttt) && ttt.length() > 2) {
					boolean isDelete = EmojiParser.getInstance(PalmchatApp.getApplication()).isDefaultEmotion(mMessageEdit,  ttt);
					 return isDelete;
				}
			}
		}

		return false;
	}

	public void emojj_del() {
		CommonUtils.isDeleteIcon(R.drawable.emojj_del, mMessageEdit);
	}

	/**
	 * 设置禁止換行监听器
	 */
	private void setListener(EditText editText) {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null) {
					return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE);
				}
				return true;
			}
		});
	}


	/**
	 * 控制按钮是否可点击
	 * 
	 * @param send
	 * @param camera
	 * @param pic
	 * @param voice
	 */
	private void setBtnClickable(boolean send, boolean camera, boolean pic, boolean voice) {
		vImageSend.setClickable(send);
		if (broadcast_type == Consts.BR_TYPE_VOICE_TEXT) {
			vImageVoice.setClickable(voice);

			if (voice)
				vImageVoice.setBackgroundResource(R.drawable.voice_icon_selector);
			else
				vImageVoice.setBackgroundResource(R.drawable.voice_icon_lock);
		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCallbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(mCallbackManager, mCallback);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 为了防止 由于facebook 解除授权,所以在这边刷新下token
		AccessToken.refreshCurrentAccessTokenAsync(mAccessTokenRefreshCallback);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 创建分享对话框
	 */
	private void createFacebookShareDialog() {
		if (isFinishing()) {
			return;
		}

		AppDialog dialog = new AppDialog(context);
		String str = getResources().getString(R.string.facebook_login_tip);
		dialog.createConfirmDialog(context, "", str, new OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						LoginManager.getInstance().logInWithPublishPermissions(ActivitySendBroadcastMessage.this, Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
					}
				}).start();
			}

			@Override
			public void onLeftButtonClick() {
				//PalmchatApp.getApplication().setFacebookShareClose(true);
				mSBt_Facebook.setChecked(false);
			}
		});

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(CommonUtils.hasPublishPermission()){
					mSBt_Facebook.setChecked(true);
				}else{
					mSBt_Facebook.setChecked(false);
				}
			}
		});
		dialog.show();

	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		String tag = "";
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_BCGET_DEFAULT_TAGS:

				if (result != null && result instanceof AfResponseComm.AfTagGetDefaultTagsResp) {
					AfTagGetDefaultTagsResp tags = (AfResponseComm.AfTagGetDefaultTagsResp) result;
					if (tags != null && !tags.tags_list.isEmpty()) {
						mDefaultNetList.clear();
						mDefaultTagMap.clear();
						mDefaultNetList.addAll(tags.tags_list);
						mTagsShowList.addAll(mDefaultNetList);
						for (String defaultTag : mDefaultNetList) {
							mDefaultTagMap.put(defaultTag, true);
						}
						PalmchatLogUtils.i(TAG, "--net---defaultTags-==" + tags.tags_list.toString());
					}
				}
				if (mTagsShowList != null) {
					setFlowlayout();
				}
				getFlowTagFromDict();
				break;

			case Consts.REQ_BCGET_LANGUAGE_PACKAGE: {
				doResultOfCurrentTags(result);
				break;
			}

			}

		} else {

		}
	}

	/**
	 * 开个线程处理tags更新
	 * 
	 * @param result
	 */
	private void doResultOfCurrentTags(final Object result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AfTagGetLangPackageResp afResponseComm = null;
				int ret = 0;
				try {
					afResponseComm = (AfTagGetLangPackageResp) result;
				} catch (Exception e) {
					e.printStackTrace();
				}

				if ((null != afResponseComm) && (null != afResponseComm.language_ver)) {
					PalmchatLogUtils.i(TAG, "tag--download===ver===" + afResponseComm.language_ver);
					SharePreferenceUtils.getInstance(ActivitySendBroadcastMessage.this).setCurrentTagsVersion(afResponseComm.language_ver);
				}

				if ((null != afResponseComm.local_list) || (null != afResponseComm.default_list)) {
					mAfCorePalmchat.AfDbBCLangPackageListDeleteAll();
				}
				if (null != afResponseComm.default_list) { //
					String[] array = (String[]) afResponseComm.default_list.toArray(new String[afResponseComm.default_list.size()]);
					ret = mAfCorePalmchat.AfDbBCLangPackageListInsert(array, (byte) 0);

				}

				if (null != afResponseComm.local_list) { //
					String[] array = (String[]) afResponseComm.local_list.toArray(new String[afResponseComm.local_list.size()]);
					ret = mAfCorePalmchat.AfDbBCLangPackageListInsert(array, (byte) 1);
				}
			}
		}).start();
	}

	private void getTagsFromDict() {
		ArrayList<String> tagsShowList = new ArrayList<String>();
		AfResponseComm.AfTagGetLangPackageResp random = mAfCorePalmchat.AfDbBCLangPackagGetLimitListDb(0, 0, DefaultValueConstant.SENDBROADCASTTAGSFROMDICT);
		int index = (int) (Math.random() * 19);
		if (random != null) {
			ArrayList<AfResponseComm.AfTagLangPackageItem> randomList = random.db_list;
			if (randomList != null) {
				for (int i = 0; i < DefaultValueConstant.SENDBROADCASTTAGFROMTAGS; i++) {
					String tagName = randomList.get((index++) % randomList.size()).tag_name;
					if (mDefaultTagMap.containsKey(tagName)) {
						PalmchatLogUtils.i(TAG, "--dict---defaultTags-==" + tagName.toString() + "---index-----" + index + "--is--repetition---");
						i--;
						continue;
					}
					PalmchatLogUtils.i(TAG, "--dict---defaultTags-==" + tagName.toString() + "---i-----" + i);
					tagsShowList.add(tagName);
				}
				Message msg = Message.obtain();
				msg.what = LooperThread.DEAL_TAGS_DICT;
				msg.obj = tagsShowList;
				mHandler.sendMessage(msg);
			} else {
				mAfCorePalmchat.AfHttpAfBCLangPackage("", ActivitySendBroadcastMessage.this);
			}
		} else {
			mAfCorePalmchat.AfHttpAfBCLangPackage("", ActivitySendBroadcastMessage.this);
		}
	}
	 
	/**
	 * add by zhh 设置文本输入框中tag颜色
	 * 
	 * @param strText
	 */
	private void parseEdtText(String strText) {
		int  _sIndex = mMessageEdit.getSelectionStart();
		
		if(isNeedParseAllText){
			_sIndex=-1;
			isNeedParseAllText=false;//重新切换回 部分匹配模式 增加处理tag和表情的速度
		}
		  EmojiParser.getInstance(context).parseEdtEmojAndTag(context, strText,mMessageEdit,_sIndex);
//		mMessageEdit.setText(text);
//		mMessageEdit.setSelection(_sIndex);
			 
		
		 
	}

	/** fackbook回调 */
	private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
		@Override
		public void onSuccess(LoginResult loginResult) {
			PalmchatLogUtils.i(TAG, "--FacebookCallback---onSuccess----" + loginResult.toString());
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);
		}

		@Override
		public void onCancel() {
			PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
			
		}

		@Override
		public void onError(FacebookException exception) {
			PalmchatLogUtils.i(TAG, "--FacebookCallback---onError----" + exception.toString());
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
		}
	};

	private AccessTokenRefreshCallback mAccessTokenRefreshCallback = new AccessTokenRefreshCallback() {

		@Override
		public void OnTokenRefreshed(AccessToken accessToken) {
			if (!CommonUtils.hasPublishPermission()) {
				mSBt_Facebook.setChecked(false);
				//PalmchatApp.getApplication().setFacebookShareClose(true);
				mFacebookShareInit = false;
			}
		}

		@Override
		public void OnTokenRefreshFailed(FacebookException exception) {
			PalmchatLogUtils.i(TAG, "----OnTokenRefreshFailed----" + exception.toString());
		}

	};

}
