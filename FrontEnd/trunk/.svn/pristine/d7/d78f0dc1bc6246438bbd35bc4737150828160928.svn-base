package com.afmobi.palmchat.ui.activity.setting;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.services.AppStatusService;
import com.afmobi.palmchat.ui.activity.friends.ContactsFriendsFragment;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnSelectButtonDialogListener;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
//import com.afmobi.palmchat.util.image.ImageLoader;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.umeng.analytics.MobclickAgent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 设置界面
 *
 */
public class SettingsActivity extends BaseActivity implements OnClickListener, AfHttpResultListener,AfHttpSysListener{

	private static final String TAG = SettingsActivity.class.getCanonicalName();

	/**通讯录备份view*/
	private RelativeLayout mRl_TelbookBackUp;
	/**我的账户*/
	private RelativeLayout mRl_MyAccount;
	/**修改密码*/
	private RelativeLayout mRl_ChangePassword;
	/**系统消息*/
	private RelativeLayout mRl_SystemSound;
	/**黑名单列表*/
	private RelativeLayout mRl_BlockedList;
	/**语言设置*/
	private RelativeLayout mRl_Language;
	/**版本信息*/
	private RelativeLayout mRl_About;
	/**注销按钮*/
	private Button mBtn_Logout;
	/**Application*/
	private PalmchatApp mPalmchatApp;
	/**本地接口*/
	private AfPalmchat mAfCorePalmchat;
	/**网络管理类*/
	/**该view用来显示new 更新显示*/
	View mView_NewFlag;
	/**回调facebooksdk*/
    private CallbackManager mCallbackManager;
    /**fackbook回调*/
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);
        }

        @Override
        public void onCancel() {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
	}
		
	@Override
	public void findViews() {
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		setContentView(R.layout.activity_settings);
		((TextView) findViewById(R.id.title_text)).setText(R.string.settings);
		
		mRl_TelbookBackUp = (RelativeLayout) findViewById(R.id.btn_phonebook_back_up);
		mRl_MyAccount = (RelativeLayout) findViewById(R.id.btn_my_account);
		mRl_ChangePassword = (RelativeLayout) findViewById(R.id.btn_change_password);
		mRl_About= (RelativeLayout)findViewById(R.id.btn_about);
		mRl_SystemSound =  (RelativeLayout)findViewById(R.id.btn_sound);
		mRl_SystemSound.setOnClickListener(this); 
		mRl_BlockedList = (RelativeLayout)findViewById(R.id.blockedlist_layout);
		
		mBtn_Logout = (Button)findViewById(R.id.btn_logout);
		
		mRl_Language = (RelativeLayout) findViewById(R.id.btn_language);
		
		mPalmchatApp=(PalmchatApp)getApplication();
		mRl_TelbookBackUp.setOnClickListener(this);
		mRl_MyAccount.setOnClickListener(this);
		if(SharePreferenceUtils.getInstance(this).getIsFacebookLogin()){//FB前台授权进来的不能显示该密码入口
			mRl_ChangePassword.setVisibility(View.GONE); 
		}else{
			mRl_ChangePassword.setOnClickListener(this);
		}
		
		mRl_BlockedList.setOnClickListener(this);
		mRl_About.setOnClickListener(this);
		mBtn_Logout.setOnClickListener(this);
		
		mAfCorePalmchat.AfAddHttpSysListener(this);
		mRl_Language.setOnClickListener(this);
		
		View backView = findViewById(R.id.back_button);
		if (backView != null) {
			backView.setOnClickListener(this);
		}	
		
		mView_NewFlag = findViewById(R.id.img_new);
	}

	@Override
	public void init() {
		
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		if(mView_NewFlag != null && mPalmchatApp.isHasNewVersion()){
			mView_NewFlag.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//mSocialNetworkManager.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//mSocialNetworkManager.onStop();
	}
 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			//返回键
			case R.id.back_button: {
				finish();
				break;
			}
			
			//通讯录备份
			case R.id.btn_phonebook_back_up:{
				startActivity(new Intent(this , BackupActivity.class));
				break;
			}
			
			//我的账号
			case R.id.btn_my_account:{
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MA);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_MA);
				startActivity(new Intent(this , MyAccountActivity.class));
				break;
			}
			//修改密码
			case R.id.btn_change_password:{
				startActivity(new Intent(this , ChangePasswordActivity.class));
				break;
			}
			
			//版本信息
			case R.id.btn_about:{
				startActivity(new Intent(this , AboutActivity.class));
				break;
			}
			//系统消息
			case R.id.btn_sound:{
	            startActivity(new Intent(this , SoundNotificationActivity.class));
				break;
			}
			
			//黑名单列表
			case R.id.blockedlist_layout: {
				startActivity(new Intent(this,BlockedListActivity.class));
				break;
			}
			//注销
			case R.id.btn_logout:{
				doBtnLogout();
				break;
			}
			//语言设置
			case R.id.btn_language:{
				startActivity(new Intent(this , LanguageActivity.class));
				break;
			}
			
			default:
				break;
		}
	}
	
	/**
	 * 助理"注销"按钮事件 
	 */
	private void doBtnLogout() {
		AppDialog dialog = new AppDialog(this);
		dialog.createLogOutDialog(this, new OnSelectButtonDialogListener() {
			@Override
			public void onCancelButtonClick(int selectIndex) {
				
				switch (selectIndex) {
				//close palmchat
				case 0: {
					//gtf 2014-11-16
					doClosePalmchat();
					break;
				}

				//logout
				case 1: {
					 
					AppUtils.doLogout(SettingsActivity.this,SettingsActivity.this);
					break;
				}
					
				default:
					break;
				}
			}
		});
		
		dialog.show();
	}
	private   Handler mHandler = new Handler();
	/**
	 * 处理注销点击
	 *//*
	private void doLogout() {
		//ContactsFriendsFragment.clearPhoneFriendList();//logout 的时候要清空
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LGO_LG);
//		MobclickAgent.onEvent(context, ReadyConfigXML.LGO_LG);
		CommonUtils.cancelNoticefacation(getApplicationContext());
		CommonUtils.cancelNetUnavailableNoticefacation();
		showProgressDialog(R.string.please_wait);
		mAfCorePalmchat.AfPollSetStatus(Consts.POLLING_STATUS_PAUSE);
		mAfCorePalmchat.AfHttpRemoveAllListener();
//		PalmchatApp.setIsLoadAccount(false);
//		PalmchatApp.getApplication().savePalmAccount(null,false);
		mPalmchatApp.setInit(false);
		mPalmchatApp.setContext(null);
		mPalmchatApp.setFacebookShareClose(true);
		mAfCorePalmchat.AfHttpLogout(0, SettingsActivity.this);
		CacheManager.getInstance().clearCache();
		mAfCorePalmchat.AfDbLoginSetStatus(CacheManager.getInstance().getMyProfile().afId, AfLoginInfo.AFMOBI_LOGIN_INFO_UNNORMAL_EXIT);
		AppStatusService.stop(SettingsActivity.this);
		PalmchatLogUtils.e(TAG, "---exit----");
		CacheManager.getInstance().setAfChatroomDetails(null);
		CacheManager.getInstance().setChatroomListTime(0);
		SharePreferenceUtils.getInstance(this).setIsFacebookLogin(false);
		mHandler.postDelayed(new Runnable() {//该为3秒后就logout 不再等待
			public void run() {
				mAfCorePalmchat.AfDbLoginSetStatus(CacheManager.getInstance().getMyProfile().afId, AfLoginInfo.AFMOBI_LOGIN_INFO_NORMAL);
				mAfCorePalmchat.AfCoreSwitchAccount();
			}
		}, 3*1000);	
	}
*/
	/**
	 * 处理关闭软件点击
	 */
	private void doClosePalmchat() {

		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LGO_EXIT);
