package com.afmobi.palmchat.ui.activity.main;

import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.eventbusmodel.ChangeRegionEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.register.CountryActivity;
import com.afmobi.palmchat.ui.activity.register.RegionTwoActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DialogUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
import com.facebook.login.LoginManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

public class UpdateStateActivity extends Activity implements OnClickListener, AfHttpResultListener, DialogInterface.OnKeyListener  {
	private static final int ACTION_REGION = 3; // 地区
	private TextView tv_notice, tv_country, tv_state;
	private Button btn_ok;
	int action; // 用户操作的行为 1代表profile中国家和手机绑定国家不一致。
				// 2代表未绑定手机,且city或者region为other/others。
				// 3代表绑定了手机，且city或者region为other/others
 	private AfProfileInfo afProfileInfo;
	private AfPalmchat mAfPalmchat;
	private String state, city;
	private Dialog dialog;
	/**
	 * 城市是否改变
	 */
	private boolean isChangeOfCity;
	/**
	 * 地区是否改变
	 */
	private boolean isChangeOfState;
	/**
	 * 国家是否改变
	 */
	private boolean isChangeOfCountry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViews();
		init();
	}

	// @Override
	public void findViews() {
		setContentView(R.layout.dialog_state_layout);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_country = (TextView) findViewById(R.id.tv_country);
		tv_state = (TextView) findViewById(R.id.tv_state);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		tv_state.setOnClickListener(this);
	}

	// @Override
	public void init() {
		mAfPalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		 
		afProfileInfo = AppUtils.createProfileInfoFromCacheValue();
//		myProfile = CacheManager.getInstance().getMyProfile();
		Intent mIntent = getIntent();
		action = mIntent.getIntExtra("action", 0);
		if (action == 2) { // 未绑定手机,且city或者region为other/others。
			if (afProfileInfo.country != null) {
				tv_country.setText(afProfileInfo.country);
				tv_country.setOnClickListener(this); // 国家可修改
			}

			tv_notice.setText(getString(R.string.uncertainity_region));
		} else { // 绑定了手机
			if (action == 1) { // profile中国家和手机绑定国家不一致。
				tv_notice.setText(getString(R.string.update_state_notice));
			} else if (action == 3) { // 绑定了手机，国家一致，但city或者region为other/others
				tv_notice.setText(getString(R.string.uncertainity_region));
			}

			String phone_cc = afProfileInfo.phone_cc;
			String bindCountry = DBState.getInstance(this).getCountryFromCode(phone_cc);
			tv_country.setText(bindCountry);
		}

	}
	
	 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok: // ok按钮
			String str = tv_state.getText().toString().trim();
			if (CommonUtils.isEmpty(str)) {
				ToastManager.getInstance().show(this, R.string.prompt_input_region);
			} else {
				showProgressDialog();
				afProfileInfo.country = tv_country.getText().toString().trim();
				afProfileInfo.region = state;
				afProfileInfo.city = city;
				/**
				 * 上传其余数据（除头像，恋爱状态）
				 */
				mAfPalmchat.AfHttpUpdateInfo(afProfileInfo, null, this);
			}
			break;
		case R.id.tv_state: // 地区
			Intent intent = new Intent(this, RegionTwoActivity.class);
			intent.putExtra("country", tv_country.getText().toString().trim());
			intent.putExtra(JsonConstant.KEY_FLAG, false);
			startActivityForResult(intent, ACTION_REGION);
			break;
		case R.id.tv_country: // 国家
			Intent intent1=new Intent(UpdateStateActivity.this, CountryActivity.class);
			intent1.putExtra(JsonConstant.KEY_SELECT_COUNTRY_ONLY, true);
			startActivityForResult(intent1, DefaultValueConstant._86);
			break;
		default:
			break;
		}

	}

	/**
	 * 进度条对话框
	 */
	public void showProgressDialog() {
		dialog = new Dialog(this, R.style.Theme_LargeDialog);
		dialog.setOnKeyListener(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_loading);
		dialog.show();
	}

	/**
	 * 取消进度条框
	 */
	public void dismissProgressDialog() {
		if (this != null) {
			if (null != dialog && dialog.isShowing()) {
				try {
					dialog.cancel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}

		if (requestCode == ACTION_REGION) { // 选择state
			String country = data.getStringExtra(JsonConstant.KEY_COUNTRY);
			state = data.getStringExtra(JsonConstant.KEY_STATE);
			city = data.getStringExtra(JsonConstant.KEY_CITY);

			if (notSame(afProfileInfo.city, city)) {
				afProfileInfo.isChange = true;
				isChangeOfCity = true;
			} else {
				isChangeOfCity = false;
			}

			if (notSame(afProfileInfo.region, state)) {
				afProfileInfo.isChange = true;
				isChangeOfState = true;
			} else {
				isChangeOfState = false;
			}

			if (notSame(afProfileInfo.country, country)) {
				afProfileInfo.isChange = true;
				isChangeOfCountry = true;
			} else {
				isChangeOfCountry = false;
			}

			// if (TextUtils.isEmpty(city)) {
			// city = getString(R.string.other);
			// }
			// if (TextUtils.isEmpty(state)) {
			// state = getString(R.string.others);
			// }

			StringBuilder sb = new StringBuilder();
			if (!TextUtils.isEmpty(city) && !DefaultValueConstant.OTHER.equals(city)) {
				sb.append(city);
			}

			if (!TextUtils.isEmpty(state) && !DefaultValueConstant.OTHERS.equals(state)) {
				if (sb.length() == 0) {
					sb.append(state);
				} else {
					sb.append(",").append(state);
				}
			}

			tv_state.setText(sb.toString());
		} else if (requestCode == DefaultValueConstant._86) { // 选择国家
			tv_country.setText(data.getStringExtra(JsonConstant.KEY_COUNTRY ));
		}

	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		if (code == Consts.REQ_CODE_SUCCESS) {
			if (flag == Consts.REQ_UPDATE_INFO) {// 上传其余资料（除头像、恋爱状态外）
				// update profile
				dismissProgressDialog();
				afProfileInfo.isChange = false;
				if (result != null && result instanceof AfProfileInfo) {
					AfProfileInfo profileInfo = (AfProfileInfo) result;
					afProfileInfo.age = profileInfo.age;
					CacheManager.getInstance().setMyProfile(afProfileInfo);
					mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, afProfileInfo);

//					if (isChangeOfCountry) {// 如果国家改变
//						DialogUtils.showReLoginDialog(UpdateStateActivity.this, UpdateStateActivity.this);
//					} else if (isChangeOfState || isChangeOfCity) {
						EventBus.getDefault().post(new ChangeRegionEvent(false, 0));
						finish();
//					}

					// //
					// SharePreferenceUtils.getInstance(this).setIsChangeCityForNearBy(true);
					// EventBus.getDefault().post(new ChangeRegionEvent(false,
					// 0));
					// Intent intent = new Intent();
					// intent.putExtra("isChangeState", true);
					// setResult(RESULT_OK, intent);
					// finish();
				}

			} else { // 上传失败
				dismissProgressDialog();
				if (code == Consts.REQ_CODE_UNNETWORK) {
					ToastManager.getInstance().show(this, R.string.network_unavailable);
				} else if (code == Consts.REQ_CODE_ILLEGAL_WORD) { // zhh相关语句中含有非法词汇
					Consts.getInstance().showToast(this, code, flag, http_code);
				} else {
					ToastManager.getInstance().show(this, R.string.failure);
				}
			}
		}

	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	/**
	 * 比较两个string是否不相等
	 * 
	 * @param t
	 * @param s
	 * @return
	 */
	private boolean notSame(String t, String s) {
		if (null == t) {
			return true;
		}
		return t.equals(s) ? false : true;
	}

	  
}
