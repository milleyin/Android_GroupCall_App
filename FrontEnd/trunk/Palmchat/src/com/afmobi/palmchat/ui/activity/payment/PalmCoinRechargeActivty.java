package com.afmobi.palmchat.ui.activity.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.customview.TextViewDropDown;
import com.afmobi.palmchat.ui.customview.TextViewDropDown.TVSelectedListener;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfNewPayment;
import com.core.AfNewPayment.AFMoney2CoinsItem;
import com.core.AfNewPayment.AFMoney2CoinsMenu;
import com.core.AfNewPayment.AFMoney2CoinsResp;
import com.core.AfNewPayment.AFRechargeOrderReq;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * plamcoin充值页面
 * 
 * @author zhh 2015-11-30
 * 
 */
public class PalmCoinRechargeActivty extends BaseActivity implements OnClickListener, AfHttpResultListener, TVSelectedListener {
	private TextView tv_title;
	private ImageView img_back;
	private TextViewDropDown tv_amount, tv_payment_mode;
	private Button btn_confirm;
	private AfPalmchat mAfCorePalmchat;
	ArrayList<AFMoney2CoinsMenu> menu_list = new ArrayList<AfNewPayment.AFMoney2CoinsMenu>();
	private int tag_from;
	private boolean isFinish = false; // 是否关闭当前页面
										// (当从服务器获取数据显示进度框时，点击back键关闭当前页面并回退)
	private Dialog mDialog;
	private TextView tv_amount_title;
	private ImageView img_help;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_palmcoin_recharge);
		tv_title = (TextView) findViewById(R.id.title_text);
		img_back = (ImageView) findViewById(R.id.back_button);
		tv_amount_title = (TextView) findViewById(R.id.tv_amount_title);
		tv_amount = (TextViewDropDown) findViewById(R.id.tv_amount);
		tv_payment_mode = (TextViewDropDown) findViewById(R.id.tv_payment_mode);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		img_help = (ImageView) findViewById(R.id.img_help);

	}

	@Override
	public void init() {
		tv_title.setText(R.string.recharge_palmcoin);
		img_back.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		img_help.setOnClickListener(this);

		/* 从哪个页面进入 */
		tag_from = getIntent().getIntExtra(PagaRechargeActivity.TAG_PAGA_FROM, 0);
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		/* 获取支付方式和面值 */
		isFinish = true;
		showProDialog();
		mAfCorePalmchat.AfHttpNewPaymentMoney2CoinsList(this);
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		ArrayList<String> paymentModeLs = new ArrayList<String>();
		for (int i = 0; i < menu_list.size(); i++) {
			AFMoney2CoinsMenu afMoney2CoinsMenu = menu_list.get(i);
			String display = afMoney2CoinsMenu.display;
			if (!display.isEmpty()) {
				paymentModeLs.add(display);
			}
		}
		tv_payment_mode.setDatas(paymentModeLs, this);

		ArrayList<String> amountLs = new ArrayList<String>();
		if (menu_list.size() > 0) {
			AFMoney2CoinsMenu afMoney2CoinsMenu = menu_list.get(0);
			ArrayList<AFMoney2CoinsItem> money2coins_list = afMoney2CoinsMenu.money2coins_list;
			if (money2coins_list != null) {
				for (int j = 0; j < money2coins_list.size(); j++) {
					AFMoney2CoinsItem afMoney2CoinsItem = money2coins_list.get(j);
					if (afMoney2CoinsItem != null) {
						if (j == 0) {
							String str = getResources().getString(R.string.amount_XXX_xxx_palmcoin);
							str = str.replace("{$x}", String.valueOf(afMoney2CoinsItem.currency));
							str = str.replace("{$y}", String.valueOf(afMoney2CoinsItem.money));
							str = str.replace("{$z}", String.valueOf(afMoney2CoinsItem.afcoin));
							tv_amount_title.setText(str);
						}
						amountLs.add(afMoney2CoinsItem.afcoin + "");
					}

				}
			}
		}

		tv_amount.setDatas(amountLs, null);
	}

	/**
	 * 生成订单号
	 */
	private void getOrderId() {
		int selectedPosition = tv_payment_mode.getSelectedPosition();
		int subPosition = tv_amount.getSelectedPosition();
		if (selectedPosition < menu_list.size()) {
			AFMoney2CoinsMenu money2CoinsMenu = menu_list.get(selectedPosition);
			ArrayList<AFMoney2CoinsItem> money2coins_list = money2CoinsMenu.money2coins_list;
			if (subPosition < money2coins_list.size()) {
				AFMoney2CoinsItem money2CoinsItem = money2coins_list.get(subPosition);
				AFRechargeOrderReq rechargeOrderReq = new AFRechargeOrderReq();
				rechargeOrderReq.item_id = money2CoinsItem.item_id;
				rechargeOrderReq.afcoin = money2CoinsItem.afcoin;
				rechargeOrderReq.money = money2CoinsItem.money;
				rechargeOrderReq.gateway = money2CoinsMenu.gateway;
				rechargeOrderReq.channel = money2CoinsMenu.channel;
				rechargeOrderReq.currency = money2CoinsItem.currency;
				rechargeOrderReq.app_channel = AfNewPayment.NewPayment_RechargeOrder_AppChannel_Type_WALLET;

				/* 生成订单ＩＤ */
				showProDialog();
				mAfCorePalmchat.AfHttpNewPaymentGetRCOrderId(rechargeOrderReq, this);
			}

		}

	}

	/**
	 * 通过paga充值
	 */
	private void rechargeByPaga(String orderId) {
		try {/* 获取当前选中的充值方式 */
			int selectedPosition = tv_payment_mode.getSelectedPosition();
			if (selectedPosition < menu_list.size()) {
				AFMoney2CoinsMenu money2CoinsMenu = menu_list.get(selectedPosition);
				String api_url = money2CoinsMenu.api_url;

				String pagaURL = api_url + "?orderid=" + URLEncoder.encode(orderId, "UTF-8");

				Intent intent = new Intent(this, PagaRechargeActivity.class);
				intent.putExtra(PagaRechargeActivity.PARAM_URL_TO_LOAD, pagaURL);
				intent.putExtra(PagaRechargeActivity.TAG_PAGA_FROM, tag_from);
				startActivity(intent);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出当前页，回到wallet页面
	 */
	private void back() {

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
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (isFinish)
					finish();
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

	/**
	 * 跳转到充值说明页面
	 */
	private void toHelpWebActivity() {
		startActivity(new Intent(this, CoinRechargeHelpActivity.class));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button: // 点击标题栏回退按钮
			finish();
			break;
		case R.id.btn_confirm: // 确认充值
			isFinish = true;
			getOrderId();
			break;
		case R.id.img_help: // 充值帮助说明
			toHelpWebActivity();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		isFinish = false;
		disMissDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_PALM_COIN_GET_MONEY2COINS_LIST: // 获取支付方式和面值成功
				if (result != null) {
					AfNewPayment.AFMoney2CoinsResp aFMoney2CoinsResp = (AFMoney2CoinsResp) result;
					ArrayList<AFMoney2CoinsMenu> menu_list = aFMoney2CoinsResp.menu_list;
					if (menu_list != null && menu_list.size() > 0) {
						this.menu_list = menu_list;
						initData();
					}
				}
				break;
			case Consts.REQ_PALM_COIN_CREATE_RECHARGE: // 获取订单成功
				if (result != null) {
					String orderid = (String) result;
					rechargeByPaga(orderid);
				}
				break;

			default:
				break;
			}
		} else {
			switch (flag) {
			case Consts.REQ_PALM_COIN_GET_MONEY2COINS_LIST: // 获取支付方式和面值
				finish();
				if (code == Consts.REQ_CODE_UNKNOWN) { // 未知错误
					ToastManager.getInstance().show(context, context.getString(R.string.code_99));
				} else if (code == Consts.REQ_CODE_NO_DATA) { // 无数据
					ToastManager.getInstance().show(context, context.getString(R.string.payment_option_not_available));
				} else {
					Consts.getInstance().showToast(context, code, flag, http_code);
				}
				break;
			case Consts.REQ_PALM_COIN_CREATE_RECHARGE: // 获取订单失败
				finish();
				if (code == Consts.REQ_CODE_UNKNOWN) { // 未知错误
					ToastManager.getInstance().show(context, context.getString(R.string.code_99));
				} else if (code == Consts.REQ_CODE_NO_RECHARGE_ITEM) { // 此充值选项不存在
					ToastManager.getInstance().show(context, context.getString(R.string.recharge_is_not_available));
				} else if (code == Consts.REQ_CODE_NO_RECHARGE_RULE) { // :此充值选项ID存在，但与制定的规则不符
					ToastManager.getInstance().show(context, context.getString(R.string.recharge_is_not_available));
				} else if (code == Consts.REQ_CODE_CREATE_RECHARGE_FAIL) { // :订单创建失败
					ToastManager.getInstance().show(context, context.getString(R.string.recharge_request_failed));
				} else {
					Consts.getInstance().showToast(context, code, flag, http_code);
				}
				break;
			case -351: // 支付模块正在维护
				ToastManager.getInstance().show(context, context.getString(R.string.system_maintaining));
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 当选择支付方式改变后回调
	 */
	@Override
	public void OnSelected(int position) {
		if (menu_list.size() > position) {
			ArrayList<String> amountLs = new ArrayList<String>();
			AFMoney2CoinsMenu afMoney2CoinsMenu = menu_list.get(position);
			ArrayList<AFMoney2CoinsItem> money2coins_list = afMoney2CoinsMenu.money2coins_list;
			if (money2coins_list != null) {
				for (int j = 0; j < money2coins_list.size(); j++) {
					AFMoney2CoinsItem afMoney2CoinsItem = money2coins_list.get(j);
					if (afMoney2CoinsItem != null) {
						int afcoin = afMoney2CoinsItem.afcoin;
						amountLs.add(afcoin + "");
					}

				}
			}
			tv_amount.setDatas(amountLs, null);
		}

	}

}
