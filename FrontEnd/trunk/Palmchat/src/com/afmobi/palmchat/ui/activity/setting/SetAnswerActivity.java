package com.afmobi.palmchat.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.ui.activity.register.ResetActivity;
import com.afmobi.palmchat.util.StringUtil;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * SetAnswer
 * @author heguiming
 *
 */
public class SetAnswerActivity extends BaseForgroundLoginActivity implements
		OnClickListener, AfHttpResultListener {
	
	private final String TAG = SetAnswerActivity.class.getCanonicalName();
	
	private Button mNextBtn;
	private LinearLayout mQuestionLayout;

	protected AfPalmchat mAfCorePalmchat;
	private List<EditText> itemEditList = new ArrayList<EditText>();
	private String[] requestAry;
	private String afid;
	
	@Override
	public void findViews() {
		setContentView(R.layout.activity_set_answer);
		
		TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.secure_answer);
		findViewById(R.id.back_button).setOnClickListener(this);
		
		mQuestionLayout = (LinearLayout) findViewById(R.id.question_layout);
		
		mNextBtn = (Button) findViewById(R.id.next_btn);
		mNextBtn.setOnClickListener(this);
		mNextBtn.setClickable(false);
		
	}

	@Override
	public void init() {
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			afid = bundle.getString(SecurityAnswerActivity.KEY_AFID);
			requestAry = (String[]) bundle.get(SecurityAnswerActivity.KEY_QUESTION_ARY);
			
			if (null != requestAry) {
				int size = requestAry.length;
				for (int i = 0; i < size; i++) {
					String str = requestAry[i];
					if (!StringUtil.isNullOrEmpty(str)) {
						View view = LayoutInflater.from(this).inflate(R.layout.layout_answer, null);
						TextView numberTxt = (TextView) view.findViewById(R.id.number_txt);
						TextView itemTxt = (TextView) view.findViewById(R.id.item_txt);
						final EditText itemEdit = (EditText) view.findViewById(R.id.item_edit);
						itemEditList.add(itemEdit);

						numberTxt.setText((i + 1) + ".");
						itemTxt.setText(str);
						mQuestionLayout.addView(view);
						itemEdit.addTextChangedListener(new TextWatcher() {
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {

							}

							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								if(getedittext() > 0 || s.length() > 0){
									isClickBtnContinue(true);
								}else{
									isClickBtnContinue(false);
								}
							}

							@Override
							public void afterTextChanged(Editable s) {

							}
						});
					}
				}
			}
			
		}
	}

	/**
	 *改变button的颜色值
	 * @param bool
	 */
	private void isClickBtnContinue(boolean bool){
		if (bool) {
			mNextBtn.setBackgroundResource(R.drawable.login_button_selector);
			mNextBtn.setClickable(true);//恢复按钮的点击
		} else {
			mNextBtn.setBackgroundResource(R.drawable.btn_blue_d);
			mNextBtn.setClickable(false);//设置按钮不能点击
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.next_btn:
			setAnswer();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取3个edittext里面是否都有内容
	 * @return 3个控件里面有多少个是有内容的
     */
	private int getedittext(){
		int index = 0;
		for(int i = 0;i<itemEditList.size();i++){
			EditText item = itemEditList.get(i);
			String str = item.getText().toString();
			if(!StringUtil.isNullOrEmpty(str)){//有内容的话就会+1
				index++;
			}
		}
		return index;
	}


	private void setAnswer() {
		if (!itemEditList.isEmpty()) {
			int size = itemEditList.size();
			String[] answerAry = new String[3];
			for (int i = 0; i < size; i++) {
				EditText item = itemEditList.get(i);
				String str = item.getText().toString();
				if (StringUtil.isNullOrEmpty(str)) {
					showToast(R.string.please_set_answer);
					return;
				}
				answerAry[i] = str;
			}
			if (answerAry.length == 1) {
				answerAry[1] = "";
				answerAry[2] = "";
			} else if (answerAry.length == 2) {
				answerAry[2] = "";
			}
			
			showProgressDialog(R.string.loading);
			mAfCorePalmchat.AfHttpFindPwdAnswer(Consts.REQ_PSD_ANSWER, afid, Consts.AF_LOGIN_AFID, requestAry, answerAry, null, this);
		}
	}
	
	private String psw;

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.e(TAG, "----Flag:" + flag + "----Code:" + code);
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_PSD_ANSWER) {
				if (null != result) {
					psw = (String) result;
					mAfCorePalmchat.AfHttpLogin(afid, psw, PalmchatApp.getOsInfo().getCountryCode(), Consts.AF_LOGIN_AFID, 0, this);
				} else {
					dismissProgressDialog();
				}
			} else if (flag == Consts.REQ_FLAG_LOGIN) {
				dismissProgressDialog();
				AfProfileInfo myProfile = (AfProfileInfo) result;
				if (myProfile != null) {
					myProfile.password = psw;
					CacheManager.getInstance().setMyProfile(myProfile);
					setLoadAccount(myProfile, Consts.AF_LOGIN_AFID, mAfCorePalmchat);//调中间件保存当前的登陆方式和信息
					Intent intent = new Intent(context, ResetActivity.class);
					intent.putExtra(JsonConstant.KEY_UUID, afid);
					intent.putExtra(JsonConstant.KEY_FLAG, true);
					intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.RESET);
					startActivity(intent);
//					finish();
				}
			}
		} else {
			dismissProgressDialog();
			if (code == 2){
				showToast(R.string.invalid_answer);
			} else {
				Consts.getInstance().showToast(context, code, flag,http_code);
			}
		}
	}

}
