package com.afmobi.palmchat.ui.activity.payment.adapter;

import java.util.ArrayList;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.DateUtil;
import com.core.AfNewPayment.AFGetCoinOrderHistoryItem;
import com.core.AfNewPayment.AFGetCoinOrderHistoryRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * wallet充值记录适配器
 * 
 * @author tony
 *
 */
public class WalletTrasactionAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<AFGetCoinOrderHistoryRecord> pCOHRecordList;
	private LayoutInflater inflater;

	public WalletTrasactionAdapter(Context context, ArrayList<AFGetCoinOrderHistoryRecord> pCOHRecordLis) {
		this.mContext = context;
		this.pCOHRecordList = pCOHRecordLis;

		inflater = LayoutInflater.from(context);
	}

	public void setDataSource(ArrayList<AFGetCoinOrderHistoryRecord> pCOHRecordLis) {
		if (pCOHRecordLis != null) {
			this.pCOHRecordList = pCOHRecordLis;
		}
	}

	@Override
	public int getCount() {
		return pCOHRecordList.size();
	}

	@Override
	public AFGetCoinOrderHistoryRecord getItem(int position) {
		return pCOHRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.store_recharging_records_item, null);
			holder = new ViewHolder();

			holder.rl_item_1 = (RelativeLayout) convertView.findViewById(R.id.rl_item_1);
			holder.rl_item_2 = (RelativeLayout) convertView.findViewById(R.id.rl_item_2);
			holder.img_icon_1 = (ImageView) convertView.findViewById(R.id.img_icon_1);
			holder.img_icon_2 = (ImageView) convertView.findViewById(R.id.img_icon_2);
			holder.tv_desc_1 = (TextView) convertView.findViewById(R.id.tv_desc_1);
			holder.tv_desc_2 = (TextView) convertView.findViewById(R.id.tv_desc_2);
			holder.tv_state_1 = (TextView) convertView.findViewById(R.id.tv_state_1);
			holder.tv_state_2 = (TextView) convertView.findViewById(R.id.tv_state_2);
			holder.tv_date_1 = (TextView) convertView.findViewById(R.id.tv_date_1);
			holder.tv_date_2 = (TextView) convertView.findViewById(R.id.tv_date_2);
			holder.tv_coin_1 = (TextView) convertView.findViewById(R.id.tv_coin_1);
			holder.tv_coin_2 = (TextView) convertView.findViewById(R.id.tv_coin_2);
			holder.line_1 = convertView.findViewById(R.id.line_1);
			holder.line_2 = convertView.findViewById(R.id.line_2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < pCOHRecordList.size()) {
			holder.rl_item_2.setVisibility(View.GONE);
			holder.line_1.setVisibility(View.VISIBLE);
			holder.line_2.setVisibility(View.GONE);

			AFGetCoinOrderHistoryRecord getCoinOrderHistoryRecord = pCOHRecordList.get(position);
			ArrayList<AFGetCoinOrderHistoryItem> pCOHItemList = getCoinOrderHistoryRecord.pCOHItemList;

			if (pCOHItemList.size() > 0) {
				AFGetCoinOrderHistoryItem getCoinOrderHistoryItem = pCOHItemList.get(0);
				String product_code = getCoinOrderHistoryItem.product_code;
				if (product_code.equals("PalmCoin")) {
					holder.img_icon_1.setImageResource(R.drawable.ic_recharge);
				} else if (product_code.equals("Point")) {
					holder.img_icon_1.setImageResource(R.drawable.ic_recharge_by_paga);
				} else if (product_code.equals("AirTime")) {
					holder.img_icon_1.setImageResource(R.drawable.ic_mobile_top_up);
				}

				holder.tv_desc_1.setText(getCoinOrderHistoryItem.order_desc);
				if (getCoinOrderHistoryItem.order_status == 1) {// 订单创建
					holder.tv_state_1.setText(R.string.predict_wating_for_result);
					holder.tv_state_1.setTextColor(mContext.getResources().getColor(R.color.log_blue));
					holder.tv_coin_1.setTextColor(mContext.getResources().getColor(R.color.predict_9af));
				} else if (getCoinOrderHistoryItem.order_status == 4) {// 操作成功
					holder.tv_state_1.setText(R.string.succeeded);
					holder.tv_state_1.setTextColor(mContext.getResources().getColor(R.color.predict_9af));
					holder.tv_coin_1.setTextColor(mContext.getResources().getColor(R.color.text_level_1));
				} else { // 操作失败
					holder.tv_state_1.setText(R.string.palmguess_awarded_failed);
					holder.tv_state_1
							.setTextColor(mContext.getResources().getColor(R.color.public_group_disband_color));
					holder.tv_coin_1.setTextColor(mContext.getResources().getColor(R.color.public_group_disband_color));

				}
				holder.tv_date_1.setText(DateUtil.getYyyyMmDdHhMmSs(getCoinOrderHistoryItem.date_time));
				switch (getCoinOrderHistoryItem.order_type) {
				case 1: // 充值
				case 2: // 加钱
				case 5: // 退款
					holder.tv_coin_1.setText("+" + getCoinOrderHistoryItem.afcoin);
					break;
				case 3: // 消费
				case 4: // 提现
					holder.tv_coin_1.setText("-" + getCoinOrderHistoryItem.afcoin);
					break;

				default:
					break;
				}

			}

			if (pCOHItemList.size() > 1) {
				holder.rl_item_2.setVisibility(View.VISIBLE);
				holder.line_1.setVisibility(View.GONE);
				holder.line_2.setVisibility(View.VISIBLE);
				AFGetCoinOrderHistoryItem getCoinOrderHistoryItem = pCOHItemList.get(1);

				holder.tv_desc_2.setText(getCoinOrderHistoryItem.order_desc);
				if (getCoinOrderHistoryItem.order_status == 1) {// 订单创建
					holder.tv_state_2.setText(R.string.predict_wating_for_result);
					holder.tv_state_2.setTextColor(mContext.getResources().getColor(R.color.log_blue));
					holder.tv_coin_2.setTextColor(mContext.getResources().getColor(R.color.predict_9af));
				} else if (getCoinOrderHistoryItem.order_status == 4) {// 操作成功
					holder.tv_state_2.setText(R.string.succeeded);
					holder.tv_state_2.setTextColor(mContext.getResources().getColor(R.color.predict_9af));
					holder.tv_coin_2.setTextColor(mContext.getResources().getColor(R.color.text_level_1));
				} else { // 操作失败
					holder.tv_state_2.setText(R.string.palmguess_awarded_failed);
					holder.tv_state_2
							.setTextColor(mContext.getResources().getColor(R.color.public_group_disband_color));
					holder.tv_coin_2.setTextColor(mContext.getResources().getColor(R.color.public_group_disband_color));

				}
				holder.tv_date_2.setText(DateUtil.getYyyyMmDdHhMmSs(getCoinOrderHistoryItem.date_time));
				switch (getCoinOrderHistoryItem.order_type) {
				case 1: // 充值
				case 2: // 加钱
				case 5: // 退款
					holder.tv_coin_2.setText("+" + getCoinOrderHistoryItem.afcoin);
					break;
				case 3: // 消费
				case 4: // 提现
					holder.tv_coin_2.setText("-" + getCoinOrderHistoryItem.afcoin);
					break;

				default:
					break;
				}
			}
		}
		return convertView;
	}

	private class ViewHolder {
		RelativeLayout rl_item_1, rl_item_2;
		ImageView img_icon_1, img_icon_2;
		TextView tv_desc_1, tv_desc_2, tv_state_1, tv_state_2, tv_date_1, tv_date_2, tv_coin_1, tv_coin_2;
		View line_1, line_2;
	}

}
