package com.afmobi.palmchat.ui.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.ui.activity.register.CountryActivity;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

public class ChangePhoneActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {

	private final static String PARAM_COUNTRY_CODE = "code";
	private final static String PARAM_COUNTRY_NAME = "name";
//	private final static int REQUEST_CODE_SELECT_COUNTRY = 87;
	public static final int SET_SUCCESS_PHONE = 8001;
	// private String mCountyName;
	private TextView tv_old_phone, tv_country_code, tv_country;
	private EditText edt_phone;
	private Button next_button;
	private TextView title_text;
	private String new_phone_number;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_change_phone);
		title_text = (TextView) findViewById(R.id.title_text);
		tv_old_phone = (TextView) findViewById(R.id.tv_old_phone);
		tv_country_code = (TextView) findViewById(R.id.tv_country_code);
		tv_country = (TextView) findViewById(R.id.tv_country);
		edt_phone = (EditText) findViewById(R.id.edt_phone);
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
	}

	@Override
	public void init() {
		title_text.setText(R.string.text_change_phone_number);

		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		String blankSpace = " ";
		String my_bind_phone_number = myProfile.phone;
		String phone_cc = myProfile.phone_cc;
		if (CommonUtils.isEmpty(phone_cc)) {
			tv_old_phone.setText(blankSpace + my_bind_phone_number);
		} else {
			if (phone_cc.contains("+"))
				tv_old_phone.setText(phone_cc + blankSpace + my_bind_phone_number);
			else
				tv_old_phone.setText("+" + phone_cc + blankSpace + my_bind_phone_number);
		}

		// String[] str = CommonUtils.getCountryAndCode(this);
		// country_text.setText(str[0]);
		// cty_code.setText(str[1]);

		if (myProfile.country != null) {
			tv_country.setText(myProfile.country);
			String cc = DBState.getInstance(this).getCcFromCountry(myProfile.country);
			if (cc != null) {
				if (cc.contains("+"))
					tv_country_code.setText(cc);
				else
					tv_country_code.setText("+" + cc);

			}

		}
		// edt_phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.login_ico_ph,
		// 0, 0, 0);
		edt_phone.addTextChangedListener(new TextWatcher() {
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_country:
			Intent intent=new Intent(this, CountryActivity.class);
			intent.putExtra(JsonConstant.KEY_SELECT_COUNTRY_ONLY,true);
			startActivityForResult(intent, DefaultValueConstant._86);
			break;
		case R.id.next_button:
			// if(!CommonUtils.isCanUseSim(this)){
			// ToastManager.getInstance().show(this, R.string.insert_sim);
			// return;
			// }
			new_phone_number = edt_phone.getText().toString().trim();
			if (CommonUtils.isEmpty(new_phone_number)) {
				ToastManager.getInstance().show(this, R.string.prompt_input_phone);
				return;
			}
			if (new_phone_number.length() < 8 || new_phone_number.length() > 15) {
				ToastManager.getInstance().show(this, R.string.invalid_number);
				return;
			}

			AppDialog appDialog = new AppDialog(this);
			appDialog.createChangePhoneDialog(context, getString(R.string.sms_code_will_sent), getCountryCode(), new_phone_number, new OnConfirmButtonDialogListener() {

				@Override
				public void onRightButtonClick() {
					// to varify
					showProgressDialog(R.string.please_wait);
					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpAccountOpr(Consts.REQ_CHECK_ACCOUNT_BY_PHONE_EX, new_phone_number, CommonUtils.getRealCountryCode(getCountryCode()), null, null, null, RegistrationActivity.BIND, ChangePhoneActivity.this);
				}

				@Override
				public void onLeftButtonClick() {

				}
			});
			appDialog.show();
			break;

		default:
			break;
		}
	}

	private void getSmsCode() {
		PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetSMSCode(Consts.REQ_GET_SMS_CODE_AFTER_LOGIN, CommonUtils.getRealCountryCode(getCountryCode()), new_phone_number, Consts.SMS_CODE_TYPE_BIND,0, null, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DefaultValueConstant._86) {
			if (data != null) {
				String temp1 = null, temp2 = null;
				temp1 = data.getStringExtra(JsonConstant.KEY_COUNTRY );
				temp2 = data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE);
				if (!TextUtils.isEmpty(temp2) && !TextUtils.isEmpty(temp2)) {
					// mCountyName = temp1;
					// if(!TextUtils.isEmpty(temp2)){
					// cty_code.setText(temp2);
					// }
					// country_text.setText(mCountyName);
				}

			}
		} else if (requestCode == SET_SUCCESS_PHONE) {
			if (data != null) {
				Intent intent = new Intent(this, MyAccountInfoActivity.class);
				setResult(8001, intent);
				MyActivityManager.getScreenManager().popActivity();
			}

		}

	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_GET_SMS_CODE_AFTER_LOGIN) {
				dismissProgressDialog();
				Intent intent = new Intent(this, NewBindPhoneActivity.class);
				intent.putExtra(JsonConstant.KEY_ACCOUNT, new_phone_number);
				intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, CommonUtils.getRealCountryCode(getCountryCode()));
				startActivityForResult(intent, SET_SUCCESS_PHONE);
			} else if (flag == Consts.REQ_CHECK_ACCOUNT_BY_PHONE_EX) {
				boolean is_exist = (boolean) (null != result);
				System.out.println("ywp: AfOnResult: check account result  = " + is_exist);
				if (is_exist) {
					dismissProgressDialog();

					AppDialog appDialog = new AppDialog(context);
					appDialog.createOKDialog(context, getString(R.string.phone_has_been_binded), new OnConfirmButtonDialogListener() {

						@Override
						public void onRightButtonClick() {

						}

						@Override
						public void onLeftButtonClick() {

						}
					});
					appDialog.show();
				} else {
					getSmsCode();
				}

			}
		} else {
			dismissProgressDialog();
			Consts.getInstance().showToast(context, code, flag, http_code);
		}
	}

	private String getCountryCode() {
		return tv_country_code.getText().toString().trim();
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
