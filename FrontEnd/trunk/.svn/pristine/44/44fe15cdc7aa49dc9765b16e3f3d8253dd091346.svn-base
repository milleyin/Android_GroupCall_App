package com.afmobi.palmchat.ui.activity.social;


import java.util.ArrayList;

import com.afmobi.palmchat.ui.customview.CutstomPopwindowEditText.OnPopItemClickListener;
import com.afmobigroup.gphone.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopwindowAdapter extends BaseAdapter implements OnClickListener{

	/**布局加载器*/
	private LayoutInflater mLayoutInflater;
	/**热门标签*/
	private ArrayList<String> mTags = new ArrayList<String>();
	/**上下文*/
	private Context mContext;
	
	private OnPopItemClickListener mOnPopItemClickListener;
	
	public PopwindowAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mTags?0:mTags.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position>mTags.size()?null:mTags.get(position);
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
			convertView = mLayoutInflater.inflate(R.layout.activitysendmessage_dicttagitem, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_TagName = (TextView)convertView.findViewById(R.id.popwindow_tagname);
			convertView.setTag(viewHolder);
			
		} else  {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.iv_TagName.setOnClickListener(this);
		viewHolder.iv_TagName.setTag(position);
		String tagName = mTags.get(position);
		if(!TextUtils.isEmpty(tagName)){
			viewHolder.iv_TagName.setText(tagName);
		}
		return convertView;
	}
	
	/**
	 * 更新数据
	 * @param afTagInfos
	 */
	public void updateData(ArrayList<String> tagNames){
		if((null!=tagNames)&&(tagNames.size()>0)){
			mTags.clear();
			mTags.addAll(tagNames);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 清理数据
	 */
	public void clearData() {
		if(null!=mTags){
			mTags.clear();
			notifyDataSetChanged();
		}
	}
	public class ViewHolder{
		TextView iv_TagName;
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.popwindow_tagname:
			if(null!=mOnPopItemClickListener){
				Integer position = (Integer)view.getTag();
				mOnPopItemClickListener.onItemClick(null, view, position, view.getId());
			}
			break;
		}
	}

    public void setOnPopItemClickListener(OnPopItemClickListener listener) {
    	mOnPopItemClickListener = listener;
    }
}
