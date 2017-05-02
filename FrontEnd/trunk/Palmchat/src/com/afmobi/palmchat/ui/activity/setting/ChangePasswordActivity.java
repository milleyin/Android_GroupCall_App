package com.afmobi.palmchat.ui.activity.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.listener.OnItemClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.register.EmailForgotPasswordActivity;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.ForgetPasswordDialog;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
/**
 * 修改密码界面
 *
 */
public class ChangePasswordActivity extends BaseActivity  implements AfHttpResultListener,OnClickListener,OnItemClick,OnEditorActionListener{
	/**旧密码输入栏*/
    private EditText mEdit_OldPwd;
    /**新密码输入栏*/
    private EditText mEdit_NewPwd;
    /**返回按钮*/
    private ImageView mBtnBack;
    /***/
    private ScrollView mScrollView;
    /***/
    public int mHandler;
    /**本地接口类*/
    private AfPalmchat mAfCorePalmchat;
    /**忘记密码*/
    private View mView_ForgetPwd;
    /**忘记密码对话框*/
    private ForgetPasswordDialog mDig_Forget;
    /**忘记模式*/
    private int mForgetMode;
    /**键盘*/
    private KeyboardListenRelativeLayout mKeyboardListenRelativeLayout;
	private Button mSaveButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub		
		setContentView(R.layout.activity_change_password);
		
		mBtnBack = (ImageView) this.findViewById(R.id.back_button);
		mKeyboardListenRelativeLayout = (KeyboardListenRelativeLayout) this.findViewById(R.id.keyboardRelativeLayout);
		mScrollView = (ScrollView) this.findViewById(R.id.sv);
		mBtnBack.setOnClickListener(this);
		
		mEdit_OldPwd=(EditText)findViewById(R.id.old_password);
		mEdit_NewPwd=(EditText)findViewById(R.id.new_password);
		
		mEdit_OldPwd.setOnEditorActionListener(this);
		mEdit_NewPwd.setOnEditorActionListener(this);
		
		mView_ForgetPwd = findViewById(R.id.forget_pwd);
		mView_ForgetPwd.setOnClickListener(this);
		mEdit_NewPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {  
		            @Override  
		            public void run() {  
		                //将ScrollView滚动到底  
		                mScrollView.fullScroll(View.FOCUS_DOWN);  
		                mEdit_NewPwd.setFocusable(true);   
		                mEdit_NewPwd.setFocusableInTouchMode(true);   
		                mEdit_NewPwd.requestFocus();  
		                
		                mEdit_NewPwd.setSelection(mEdit_NewPwd.length());
		            }  
		        }, 200);  
			}
		});
		
		TextView titleText=(TextView)findViewById(R.id.title_text);
		titleText.setText(R.string.password_label);
	/*	mRelLayout_ConfirmButton=(RelativeLayout)findViewById(R.id.ok_button);
		mRelLayout_ConfirmButton.setOnClickListener(this);		*/
		mSaveButton = (Button) findViewById(R.id.btn_ok);
		mSaveButton.setText(R.string.save);
		mSaveButton.setVisibility(View.VISIBLE);
		mSaveButton.setOnClickListener(this);
