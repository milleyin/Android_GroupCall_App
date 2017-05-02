package com.afmobi.palmchat.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.eventbusmodel.BlockFriendEvent;
import com.afmobi.palmchat.eventbusmodel.ChangeCountryEvent;
import com.afmobi.palmchat.eventbusmodel.ChatRoomListEvent;
import com.afmobi.palmchat.eventbusmodel.EventLoginBackground;
import com.afmobi.palmchat.eventbusmodel.EventPalmcallNotication;
import com.afmobi.palmchat.eventbusmodel.LocalsFilterEvent;
import com.afmobi.palmchat.eventbusmodel.UpdateCoinsEvent;
import com.afmobi.palmchat.eventbusmodel.UploadHeadEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.friends.ContactsAddActivity;
import com.afmobi.palmchat.ui.activity.friends.GroupListActivity;
import com.afmobi.palmchat.ui.activity.friends.LocalSearchActivity;
import com.afmobi.palmchat.ui.activity.main.ChatsContactsTab;
import com.afmobi.palmchat.ui.activity.main.UpdateStateActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallFragment;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallRecentsActivity;
import com.afmobi.palmchat.ui.activity.palmcall.PalmCallSettingActivity;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallLoginDelegate;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallManager;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.qrcode.activity.CaptureActivity;
import com.afmobi.palmchat.ui.activity.setting.ExploreFragment;
import com.afmobi.palmchat.ui.activity.setting.SettingsActivity;
import com.afmobi.palmchat.ui.activity.setting.UpdateVersion;
import com.afmobi.palmchat.ui.activity.social.ActivitySendBroadcastMessage;
import com.afmobi.palmchat.ui.activity.social.AreaListAdapter;
import com.afmobi.palmchat.ui.activity.social.BroadcastAreaActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastFragment;
import com.afmobi.palmchat.ui.activity.social.BroadcastNotificationActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastTab;
import com.afmobi.palmchat.ui.activity.social.EditBroadcastPictureActivity;
import com.afmobi.palmchat.ui.activity.social.HomeFragment;
import com.afmobi.palmchat.ui.activity.social.ShakeFragmentActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnGiveScoreListener;
import com.afmobi.palmchat.ui.customview.CustomViewPager;
import com.afmobi.palmchat.ui.customview.SendBroadBtnListener;
import com.afmobi.palmchat.ui.customview.SendBroadcastMaskingView;
import com.afmobi.palmchat.ui.customview.TopDialog;
import com.afmobi.palmchat.ui.customview.UnderlinePageIndicator;
import com.afmobi.palmchat.util.AnimationController;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.KeyDownUtil;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmCallResp;
import com.core.AfPalmchat;
import com.core.AfPaystoreCommon;
import com.core.AfPaystoreCommon.AfStoreDlProdInfo;
import com.core.AfPaystoreCommon.AfStoreDlProdInfoList;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfTagGetLangPackageResp;
import com.core.AfStoreProdList;
import com.core.AfStoreProdList.AfPageInfo;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
import com.facebook.appevents.AppEventsLogger;
import com.justalk.cloud.lemon.MtcCallConstants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 *
 */
