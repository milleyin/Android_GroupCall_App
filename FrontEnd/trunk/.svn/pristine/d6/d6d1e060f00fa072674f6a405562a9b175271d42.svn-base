package com.afmobi.palmchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.BroadcastFollowsNotificationEvent;
import com.afmobi.palmchat.eventbusmodel.ChatRoomListEvent;
import com.afmobi.palmchat.eventbusmodel.DestroyGroupEvent;
import com.afmobi.palmchat.eventbusmodel.EventLoginBackground;
import com.afmobi.palmchat.eventbusmodel.EventRefreshFriendList;
import com.afmobi.palmchat.eventbusmodel.PalmGuessNotificationEvent;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobi.palmchat.eventbusmodel.RefreshFollowerFolloweringOrGroupListEvent;
import com.afmobi.palmchat.eventbusmodel.UpdateCoinsEvent;
import com.afmobi.palmchat.eventbusmodel.UpdatePalmCallDurationEvent;
import com.afmobi.palmchat.gif.GifDrawable;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.network.dataparse.CommonJson;
import com.afmobi.palmchat.services.AppStatusService;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.PopMessageActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomMainAct;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.groupchat.PopGroupMessageActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.RefreshNotificationManager;
import com.afmobi.palmchat.ui.activity.main.helper.RefreshQueueManager;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallLoginDelegate;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallManager;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.ui.activity.setting.AboutActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastNotificationData;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BrandXMLUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FolllowerUtil;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.LanguageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SettingMode;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfAttachPalmCoinSysInfo;
import com.core.AfDPProxyItem;
import com.core.AfFriendInfo;
import com.core.AfGrpNotifyMsg;
import com.core.AfGrpProfileInfo;
import com.core.AfLoginInfo;
import com.core.AfMessageInfo;
import com.core.AfOnlineStatusInfo;
import com.core.AfPalmCallResp;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfVersionInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
import com.core.param.AfCacheParam;
import com.core.param.AfPhoneInfoParam;
import com.facebook.FacebookSdk;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TimeZone;

import de.greenrobot.event.EventBus;

/**
 * Application 类
 *
 */
public class PalmchatApp extends MultiDexApplication implements AfHttpSysListener, AfHttpResultListener, UncaughtExceptionHandler{

	/** Application TAG */
	private static final String TAG = PalmchatApp.class.getCanonicalName();
	/** apk */
	public static final String _apk = ".apk";
	/** 异常捕获 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** jni接口类 */
	public AfPalmchat mAfCorePalmchat;
	/** 用于获取SIM卡的 imsi等 */
	private TelephonyManager tm;
	/** 提示音消息音等个人设置 */
	private SettingMode settingMode;
	private static PalmchatApp sPalmchatApp = null;
	/** 是否已经获取要登录的账号 */
//	private static boolean isLoadAccount = false;
	public DisplayMetrics dm;
	/** 来消息提示音等各类声音管理 */
	private SoundManager soundManager;
	private Context context;
	// 同步广播到Facebook的开关
	private boolean mFacebookShareClose = true;
	/**后台请求队列 如请求发消息的人的profile*/
	private HashMap<String, AfMessageInfo> mReqInfoMap = new HashMap<String, AfMessageInfo>();
	/** Handler Map -- Activity */
//	private Map<String, Handler> handlerMap = new HashMap<String, Handler>();
	/** 存储好友在线状态 */
	private Map<String, AfOnlineStatusInfo> onlineStatusInfoMap = new HashMap<String, AfOnlineStatusInfo>();
	/** 当前activity */
	private static Activity curActivity;
	/** 手机系统信息 */
	public static OSInfo osInfo;
	/** 服务器推送的私聊 群聊 聊天室 等信息 接收者的对象列表， 如Chatting GroupChatting等需要接收的 */
	private ArrayList<MessageReceiver> mMsgRecList = new ArrayList<MessageReceiver>();
	/** 已读消息接收者 */
	private MessageReadReceiver messageReadReceiver;
	/** 中间件是否已完成本地加载好友列表，黑名单，群组等信息完成后通知init,用于调中间件poll请求开始的判断 */
	private boolean isInit = false;
	/** 软键盘监听 */
	public ColseSoftKeyBoardLister mColseSoftKeyBoardListe;
	/** 是否有新版本 */
	private boolean hasNewVersion = false;

	/**
	 * 是否开放PalmGuess,如果开放则设置为true
	 */
	public final boolean isOpenPalmGuess = false;
	private static final float MEMORY_PERCENT = 0.06f;
	private static final int DEFAULT_MEM_CACHE_SIZE = Math.round(MEMORY_PERCENT * Runtime.getRuntime().maxMemory() / 1024);
    public LruCache<String, GifDrawable> mMemoryCache;// 给GIF表情动画设置内存缓存
	/**pop时，是否双击屏幕*/
	public boolean isDoubleClickScreen;
	/** 保存那些地区有广播的 */
	private ArrayList<AfResponseComm.AfBCRegionBroadcast> broadcast_area_list;
	/** 跳转服务器地址 */
//	private String mWebJumpUrl = "";
	/** 表情url地址 */
	private String mGifFace_Url = "";
	private LocalBroadcastManager mCallSdkLocalBroadcastManager;
	private String mHostAddr_Palmcall = "";

