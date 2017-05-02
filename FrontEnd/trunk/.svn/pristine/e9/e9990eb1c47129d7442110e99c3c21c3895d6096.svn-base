package com.afmobi.palmchat.ui.activity.groupchat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.util.CommonUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.Consts;

/**
 * 群设置适配器
 * @author heguiming
 *
 */
public class EditGroupAdapter extends BaseAdapter {
	
	private static final String TAG = EditGroupAdapter.class.getCanonicalName();
	
	private Context mContext;
	int index = 0;
	
	/**
	 * 数据源
	 */
	private ArrayList<AfGrpProfileItemInfo> dataList = new ArrayList<AfGrpProfileItemInfo>();
	
	public EditGroupAdapter(Context mContext, List<AfGrpProfileItemInfo> dataList) {
		this.mContext = mContext;
		setDataList(dataList);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public AfGrpProfileItemInfo getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,final ViewGroup parent) {
		ViewHodler viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.edit_my_group_grid_item, parent, false);
			viewHolder.usernameTxt = (TextView) convertView.findViewById(R.id.invite_friend_setting_item_name);
			viewHolder.photoImg = (ImageView) convertView.findViewById(R.id.invite_friend_setting_item_iamge);
			viewHolder.masterImg = (ImageView) convertView.findViewById(R.id.editgroup_member_master);
			viewHolder.ageTxt = (TextView) convertView.findViewById(R.id.gender_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHodler) convertView.getTag();
		}
		
		AfGrpProfileItemInfo member = getItem(position);
		loadViewData(member, viewHolder);
		return convertView;
	}

	/**
	 * 加载Item数据
	 * @param member
	 * @param viewHolder
	 */
	private void loadViewData(final AfGrpProfileItemInfo member,final ViewHodler viewHolder) {
		viewHolder.photoImg.setBackgroundResource(R.drawable.head_male2);
		viewHolder.masterImg.setVisibility(member.isMaster == 0 ? View.GONE : View.VISIBLE);
		
		if (null != member.alias && !"".equals(member.alias)) {
			viewHolder.usernameTxt.setText(member.alias);
		} else {
			viewHolder.usernameTxt.setText(member.name == null ? "" : member.name);
		}
		
		String nameStr = viewHolder.usernameTxt.getText().toString();
		if (StringUtil.isNullOrEmpty(nameStr)) {
			String name = "";
			if (null != member.afid) {
				name = member.afid.replace("a", "");
			}
			viewHolder.usernameTxt.setText(name);
		}
		
		if (!StringUtil.isNullOrEmpty(member.afid)) {
			//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
			ImageManager.getInstance().DisplayAvatarImage(viewHolder.photoImg, member.getServerUrl(),member.getAfidFromHead()
					,Consts.AF_HEAD_MIDDLE,member.sex,member.getSerialFromHead(),null);  
		/*	// 何桂明  2013-10-23
			String name = member.alias == null ? member.name : member.alias;
			// name is empty,so afid
			if (StringUtil.isNullOrEmpty(name)) {
				name = member.afid.replace("a", "");
			}
			AvatarImageInfo imageInfo = new AvatarImageInfo(member.getAfidFromHead(), member.getSerialFromHead(), name, Consts.AF_HEAD_MIDDLE, member.getServerUrl(), member.sex);
			ImageLoader.getInstance().displayImage(viewHolder.photoImg, imageInfo);*/
		} else {
			if ((dataList.size()-1) < 50) {
				viewHolder.photoImg.setBackgroundColor(Color.TRANSPARENT);
				viewHolder.photoImg.setBackgroundResource(R.drawable.editgroup_album_add_btn_selector);
			}else {
				viewHolder.photoImg.setVisibility(View.GONE);
			}
		}
	}

	public ArrayList<AfGrpProfileItemInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<AfGrpProfileItemInfo> dataList) {
		this.dataList.clear();
		if (null != dataList) {
			this.dataList.addAll(dataList);
		}
	}
	
	public void setIndex(int position) {
		index = position;
	}
	
	public void updateAdapter(List<AfGrpProfileItemInfo> dataList) {
		setDataList(dataList);
		notifyDataSetChanged();
	}
	
	/**
	 * View 控件
	 * @author heguiming
	 *
	 */
	class ViewHodler {
		// 用户头像
		ImageView photoImg;
		// 群主
		ImageView masterImg;
		// 用户名
		TextView usernameTxt;
		// 年齡
		TextView ageTxt;
	}

}
