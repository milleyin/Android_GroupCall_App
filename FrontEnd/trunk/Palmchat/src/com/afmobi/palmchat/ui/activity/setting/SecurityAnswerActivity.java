package com.afmobi.palmchat.ui.activity.setting;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.SecurityAnswerEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

import de.greenrobot.event.EventBus;
//import com.umeng.analytics.MobclickAgent;

/**
 * Security Answer
 * @author heguiming
 *
 */
public class SecurityAnswerActivity extends BaseActivity implements
		OnClickListener, AfHttpResultListener {
	
	public static final String KEY_AFID = "Afid";
	public static final String KEY_QUESTION_ARY = "QuestionAry";
	
	private final String TAG = SecurityAnswerActivity.class.getCanonicalName();
	
	private EditText mAfidEdit;
	private Button mNextBtn;
	
	private boolean isLogin = false;

	protected AfPalmchat mAfCorePalmchat;
	
	@Override
	public void findViews() {
		setContentView(R.layout.activity_security_answer);
		
		TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.secure_answer);
		findViewById(R.id.back_button).setOnClickListener(this);
		isLogin = getIntent().getBooleanExtra(JsonConstant.KEY_IS_LOGIN, false);
		mNextBtn = (Button) findViewById(R.id.next_btn);
		mNextBtn.setOnClickListener(this);
		mNextBtn.setClickable(false);
		mAfidEdit = (EditText) findViewById(R.id.afid_edit);
		mAfidEdit.addTextChangedListener(new LimitTextWatcher(mAfidEdit, 11,new Handler(),MessagesUtils.EDIT_TEXT_CHANGE));
		
		new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_S_F);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_S_F);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public void init() {
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.next_btn:
			getQuestion();
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(SecurityAnswerEvent event){
		isClickBtnContinue(event.getSetColor());
	}

	/**
	 *改变button的颜色值
	 * @param bool
	 */
	public void isClickBtnContinue(boolean bool){
		if (bool) {
			mNextBtn.setBackgroundResource(R.drawable.login_button_selector);
			mNextBtn.setClickable(true);
		} else {
			mNextBtn.setBackgroundResource(R.drawable.btn_blue_d);
			mNextBtn.setClickable(false);
		}
	}

	private void getQuestion() {
		String afidStr =  mAfidEdit.getText().toString();
		if (!StringUtil.isNullOrEmpty(afidStr)) {
			showProgressDialog(R.string.loading);
			if (isLogin) {
				mAfCorePalmchat.AfHttpFindPwdGetQuestion("", Consts.AF_LOGIN_AFID, null, this);
			}else {
				mAfCorePalmchat.AfHttpFindPwdGetQuestion("a"+afidStr, Consts.AF_LOGIN_AFID, null, this);
			}
		} else {
			showToast(R.string.please_enter_your_af_id);
		}
	
	}

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		PalmchatLogUtils.e(TAG, "----Flag:" + flag + "----Code:" + code);
		dismissProgressDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_PSD_GET_QUESTION:
			case Consts.REQ_PSD_GET_QUESTION_EX:
				if (null != result) {
					String[] questionAry = (String[]) result;
					Intent intent = new Intent(SecurityAnswerActivity.this, SetAnswerActivity.class);
					String afidStr = "a" + mAfidEdit.getText().toString();
//					if (isLogin) {
//						afidStr="";
//					}
					intent.putExtra(KEY_AFID, afidStr);
					intent.putExtra(KEY_QUESTION_ARY, questionAry);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		} else {
			// code == -56 时表示没有安全问题
			// code == -4时表示账号不存在
			if (code == -56) {
				showToast(R.string.you_havent_set_your_security_questions_yet);
			} else if (code == -4) {
				showToast(R.string.invalid_af_id_please_try_again);
			} else {
				Consts.getInstance().showToast(context, code, flag,http_code);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
