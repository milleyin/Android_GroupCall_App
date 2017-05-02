package com.afmobi.palmchat.ui.activity.setting.adapter;

import java.util.ArrayList;
import java.util.List;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.Consts;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlockedListAdapter extends BaseAdapter {

	/**上下文*/
	private Context mContext;
	/**黑名单列表*/
	private List<AfFriendInfo> mAfFriendList;
	
	/**
	 *构造方法
	 * @param context
	 */
	public BlockedListAdapter(Context context) {
		mContext  = context;
		mAfFriendList = new ArrayList<AfFriendInfo>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mAfFriendList?0:mAfFriendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (null==mAfFriendList||(position>=getCount()))?null:mAfFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(null==convertView) {
			convertView  = LayoutInflater.from(mContext).inflate(R.layout.item_blockedlist, null);
			viewHolder = new ViewHolder();
			viewHolder.headIcon = (ImageView)convertView.findViewById(R.id.blockedlist_item_headicon);
			viewHolder.freadName = (TextView)convertView.findViewById(R.id.blockedlist_item_friendname);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		AfFriendInfo afFriendInfo = mAfFriendList.get(position);
		ImageManager.getInstance().DisplayAvatarImage(viewHolder.headIcon, afFriendInfo.getServerUrl(),afFriendInfo .getAfidFromHead()
				,Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
        String displayName = CommonUtils.getRealDisplayName(afFriendInfo);
        if (TextUtils.isEmpty(displayName)) {
            if (!TextUtils.isEmpty(afFriendInfo.afId)) {
                displayName = afFriendInfo.afId.substring(1);
            }
        }
        viewHolder.freadName.setText(displayName);
		
		return convertView;
	}
	
	/**
	 * 更新数据
	 * @param afFrendList
	 */
	public void updata(List<AfFriendInfo> afFrendList) {
		mAfFriendList.clear();
		if((null!=afFrendList)&&(afFrendList.size()>0)) {
			mAfFriendList.addAll(afFrendList);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * 清理容器
	 */
	public void clean() {
		if(null!=mAfFriendList) {
			mAfFriendList.clear();
			mAfFriendList=null;
		}
	}
	
	/**
	 * viewHolder
	 * @author starw
	 *
	 */
	private class ViewHolder{
		/**头像*/
		ImageView headIcon;
		/**黑名单好友姓名*/
		TextView freadName;
	}

}
