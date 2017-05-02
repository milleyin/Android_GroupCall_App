package com.afmobi.palmchat.ui.activity.login;

import java.util.Arrays;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.OSInfo;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.services.AppStatusService;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.register.CompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.register.CountryActivity;
import com.afmobi.palmchat.ui.activity.register.EmailForgotPasswordActivity;
import com.afmobi.palmchat.ui.activity.register.ForgotPasswordActivity;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.ui.activity.setting.SecurityAnswerActivity;
import com.afmobi.palmchat.ui.customview.AfidLoginView;
import com.afmobi.palmchat.ui.customview.BaseDialog;
import com.afmobi.palmchat.ui.customview.EmailView;
import com.afmobi.palmchat.ui.customview.ForgetPasswordDialog;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.ui.customview.ListDialog;
import com.afmobi.palmchat.ui.customview.PhoneNumberView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfRegInfoParam;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.Profile.FetchProfileCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends BaseForgroundLoginActivity implements OnClickListener, OnItemClick, AfHttpResultListener  {

	private static final String TAG = LoginActivity.class.getCanonicalName();

	public static final int AFID = 1;
	public static final int PHONE_NUMBER = 2;
	public static final int EMAIL = 3;

	public static final int IMEI = 4;
	private static final int LENGTH_PASSWORD_BELOW_ONE = 0;
	private static final int LENGTH_PASSWORD_ABOVE_SIXTHTEEN = 16;
	private EditText vEditTextLoginUser, vEditTextPassword;
	private Button vButtonLogin;
	private AfidLoginView contentViewAfid;
	private PhoneNumberView contentViewPhoneNumber;
	private EmailView contentViewEmail;
	private View viewRoot, viewSignUp, viewForgetPassword, viewSelectCountry;
	private ForgetPasswordDialog dialogForget;
	private int loginMode = AFID;
	private String mAfid = "";
	private String mEmail = "";
	private String mPhone;
	private AfPalmchat mAfCorePalmchat;
	private boolean mIsShowPhone, mLoginModePhoneNumber;
	String afid;
	String email;
	String phone;
	String mPassword;
	private TextView mForgotPwd, mSignUp, mLoginPhone, mLoginEmail, vTextViewCode, vTextViewCountryText, vTextViewCountryTextPrompt;
	private ImageView vImageViewBack, vImageViewHelp;
	private int forgetMode;

	private BaseDialog mWheelWindow;
	private View viewHelp;
	private boolean isRegByEmail = false;
	private boolean isThirdPartLogin;
	private Dialog dialog;
	private String mThirdPartUserId;
	private boolean is_logout;
	private boolean is_third_part;
	private int mHttpHandle = 0;// 登录请求的Http Handle，加这个变量是为了解决：ID号登录过程中 按back
								// 关闭登录中的进度条， 再按back回到桌面时 可以cancel掉请求， bug ID
								// 8500

	private int mType = Consts.AF_LOGIN_AFID; // 登陆方式
	
	/**facebook个人信息*/
	private Profile mFbProfile;
    /**回调facebooksdk*/
    private CallbackManager mCallbackManager;
    /**获取FB个人资料回调*/
    private FetchProfileCallback mFetchProfileCallback = new FetchProfileCallback(){

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			PalmchatLogUtils.i(TAG, "--FetchProfileCallback---onSuccess----");
			mHandler.sendEmptyMessage(DefaultValueConstant.FETCHPROFILE_SUCCESS);
		}

		@Override
		public void onError(FacebookException error) {
			// TODO Auto-generated method stub
			PalmchatLogUtils.i(TAG, "--FetchProfileCallback---onError----"+error.toString());
			mHandler.sendEmptyMessage(DefaultValueConstant.FETCHPROFILE_FAILURE);
		}
    	
    };
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
    
	private Handler mHandler = new Handler() {
		public void handleMessage (Message msg) {
			switch (msg.what) {
				//facebook登陆成功
				case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
					ToastManager.getInstance().show(LoginActivity.this, R.string.success);
					//showProgressDialog(R.string.loading);
					setProfile();
					break;
				}
				//facebook登陆失败
				case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
					dismissAllDialog();
					ToastManager.getInstance().show(LoginActivity.this, R.string.please_try_again);
					break;
				}

				case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
					ToastManager.getInstance().show(LoginActivity.this, R.string.facebook_login_cancel_tip);
					break;
				}
				
				//获取个人资料成功
				case DefaultValueConstant.FETCHPROFILE_SUCCESS:{
					setProfile();
					break;
				}
				//获取个人资料失败
				case DefaultValueConstant.FETCHPROFILE_FAILURE:{
					dismissAllDialog();
					ToastManager.getInstance().show(LoginActivity.this,R.string.fetchprofile_failure);
					break;
				}
				
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		super.onCreate(savedInstanceState);
		mCallbackManager = CallbackManager.Factory.create();
	    LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);

	}

	@Override
	public void findViews() {
		Intent intent = getIntent();
		mPhone = intent.getStringExtra(JsonConstant.KEY_PHONE);
		mEmail = intent.getStringExtra(JsonConstant.KEY_EMAIL);

		mIsShowPhone = intent.getBooleanExtra(JsonConstant.KEY_IS_SHOW_PHONE_NUMBER, false);
		isRegByEmail = intent.getBooleanExtra(JsonConstant.KEY_IS_REG_BY_EMAIL, false);
		loginMode = intent.getIntExtra(JsonConstant.KEY_MODE, AFID);

		is_logout = intent.getBooleanExtra(Constants.LOGOUT, false);

		is_third_part = intent.getBooleanExtra(JsonConstant.KEY_IS_THIRD_LOGIN, false);

		mLoginModePhoneNumber = SharePreferenceUtils.getInstance(this).getLoginModePhoneNumber();
		PalmchatLogUtils.println("mLoginModePhoneNumber:" + mLoginModePhoneNumber);
		if (mIsShowPhone) {
			setLoginMode(PHONE_NUMBER);
		}

		AfLoginInfo afLoginInfo =null;// PalmchatApp.getApplication().getPalmchatAccounts();
		if (afLoginInfo == null) {
			AfLoginInfo[] myAccounts = mAfCorePalmchat.AfDbLoginGetAccount();
			if (myAccounts != null && myAccounts.length > 0) {
				PalmchatLogUtils.e("LoginActivity", "LoginActivity  findViews  ");
				afLoginInfo = myAccounts[0];
			}
		}

		if (afLoginInfo != null) {
			afid = afLoginInfo.afid;
			email = afLoginInfo.email;
			phone = afLoginInfo.phone;
			mPassword = afLoginInfo.password;
			mType = afLoginInfo.type;
		}
		if (!TextUtils.isEmpty(mPhone)) {
			phone = mPhone;
		}
		/*根据登陆方式，加载布局*/
		setView(getLoginMode());
		set();

		SharePreferenceUtils.getInstance(this).setNotCompleteImei(false);

		if (mLoginModePhoneNumber) {
			mLoginPhone.performClick();
			SharePreferenceUtils.getInstance(this).setLoginModePhoneNumber(false);
		}
	}
	

	@Override
	public void init() {

	}

	/**
	 * if has facebook client in phone
	 * 
	 * @return
	 */
	private boolean hasThirdLoginClient(byte type) {

		String thirdPackageName;

		switch (type) {

		case Consts.AF_LOGIN_FACEBOOK:
			thirdPackageName = FacebookConstant.FACEBOOKPACKAGE;
			break;

		case Consts.AF_LOGIN_GOOGLE:
			thirdPackageName = "com.google.android.apps.plus";
			break;

		case Consts.AF_LOGIN_TWITTER:
			thirdPackageName = "com.twitter.android";
			break;

		default:
			thirdPackageName = "";
			break;
		}

		boolean value;
		try {
			getPackageManager().getApplicationInfo(thirdPackageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			value = true;
		} catch (NameNotFoundException e) {
			value = false;
		}

		PalmchatLogUtils.println("--xxxx hasThirdLoginClient: type = " + type + " value = " + value);
		return value;
	}

	 

	/**
	 * 根据登陆方式，显示页面布局
	 * @param loginMode
	 */
	private void setView(int loginMode) {
		setLoginMode(loginMode);
		if (loginMode == AFID) {  //afid登陆 

			// heguiming 2013-12-04
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_A);
//			MobclickAgent.onEvent(this, ReadyConfigXML.ENTRY_LG_A);

			if (contentViewAfid == null) {
				contentViewAfid = new AfidLoginView(this);
			}
			viewRoot = contentViewAfid.getRoot();
			setContentView(viewRoot);
			((TextView) findViewById(R.id.title_text)).setText(getString(R.string.af_id));
		} else if (loginMode == PHONE_NUMBER) {  //手机号登陆
			// heguiming 2013-12-04
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_P);
//			MobclickAgent.onEvent(this, ReadyConfigXML.ENTRY_LG_P);
			
			if (contentViewPhoneNumber == null) {
				contentViewPhoneNumber = new PhoneNumberView(this);
			}
			viewRoot = contentViewPhoneNumber.getRoot();
			setContentView(viewRoot);
			((TextView) findViewById(R.id.title_text)).setText(getString(R.string.phone_number));
		} else if (loginMode == EMAIL) {  //email登陆

			// heguiming 2013-12-04
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_E);
//			MobclickAgent.onEvent(this, ReadyConfigXML.ENTRY_LG_E);

			if (contentViewEmail == null) {
				contentViewEmail = new EmailView(this);
			}
			viewRoot = contentViewEmail.getRoot();
			setContentView(viewRoot);
			((TextView) findViewById(R.id.title_text)).setText(getString(R.string.email));
		}

		findViewById(R.id.text_more).setOnClickListener(this);

		setAccount();

	}

	private void onBack() {
		if (loginMode != AFID && !mIsShowPhone && !isRegByEmail) {
			setLoginMode(AFID);
			setView(AFID);
			onNotify();
		} else {
			if (mAfCorePalmchat != null && mHttpHandle != Consts.AF_HTTP_HANDLE_INVALID) {
				mAfCorePalmchat.AfHttpCancel(mHttpHandle);
			}
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		CommonUtils.cancelNoticefacation(getApplicationContext());

		if (loginMode == AFID) {
			vImageViewBack.setVisibility(View.GONE);
		} else {
			vImageViewBack.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mSocialNetworkManager.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		mSocialNetworkManager.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		mSocialNetworkManager.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("LoginActivity");
//		MobclickAgent.onPause(this);
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		mSocialNetworkManager.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:

			onBack();

			break;
		}

		return true;
	}



	private void showForgetPasswordDialog(int forgetMode) {
		dialogForget = null;
		dialogForget = new ForgetPasswordDialog(this, forgetMode);
		dialogForget.setItemClick(this);
		dialogForget.show();
	}

	private void setPhoneView() {
		String[] str = CommonUtils.getCountryAndCode(this);
		vTextViewCountryText.setText(str[0]);
		vTextViewCode.setText(str[1]);
	}

	private void setLoginMode(int loginMode) {
		this.loginMode = loginMode;
	}

	private int getLoginMode() {
		return loginMode;
	}

	private void setAccount() {
		if (vEditTextLoginUser != null) {
			vEditTextLoginUser.setText("");
			if (getLoginMode() == AFID) {
				String lastLoginAfid = afid;// SharePreferenceUtils.getInstance(LoginActivity.this).getLastLoginAfid();
				lastLoginAfid = CommonUtils.getCorrectAfid(lastLoginAfid);
				vEditTextLoginUser.setText(lastLoginAfid);
				vEditTextPassword.setText(mPassword);

				if (!TextUtils.isEmpty(mAfid) ) {
					vEditTextLoginUser.setText(mAfid);
				}

				vEditTextLoginUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login_ico_id, 0, 0, 0);
				if (vTextViewCountryTextPrompt != null) {
					vTextViewCountryTextPrompt.setVisibility(View.GONE);
				}
			} else if (getLoginMode() == PHONE_NUMBER) {
				String lastLoginAccount = phone;// SharePreferenceUtils.getInstance(LoginActivity.this).getLastLoginInfo();
				if (!TextUtils.isEmpty(lastLoginAccount) ) {
					vEditTextLoginUser.setText(lastLoginAccount);
					vEditTextPassword.setText(mPassword);
				} else {
					vEditTextPassword.setText("");
				}
				vEditTextLoginUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login_ico_ph, 0, 0, 0);
				if (vTextViewCountryTextPrompt != null) {
					vTextViewCountryTextPrompt.setVisibility(View.VISIBLE);
				}
				if (mIsShowPhone) {
					vEditTextPassword.setText("");
				}
			} else if (getLoginMode() == EMAIL) {
				String lastLoginEmail = email;// SharePreferenceUtils.getInstance(LoginActivity.this).getLastLoginEmail();
				if (!TextUtils.isEmpty( lastLoginEmail)  ) {
					vEditTextLoginUser.setText(lastLoginEmail);
					vEditTextPassword.setText(mPassword);
				} else {
					vEditTextPassword.setText("");
				}
				if (!TextUtils.isEmpty(mEmail)) {
					vEditTextLoginUser.setText(mEmail);
					vEditTextPassword.setText("");
				} else {

				}
				vEditTextLoginUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login_ico_em, 0, 0, 0);
				if (vTextViewCountryTextPrompt != null) {
					vTextViewCountryTextPrompt.setVisibility(View.GONE);
				}
				if (isRegByEmail) {
					vEditTextPassword.setText("");
				}
			}

			PalmchatLogUtils.println("--fffdddss is_third_part " + is_third_part);

			if (mType == Consts.AF_LOGIN_FACEBOOK || mType == Consts.AF_LOGIN_IMEI || is_third_part) {
				vEditTextPassword.setText("");
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_button: {// login
			login();
			break;
		}
		case R.id.linearLayout_sign_up: {// sign up
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_PNUM);
//			MobclickAgent.onEvent(context, ReadyConfigXML.REG_PNUM);
			toSignUp();
			break;
		}
		case R.id.linearLayout_forget_password: {// forget password
			toForgotPassword();

			break;
		}

		case R.id.forget_pwd: {
			showForgetPasswordDialog(forgetMode);
			break;
		}
		case R.id.sign_up: {
			Intent intent = new Intent(this, RegistrationActivity.class);
			intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.REG);
			intent.putExtra(JsonConstant.KEY_FROM, SignUpOrLoginActivity.class.getName());
			startActivity(intent);
			break;
		}

		case R.id.btn_ph: {
			if (getLoginMode() == LoginActivity.AFID) {
				setView(PHONE_NUMBER);
			} else if (getLoginMode() == LoginActivity.PHONE_NUMBER) {
				setView(AFID);
			} else {
				setView(AFID);
			}
			onNotify();

			// setPhoneView();
			break;
		}

		case R.id.btn_em: {
			if (getLoginMode() == LoginActivity.AFID) {
				setView(EMAIL);
			} else if (getLoginMode() == LoginActivity.PHONE_NUMBER) {
				setView(EMAIL);
			} else {
				setView(PHONE_NUMBER);
			}
			onNotify();
			break;
		}

		case R.id.back_button: {

			onBack();
			break;

		}

		case R.id.select_country: {
			Intent intent=new Intent(this, CountryActivity.class);
			intent.putExtra(JsonConstant.KEY_SELECT_COUNTRY_ONLY,true);
			startActivityForResult(intent, DefaultValueConstant._86);

			break;
		}
		case R.id.help: {
			mWheelWindow.show();
			break;
		}

		case R.id.layout_login_facebook:

			if (hasThirdLoginClient(Consts.AF_LOGIN_FACEBOOK)) {
				new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_FB);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_FB);
			} else {
				new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_WFB);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_WFB);
			}

			try {
				LoginManager.getInstance().logOut();
			} catch (Exception e) {
			}
			vEditTextLoginUser.setText("");
			vEditTextPassword.setText("");
			//showProgressDialog(R.string.loading);
			new Thread(new Runnable() {  
				@Override  
				public void run() {
					LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
							Arrays.asList("user_location", "user_birthday", "email", "user_friends"));
				}  
			}).start();
			break;

		// google+ twitter login dialog
		case R.id.text_more:
			alertMenu();
			break;

		default:
			break;
		}
	}

	/**
	 * google+ twitter login dialog
	 */
	private void alertMenu() {
		DialogItem[] items = new DialogItem[] { new DialogItem(R.string.login_with_google_plus, R.layout.custom_dialog_normal), new DialogItem(R.string.login_with_twitter, R.layout.custom_dialog_normal), new DialogItem(R.string.cancel, R.layout.custom_dialog_cancel) };
		ListDialog dialog = new ListDialog(context, Arrays.asList(items));

		dialog.setItemClick(new ListDialog.OnItemClick() {

			@Override
			public void onItemClick(DialogItem item) {

				switch (item.getTextId()) {

				case R.string.login_with_google_plus:

					if (hasThirdLoginClient(Consts.AF_LOGIN_GOOGLE)) {
						new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_GG);
//						MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_GG);
					} else {
						new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_WGG);
//						MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_WGG);
					}

					try {
						LoginManager.getInstance().logOut();
					} catch (Exception e) {
						// TODO: handle exception
					}
//					mSocialNetworkManager.requestLogin(com.afmobi.asn.lib.Consts.GOOGLEPLUS_SOCIAL, new MyLoginCompleteListener());
					break;

				case R.string.login_with_twitter:

					if (hasThirdLoginClient(Consts.AF_LOGIN_TWITTER)) {
						new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_TW);
//						MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_TW);
					} else {
						new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_WTW);
//						MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_WTW);
					}

					try {
						LoginManager.getInstance().logOut();
					} catch (Exception e) {
					}


					break;

				default:
					break;
				}

			}
		});

		dialog.show();
	}




	
	private void setProfile(){
		showProgressDialog(R.string.loading);
		OSInfo os = PalmchatApp.getApplication().osInfo;
		Profile profile = Profile.getCurrentProfile();
		mFbProfile = profile;
		if(null==profile){
			new Thread(new Runnable() {
				@Override
				public void run() {
					PalmchatLogUtils.i(TAG, "--FacebookCallback---profile==NULL----");	
					Profile.setFetchProfileCallback(mFetchProfileCallback);	
					Profile.fetchProfileForCurrentAccessToken();
				}
			}).start();
		}
		
		if((null!=os)&&(null!=profile)){
			String countryCode = PalmchatApp.getApplication().osInfo.getCountryCode();
			isThirdPartLogin = true;
			mThirdPartUserId = profile.getId();
			AfRegInfoParam param = new AfRegInfoParam();
			param.cc = countryCode;
			param.imei = PalmchatApp.getOsInfo().getImei();
			param.imsi = PalmchatApp.getOsInfo().getImsi();
			param.sex = Consts.AFMOBI_SEX_MALE;
			
			param.birth = DefaultValueConstant.BIRTHDAY;
			param.name = null;
			param.phone_or_email = profile.getId();
			param.password = Constants.FACEBOOK_PASSWORD;
			
			param.region = PalmchatApp.getOsInfo().getState(context);
			param.city = PalmchatApp.getOsInfo().getCity(context);
			param.country = PalmchatApp.getOsInfo().getCountry(context);
            param.fb_token = AccessToken.getCurrentAccessToken().getToken();
            mAfCorePalmchat.AfHttpRegLogin(param, mThirdPartUserId, Consts.AF_LOGIN_FACEBOOK, Consts.AF_LOGIN_FACEBOOK, LoginActivity.this);
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);;
		if (requestCode == DefaultValueConstant._86) {
			if (!"".equals(data) && data != null) {
				if(vTextViewCountryText!=null) {
					vTextViewCountryText.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY ));
				}
				if(vTextViewCode!=null) {
					vTextViewCode.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE));
				}
			}
		}
	}

	private void toSignUp() {
		Intent intent = new Intent(this, RegistrationActivity.class);
		intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.REG);
		startActivity(intent);
	}

	private void toForgotPassword() {
		Intent intent = new Intent(this, ForgotPasswordActivity.class);
		startActivity(intent);
	}

	public void login() {
		String user = getAfid();
		String pass = getPassword();
		if (user.length() <= 0) {
			if (getLoginMode() == AFID) {
				ToastManager.getInstance().show(LoginActivity.this, R.string.prompt_input_afid);
				return;
			} else if (getLoginMode() == PHONE_NUMBER) {
				ToastManager.getInstance().show(LoginActivity.this, R.string.prompt_input_phone);
				return;
			} else if (getLoginMode() == EMAIL) {
				ToastManager.getInstance().show(LoginActivity.this, R.string.prompt_input_email);
				return;
			}
		} else {
			if (getLoginMode() == EMAIL && !CommonUtils.isEmail(user)) {
				ToastManager.getInstance().show(LoginActivity.this, R.string.prompt_email_address_not_legal);
				return;
			}
		}

		if (getLoginMode() == PHONE_NUMBER && user.startsWith(CommonUtils.getRealCountryCode(getTextViewCode()))) {
			ToastManager.getInstance().show(this, R.string.phone_inputcoutrycode_tips1);
		} else if (pass.length() <= LENGTH_PASSWORD_BELOW_ONE) {
			ToastManager.getInstance().show(LoginActivity.this, R.string.enter_password);
		} else if (pass.length() > LENGTH_PASSWORD_ABOVE_SIXTHTEEN) {

			ToastManager.getInstance().show(LoginActivity.this, R.string.prompt_input_password_little6);
		} else if (getLoginMode() == PHONE_NUMBER && (TextUtils.isEmpty(user) || user.length() < 8 || user.length() > 15)) { // 服务器手机号限制为大于等于8且小于等于15
			ToastManager.getInstance().show(LoginActivity.this, R.string.invalid_number);
		} else {
			showProgDialog(R.string.logining);
			if (loginMode == AFID) {
				isThirdPartLogin = false;
				mHttpHandle = PalmchatApp.getApplication().AfHttpLogin(user, PalmchatApp.getOsInfo().getMcc(), user, pass, PalmchatApp.getOsInfo().getCountryCode(), Consts.AF_LOGIN_AFID, 0, this);
			} else if (loginMode == PHONE_NUMBER) {

				isThirdPartLogin = false;
				PalmchatApp.getApplication().AfHttpLogin(user, PalmchatApp.getOsInfo().getMcc(), user, pass, CommonUtils.getRealCountryCode(getTextViewCode()), Consts.AF_LOGIN_PHONE, 0, this);
			} else if (loginMode == EMAIL) {
				isThirdPartLogin = false;
				PalmchatApp.getApplication().AfHttpLogin(user, PalmchatApp.getOsInfo().getMcc(), user, pass, PalmchatApp.getOsInfo().getCountryCode(), Consts.AF_LOGIN_EMAIL, 0, this);
			}

		}

	}

	private String getFormatCountryCode() {
		return vTextViewCode.getText().toString().replace("+", "");
	}

	private String getTextViewCode() {
		if (vTextViewCode != null) {
			return vTextViewCode.getText().toString().trim();
		}
		return "+" + PalmchatApp.getOsInfo().getCountryCode();
	}

	private String getPassword() {
		return vEditTextPassword.getText().toString().trim();
	}

	private String getAfid() {
		return vEditTextLoginUser.getText().toString().trim();
	}

	
	/*private void toMain(byte login_type) {
		dismissDialog();
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend, true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, login_type);
		startActivity(intent);
		finish();
	}*/
	/** 跳转到主界面 */
	private void toMainTab(byte login_type) {
		Intent intent = new Intent(LoginActivity.this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, login_type);
		intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	final public void onNotify() {
		set();
	}

	private void set() {
		vTextViewCountryTextPrompt = (TextView) viewRoot.findViewById(R.id.tv_mode);
		vEditTextLoginUser = (EditText) findViewById(R.id.login_user);
		vEditTextPassword = (EditText) findViewById(R.id.login_password);
		vButtonLogin = (Button) findViewById(R.id.login_button);
		vButtonLogin.setOnClickListener(this);

		viewSelectCountry = viewRoot.findViewById(R.id.select_country);
		vImageViewHelp = (ImageView) viewRoot.findViewById(R.id.help);
		vImageViewHelp.setOnClickListener(this);
		if (getLoginMode() == EMAIL) {
			vEditTextLoginUser.addTextChangedListener(new LimitTextWatcher(vEditTextLoginUser, DefaultValueConstant.LENGTH_100));
		} else {
			vEditTextLoginUser.addTextChangedListener(new LimitTextWatcher(vEditTextLoginUser, DefaultValueConstant.LENGTH_16));
			vEditTextLoginUser.setText(mPhone);
		}
		setAccount();

		vEditTextPassword = (EditText) viewRoot.findViewById(R.id.login_password);
		vEditTextPassword.addTextChangedListener(new LimitTextWatcher(vEditTextPassword, 16));
		vEditTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login_ico_psw_style, 0, 0, 0);
		viewForgetPassword = viewRoot.findViewById(R.id.linearLayout_forget_password);
		viewForgetPassword.setOnClickListener(this);
		viewSignUp = viewRoot.findViewById(R.id.linearLayout_sign_up);
		viewSignUp.setOnClickListener(this);
		findViewById(R.id.layout_login_facebook).setOnClickListener(this);
		if (getLoginMode() == AFID) {

			mLoginPhone = (TextView) viewRoot.findViewById(R.id.btn_ph);
			mLoginPhone.setOnClickListener(this);

			mLoginEmail = (TextView) viewRoot.findViewById(R.id.btn_em);
			mLoginEmail.setOnClickListener(this);

		} else if (getLoginMode() == PHONE_NUMBER) {
			vTextViewCode = (TextView) viewRoot.findViewById(R.id.cty_code);
			vTextViewCountryText = (TextView) viewRoot.findViewById(R.id.country_text);
			setPhoneView();
		}

		mForgotPwd = (TextView) viewRoot.findViewById(R.id.forget_pwd);
		mForgotPwd.setOnClickListener(this);
		mSignUp = (TextView) viewRoot.findViewById(R.id.sign_up);
		mSignUp.setOnClickListener(this);
		vImageViewBack = (ImageView) findViewById(R.id.back_button);
		vImageViewBack.setOnClickListener(this);

		mWheelWindow = new BaseDialog(this);

		viewHelp = LayoutInflater.from(this).inflate(R.layout.login_help_layout, null);
		mWheelWindow.setContentView(viewHelp);

		if (PHONE_NUMBER == loginMode) {
			viewSelectCountry.setOnClickListener(this);
		}
		viewForgetPassword.setOnClickListener(this);
		vButtonLogin.setOnClickListener(this);
	}

	@Override
	public void onItemClick(int item) {
		Intent mIntent;
		switch (item) {
		case ForgetPasswordDialog.FORGET_MODE_PHONE_NUMBER:
			mIntent = new Intent(this, RegistrationActivity.class);
			mIntent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.RESET);
			mIntent.putExtra(RegistrationActivity.REGISTER_TYPE, RegistrationActivity.R_PHONE_NUMBER);
			if (getLoginMode() == LoginActivity.AFID) {
				mIntent.putExtra(JsonConstant.KEY_MODE, LoginActivity.AFID);
			} else if (getLoginMode() == LoginActivity.PHONE_NUMBER) {
				mIntent.putExtra(JsonConstant.KEY_MODE, LoginActivity.PHONE_NUMBER);
			} else if (getLoginMode() == LoginActivity.EMAIL) {
				mIntent.putExtra(JsonConstant.KEY_MODE, LoginActivity.EMAIL);
			}
			startActivity(mIntent);
			finish();
			break;
		case ForgetPasswordDialog.FORGET_MODE_EMAIL:
			mIntent = new Intent(this, EmailForgotPasswordActivity.class);
			startActivity(mIntent);
			break;
		case ForgetPasswordDialog.FORGET_MODE_SECURITY:
			Intent in = new Intent(this, SecurityAnswerActivity.class);
			startActivity(in);
			break;
		default:

			break;
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		//dismissAllDialog();
		PalmchatLogUtils.e(TAG, "----AfOnResult--Flag:" + flag + "---Code:" + code);
		mHttpHandle = Consts.AF_HTTP_HANDLE_INVALID;
		if (code == Consts.REQ_CODE_SUCCESS) {

			switch (flag) {
			case Consts.REQ_FLAG_LOGIN:

				AfProfileInfo myProfile = (AfProfileInfo) result;
				if (myProfile != null) {
					CacheManager.getInstance().setMyProfile(myProfile);
					CacheManager.getInstance().setPalmGuessShow(myProfile.palmguess_flag == 1);
//					CacheManager.getInstance().setOpenAirtime(myProfile.airtime == 1);
 					CacheManager.getInstance().setRecharge_intro_url(myProfile.recharge_intro_url); //设置充值方式说明的 url
					double lat = 0d;
					double lon = 0d;
					PalmchatLogUtils.println("myProfile.lat:" + myProfile.lat + "  myProfile.lng:" + myProfile.lng);
					if (  !TextUtils.isEmpty(myProfile.lat) && myProfile.lat.trim().length() > 0) {
						lat =CommonUtils.stringToDouble(myProfile.lat);
					}
					if (  !TextUtils.isEmpty(myProfile.lng) && myProfile.lat.trim().length() > 0) {
						lon = CommonUtils.stringToDouble(myProfile.lng);
					}
					PalmchatLogUtils.e(TAG, "login....lat = " + lat + ",lon = " + lon);
					SharePreferenceUtils.getInstance(context).setLatitudeAndLongitude(lat, lon);

					if (isThirdPartLogin) {

						if (user_data instanceof Byte) {
							byte thirdLoginType = (Byte) user_data;

							switch (thirdLoginType) {

							case Consts.AF_LOGIN_FACEBOOK:
								if (hasThirdLoginClient(Consts.AF_LOGIN_FACEBOOK)) {
									new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.LG_FB_SUCC);
//									MobclickAgent.onEvent(context, ReadyConfigXML.LG_FB_SUCC);
								} else {
									new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.LG_WFB_SUCC);
//									MobclickAgent.onEvent(context, ReadyConfigXML.LG_WFB_SUCC);
								}

								new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_HOME_FB);
//								MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_HOME_FB);

								break;

							case Consts.AF_LOGIN_GOOGLE:

								if (hasThirdLoginClient(Consts.AF_LOGIN_GOOGLE)) {
									new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.LG_GG_SUCC);
//									MobclickAgent.onEvent(context, ReadyConfigXML.LG_GG_SUCC);
								} else {
									new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.LG_WGG_SUCC);
//									MobclickAgent.onEvent(context, ReadyConfigXML.LG_WGG_SUCC);
								}

								new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_HOME_GG);
//								MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_HOME_GG);

								break;

							case Consts.AF_LOGIN_TWITTER:

								if (hasThirdLoginClient(Consts.AF_LOGIN_TWITTER)) {
									new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.LG_TW_SUCC);
//									MobclickAgent.onEvent(context, ReadyConfigXML.LG_TW_SUCC);
								} else {
									new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.LG_WTW_SUCC);
