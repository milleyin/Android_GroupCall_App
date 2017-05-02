package com.afmobi.palmchat.ui.activity.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.R.integer;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.TextViewDropDown;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfLottery;
import com.core.AfNewPayment;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.AfLottery.PayInfo;
import com.core.AfLottery.PointsInfo;
import com.core.AfNewPayment.AFMoney2CoinsMenu;
import com.core.AfNewPayment.AFMoney2CoinsResp;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

/**
 * points充值页面
 * 
 * @author zhh
 * 
 */
public class PointsRechargeActivity extends BaseActivity implements OnClickListener, AfHttpResultListener {
	private TextView tv_title, tv_points, tv_n;
	private ImageView img_back;
	private TextViewDropDown tv_pay_mode;
	private Button btn_pay_now;
	int coins;
	long points;
	private String front_url;// 必须 Paga充值获取订单用的API,返回的front_url
	private String front_url1; // 进行paga充值URL
	private String currency = ""; // 可选 操作的币种, 充值包括国际货币的币种，如奈拉为NGN、CNY等。全球货币符号，
	private String moneyType = "POINT"; // 必须 充值到账的方式,
	private String remark = "PAGA"; // 必须说明该订单购买的什么东西等
	private String payGateway = Constants.RECHARGE_TYPE_PAGA; // 必须用于支付该笔订单的支付网关PAGA：指PAGA
	String returnUrl = "";
	private AfPalmchat mAfCorePalmchat;
	private boolean isFinish = false; // 是否关闭当前页面
	private Dialog mDialog;
	private int tag_from;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_points_recharge);
		tv_title = (TextView) findViewById(R.id.title_text);
		img_back = (ImageView) findViewById(R.id.back_button);
		tv_points = (TextView) findViewById(R.id.tv_points);
		tv_n = (TextView) findViewById(R.id.tv_n);
		tv_pay_mode = (TextViewDropDown) findViewById(R.id.tv_pay_mode);
		btn_pay_now = (Button) findViewById(R.id.btn_pay_now);
	}

	@Override
	public void init() {
		tv_title = (TextView) findViewById(R.id.title_text);
		tv_title.setText(R.string.recharging);

		/* 从哪个页面进入 */
		tag_from = getIntent().getIntExtra(PagaRechargeActivity.TAG_PAGA_FROM, 0);

		AfLottery.LotteryInit li = CacheManager.getInstance().getLotteryInit(); // 调用博彩初始化接口返回的数据
		if (null != li) {
			/* 初始化充值方式 */
			ArrayList<PayInfo> payInfo_list = li.payInfo_list; // 充值方式列表
			if (payInfo_list != null && payInfo_list.size() > 0) {
				PayInfo pInfo = payInfo_list.get(0);
				tv_pay_mode.setText(pInfo.sname);
				front_url = pInfo.front_url;
				front_url1 = pInfo.front_url1;
				ArrayList<PointsInfo> pointsInfo_list = pInfo.pointsInfo_list; // 充值金额列表
				if (pointsInfo_list != null && pointsInfo_list.size() > 0) {
					PointsInfo pointsInfo = pointsInfo_list.get(0);
					coins = pointsInfo.coins;
					points = pointsInfo.points;
					String pointsStr = getResources().getString(R.string.recharge_xxx_points);
					pointsStr = pointsStr.replace("{$xxx}", String.valueOf(points));
					tv_points.setText(pointsStr);

					String nStr = getResources().getString(R.string.n_xxx);
					nStr = nStr.replace("{$xxx}", String.valueOf(coins));
					tv_n.setText(nStr);
				}
			}

		}

		img_back.setOnClickListener(this);
		btn_pay_now.setOnClickListener(this);
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;

	}

	/**
	 * 获取订单号
	 */
	private void getOrderId() {
		String afid = CacheManager.getInstance().getMyProfile().afId;
		/* 获取支付方式和面值 */
		showProDialog();
		mAfCorePalmchat.AfHttpPaymentGetPagaOrderId(afid, front_url, String.valueOf(coins), "0", currency, moneyType, remark, payGateway, returnUrl, this);
	}

	/**
	 * 通过paga充值
	 */
	private void rechargeByPaga(String orderId) {
		try {/* 获取当前选中的充值方式 */
			if (!front_url1.isEmpty()) {
				String pagaURL = front_url1 + "?orderid=" + URLEncoder.encode(orderId, "UTF-8");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button: // 点击标题栏回退按钮
			finish();
			break;
		case R.id.btn_pay_now: // 充值
			isFinish = true;
			getOrderId();
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
			case Consts.REQ_PREDICT_BUY_POINTS_BYPAGA_GETORDERID: // 获取订单
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
			case Consts.REQ_PREDICT_BUY_POINTS_BYPAGA_GETORDERID: // 获取订单
				finish();
				if (code == -8014) { // 充值不成功
					ToastManager.getInstance().show(context, context.getString(R.string.pay_failed));
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
