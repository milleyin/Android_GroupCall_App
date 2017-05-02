package com.afmobi.palmchat.ui.activity.chats.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.chats.model.BackgroundModel;
import com.afmobi.palmchat.util.ImageUtil;

public class ActivityBackgroundChangeAdapter extends BaseAdapter {

	private ArrayList<BackgroundModel> mListBackground;
	private Context mContext;
	public HashMap<String, BackgroundModel> hashMap = new HashMap<String, BackgroundModel>();
	public BackgroundModel selected_background_model;
	
	public ActivityBackgroundChangeAdapter(Context context,ArrayList<BackgroundModel> list){
		mContext = context;
		mListBackground = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListBackground.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListBackground.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_chatting_backgournd_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.icon);
			holder.isselected = (ImageView) convertView.findViewById(R.id.isselected);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		final BackgroundModel backgroundModel = mListBackground.get(position);
		
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.img.getLayoutParams();
		int iconWidth = ImageUtil.DISPLAYW / 3  ;
		params.width = iconWidth;
		params.height = iconWidth;
		
		holder.img.setBackgroundResource(backgroundModel.res_id_little);
		
		if(hashMap.containsKey(backgroundModel.background_name)) {
			holder.isselected.setImageResource(R.drawable.btn_chattingbackground_sel);
			selected_background_model = backgroundModel;
		} else {
			holder.isselected.setImageResource(R.drawable.btn_chattingbackground_nosel);
		}
		
//		holder.isselected.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(!hashMap.containsKey(backgroundModel.background_name)){
//					hashMap.clear();
//					hashMap.put(backgroundModel.background_name, backgroundModel);
//					holder.isselected.setImageResource(R.drawable.btn_chattingbackground_sel);
//					selected_background_model = backgroundModel;
//					notifyDataSetChanged();
//				}
//			}
//		});
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!hashMap.containsKey(backgroundModel.background_name)){
					hashMap.clear();
					hashMap.put(backgroundModel.background_name, backgroundModel);
					holder.isselected.setImageResource(R.drawable.btn_chattingbackground_sel);
					selected_background_model = backgroundModel;
					notifyDataSetChanged();
				}
			}
		});
		
		return convertView;
	}
	
	
	private class ViewHolder {
		ImageView img, isselected;
	}
}