//									MobclickAgent.onEvent(context, ReadyConfigXML.LG_WTW_SUCC);
								}

								new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_HOME_TW);
//								MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_HOME_TW);

								break;

							default:
								break;

							}
//							myProfile.login_type = thirdLoginType;
						}
						myProfile.password = Constants.FACEBOOK_PASSWORD;
						myProfile.third_account = mThirdPartUserId;
						SharePreferenceUtils.getInstance(this).setIsFacebookLogin(true);
						CacheManager.getInstance().setMyProfile(myProfile);

						mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, myProfile);
						setLoadAccount(myProfile, Consts.AF_LOGIN_FACEBOOK, mAfCorePalmchat);//调中间件保存当前的登陆方式和信息
						// "1," was setted by server to distinguish whether
						// third login user registered or not.
						if (!"1,".equals(myProfile.attr2) || (DefaultValueConstant.OTHER.equalsIgnoreCase(myProfile.city) || DefaultValueConstant.OTHERS.equalsIgnoreCase(myProfile.city) || DefaultValueConstant.OTHER.equalsIgnoreCase(myProfile.country) || DefaultValueConstant.OTHERS.equalsIgnoreCase(myProfile.country) || DefaultValueConstant.OTHER.equalsIgnoreCase(myProfile.region) || DefaultValueConstant.OTHERS.equalsIgnoreCase(myProfile.region))) {
							Intent intent = new Intent(this, FacebookCompleteProfileActivity.class);
							intent.putExtra("FacebookId", mThirdPartUserId);
							intent.putExtra("afid", myProfile.afId);
							intent.putExtra("SocialPerson", mFbProfile);

							if (user_data instanceof Byte) {
								byte thirdLoginType = (Byte) user_data;
								intent.putExtra("ThirdLoginType", thirdLoginType);
							}
							dismissAllDialog();
							startActivity(intent); 
						} else {
							toMainTab(Consts.AF_LOGIN_FACEBOOK); 
						}
					} else {
						myProfile.password = getPassword();

						// heguiming 2013-12-04
						if (loginMode == AFID) {
							new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_A_SUCC);
//							MobclickAgent.onEvent(this, ReadyConfigXML.LG_A_SUCC);
						} else if (loginMode == PHONE_NUMBER) {
							new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_P_SUCC);