// import com.afmobi.palmchat.ui.activity.main.MainLeftFragment;
@SuppressWarnings("deprecation")
public class MainTab extends BaseFragmentActivity implements AfHttpResultListener, OnGiveScoreListener, AfHttpSysListener,
		OnClickListener, OnItemClickListener {

	public static final String TAG = MainTab.class.getSimpleName();

	public static final int CONTENT_FRAGMENT_BROADCAST = 0;
	public static final int CONTENT_FRAGMENT_HOME = 1;
	public static final int CONTENT_FRAGMENT_PALMCALL = 2;
	public static final  int CONTENT_FRAGMENT_CHAT = 3;
	public static final  int CONTENT_FRAGMENT_EXPLORE = 4;
	public static final int UPDATE_STATE_CITY = 1001;
	//在新手引导提示奖励的数值
	public static final int TIME = 100;
	private View   layout_home;
	private View  layout_broadcast;
	private View  layout_chat;
	private View   layout_explore;
	private View  layout_palmcall;
	private ImageView rightImageview, img_ico, filterImage;
	private TextView unReadText, mUnReadBrdNotify, profile_Name;
	private ImageView unReadText_HasNewStore;
	private ImageView mRightChatSearchBtn;
	private ImageView mUnReadBrdFriendCircleNotify;
	private ImageView me_back;
	private LinearLayout layout_loginLoading;
	public int currIndex = 0;



	public CustomViewPager viewpager;

	public View mTabsView;

	private PalmchatApp app;
	private AfPalmchat mAfCorePalmchat;
	public Context mContext;
	private boolean isLoginForground = false;//是否是前台登陆进来的
	public static final int STORE_HAS_NEW_PROINFO = 1;
	private byte mSex = Consts.AFMOBI_SEX_FEMALE_AND_MALE;
	private boolean mIsOnline, mIsNearby;
	/**由于有机器差异，不确定第一次一定是运行隐藏，所有给个标记初始化*/
	private boolean mIsFirstRun = true;
	/**发送按钮蒙**/
	private SendBroadcastMaskingView mSendBroadcastMaskingView;
	public ArrayList<Fragment> listFragments;
	MenuAdapter adapter;
	AddPopWindow addPopWindow;
	//改变国家的标识
	private boolean isChange;
	private TopDialog topDialog;
	private boolean mIsShowPalmCall;
	private Handler mainHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					if (typeTitleAnimation != 0) {
						RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ImageUtil.DISPLAYW, defTitleHeight);
						layout.topMargin = -(defTitleHeight - titleHeight);
						mTitleLinearLayout.setLayoutParams(layout);
					}
					break;
			}
		}
	};

	private View viewOffline;

	ExploreFragment exploreFragment;
	private BroadcastTab broadcastFragmentTab;
	private ChatsContactsTab chatFragment;
	private HomeFragment homeFragment;
	private PalmCallFragment palmCallFragment;

	/** 广播消息提示 铃铛图片 */
	private ImageView mIv_BroadcastNotification;
	/***/
	private TextView mTv_BroadcastNotificationCount;
	//未接电话显示
	private TextView mUnreadMsgRight;
	/** 标题栏判断是否显示未读标�?*/
	private boolean mIsShowNotificationCount;
	/**广播选择区域按钮*/
	private ImageView mBroadcastAreaSelect;

	/**点击here后弹出Dialog选择区域*/
	private Dialog mDialog;

	private AreaListAdapter mAreaListAdapter;

	private ListView mAreaListView;
	/**保存选择�?*/
	private int checkedIndex = -1;

	private String myAfid;
	/**tabs原始坐标*/
	private float mTabsYCoord;
	private float mTitleYCoord;
	/**可见高度*/
	private float mTitleRealHeight;
	/**标题栏高**/
	private int mStatusBarHeight=-1;
	/**guide*/
	private View mV_Fab_guide;
	/**引导页按**/
	private ImageButton mBtn_Sendbroad_guide;
	private UnderlinePageIndicator indicator;
	private View mRL_RegionSelectTip;

	/**PalmCall引导页**/
	private View mV_PalmCall_Main,mV_PalmCall_Recent,mV_PalmCall_Recharge,mV_PalmCall_Intro,mV_PalmCall_Main_Intro;
	/**PalmCall引导页**/
	private TextView mTv_Call_Recharge_Often;
	private RelativeLayout mRl_Guide_intro,mRl_intro;
	/*zhh 用于保存当前发送的充值统计文件*/
	HashMap<String, String> fileMap;
	/**
	 * PalmCall引导页显示第几个
	 */
	private int mPalmCallClickCount;
	private LinearLayout.LayoutParams mColLayoutParams;
	/**PalmCall引导页**/
	private Runnable mRunnable = new Runnable() {
		public void run() {
			mainHandler.removeCallbacks(mRunnable);
			showTipHideAnimation();
		}
	};
	public BroadcastFragment getbroadcastFragment() {
		return null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		showChatFragment(intent);
		resetTitlePosition();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_main_tab);
		app = PalmchatApp.getApplication();
		findViews();
		init();
		// InitWidth();
		showSelectedOption(CONTENT_FRAGMENT_BROADCAST);
		viewpager.setCurrentItem(CONTENT_FRAGMENT_BROADCAST);

		showChatFragment(getIntent());

		registerBroadcastReceiver();
		// gtf 2014-11-16
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_HOME);
//		MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_HOME);

		MyActivityManager.getScreenManager().popAllActivityExceptOne(MainTab.class);

		Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
		int w = getIntent().getIntExtra("display_width", defaultDisplay.getWidth());
		int h = getIntent().getIntExtra("display_height", defaultDisplay.getHeight());
		PalmchatLogUtils.println("wwwwwwwww:" + w + "  hhhhhhhhh:" + h);
		if (CommonUtils.isHD(w, h)) {
			super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			if (w > h) {
				super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}


		judgeSkipEditBroadcast();
	}

	/*public boolean getIsLogin() {
		return isLogin;
	}*/

	OfflineReceiver offlineReceiver;

	private void registerBroadcastReceiver() {

		offlineReceiver = new OfflineReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// filter2.addAction(Constants.REFRESH_CHATS_ACTION);

		registerReceiver(offlineReceiver, filter2);
		EventBus.getDefault().register(this);
	}

	private void unRegisterBroadcastReceiver() {
		unregisterReceiver(offlineReceiver);
		EventBus.getDefault().unregister(this);
	}

	private void showChatFragment(Intent intent){
		if(intent != null) {
			boolean toMain = intent.getBooleanExtra(JsonConstant.KEY_TO_MAIN, false);
			if (toMain) {
				CommonUtils.cancelNoticefacation(getApplicationContext());
				// show ChatFragment
				int index = CONTENT_FRAGMENT_CHAT;
				if (!PalmchatApp.getApplication().isCountry()) {
					index = --index;
				}
				viewpager.setCurrentItem(index);
			}
		}
	}


	private   final int DAY1 = 1;//
	private   final int DAY2 = 3;
	private   final int DAY3 = 7;
	private   final int SHOW_TIMES = 3;
	private static final int FIRST_TIME = 1;
	private static final int SECOND_TIME = 2;
	private static final int THIRD_TIME = 3;
	/**
	 * 每次登陆后 如果是第1 3 7 天登陆 提示绑定Email
	 *
	 * @param context
	 */
	public void showBindEmailDialog(MainTab context) {
		// TODO Auto-generated method stub
		AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
		String currentAfid = profile.afId;
		String email = profile.email;
		boolean isBindEmail = profile.is_bind_email;
		PalmchatLogUtils.println("PalmChatApp  showBindEmailDialog  currentAfid  " + currentAfid + "  email  " + email + "  isBindEmail  " + isBindEmail);
		if (CommonUtils.isEmpty(currentAfid) || CommonUtils.isEmpty(email) || isBindEmail) {
			return;
		}
		int times = (int) mAfCorePalmchat.AfDbSettingGetEmailOrCount(currentAfid, false);
		long lastShowTime = mAfCorePalmchat.AfDbSettingGetEmailOrCount(currentAfid, true);
		long currentTime = System.currentTimeMillis();
		/*MainTab mainAct = context;
		boolean isNewUser = mainAct.isNewUser();
		PalmchatLogUtils.println("PalmchatApp  isNewUser  " + isNewUser);
		if (isNewUser) {
			mainAct.setNewUser(false);
			mAfCorePalmchat.AfDbSettingSetEmailOrCount(currentAfid, currentTime, true);
			return;
		}*/
		times++;
		toShowEmailDialog(context, email, isBindEmail, times, lastShowTime, currentTime, currentAfid);
	}

	/*private int getIndex(){

	}*/

	/**
	 * 显示电子邮件对话框
	 *
	 * @param context
	 * @param email
	 * @param isBindEmail
	 * @param times
	 * @param lastShowTime
	 * @param currentTime
	 * @param currentAfid
	 */
	private void toShowEmailDialog(MainTab context, String email, boolean isBindEmail, int times, long lastShowTime, long currentTime, String currentAfid) {
		if (!isBindEmail && !TextUtils.isEmpty(email)) {// not email
			long diff = currentTime - lastShowTime; // compare last time
			long days = diff / (1000 * 60 * 60 * 24);//
			PalmchatLogUtils.println("PalmchatApp  toShowEmailDialog  days  " + days + "  times  " + times);
			if (times <= SHOW_TIMES) {
				switch (times) {
					case FIRST_TIME:
						if (days >= DAY1) {
							showEmailDialog(context, times, email, currentAfid);
						}
						break;
					case SECOND_TIME:
						if (days >= DAY2) {
							showEmailDialog(context, times, email, currentAfid);
						}
						break;
					case THIRD_TIME:
						if (days >= DAY3) {
							showEmailDialog(context, times, email, currentAfid);
						}
						break;
				}
			}
		}
	}

	/**
	 * 显示电子邮件对话框
	 *
	 * @param context
	 * @param times
	 * @param email
	 * @param currentAfid
	 */
	private void showEmailDialog(final MainTab context, int times, final String email, String currentAfid) {
		AppDialog appDialog = new AppDialog(context);
		String msg = context.getResources().getString(R.string.email_dialog_messages);
		appDialog.createConfirmDialog(context, msg, new AppDialog.OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
				String countryCode = PalmchatApp.getOsInfo().getCountryCode();
				context.showProgressDialog(R.string.Sending);
				mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_BIND_BY_EMAIL, email, countryCode, Consts.HTTP_PARAMS_TYPE_TRUE, null, null, 0, context);
			}

			@Override
			public void onLeftButtonClick() {

			}
		});
		appDialog.show();
		mAfCorePalmchat.AfDbSettingSetEmailOrCount(currentAfid, times, false);
		long currentTime = System.currentTimeMillis();
		mAfCorePalmchat.AfDbSettingSetEmailOrCount(currentAfid, currentTime, true);
		long count = mAfCorePalmchat.AfDbSettingGetEmailOrCount(currentAfid, false);
		int timesAfter = (int) count;
		PalmchatLogUtils.println("PalmchatApp  showEmailDialog  count  " + count + "  timesAfter  " + timesAfter);
	}

	/**
	 * 完整资料界面 改了头像�?进MainTab �?头像才上传完�?收到这个通知 更新头像
	 *
	 * @param event
	 */
	public void onEventMainThread(UploadHeadEvent event) {
		updateHead();
	}



	/**
	 * 后台登陆完成事件 前台登陆完成不会调这
	 *
	 * @param event
	 */

	public void onEventMainThread(EventLoginBackground event) {
		if (event.getIsLoginSuccess()) {
			/*if (event.getIsShowPalmGuessNew() || SharePreferenceUtils.getInstance(this).getUnReadNewPalmGuess()) {
				showNew();
			}*/
			/**
			 * 这里是为了再后台登录完成�?再告诉一次中间件 当前的账号情�?2015 11 15跟运祥沟通后 确认登录后也要设置一�?
			 * （之前只有登录前 和非后台登陆有）这里要加�?  是为了同步好友列表等  (因为会遇到换手机后台登陆的情况，这个时候fdsn等本地存的就不是最新的了）
			 */
			AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
			AfLoginInfo login =CacheManager.getInstance().getLoginInfo();//new AfLoginInfo();

			PalmchatLogUtils.e("WXL", "third_account="+login.third_account+"login.type="+login.type
					+"login.password="+login.password
					+"myProfile.password="+myProfile.password
					+"myProfile.name="+myProfile.name
					+" myProfile.fdsn"+myProfile.fdsn+
					""
			);

			login.afid = myProfile.afId;
			login.password = myProfile.password;
			login.cc = PalmchatApp.getOsInfo().getCountryCode();
			login.email = myProfile.email;
			login.fdsn = myProfile.fdsn;
			login.blsn = myProfile.blsn;
			login.gpsn = myProfile.gpsn;
			login.phone = myProfile.phone;
			login.pbsn = myProfile.pbsn;
			login.mcc = PalmchatApp.getOsInfo().getAfid_mcc();
			/*login.third_account = myProfile.third_account;
			login.type =myProfile.login_type; */

			mAfCorePalmchat.AfLoadAccount(login);// 这里是为了再后台登录完成�?再告诉一次中间件
			// 当前的账号情�?2015 11 15跟运祥沟通后
			// 确认登录后也要设置一�?（之前只有登录前
			// 和非后台登陆有）这里要加�?  是为了同步好友列表等

			updateHead();
			// 取消loding状�?
			cancelLoading();
			/* 弹出修改state提示�?*/
			showUpdateRegionDialog();

			doAfterLoginFinishDelay2Mins();
			CacheManager.getInstance().setLoginSuccess(true);
			showBindEmailDialog(this);//后台登陆完成后 才去检测是否需要提示 绑定Email账号。 前台登陆不提示
			if(palmCallFragment != null){
				PalmchatLogUtils.i("MainTab","EventLoginBackground Show PalmCall");
				palmCallFragment.setListviewHeader(true);
			}
			showPalmCall();
		} else {
			cancelLoading();
			if(palmCallFragment != null &&  palmCallFragment.isAdded()){
				palmCallFragment.setListviewHeader(false);
			}
		}
		PalmchatLogUtils.i("ActivityMainTab","EventLoginBackground");
	}


	public void onEventMainThread(BlockFriendEvent mBlockFriendEvent) {
		chatFragment.refreshContactsList(mBlockFriendEvent);
	}

	/**
	 * 消失标题loading状�?
	 */
	private void cancelLoading() {
		if (me_back.getVisibility() != View.VISIBLE) {
			me_back.setVisibility(View.VISIBLE);
			profile_Name.setVisibility(View.VISIBLE);
			layout_loginLoading.setVisibility(View.GONE);
		}
	}

	public Fragment getFragment(int tag) {
		return adapter.getItem(tag);
	}

	/**
	 *
	 */
	private void init() {
		// TODO Auto-generated method stub
		initScrollData();
		mContext = this;
		mAfCorePalmchat = app.mAfCorePalmchat;
		Display defaultDisplay;
		int w;
		int h;
		myAfid = CacheManager.getInstance().getMyProfile().afId;
		defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
		mTabsYCoord = defaultDisplay.getHeight();
		w = getIntent().getIntExtra("display_width", defaultDisplay.getWidth());
		h = getIntent().getIntExtra("display_height", defaultDisplay.getHeight());

//		isNewUser = getIntent().getBooleanExtra(JsonConstant.KEY_FLAG, false);
		isLoginForground = getIntent().getBooleanExtra(JsonConstant.KEY_IS_LOGIN, false);


		boolean isNetUnavilable = getIntent().getBooleanExtra(JsonConstant.KEY_NET_UNAVAILABLE, false);
		if (isNetUnavilable) {
			CommonUtils.cancelNetUnavailableNoticefacation();
		}

		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		mIsShowPalmCall = PalmchatApp.getApplication().isCountry();
		listFragments = new ArrayList<Fragment>();
		homeFragment = new HomeFragment();
		broadcastFragmentTab = new BroadcastTab();
		chatFragment = new ChatsContactsTab();
		exploreFragment = new ExploreFragment();
		listFragments.add(broadcastFragmentTab);
		listFragments.add(homeFragment);

		if(mIsShowPalmCall) {//是否包含PalmCall
			PalmchatLogUtils.i("ActivityMainTab","addPalmCall");
			palmCallFragment = new PalmCallFragment();
			listFragments.add(palmCallFragment);
			PalmchatLogUtils.d("mengjie","创建Fragment了吗");
		}

		listFragments.add(chatFragment);
		listFragments.add(exploreFragment);
		adapter = new MenuAdapter(getSupportFragmentManager(), listFragments);
		viewpager.setAdapter(adapter);

		initHeight();

		viewpager.setOffscreenPageLimit(4);

		if (isLoginForground) {// 前台登陆页进�?  ;
//			setPalmGuessVersion(myProfile.palmguess_version);
			showUpdateRegionDialog();
			doAfterLoginFinishDelay2Mins();
//			setLoadAccount(myProfile,isLoginForground);//告诉中间件要保存的账号和登陆方式
			CacheManager.getInstance().setLoginSuccess(true);
			isShowPalmcall(mIsShowPalmCall,true);
		} else if (mAfCorePalmchat.AfHttpGetServerInfo() == null || TextUtils.isEmpty(mAfCorePalmchat.AfHttpGetServerInfo()[1])) {// 如果是后台登陆的
			// 显示loading状�?
			me_back.setVisibility(View.GONE);
			profile_Name.setVisibility(View.GONE);
			layout_loginLoading.setVisibility(View.VISIBLE);
			Bundle bundle = new Bundle();
			bundle.putBoolean(JsonConstant.KEY_IS_BACKGROUD_LOGIN,true);
			if(palmCallFragment != null) {
				palmCallFragment.setArguments(bundle);
			}
		/*	if(palmCallFragment != null && palmCallFragment.isAdded()){
				palmCallFragment.setListviewHeader(false);
				PalmchatLogUtils.i("MainTab","palmCallFragment setListViewHeader");
			}*/
			isShowPalmcall(mIsShowPalmCall,false);
			PalmchatLogUtils.i("MainTab","NOT isLoginForground");
		}
		//隐藏或显示PalmCall选项

		if (w > h) {
			super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		String commonString = getScreenQuality(MainTab.this, w, h);
		CacheManager.getInstance().setScreenString(commonString);

		String myAfid = myProfile.afId;

		if (!TextUtils.isEmpty(myAfid)) {
			EmojiParser.getInstance(mContext).readGif(mContext, myAfid, null, null);
			mAfCorePalmchat.AfDbLoginSetStatus(myAfid, AfLoginInfo.AFMOBI_LOGIN_INFO_LOGINED);
		}
		SharePreferenceUtils.getInstance(this).setIsClosePalmchat(false);
		profile_Name.setText(myProfile.name);

		filterImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PalmchatLogUtils.d("mengjie","checkPositionByChannel点击了");
				switch (checkPositionByChannel(currIndex)) {
					case CONTENT_FRAGMENT_HOME:
						// switchHomeGrid();

						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_FILT);
//					MobclickAgent.onEvent(MainTab.this, ReadyConfigXML.ENTRY_FILT);
						topDialog = new TopDialog(MainTab.this);
						topDialog.createHomeFilterDialog(mSex, mIsOnline, mIsNearby, CacheManager.getInstance().getMyProfile().region, new TopDialog.OnOkButtonDialogListener() {
							@Override
							public void onOkButtonClick(Bundle bundle) {
								if (null != bundle) {
									mSex = bundle.getByte(TopDialog.M_SEX);
									mIsOnline = bundle.getBoolean(TopDialog.IS_ONLINE);
									mIsNearby = bundle.getBoolean(TopDialog.IS_NEARBY);
									if (mSex == Consts.AFMOBI_SEX_FEMALE) {
										new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FILT_FM);
//									MobclickAgent.onEvent(MainTab.this, ReadyConfigXML.FILT_FM);
									} else if (mSex == Consts.AFMOBI_SEX_MALE) {
										new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FILT_M);
//									MobclickAgent.onEvent(MainTab.this, ReadyConfigXML.FILT_M);
									}
								}
								EventBus.getDefault().post(new LocalsFilterEvent(mSex,mIsOnline,mIsNearby));

							}
						});
						topDialog.show();

						break;
					case CONTENT_FRAGMENT_PALMCALL:
						startActivity(new Intent(MainTab.this,PalmCallSettingActivity.class));
						break;
					case CONTENT_FRAGMENT_BROADCAST:

						break;
					case CONTENT_FRAGMENT_CHAT:
						addPopWindow = new AddPopWindow(MainTab.this);
						addPopWindow.setOnDismissListener(new OnDismissListener() {

							@Override
							public void onDismiss() {
								backgroundAlpha(1f);

							}
						});
						addPopWindow.showPopupWindow(filterImage);

						break;

					default:
						break;
				}
			}
		});

		indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(viewpager);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources().getColor(R.color.log_blue));
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				showSelectedOption(position);
				typeTitleAnimation=2;
				showTitleOption(position);
				if (listFragments != null) {
					listFragments.get(CONTENT_FRAGMENT_BROADCAST).onStop();
				}
				currIndex = position;
				boolean isPlay = VoiceManager.getInstance().isPlaying();
				if (isPlay) {
					VoiceManager.getInstance().pause();
				}

				/*updateBadge(currIndex, false);*/
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
//		PalmchatApp.getApplication().setOnRefreshAtMeListener(this);
		String versionName = SharePreferenceUtils.getInstance(this).getCurrentVersionnameByAccount(CacheManager.getInstance().getMyProfile().afId);
		if(!versionName.equals(AppUtils.getAppVersionName(this))){
			mV_Fab_guide.setVisibility(View.VISIBLE);
			setFabButton(false);
		} else {
			mV_Fab_guide.setVisibility(View.GONE);
			setFabButton(true);
		}