	/**
	 * copy so 库到data/data/jniLibs/
	 * 
	 * @param context
	 * @param fileName
	 * @param path
	 * @return
	 */
	private boolean copyFileFromAssets(Context context, String fileName, String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			// file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e("MainActivity", "[copyFileFromAssets] IOException
			// "+e.toString());
		}
		return copyIsFinish;
	}

	/**
	 * 载入So库
	 * 
	 * @param fileName
	 */
	private void loadSoLib(String fileName) {
		try {
			File dir = this.getDir("jniLibs", Activity.MODE_PRIVATE);
			File distFile = new File(dir.getAbsolutePath() + File.separator + fileName);
			/** 当第一次安装和版本号不同时才从assets下拷贝到内存中 */
			int verioncode = SharePreferenceUtils.getInstance(this).getVerisonCodeByPackage();
			int currentVersionCode = AppUtils.getAppVersionCode(this);
			if (PalmchatLogUtils.DEBUG || verioncode == 0 || verioncode != currentVersionCode) {
				if (copyFileFromAssets(this, fileName, distFile.getAbsolutePath())) {
					// 使用load方法加载内部储存的SO库
					System.load(distFile.getAbsolutePath());
				}

			} else {
				System.load(distFile.getAbsolutePath());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate() {
		super.onCreate();
		loadSoLib("libafmobi.so");
		loadSoLib("libafmobigif.so");
		loadSoLib("libPatcher.so");
		loadSoLib("libgpuimage-library.so");
		/** 根据包名保存当前版本号 */
		SharePreferenceUtils.getInstance(this).setVerisonCodeByPackage();

		dm = new DisplayMetrics();
		((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		sPalmchatApp = this;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		try {
			buildOsInfo();// 获取手机版本号等相关信息
		} catch (Exception e) {
			PalmchatLogUtils.e("onCreate", "Exception " + e.getMessage());
			e.printStackTrace();
		}

		PalmchatLogUtils.println("PalmchatApp  PalmchatApp.getOsInfo().getMcc()  end  " + PalmchatApp.getOsInfo().getMcc());

		// init app language
		String sCurrentLocalCode = SharePreferenceUtils.getInstance(this).getLocalLanguage();
		LanguageUtil.updateLanguage(this, sCurrentLocalCode);

		initAfPalmchatCore();
		CommonUtils.getInitData(PalmchatApp.this); // cc和country初始化
		long begin = System.currentTimeMillis();
		PalmchatLogUtils.e("PalmchatApp", "PalmchatApp  onCreate begin  " + begin);
		ImageManager.getInstance().initImageLoader(this);// UIL初始化

		settingMode = new SettingMode(this);// APP 配置
		soundManager = new SoundManager(this);
		soundManager.addSound(1, R.raw.msg);
		FacebookSdk.sdkInitialize(getApplicationContext());
		mMemoryCache = new LruCache<String, GifDrawable>(DEFAULT_MEM_CACHE_SIZE) {// 给GIF表情动画设置内存缓存

			@Override
			protected int sizeOf(String key, GifDrawable value) {
				// TODO Auto-generated method stub
				final int size = (int) (value.getAllocationByteCount() / 1024);
				System.out.println("--sss sizeOf:" + size);
				return size == 0 ? 1 : size;
			}

			@Override
			protected void entryRemoved(boolean evicted, String key, GifDrawable oldValue, GifDrawable newValue) {
				// TODO Auto-generated method stub
				oldValue.recycle();
				oldValue = null;
				System.out.println("--sss entryRemoved");
			}
		};


		loadLoginInfoFromDB();
		if (osInfo != null) {
			osInfo.toString();
		}

		if (ImageUtil.DISPLAYH < ImageUtil.DISPLAYW) {
			int tmp = ImageUtil.DISPLAYH;
			ImageUtil.DISPLAYH = ImageUtil.DISPLAYW;
			ImageUtil.DISPLAYW = tmp;
		}

		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
		strategy.setAppChannel(ReadyConfigXML.R_DSRC);
		CrashReport.initCrashReport(this, "900012321", PalmchatLogUtils.DEBUG);
		PalmchatLogUtils.i(TAG,"-----------onCreate-------------");
		//palmcall 初始化
		PalmcallManager.getInstance().justCallInit();
	}

    /**
     * 从数据库读取登陆过的账号信息
     */
    private void loadLoginInfoFromDB(){
        AfLoginInfo afLoginInfo = null; // 如果客户端本地保存为空 那就从中间件获取登录过的账号
        AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
        if (myAccounts != null && myAccounts.length > 0) {
            afLoginInfo = myAccounts[0];
            CacheManager.getInstance().setLoginInfo(afLoginInfo);// 登陆信息设置
        }

        if (afLoginInfo != null) {
            String afid = afLoginInfo.afid;
            String pass = afLoginInfo.password;
            String cc = afLoginInfo.cc;
            int status = afLoginInfo.status;
            String email = afLoginInfo.email;
            String phone = afLoginInfo.phone;
            AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
            myAfProfileInfo.afId = afid;
            myAfProfileInfo.email = email;
            myAfProfileInfo.phone = phone;
            myAfProfileInfo.password = null;
            // myAfProfileInfo.login_type = afLoginInfo.type;
            myAfProfileInfo.login_status = status;
            CacheManager.getInstance().setMyProfile(myAfProfileInfo);// 设置当前要登录的账号
            if ((AfLoginInfo.AFMOBI_LOGIN_INFO_NORMAL != status && AfLoginInfo.AFMOBI_LOGIN_INFO_UNNORMAL_EXIT != status) && !TextUtils.isEmpty(afid)) {
                PalmchatLogUtils.println("PalmchatApp  again  status  " + status + "  afid  " + afid + "  pass  " + pass + "  cc  " + cc);
                mAfCorePalmchat.AfCoreOpenDatabase(afid);
                AfProfileInfo profileInfo = (AfProfileInfo) mAfCorePalmchat.AfDbProfileGet(Consts.AFMOBI_DB_TYPE_PROFILE, afid);
                if (profileInfo == null) {
                    return;
                }

                PalmchatLogUtils.println("PalmchatApp  profileInfo  " + profileInfo + "  profileInfo.status  " + profileInfo.status + "  profileInfo.afid  " + profileInfo.afId + "  profileInfo.pass  " + profileInfo.password +
                        // " PalmchatApp.getIsLoadAccount "+getIsLoadAccount()+
                        "  profileInfo.phone  " + profileInfo.phone + "  profileInfo.pbsn  " + profileInfo.pbsn + "  profileInfo.gpsn  " + profileInfo.gpsn + " profileInfo head_img_path  " + profileInfo.head_img_path);
                profileInfo.password = pass;
                myAfProfileInfo.password = pass;
                afLoginInfo.pbsn = profileInfo.pbsn;
                afLoginInfo.gpsn = profileInfo.gpsn;
                // afLoginInfo.blsn=profileInfo.blsn;//中间件做掉了
                // afLoginInfo.fdsn=profileInfo.fdsn;
                CacheManager.getInstance().setMyProfile(profileInfo);
                // PalmchatApp.setIsLoadAccount(true);
                settingMode.getSettingValue();
                mAfCorePalmchat.AfSetMcc(afLoginInfo.mcc);
                PalmchatApp.getOsInfo().setAfid_mcc(afLoginInfo.mcc);
                mAfCorePalmchat.AfLoadAccount(afLoginInfo);// 告诉中间件
                // 中间件根据afLoginInfo后台自动登陆
                mAfCorePalmchat.AfDbLoginSetStatus(afid, AfLoginInfo.AFMOBI_LOGIN_INFO_LOGINED);

                PalmchatLogUtils.e("WXL", "On Create :third_account=" + afLoginInfo.third_account + "login.type=" + afLoginInfo.type);
            }
        }
    }
	@Override
	public void onTerminate() {
		PalmchatLogUtils.e("PalmchatApp", "PalmchatApp  onTerminate ");
		PalmcallLoginDelegate.destroy();
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		PalmchatLogUtils.e("PalmchatApp", "PalmchatApp  onLowMemory ");
		super.onLowMemory();
	}


	/**
	 * 获取context
	 * 
	 * @return context
	 */
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/*public Map<String, Handler> getHandlerMap() {
		return handlerMap;
	}*/

	/*
	 * public boolean getIsLoadAccount() { return PalmchatApp.isLoadAccount; }
	 * 
	 * public static void setIsLoadAccount(boolean isLoadAccount) {
	 * PalmchatApp.isLoadAccount = isLoadAccount; }
	 */
	/**用来保存私聊过的人的在线状态信息，防止每次都要重新请求*/
	public Map<String, AfOnlineStatusInfo> getOnlineStatusInfoMap() {
		return onlineStatusInfoMap;
	}

	public void setOnlineStatusInfoMap(String key, AfOnlineStatusInfo statusInfo) {
		onlineStatusInfoMap.put(key, statusInfo);
	}

	synchronized public static PalmchatApp getApplication() {
		return sPalmchatApp;
	}

	/**
	 * 获得设置数据
	 * 
	 * @return settingMode
	 */
	public SettingMode getSettingMode() {
		return settingMode;
	}

	/**
	 * 配置设置数据
	 * 
	 * @param settingMode
	 */
	public void setSettingMode(SettingMode settingMode) {
		this.settingMode = settingMode;
	}

	/**
	 * 设置当前activity
	 * 
	 * @param curActivity
	 */
	public static void setCurActivity(Activity curActivity) {
		PalmchatApp.curActivity = curActivity;
	}

	/**
	 * 获取当前activity
	 * 
	 * @return
	 */
	public static Activity getCurActivity() {
		return curActivity;
	}

	/**
	 * 设置跟手机系统相关信息
	 * 
	 * @throws Exception
	 */
	public void buildOsInfo() throws Exception {
		PalmchatLogUtils.println("PalmchatApp onCreate buildOsInfo entry");
		osInfo = new OSInfo();
		CommonUtils.getLocalIpAddress();
		// osInfo.setImsi( tm.getSubscriberId());
		osInfo.setImsi(tm.getSubscriberId());

		osInfo.setBrand(android.os.Build.BRAND);
		String ua = android.os.Build.MODEL;

		osInfo.setUa(ua);

		PalmchatLogUtils.println("User Agent ua  " + ua);
		/** google play begin */
		/** begin */
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = getApplicationContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getApplicationContext().getPackageName(), 0);
			versionName = pi.versionName;
			osInfo.setCver(versionName);

			if (versionName == null || versionName.length() <= 0) {
				versionName = "";
			}
		} catch (Exception e) {
			PalmchatLogUtils.e("VersionInfo", "Exception " + e.getMessage());
		} finally {
			osInfo.setOsVersion(RequestConstant.ANDROID + android.os.Build.VERSION.RELEASE);
		}
		/** end */
		/** google play end */

		Map<String, List<String>> brandMap = BrandXMLUtils.parse(this);
		for (Map.Entry<String, List<String>> entry : brandMap.entrySet()) {// 获取传音手机的品牌
			for (String item : entry.getValue()) {
				if (ua.equalsIgnoreCase(item)) {
					osInfo.setBrand(entry.getKey());
				}
			}
		}
		osInfo.setImei(CommonUtils.getRightIMEI(tm.getDeviceId()));
	}

	/**
	 * 获得手机系统信息
	 * 
	 * @return
	 */
	public static OSInfo getOsInfo() {
		return osInfo;
	}

	/**
	 * 设置手机系统信息
	 * 
	 * @param os
	 */
	public static void setOsInfo(OSInfo os) {
		osInfo = os;
	}

	/**
	 * 当某个类需要接收消息时 先调次接口add进来
	 * 
	 * @param messageReceiver
	 */
	public void addMessageReceiver(MessageReceiver messageReceiver) {
		mMsgRecList.add(messageReceiver);
	}

	/**
	 * 当在监听接口中的类要销毁时 调此接口移出监听队列
	 * 
	 * @param messageReceiver
	 */
	public void removeMessageReceiver(MessageReceiver messageReceiver) {
		mMsgRecList.remove(messageReceiver);
	}

	/**
	 * 私聊 群聊 聊天室等消息的 接收者接口
	 */
	public interface MessageReceiver {
		void handleMessageFromServer(AfMessageInfo afMessageInfo);
	}

	/**
	 * 设置接收 已读消息的对象
	 * 
	 * @param messageReadReceiver
	 *            现在只用于私聊
	 */
	public void setMessageReadReceiver(MessageReadReceiver messageReadReceiver) {
		this.messageReadReceiver = messageReadReceiver;
	}

	/**
	 * 已读消息接收者接口 (用于私聊消息对方已读的通知）
	 * 
	 * @author
	 *
	 */
	public interface MessageReadReceiver {
		void handleMessageForReadFromServer(AfMessageInfo afReadInfo);
	}

	/**
	 * 灭屏锁屏下的弹框消息接收者
	 */
	private PopMessageReceiver mPopmessageReceiver;

	/**
	 * 弹框消息接收者接口
	 */
	public interface PopMessageReceiver {
		void handlePopMessageFromServer(AfMessageInfo afMessageInfo);
	}

	/**
	 * 设置popmessage接收器
	 * 
	 * @param popmessageReceiver
	 */
	public void setPopMessageReceiver(PopMessageReceiver popmessageReceiver) {
		this.mPopmessageReceiver = popmessageReceiver;
	}


	/**
	 * 设置软键盘监听器
	 * 
	 * @param listener
	 */
	public void setColseSoftKeyBoardListe(ColseSoftKeyBoardLister listener) {
		mColseSoftKeyBoardListe = listener;
	}

	/**
	 * 主要用于灭屏并且显示输入法，弹出窗口时，关闭输入法(有时第三方输入法未关闭)
	 * 
	 * @author Transsion
	 *
	 */
	public interface ColseSoftKeyBoardLister {
		void onColseSoftKeyBoard();
	}

	/**
	 * 是否有新版本
	 * 
	 * @return hasNewVersion
	 */
	public boolean isHasNewVersion() {
		return hasNewVersion;
	}

	/**
	 * 设置是否有新版本
	 * 
	 * @param hasNewVersion
	 */
	public void setHasNewVersion(boolean hasNewVersion) {
		this.hasNewVersion = hasNewVersion;
	}

	/**
	 *
	 * @return true 本地加载好友列表，黑名单，群组等信息完成后通知init
	 */
	public boolean isInit() {
		return isInit;
	}

	/**
	 * 当退出登录时置为false
	 * 
	 * @param isInit
	 */
	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	/**
	 * 中间件本地加载好友列表，黑名单，群组等信息完成后通知AF_SYS_MSG_INIT
	 */
	private void parseAfSysMsgInit(){
		setInit(true);
		final AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		if (!CommonUtils.isEmpty(myAfProfileInfo.afId)) {
			new Thread(new Runnable() {
				public void run() {
					//保存自己的profile到数据库
					int msg_id = mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, CacheManager.getInstance().getMyProfile());
					PalmchatLogUtils.println("PalmchatApp  msg_id  " + msg_id);
				}
			}).start();
		}
		mAfCorePalmchat.AfPollSetStatus(Consts.POLLING_STATUS_RUNNING);
		AppStatusService.start(this, false);
	}

	/**
	 * notify the application after core relogin because of -66 or -65 或后台登陆
	 * @param wparam
     */
	private void parseAfSysMsgBackgroundRelogin(final Object wparam){
		if (wparam != null) {
			AfProfileInfo myNewAfProfileInfo = (AfProfileInfo) wparam;
			AfProfileInfo myOldAfProfileInfo = CacheManager.getInstance().getMyProfile();
			myNewAfProfileInfo.password = myOldAfProfileInfo.password;
			CacheManager.getInstance().setMyProfile(myNewAfProfileInfo);
			CacheManager.getInstance().setPalmGuessShow(myNewAfProfileInfo.palmguess_flag == 1);
			// CacheManager.getInstance().setOpenAirtime(myNewAfProfileInfo.airtime
			// == 1);
			CacheManager.getInstance().setRecharge_intro_url(myNewAfProfileInfo.recharge_intro_url); // 设置充值方式说明的
			// url
			/*boolean isShowPalmGuessNew = false;
			int ver = SharePreferenceUtils.getInstance(this).getPalmguessVersion(myNewAfProfileInfo.afId);
			if (ver < myNewAfProfileInfo.palmguess_version) {
				SharePreferenceUtils.getInstance(this).setPalmguessVersion(myNewAfProfileInfo.afId, myNewAfProfileInfo.palmguess_version);
				SharePreferenceUtils.getInstance(this).setUnReadNewPalmGuess(true);
				isShowPalmGuessNew = true;
			}*/
			EventBus.getDefault().post(new EventLoginBackground(false, true));
			CrashReport.setUserId(myNewAfProfileInfo.afId);
			PalmchatLogUtils.i(TAG,"-----to----do-----loginPalmchatCall---------");
			if(isCountry()){
				if(!PalmcallManager.getInstance().isPalmcallLogined()){
					PalmcallManager.getInstance().loginPalmchatCall(DefaultValueConstant.PALMCLAL_LOGIN_DURATION);
				}
			}
//			PalmcallManager.getInstance(this).loginPalmchatCall(DefaultValueConstant.PALMCLAL_LOGIN_DURATION);
		}
		PalmchatLogUtils.e(TAG, "AFMOBI_SYS_MSG_BACKGROUND_RELOGIN AppStatusService.start");
		AppStatusService.start(this, true);
	}
	/**
	 * 中间件开始在后台自动登录或返回失败只有-66等错误才会进来
	 */
	private void parseAfAutoLoginStatus( Object wparam, int lParam){
		// /这里还要验证-66情况的自动登录 显示loading 第一次登录如果失败 会不会进来?
		// public static final int AUTO_LOGIN_STATUS_TYPE_START = 0; /*send
		// to sys before auto-login*/
		// public static final int AUTO_LOGIN_STATUS_TYPE_FAILED = 1; /*send
		// to sys after auto-login*/
		if (lParam == Consts.AUTO_LOGIN_STATUS_TYPE_PARAM_FAILED) {// 登录参数错误，如登录方式为5（Facebook），但是FacebookID为空的情况
			// 就会返回该错误，
			// 就需要退到login界面
			CommonUtils.cancelNoticefacation(getApplicationContext());
			ToastManager.getInstance().show(this, R.string.request_params_error);
			MsgAlarmManager.getInstence().cancelAllAlarm();
			toLogin();
			PalmchatLogUtils.e("JNI SYSTEM", "AUTO_LOGIN_STATUS_TYPE_PARAM_FAILED");
		} else if (lParam == Consts.AUTO_LOGIN_STATUS_TYPE_FAILED) {// 后台登陆失败
			// 包括无网络的失败
			PalmchatLogUtils.i(TAG,"-----------AUTO_LOGIN_STATUS_TYPE_FAILED-------------");
			EventBus.getDefault().post(new EventLoginBackground(false, false));
		}
		PalmchatLogUtils.e(TAG, "AFMOBI_SYS_AUTO_LOGIN_STATUS  " + lParam);
	}

	/**
	 * 处理服务器推送的各种消息
	 * @param wparam
	 * @param lParam
     */
	private void parseAfSysMsgRecv(final Object wparam, int lParam){
		if (wparam != null) {
			// 把收消息任务加入线程池
			CacheManager.getInstance().getThreadPoolInstance().execute(new Thread() {
				public void run() {
					AfMessageInfo afMessageInfo = (AfMessageInfo) wparam;

					PalmchatLogUtils.println("--- bbb ---unread AfHttpSysMsgProc:" + afMessageInfo.unReadNum + " -- key:" + afMessageInfo.getKey());
					PalmchatLogUtils.println("--- thread id AF_SYS_MSG_RECV " + Thread.currentThread().getId());
					// received card
					if (MessagesUtils.isCardMessage(afMessageInfo.type)) {
						MessagesUtils.insertMsg(afMessageInfo, false, true);
						AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.msg);
						AfFriendInfo afFriendInfo1 = null;

						if (MessagesUtils.isPrivateMessage(afMessageInfo.type)) {
							afFriendInfo1 = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
						} else if (MessagesUtils.isGroupChatMessage(afMessageInfo.type)) {
							afFriendInfo1 = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.fromAfId);
						}

						// both from afid and msg afid 's profile are not exist
						// in cache
						if (afFriendInfo1 != null && afFriendInfo == null) {
							getInfoFromNet(afMessageInfo, Constants.GET_MSG);

							// from afid 's profile exist in cache, msg afid 's
							// profile not exist in cache
						} else if (afFriendInfo1 == null && afFriendInfo == null) {
							getInfoFromNet(afMessageInfo, Constants.GET_FROM_AFID_AND_MSG);

							// from afid 's profile not exist in cache, msg afid
							// 's profile exist in cache
						} else if (afFriendInfo1 != null && afFriendInfo != null) {
							updateUi(afMessageInfo);

							// both are exist in cache
						} else if (afFriendInfo1 == null && afFriendInfo != null) {

							getInfoFromNet(afMessageInfo, Constants.GET_FROM_AFID);
						}

						// received friend request
					} else if (MessagesUtils.isFriendReqMessage(afMessageInfo.type) || MessagesUtils.isFollowMessage(afMessageInfo.type)) {
						if (MessagesUtils.isFriendReqMessage(afMessageInfo.type)) {
							final AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(afMessageInfo.fromAfId);
							if (afF != null) {//如果已经在自己的好友列表中 直接发同意好友请求消息
								afMessageInfo.accept_friend_defualt = true;
								acceptFriendRequest(afMessageInfo);
								return;
							}
						}
						// 将好友请求消息放入缓存中
						if (MessagesUtils.insertFreqMsg(PalmchatApp.this, afMessageInfo, false, true) == Constants.DB__SUC) {
							updateUi(afMessageInfo);
						} else {
							// 本地此人的profile, 从服务器下载profile
							getInfoFromNet(afMessageInfo);
						}

						// friend request success
					} else if (MessagesUtils.isFriendReqSucMessage(afMessageInfo.type)) {
						PalmchatLogUtils.println("---Frd req Suc rec msg");
						AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
						if (afFriendInfo != null) {
							notifyFreqSucMsg(afMessageInfo);
						} else {
							getInfoFromNet(afMessageInfo);
						}
						// private chat or group chat
					} else if (MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isGroupChatMessage(afMessageInfo.type)) {
						// 将消息放入最近聊天列表中
						int status = MessagesUtils.insertMsg(afMessageInfo, false, true);

						// 群聊类型消息处理，cache中有 消息对应群的profile,则刷新界面。没有则从服务器下载profile
						// 再刷新界面
						if (MessagesUtils.isGroupChatMessage(afMessageInfo.type)) {

							if (MessagesUtils.queryFriend(afMessageInfo.fromAfId) != Constants.DB__SUC) {
								getInfoFromNet(afMessageInfo);
							}

							// if cache doesn't exist this group info
							if (status == Constants.DB__SUC) {
								updateUi(afMessageInfo);
							} else {
								getGrpInfoFromNet(afMessageInfo);
							}

							// 私聊类型消息处理，cache中有
							// 消息对应人的profile,则刷新界面。没有则从服务器下载profile 再刷新界面
						} else if (MessagesUtils.isPrivateMessage(afMessageInfo.type)) {
							// private msg
							if (status == Constants.DB__SUC) {
								updateUi(afMessageInfo);
							} else {
								getInfoFromNet(afMessageInfo);
							}

						}
						// 广播通知消息
					} else if (MessagesUtils.isBrdNotification(afMessageInfo.type)) {
						PalmchatLogUtils.println("AfHttpSysMsgProc isBrdNotification:" + afMessageInfo.msg);
						// 解析服务端返回的 json 字符串 ,包含 评论 赞 等 内容
						ArrayList<HashMap<String, List<BroadcastNotificationData>>> list = CommonJson.getInstance().parseBrdNotiJson(afMessageInfo.msg, true);
						if (list != null) {
							// 后台状态时显示通知
							if (isApplicationBroughtToBackground()) {
								ArrayList<HashMap<String, List<BroadcastNotificationData>>> data = CommonJson.getBrdNotifyDataFromDb(BroadcastNotificationData.STATUS_UNREAD);
								CommonUtils.showBroadCastNotification(PalmchatApp.this, data);
							}
							mHandler.obtainMessage(Constants.BROADCAST_NOTIFICATION).sendToTarget();
						}

						ArrayList<String> broadcast_friendscircle_newlist = CommonJson.getInstance().parseBrdFriendCircleNotiJson(afMessageInfo.msg);
						if (broadcast_friendscircle_newlist != null && broadcast_friendscircle_newlist.size() > 0) {
							SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).setBradcast_FriendCircleNew(broadcast_friendscircle_newlist.size());
							mHandler.obtainMessage(Constants.BROADCAST_FRIENDCIRCLE_NOTIFICATION).sendToTarget();
						}
					} else if (MessagesUtils.isPredictNotification(afMessageInfo.type)) {// Predict开奖消息
						// CommonJson.parsePredictRecordNotifyJosn(afMessageInfo.msg);
						mHandler.obtainMessage(Constants.PREDICT__NOTIFICATION).sendToTarget();
					} else if (MessagesUtils.isPredictPayNotification(afMessageInfo.type)) { // Predict充值成功消息
						mHandler.obtainMessage(Constants.PREDICT__PAYMENT_NOTIFICATION).sendToTarget();
					} else if (MessagesUtils.isPredictPrizeNotification(afMessageInfo.type)) { // Predict派奖消息
						mHandler.obtainMessage(Constants.PREDICT__PRIZE).sendToTarget();
					} else if (MessagesUtils.isPayCoinChange(afMessageInfo.type)) {// B31,coin改变
						AfAttachPalmCoinSysInfo afAttachPalmCoinSysInfo = (AfAttachPalmCoinSysInfo) (afMessageInfo.attach);
						if (afAttachPalmCoinSysInfo != null) {
							if(afAttachPalmCoinSysInfo.refresh_balance == 1){// coin余额有改变
								SharePreferenceUtils.getInstance(context).setBalancePalmcoinRedDot(true);
								// coin改变消息
								EventBus.getDefault().post(new UpdateCoinsEvent(0, true)); // 重新从服务器获取最新Balance
							}else if(afAttachPalmCoinSysInfo.refresh_balance == 2) { //刷新PallCall帐户余额
								//palm call时长变化
								EventBus.getDefault().post(new UpdatePalmCallDurationEvent());
							}

						}

						// 将消息放入最近聊天列表中
						int status = MessagesUtils.insertMsg(afMessageInfo, false, true);

						if (status == Constants.DB__SUC) {
							updateUi(afMessageInfo);
						} else {
							getInfoFromNet(afMessageInfo);
						}
					}
					// chatroom at me msg
					else if (MessagesUtils.isChatroomAtMeMessage(afMessageInfo.type)) {
						if (afMessageInfo.unReadNum != 0) {
							PalmchatLogUtils.println("AfHttpSysMsgProc  chatroom @ msg:" + afMessageInfo.msg);
							EventBus.getDefault().post(new ChatRoomListEvent(ChatRoomListEvent.TYPE_REFRESH_ATME));
							/*if (mChatroomListener != null) {
								mChatroomListener.onRefreshChatroomList();
							}
							if (mOnRefreshAtMeListener != null) {
								mOnRefreshAtMeListener.onRefreshAtMe();
							}*/
							CommonUtils.showAtMeNotification(PalmchatApp.this, afMessageInfo);
						}
						mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMessageInfo).sendToTarget();
						// palmchat team //带R的都是发这个消息，目前有palmchat team，公共账号。
					} else if (MessagesUtils.isSystemMessage(afMessageInfo.type)) {
						int status=MessagesUtils.insertMsg(afMessageInfo, false, true);
						if (status == Constants.DB__SUC) {//20160824 5.4新逻辑 公众号如果不在关注里 需要取一下 Profile
							updateUi(afMessageInfo);
							parseIsEmailVerify(afMessageInfo);
						} else {
							getInfoFromNet(afMessageInfo);
						}
						if(afMessageInfo.fromAfId.equals(DefaultValueConstant._R_PALMCALL)){//这里有问题，PalmCall id会变
							EventBus.getDefault().post(new UpdatePalmCallDurationEvent());
						}
						// chatroom msg
					} else if (MessagesUtils.isChattingRoomMessage(afMessageInfo.type)) {
						PalmchatLogUtils.println("AfHttpSysMsgProc isChattingRoomMessage:" + afMessageInfo.msg + " type:" + afMessageInfo.type);
						mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMessageInfo).sendToTarget();
					}
				};
			});

		}
	}
	private void parseAfSysMsgChatRoomExit( int lParam){
		PalmchatLogUtils.println("PalmchatApp  Consts.AFMOBI_SYS_MSG_CHATROOM_EXIT  code  " + lParam);
		MessagesUtils.setChatroomExit();
		// refresh chatroom list.(isEnter flag)
		/*if (mChatroomListener != null) {
			mChatroomListener.onRefreshChatroomList();
		}*/
		EventBus.getDefault().post(new ChatRoomListEvent(ChatRoomListEvent.TYPE_REFRESH_CHATROOM_LIST));

		PalmchatLogUtils.i("hj", "chatRoom Exit");
		PalmchatLogUtils.i("hj ChattingRoomMainAct", "Name:" + CommonUtils.getCurrentActivity(this));
		mAfCorePalmchat.AfChatroomPollSetStatus(Consts.POLLING_STATUS_PAUSE);
			/*
			 * if(ChattingRoomMainRightActivity.class.getName().equals(
			 * CommonUtils.getCurrentActivity(this)) ||
			 * MyActivityManager.getScreenManager().getCurrentActivity()
			 * instanceof ChattingRoomMainRightActivity){ if(null !=
			 * MyActivityManager.getScreenManager().getCurrentActivity()){
			 * PalmchatLogUtils.i("hj",
			 * "chatRoom Exit and close ChattingRoomMainAct");
			 * MyActivityManager.getScreenManager().getCurrentActivity().finish(
			 * ); } }
			 */
		if (ChattingRoomMainAct.class.getName().equals(CommonUtils.getCurrentActivity(this)) || MyActivityManager.getScreenManager().currentFragmentActivity() instanceof ChattingRoomMainAct) {
			if (null != MyActivityManager.getScreenManager().currentFragmentActivity()) {
				PalmchatLogUtils.i("hj", "chatRoom Exit and close ChattingRoomMainAct");
				MyActivityManager.getScreenManager().currentFragmentActivity().finish();
			}
		}
	}

	/**
	 * 收到 群系统消息 创建群， 修改群名称 ，添加群成员 ， 删除群等
	 * @param wparam
     */
	private void parseAfSysMsgGrpNotify(final Object wparam) {
		if (wparam != null  ) {//
			// Consts.AFMOBI_SYS_MSG_GRP_NOTIFY AfGrpNotifyMsg AfHttpGrpOpr

			CacheManager.getInstance().getThreadPoolInstance().execute(new Thread() {
				public void run() {
					System.out.println("--- thread id AFMOBI_SYS_MSG_GRP_NOTIFY " + Thread.currentThread().getId());

					AfGrpNotifyMsg afGrpNotifyMsg = (AfGrpNotifyMsg) wparam;
					AfMessageInfo afMessageInfo = AfGrpNotifyMsg.toAfMessageInfo(afGrpNotifyMsg, PalmchatApp.getApplication());
					afMessageInfo.client_time = System.currentTimeMillis();
					int msg_id = mAfCorePalmchat.AfDbGrpMsgInsert(afMessageInfo);
					afMessageInfo._id = msg_id;

					switch (afGrpNotifyMsg.type) {
						case AfMessageInfo.MESSAGE_GRP_CREATE: {
							AfGrpProfileInfo afCreate = new AfGrpProfileInfo();
							afCreate.afid = afGrpNotifyMsg.gid;
							afCreate.name = afGrpNotifyMsg.gname;
							afCreate.version = afGrpNotifyMsg.gver;
							afCreate.admin = afGrpNotifyMsg.afid;
							if (afGrpNotifyMsg.users_afid != null) {
								for (int i = 0; i < afGrpNotifyMsg.users_afid.size(); i++) {
									AfGrpProfileInfo.AfGrpProfileItemInfo gItem = new AfGrpProfileInfo.AfGrpProfileItemInfo();

									gItem.afid = afGrpNotifyMsg.users_afid.get(i);
									gItem.name = afGrpNotifyMsg.users_name.get(i);
									afCreate.members.add(i, gItem);
								}
							}

							AfGrpProfileInfo gtemp = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);
							// receive create group msg, set tips = true, notify
							// group msg
							afCreate.tips = mAfCorePalmchat.AfDbSettingGetTips(afCreate.afid);

							if (gtemp != null) {

								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(afCreate, true, true);

							} else {
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).insert(afCreate, true, true);
							}

							getGrpInfoFromNet(afMessageInfo);// release by tony
							MessagesUtils.insertMsg(afMessageInfo, false, true);
						}
						break;

						case AfMessageInfo.MESSAGE_GRP_ADD_MEMBER: {
							AfGrpProfileInfo grpInfo = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);

							if (grpInfo != null) {
								grpInfo.version = afGrpNotifyMsg.gver;
								grpInfo.members = CommonUtils.getRealList(grpInfo.members, afGrpNotifyMsg.users_afid, afGrpNotifyMsg.users_name, Consts.ACTION_ADD);
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(grpInfo, true, true);
								updateGroupStatus(grpInfo, afMessageInfo, AfMessageInfo.MESSAGE_GRP_ADD_MEMBER);
							} else {
								getGrpInfoFromNet(afMessageInfo);
							}
							MessagesUtils.insertMsg(afMessageInfo, true, true);
						}
						break;
						// 修改群资料
						case AfMessageInfo.MESSAGE_GRP_MODIFY: {
							AfGrpProfileInfo grpInfo3 = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);

							if (grpInfo3 != null) {
								grpInfo3.name = afGrpNotifyMsg.gname;
								grpInfo3.sig = afGrpNotifyMsg.sign;
								grpInfo3.version = afGrpNotifyMsg.gver;
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(grpInfo3, true, true);
							} else {
								getGrpInfoFromNet(afMessageInfo);
							}
							int unreadCount = mAfCorePalmchat.AfRecentMsgGetUnread(afMessageInfo.getKey());
							afMessageInfo.unReadNum = unreadCount;
							MessagesUtils.insertMsg(afMessageInfo, true, true);
						}
						break;
						// 移除群成员
						case AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER: {
							AfGrpProfileInfo grpInfo4 = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);

							if (grpInfo4 != null) {
								grpInfo4.version = afGrpNotifyMsg.gver;
								grpInfo4.members = CommonUtils.getRealList(grpInfo4.members, afGrpNotifyMsg.users_afid, afGrpNotifyMsg.users_name, Consts.ACTION_REMOVE);
								afMessageInfo.afidList = afGrpNotifyMsg.users_afid;
								updateGroupStatus(grpInfo4, afMessageInfo, AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER);

								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(grpInfo4, true, true);

							} else {

								getGrpInfoFromNet(afMessageInfo);
							}
							MessagesUtils.insertMsg(afMessageInfo, true, true);
						}
						break;
						// 退出群
						case AfMessageInfo.MESSAGE_GRP_DROP: {
							AfGrpProfileInfo grpInfo5 = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);

							if (grpInfo5 != null) {
								grpInfo5.version = afGrpNotifyMsg.gver;
								grpInfo5.members = CommonUtils.getRealList(grpInfo5.members, afGrpNotifyMsg.afid, Consts.ACTION_REMOVE);
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(grpInfo5, true, true);
							} else {

								getGrpInfoFromNet(afMessageInfo);
							}
							MessagesUtils.insertMsg(afMessageInfo, true, true);
						}
						break;

						case AfMessageInfo.MESSAGE_GRP_DESTROY: {
							AfGrpProfileInfo grpInfo6 = MessagesUtils.dispatchGrpNotifyMsg(afGrpNotifyMsg, false, true);
							if (grpInfo6 != null) {
								grpInfo6.status = Consts.AFMOBI_GRP_STATUS_EXIT;// group
								// dismiss
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(grpInfo6, false, true);
								mAfCorePalmchat.AfDbGrpProfileSetStatus(grpInfo6.afid, grpInfo6.status);
							}
							MessagesUtils.insertMsg(afMessageInfo, true, true);
							EventBus.getDefault().post(new DestroyGroupEvent());
						}
						break;
						default:
							break;
					}

					Message msg = mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMessageInfo);
					mHandler.sendMessage(msg);

