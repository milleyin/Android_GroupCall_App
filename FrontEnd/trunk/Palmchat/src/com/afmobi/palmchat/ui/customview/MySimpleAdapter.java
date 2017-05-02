package com.afmobi.palmchat.ui.customview;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.EmojiParser;

public class MySimpleAdapter extends BaseAdapter {
	Context context;
	List<HashMap<String, Object>> dataList;
	LayoutInflater inflater;
	String type;
//	List<String> listString;
	public MySimpleAdapter(Context context ,List<HashMap<String, Object>> dataList,String type) {
		this.context = context;
		this.dataList = dataList;
		this.inflater = LayoutInflater.from(context);
		this.type = type;
	}
	
//	public MySimpleAdapter(Context context,List<String> listString, String type,int other) {
//		this.context = context;
//		this.listString = listString;
//		this.inflater = LayoutInflater.from(context);
//		this.type = type;
//		
//	}

	@Override
	public int getCount() {
//		if(EmojiParser.GIF.equals(type)){
//			return listString.size();
//		}else{
//			return dataList.size();
//		}
		return dataList==null?0:dataList.size();
	}

	@Override
	public Object getItem(int position) {
//		if(EmojiParser.GIF.equals(type)){
//			return listString.get(position);
//		}else{
//			return dataList.get(position);
//		}
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder2 holder;
		HashMap<String, Object> dataItem = null;
		if(EmojiParser.GIF.equals(type)){
			dataItem = dataList.get(position);
		}else{
			dataItem = dataList.get(position);
		}
		if(convertView == null){
			holder = new ViewHolder2();
			if(EmojiParser.GIF.equals(type)){
				convertView = inflater.inflate(R.layout.chatting_grid_face_gif_items, null);
			}else{
				convertView = inflater.inflate(R.layout.chatting_grid_emotion_items, null);
			}
			holder.image = (ImageView)convertView.findViewById(R.id.chatting_emotion_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder2)convertView.getTag();
		}
		
		if(EmojiParser.GIF.equals(type)){//动态表情商店表情
			String imgPath = (String) dataItem.get("IMAGE");
		/*	PreviewImageInfo imageInfo = new PreviewImageInfo(imgPath, false,true);
			ImageLoader.getInstance().displayImage(holder.image, imageInfo);*/
			ImageManager.getInstance().DisplayLocalImage( holder.image,imgPath,R.drawable.ic_chatting_sticker_loadfailed,R.drawable.ic_chatting_sticker_loadfailed,null);
		}else{ //基本表情
			if(type != null && type.equals(EmojiParser.GIF)){
				
			} else{
				holder.image.setImageResource((Integer) dataItem.get("IMAGE"));
			}
		}
		
		return convertView;
	}
	
	private class ViewHolder2 {
		ImageView image, isselected;
	}
	
}
