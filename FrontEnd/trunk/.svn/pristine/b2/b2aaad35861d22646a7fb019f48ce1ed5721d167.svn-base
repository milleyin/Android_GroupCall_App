package com.afmobi.palmchat.ui.activity.store.adapter;

import java.util.List;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfPalmchat;
import com.core.AfStoreConsumeRecord.AfConsumeItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 购买记录adapter
 *
 */
public class StorePurchaseHistoryAdapter extends BaseAdapter {
	
	/**上下文*/
	private Context mContext;
	/**数据列表*/
	private List<AfConsumeItem> mList;
	/**进入类型*/
	private int type;
	/**布局加载器*/
	private LayoutInflater mInflater;  
	/**本地jni接口*/
	private AfPalmchat mAfCorePalmchat;
	/**URL前半段地址*/
	private String mStaticAddr;
	
	public static final int LAST_ENTRY = 1;
	public static final int COMMON_ENTRY = 2;
	
	/**
	 * 
	 * @param context 上下文
	 * @param list 数据列表
	 */
	public StorePurchaseHistoryAdapter(Context context, List<AfConsumeItem> list) {
		this.mContext = context;
		mList = list;
		mInflater = LayoutInflater.from(context);
		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;

	}
	
	/**
	 * 
	 * @param addr url前半段地址
	 */
	public void setAddr(String addr) {
		mStaticAddr = addr;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public AfConsumeItem getItem(int position) {
		// TODO Auto-generated method stub
		if(LAST_ENTRY == type){
			return mList.get(position);
		}
		int pos = --position;
		if(pos < 0) return null; 
		return mList.get(pos);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(LAST_ENTRY == type){
			return position;
		}
		return --position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.store_history_item, null);
			holder = new ViewHolder(); 
			
			    holder.head = (ImageView)convertView.findViewById(R.id.img_photo);
	            holder.name = (TextView)convertView.findViewById(R.id.text_name);
	            holder.score = (TextView)convertView.findViewById(R.id.text_sign);
	            holder.time = (TextView)convertView.findViewById(R.id.text_time);
	            holder.linefault_view = convertView.findViewById(R.id.linefault_view);
	            holder.linefull_view =  convertView.findViewById(R.id.linefull_view);
	            convertView.setTag(holder); 
		} else {
			  holder = (ViewHolder) convertView.getTag();  
		}
		bintSetLine(holder, position);
		AfConsumeItem profile = mList.get(position);
		
		if (mStaticAddr != null) {
			ImageManager.getInstance().DisplayImage(holder.head, new StringBuffer(mStaticAddr).append(profile.item_icon).toString(),
					R.drawable.store_emoji_default, false, null);
		}
		
		holder.name.setText(profile.name);
		holder.score.setText(String.valueOf(profile.afcoin));
		holder.time.setText(profile.date);
		
		return convertView;
	}
	
	/**
	 * 设置line显示与否
	 * @param viewHolder
	 * @param position
	 */
	private void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (getCount() - 1)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}
	
	/**
	 * viewHolder
	 *
	 */
	private class ViewHolder {
		
		/**small_icon*/
		ImageView head;
		/**name*/
		TextView name;
		/**afcoin*/
		TextView score;
		/**time*/
		TextView time;
		/**linefault*/
		View linefault_view;
		/**linefull*/
		View linefull_view;
	}

}