//							MobclickAgent.onEvent(this, ReadyConfigXML.LG_P_SUCC);
						} else if (loginMode == EMAIL) {
							new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LG_E_SUCC);
//							MobclickAgent.onEvent(this, ReadyConfigXML.LG_E_SUCC);
						}
						setLoadAccount(myProfile, Consts.AF_LOGIN_AFID, mAfCorePalmchat);//调中间件保存当前的登陆方式和信息
						if (CommonUtils.isEmpty(myProfile.name) || CommonUtils.isEmpty(myProfile.birth) || DefaultValueConstant.OVERSEA.equals(myProfile.country)) {
							Intent intent = new Intent(this, CompleteProfileActivity.class);
							intent.putExtra(JsonConstant.KEY_PROFILE, myProfile);
							dismissAllDialog();
							startActivity(intent);
						} else { 
							 if (loginMode == AFID) {
								toMainTab(Consts.AF_LOGIN_AFID);
							} else if (loginMode == PHONE_NUMBER) {
								toMainTab(Consts.AF_LOGIN_PHONE);
							} else if (loginMode == EMAIL) {
								toMainTab(Consts.AF_LOGIN_EMAIL);
							} else if (loginMode == IMEI) {
								toMainTab(Consts.AF_LOGIN_IMEI);
							} 
						}

					}

					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStatistic(true, true, new ReadyConfigXML().getLoginSuccessHttpJsonStr(), null, this);
				} else {
					Consts.getInstance().showToast(this, code, flag, http_code);
				}
				break;
			default:
				dismissDialog();
				break;
			}
		} else {
			dismissDialog();
			if (flag == Consts.REQ_FLAG_LOGIN && code == -160) {// 说明第三方登录信息还未存入服务器，跳转到完成第三方信息资料页面
				if (isThirdPartLogin && null != mThirdPartUserId) {

					if (user_data instanceof Byte) {
						byte thirdLoginType = (Byte) user_data;
						switch (thirdLoginType) {

						case Consts.AF_LOGIN_FACEBOOK:
							new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_CPF_FB);
//							MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_CPF_FB);
							break;

						case Consts.AF_LOGIN_GOOGLE:
							new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_CPF_GG);
//							MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_CPF_GG);
							break;

						case Consts.AF_LOGIN_TWITTER:
							new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_CPF_TW);
//							MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_CPF_TW);
							break;

						default:
							break;
						}
					}

					Intent intent = new Intent(this, FacebookCompleteProfileActivity.class);
					intent.putExtra("FacebookId", mThirdPartUserId);

					intent.putExtra("SocialPerson", mFbProfile);

					if (user_data instanceof Byte) {
						byte thirdLoginType = (Byte) user_data;
						intent.putExtra("ThirdLoginType", thirdLoginType);
					}
					startActivity(intent);

				} else {

					Consts.getInstance().showToast(this, code, flag, http_code);
				}
			} else {

				if (flag == Consts.REQ_FLAG_LOGIN && code == Consts.REQ_CODE_UNNETWORK && isThirdPartLogin) {

					if (user_data instanceof Byte) {
						byte thirdLoginType = (Byte) user_data;
						switch (thirdLoginType) {

						case Consts.AF_LOGIN_FACEBOOK:
							new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.FB_NETWORK_UN);
//							MobclickAgent.onEvent(context, ReadyConfigXML.FB_NETWORK_UN);
							break;

						case Consts.AF_LOGIN_GOOGLE:
							new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.GG_NETWORK_UN);
//							MobclickAgent.onEvent(context, ReadyConfigXML.GG_NETWORK_UN);
							break;

						case Consts.AF_LOGIN_TWITTER:
							new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.TW_NETWORK_UN);
//							MobclickAgent.onEvent(context, ReadyConfigXML.TW_NETWORK_UN);
							break;

						default:
							break;
						}
					}

				}

				Consts.getInstance().showToast(this, code, flag, http_code);
			}
		}
	}
/*

	@Override
	public void reqCode(int code, int flag) {
		// TODO Auto-generated method stub
		switch (code) {
		case Consts.REQ_CODE_UNBIND_PHONE: {
			Intent intent = new Intent(this, RegistrationActivity.class);
			intent.putExtra(JsonConstant.KEY_AFID, getAfid());
			intent.putExtra(JsonConstant.KEY_PASS, getPassword());
			intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.BIND);
			startActivity(intent);
			break;
		}

		default:
			break;
		}
	}
*/

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	private void showProgDialog(int resId) {
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

	private void dismissDialog() {
		if (null != dialog && dialog.isShowing()) {
			try {
				dialog.cancel();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

}
