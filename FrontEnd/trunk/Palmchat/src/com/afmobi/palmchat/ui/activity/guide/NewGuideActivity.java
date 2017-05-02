package com.afmobi.palmchat.ui.activity.guide;

import java.util.ArrayList;
import java.util.Arrays;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.OSInfo;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.login.FacebookCompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.util.CommonUtils;
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
//import com.umeng.analytics.MobclickAgent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NewGuideActivity extends BaseForgroundLoginActivity implements OnClickListener,
		DialogInterface.OnClickListener, AfHttpResultListener {
	
	private static final String TAG = NewGuideActivity.class.getCanonicalName();

	public ViewGroup createView(int res) {
		ViewGroup viewGroup = new LinearLayout(this);
		viewGroup.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,CommonUtils.dip2px(this, 455)));
		viewGroup.setBackgroundResource(res);
		return viewGroup;
	}
	
	private ViewPager viewPager;
	private ImageView imageView;
	private ArrayList<View> pageViews;
	private ImageView[] imageViews;
	private ViewGroup viewPictures;
	private ViewGroup viewPoints;
	private TextView mSignUp, mLogin;

	public AfPalmchat mAfCorePalmchat;
	
 
	
	private String facebookId;
	
	/**个人Profile*/
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
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onSuccess----"+loginResult.toString());
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);
        }

        @Override
        public void onCancel() {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onError----"+exception.toString());
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
				//facebook登陆成功
			    case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
			    	ToastManager.getInstance().show(NewGuideActivity.this, R.string.success);
			    	setProfile();
			    	break;
			    }
			    //facebook登陆失败
			    case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
					ToastManager.getInstance().show(NewGuideActivity.this, R.string.failure);
					dismissAllDialog();
			    	break;
			    }
			    
				case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
					ToastManager.getInstance().show(NewGuideActivity.this, R.string.facebook_login_cancel_tip);
					break;
				}
			  //获取个人资料成功
				case DefaultValueConstant.FETCHPROFILE_SUCCESS:{					
					setProfile();
					dismissAllDialog();
					break;
				}
				//获取个人资料失败
				case DefaultValueConstant.FETCHPROFILE_FAILURE:{
					dismissAllDialog();
					ToastManager.getInstance().show(NewGuideActivity.this,R.string.fetchprofile_failure);
					break;
				}
				
			    default: {
			    	String imei = PalmchatApp.getOsInfo().getImei();
					if(TextUtils.isEmpty(imei)){
						Intent intent =  new Intent(NewGuideActivity.this, RegistrationActivity.class);
						intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.REG);
						intent.putExtra(RegistrationActivity.REGISTER_TYPE, RegistrationActivity.EMAIL);
						intent.putExtra(JsonConstant.KEY_FROM, NewGuideActivity.class.getName());
						startActivity(intent);
						finish();
						return;
					}
					PalmchatApp.getApplication().AfHttpLogin(imei,PalmchatApp.getOsInfo().getMcc(),imei, null, PalmchatApp.getOsInfo().getCountryCode(), Consts.AF_LOGIN_IMEI, 0, NewGuideActivity.this);
					break;
			    }
			    	
			}
			
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
		
	}

	@Override
	public void findViews() {
		
	}

	@Override
	public void init() {
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.new_guide_01, null));
		pageViews.add(inflater.inflate(R.layout.new_guide_02, null));
		pageViews.add(inflater.inflate(R.layout.new_guide_03, null));
		imageViews = new ImageView[pageViews.size()];
		viewPictures = (ViewGroup) inflater.inflate(R.layout.activity_new_guide, null);

		viewPager = (ViewPager) viewPictures.findViewById(R.id.viewpager);
		viewPoints = (ViewGroup) viewPictures.findViewById(R.id.viewPoints);

		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(NewGuideActivity.this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
			lp.setMargins(0, 0, 0, 0);
//			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setLayoutParams(lp);
			imageView.setPadding(5, 0, 5, 0);
			imageViews[i] = imageView;
			if (i == 0)
				imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.page_guidepage_sel));
			else
				imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.page_guidepage_nosel));
			viewPoints.addView(imageViews[i]);
			
		}

		setContentView(viewPictures);
		viewPager.setAdapter(new NavigationPageAdapter());
		viewPager.setOnPageChangeListener(new NavigationPageChangeListener());
		mSignUp = (TextView)findViewById(R.id.signup);
		mLogin = (TextView)findViewById(R.id.login);
		mSignUp.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		
		findViewById(R.id.layout_login_facebook).setOnClickListener(this);
 
//		launcher();
	}
	
	class NavigationPageAdapter extends PagerAdapter {

		
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(pageViews.get(position));
			return pageViews.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(pageViews.get(position));
		}
		
	}

	int onPageScrollStateChangedInt;
	int times = 0;
	boolean flag = false;
	class NavigationPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {//(1 (0 0.0 0) 0)(1 (0.31111112 168) 2 (1 0.0 0) 0) (1 (1 0.7425926 401) 2 (2 0.0 0) 0)  (1 (2 0.0 0) 0)
			onPageScrollStateChangedInt = arg0;
			PalmchatLogUtils.e("onPageScrollStateChanged", "arg0  "+arg0);
			switch (arg0) {
		    case ViewPager.SCROLL_STATE_DRAGGING:
		     flag= false;
		     break;
		    case ViewPager.SCROLL_STATE_SETTLING:
		     flag = true;
		     break;
		    case ViewPager.SCROLL_STATE_IDLE:
		     if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !flag) {
		    	 SharePreferenceUtils.getInstance(NewGuideActivity.this).setIsFirstTimeEnter(false);
		    	 startActivity(new Intent(NewGuideActivity.this, NewGuideNextActivity.class));
		    	 finish();
		     }
		     flag = true;
		     break;
		    }

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			PalmchatLogUtils.e("onPageScrolled", "arg0  "+arg0+"  arg1  "+arg1+"  arg2  "+arg2);
			if(onPageScrollStateChangedInt == 1 && arg0 == 3){
				times ++;
				if(times >= 2){
//					toLogin("");
//					startActivity(new Intent(NewGuideActivity.this, LoginActivity.class));
				}
			}else{
				times = 0;
			}
		}

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.page_guidepage_sel));
				if (position != i)
					imageViews[i].setImageDrawable(getResources().getDrawable(R.drawable.page_guidepage_nosel));
			}
		}
	}
	

