package com.afmobi.palmchat.ui.activity.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;

public class ForgotPasswordActivity extends BaseActivity implements OnClickListener{

	private Button vButtonNumberForgot,vButtonEmailForgot, vButtonCancel;
	private ImageView vImageViewBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_forgot_password);
		vButtonNumberForgot = (Button)findViewById(R.id.number_forgot);
		vButtonNumberForgot.setOnClickListener(this);
		vButtonEmailForgot = (Button)findViewById(R.id.email_forgot);
		vButtonEmailForgot.setOnClickListener(this);
		vImageViewBack = (ImageView)findViewById(R.id.back_button);
		vImageViewBack.setOnClickListener(this);
		
		
		vButtonCancel = (Button)findViewById(R.id.cancel_forgot);
		vButtonCancel.setOnClickListener(this);
		
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_layout:
		case R.id.back_button:
		case R.id.cancel_forgot:
			finish();
			break;
		case R.id.number_forgot:
			if (CommonUtils.isCanUseSim(ForgotPasswordActivity.this)) {
				Intent i = new Intent(ForgotPasswordActivity.this, RegistrationActivity.class);
				i.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.RESET);
				i.putExtra(RegistrationActivity.REGISTER_TYPE, RegistrationActivity.R_PHONE_NUMBER);
				startActivity(i);
				finish();
			}else{
				ToastManager.getInstance().show(ForgotPasswordActivity.this, R.string.insert_sim);
			}
			break;
		case R.id.email_forgot:
			Intent i = new Intent(ForgotPasswordActivity.this, EmailForgotPasswordActivity.class);
			startActivity(i);
//			finish();
			break;

		default:
			break;
		}
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
	

}
