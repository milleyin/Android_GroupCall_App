package com.afmobi.palmchat.ui.activity.payment;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.UpdateCoinsEvent;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfLottery;
import com.core.AfNewPayment.AFAirtimeTopupReq;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 手机话费充值确认页面
 * 
 * @author tony
 * 
 */
public class MobileTopUpSureActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {
	private TextView tv_title, tv_phone, tv_title_airtime, tv_palmcoin_balance, tv_insufficient_balance;
	private Button btn_confirm, btn_recharge;
	private ImageView img_back;
	private AfPalmchat mAfCorePalmchat;
	private int afcoin; // 花费的 coin 的数量
	String phone_number;
	String phone_country_code;
	int bill_item_id;
	int airtime;
	String network;
	String afId;
	private int flag = 1; // 用于区分当前是从服务器获取数据，还是提交数据到服务器 1：获取数据 2：提交数据
	private Dialog mDialog;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_mobile_top_up_sure);
		tv_title = (TextView) findViewById(R.id.title_text);
		img_back = (ImageView) findViewById(R.id.back_button);
		tv_phone = (TextView) findViewById(R.id.tv_phone_number);
		tv_title_airtime = (TextView) findViewById(R.id.tv_title_airtime);
		tv_palmcoin_balance = (TextView) findViewById(R.id.tv_palmcoin_balance);
		tv_insufficient_balance = (TextView) findViewById(R.id.tv_insufficient_balance);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_recharge = (Button) findViewById(R.id.btn_recharge);

		EventBus.getDefault().register(this);
	}

	@Override
	public void init() {
		tv_title.setText(R.string.mobile_top_up);
		btn_confirm.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
		img_back.setOnClickListener(this);

		Intent mIntent = getIntent();
		Bundle mBundle = mIntent.getExtras();
		if (mBundle != null) {
			phone_number = mBundle.getString("phone_number");
			phone_country_code = mBundle.getString("phone_country_code");
			bill_item_id = mBundle.getInt("bill_item_id");
			afcoin = mBundle.getInt("afcoin");
			airtime = mBundle.getInt("airtime");
			network = mBundle.getString("network");

			tv_phone.setText(phone_number);

			String str = getResources().getString(R.string.top_up_xxx_airtime_for_yyy_palmcoin);
			str = str.replace("{$xxx}", CommonUtils.getMicrometerData(airtime));
			str = str.replace("{$yyy}", CommonUtils.getMicrometerData(afcoin));
			SpannableStringBuilder style = new SpannableStringBuilder(str);
			style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.look_around_range1)), 7,
					7 + CommonUtils.getMicrometerData(airtime).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.look_around_range1)),
					20 + CommonUtils.getMicrometerData(airtime).length(),
					20 + CommonUtils.getMicrometerData(airtime).length()
							+ CommonUtils.getMicrometerData(afcoin).length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv_title_airtime.setText(style);

			AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
			afId = profileInfo.afId;
			mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
			getCurrentPoint();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/* 接收到eventbus事件时，刷新available balance */
	public void onEventMainThread(UpdateCoinsEvent event) {
		if (event.isUpdateFromServer()) { // 从服务器获取最新Balance
			getCurrentPoint();
		}

	}

	/**
	 * 刷新coins
	 */
	private void updateCoins(int balance) {
		EventBus.getDefault().post(new UpdateCoinsEvent(balance, false));
	}

	
	private void getCurrentPoint() {
		/* 获取当前palmcoin余额 */
		flag = 1;
		showProDialog();
		mAfCorePalmchat.AfHttpPredictGetCoins(afId, this);
	}
	/**
	 * 手机话费充值
	 */
	private void airtimeTopup() {
		AFAirtimeTopupReq req = new AFAirtimeTopupReq();
		req.from_afid = afId;
		req.to_afid = afId;
		req.phone_number = phone_number;
		req.phone_country_code = phone_country_code;
		req.bill_item_id = bill_item_id;
		req.afcoin = afcoin;
		req.amount = airtime;
		req.network = network;
		req.remark = "";
		/* Mobile TOp Up */
		// showProgressDialog(getResources().getString(R.string.please_wait));
		showProDialog();
		mAfCorePalmchat.AfHttpNewPaymentAirtimeTopup(req, this);
	}

	/**
	 * 更新界面
	 * 
	 */
	private void updateUI(int pCoin) {
	 
		if (pCoin >= afcoin) { // 余额充足
			String str = getResources().getString(R.string.balance_xxx);
			str = str.replace("{$xxx}", CommonUtils.getMicrometerData(pCoin));
			tv_palmcoin_balance.setText(str);
		} else { // 余额不足
			tv_palmcoin_balance.setVisibility(View.GONE);
			btn_confirm.setVisibility(View.GONE);
			String str = getResources().getString(R.string.insufficient_balance_xxx);
			str = str.replace("{$xxx}", CommonUtils.getMicrometerData(pCoin));
			tv_insufficient_balance.setText(str);
			tv_insufficient_balance.setVisibility(View.VISIBLE);
			btn_recharge.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 进入成功页面
	 */
	private void toSuccessActivity() {
		startActivity(new Intent(this, PaymentSuccessActivity.class));
	}

	/**
	 * 进度条对话框
	 */
	private void showProDialog() {
		mDialog = new Dialog(this, R.style.Theme_LargeDialog);
		mDialog.setOnKeyListener(this);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_loading);
		mDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (flag == 1) { // 获取数据
					mDialog.dismiss();
					finish();
				} else if (flag == 2) { // 提交数据

				}
				return true;
			}
		});

		mDialog.show();
	}

	/**
	 * 取消进度条框
	 */
	public void disMissDialog() {
		if (this != null) {
			if (null != mDialog && mDialog.isShowing()) {
				try {
					mDialog.cancel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button: // 点击标题栏回退按钮
			finish();
			break;
		case R.id.btn_confirm: // 手机话费充值
			flag = 2;
			airtimeTopup();
			break;
		case R.id.btn_recharge: // palmcoin充值
			Intent mIntent = new Intent(MobileTopUpSureActivity.this, PalmCoinRechargeActivty.class);
			mIntent.putExtra(PagaRechargeActivity.TAG_PAGA_FROM, PagaRechargeActivity.TAG_PAGA_FROM_AIRTIME);
			startActivity(mIntent);
			break;

		default:
			break;
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		disMissDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_PREDICT_BUY_POINTS_GETCOINS: // 获取palmcoin余额成功
 
				AfLottery lottery = (AfLottery) result;
				updateUI((int)lottery.getconiresp.balance);
//				int pCoin = (Integer) result;
//				updateUI(pCoin);
 
				break;
			case Consts.REQ_PALM_COIN_GET_AIRTIME_TOPUP: // Mobile Top Up成功
				updateCoins((Integer) result);
				toSuccessActivity();
				break;
				

			default:
				break;
			}
		} else {
			switch (flag) {
			case Consts.REQ_PREDICT_BUY_POINTS_GETCOINS: // 获取palmcoin余额失败
				finish();
				/* 按照交互改成统一提示 */
				ToastManager.getInstance().show(context, context.getString(R.string.pay_failed));
				break;
			case Consts.REQ_PALM_COIN_GET_AIRTIME_TOPUP: // Mobile Top Up失败
				if (code == Consts.REQ_CODE_NO_ENOUGH_COIN) { // 该帐号 Coin 余额不足
					ToastManager.getInstance().show(context, context.getString(R.string.insufficient_balance));
				} else if (code == Consts.REQ_CODE_NOT_SUPPORT_COUNTRY) { // 国家码出错或该国家暂不支持此服务
					ToastManager.getInstance().show(context, context.getString(R.string.phone_number_supported));
				} else if (code == Consts.REQ_CODE_RECHARGE_FAIL) { // 话费充值失败
					ToastManager.getInstance().show(context, context.getString(R.string.mobile_top_up_failed));
				}
				if (code == Consts.REQ_CODE_UNKNOWN) { // 未知错误
					ToastManager.getInstance().show(context, context.getString(R.string.code_99));
				} else {
					Consts.getInstance().showToast(context, code, flag, http_code);
				}
				break;
			default:
				break;
			}
		}

	}
	
	
	

}