//    }

    @Override
    public void onClick(DialogInterface dialogInterface, int arg1)
    {

        if(arg1 == DialogInterface.BUTTON_POSITIVE){
//            addShortCut();
        }
        else if(arg1 == DialogInterface.BUTTON_NEGATIVE){

        }
    }

//    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();

    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		// heguiming 2013-12-04
		new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_GUIDE);
	}
    
    @Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("GuideActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//	    MobclickAgent.onPause(this);
	    AppEventsLogger.deactivateApp(this);
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
	/*
	private void launcher() {
		new Thread(runnable).start();
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.signup:
			SharePreferenceUtils.getInstance(this).setIsFirstTimeEnter(false);
			Intent intent =  new Intent(this, RegistrationActivity.class);
			intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.REG);
			intent.putExtra(JsonConstant.KEY_FROM, NewGuideActivity.class.getName());
			startActivity(intent);
			finish(); 
			
//			showProgressDialog(R.string.please_wait);
//			CommonUtils.getMyCountryInfo(context,mHandler,0);
			
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_REG_I);
//			MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_REG_I);
			
	 		new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_PNUM);
//	 		 MobclickAgent.onEvent(context, ReadyConfigXML.REG_PNUM);
			break;

		case R.id.login:
			SharePreferenceUtils.getInstance(this).setIsFirstTimeEnter(false);
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
			
		case R.id.layout_login_facebook:
			
			if (hasThirdLoginClient(Consts.AF_LOGIN_FACEBOOK)) {
				new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_FB);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_FB);
			} else {
				new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_WFB);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_WFB);
			}
			
			SharePreferenceUtils.getInstance(this).setIsFirstTimeEnter(false);
			
		 
			
			try {
				LoginManager.getInstance().logOut();
			} catch (Exception e) {
				// TODO: handle exception
			}
			//showProgressDialog(R.string.loading);
			new Thread(new Runnable() {  
				@Override  
				public void run() {  
					LoginManager.getInstance().logInWithReadPermissions(NewGuideActivity.this,
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
			mProfile = profile;
			String countryCode = PalmchatApp.getApplication().osInfo.getCountryCode();
			 
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
            mAfCorePalmchat.AfHttpRegLogin(param, facebookId, Consts.AF_LOGIN_FACEBOOK, Consts.AF_LOGIN_FACEBOOK, NewGuideActivity.this);
		}
	}


	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		PalmchatLogUtils.e(TAG, "----AfOnResult--Flag:" + flag + "---Code:" + code);
		//dismissAllDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_FLAG_LOGIN:
				AfProfileInfo myProfile = (AfProfileInfo) result;
				if (myProfile != null) {
					myProfile.password = Constants.FACEBOOK_PASSWORD;
					CacheManager.getInstance().setMyProfile(myProfile);
					CacheManager.getInstance().setPalmGuessShow(myProfile.palmguess_flag==1);
					/*boolean isShowPalmGuessNew=false;
					int ver = SharePreferenceUtils.getInstance(this).getPalmguessVersion(myProfile.afId);
					if (ver < myProfile.palmguess_version) {
						SharePreferenceUtils.getInstance(this).setPalmguessVersion(myProfile.afId, myProfile.palmguess_version);
						SharePreferenceUtils.getInstance(this).setUnReadNewPalmGuess(true);
						isShowPalmGuessNew=true;
					} */
						myProfile.password = Constants.FACEBOOK_PASSWORD;
						myProfile.third_account = facebookId;
						SharePreferenceUtils.getInstance(this).setIsFacebookLogin(true);
						CacheManager.getInstance().setMyProfile(myProfile);

						mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, myProfile);
						setLoadAccount(myProfile, Consts.AF_LOGIN_FACEBOOK, mAfCorePalmchat);//调中间件保存当前的登陆方式和信息
						if(!"1,".equals(myProfile.attr2) || (myProfile.city.equals("other") || myProfile.city.equals(DefaultValueConstant.OTHERS) ||  myProfile.country.equals(DefaultValueConstant.OTHERS) ||myProfile.country.equals(DefaultValueConstant.OVERSEA)
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
							toMainTab(Consts.AF_LOGIN_FACEBOOK);
						}
					 
				}
				break;

			default:
				break;
			}
		} else {
			 dismissAllDialog(); 
			 Consts.getInstance().showToast(this, code, flag,http_code);
			 
		}
	}

 
	
	private void toMainTab(byte login_type) {
		dismissProgressDialog();
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend,true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, login_type);
		startActivity(intent);
		finish();
	} 
	
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
	        
	        PalmchatLogUtils.println("--xxxx hasThirdLoginClient: type = " + type  + " value = " + value);
	        return value;
	    }
	
	
}
