package com.afmobi.palmchat.ui.activity.login;

import java.util.Arrays;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.OSInfo;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SignUpOrLoginActivity extends BaseForgroundLoginActivity  implements OnClickListener, AfHttpResultListener {
	
	private static final String TAG = SignUpOrLoginActivity.class.getCanonicalName();

	TextView mSignUp, mLogin;
	
	private boolean isFacebookLogin, isFacebookClick;
	private AfPalmchat mAfCorePalmchat;
	private Profile mProfile;
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
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}
	
	private String facebookId;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				//fackbook登陆成功
				case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
					//int socialNetworkID = msg.arg1;
					ToastManager.getInstance().show(SignUpOrLoginActivity.this, R.string.success);
					setProfile();
					break;
				}
				//fackbook登陆失败
				case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
					dismissAllDialog();
					ToastManager.getInstance().show(SignUpOrLoginActivity.this, R.string.failure);
					break;
				}
				
				case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
					ToastManager.getInstance().show(SignUpOrLoginActivity.this, R.string.facebook_login_cancel_tip);
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
					ToastManager.getInstance().show(SignUpOrLoginActivity.this,R.string.fetchprofile_failure);
					break;
				}
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		mCallbackManager = CallbackManager.Factory.create();
	    LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
	}

	@Override
	public void findViews() {

		setContentView(R.layout.sign_login);
		mSignUp = (TextView)findViewById(R.id.signup);
		mLogin = (TextView)findViewById(R.id.login);
		mSignUp.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		
		findViewById(R.id.layout_login_facebook).setOnClickListener(this);
		
	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signup:
			Intent intent =  new Intent(SignUpOrLoginActivity.this, RegistrationActivity.class);
			intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.REG);
			intent.putExtra(JsonConstant.KEY_FROM, SignUpOrLoginActivity.class.getName());
			startActivity(intent);
			finish();
			break;

		case R.id.login:
			startActivity(new Intent(SignUpOrLoginActivity.this, LoginActivity.class));
			finish();
			break;
			
		case R.id.layout_login_facebook:
		
			isFacebookClick = true;
			
			try {
				LoginManager.getInstance().logOut();
			} catch (Exception e) {
				// TODO: handle exception
			}
			//showProgressDialog(R.string.loading);
			new Thread(new Runnable() {  
				@Override  
				public void run() {  
					LoginManager.getInstance().logInWithReadPermissions(SignUpOrLoginActivity.this,
							Arrays.asList("user_location", "user_birthday", "email", "user_friends"));
				}  
			}).start();
			
			break;
			
		default:
			break;
		}
		
	}


	
	
	private void setProfile(){
		showProgressDialog(R.string.loading);
		OSInfo os = PalmchatApp.getApplication().osInfo;
		Profile profile = Profile.getCurrentProfile();
		mProfile = profile;
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
			isFacebookLogin = true;
			facebookId = profile.getId();
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
            mAfCorePalmchat.AfHttpRegLogin(param, facebookId, Consts.AF_LOGIN_FACEBOOK, Consts.AF_LOGIN_FACEBOOK, SignUpOrLoginActivity.this);
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mSocialNetworkManager.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		mSocialNetworkManager.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		mSocialNetworkManager.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
//		mSocialNetworkManager.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		
		case KeyEvent.KEYCODE_BACK: 
			
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // //FLAG_ACTIVITY_NEW_TASK
			startActivity(startMain);
			
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
/*	private void toMain() {
		dismissProgressDialog();
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
        intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend,true);
        intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, Consts.AF_LOGIN_FACEBOOK);
		startActivity(intent);
		finish();
	}*/
	
	private void toMainTab() {
		dismissProgressDialog();
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend,true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, Consts.AF_LOGIN_FACEBOOK);
		startActivity(intent);
		finish();
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		//dismissAllDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_FLAG_LOGIN:
				AfProfileInfo myProfile = (AfProfileInfo) result;
				if (myProfile != null) {
					CacheManager.getInstance().setMyProfile(myProfile);
					
					if (isFacebookLogin) {
						myProfile.password = Constants.FACEBOOK_PASSWORD;
						myProfile.third_account = facebookId;
						SharePreferenceUtils.getInstance(this).setIsFacebookLogin(true);
						CacheManager.getInstance().setMyProfile(myProfile);

						mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, myProfile);
						setLoadAccount(myProfile, Consts.AF_LOGIN_FACEBOOK , mAfCorePalmchat);//调中间件保存当前的登陆方式和信息
						
						if(!"1,".equals(myProfile.attr2) || (myProfile.city.equals("other") || myProfile.city.equals(DefaultValueConstant.OTHERS) ||  myProfile.country.equals(DefaultValueConstant.OTHERS) ||myProfile.country.equals(DefaultValueConstant.OTHERS)
								|| myProfile.region.equals(DefaultValueConstant.OTHERS) || myProfile.region.equals("other"))){// "1," was setted by server to distinguish whether third login user registered or not.
							Intent intent = new Intent(this, FacebookCompleteProfileActivity.class);
							intent.putExtra("FacebookId", facebookId);
							intent.putExtra("afid", myProfile.afId);
							intent.putExtra("SocialPerson", mProfile);

							if (user_data instanceof Byte) {
								byte thirdLoginType = (Byte)user_data;
								intent.putExtra("ThirdLoginType", thirdLoginType);
							}
							dismissAllDialog();
							startActivity(intent);
						}else{
							toMainTab();
						}
					}
				}
				break;

			default:
				dismissAllDialog();
				break;
			}
		} else {
			dismissAllDialog();
			if (flag == Consts.REQ_FLAG_LOGIN && code == -160) {
				if (isFacebookLogin && null != facebookId) {
					
					Intent intent = new Intent(this, FacebookCompleteProfileActivity.class);
					intent.putExtra("FacebookId", facebookId);
					intent.putExtra("SocialPerson", mProfile);
					startActivity(intent);
										
				} else {
					
					Consts.getInstance().showToast(this, code, flag,http_code);
				}
			} else {
				Consts.getInstance().showToast(this, code, flag,http_code);
			}
		} 
	}

}