//					updateGrp(afMessageInfo);
					EventBus.getDefault().post(new RefreshFollowerFolloweringOrGroupListEvent(Constants.REFRESH_GROUPLIST));
				};
			});

			// 对方已读 消息（私聊类型消息）， 将消息状态设为已读
		}
	}
	/**
	 * 服务器推送的各种消息， 实现中间件的监听接口 中间件会回调该函数
	 */
	@Override
	public boolean AfHttpSysMsgProc(int msg, final Object wparam, int lParam) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.e(TAG, "AfHttpSysMsgProc time:" + System.currentTimeMillis() + "---PalmchatApp  AfHttpSysMsgProc  " + wparam + " msg  " + msg + "  lParam  " + lParam);
	  	switch (msg) {
			case Consts.AFMOBI_SYS_MSG_DP_PROXYLIST://获取代理列表
				Constants.USE_PROXY=false;//先复位
				if(wparam instanceof AfDPProxyItem[]){
					AfDPProxyItem[] proxyItems=(AfDPProxyItem[])wparam;
					String imsi=null;
					for(AfDPProxyItem proxyItem: proxyItems){
						imsi=PalmchatApp.getOsInfo().getImsi();
						if(imsi!=null&&imsi.startsWith(proxyItem.shortimsi) ){
							if(proxyItem.type==1) {//http
								Constants.USE_PROXY = true;
								Constants.PROXY_PORT = proxyItem.port;
								Constants.PROXY_URL=proxyItem.ip;
								break;
							}
						}
					}
				}
				break;
			case Consts.AF_SYS_MSG_INIT:
			case Consts.AF_SYS_MSG_UPDATE_FRIEND:
			case Consts.AFMOBI_SYS_MSG_UPDATE_GRP:
			case Consts.AFMOBI_SYS_MSG_UPDATE_FOLLOW: {
				if(msg==Consts.AF_SYS_MSG_INIT) {
                    parseAfSysMsgInit();
				}
				AfCacheParam param = (AfCacheParam) wparam;
				if (null != param) {
					param.msg = msg;
					AfPalmchat.getCacheManager().loadCache(param);
					if(msg== Consts.AFMOBI_SYS_MSG_UPDATE_FOLLOW){
						//当中间件登陆自动获取following列表后 发following列表获取成功的事件 （之前是在getBatchInfo之后通知的，5.3改为中间件不主动获取batchinfo了 所以要在这里通知）
						EventBus.getDefault().post(new RefreshFollowerFolloweringOrGroupListEvent(Constants.REFRESH_FOLLOWING));
					}
				}
			}
				break;

			case Consts.AFMOBI_SYS_MSG_BACKGROUND_RELOGIN:// -66 or -65引起的重登成功
				CommonUtils.cancelNetUnavailableNoticefacation();
                parseAfSysMsgBackgroundRelogin(wparam);
				return true;//true 该事件不再向其他监听者分发了
			case Consts.AFMOBI_SYS_AUTO_LOGIN_STATUS:// 中间件开始在后台自动登录或返回失败只有-66等错误才会进来
                parseAfAutoLoginStatus(wparam,lParam);
				break;
			case Consts.AF_SYS_MSG_RECV:  //服务器推送的各种消息
				CommonUtils.cancelNetUnavailableNoticefacation();
                parseAfSysMsgRecv(wparam,lParam);
				break;
			case Consts.AFMOBI_SYS_MSG_RELOGIN://-76异地登陆
				CommonUtils.cancelNoticefacation(getApplicationContext());
				ToastManager.getInstance().show(this, R.string.have_been_log_out);
				MsgAlarmManager.getInstence().cancelAllAlarm();
				toLogin();
				PalmchatLogUtils.e("JNI SYSTEM", "ywp: -76 relogin");
				break;
			case Consts.AFMOBI_SYS_MSG_OFFLINE:
				PalmchatLogUtils.println("PalmchatApp  Consts.AFMOBI_SYS_MSG_OFFLINE");
				CommonUtils.showOffLineToast(context);
				break;
			case Consts.AFMOBI_SYS_MSG_CHATROOM_EXIT:
                parseAfSysMsgChatRoomExit(lParam);
				break;
			case Consts.AFMOBI_SYS_MSG_GRP_NOTIFY://收到 群系统消息 创建群， 修改群名称 ，添加群成员 ， 删除群等
                parseAfSysMsgGrpNotify(wparam);
				break;
			case Consts.AFMOBI_SYS_MSG_READ_NOTIFY://已读消息通知
				if (Chatting.class.getName().equals(CommonUtils.getCurrentActivity(this)) || MyActivityManager.getScreenManager().getCurrentActivity() instanceof Chatting) {
					if (messageReadReceiver != null) {
						messageReadReceiver.handleMessageForReadFromServer((AfMessageInfo) wparam);
					}
				}
				break;
		}

		return false;
	}

	/**
	 * 注销账号到登录页面
	 */
	private void toLogin() {
		mAfCorePalmchat.AfPollSetStatus(Consts.POLLING_STATUS_PAUSE);
		setInit(false);
		setContext(null);
		mAfCorePalmchat.AfHttpRemoveAllListener();
		// PalmchatApp.setIsLoadAccount(false);
		CacheManager.getInstance().clearCache();
	//	PalmcallManager.getInstance(this).logoutPalmcall();
		mAfCorePalmchat.AfDbLoginSetStatus(CacheManager.getInstance().getMyProfile().afId, AfLoginInfo.AFMOBI_LOGIN_INFO_NORMAL);
		mAfCorePalmchat.AfCoreSwitchAccount();
		MyActivityManager.getScreenManager().clear();
		CacheManager.getInstance().getGifImageUtilInstance().getImageFolder().clear();
		CacheManager.getInstance().getGifImageUtilInstance().getListFolders().clear();

		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Constants.LOGOUT, true);
		boolean isThirdParLogin = SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).getThirdPartLogin(CacheManager.getInstance().getMyProfile().afId);
		intent.putExtra(JsonConstant.KEY_IS_THIRD_LOGIN, isThirdParLogin);
		startActivity(intent);
		AppStatusService.stop(this);

		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).setThirdPartLogin(myProfile.afId, false);
		// savePalmAccount(null,false);
		CacheManager.getInstance().setMyProfile(null);
