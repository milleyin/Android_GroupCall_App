package com.afmobi.palmchat.ui.activity.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

public class EmailForgotPasswordActivity extends BaseActivity implements OnClickListener,AfHttpResultListener{

	private ImageView vImageViewBack;
	private EditText vEditTextEmail;
	private AfPalmchat mAfCorePalmchat;
	//确认，下一步按钮
	private Button mResetPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		
		// heguiming 2013-12-04
		new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_E_F);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_E_F);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart("EmailForgotPasswordActivity");
//	    MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("EmailForgotPasswordActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
//	    MobclickAgent.onPause(this);
	}
	
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_email_forgot_password);
		vEditTextEmail = (EditText) findViewById(R.id.edit_email);
		TextView title = (TextView)findViewById(R.id.title_text);
		title.setText(R.string.reg_email);
		mResetPassword = (Button) findViewById(R.id.next_button);
		mResetPassword.setText(R.string.send);
		mResetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//请求验证email
				String email = vEditTextEmail.getText().toString().trim();
				if (email.length() <= DefaultValueConstant.LENGTH_0) {
					/*ToastManager.getInstance().show(context, R.string.prompt_input_email);*/
				} else if(!CommonUtils.isEmail(email)){
					ToastManager.getInstance().show(context, R.string.prompt_email_address_not_legal);
				} else {
					showProgressDialog(R.string.verifyloading);
					resetEmail();
//					JSONObject request = CommonJson.getInstance().makeResetEmailJson(email);
//					if (request != null) {
//						singleTask = new BaseSingleAsyncTask(EmailForgotActivity.this);
//						singleTask.setResultListener(EmailForgotActivity.this);
//						singleTask.setFlag(RequestType.RESET_PASS_EMAIL);
//						singleTask.execute(RequestConstant.RequestType.RESET_PASS_EMAILT_URL(), request);
//						EmailForgotActivity.this.showProgressDialog(getString(R.string.verifyloading));
//					} else {
//						dispatchServer();
//					}
				}
				
			}

		});
		vEditTextEmail.addTextChangedListener(new TextWatcher() {
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
		vImageViewBack = (ImageView)findViewById(R.id.back_button);
		vImageViewBack.setOnClickListener(this);
	}

	
	private String getEmailText(){
		return vEditTextEmail.getText().toString().trim();
	}
	
	private int resetEmail() {
		// TODO Auto-generated method stub
		return mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_RESET_PWD_BY_EMAIL, getEmailText(), null, null, null, null,0, this);
	}
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_button:
			finish();
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
	


	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		// TODO Auto-generated method stub
		if(code == Consts.REQ_CODE_SUCCESS){
			switch (flag) {
				case Consts.REQ_RESET_PWD_BY_EMAIL:
				{
					// heguiming 2013-12-04
					new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.RESET_P_SUCC);
					new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.SM_NUM);
					
//					MobclickAgent.onEvent(context, ReadyConfigXML.RESET_P_SUCC);
//					MobclickAgent.onEvent(context, ReadyConfigXML.SM_NUM);
					
					String info = (String)result;
					System.out.println("ywp: AfOnResult: REQ_RESET_PWD_BY_PHONE or REQ_RESET_PWD_BY_EMAIL info  = "  +  info);	
					dismissProgressDialog();
					/*AlertDialog.Builder dialog = new AlertDialog.Builder(this);
					dialog.setTitle(R.string.password_reset);
					dialog.setMessage(R.string.password_reset_email_success2);
					dialog.setNegativeButton(R.string.ok, this);
					dialog.show();*/

					AppDialog emailForgotPasswordAppDialog = new AppDialog(context);
					String msg = context.getResources().getString(R.string.password_reset_email_success);
					emailForgotPasswordAppDialog.createConfirmDialog(context, msg, new AppDialog.OnConfirmButtonDialogListener() {
						@Override
						public void onLeftButtonClick() {//取消
							finish();
						}

						@Override
						public void onRightButtonClick() {//确认
							finish();
						}
					});
					emailForgotPasswordAppDialog.show();
					break;
				}
				default:
					break;
			}
		}else{
			dismissProgressDialog();
			Consts.getInstance().showToast(context, code, flag,http_code);
		}
		
	}


	/*@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		if( !isFinishing() ){
			finish();
		}
	}*/

	/**
	 * 是否可点击继续按钮
	 *
	 * @param bool
	 */
	private void isClickBtnContinue(boolean bool) {
		if (bool) {
			mResetPassword.setBackgroundResource(R.drawable.login_button_selector);
			mResetPassword.setTextColor(Color.WHITE);
			mResetPassword.setClickable(true);
		} else {
			mResetPassword.setBackgroundResource(R.drawable.btn_blue_d);
			mResetPassword.setTextColor(getResources().getColor(R.color.guide_text_color));
			mResetPassword.setClickable(false);
		}
	}

}
