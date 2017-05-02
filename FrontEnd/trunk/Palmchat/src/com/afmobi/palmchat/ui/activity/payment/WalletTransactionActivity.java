package com.afmobi.palmchat.ui.activity.payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.payment.adapter.WalletTrasactionAdapter;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfNewPayment.AFGetCoinOrderHistoryRecord;
import com.core.AfNewPayment.AFGetCoinOrderHistoryResp;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * wallet充值记录
 * 
 * @author zhh 2015-12-22
 *
 */
public class WalletTransactionActivity extends BaseActivity
		implements OnClickListener, IXListViewListener, AfHttpResultListener {
	private XListView mListView;
	private WalletTrasactionAdapter mAdapter;
	private ImageView img_back;
	private TextView tv_title;
	private ArrayList<AFGetCoinOrderHistoryRecord> pCOHRecordList = new ArrayList<AFGetCoinOrderHistoryRecord>();
	private boolean isRefresh = false;
	private boolean isLoadMore = false;
	private AfPalmchat mAfCorePalmchat;
	private static final int LIMIT = 20;
	private Handler mHandler = new Handler();
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	private int mStartIndex = 0;

	@Override
	public void findViews() {
		setContentView(R.layout.activity_transaction);
		tv_title = (TextView) findViewById(R.id.title_text);
		img_back = (ImageView) findViewById(R.id.back_button);

	}

	@Override
	public void init() {
		tv_title.setText(R.string.transaction);
		img_back.setOnClickListener(this);

		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;

		mListView = (XListView) findViewById(R.id.listview);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);

		mAdapter = new WalletTrasactionAdapter(context, pCOHRecordList);
		mListView.setAdapter(mAdapter);

		onRefreshInternal();
	}

	private void onRefreshInternal() {
		int px = AppUtils.dpToPx(context, 60);
		mListView.performRefresh(px);
	}

	private void two_minutes_Cancel_Refresh_Animation() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isRefresh) {
					settingStop();
					ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
				}

			}
		}, Constants.TWO_MINUTER);
	}

	private void settingStop() {
		if (isLoadMore) {
			stopLoadMore();
		}
		if (isRefresh) {
			stopRefresh();
		}
	}

	private void stopLoadMore() {
		isLoadMore = false;
		mListView.stopLoadMore();
	}

	private void stopRefresh() {
		isRefresh = false;
		mListView.stopRefresh(true);
		mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;

		}
	}

	@Override
	public void onRefresh(View view) {
		if (NetworkUtils.isNetworkAvailable(context)) {
			if (!isRefresh) {
				isLoadMore = false;
				isRefresh = true;
				mAfCorePalmchat.AfCoreAfNewPaymentGetRechargeOrderLogs(1, LIMIT, this);  //索引从1开始代表第一页
				two_minutes_Cancel_Refresh_Animation();
			}
		} else {
			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
			stopRefresh();
		}
	}

	@Override
	public void onLoadMore(View view) {
		if (NetworkUtils.isNetworkAvailable(context)) {
			if (!isLoadMore) {
				isLoadMore = true;
				mAfCorePalmchat.AfCoreAfNewPaymentGetRechargeOrderLogs(mStartIndex, LIMIT, this);
			}
		} else {
			ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
			stopLoadMore();
		}
	}

	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_PAYMENT_GET_RECHARGE_ORDERS_LOGS:
				if (result != null) {
					AFGetCoinOrderHistoryResp getCoinOrderHistoryResp = (AFGetCoinOrderHistoryResp) result;
					if (getCoinOrderHistoryResp != null) {
						ArrayList<AFGetCoinOrderHistoryRecord> orderLs = getCoinOrderHistoryResp.pCOHRecordList;
						if (orderLs != null) {
							if (isRefresh) {
								pCOHRecordList.clear();
							}

							mStartIndex += orderLs.size();

							pCOHRecordList.addAll(orderLs);
							mAdapter.setDataSource(pCOHRecordList);
							mAdapter.notifyDataSetChanged();
						}
					}

				}

				break;

			}

		} else {

			if (!isFinishing())
				Consts.getInstance().showToast(context, code, flag, http_code);

		}
		settingStop();
	}

}
