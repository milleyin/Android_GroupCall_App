package com.afmobi.palmchat.ui.activity.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

public class UpdateStatusActivity extends BaseActivity implements AfHttpResultListener {

	public AfProfileInfo afProfileInfo;
	private AfPalmchat mAfPalmchat;
	
	private EditText editContent;
	private Button buttonConfim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		return false;
	}

	@Override
	public void findViews() {
		setContentView(R.layout.activity_update_status);
		((TextView)(findViewById(R.id.title_text))).setText(R.string.whatsup);
		
		editContent = (EditText) findViewById(R.id.content_edit);
		editContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});
		buttonConfim = (Button) findViewById(R.id.ok_button);
		
		buttonConfim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String text = editContent.getText().toString();
				if (mAfPalmchat != null && afProfileInfo != null) {
					afProfileInfo.signature = text;
					showProgressDialog(R.string.loading);
					afProfileInfo.birth = afProfileInfo.getUploadBirth(UpdateStatusActivity.this);
					mAfPalmchat.AfHttpUpdateInfo(afProfileInfo, null, UpdateStatusActivity.this);
				}
			}
		});
		
		findViewById(R.id.back_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void init() {
		mAfPalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
		afProfileInfo = (AfProfileInfo) getIntent().getSerializableExtra(JsonConstant.KEY_PROFILE);
		if (editContent != null && afProfileInfo != null) {
			editContent.setText(afProfileInfo.signature);
			editContent.setSelection(editContent.getText().length());
		}
	}
	
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		dismissProgressDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_UPDATE_INFO) {
				//update profile
				if (result != null && result instanceof AfProfileInfo) {
					Intent intent = new Intent(this, MyProfileDetailActivity.class);
					intent.putExtra(JsonConstant.KEY_STATUS, ((AfProfileInfo)result).signature);
					setResult(1, intent);
					finish();
				}
			} 
		}else {
			if (flag == Consts.REQ_UPDATE_INFO) {//zhh上传其余资料（除头像、恋爱状态外）
				if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
					Consts.getInstance().showToast(this, code, flag, http_code);
				}
			}
		}
	}

}
