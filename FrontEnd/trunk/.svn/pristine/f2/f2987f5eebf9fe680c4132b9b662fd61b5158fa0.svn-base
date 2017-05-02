package com.afmobi.palmchat.ui.activity.chats.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chats.model.EmotionModel;

public class EmotionAdapter extends BaseAdapter{
	private List<EmotionModel> list = new ArrayList<EmotionModel>();
	
	
	private LayoutInflater inflater;  
	public EmotionAdapter(Context context,List<EmotionModel> list){
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public EmotionModel getItem(int position) {
			return list.get(position);
	}

	@Override
	public long getItemId(int position) {
			return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chatting_grid_options_items, null);
            holder = new ViewHolder();  
            holder.imageView = (ImageView)convertView.findViewById(R.id.chatting_options_img);
            holder.textView = (TextView)convertView.findViewById(R.id.chatting_options_text);
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }
		if(position < 0) return convertView;
		EmotionModel entity = list.get(position);
		holder.imageView.setBackgroundResource(entity.getResId());
		holder.textView.setText(entity.getText());
		PalmchatLogUtils.println("entity  "+entity.getText());
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView textView;  
	}
}