/*		mKeyboardListenRelativeLayout.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {
              
	            public void onKeyboardStateChanged(int state) {  
	                switch (state) {  
	                case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://keyboard hide
						mSaveButton.setVisibility(View.VISIBLE);
	                    break;  
	                case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://keyboard show
						mSaveButton.setVisibility(View.INVISIBLE);
	                    break;  
	                default:  
	                    break;  
	                }  
	            }
	        });*/
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onResume() {
			// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void setLoginInfo(AfProfileInfo info) {
		// TODO Auto-generated method stub
		AfLoginInfo login = CacheManager.getInstance().getLoginInfo();//new AfLoginInfo();
		login.afid = info.afId;
		login.password = info.password;
		login.cc= PalmchatApp.getOsInfo().getCountryCode();
		login.email = info.email;
		login.fdsn = info.fdsn;
		login.blsn = info.blsn;
		login.gpsn = info.gpsn;
		login.phone = info.phone;	
		login.pbsn = info.pbsn;
		PalmchatLogUtils.println("CompleteProfileActivity  setLoginInfo");
		mAfCorePalmchat.AfSetLoginInfo(login);
//		PalmchatApp.getApplication().savePalmAccount(login, true);
	}

	@Override

	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		// TODO Auto-generated method stub
		System.out.println("ywp: AfOnResult:  code  = "  +  code);
		dismissAllDialog();
		if(code == Consts.REQ_CODE_SUCCESS){
			ToastManager.getInstance().show(this, R.string.succeeded);
			switch (flag) {
				case Consts.REQ_CHANGE_PASSWORD:{
					String res = (String)result;
					System.out.println("ywp: AfOnResult: REQ_CHANGE_PASSWORD afid  = "  +  res);
					//
					AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
					myAfProfileInfo.password = getEditTextNewPwd();
//					CacheManager.getInstance().setMyProfile(myAfProfileInfo);
					setLoginInfo(myAfProfileInfo);
					app.mAfCorePalmchat.AfDbLoginUpdatePassword(myAfProfileInfo.afId, myAfProfileInfo.password);
				
					finish();
				break;
				}
			}
		}else {
			if(Consts.REQ_CODE_ACCOUNT_NOEXIST == code){
				ToastManager.getInstance().show(context, R.string.fail_password_attempt);
			}else{
				Consts.getInstance().showToast(context, code, flag,http_code);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		//忘记密码
		//现在没啥用，屏蔽了
		case R.id.forget_pwd:
			AfProfileInfo info = CacheManager.getInstance().getMyProfile();
			boolean isSet = new ReadyConfigXML().getBoolean(ReadyConfigXML.KEY_ALREAD_SET + info.afId);
			if ((isSet || info.is_bind_phone || info.is_bind_email)) {
				showForgetPasswordDialog(mForgetMode);
			}else {
				showPleaseVerifyPhone_or_Email();
			}
			break;
		case R.id.btn_ok:
			String oldpwd = getEditTextOldPwd();
			String newpwd =getEditTextNewPwd();
			//String re_newpwd=getEditTextReNewPwd();
			if (TextUtils.isEmpty(oldpwd)) {
				ToastManager.getInstance().show(this, getString(R.string.enter_old_password));
				return;
			}
			if (TextUtils.isEmpty(newpwd)) {
				ToastManager.getInstance().show(this, getString(R.string.enter_new_password));
				return;
			}
			if (newpwd.length() < 6 || newpwd.length() > 16) {
				ToastManager.getInstance().show(this, getString(R.string.prompt_input_password_little6));
				return;
			}
			
			if (!TextUtils.isEmpty(oldpwd) && !TextUtils.isEmpty(newpwd)/*&&!TextUtils.isEmpty(re_newpwd)*/) {
				if (oldpwd.equals(newpwd)) {
					ToastManager.getInstance().show(this, getString(R.string.new_psw_old_pwd_can_not_same));
					return;
				}else {
					if (NetworkUtils.isNetworkAvailable(context)) {
						mHandler= mAfCorePalmchat.AfHttpChangPwd(oldpwd, newpwd, null, 0, this);		
						this.showProgressDialog(getString(R.string.loading));
					}else {
						ToastManager.getInstance().show(this, getString(R.string.network_unavailable));
					}
				}
			}
						
	       break;
		case R.id.back_button:
			finish();
			break;
		default:
			break;
			
		
		}
	}
	
	/**
	 * 
	 */
	private void showPleaseVerifyPhone_or_Email() {
		AppDialog appDialog = new AppDialog(this);
		appDialog.createConfirmDialog(context, "", getString(R.string.please_verify_phone_or_email), getString(R.string.cancel), getString(R.string.ok), new OnConfirmButtonDialogListener() {

			@Override
			public void onLeftButtonClick() {
			}

			@Override
			public void onRightButtonClick() {
				startActivity(new Intent(ChangePasswordActivity.this, MyAccountActivity.class));
			}

		});
		appDialog.show();
	}
	
	/**
	 * 显示忘记密码对话框
	 * @param forgetMode 模式
	 */
	private void showForgetPasswordDialog(int forgetMode) {
		mDig_Forget = null;
		mDig_Forget = new ForgetPasswordDialog(this,forgetMode); 
		mDig_Forget.setItemClick(this);
		mDig_Forget.show();
	}

    /**
     * 	 获取旧密码
     * @return 旧密码
     */
	private String getEditTextOldPwd(){
		return mEdit_OldPwd.getText().toString().trim();
	}

    /**
     * 	 获取新密码
     * @return 新密码
     */
	private String getEditTextNewPwd(){
		return mEdit_NewPwd.getText().toString().trim();
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}

	@Override
	public void onItemClick(int item) {
		// TODO Auto-generated method stub
		switch (item) {
		//
		case ForgetPasswordDialog.FORGET_MODE_PHONE_NUMBER:
			if (CommonUtils.isCanUseSim(this)) {
				Intent i = new Intent(this, RegistrationActivity.class);
				i.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.RESET);
				i.putExtra(RegistrationActivity.REGISTER_TYPE, RegistrationActivity.R_PHONE_NUMBER);
				i.putExtra(JsonConstant.KEY_IS_LOGIN, true);
				i.putExtra(JsonConstant.KEY_FROM, ChangePasswordActivity.class.getCanonicalName());
				startActivity(i);
			}else{
				ToastManager.getInstance().show(this, R.string.insert_sim);
			}
			break;
		//
		case ForgetPasswordDialog.FORGET_MODE_EMAIL:
			Intent i = new Intent(this, EmailForgotPasswordActivity.class);
			startActivity(i);
			break;
		//
		case ForgetPasswordDialog.FORGET_MODE_SECURITY:
			Intent in = new Intent(this, SecurityAnswerActivity.class);
			in.putExtra(JsonConstant.KEY_IS_LOGIN, true);
			startActivity(in);
			break;
		//
	 	default:

			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			return true;
		}
		return false;

	}

}
