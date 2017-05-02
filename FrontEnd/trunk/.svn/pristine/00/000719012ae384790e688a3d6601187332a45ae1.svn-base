package com.afmobi.palmchat.ui.activity.tagpage.adapter;


import java.util.ArrayList;

import com.afmobi.palmchat.ui.customview.RectImageView;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm.AfTagInfo;
import com.core.cache.CacheManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotTagsAdapter extends BaseAdapter {

	/**布局加载器*/
	private LayoutInflater mLayoutInflater;
	/**热门标签*/
	private ArrayList<AfTagInfo> mHotTags;
	/**上下文*/
	private Context mContext;
	
	public HotTagsAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		mHotTags = new ArrayList<AfTagInfo>();
		mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mHotTags?0:mHotTags.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position>mHotTags.size()?null:mHotTags.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(null==convertView ){
			convertView = mLayoutInflater.inflate(R.layout.item_treading_headview_hottags, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_HotTag = (RectImageView)convertView.findViewById(R.id.broadcast_trending_hottags_img_id);
			viewHolder.tv_HotTag = (TextView)convertView.findViewById(R.id.broadcast_trending_hottags_name_id);
			convertView.setTag(viewHolder);
		} else  {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		AfTagInfo afTagInfo = mHotTags.get(position);
		if(null!=afTagInfo){
			ImageManager.getInstance().DisplayImage(viewHolder.iv_HotTag,CacheManager.getInstance().getTrThumb_url(afTagInfo.pic_url,false,false),
					R.color.base_back,false,null);
			//viewHolder.tv_HotTag.setText(mContext.getResources().getString(R.string.broadcast_trending_tag)+afTagInfo.tag);
			viewHolder.tv_HotTag.setText(afTagInfo.tag);
		}
		
		return convertView;
	}
	
	/**
	 * 更新数据
	 * @param afTagInfos
	 */
	public void updateData(ArrayList<AfTagInfo> afTagInfos){
		if((null!=afTagInfos)&&(afTagInfos.size()>0)){
			mHotTags.clear();
			mHotTags.addAll(afTagInfos);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 清理数据
	 */
	public void clearData() {
		if(null!=mHotTags){
			mHotTags.clear();
			notifyDataSetChanged();
		}
	}
	public class ViewHolder{
		RectImageView iv_HotTag;
		TextView  tv_HotTag;
	}

}
