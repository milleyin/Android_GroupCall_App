package com.afmobi.palmchat.ui.activity.tagpage.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.afmobigroup.gphone.R;
import com.core.AfResponseComm.AfTagInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * tags适配器
 * @author Transsion
 *
 */
public class TagsAdapter extends BaseAdapter {

	/**布局加载器*/
	private LayoutInflater mLayoutInflater;
	/**当前检索文字*/
	private String mKeyword="";
	/**数据列表*/
	private ArrayList<AfTagInfo> mAfTagInfoList;
	/**上下文*/
	private Context mContext;
	/**数组转换*/
	private DecimalFormat mDecimalFormat;
	/**
	 * 构造方法
	 * @param context
	 */
	public TagsAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		mAfTagInfoList = new ArrayList<AfTagInfo>();
		this.mContext = context;
		this.mDecimalFormat = new DecimalFormat("#,###");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mAfTagInfoList?0:mAfTagInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position>=mAfTagInfoList.size()?null:mAfTagInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(null==convertView) {
			convertView = mLayoutInflater.inflate(R.layout.item_tags,null);
			viewHolder = new ViewHolder();
			viewHolder.tv_TagName = (TextView)convertView.findViewById(R.id.tags_item_tag_name_id);
			viewHolder.tv_PostSum = (TextView)convertView.findViewById(R.id.tags_item_tag_postsum);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		AfTagInfo afTagInfo = mAfTagInfoList.get(position);
		
		if(null!=afTagInfo){
			//String tagName = mContext.getString(R.string.broadcast_trending_tag)+afTagInfo.tag;
			String tagName = afTagInfo.tag;
			String tagNameLower = tagName.toLowerCase();
			if(tagNameLower.contains(mKeyword)){
				SpannableStringBuilder spannableSBuilder = new SpannableStringBuilder(tagName);
				int index = tagNameLower.indexOf(mKeyword);
				spannableSBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.log_blue)), index, index+mKeyword.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				viewHolder.tv_TagName.setText(spannableSBuilder);
				
			} else {
				viewHolder.tv_TagName.setText(tagName);
			}
			try{
				viewHolder.tv_PostSum.setText(mDecimalFormat.format(afTagInfo.post_number));
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			viewHolder.tv_TagName.setText("");
			viewHolder.tv_PostSum.setText("");
		}
		 

		return convertView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	/**
	 * 设置关键字
	 * @param keyword
	 */
	public void setKeyword(String keyword){
		mKeyword = keyword;
	}
	
	/**
	 * 获取检索关键字
	 * @return
	 */
	public String getKeyword() {
		return this.mKeyword;
	}
	
	/**
	 * 返回当前的点击tag
	 * @param position
	 * @return
	 */
	public String getCurTag(int position){
		String tagString = "";
		if(position-1>=mAfTagInfoList.size()){
			
		}else {
			AfTagInfo afTagInfo = mAfTagInfoList.get(position-1);
			if(null!=afTagInfo){
				tagString = afTagInfo.tag;
			}
		}
		
		return tagString;
	}
	
	/**
	 * 更新数据
	 * @param afTagInfo
	 * @param isRefresh
	 */
	public void updateData(ArrayList<AfTagInfo> afTagInfo,boolean isRefresh){
		if(null!=afTagInfo) {
			if(isRefresh){
				mAfTagInfoList.clear();
			}
			mAfTagInfoList.addAll(afTagInfo);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 清理数据
	 */
	public void clearData(){
		if(null!=mAfTagInfoList){
			mAfTagInfoList.clear();
			notifyDataSetChanged();
		}
		
	}
	private class ViewHolder {
		TextView tv_TagName;
		TextView tv_PostSum;
	}

}