//		  touchSlop = (int) (ViewConfiguration.get(this).getScaledTouchSlop() * 0.9);//滚动过多少距离后才开始计算是否隐�?显示头尾元素。这里用了默认touchslop�?.9倍�?
	}

	/**
	 * 初始化滑动的数据
	 */
	private void initScrollData(){
		mTabsYCoord = 0;
		mTitleYCoord = 0;
		mTitleRealHeight = 0;
		mStatusBarHeight=-1;
	}

	/** 显示标题选项
	 * @param position
	 */
	private void showTitleOption(int position){
		switch (checkPositionByChannel(position)) {
			case CONTENT_FRAGMENT_HOME:
				// gtf 2014-11-16
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_HOME);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_HOME);
				mRightChatSearchBtn.setVisibility(View.GONE);
				rightImageview.setVisibility(View.GONE);
				filterImage.setVisibility(View.VISIBLE);
				filterImage.setBackgroundResource(R.drawable.broadcast_area_actionbar);
				mIv_BroadcastNotification.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setVisibility(View.GONE);
				mBroadcastAreaSelect.setVisibility(View.GONE);
				mUnreadMsgRight.setVisibility(View.GONE);
				mV_PalmCall_Main.setVisibility(View.GONE);
				// AppFunctionTips_pop_Utils.getInit().dismiss();
				break;


			case CONTENT_FRAGMENT_BROADCAST:
				// gtf 2014-11-16
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_BCM);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_BCM);

				filterImage.setVisibility(View.GONE);
				rightImageview.setVisibility(View.GONE);
				mRightChatSearchBtn.setVisibility(View.GONE);
//					mBroadcastAreaSelect.setVisibility(View.VISIBLE);
				mIv_BroadcastNotification.setVisibility(View.VISIBLE);
				if (mIsShowNotificationCount) {
					mTv_BroadcastNotificationCount.setVisibility(View.VISIBLE);
				}
				mUnreadMsgRight.setVisibility(View.GONE);
				mV_PalmCall_Main.setVisibility(View.GONE);
				break;
			case CONTENT_FRAGMENT_CHAT:
				// gtf 2014-11-16
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_CL);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_CL);
				// lookaround_filter.setVisibility(View.GONE);
				filterImage.setVisibility(View.VISIBLE);
				filterImage.setBackgroundResource(R.drawable.btn_chat_more);
				rightImageview.setVisibility(View.VISIBLE);
				rightImageview.setBackgroundResource(R.drawable.selector_right_search);
				mRightChatSearchBtn.setVisibility(View.GONE);
				mIv_BroadcastNotification.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setVisibility(View.GONE);
				mBroadcastAreaSelect.setVisibility(View.GONE);
				mUnreadMsgRight.setVisibility(View.GONE);
				// broadcastFragment.close_inputbox(true);
//						AppFunctionTips_pop_Utils.getInit().dismiss();
//						typeTitleAnimation=2;
				mV_PalmCall_Main.setVisibility(View.GONE);

				break;
			case CONTENT_FRAGMENT_PALMCALL:

				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_BCM);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_BCM);

				filterImage.setVisibility(View.VISIBLE);
				filterImage.setBackgroundResource(R.drawable.btn_call_settings_selector);
				rightImageview.setVisibility(View.VISIBLE);
				rightImageview.setBackgroundResource(R.drawable.img_ic_history_more);
				mRightChatSearchBtn.setVisibility(View.GONE);
//					mBroadcastAreaSelect.setVisibility(View.VISIBLE);
				mIv_BroadcastNotification.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setVisibility(View.GONE);
				if(SharePreferenceUtils.getInstance(mContext).getMissedCalls()){
					mUnreadMsgRight.setVisibility(View.VISIBLE);
				}
				break;
			case CONTENT_FRAGMENT_EXPLORE:
				// gtf 2014-11-16
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_XEP);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_XEP);
				// lookaround_filter.setVisibility(View.GONE);
				filterImage.setVisibility(View.GONE);
				rightImageview.setVisibility(View.GONE);
				mRightChatSearchBtn.setVisibility(View.VISIBLE);
				mRightChatSearchBtn.setBackgroundResource(R.drawable.setting_selector);
				mIv_BroadcastNotification.setVisibility(View.GONE);
				mBroadcastAreaSelect.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setVisibility(View.GONE);
				mUnreadMsgRight.setVisibility(View.GONE);
				mV_PalmCall_Main.setVisibility(View.GONE);
				// broadcastFragment.close_inputbox(true);
//						AppFunctionTips_pop_Utils.getInit().dismiss();
//						typeTitleAnimation=2;


				break;
			default:
				// lookaround_filter.setVisibility(View.GONE);
				filterImage.setVisibility(View.GONE);
				rightImageview.setVisibility(View.GONE);
				mRightChatSearchBtn.setVisibility(View.GONE);
				mIv_BroadcastNotification.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setVisibility(View.GONE);
				mBroadcastAreaSelect.setVisibility(View.GONE);
				typeTitleAnimation=2;
				mUnreadMsgRight.setVisibility(View.GONE);
				mV_PalmCall_Main.setVisibility(View.GONE);

				break;
		}
	}

	/**
	 * 设置Palmguess版本并显示新活动提示
	 *
	 * @param
	 */
	/*public void setPalmGuessVersion(int version) {
		String name = CacheManager.getInstance().getMyProfile().afId;
		int ver = SharePreferenceUtils.getInstance(this).getPalmguessVersion(name);
		if (ver < version) {
			SharePreferenceUtils.getInstance(this).setPalmguessVersion(name, version);
			SharePreferenceUtils.getInstance(this).setUnReadNewPalmGuess(true);
		}
		showNew();
	}*/

	private void getStoreHaveNewProd() {
		String name = CacheManager.getInstance().getMyProfile().afId + "_store";
		if (CommonUtils.is_req(name, this)) {
			//mAfCorePalmchat.AfHttpStoreHaveNewProd(null, this);
			checkStroeNewVersion();

			SharePreferenceUtils.getInstance(this).setLastTime(name, System.currentTimeMillis());
		}
	}

	private int mOriginTabsTop;
	private int mOriginViewPagerTop;

	private void initHeight() {

		mTabsView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				mOriginTabsTop = mTabsView.getTop()+20;
				if (mOriginTabsTop > 0) {

					PalmchatLogUtils.println("onGlobalLayout viewpager.getHeight:" + viewpager.getHeight() + " ");
					mTabsView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});

		viewpager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				mOriginViewPagerTop = viewpager.getTop();
				if (mOriginViewPagerTop > 0) {
					viewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}

			}
		});

	}

	public void setChatUnread(int unread) {

		if (unReadText != null) {

			if (unread > 0) {
				unReadText.setVisibility(View.VISIBLE);
				unReadText.setText(unread > 99 ? "99+" : String.valueOf(unread));
			} else {
				unReadText.setVisibility(View.GONE);
			}

		}
	}

	/**
	 * 设置朋友圈有新广播时 在广播标签下设置红点
	 *
	 * @param isShow
	 */
	public void setBrdFriendCircleUnread(boolean isShow) {
		if (mUnReadBrdFriendCircleNotify != null) {
			if (isShow) {
				if (mUnReadBrdNotify.getVisibility() == View.GONE) {
					mUnReadBrdFriendCircleNotify.setVisibility(View.VISIBLE);
				}
			} else {
				if (mUnReadBrdFriendCircleNotify.getVisibility() == View.VISIBLE) {
					mUnReadBrdFriendCircleNotify.setVisibility(View.GONE);
				}
			}

		}
	}

	/**
	 * 设置广播栏有未读消息，显示红点和未读�?
	 *
	 * @param unread
	 */
	public void setBrdNotifyUnread(int unread) {
		if (mUnReadBrdNotify != null) {
			if (unread > 0) {
				String count = (unread > 99 ? "99+" : String.valueOf(unread));
				mUnReadBrdNotify.setVisibility(View.VISIBLE);
				mUnReadBrdNotify.setText(count);
				mTv_BroadcastNotificationCount.setText(count);
				mIsShowNotificationCount = true;
				if (mIv_BroadcastNotification.getVisibility() == View.VISIBLE) {
					mTv_BroadcastNotificationCount.setVisibility(View.VISIBLE);
				}

				if (mUnReadBrdFriendCircleNotify.getVisibility() == View.VISIBLE) {
					mUnReadBrdFriendCircleNotify.setVisibility(View.GONE);
				}
			} else {
				mUnReadBrdNotify.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setVisibility(View.GONE);
				mTv_BroadcastNotificationCount.setText("");
				mIsShowNotificationCount = false;
			}
		}
	}

	private String headImgPath;

	/**
	 * 设置头像
	 */
	private void updateHead() {
		final AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
		if (TextUtils.isEmpty(profileInfo.head_img_path) // 这个用来处理默认头像比如Profile改性别�?
				|| (!TextUtils.isEmpty(profileInfo.head_img_path) && !profileInfo.head_img_path.equals(headImgPath))) {
			// 调UIL的显示头像方�?
			ImageManager.getInstance().DisplayAvatarImage(me_back, profileInfo.getServerUrl(), profileInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, profileInfo.sex, profileInfo.getSerialFromHead(), new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage != null) {
						headImgPath = profileInfo.head_img_path;
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

				}

			});
		}
	}

	public void display_uploadHead(String filepath) {
		/*Bitmap bitmap = ImageManager.getInstance().loadLocalImageSync(filepath,false);//ImageUtil.getBitmapFromFile(filepath);
		me_back.setImageBitmap(ImageUtil.toRoundCorner(bitmap));*/
		ImageManager.getInstance().displayLocalImage_circle(me_back,filepath,0,0,null);
	}

	private void updateProfileName() {
		AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
		profile_Name.setText(profileInfo.name);
	}

	@Override
	protected void onResume() { // svn main
		super.onResume();
		/*if (PalmchatApp.getApplication().isInit()) {// not ready init
			PalmchatLogUtils.println("PalmchatApp  MainAct onResume init");
			if (CommonUtils.isNetworkAvailable(this)) {
				PalmchatApp.getApplication().showBindEmailDialog(this);
			}
		}*/
		//今天赶版本上�?为了解决 #13099 不稳定出现任意操作后，顶部状态栏UI显示白色画面，先在这里强制设置透明�?后面有时间找到深层原因再改好
		if(relativelayout_title!=null ){
			relativelayout_title.getBackground().setAlpha(0xFF);
		}
		if(CommonUtils.isAndroidGp()){//oogle渠道才可以弹
			toShowScoreDialog(this);
		}
		toUpdateInfo();

		// add by HJG 2015-12-16
		/*updateBadge(currIndex, true);*/

		getStoreHaveNewProd();
		if (SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getChangeOfCountry()) {// 若已改变
			hideNew(true, true);
			SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setChangeOfCountry(false);
		}
		AppEventsLogger.activateApp(this);
		PalmcallLoginDelegate.enterForeground();
		/*if(isCountry()) {//是否包含PalmCall
			palmCallFragment = new PalmCallFragment();
			listFragments.add(palmCallFragment);
		}*/
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPageEnd("MainTab");
	}
	// add by HJG 2015-12-16

	@Override
	protected void onStop() {
		super.onStop();
		PalmcallLoginDelegate.enterBackground();
	}

	private synchronized void toShowScoreDialog(Activity context) {
		// TODO Auto-generated method stub
		int count = SharePreferenceUtils.getInstance(this).getScoreCount();
		count++;
		SharePreferenceUtils.getInstance(this).setScoreCount(count);
		boolean isShow = SharePreferenceUtils.getInstance(context).getScoreShow();
		if (isShow) {//
			long time = SharePreferenceUtils.getInstance(context).getScoreTime();
			long diff = System.currentTimeMillis() - time; // compare last time
			long days = diff / (1000 * 60 * 60 * 24);//
			if (days >= 3 && count >= 50) {// to show score dialog
				AppDialog appDialog = new AppDialog(context);
				appDialog.createGiveScore(context, MainTab.this);
				appDialog.show();

				SharePreferenceUtils.getInstance(context).setScoreShow(false);

				appDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							SharePreferenceUtils.getInstance(MainTab.this).setScoreCount(0);
							SharePreferenceUtils.getInstance(MainTab.this).setScoreShow(true);
						}
						return false;
					}
				});
			}
		}
	}

	private void toUpdateInfo() {
		// TODO Auto-generated method stub
		AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();
		String country = DefaultValueConstant.OVERSEA;
		if (null != afProfileInfo.afId && afProfileInfo.is_bind_phone && (TextUtils.isEmpty(afProfileInfo.country) || country.equals(afProfileInfo.country))) {
			PalmchatLogUtils.println("toUpdateInfo");
			String localCountry = PalmchatApp.getOsInfo().getCountry(this);
			PalmchatLogUtils.println("toUpdateInfo  localCountry  " + localCountry);
			if (!localCountry.equals(afProfileInfo.country)) {// not oversea
				afProfileInfo.country = localCountry;
				afProfileInfo.region = DefaultValueConstant.OTHERS;
				afProfileInfo.city = DefaultValueConstant.OTHER;
				PalmchatLogUtils.println("toUpdateInfo  updateInfo localCountry  " + localCountry);
				PalmchatApp.getApplication().mAfCorePalmchat.AfHttpUpdateInfo(afProfileInfo, null, MainTab.this);
			}
		}
	}

	public static String getScreenQuality(Context context, int w, int h) {
		if (w >= 1080) {
			return "xd";
		} else if (w >= 720) {
			return "hd";
		} else if (w >= 320) {
			return "md";
		} else {
			// no handle
			return "md";
		}
	}

	public LinearLayout mTitleLinearLayout;
	private int defTitleHeight;
	private int titleHeight;
	private int  typeTitleAnimation;//0普通情�?1隐去 2展开 3隐藏状�? 4展开状�?
	private View relativelayout_title;
	private void findViews() {
		// TODO Auto-generated method stub
		mRL_RegionSelectTip = findViewById(R.id.region_select_tip_layout);
		mV_Fab_guide = findViewById(R.id.fab_guide_view_id);
		mBtn_Sendbroad_guide  = (ImageButton)findViewById(R.id.maintab_send_guide_id);
		mBtn_Sendbroad_guide.setOnClickListener(this);
		mV_Fab_guide.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				mV_Fab_guide.setVisibility(View.GONE);
				showFabButton();
				SharePreferenceUtils.getInstance(MainTab.this).setCurrentVersionnameByAccount(CacheManager.getInstance().getMyProfile().afId);
				return true;
			}
		});
		mTitleLinearLayout=(LinearLayout)findViewById(R.id.title_layout);
		relativelayout_title = findViewById(R.id.relativelayout_title);
		mTitleLinearLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		mSendBroadcastMaskingView = (SendBroadcastMaskingView)findViewById(R.id.fab_view_id);
		mSendBroadcastMaskingView.setSendBroadBtnListener(new SendBroadBtnListener() {
			@Override
			public void sendVoice() {
				toSendVoiceBroadcast();
			}

			@Override
			public void sendPhoto() {
				toSendPhotoBroadcast(null);
			}

			@Override
			public void dismiss() {
				showFabButton();
			}
		});

		mIv_BroadcastNotification = (ImageView)findViewById(R.id.broadcast_notification_id);
		mIv_BroadcastNotification.setOnClickListener(this);
		mIv_BroadcastNotification.setVisibility(View.VISIBLE);
		mBroadcastAreaSelect = (ImageView)findViewById(R.id.broadcast_area_select);
		mBroadcastAreaSelect.setOnClickListener(this);
		mTv_BroadcastNotificationCount = (TextView) findViewById(R.id.broadcast_notification_count_id);
		mUnreadMsgRight = (TextView)findViewById(R.id.unread_msg_right);
		mTv_BroadcastNotificationCount.setOnClickListener(this);