//		MobclickAgent.onEvent(context, ReadyConfigXML.LGO_EXIT);
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
		PalmcallManager.getInstance().logoutPalmcall();
        SharePreferenceUtils.getInstance(context).setIsClosePalmchat(true);
        CommonUtils.cancelNoticefacation(getApplicationContext());
		CommonUtils.cancelNetUnavailableNoticefacation();
		MyActivityManager.getScreenManager().clear(); 
		android.os.Process.killProcess(Process.myPid()); 
	}
	/**
	 * 登入
	 * @param isThirdParLogin
	 */
	private void toLogin(boolean isThirdParLogin) {
		CacheManager.getInstance().setLoginSuccess(false);
		Intent intent = new Intent(this,LoginActivity.class);
		intent.putExtra(Constants.LOGOUT, true);
		intent.putExtra(JsonConstant.KEY_IS_THIRD_LOGIN, isThirdParLogin);
		if(isThirdParLogin){
			intent.putExtra(JsonConstant.KEY_MODE, 1);
		}
		startActivity(intent);
		finish();
	}

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.e(TAG, "----ywp: AfOnResult httpHandle =" + httpHandle +  " flag=" + flag + " code=" + code);
//		if(code == Consts.REQ_CODE_SUCCESS){
//			mAfCorePalmchat.AfDbLoginSetStatus(CacheManager.getInstance().getMyProfile().afId, AfLoginInfo.AFMOBI_LOGIN_INFO_NORMAL);
//		}
//		mAfCorePalmchat.AfCoreSwitchAccount();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	
	
	@Override
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		return true;
	}

	@Override
	public boolean AfHttpSysMsgProc(int msg, Object wparam, int lParam) {
		PalmchatLogUtils.println("SettingsActivity  AfHttpSysMsgProc  ");
		if(msg == Consts.AFMOBI_SYS_MSG_SWITCH_ACCOUNT){
			MyActivityManager.getScreenManager().clear();
			CommonUtils.cancelNoticefacation(getApplicationContext());
			dismissProgressDialog();
			
			AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
			boolean isThirdParLogin = SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).getThirdPartLogin(myProfile.afId);
			
			CacheManager.getInstance().setMyProfile(null);
//			ImageLoader.getInstance().clear();
			ImageManager.getInstance().clear();
			MsgAlarmManager.getInstence().cancelAllAlarm();
			CacheManager.getInstance().clearCache();
			CacheManager.getInstance().getGifImageUtilInstance().getImageFolder().clear();
			CacheManager.getInstance().getGifImageUtilInstance().getListFolders().clear();
		
			try {
				LoginManager.getInstance().logOut();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			toLogin(isThirdParLogin);
		}
		return false;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	//	mSocialNetworkManager.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAfCorePalmchat.AfRemoveHttpSysListener(this);
	//	mSocialNetworkManager.onDestroy();
	}
	
}