//		ImageLoader.getInstance().clear();
		ImageManager.getInstance().clear();
		CommonUtils.cancelNoticefacation(getApplicationContext());
		CommonUtils.cancelNetUnavailableNoticefacation();
		CacheManager.getInstance().setAfChatroomDetails(null);
		CacheManager.getInstance().setChatroomListTime(0);
		FolllowerUtil.getInstance().clear();
		JsonConstant.getMyProfileFromServer_forFollowCount_byPalmID = null;
	}

	/**
	 * 收到各种消息后解析完数据 去更新UI
	 * 
	 * @param afMsg
	 */
	private void updateUi(AfMessageInfo afMsg) {
		System.out.println("--- thread id  updateUi " + Thread.currentThread().getId());
		System.out.println("---updateUi:" + afMsg.msg);

		PalmchatLogUtils.e(TAG, "updateUi time:" + System.currentTimeMillis() + "---updateUi:" + afMsg);

		if (MessagesUtils.isPrivateMessage(afMsg.type) || MessagesUtils.isGroupChatMessage(afMsg.type) || MessagesUtils.isSystemMessage(afMsg.type)) {
			if (MessagesUtils.isGroupChatMessage(afMsg.type)) {
				AfGrpProfileInfo afGrp = new AfGrpProfileInfo();
				afGrp.afid = afMsg.getKey();
				AfGrpProfileInfo mGrp = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).search(afGrp, false, true);

				// tips == false, don't show notification
				MainAfFriendInfo afMain = new MainAfFriendInfo();
				afMain.afMsgInfo = afMsg;
				MainAfFriendInfo mMain = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).search(afMain, false, true);
				if (mGrp != null && !mGrp.tips) {
					boolean closePalmchat = SharePreferenceUtils.getInstance(context).getIsClosePalmchat();
					if(closePalmchat)
						return;
					RefreshQueueManager.getInstence().sendRefreshRequest();
					mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMsg).sendToTarget();

					// switch pop msg
					if (settingMode.isNewMsgNotice() && settingMode.isPopMsg()) {
						// if pop msg
						if (CommonUtils.isScreenLocked(this) || (CacheManager.getInstance().getPopMessageManager().popMsgDisplaying() && isClosePopMessage())) {// (CacheManager.getInstance().getPopMessageManager().popMsgDisplaying()
																																									// &&
																																									// isClosePopMessage(true))主要解决公司手机滑动锁屏的问题
							PalmchatLogUtils.println("--qqq isRunningBackGround");
							if (!isDoubleClickScreen) {// 若双击，用户未输入密码时解锁时，则不弹出窗口
								CacheManager.getInstance().getPopMessageManager().sendPopMsgRequest(mHandler, afMsg);
							}
						}
					}
					return;
				} else {
					if (mMain != null && mMain.afMsgInfo != null) {
						mMain.afMsgInfo.unReadNum = mAfCorePalmchat.AfRecentMsgGetUnread(mMain.afMsgInfo.getKey());
						PalmchatLogUtils.println("--- bbb mMain.afMsgInfo.unReadNum:" + mMain.afMsgInfo.unReadNum);
					}
				}
			}

			int messageCount = 0;
			int index = 0;
			List<MainAfFriendInfo> list = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();

			for (MainAfFriendInfo chats : list) {
				int num = chats.afMsgInfo.unReadNum;
				if (num > 0) {
					index++;
					messageCount += num;
				}
			}

			int[] num = new int[2];
			num[0] = index;
			num[1] = messageCount;

			if (!(MessagesUtils.isFriendReqMessage(afMsg.type) || MessagesUtils.isFriendReqSucMessage(afMsg.type)
					|| MessagesUtils.isFollowMessage(afMsg.type))) {//hj 被关注、取消被关注都不会消息提示以及不会在黑屏下弹出窗
				boolean closePalmchat = SharePreferenceUtils.getInstance(context).getIsClosePalmchat();
				if(closePalmchat)
					return;
				RefreshNotificationManager.getInstence().sendRefreshRequest(PalmchatApp.this, num, afMsg);

				// switch pop msg
				if (settingMode.isNewMsgNotice() && settingMode.isPopMsg()) {
					// if pop msg
					if ((CommonUtils.isScreenLocked(this) || (CacheManager.getInstance().getPopMessageManager().popMsgDisplaying() && isClosePopMessage())) && //// (CacheManager.getInstance().getPopMessageManager().popMsgDisplaying()
																																									//// &&
																																									//// isClosePopMessage(true))主要解决公司手机滑动锁屏的问题
					// 以“r”开头的为公共账号和palmchat team，公共账号在黑屏下不弹出 对话框，palmchat team
					// 需要
					((!afMsg.fromAfId.startsWith(DefaultValueConstant._R)) || (afMsg.fromAfId.endsWith(DefaultValueConstant._R_PALMTEMA)))) {// ||
																																				// CacheManager.getInstance().getPopMessageManager().popMsgDisplaying())
																																				// {
						PalmchatLogUtils.println("--qqq popMsg");
						if (!isDoubleClickScreen) {// 若双击，用户未输入密码时解锁时，则不弹出窗口
							CacheManager.getInstance().getPopMessageManager().sendPopMsgRequest(mHandler, afMsg);
						}
					}
				}
			}

			// screen off, receive frd req msg, notify user.
			if (MessagesUtils.isFriendReqMessage(afMsg.type) || MessagesUtils.isFriendReqSucMessage(afMsg.type)) {
				if ((CommonUtils.isScreenLocked(this) || (CacheManager.getInstance().getPopMessageManager().popMsgDisplaying() && isClosePopMessage())) && // (CacheManager.getInstance().getPopMessageManager().popMsgDisplaying()
																																								// &&
																																								// isClosePopMessage(true))主要解决公司手机滑动锁屏的问题
				// 以“r”开头的为公共账号和palmchat team，公共账号在黑屏下不弹出 对话框，palmchat team 需要
				((!afMsg.fromAfId.startsWith(DefaultValueConstant._R)) || (afMsg.fromAfId.endsWith(DefaultValueConstant._R_PALMTEMA)))) {
					boolean closePalmchat = SharePreferenceUtils.getInstance(context).getIsClosePalmchat();
					if(closePalmchat)
						return;
					PalmchatLogUtils.println("--req msg");
					CommonUtils.friendReqNotify();
					if (!isDoubleClickScreen) {// 若双击，用户未输入密码时解锁时，则不弹出窗口
						CacheManager.getInstance().getPopMessageManager().sendPopMsgRequest(mHandler, afMsg);
					}
				}
			}
		}

		RefreshQueueManager.getInstence().sendRefreshRequest();
		mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMsg).sendToTarget();
	}



	/**
	 * 刷新群列表
	 */
	/*
	 * private void updateGroupList() { Intent intent = new Intent();
	 * intent.setAction(Constants.REFRESH_GROUP_LIST_ACTION);
	 * sendBroadcast(intent); }
	 */

	/**
	 * 刷新粉丝(follower)列表
	 */
	/*
	 * private void updateFollowerList() { Intent intent = new Intent();
	 * intent.setAction(Constants.REFRESH_FOLLOWER_LIST_ACTION);
	 * sendBroadcast(intent); }
	 */


	/**
	 * 处理对方同意加好友消息
	 * 
	 * @param afMessageInfo
	 */
	private void notifyFreqSucMsg(AfMessageInfo afMessageInfo) {
		PalmchatLogUtils.println("---Frd req Suc notifyFreqSucMsg");
		// 对方同意加好友 请求服务器把这个人的afid加入我的好友列表中
		mAfCorePalmchat.AfHttpFriendOpr("all", afMessageInfo.fromAfId, Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE, (byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, afMessageInfo, PalmchatApp.this);
	}

	/**
	 * 更新群列表
	 * 
	 * @param afMessageInfo
	 */
	/*private void updateGrp(AfMessageInfo afMessageInfo) {
		Message msg = mHandler.obtainMessage(Constants.REFRESH_GROUP_LIST, afMessageInfo);
		mHandler.sendMessage(msg);
	}*/

	/**
	 * @param afMessageInfo
	 */
	private void getInfoFromNet(AfMessageInfo afMessageInfo) {
		Message msg = mHandler.obtainMessage(Constants.FRIEND_INFO, afMessageInfo);
		mHandler.sendMessage(msg);
	}

	private void getInfoFromNet(AfMessageInfo afMessageInfo, int flag) {
		Message msg = mHandler.obtainMessage(Constants.FRIEND_INFO, afMessageInfo);
		msg.arg2 = flag;
		mHandler.sendMessage(msg);
	}

	/**
	 * 从服务器下载群profile
	 * 
	 * @param afMessageInfo
	 */
	private void getGrpInfoFromNet(AfMessageInfo afMessageInfo) {
		if (mReqInfoMap.get(afMessageInfo.getKey()) == null) {
			mReqInfoMap.put(afMessageInfo.getKey(), afMessageInfo);
			Message msg = mHandler.obtainMessage(Constants.NOTIFY_GRP, afMessageInfo);
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 同意好友请求
	 * 
	 * @param afMessageInfo
	 */
	private void acceptFriendRequest(AfMessageInfo afMessageInfo) {
		Message msg = mHandler.obtainMessage(Constants.FREQ_ACCEPT_MSG, afMessageInfo);
		mHandler.sendMessage(msg);
	}

	/**
	 * 分发私聊消息 群消息 聊天室消息 到格子
	 * 
	 * @param afMessageInfo
	 */
	private void toDiffrentView(AfMessageInfo afMessageInfo) {
		for (MessageReceiver msgReceiver : mMsgRecList) {
			if (msgReceiver != null) {
				/* modify by zhh 取消关注，或者取消被关注时不做任何显示 */
				if (MessagesUtils.isFollowMessage(afMessageInfo.type))  //hj 关注的消息不做任何提示
					return;
				msgReceiver.handleMessageFromServer(afMessageInfo);
			}
		}
	}

	/**
	 * 请求接口服务器返回
	 */
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		PalmchatLogUtils.println("flag  " + flag + "  code  " + code);
		final AfMessageInfo afMsg = (AfMessageInfo) user_data;

		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_GET_INFO: {// 获取用户Profile
				if (afMsg != null) {
					if (!RequestConstant.PALMCHAT_ID.equalsIgnoreCase(afMsg.fromAfId)) {// private
																						// chat

						if (result != null) {
							final AfFriendInfo afF = (AfFriendInfo) result;
							new Thread() {
								public void run() {
									// insert to stranger list
									AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afF.afId);

									if (afFriendInfo == null) {
										MessagesUtils.insertStranger(afF);
									}

									if (MessagesUtils.isFriendReqMessage(afMsg.type) || MessagesUtils.isFollowMessage(afMsg.type)) {
										// insert to friend list
										MessagesUtils.insertFreqMsg(PalmchatApp.this, afMsg, false, true);

									} else if (MessagesUtils.isFriendReqSucMessage(afMsg.type)) {
										PalmchatLogUtils.println("---Frd req Suc AfOnResult isFriendReqSucMessage");
										notifyFreqSucMsg(afMsg);
										// insert recent msg list
									} else {
										if (!MessagesUtils.isGroupChatMessage(afMsg.type) && !MessagesUtils.isCardMessage(afMsg.type)) {
											MessagesUtils.insertMsg(afMsg, false, true);
										}
									}

									if (!MessagesUtils.isGroupChatMessage(afMsg.type) || (MessagesUtils.isGroupChatMessage(afMsg.type) && MessagesUtils.isCardMessage(afMsg.type))) // 加后面一个条件，主要解决在群聊天界面，
																																													// 若其他群成员发了名片且名片的人不存在本机，则聊天界面接收不到此名片消息
									{
										updateUi(afMsg);
									}

								};

							}.start();
						} else {
							PalmchatLogUtils.println("---afOnResult REQ_GET_INFO result = null");
						}

					} else {// Palmchat team msg
						AfProfileInfo profile = (AfProfileInfo) result;
						String afid = null;
						if (profile != null) {
							afid = profile.afId;
						}
						AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
						if (null != profile && null != profile.email && null != afid && myProfile.afId.equalsIgnoreCase(afid)) {

							if (profile.email.equals(myProfile.email)) {
								myProfile.is_bind_email = true;
							}
						}
						PalmchatLogUtils.println("REQ_GET_INFO  profile  " + profile);
					}
				}
				break;
			}

			case Consts.REQ_GET_BATCH_INFO: {
				// final AfMessageInfo afMsg = (AfMessageInfo)user_data;
				if (afMsg != null) {
					if (result != null) {
						final AfFriendInfo info[] = (AfFriendInfo[]) result;
						new Thread() {
							public void run() {
								for (AfFriendInfo tmp : info) {
									if (tmp != null) {
										AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(tmp.afId);
										if (afFriendInfo == null) {
											MessagesUtils.insertStranger(tmp);
										}
									}
								}
								// MessagesUtils.insertMsg(afMsg, false, true);
								Message msg = mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMsg);
								mHandler.sendMessage(msg);

							}
						}.start();
					} else {
						PalmchatLogUtils.println("---afOnResult REQ_GET_BATCH_INFO result = null");
					}
				}
				break;
			}
			case Consts.REQ_GRP_GET_PROFILE: {
				// final AfMessageInfo afMsg = (AfMessageInfo)user_data;
				if (afMsg != null) {
					mReqInfoMap.remove(afMsg.getKey());

					if (result != null) {
						final AfGrpProfileInfo afGrpInfo = (AfGrpProfileInfo) result;
						new Thread() {
							public void run() {
								afGrpInfo.tips = mAfCorePalmchat.AfDbSettingGetTips(afGrpInfo.afid);
								int type = afMsg.type & AfMessageInfo.MESSAGE_TYPE_MASK;
								PalmchatLogUtils.println("Consts.REQ_GRP_GET_PROFILE  type  " + type + "--tips:" + afGrpInfo.tips);

								if (AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER == type) {
									updateGroupStatus(afGrpInfo, afMsg, AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER);
								} else if (AfMessageInfo.MESSAGE_GRP_ADD_MEMBER == type) {
									updateGroupStatus(afGrpInfo, afMsg, AfMessageInfo.MESSAGE_GRP_ADD_MEMBER);
								}
								CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).saveOrUpdate(afGrpInfo, true, true);
								MessagesUtils.insertMsg(afMsg, false, true);
								updateUi(afMsg);
//								updateGrp(afMsg);
								EventBus.getDefault().post(new RefreshFollowerFolloweringOrGroupListEvent(Constants.REFRESH_GROUPLIST));
							}
						}.start();
					} else {
						PalmchatLogUtils.println("---afOnResult REQ_GRP_GET_PROFILE result = null");
					}
				}
				break;
			}

				// tell server to add frd suc
			case Consts.REQ_FRIEND_LIST: {
				// final AfMessageInfo afMsg = (AfMessageInfo)user_data;
				PalmchatLogUtils.println("PalmchatApp  REQ_FRIEND_LIST ");
				if (afMsg != null) {
					new Thread() {
						public void run() {
							MessagesUtils.insertFreqMsg(PalmchatApp.this, afMsg, false, true);
							if (afMsg.accept_friend_defualt) {
								final AfFriendInfo afF = CacheManager.getInstance().findAfFriendInfoByAfId(afMsg.fromAfId);
								if (afF != null) {
									String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, afF.name, getString(R.string.frame_toast_friend_req_success));
									afMsg.msg = aa;
									MessagesUtils.onAddFriendSuc(afF);

									// 添加好友成功。删除好友请求,刷新好友列表
									MessagesUtils.removeFreqMsg(afMsg, true, true);
									// hj 不用改为普通消息，改为请求成功
									afMsg.type = afMsg.type + 1;
									int _id = mAfCorePalmchat.AfDbMsgInsert(afMsg);
									afMsg._id = _id;
									MessagesUtils.insertMsg(afMsg, true, true);
									MainAfFriendInfo friend = new MainAfFriendInfo();
									friend.afFriendInfo = afF;
									friend.afMsgInfo = afMsg;
									CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FRD_REQ).insert(friend, false, true);
									mHandler.obtainMessage(Constants.NOTIFY_UPDATE_UI, afMsg).sendToTarget();
								}
							}
							updateUi(afMsg);
						};
					}.start();
				}
				break;
			}
			case Consts.REQ_MSG_SEND: {
				final AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
				notifyFreqSucMsg(afMessageInfo);
				break;
			}
			}

			// code != Consts.REQ_CODE_SUCCESS
		} else {
			PalmchatLogUtils.println("---Frd req Suc req failed flag=" + flag + "-code = " + code);
			switch (flag) {

			case Consts.REQ_GET_INFO:
				break;

			case Consts.REQ_GET_BATCH_INFO:
				break;

			case Consts.REQ_GRP_GET_PROFILE:
				mReqInfoMap.remove(afMsg.getKey());
				if (afMsg != null && !(code == Consts.REQ_CODE_NO_GROUP || code == Consts.REQ_CODE_GROUP_NOT_EXIST || code == Consts.REQ_CODE_65)) {
					getGrpInfoFromNet(afMsg);
				}
				break;

			case Consts.REQ_FLAG_VERSION_CHECK: {
				AfVersionInfo afVersionInfo = (AfVersionInfo) result;
				PalmchatLogUtils.println("Failure  Consts.REQ_FLAG_VERSION_CHECK  afVersionInfo  " + afVersionInfo);
				if (afVersionInfo != null) {
					PalmchatLogUtils.println("Failure  afVersionInfo  " + afVersionInfo.toString());
				}
				if (context instanceof AboutActivity) {
					((AboutActivity) context).dismissProgressDialog();
				}
				ToastManager.getInstance().show(context, R.string.network_unavailable);
				break;
			}
			default:
				break;
			}

		}
	}

    private Handler mHandler = new Handler() {

                public void handleMessage(Message msg) {

                    switch (msg.what) {
                        case Constants.IS_BACKGROUND: {//挂后台收消息要弹窗
                            AfMessageInfo afMsg = (AfMessageInfo) msg.obj;
                            int count = msg.arg1;
                            popMessage(afMsg, count);
                            break;
                        }

                        case Constants.NOTIFY_UPDATE_UI: {
                            AfMessageInfo afMsg = (AfMessageInfo) msg.obj;
                            if (afMsg == null) {
                                return;
                            }

                            if (MessagesUtils.isFriendReqSucMessage(afMsg.type)) {
                                EventBus.getDefault().post(new EventRefreshFriendList());
                            }else if (MessagesUtils.isFriendReqMessage(afMsg.type)) {
                                /*
                                 * Intent intent = new Intent();好友请求不再发通知
                                 * intent.setAction(Constants.REFRESH_NOTIFICATION_ACTION);
                                 * sendBroadcast(intent);
                                 */
                                EventBus.getDefault().post(new RefreshChatsListEvent());
                            } else if (MessagesUtils.isFollowMessage(afMsg.type)) {
                                EventBus.getDefault().post(new RefreshFollowerFolloweringOrGroupListEvent(Constants.REFRESH_FOLLOWER));
								if(!(MessagesUtils.DEL_FOLLOW_MSG.equals(afMsg.msg) && MessagesUtils.DEL_BE_FOLLOWED_MSG.equals(afMsg.msg))) {
									SharePreferenceUtils.getInstance(getApplicationContext()).setIsFolloed(CacheManager.getInstance().getMyProfile().afId, true);//follow消息的红点提示
								}
                            }
                            toDiffrentView((AfMessageInfo) msg.obj);//消息内容分发
                            break;
                        }
                        case Constants.FRIEND_INFO: {
                            AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                            String afid[] = new String[1];
                            if (MessagesUtils.isCardMessage(afMessageInfo.type)) {

                                switch (msg.arg2) {
                                    // get card profile
                                    case Constants.GET_MSG:

                                        afid[0] = afMessageInfo.msg;
                                        break;

                                    // get people profile who send card
                                    case Constants.GET_FROM_AFID:
                                        afid[0] = afMessageInfo.fromAfId;
                                        break;
                                    case Constants.GET_FROM_AFID_AND_MSG:
                                        String afids[] = new String[2];
                                        afids[0] = afMessageInfo.fromAfId;
                                        afids[1] = afMessageInfo.msg;
                                        mAfCorePalmchat.AfHttpGetInfo(afids, Consts.REQ_GET_BATCH_INFO, true, null, afMessageInfo, PalmchatApp.this);
                                        return;

                                    default:
                                        break;
                                }

                            } else if (MessagesUtils.isFriendReqMessage(afMessageInfo.type)) {
                                afid[0] = afMessageInfo.fromAfId;
                            } else {
                                afid[0] = afMessageInfo.fromAfId;
                            }
                            mAfCorePalmchat.AfHttpGetInfo(afid, Consts.REQ_GET_INFO, true, null, afMessageInfo, PalmchatApp.this);
                            break;
                        }

                        case Constants.NOTIFY_GRP: {
                            AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                            mAfCorePalmchat.AfHttpGrpOpr(null, null, afMessageInfo.toAfId, Consts.REQ_GRP_GET_PROFILE, true, afMessageInfo, PalmchatApp.this);
                            break;
                        }
                        case Constants.FREQ_ACCEPT_MSG: {
                            AfMessageInfo afMessageInfo = (AfMessageInfo) msg.obj;
                            mAfCorePalmchat.AfHttpSendMsg(afMessageInfo.fromAfId, System.currentTimeMillis(), null, Consts.MSG_CMMD_AGREE_FRD_REQ, afMessageInfo, PalmchatApp.this);
                            break;
                        }

                        case Constants.TO_REFRESH_FRIEND_LIST: {
                            EventBus.getDefault().post(new EventRefreshFriendList());
                            EventBus.getDefault().post(new RefreshChatsListEvent());
                            break;
                        }
                        case Constants.BROADCAST_NOTIFICATION: {
                            AfChapterInfo _af = new AfChapterInfo();
                            _af.eventBus_action = Constants.BROADCAST_NOTIFICATION_ACTION;
                            EventBus.getDefault().post(_af);
                            break;

                        }
                        case Constants.BROADCAST_FRIENDCIRCLE_NOTIFICATION: {
                            EventBus.getDefault().post(new BroadcastFollowsNotificationEvent());
                            break;

                        }
                        case Constants.PREDICT__NOTIFICATION: { // 开奖消息
                            SharePreferenceUtils.getInstance(getCurActivity()).setUnReadPredictRecord(true);
                            EventBus.getDefault().post(new PalmGuessNotificationEvent());
                            break;
                        }
                        case Constants.PREDICT__PAYMENT_NOTIFICATION: { // 充值成功消息
                            SharePreferenceUtils.getInstance(getCurActivity()).setUnReadExchange(true);
                            EventBus.getDefault().post(new PalmGuessNotificationEvent());
                            break;
                        }
                        case Constants.PREDICT__PRIZE: { // 派奖消息
                            SharePreferenceUtils.getInstance(getCurActivity()).setUnReadPredictPrize(true);
                            EventBus.getDefault().post(new PalmGuessNotificationEvent());
                            break;
                        }
                    }

                }

            };

	/**
	 * 判断有没收到Email验证码的系统消息
	 * 
	 * @param afMessageInfo
	 */
	private void parseIsEmailVerify(AfMessageInfo afMessageInfo) {
		/** Palmchat team message */
		if (RequestConstant.PALMCHAT_ID.equalsIgnoreCase(afMessageInfo.fromAfId)) {
			/** judge message */
			String msg = afMessageInfo.msg;
			if (!TextUtils.isEmpty(msg) && msg.equalsIgnoreCase(DefaultValueConstant.VERIFY_MESSAGE)) {
				mAfCorePalmchat.AfHttpGetInfo(new String[] { CacheManager.getInstance().getMyProfile().afId }, Consts.REQ_GET_INFO, null, afMessageInfo, PalmchatApp.this);
			}
		}
	}

	/**
	 * 更新群状态
	 * 
	 * @param afGrpInfo
	 * @param afMessageInfo
	 */
	private void updateGroupStatus(final AfGrpProfileInfo afGrpInfo, final AfMessageInfo afMessageInfo, final int status) {
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		if (status == AfMessageInfo.MESSAGE_GRP_ADD_MEMBER) {
			afGrpInfo.status = Consts.AFMOBI_GRP_STATUS_ACTIVE;
			mAfCorePalmchat.AfDbGrpProfileSetStatus(afGrpInfo.afid, afGrpInfo.status);
			afMessageInfo.status = afGrpInfo.status;
			return;
		}
		List<String> usersAfid = afMessageInfo.afidList;
		int size = usersAfid.size();
		for (int i = 0; i < size; i++) {
			String afid = usersAfid.get(i);
			if (!CommonUtils.isEmpty(myAfProfileInfo.afId) && myAfProfileInfo.afId.equals(afid)) {
				afGrpInfo.status = Consts.AFMOBI_GRP_STATUS_EXIT;
				mAfCorePalmchat.AfDbGrpProfileSetStatus(afGrpInfo.afid, afGrpInfo.status);
				afMessageInfo.status = afGrpInfo.status;
			}
		}
	};

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		CommonUtils.saveCrashInfo2File(ex, getApplicationContext());
		if (mDefaultHandler != null && ex != null) {
			//
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	/**
	 * 获得声音管理类
	 * 
	 * @return
	 */
	public SoundManager getSoundManager() {
		return soundManager;
	}

	/**
	 * 设置声音管理类
	 * 
	 * @param soundManager
	 */
	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	/*public ILanguageChange setILanguageChange(ILanguageChange l) {
		iLanguageChange = l;
		return iLanguageChange;
	}

	public interface ILanguageChange {
		void onLanguageChange();
	}*/

	/*public CheckUpdateInterface getCheckUpdateInterface() {
		return checkUpdateInterface;
	}

	public void setCheckUpdateInterface(CheckUpdateInterface checkUpdateInterface) {
		this.checkUpdateInterface = checkUpdateInterface;
	}

	public interface CheckUpdateInterface {
		void toNoticefy();
	}*/

	/**
	 * 初始化中间件
	 */
	private void initAfPalmchatCore() {
		AfPhoneInfoParam param = new AfPhoneInfoParam();
		param.imei = PalmchatApp.getOsInfo().getImei();
		param.imsi = PalmchatApp.getOsInfo().getImsi();
		param.mcc = PalmchatApp.getOsInfo().getMcc();
		param.mnc = PalmchatApp.getOsInfo().getMnc();
		param.width = getWindowWidth();
		param.height = getWindowHeight();
		param.language = CommonUtils.getRightLanguage(getApplication(), CommonUtils.TYPE_LOGIN);
		param.keypad_type = Consts.AF_KEYTYPE_9KEYS_TOUCH;
		param.cver = PalmchatApp.getOsInfo().getCver();
		param.osver = PalmchatApp.getOsInfo().getOsVersion();
		param.mver = PalmchatApp.getOsInfo().getMver();
		param.dsrc = ReadyConfigXML.R_DSRC;
		param.packagename = getPackageName();
		param.smsc = "0";
		param.work_dir = getFilesDir().getParent();
		param.res_dir = DefaultValueConstant.RES_DIR;
		param.timezone = TimeZone.getDefault().getID();
		param.ua = PalmchatApp.getOsInfo().getUa();
		if(PalmchatLogUtils.USE_PALMCALL){
			param.supportjustalk =1;//1:support;other not
		} else {
			param.supportjustalk =0;//other not
		}
		mAfCorePalmchat = AfPalmchat.create(this, param, this);
//		mAfCorePalmchat.AfAddHttpSysListener(this);

		PalmchatLogUtils.println("initAfPalmchatCore  param.language  " + param.language + "param.width = " + getWindowWidth() + "param.height =" + getWindowHeight() + "param.mcc =" + param.mcc);
	}



	/**
	 * 登陆
	 * 
	 * @param user
	 * @param mcc
	 * @param user_name
	 * @param password
	 * @param country_code
	 * @param type
	 * @param user_data
	 * @param result
	 * @return
	 */
	public int AfHttpLogin(String user, String mcc, String user_name, String password, String country_code, byte type, Object user_data, AfHttpResultListener result) {
		PalmchatLogUtils.println("AfHttpLogin  afid  " + user + "  mcc  " + mcc + "  username  " + user_name + " country_code  " + country_code);
		AfLoginInfo aflogin = mAfCorePalmchat.AfDbLoginGet(type, user);

		if (null != aflogin) {
			if (aflogin.status == AfLoginInfo.AFMOBI_LOGIN_INFO_UNNORMAL_EXIT) {
				mcc = aflogin.mcc;
			}
		}
		mAfCorePalmchat.AfSetMcc(mcc);
		getOsInfo().setAfid_mcc(mcc);
		return mAfCorePalmchat.AfHttpLogin(user_name, password, country_code, type, user_data, result);
	}

	/**
	 * pop message
	 */
	private void popMessage(AfMessageInfo afMsg, int count) {

		PalmchatLogUtils.println("--rrr   popMessage:" + afMsg.msg);
		if (MessagesUtils.isPrivateMessage(afMsg.type) || MessagesUtils.isSystemMessage(afMsg.type)) {

			// current popMsg has been shown
			if (PopMessageActivity.class.getName().equals(CommonUtils.getCurrentActivity(this)) || MyActivityManager.getScreenManager().getCurrentActivity() instanceof PopMessageActivity) {

				AfMessageInfo firstPopMsg = CacheManager.getInstance().getPopMessageManager().getmFirstPopMsg();

				if (firstPopMsg != null && afMsg.getKey().equals(firstPopMsg.getKey())) {

					if (mPopmessageReceiver != null) {
						mPopmessageReceiver.handlePopMessageFromServer(afMsg);
					}
				} else {

					toPopMessage(afMsg);

				}

			} else {
				toPopMessage(afMsg);

			}

		} else if (MessagesUtils.isGroupChatMessage(afMsg.type)) {

			// current popMsg has been shown
			if (PopGroupMessageActivity.class.getName().equals(CommonUtils.getCurrentActivity(this)) || MyActivityManager.getScreenManager().getCurrentActivity() instanceof PopGroupMessageActivity) {

				AfMessageInfo firstPopMsg = CacheManager.getInstance().getPopMessageManager().getmFirstPopMsg();

				if (firstPopMsg != null && afMsg.getKey().equals(firstPopMsg.getKey())) {
					if (mPopmessageReceiver != null) {
						mPopmessageReceiver.handlePopMessageFromServer(afMsg);
					}

				} else {
					toGroupPopMessage(afMsg);
				}
			} else {
				toGroupPopMessage(afMsg);
			}

		}

	}

	/**
	 * 
	 * @param afMsg
	 */
	private void toPopMessage(AfMessageInfo afMsg) {
		PalmchatLogUtils.println("toPopMessage " + "afMsg type:" + afMsg.type + "afMsg afid:" + afMsg.getKey());
		CacheManager.getInstance().getPopMessageManager().finishPrePopMessageActivity();
		CacheManager.getInstance().getPopMessageManager().setmFirstPopMsg(afMsg);
		Intent intent = new Intent(PalmchatApp.this, PopMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(JsonConstant.KEY_FROM_UUID, afMsg.getKey());
		startActivity(intent);
	}

	/**
	 * 
	 * @param afMsg
	 */
	private void toGroupPopMessage(AfMessageInfo afMsg) {
		CacheManager.getInstance().getPopMessageManager().finishPrePopMessageActivity();
		CacheManager.getInstance().getPopMessageManager().setmFirstPopMsg(afMsg);
		Intent intent = new Intent(PalmchatApp.this, PopGroupMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(GroupChatActivity.BundleKeys.ROOM_ID, afMsg.getKey());
		startActivity(intent);
	}

	private boolean isClosePopMessage() {
		Stack<Activity> activityStack = MyActivityManager.getActivityStack();
		int acSize = activityStack.size();
		if (acSize > 0) {
			for (int i = acSize - 1; i >= 0; i--) {
				Activity act = activityStack.get(i);
				if (act instanceof PopGroupMessageActivity)
					return true;
				else if (act instanceof PopMessageActivity)
					return true;
			}
		}
		return false;
	}

	/**
	 * zhh 通过RunningTaskInfo类判断（需要额外权限）：
	 * <uses-permission android:name="android.permission.GET_TASKS" />
	 * 判断当前应用程序处于前台还是后台
	 * 
	 * @return
	 */
	public boolean isApplicationBroughtToBackground() {
		ActivityManager am = (ActivityManager) getSystemService(android.content.Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity != null && !topActivity.getPackageName().equals(getPackageName())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * zhh 通过RunningAppProcessInfo类判断（不需要额外权限）： 判断当前应用程序处于前台还是后台
	 * 
	 * @return
	 */
	/*public boolean isBackground() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if(appProcesses != null) {
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess != null && appProcess.processName.equals(getPackageName())) {
					*//*由于挂后台后我们应用中仍然有service在跑，此时系统状态为IMPORTANCE_SERVICE  而不是 IMPORTANCE_BACKGROUND*//*
					if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND || appProcess.importance == RunningAppProcessInfo.IMPORTANCE_SERVICE) {
						PalmchatLogUtils.println("后台" + appProcess.processName);
						return true;
					} else {
						PalmchatLogUtils.println("前台" + appProcess.processName);
						return false;
					}
				}
			}
		}

		return false;
	}*/


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		LanguageUtil.updateLanguage(this, SharePreferenceUtils.getInstance(this).getLocalLanguage());
	}

	/**
	 * 此方法有问题，建议别用
	 * 
	 * @return
	 */
	public final int getWindowWidth() {
		return dm.widthPixels;
	}

	/**
	 * 此方法有问题，建议别用
	 * 
	 * @return
	 */
	public final int getWindowHeight() {
		return dm.heightPixels;
	}

	/**
	 * 获取目前的宽度
	 * 
	 * @return
	 */
	public final int getRealtimeWindowWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}



	public boolean isFacebookShareClose() {
		return mFacebookShareClose;
	}

	public void setFacebookShareClose(boolean facebookShareClose) {
		this.mFacebookShareClose = facebookShareClose;
	}

	/** 设置default_list */
	public void setDefaultList(ArrayList<AfResponseComm.AfBCRegionBroadcast> default_list) {
		this.broadcast_area_list = default_list;
	}

	/** 获取defgult_list */
	public ArrayList<AfResponseComm.AfBCRegionBroadcast> getDefaultList() {
		return broadcast_area_list;
	}

	/**
	 * 服务器跳转url
	 *
	 * @return
	 */
	public String getWebSkipUrl(int type) {
		String jumpUrl=null;
		if (TextUtils.isEmpty(jumpUrl)) {
			// 0测试服务器，1是线上服务器
			if (!isFormalServer()) {
				if(type==DefaultValueConstant.FILTER_RUL_BETWAY){//betway专用
					jumpUrl = DefaultValueConstant.URL_BETWAY_FILTER_TEST;
				}else {
					jumpUrl = DefaultValueConstant.URL_WEBSKIPTEST;
				}
			} else {
				if(type==DefaultValueConstant.FILTER_RUL_BETWAY){
					jumpUrl = DefaultValueConstant.URL_BETWAY_FILTER;
				}else {
					jumpUrl = DefaultValueConstant.URL_WEBSKIP;
				}
			}
		}
		return jumpUrl;
	}

	public String getPalmcallHost(){
		if(TextUtils.isEmpty(mHostAddr_Palmcall)){
			String[] info = mAfCorePalmchat.AfHttpGetServerInfo();
			if (info.length >= 7) {
				mHostAddr_Palmcall = mAfCorePalmchat.AfHttpGetServerInfo()[6];
			}
		}
		return  mHostAddr_Palmcall;
	}

	/**
	 * 根据当前库获取域名(新加坡、爱尔兰)
	 * 
	 * @param module
	 *            用来区分不同模块
	 * @return
	 */
	public String getDomainBySo(String module) {
		String url = "";
		if (module != null) {
			if (isFormalServer()) { // 正式环境
				if (module.equals(MyWapActivity.MODULE_PALMCOIN_TERMS) || module.equals(MyWapActivity.MODULE_GENERIC_TERMS) || module.equals(MyWapActivity.MODULE_AIRTIME_TERMS)) {//具体模块免责申明
					url = DefaultValueConstant.DOMAIN_TERMS_FORMAL;
				} else if (module.equals(MyWapActivity.MODULE_FEEDBACK)) {  //一般免责声明
					url = DefaultValueConstant.DOMAIN_FEEDBACK_FORMAL;
				}

			} else { // 测试环境
				if (module.equals(MyWapActivity.MODULE_PALMCOIN_TERMS) || module.equals(MyWapActivity.MODULE_GENERIC_TERMS) || module.equals(MyWapActivity.MODULE_AIRTIME_TERMS)) {// 免责声明
					url = DefaultValueConstant.DOMAIN_TERMS_TEST;
				} else if (module.equals(MyWapActivity.MODULE_FEEDBACK)) {
					url = DefaultValueConstant.DOMAIN_FEEDBACK_TEST;
				}
			}
		}

		return url;
	}

	/**
	 * 获取gif Url
	 * 
	 * @return
	 */
	public String getGifUrl() {
		if (TextUtils.isEmpty(this.mGifFace_Url)) {
			if (!isFormalServer()) {
				this.mGifFace_Url = DefaultValueConstant.GIF_FACE_TEST;
			} else {
				this.mGifFace_Url = DefaultValueConstant.GIF_FACE_FORMAL;
			}
		}
		return this.mGifFace_Url;
	}

	public boolean isFormalServer() {
		// 默认正式服务器，防止出错
		boolean isFormalServer = true;
		String[] info = mAfCorePalmchat.AfHttpGetServerInfo();
		if (info.length >= 6) {
			String serverName = mAfCorePalmchat.AfHttpGetServerInfo()[5];
			// 0测试服务器，1是线上服务器
			if (serverName.equals(String.valueOf(0))) {
				isFormalServer = false;
			} else {
				isFormalServer = true;
			}
		} else {
			// 如果中间件没有返回，默认正式
			isFormalServer = true;
		}

		return isFormalServer;
	}

	/** 是否是显示PalmCall的国家
	 * @return
	 */
	public boolean isCountry(){
		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		String country = myProfile.country;
		if (country != null && country.equals(DefaultValueConstant.COUNTRY_NIGERIA)) {
			PalmchatLogUtils.d("mengjie", "是尼日利亚");
			return true;
		}else {
			PalmchatLogUtils.d("mengjie", "不是尼日利亚");
			return false;
		}
	}
}
