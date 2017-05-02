package com.afmobi.palmchat.ui.activity.payment;

import java.util.ArrayList;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.ui.customview.TextViewDropDown;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfNewPayment;
import com.core.AfNewPayment.AFCoin2GoodsItem;
import com.core.AfNewPayment.AFCoin2GoodsMenu;
import com.core.AfNewPayment.AFCoin2GoodsResp;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Mobile Top Up 璇濊垂鍏呭�奸〉闈�
 * 
 * @author zhh 2015-12-03
 * 
 */
public class MobileTopUpActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {
	private TextView tv_title, tv_code;
	private TextViewDropDown tv_operator;
	private TextView tv_airtime_1, tv_airtime_2, tv_airtime_3, tv_coin_1, tv_coin_2, tv_coin_3;
	private ViewGroup rl_face_value_1, rl_face_value_2, rl_face_value_3;
	private ImageView img_back;
	private EditText edt_phone_number;
	private AfPalmchat mAfCorePalmchat;
	private ArrayList<AFCoin2GoodsItem> coin2goods_list;// 鏀寔鐨勬秷璐归�夐」鍒楄〃
	private ArrayList<String> network_list;// 鏀寔缃戠粶杩愯惀鍟嗗垪琛�
	private boolean isFinish = false; // 鏄惁鍏抽棴褰撳墠椤甸潰
	private Dialog mDialog;
	private String country_code;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_mobile_top_up);
		tv_title = (TextView) findViewById(R.id.title_text);
		img_back = (ImageView) findViewById(R.id.back_button);
		edt_phone_number = (EditText) findViewById(R.id.edt_phone_number);
		tv_operator = (TextViewDropDown) findViewById(R.id.tv_operator);
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_airtime_1 = (TextView) findViewById(R.id.tv_airtime_1);
		tv_airtime_2 = (TextView) findViewById(R.id.tv_airtime_2);
		tv_airtime_3 = (TextView) findViewById(R.id.tv_airtime_3);
		tv_coin_1 = (TextView) findViewById(R.id.tv_coin_1);
		tv_coin_2 = (TextView) findViewById(R.id.tv_coin_2);
		tv_coin_3 = (TextView) findViewById(R.id.tv_coin_3);
		rl_face_value_1 = (ViewGroup) findViewById(R.id.rl_face_value_1);
		rl_face_value_2 = (ViewGroup) findViewById(R.id.rl_face_value_2);
		rl_face_value_3 = (ViewGroup) findViewById(R.id.rl_face_value_3);
	}

	@Override
	public void init() {
		tv_title.setText(R.string.mobile_top_up);
		tv_operator.setText("MTN");

		AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
		String country = myProfile.country;
		if (country != null) {
			country_code = DBState.getInstance(this).getCcFromCountry(country);
			tv_code.setText("+" + country_code);
		} else {
			country_code = "234";
			tv_code.setText("+" + country_code);
		}

		initMyPhone();

		img_back.setOnClickListener(this);
		rl_face_value_1.setOnClickListener(this);
		rl_face_value_2.setOnClickListener(this);
		rl_face_value_3.setOnClickListener(this);

		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		/* 鑾峰彇杩愯惀鍟嗗拰闈㈠�煎垪琛� */
		// showProgressDialog(getResources().getString(R.string.please_wait));
		isFinish = true;
		showProDialog();
		mAfCorePalmchat.AfHttpNewPaymentCoins2GoodsList(AfNewPayment.NewPayment_Coin2Goods_Type_AirTime, this);
	}

	/**
	 * 鍒濆鍖栨墜鏈哄彿涓烘湰鏈哄彿鐮� zhh
	 */
	private void initMyPhone() {
		String tel = CommonUtils.getMyPhoneNumber(this);
		edt_phone_number.setText(tel);
		edt_phone_number.setSelection(tel.length());
	}

	/**
	 * 鍒濆鍖栭〉闈㈡暟鎹�
	 */
	private void initData() {
		tv_operator.setDatas(network_list, null);
		if (coin2goods_list != null) {
			AFCoin2GoodsItem afCoin2GoodsResp;
			if (0 < coin2goods_list.size()) {
				afCoin2GoodsResp = coin2goods_list.get(0);
				tv_airtime_1.setText( String.valueOf(afCoin2GoodsResp.aim_value));
				String priceStr = getResources().getString(R.string.palmcoin_xxx);
				/* 用千分位格式化 */
				priceStr = priceStr.replace("{$xxx}", CommonUtils.getMicrometerData(afCoin2GoodsResp.afcoin));
				tv_coin_1.setText(priceStr);
			} else {
				rl_face_value_1.setVisibility(View.GONE);
			}

			if (1 < coin2goods_list.size()) {
				afCoin2GoodsResp = coin2goods_list.get(1);
				tv_airtime_2.setText( String.valueOf(afCoin2GoodsResp.aim_value));
				String priceStr = getResources().getString(R.string.palmcoin_xxx);
				/* 用千分位格式化 */
				priceStr = priceStr.replace("{$xxx}", CommonUtils.getMicrometerData(afCoin2GoodsResp.afcoin));
				tv_coin_2.setText(priceStr);
			} else {
				rl_face_value_2.setVisibility(View.GONE);
			}

			if (2 < coin2goods_list.size()) {
				afCoin2GoodsResp = coin2goods_list.get(2);
				tv_airtime_3.setText( String.valueOf(afCoin2GoodsResp.aim_value));
				String priceStr = getResources().getString(R.string.palmcoin_xxx);
				/* 用千分位格式化 */
				priceStr = priceStr.replace("{$xxx}", CommonUtils.getMicrometerData(afCoin2GoodsResp.afcoin));
				tv_coin_3.setText(priceStr);
			} else {
				rl_face_value_3.setVisibility(View.GONE);
			}

		}
	}

	/**
	 * 确认充值
	 * 
	 * @param positon
	 */
	private void sureRecharge(int positon) {
		String phone = edt_phone_number.getText().toString().trim();
		if (phone != null && phone.length() >= 8 && phone.length() <= 15) {
			Intent mIntent = new Intent(MobileTopUpActivity.this, MobileTopUpSureActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putString("phone_number", phone);
			mBundle.putString("phone_country_code", country_code);
			mBundle.putInt("bill_item_id", coin2goods_list.get(positon).item_id);
			mBundle.putInt("afcoin", coin2goods_list.get(positon).afcoin);
			mBundle.putInt("airtime", coin2goods_list.get(positon).aim_value);
			mBundle.putString("network", tv_operator.getText().toString().trim());
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
		} else { // 鎵嬫満鍙锋棤鏁�
			ToastManager.getInstance().show(this, R.string.invalid_number);
		}

	}

	/**
	 * 进度框
	 */
	private void showProDialog() {
		mDialog = new Dialog(this, R.style.Theme_LargeDialog);
		mDialog.setOnKeyListener(this);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_loading);
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (isFinish)
					exit();
			}
		});
		mDialog.show();
	}

	/**
	 * 鍙栨秷杩涘害鏉℃
	 */
	private void disMissDialog() {
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
	
	/**
	 * 退出当前页面
	 */
	private void exit() {
		CommonUtils.closeSoftKeyBoard(edt_phone_number);
		finish();
	}
	

	@Override
	public void onBackPressed() {
		exit();
	}

	@Override
	public void onClick(View v) {
		Intent mIntent;
		Bundle mBundle;
		String phone;
		switch (v.getId()) {
		case R.id.back_button: //返回按钮
			exit();
			break;
		case R.id.rl_face_value_1: //面值1
			sureRecharge(0);
			break;
		case R.id.rl_face_value_2: //面值2
			sureRecharge(1);
			break;
		case R.id.rl_face_value_3: //面值3
			sureRecharge(2);
			break;
		default:
			break;
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		isFinish = false;
		disMissDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_PALM_COIN_GET_GOODS_LIST: // 鑾峰彇杩愯惀鍟嗗拰闈㈠�煎垪琛�
				if (result != null) {
					AfNewPayment.AFCoin2GoodsResp afcoin2GoodsResp = (AFCoin2GoodsResp) result;
					ArrayList<AFCoin2GoodsMenu> menu_list = afcoin2GoodsResp.menu_list;
					if (menu_list != null && menu_list.size() > 0) {
						AFCoin2GoodsMenu coin2GoodsMenu = menu_list.get(0); // 鍟嗗搧鍒楄〃,鐢变簬鐩墠鍙槸閽堝璇濊垂鍏呭�兼墍浠ュ彧鏈変竴椤�
						if (coin2GoodsMenu != null) {
							coin2goods_list = coin2GoodsMenu.coin2goods_list;
							network_list = coin2GoodsMenu.network_list;
							initData();
						}
					}
				}

				break;

			default:
				break;
			}
		} else {
			switch (flag) {
			case Consts.REQ_PALM_COIN_GET_GOODS_LIST: //获取消费选项列表
				exit();
				if (code == Consts.REQ_CODE_UNKNOWN) { //未知错误
					ToastManager.getInstance().show(context, context.getString(R.string.code_99));
				} else if (code == Consts.REQ_CODE_NO_DATA) { //无数据
					ToastManager.getInstance().show(context, context.getString(R.string.mobile_top_up_not_available));
				} else if(code ==  -351) {  //支付模块正在维护
					ToastManager.getInstance().show(context, context.getString(R.string.system_maintaining));
				}else {
					Consts.getInstance().showToast(context, code, flag, http_code);
				}
				break;

			default:
				break;
			}
		}

	}
	
	

}