//		container = (ViewGroup) findViewById(R.id.main_layout);
//		tabHome = findViewById(R.id.main_tab_home);
		layout_home = findViewById(R.id.layout_home);

//		tabBroadcast = findViewById(R.id.main_tab_broadcast);
		layout_broadcast = findViewById(R.id.layout_broadcast);
//		tabCall = findViewById(R.id.main_tab_call);//新增callfragment mj
		layout_palmcall = findViewById(R.id.layout_call);
//		tabChats = findViewById(R.id.main_tab_chat);
		layout_chat = findViewById(R.id.layout_chat);
//		tabExplore = findViewById(R.id.main_tab_explore);
		layout_explore = findViewById(R.id.layout_explore);
		rightImageview = (ImageView) findViewById(R.id.op2);
		filterImage = (ImageView) findViewById(R.id.op3);



//		filterImage.setVisibility(View.VISIBLE);
		filterImage.setBackgroundResource(R.drawable.broadcast_area_actionbar);
		mRightChatSearchBtn = (ImageView) findViewById(R.id.op1);
		img_ico = (ImageView) findViewById(R.id.img_ico);
		rightImageview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (checkPositionByChannel(currIndex)) {
				/*case CONTENT_FRAGMENT_PALMPLAY: // add by HJG 2015-12-16
					startActivity(new Intent(MainTab.this, PalmPlaySearchActivity.class));
					break;*/
					case CONTENT_FRAGMENT_CHAT:
						startActivity(new Intent(MainTab.this, LocalSearchActivity.class));
						break;
					case CONTENT_FRAGMENT_PALMCALL:
						Intent intent = new Intent(MainTab.this,PalmCallRecentsActivity.class);
						intent.putExtra(JsonConstant.CALLLISTTIME,palmCallFragment.mLeftTime);
						startActivity(intent);
						mUnreadMsgRight.setVisibility(View.GONE);

						SharePreferenceUtils.getInstance(mContext).setMissedCalls(false);
						break;
					default:
						break;
				}
			}
		});


		mRightChatSearchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (checkPositionByChannel(currIndex)) {
					case CONTENT_FRAGMENT_HOME:
						// switchHomeGrid();

						break;
					case CONTENT_FRAGMENT_BROADCAST:
						break;

					case CONTENT_FRAGMENT_CHAT:

						break;
					case CONTENT_FRAGMENT_EXPLORE:
						// gtf 2014-11-16
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_SETTING);
//					MobclickAgent.onEvent(MainTab.this, ReadyConfigXML.ENTRY_SETTING);
						startActivity(new Intent(MainTab.this, SettingsActivity.class));
						break;

					default:
						break;
				}
			}
		});

		unReadText = (TextView) findViewById(R.id.unread_msg);

		mUnReadBrdNotify = (TextView) findViewById(R.id.unread_notify);
		mUnReadBrdFriendCircleNotify = (ImageView) findViewById(R.id.unread_friendCircle_notify);

		unReadText_HasNewStore = (ImageView) findViewById(R.id.unread_msg_explore);

		setOnMyClick();
		viewpager = (CustomViewPager) findViewById(R.id.viewpager);
		viewpager.setScanScroll(false);
		mTabsView = findViewById(R.id.tabs);

		me_back = (ImageView) findViewById(R.id.back_button);
		layout_loginLoading = (LinearLayout) findViewById(R.id.frameLayout_middle_backlogin);

		AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
		me_back.setImageResource(profileInfo.sex == Consts.AFMOBI_SEX_FEMALE ? R.drawable.head_female2 : R.drawable.head_male2);
		int w =(int)getResources().getDimension(R.dimen.d_broadcast_headszie) ;//CommonUtils.dip2px(getApplicationContext(), 40);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, w);
		params.setMargins((int)getResources().getDimension(R.dimen.d_head_left_margin)//CommonUtils.dip2px(getApplicationContext(), 15)
				, 0, 0, 0);
		me_back.setLayoutParams(params);
		me_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toMyProfile();
			}
		});
		profile_Name = (TextView) findViewById(R.id.myProfile_Name);
		profile_Name.setVisibility(View.VISIBLE);
		profile_Name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toMyProfile();
			}
		});

		SharePreferenceUtils.getInstance(this).setNotCompleteImei(false);

		viewOffline = findViewById(R.id.view_offline);
		viewOffline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Settings.ACTION_SETTINGS //系统设置界面
				// Settings.ACTION_WIFI_SETTINGS //wifi设置界面
				// Settings.ACTION_DATA_ROAMING_SETTINGS)//移动网络设置界面
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivity(intent);
			}
		});
		/*if(!PalmchatLogUtils.USE_PALMCALL) {
			findViewById(R.id.frameLayout_palmpcall).setVisibility(View.GONE);
		}*/

		//View mV_PalmCall_Main,mV_PalmCall_Recent,mV_PalmCall_Recharge,mV_PalmCall_Intro;
		mV_PalmCall_Main = findViewById(R.id.palmcall_guide_layout_id);
		mV_PalmCall_Main.setOnClickListener(this);
		mTv_Call_Recharge_Often  =(TextView) findViewById(R.id.tv_call_recharge_often);
		String text = String.format(getResources().getString(R.string.palmcall_surplus_minutes),TIME);
		SpannableStringBuilder styke = new SpannableStringBuilder(text);
		String mTime = TIME + "";
		int index = text.indexOf(mTime);
		styke.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.log_blue)),index,index+ mTime.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		mTv_Call_Recharge_Often.setText(styke);
		mV_PalmCall_Recent = findViewById(R.id.recent_guide_layout);
		mV_PalmCall_Recharge = findViewById(R.id.guide_rechange);
		mV_PalmCall_Intro = findViewById(R.id.rl_guide_intro);
		mV_PalmCall_Main_Intro = findViewById(R.id.guide_intro);
		mRl_Guide_intro = (RelativeLayout) findViewById(R.id.rl_guide_intro_text);
		mRl_intro = (RelativeLayout)findViewById(R.id.rl_intro);
	}

	private void setOnMyClick() {
		layout_home.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_HOME));
		layout_broadcast.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_BROADCAST));
		layout_palmcall.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_PALMCALL));
		layout_chat.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_CHAT));
		layout_explore.setOnClickListener(new MyOnClickListener(CONTENT_FRAGMENT_EXPLORE));
	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
		updateHead();
		updateProfileName();
	}

	public void setIco_start(int start) {
		if (start == 0) {
			img_ico.setBackgroundResource(R.drawable.arrow_white_open);
		} else {
			img_ico.setBackgroundResource(R.drawable.arrow_white_close);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mSendBroadcastMaskingView.getVisibility()==View.VISIBLE){
				mSendBroadcastMaskingView.setVisibility(View.GONE);
				showFabButton();
				return true;
			}

			if(mV_Fab_guide.getVisibility()==View.VISIBLE){
				mV_Fab_guide.setVisibility(View.GONE);
				showFabButton();
				SharePreferenceUtils.getInstance(MainTab.this).setCurrentVersionnameByAccount(CacheManager.getInstance().getMyProfile().afId);
				return true;
			}
			if (currIndex == CONTENT_FRAGMENT_BROADCAST && broadcastFragmentTab != null && broadcastFragmentTab.isAdded()) {
				{
					/*Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startActivity(startMain);*/
					KeyDownUtil.actionMain(MainTab.this);
					return true;
				}
			}
			/*Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startActivity(startMain);*/
			KeyDownUtil.actionMain(MainTab.this);

			return true;
		}
		return false;
	}

	@Override
	public void OnGiveScore(int type) {
		// TODO Auto-generated method stub
		switch (type) {
			case AppDialog.GIVE_SCORE_SURE:
				SharePreferenceUtils.getInstance(this).setScoreShow(false);
				Uri uri = Uri.parse(Consts.GET_SCORE);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
				break;
			case AppDialog.GIVE_SCORE_NOT_NOW:
				SharePreferenceUtils.getInstance(this).setScoreCount(0);
				SharePreferenceUtils.getInstance(this).setScoreShow(true);
				break;
			case AppDialog.GIVE_SCORE_NO_THANKS:
				SharePreferenceUtils.getInstance(this).setScoreShow(false);
				break;
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		switch (requestCode) {
			/*case UPDATE_STATE_CITY: //这个已经不用�?已用EventBus机制
				break;*/
			case IntentConstant.SHARE_BROADCAST:
				if (data != null) {
					boolean isSuccess = data.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
					String tempTipContent = data.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
					String tipContent;
					if (isSuccess) {
						tipContent = getResources().getString(R.string.share_friend_success);
					}
					else {
						if(!TextUtils.isEmpty(tempTipContent)){
							tipContent = tempTipContent;
						}
						else {
							tipContent = getResources().getString(R.string.share_friend_failed);
						}
					}
					ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess,tipContent);
				}
				break;

			case IntentConstant.REQUEST_CODE_MAINTAB:{
				if(mSendBroadcastMaskingView.getVisibility()==View.VISIBLE){
					mSendBroadcastMaskingView.setVisibility(View.GONE);
					showFabButton();
				}
				break;
			}
		}
		if (Constants.COMMENT_FLAG == resultCode) {
			if (broadcastFragmentTab != null) {//处理评论结果
				broadcastFragmentTab.onActivityResult(Constants.GET_MSG, resultCode, data);
			}
			return; //评论回调 如果调用父类的onActivityResult 会使new around再调一�?显示2条评论， 而朋友圈是第3个Fragment 又不会的�?
			//这是因为FragmenActivity�?onActivityResult只会调用当前Fragment里活跃的第一页的 所以这里return
		}

		PalmchatLogUtils.e("WXL", "--TAB--onActivityResult----");
		//注意 这里�?super.onActivityResult不能移到上面�?放到函数�?原因见上文对COMMENT_FLAG的说�?
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.e("MainTab", "----AfOnResult--Flag:" + flag + "---Code:" + code + "  result:" + result);
//		dismissProgDialog();
		dismissProgressDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
				case Consts.REQ_BIND_BY_EMAIL: {
					String des = (String) result;
					PalmchatLogUtils.println("ywp: AfOnResult: REQ_BIND_BY_EMAIL descrip =  " + des);
					ToastManager.getInstance().show(this, getString(R.string.success));
					break;
				}
				case Consts.REQ_PHONEBOOK_BACKUP: {
					// app.setImporting(false);
					mAfCorePalmchat.AfDbSettingPbTimeOpr(CacheManager.getInstance().getMyProfile().afId, System.currentTimeMillis(), true);
					PalmchatLogUtils.println("import success.");
					break;
				}
				case Consts.REQ_UPDATE_INFO: {

					if (result != null && result instanceof AfProfileInfo) {
						String country = ((AfProfileInfo) result).country;
						PalmchatLogUtils.println("REQ_UPDATE_INFO country " + country);
						CacheManager.getInstance().getMyProfile().country = country;
					}
					break;
				}
				case Consts.REQ_STORE_HAVE_NEW_PROD: {
					if (result != null && result instanceof Integer) {
						int has_new_store = Integer.parseInt(result.toString());
						if (STORE_HAS_NEW_PROINFO == has_new_store) {
//						CacheManager.getInstance().setHasNewStoreProInfo(true);
//						showNew();
						}
					}
					break;
				}
				case Consts.REQ_STORE_VERSION_CHECK: {
					AfStoreProdList afStoreProdList = (AfStoreProdList) result;
					AfPageInfo<AfProdProfile> page_info = afStoreProdList.page_info;
					if (page_info != null && page_info.prof_list != null && page_info.prof_list.size() > 0) {
						AfProdProfile prod = page_info.prof_list.get(0);
						if (prod != null && prod.item_id != null) {
							CacheManager.getInstance().getItemid_update().put(prod.item_id, prod);
							PalmchatLogUtils.println("prod.item_id:" + prod.item_id + "  vercode:" + prod.ver_code);
							CacheManager.getInstance().setHasNewStoreProInfo(true);
							showNew();
						}
					}
					break;
				}
				case Consts.REQ_BCGET_REGION_BROADCAST:
					if(null != result){
						AfResponseComm.AfBCGetRegionBroadcastResp afResponseComm = (AfResponseComm.AfBCGetRegionBroadcastResp)result;
						if(afResponseComm != null && afResponseComm.default_list.size() >0){
							PalmchatApp.getApplication().setDefaultList(afResponseComm.default_list);
							dismissProgressDialog();
							if(PalmchatApp.getApplication().getDefaultList() != null){
								StartAreaAdapter();
							}
						}else{//请求区域没有数据的情况下
							dismissProgressDialog();
							ToastManager.getInstance().show(this, R.string.select_area_not_broadcast);
						}
					}
					break;

				//获得tags词典
				case Consts.REQ_BCGET_LANGUAGE_PACKAGE:{
					doResultOfCurrentTags(result);
					break;
				}
				case Consts.REQ_FLAG_PAYMENT_ANALYSIS:  //支付统计上报成功后，删除已上报过的文件
					FileUtils.deleteFilesBydir(RequestConstant.MOBILE_TOP_UP_CACHE, fileMap);
					break;
			}

		} else {
			if (Consts.REQ_PHONEBOOK_BACKUP == flag) {
				// app.setImporting(false);
				PalmchatLogUtils.println("import failure code  " + code);
				return;
			} else if (Consts.REQ_UPDATE_INFO == flag) {
				PalmchatLogUtils.println("REQ_UPDATE_INFO  failure" + code);
				if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词�?
					Consts.getInstance().showToast(this, code, flag, http_code);
				}
				return;
			} else if (flag == Consts.REQ_STORE_HAVE_NEW_PROD) {
//				mainHandler.postDelayed(new Runnable() {
//					public void run() {
//						mAfCorePalmchat.AfHttpStoreHaveNewProd(null, MainTab.this);
//					}
//				}, 30000);
				return;
			} else if (flag == Consts.REQ_STORE_VERSION_CHECK) {
				return;
			} else if(Consts.REQ_BCGET_LANGUAGE_PACKAGE == flag){
				PalmchatLogUtils.i(TAG,"----REQ_BCGET_LANGUAGE_PACKAGE---failure");
			}

			Consts.getInstance().showToast(MainTab.this, code, flag, http_code);
		}
	}

	private void StartAreaAdapter(){
		if(!isFinishing()){//判断Activity是否存在
			try{
				mDialog = new Dialog(MainTab.this,R.style.CustomAlbumDialogTheme);
				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				LayoutInflater inflater =LayoutInflater.from(mContext);
				View viewdialog = inflater.inflate(R.layout.activity_broadcast_area_listview,null);
				LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				mDialog.setContentView(viewdialog,layoutParams);
				WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
				Display mDisplay = m.getDefaultDisplay();

				Window dialogWindow = mDialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.CENTER);//居中显示
				lp.width = (int)(mDisplay.getWidth() /1.2); // 宽度
				lp.height = (int)(mDisplay.getHeight() / 1.5); // 高度
				dialogWindow.setAttributes(lp);

				mAreaListView = (ListView)viewdialog.findViewById(R.id.area_listview);

				mAreaListAdapter = new AreaListAdapter(mContext,PalmchatApp.getApplication().getDefaultList());
				mAreaListView.setAdapter(mAreaListAdapter);
				mAreaListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//只能有一项选中
				mAreaListView.setOnItemClickListener(MainTab.this);
				mDialog.show();
			}catch(Exception e){
				PalmchatLogUtils.e(TAG, e.toString());
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		ListView lv = (ListView)parent;
		String country = PalmchatApp.getApplication().getDefaultList().get(position).country;
		String state = PalmchatApp.getApplication().getDefaultList().get(position).state;
		if(checkedIndex != position){

			int ids = checkedIndex - lv.getFirstVisiblePosition();
			if(ids >= 0){

				View item =lv.getChildAt(ids);
				if(item != null){
					RadioButton rb = (RadioButton)item.findViewById(checkedIndex);
					if(rb != null){
						rb.setChecked(false);
					}
				}
			}

			RadioButton rb1 = (RadioButton)view.findViewById(position);
			if(rb1 != null){
				rb1.setChecked(true);
				checkedIndex = position;
			}

		}
		mDialog.dismiss();
		if (NetworkUtils.isNetworkAvailable(mContext)) {
			Intent intent = new Intent();
			intent.putExtra("country",country);
			intent.putExtra("state",state);
			intent.putExtra("checkedIndex",position+"");
			intent.setClass(mContext,BroadcastAreaActivity.class);
			startActivity(intent);
		}else{
			ToastManager.getInstance().show(mContext, mContext.getString(R.string.network_unavailable));
		}

	}

	/**
	 * 是否显示红点
	 */
	public void showNew() {
		// TODO Auto-generated method stub
		if (((SharePreferenceUtils.getInstance(this).getUnReadExchange() // 充值成功信息未�?
				|| SharePreferenceUtils.getInstance(this).getUnReadPredictRecord() // 开奖消�?
				|| SharePreferenceUtils.getInstance(this).getUnReadPredictPrize() // 派奖消息
				|| SharePreferenceUtils.getInstance(this).getIsFirstInstall()
				|| SharePreferenceUtils.getInstance(this).getUnReadNewPalmGuess())
				&& CacheManager.getInstance().isPalmGuessShow()
				&& PalmchatApp.getApplication().isOpenPalmGuess //如果开放PalmGuess，一定要把此变量设置为true
		)
				|| CacheManager.getInstance().hasNewStoreProInfo() // store有新表情
				|| SharePreferenceUtils.getInstance(getApplicationContext()).getIsAtme(myAfid)
				|| SharePreferenceUtils.getInstance(this).getBalancePalmcoinRedDot()) {
			unReadText_HasNewStore.setVisibility(View.VISIBLE);
		} else {
			if (!CacheManager.getInstance().isPalmGuessShow()
					&& SharePreferenceUtils.getInstance(this).getUnReadNewPalmGuess()
					&& !CacheManager.getInstance().hasNewStoreProInfo()
					&& !SharePreferenceUtils.getInstance(getApplicationContext()).getIsAtme(myAfid)
					&& !SharePreferenceUtils.getInstance(this).getBalancePalmcoinRedDot()) {
				unReadText_HasNewStore.setVisibility(View.GONE);
			}
		}
		if (CacheManager.getInstance().hasNewStoreProInfo()) {
			if (exploreFragment != null) {
				exploreFragment.showNew();
			}
		}
		if (SharePreferenceUtils.getInstance(getApplicationContext()).getIsAtme(myAfid)) {
			if (exploreFragment != null) {
				exploreFragment.showNewAtMe();
			}
		}
//		if (SharePreferenceUtils.getInstance(this).getBalancePalmcoinRedDot()) {
//			if (exploreFragment != null) {
//				exploreFragment.showPalmcoinRedDot();
//			}
//		}
	}

	public void hideNew(boolean isCheck, boolean isChangeCountry) {
		if(!isShowExploreTips(isCheck,isChangeCountry)){
			unReadText_HasNewStore.setVisibility(View.GONE);
		}
	}

	private void showSelectedOption(int pos) {
		// TODO Auto-generated method stub
		switch (checkPositionByChannel(pos)) {
			case CONTENT_FRAGMENT_HOME:
				layout_home.setSelected(true);
			/*layout_palmplay.setSelected(false);// add by hxy*/
				layout_broadcast.setSelected(false);
				layout_chat.setSelected(false);
				layout_explore.setSelected(false);
				layout_palmcall.setSelected(false);
				showRegionSelectTip(false);
				break;
			case CONTENT_FRAGMENT_BROADCAST:
				layout_home.setSelected(false);
			/*layout_palmplay.setSelected(false);// add by hxy*/
				layout_broadcast.setSelected(true);
				layout_chat.setSelected(false);
				layout_explore.setSelected(false);
				layout_palmcall.setSelected(false);
				break;
			case CONTENT_FRAGMENT_CHAT:
				layout_home.setSelected(false);
			/*layout_palmplay.setSelected(false);// add by hxy*/
				layout_broadcast.setSelected(false);
				layout_chat.setSelected(true);
				layout_explore.setSelected(false);
				layout_palmcall.setSelected(false);
				showRegionSelectTip(false);
				break;
			case CONTENT_FRAGMENT_EXPLORE:
				layout_home.setSelected(false);
			/*layout_palmplay.setSelected(false);// add by hxy*/
				layout_broadcast.setSelected(false);
				layout_chat.setSelected(false);
				layout_explore.setSelected(true);
				layout_palmcall.setSelected(false);
				showNew();
				showRegionSelectTip(false);
				break;
			case CONTENT_FRAGMENT_PALMCALL:
				layout_home.setSelected(false);
				layout_broadcast.setSelected(false);
				layout_chat.setSelected(false);
				layout_explore.setSelected(false);
				layout_palmcall.setSelected(true);
				showRegionSelectTip(false);
				break;
		/*case CONTENT_FRAGMENT_PALMPLAY:
			layout_home.setSelected(false);
			layout_palmplay.setSelected(true);// add by hxy
			layout_broadcast.setSelected(false);
			layout_chat.setSelected(false);
			layout_explore.setSelected(false);

			break;*/

		}
	}
	public void onEventMainThread(ChatRoomListEvent event){
		if(event.getType()==ChatRoomListEvent.TYPE_REFRESH_ATME){
			SharePreferenceUtils.getInstance(getApplicationContext()).setIsAtme(myAfid, true);
			unReadText_HasNewStore.setVisibility(View.VISIBLE);
			if (exploreFragment != null) {
				exploreFragment.showNewAtMe();
			}
		}
	}
	/*@Override
	public void onRefreshAtMe() {
		mainHandler.post(new Runnable() {
			public void run() {
				SharePreferenceUtils.getInstance(getApplicationContext()).setIsAtme(myAfid, true);
				unReadText_HasNewStore.setVisibility(View.VISIBLE);
				if (exploreFragment != null) {
					exploreFragment.showNewAtMe();
				}
			}
		});
	}*/

	/**
	 * coin改变通知 B31通知
	 *
	 * @param event
	 */
	public void onEventMainThread(UpdateCoinsEvent event) {
		mainHandler.post(new Runnable() {
			public void run() {
				unReadText_HasNewStore.setVisibility(View.VISIBLE);
//				if (exploreFragment != null) {
//					exploreFragment.showPalmcoinRedDot();
//				}
			}
		});
	}

	/**
	 * 通知是否有未接电话
	 * @param notication
	 */
	public void onEventMainThread(EventPalmcallNotication notication) {
		String callState = notication.getCallState();
		if(notication.getCallType() == AfPalmCallResp.AFMOBI_CALL_TYPE_MISSED){//判断是否是未接电话
			if(callState.equals(MtcCallConstants.MtcCallDidTermNotification) || callState.equals(MtcCallConstants.MtcCallTermedNotification)){
				if(currIndex == CONTENT_FRAGMENT_PALMCALL) {
					mUnreadMsgRight.setVisibility(View.VISIBLE);
				}
				SharePreferenceUtils.getInstance(mContext).setMissedCalls(true);
			}
		}
	}

	/**
	 * 开个线程处理tags更新
	 * @param result
	 */
	private void doResultOfCurrentTags(final Object result){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AfTagGetLangPackageResp afResponseComm=null;
				try{
					afResponseComm = (AfTagGetLangPackageResp)result;
				} catch(Exception e){
					e.printStackTrace();
				}
				if((null!=afResponseComm)&&(null!=afResponseComm.language_ver)){
					PalmchatLogUtils.i(TAG,"tag--download===ver==="+afResponseComm.language_ver);
					SharePreferenceUtils.getInstance(MainTab.this).setCurrentTagsVersion(afResponseComm.language_ver);
				} else {
					SharePreferenceUtils.getInstance(MainTab.this).setCurrentTagsVersion(null);
					return;
				}

				if((null!=afResponseComm.local_list)||(null != afResponseComm.default_list)){
					mAfCorePalmchat.AfDbBCLangPackageListDeleteAll();
				}
				if( null != afResponseComm.default_list )
				{   //
					String[] array = (String[])afResponseComm.default_list.toArray(new String[afResponseComm.default_list.size()]);
					mAfCorePalmchat.AfDbBCLangPackageListInsert(array, (byte)0);

				}

				if(null!=afResponseComm.local_list)
				{   //
					String[] array = (String[])afResponseComm.local_list.toArray(new String[afResponseComm.local_list.size()]);
					mAfCorePalmchat.AfDbBCLangPackageListInsert(array, (byte)1);
				}
			}
		}).start();
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = getIndexByChannel(i);
			PalmchatLogUtils.d("mengjie","index是："+index);
		}

		@Override
		public void onClick(View v) {
			if(viewpager!=null) {
				stopListViewScroll();
				if (viewpager.getCurrentItem() == index && (!isChange && index != 2)) {
					switch (checkPositionByChannel(index)) {
						case CONTENT_FRAGMENT_HOME:
							homeFragment.setListScrolltoTop();
							break;
						case CONTENT_FRAGMENT_BROADCAST:
							broadcastFragmentTab.setListScrolltoTop();
							break;
						case CONTENT_FRAGMENT_CHAT:
							chatFragment.setListScrolltoTop();
							break;
						case CONTENT_FRAGMENT_EXPLORE:
							if (!CacheManager.getInstance().isPalmGuessShow()) {
								SharePreferenceUtils.getInstance(MainTab.this).setUnReadNewPalmGuess(false);
								hideNew(true, false);
							}
							break;
						case CONTENT_FRAGMENT_PALMCALL:

							break;
					}
				} else {
					if(palmCallFragment != null && viewpager.getCurrentItem() == index && checkPositionByChannel(index) == CONTENT_FRAGMENT_PALMCALL){
						palmCallFragment.setListScrolltoTop();//点击回到顶部
					}
					viewpager.setCurrentItem(index);
					//showSelectedOption(index);
				}
				resetTitlePosition();
				isChange = false;
			}
		}
	}

	/*public void showProgDialog(int resId) {
		if (dialog == null) {
			dialog = new Dialog(this, R.style.Theme_LargeDialog);
			dialog.setOnKeyListener(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_loading);
			((TextView) dialog.findViewById(R.id.textview_tips)).setText(resId);

		}
		dialog.show();
	}

	private void dismissProgDialog() {
		if (null != dialog && dialog.isShowing()) {
			try {
				dialog.cancel();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}*/

	class OfflineReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			// 网络状态改变广�?
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()) || Constants.REFRESH_WAKEUPSCROLL_ACTION.equals(intent.getAction())) {

				if (NetworkUtils.isNetworkAvailable(context)) {
					if (viewOffline != null) {
						//viewOffline.setVisibility(View.GONE);
					}

				} else {
					if (viewOffline != null) {
						//viewOffline.setVisibility(View.VISIBLE);
						cancelLoading();
					}
				}

			}
		}
	}

	private void toMyProfile() {
		// TODO Auto-generated method stub
		// GTF 2014-11-16
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
//		MobclickAgent.onEvent(MainTab.this, ReadyConfigXML.ENTRY_MMPF);
		// 测试
		// startActivity(new Intent(MainTab.this,
		// PredictAddressListActivity.class));
		startActivity(new Intent(MainTab.this, MyProfileActivity.class));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PalmchatLogUtils.e("WXL","MainTab onDestroy");
		if (addPopWindow != null) {
			addPopWindow.dismiss();
			addPopWindow = null;
		}
		unRegisterBroadcastReceiver();
		SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setChangeOfCountry(false);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (viewpager != null)
			viewpager.setCurrentItem(savedInstanceState.getInt("pos"));
//		 	super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.clear();
		if (viewpager != null)
			outState.putInt("pos", viewpager.getCurrentItem());
//		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
		switch (msg) {
			case Consts.AF_SYS_MSG_INIT:
				viewOffline.setClickable(true);
				break;
		}
		return false;
	}

	private void checkStroeNewVersion() {
		// TODO Auto-generated method stub
		AfPaystoreCommon afPaystoreCommon = mAfCorePalmchat.AfDBPaystoreProdinfoList();
		if (afPaystoreCommon != null) {

			AfStoreDlProdInfoList afStoreDlProdInfoList = (AfStoreDlProdInfoList) afPaystoreCommon.obj;
			if (afStoreDlProdInfoList != null) {
				ArrayList<AfStoreDlProdInfo> list = afStoreDlProdInfoList.prod_list;
				if (null != list && list.size() > 0) {
					for (AfStoreDlProdInfo afStoreDlProdInfo : list) {
						String ver_json = CommonUtils.stringToJson(afStoreDlProdInfo);
						mAfCorePalmchat.AfHttpStoreProdVersionCheck(Consts.PROD_TYPE_EXPRESS_WALLPAPER, ver_json, afStoreDlProdInfo, this);
					}
				}
			}
		}
	}

	/**
	 * zhh弹出修改地区对话框（当profile中国家和手机绑定国家不一致。或�?第二次登录时region或city为others�?
	 * 客户端第二次登录时判断state或city是others的，提示用户完善region信息，记录到本地，只提示一�?
	 */
	private void showUpdateRegionDialog() {
		PalmchatApp app = (PalmchatApp) getApplication();
		if (!app.isApplicationBroughtToBackground()) { // 当应用挂后台时，不弹此对话框
			AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();

			// zhh 记录登录次数
			int times = SharePreferenceUtils.getInstance(this).getLoginTimes(myProfile.afId);
			if (times < 3) {
				times++;
				SharePreferenceUtils.getInstance(this).setLoginTimes(myProfile.afId, times);
			}

			String phone_cc = myProfile.phone_cc;
			String country = myProfile.country;
			if (phone_cc != null && country != null) {
				String bindCountry = DBState.getInstance(this).getCountryFromCode(phone_cc);
				if (bindCountry != null && !bindCountry.equalsIgnoreCase(country)) { // 当profile中国家和手机绑定国家不一�?
					Intent mIntent = new Intent(this, UpdateStateActivity.class);
					mIntent.putExtra("action", 1); // 1代表profile中国家和手机绑定国家不一�?
					startActivityForResult(mIntent, UPDATE_STATE_CITY);
					return;

				}
			}

			if (times == 2 && !SharePreferenceUtils.getInstance(this).getIsShowUpdateRegion(myProfile.afId)) { // 第二次登录，且未弹过地区修改�?
				// 如果city为空或为other,region为空或为others
				if (DefaultValueConstant.OTHER.equals(myProfile.city) || DefaultValueConstant.OTHERS.equals(myProfile.region)) {
					SharePreferenceUtils.getInstance(this).setIsShowUpdateRegion(myProfile.afId, true); // 设置为已弹出过state修改提示�?
					if (null == phone_cc) {// 未绑定手�?
						Intent mIntent = new Intent(this, UpdateStateActivity.class);
						mIntent.putExtra("action", 2); // 2代表未绑定手�?且city或者region为other/others
						startActivityForResult(mIntent, UPDATE_STATE_CITY);
					} else {// 绑定了手机号
						Intent mIntent = new Intent(this, UpdateStateActivity.class);
						mIntent.putExtra("action", 3); // 3代表绑定了手机，且city或者region为other/others
						startActivityForResult(mIntent, UPDATE_STATE_CITY);
					}

				}
			}
		}

	}

	/**
	 * 弹出的PopupWindow视图
	 */
	public class AddPopWindow extends PopupWindow {
		private View conentView;

		public AddPopWindow(final Activity context) {
			super(context);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			conentView = inflater.inflate(R.layout.activity_qrcode_popupwindow, null);
			// 设置SelectPicPopupWindow的View
			this.setContentView(conentView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点�?
			this.setFocusable(true);
			this.setOutsideTouchable(true);
			// 刷新状�?
			this.update();
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0000000000);
			// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
			this.setBackgroundDrawable(dw);
			// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			// this.setAnimationStyle(R.style.AnimationPreview);
			backgroundAlpha(0.4f);
			/**
			 * 二维码点击事�?
			 */
			LinearLayout addTaskLayout = (LinearLayout) conentView.findViewById(R.id.qr_code_linear);
			addTaskLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainTab.this, CaptureActivity.class);
					intent.putExtra("QrCode", "Main");
					startActivity(intent);
					AddPopWindow.this.dismiss();
				}
			});
			/**
			 * 摇一摇点击事�?
			 */
			LinearLayout mShakeShakeText = (LinearLayout) conentView.findViewById(R.id.shakeshake_text);
			mShakeShakeText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LEFT_T_SHAKE);
