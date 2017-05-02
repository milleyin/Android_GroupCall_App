package com.afmobi.palmchat.ui.activity.setting;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.broadcasts.SMSBroadcastReceiver;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NewBindPhoneActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {

	private EditText edit_sms_code;
	private Button next_button;
	private String sms_code;
	
	private TextView title_text;
	private String new_phone_number,country_code;
	private AfPalmchat mAfCorePalmchat;
	
	private SMSBroadcastReceiver mSMSBroadcastReceiver;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	
	 
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   
	} 
	
	 
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy(); 
		if (mSMSBroadcastReceiver != null) {
			this.unregisterReceiver(mSMSBroadcastReceiver);
		}
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_new_bind_phone);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		new_phone_number = getIntent().getStringExtra(JsonConstant.KEY_ACCOUNT);
		country_code = getIntent().getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
	 
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(R.string.phone);
		
		edit_sms_code = (EditText) findViewById(R.id.edit_sms_code);
		
		next_button = (Button) findViewById(R.id.next_button);
		next_button.setOnClickListener(this);
		next_button.setClickable(false);
		ImageView back_button = (ImageView) findViewById(R.id.back_button);
		back_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		edit_sms_code.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString().trim();
				if (str.length() > 0) {
					isClickBtnContinue(true);
				}else {
					isClickBtnContinue(false);
				}
			}
		});
		registerMsms_broadcast();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next_button:
			sms_code = edit_sms_code.getText().toString().trim();
			if(TextUtils.isEmpty(sms_code)){
				ToastManager.getInstance().show(this,R.string.verification_code_not_empty);
				return;
			}
			showProgressDialog(R.string.please_wait);
			toUpdateBindPhone();
			
			break;

		default:
			break;
		}
	}

	private void toUpdateBindPhone() {
		// TODO Auto-generated method stub
		PalmchatApp.getApplication().mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_BIND_BY_PHONE_EX, new_phone_number, 
				country_code, CacheManager.getInstance().getMyProfile().afId ,
				null,edit_sms_code.getText().toString().trim(), 0, NewBindPhoneActivity.this);
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		// TODO Auto-generated method stub
		if(code == Consts.REQ_CODE_SUCCESS){
			switch (flag) {
			case Consts.REQ_BIND_BY_PHONE_EX:
			{ 
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_BP_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.SET_BP_SUCC); 
				
				AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
				profile.is_bind_phone = true;
				profile.phone = new_phone_number;	
				profile.phone_cc =country_code;
//				CacheManager.getInstance().setMyProfile(profile);
				updateProfileToDB();
				loadAccount();
				toMyAccountActivity();
				 
				break;
			}
			 
			}
		} else {
			dismissProgressDialog();
			Consts.getInstance().showToast(context, code, flag,http_code);
		}
		 
	}
	
	
	private void toMyAccountActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ChangePhoneActivity.class);
		setResult(8001, intent);
		MyActivityManager.getScreenManager().popActivity();
	}

	/**
	 * 把新的手机号保存到数据库中
	 */
	private void loadAccount() {
		// TODO Auto-generated method stub
		AfLoginInfo login = CacheManager.getInstance().getLoginInfo();
		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		if(!TextUtils.isEmpty( myProfile.afId)&&!TextUtils.isEmpty(myProfile.password )){
			login.phone=myProfile.phone;
			login.cc=myProfile.phone_cc; 
			login.password = myProfile.password; 
			login.mcc=PalmchatApp.getOsInfo().getAfid_mcc();
			mAfCorePalmchat.AfLoadAccount(login);
		}
		 
	} 

	private void updateProfileToDB() {
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		if(!CommonUtils.isEmpty(myAfProfileInfo.afId)){
			new Thread(new Runnable() {
				public void run() {
					int msg_id = PalmchatApp.getApplication().mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, CacheManager.getInstance().getMyProfile());
					PalmchatLogUtils.println("NewBindPhone PalmchatApp  msg_id  "+msg_id);
				}
			}).start();
		}
	}
	
	public void registerMsms_broadcast(){
		// 生成广播处理
		mSMSBroadcastReceiver = new SMSBroadcastReceiver();
		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(ACTION);
		// intentFilter.setPriority(Integer.MAX_VALUE);
		// 注册广播
		this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

		mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
			@Override
			public void onReceived(String message) {
				edit_sms_code.setText(message);
				sms_code = message;
				edit_sms_code.setSelection(message.length());
			}
		});
	}

	/**
	 * 是否可点击继续按钮
	 *
	 * @param bool
	 */
	private void isClickBtnContinue(boolean bool) {
		if (bool) {
			next_button.setBackgroundResource(R.drawable.login_button_selector);
			next_button.setTextColor(Color.WHITE);
			next_button.setClickable(true);
		} else {
			next_button.setBackgroundResource(R.drawable.btn_blue_d);
			next_button.setTextColor(getResources().getColor(R.color.guide_text_color));
			next_button.setClickable(false);
		}
	}
}