//					MobclickAgent.onEvent(context, ReadyConfigXML.LEFT_T_SHAKE);
					startActivity(new Intent(mContext, ShakeFragmentActivity.class));
					AddPopWindow.this.dismiss();
				}
			});
			/**
			 * 群组点击事件
			 */
			LinearLayout mGroupChatText = (LinearLayout) conentView.findViewById(R.id.group_chat_text);
			mGroupChatText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(MainTab.this, GroupListActivity.class));
					AddPopWindow.this.dismiss();
				}
			});
			/**
			 * 增加好友点击事件
			 */
			LinearLayout mAddContactsText = (LinearLayout) conentView.findViewById(R.id.add_contacts_text);
			mAddContactsText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(mContext, ContactsAddActivity.class));
					AddPopWindow.this.dismiss();
				}
			});
//			this.setOnDismissListener(new poponDismissListener());
		}

		/**
		 * 显示popupWindow
		 *
		 * @param parent
		 */
		public void showPopupWindow(View parent) {
			if (!this.isShowing()) {
				// 以下拉方式显示popupwindow
				this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);// 显示popupwindow的位�?
			} else {
				this.dismiss();
			}
		}
	}
	/**
	 * 把优先级较低的请求都放在登陆完成 2分钟后执�?
	 */
	private void doAfterLoginFinishDelay2Mins(){
		mainHandler.postDelayed(new Runnable() {
			public void run() {
				checkVersionUpdate();
				sendPaymentAnalysis();
			}
		}, 2*60000);
	}
	/**
	 * 判断版本是否需要升�?
	 */
	private void checkVersionUpdate() {

		final long lastVerAppTime = SharePreferenceUtils.getInstance(this).getLastCheckTime();
		final long lastVerTagsTime = SharePreferenceUtils.getInstance(this).getCurrentTagsTime();
		PalmchatLogUtils.println("MainAct  postDelayed    " + isLoginForground);
		long currentTime = System.currentTimeMillis();
		if((currentTime - lastVerAppTime) > DefaultValueConstant.ONDDAYTIME){//24小时检查一�?
			if(lastVerAppTime==0){//如果是第一次启�? 那也不需要去检查更�?
				SharePreferenceUtils.getInstance(this).setCurrentVerCheckTime();
			}else{
				String product_id = getString(R.string.product_id);
				UpdateVersion.getUpdateVersion().AfHttpVersionCheck(product_id, CacheManager.getInstance().getMyProfile().afId, getPackageName(),ReadyConfigXML.R_DSRC,null, MainTab.this);
			}
		}

		if((currentTime - lastVerTagsTime) > DefaultValueConstant.ONDDAYTIME){
			//tags词典
			String tag_ver = SharePreferenceUtils.getInstance(MainTab.this).getCurrentTagsVersion();
			PalmchatLogUtils.i(TAG,"tag--cur===ver==="+tag_ver);
			mAfCorePalmchat.AfHttpAfBCLangPackage(tag_ver,MainTab.this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 铃铛图标
			case R.id.broadcast_notification_id:
				// 红圈数量提示
			case R.id.broadcast_notification_count_id: {
				CommonUtils.cancelBrdNotification();
				Intent intent1 = new Intent(MainTab.this, BroadcastNotificationActivity.class);
				startActivity(intent1);
				mIsShowNotificationCount = false;
				break;
			}

			case R.id.broadcast_area_select:
				if (NetworkUtils.isNetworkAvailable(mContext)) {
					if (null == PalmchatApp.getApplication().getDefaultList()) {
						mAfCorePalmchat.AfHttpAfBcgetRegionBroadcast(MainTab.this);
						showProgressDialog(R.string.please_wait);
					} else {
						StartAreaAdapter();
					}
				}else{
					ToastManager.getInstance().show(mContext, mContext.getString(R.string.network_unavailable));
				}
				break;

			//引导页发送按�?
			case R.id.maintab_send_guide_id:{
				mV_Fab_guide.setVisibility(View.GONE);
				SharePreferenceUtils.getInstance(MainTab.this).setCurrentVersionnameByAccount(CacheManager.getInstance().getMyProfile().afId);
				showMaskingView();
				break;
			}
			case R.id.palmcall_guide_layout_id:{
				switch (mPalmCallClickCount){
					case 0:
						mV_PalmCall_Recent.setVisibility(View.GONE);
						mV_PalmCall_Recharge.setVisibility(View.VISIBLE);
						mV_PalmCall_Main_Intro.setVisibility(View.GONE);
						mPalmCallClickCount ++;
						break;
					case 1:
						mV_PalmCall_Recent.setVisibility(View.VISIBLE);
						mV_PalmCall_Recharge.setVisibility(View.GONE);
						mV_PalmCall_Main_Intro.setVisibility(View.GONE);
						mPalmCallClickCount ++;
						break;
					case 2:
						int dip = getResources().getDimensionPixelSize(R.dimen.palmcall_Hostlist_intro_Heigth);//增加头像下面控件的高度
						int dipVice = CommonUtils.dip2px(mContext,22);//上边距
						int headerPx = getResources().getDimensionPixelSize(R.dimen.header_More_Heigth);
						int listViewHeaderPx = getResources().getDimensionPixelSize(R.dimen.sendbroadcastshareto_height);
						int guideWidth = CommonUtils.dip2px(mContext,31);
						int guideVedioWidth = CommonUtils.dip2px(mContext,53);
						int height = (dip - dipVice)/2;
						int displayWidth = ImageUtil.DISPLAYW / 2;//iamge头像显示的宽高

						/************根据手机设置宽度************/
						RelativeLayout.LayoutParams layoutParams_guide_intro = (RelativeLayout.LayoutParams)mRl_Guide_intro.getLayoutParams();
						layoutParams_guide_intro.width = displayWidth + guideWidth;
						mRl_Guide_intro.setLayoutParams(layoutParams_guide_intro);

						/************根据手机设置左边距************/
						RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mRl_intro.getLayoutParams();
						if(layoutParams != null){
							layoutParams.setMargins(displayWidth - guideVedioWidth,0,0,0);
						}
						/************根据手机设置上边距************/
						mColLayoutParams =(LinearLayout.LayoutParams) mV_PalmCall_Intro.getLayoutParams();
						if(mColLayoutParams != null){
							mColLayoutParams.setMargins(0,(displayWidth + height + headerPx + listViewHeaderPx),0,0);
						}
						mV_PalmCall_Recent.setVisibility(View.GONE);
						mV_PalmCall_Recharge.setVisibility(View.GONE);
						mV_PalmCall_Main_Intro.setVisibility(View.VISIBLE);
						mPalmCallClickCount ++;
						SharePreferenceUtils.getInstance(this).setPalmCallIsGuide(true);
						break;
					default:
						if(palmCallFragment != null){ //导航结束之后，弹出拨打对话框
							palmCallFragment.toPalmCallDetail();
						}
						mV_PalmCall_Main.setVisibility(View.GONE);
				}
			}
			break;
			default:
				break;
		}
	}

	/**
	 * 跳转到发送广播界�?
	 * @param imageUris
	 */
	private void toSendPhotoBroadcast(ArrayList<Uri> imageUris){
		Intent intent = new Intent(this, EditBroadcastPictureActivity.class);
		if(null!=imageUris){
			intent.putExtra(IntentConstant.SHAREIMAGE_URLS, imageUris);
		}
		intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, JsonConstant.KEY_SENDBROADCAST_TYPE_CAMERA);
		startActivityForResult(intent,IntentConstant.REQUEST_CODE_MAINTAB);
	}

	private void toSendVoiceBroadcast(){
		Intent intent = new Intent(this, ActivitySendBroadcastMessage.class);
		intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE,  Consts.BR_TYPE_VOICE_TEXT );
		startActivityForResult(intent,IntentConstant.REQUEST_CODE_MAINTAB);
	}
	/**
	 * 切换tab时让列表停止滑动
	 */
	private void stopListViewScroll(){
		int index = viewpager.getCurrentItem();
		switch (checkPositionByChannel(index)) {
			case CONTENT_FRAGMENT_HOME:{

				if(null!=homeFragment){
					homeFragment.stopListViewScroll();
				}
				break;
			}
			case CONTENT_FRAGMENT_BROADCAST:{
				if(null!=broadcastFragmentTab){
					broadcastFragmentTab.stopListViewScroll();
				}
				break;
			}
			/*case CONTENT_FRAGMENT_PALMPLAY:{
				if(null!=palmPlayHome){
					palmPlayHome.stopListViewScroll();
				}
				break;
			}*/
			default:
				break;
		}
	}
	/**
	 * 第一次舒适化坐标�?
	 */
	private boolean initCoord(float titleYCoord,boolean isExtraTabs){
		mTitleYCoord=titleYCoord;
		mStatusBarHeight = CommonUtils.getStatusBarHeight(this);
		if((null!=broadcastFragmentTab)&&(isExtraTabs)){
			float height = broadcastFragmentTab.getLayoutHeight();
			if(height==0){
				return false;
			}
			mTitleRealHeight=height+10;
		} else {
			mTitleRealHeight = mTitleLinearLayout.getHeight();
		}
		if(null!=broadcastFragmentTab){
			broadcastFragmentTab.initCoord(mOriginTabsTop);
		}
		mIsFirstRun = false;
		return true;
	}

	/**
	 * 移动titile
	 * @param scrollH
	 * @param isExtraTabs
	 */
	public void moveTitle(float scrollH,boolean isExtraTabs){
		if(scrollH<0.0){
			hideTitle(scrollH, isExtraTabs);
		} else if(scrollH>0.0){
			showTitle(scrollH, isExtraTabs);
		}
	}

	/**
	 * 下拉显示title
	 * @param scrollH
	 */
	@SuppressLint("NewApi")
	public void showTitle(float scrollH,boolean isExtraTabs) {
		float titleYCoord = mTitleLinearLayout.getY();
		float layoutY = titleYCoord+scrollH;
		if(mIsFirstRun) {
			if(!initCoord(titleYCoord, isExtraTabs)){
				return;
			}
		}

		if(layoutY<mTitleYCoord){
			mTitleLinearLayout.setY(layoutY);
		}  else {
			layoutY = mTitleYCoord;
			mTitleLinearLayout.setY(mTitleYCoord);
		}
		if((null!=broadcastFragmentTab)&&(isExtraTabs)){
			broadcastFragmentTab.tabsShow(layoutY,scrollH);
		}
	}

	/**
	 * 上划隐藏title
	 * @param scrollH
	 */
	@SuppressLint("NewApi")
	public void hideTitle(float scrollH,boolean isExtraTabs) {

		float titleYCoord = mTitleLinearLayout.getY();
		float layoutY = titleYCoord+scrollH;
		if(mIsFirstRun){
			if(!initCoord(titleYCoord, isExtraTabs)){
				return;
			}
		}

		if(layoutY+mTitleRealHeight>=0){
			mTitleLinearLayout.setY(layoutY);
		} else {
			layoutY = -(mTitleRealHeight+4);
			mTitleLinearLayout.setY(layoutY);
		}

		if((null!=broadcastFragmentTab)&&(isExtraTabs)){
			broadcastFragmentTab.tabsHide(layoutY,scrollH);
		}
	}

	/**
	 * 重置title位置
	 */
	@SuppressLint("NewApi")
	public void resetTitlePosition(){
		if((mTitleLinearLayout!=null)&&(mTitleLinearLayout.getY()<mTitleYCoord)){
			mTitleLinearLayout.setY(mTitleYCoord);
		}

		if(null!=broadcastFragmentTab){
			broadcastFragmentTab.resetTabsPosition();
		}
	}

	/**
	 * 获取title的坐�?
	 * @return
	 */
	@SuppressLint("NewApi")
	public float getTitleY(){
		return mTitleYCoord;
	}

	public void hideStateBtn(){
		mBroadcastAreaSelect.setVisibility(View.GONE);
	}

	public void shouStateBtn(){
		mBroadcastAreaSelect.setVisibility(View.VISIBLE);
		int number = viewpager.getCurrentItem();
		if(number == CONTENT_FRAGMENT_HOME){
			mBroadcastAreaSelect.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示蒙层
	 */
	public void showMaskingView(){
		if(mSendBroadcastMaskingView.getVisibility()!= View.VISIBLE){
			if(broadcastFragmentTab!=null){
				broadcastFragmentTab.showViewFab(false);
			}
			mSendBroadcastMaskingView.show(mainHandler);
		}
	}

	/**
	 * 显示FabBUtton
	 */
	private void showFabButton(){
		if(null!=broadcastFragmentTab){
			broadcastFragmentTab.showViewFab(true);
		}
	}

	/**
	 * 隐藏FabBUtton
	 */
	private void setFabButton(final boolean state){
		mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(null!=broadcastFragmentTab){
					broadcastFragmentTab.showViewFab(state);
				}
			}
		},100);

	}

	/**
	 * 设置添加屏幕的背景透明�?
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	/** Explore是否显示红点提示
	 * @param isCheck
	 * @param isChangeCountry 国定是否改变
	 * @return true显示，false不显�?
	 */
	private boolean isShowExploreTips(boolean isCheck, boolean isChangeCountry){
		if (isCheck) {
			if (!isChangeCountry) {
				if (!CacheManager.getInstance().hasNewStoreProInfo() && !SharePreferenceUtils.getInstance(this).getUnReadExchange() // 充值成功信息已�?
//						&& !SharePreferenceUtils.getInstance(this).getUnReadPredictRecord()// 开奖信息已�?{
//						&& !SharePreferenceUtils.getInstance(this).getUnReadPredictPrize()
//						&& !SharePreferenceUtils.getInstance(this).getUnReadNewPalmGuess()// 派奖消息
						&& !SharePreferenceUtils.getInstance(this).getBalancePalmcoinRedDot()
						&& !SharePreferenceUtils.getInstance(this).getIsAtme(myAfid)) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void judgeSkipEditBroadcast(){
		Intent intent =getIntent();
		ArrayList<Uri> imageUris = null;
		if(null!=intent){
			try{
				imageUris = (ArrayList<Uri>)intent.getSerializableExtra(IntentConstant.SHAREIMAGE_URLS);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		if(null!=imageUris){
			toSendPhotoBroadcast(imageUris);
		}
	}
	/*begin add by liuzhi for hide palmplay store 20160505*/
	private int checkPositionByChannel(int oldPosition) {
		int newPosition = oldPosition;
		PalmchatLogUtils.d("mengjie","oldPosition:"+ oldPosition +"=====listFragemnt.sinze:"+listFragments.size());
		if (!PalmchatApp.getApplication().isCountry()) {
			if(oldPosition >= CONTENT_FRAGMENT_PALMCALL ) {
				newPosition = oldPosition + 1;
				PalmchatLogUtils.d("mengjie","checkposition点击了加1了："+oldPosition);
			}
		}
		return newPosition;
	}

	private int getIndexByChannel(int oldPosition) {
		int index = oldPosition;
		PalmchatLogUtils.d("mengjie","getIndexByChannel点击了？" + "oldPostion:"+oldPosition);
		if (!PalmchatApp.getApplication().isCountry()) {
			if (oldPosition > CONTENT_FRAGMENT_PALMCALL) {
				index = oldPosition - 1;
				PalmchatLogUtils.d("mengjie","oldPosition-1了");
			}
		}
		return index;
	}
	/**
	 *
	 * @param isShow
	 */
	public void showRegionSelectTip(boolean isShow){
		if(isShow){
			mRL_RegionSelectTip.setVisibility(View.VISIBLE);
			AnimationController.createAlphaAnimation(DefaultValueConstant.DURATION_ANIMATION_500,mRL_RegionSelectTip,0.0F,1.0F).start();
			mainHandler.postDelayed(mRunnable,DefaultValueConstant.DURATION_STATIC_2S);
		} else {
			mRL_RegionSelectTip.setVisibility(View.GONE);
		}

	}

	/**
	 * 显示Palmcall新手导航
	 */
	public void showPalmcallGuide(boolean isCheckCurrent){
		if(!SharePreferenceUtils.getInstance(mContext).getPalmCallIsGuide() && (isCheckCurrent || currIndex == CONTENT_FRAGMENT_PALMCALL)) {
			if(mPalmCallClickCount == 0) {
				mV_PalmCall_Main.setBackgroundColor(getResources().getColor(R.color.palmcall_guide_backgroud_color));
				mV_PalmCall_Main.setVisibility(View.VISIBLE);
				mV_PalmCall_Recent.setVisibility(View.GONE);
				mV_PalmCall_Main_Intro.setVisibility(View.GONE);
				mV_PalmCall_Recharge.setVisibility(View.VISIBLE);
				mPalmCallClickCount++;
			}
		}
	}

	public void setPalmcallTransparent(boolean isCheckCurrent){
		if(!SharePreferenceUtils.getInstance(mContext).getPalmCallIsGuide() && (isCheckCurrent || currIndex == CONTENT_FRAGMENT_PALMCALL)) {
			mV_PalmCall_Main.setVisibility(View.VISIBLE);
			mV_PalmCall_Main.setBackgroundColor(getResources().getColor(R.color.transparent));
			mV_PalmCall_Recent.setVisibility(View.GONE);
			mV_PalmCall_Main_Intro.setVisibility(View.GONE);
			mV_PalmCall_Recharge.setVisibility(View.GONE);
		}
	}
	/**
	 * 隐藏动画
	 */
	private void showTipHideAnimation(){
		AnimationController.createAlphaAnimation(DefaultValueConstant.DURATION_ANIMATION_500,mRL_RegionSelectTip,1.0F,0.0F).start();
	}
	/*end add by liuzhi for hide palmplay store 20160505*/

	/**
	 * 国家改变的通知
	 *
	 * @param event
	 */
	public void onEventMainThread(ChangeCountryEvent event) {
		showPalmCall();
	}

	/**
	 * 显示PalmCall主页内容
	 */
	private void showPalmCall(){
		isChange = true;
		mIsShowPalmCall = PalmchatApp.getApplication().isCountry();
		isShowPalmcall(mIsShowPalmCall,false);
		if (mIsShowPalmCall) {
			for(Fragment fragment : listFragments){
				if(fragment instanceof PalmCallFragment){
					return;
				}
			}
			palmCallFragment = new PalmCallFragment();
			listFragments.add(2, palmCallFragment);
			adapter.notifyDataSetChanged();
			if (currIndex == 2) {//若当前选中的chats，那么增加了palmCall之后，需要还是显示chats，索引就需要加1
				viewpager.setCurrentItem(currIndex + 1);
				showSelectedOption(currIndex);
			} else if (currIndex >= 3) {//若当前选中的explore，那么删除了palmCall之后，需要显示explore
				viewpager.setCurrentItem(currIndex + 1);
				showSelectedOption(currIndex);
			}
		} else {
			if (palmCallFragment != null) {
				listFragments.remove(palmCallFragment);
				adapter.notifyDataSetChanged();
				PalmchatLogUtils.d("mengjie", "是否删除过");

				if (currIndex == 2) {//若当前选中的PalmCall，那么删除了palmCall之后，需要显示chats
					viewpager.setCurrentItem(currIndex);
					showSelectedOption(currIndex);
					showTitleOption(currIndex);
				} else if (currIndex >= 3) {//若当前选中的Chasts，那么删除了palmCall之后，需要显示chats
					viewpager.setCurrentItem(currIndex - 1);
					showSelectedOption(currIndex);
				}
			}
			PalmchatLogUtils.d("mengjie", "改变地区了国家不是尼日利亚的");
		}
		setOnMyClick();
	}

	/**
	 * 显示PalmCall下方导航
	 */
	private void isShowPalmcall(boolean isShowPalmcall,boolean checkForegroundLogin){
		if(isShowPalmcall){
			PalmcallManager.getInstance().setSupportPalmcall(true);
			findViewById(R.id.frameLayout_palmpcall).setVisibility(View.VISIBLE);
			findViewById(R.id.fl_palmcall_tab).setVisibility(View.VISIBLE);
			PalmchatLogUtils.d("mengjie","改变了国家，是否创建了Fragment");
			if(!PalmcallManager.getInstance().isPalmcallLogined()){
				PalmchatLogUtils.i(TAG,"-------------notlogined-----------------");
				PalmcallManager.getInstance().loginPalmchatCall(DefaultValueConstant.PALMCLAL_LOGIN_DURATION);
			} else if(checkForegroundLogin){
				PalmchatLogUtils.i(TAG,"------------checkForegroundLogin---true-------logoutPalmcall------");
				PalmcallManager.getInstance().logoutPalmcall();
			}
		}else{
			PalmcallManager.getInstance().setSupportPalmcall(false);
			findViewById(R.id.frameLayout_palmpcall).setVisibility(View.GONE);
			findViewById(R.id.fl_palmcall_tab).setVisibility(View.GONE);
			PalmchatLogUtils.d("mengjie","判断国家不是尼日利亚的就删除fragment:"+listFragments.size());
			PalmcallManager.getInstance().logoutPalmcall();
		}
	}

	/**
	 * zhh
	 * 支付统计上报
	 */
	private void sendPaymentAnalysis() {
		try{
			JSONArray jsonArr = new JSONArray();
			fileMap = FileUtils.getFileList(RequestConstant.MOBILE_TOP_UP_CACHE);
			Iterator iter = fileMap.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String data = (String) entry.getValue();
				jsonArr.put(i, data.toString());
				i++;
			}
			mAfCorePalmchat.AfHttpPaymentAnalysis(jsonArr.toString(), null, this);
		}catch (Exception e) {
			e.printStackTrace();
		}

	}



}
