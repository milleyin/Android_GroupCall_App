package com.afmobi.palmchat.ui.activity.friends.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
//import com.afmobi.palmchat.util.image.ImageLoader;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hj on 2015/12/24.
 */
public class ContactFriendGroupAdapter  extends BaseExpandableListAdapter {
	FragmentSendFriendCallBack mFragmentSendFriendCallBack;
	// 分组数据
	private List<String> group;
	private List<List<MainAfFriendInfo>> child ;
	private Context mContext;
	private SearchFilter searchFilter = SearchFactory.getSearchFilter(SearchFactory.DEFAULT_CODE);

	public ContactFriendGroupAdapter(Context mContext,List<String> group,List<List<MainAfFriendInfo>> child,FragmentSendFriendCallBack mFragmentSendFriendCallBack) {
		super();
		this.mContext = mContext;
		this.group=group;
		this.child=child;
		this.mFragmentSendFriendCallBack = mFragmentSendFriendCallBack;
	}
	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return child.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public MainAfFriendInfo getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_group_expandlistview_title_item,null);
			viewHolder = new ViewHolder();
			viewHolder.group_member_count = (TextView)convertView.findViewById(R.id.group_member_count);
			viewHolder.img_group = (ImageView)convertView.findViewById(R.id.img_group);
			viewHolder.title = (TextView)convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String groupName = (String) group.get(groupPosition);
		viewHolder.title.setText(groupName);
		if(child.size() > groupPosition) {
			viewHolder.group_member_count.setText(" (" + child.get(groupPosition).size() + ")");
		}
		if(isExpanded){
			viewHolder.img_group.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_list_select_contact_exhibition));
		}else{
			viewHolder.img_group.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_list_select_contact_income));
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View arg3, ViewGroup parent) {
		ViewHolderChild holder;
		if (arg3 == null) {
			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.contacts_group_item, null);
			holder = new ViewHolderChild();
			holder.icon = (ImageView) arg3.findViewById(R.id.icon);
			holder.title = (TextView) arg3.findViewById(R.id.title);
			holder.textSort = (TextView) arg3.findViewById(R.id.friend_sort);
			holder.iconAdd = (TextView) arg3.findViewById(R.id.add_friend);
			holder.group_heads = (RelativeLayout) arg3.findViewById(R.id.group_heads);
			holder.group_head_1 = (ImageView) arg3.findViewById(R.id.group_head_1);
			holder.group_head_2 = (ImageView) arg3.findViewById(R.id.group_head_2);
			holder.group_head_3 = (ImageView) arg3.findViewById(R.id.group_head_3);
			holder.img_group_of_lord = (ImageView) arg3.findViewById(R.id.img_group_of_lord);
			holder.mParent = (RelativeLayout) arg3.findViewById(R.id.parent);
			arg3.setTag(holder);
		} else {
			holder = (ViewHolderChild) arg3.getTag();
		}
		holder.iconAdd.setVisibility(View.GONE);
		MainAfFriendInfo obFriendInfo = child.get(groupPosition).get(childPosition);
		holder.icon.setVisibility(View.VISIBLE);
		holder.title.setText(null);
		if(obFriendInfo != null && obFriendInfo.afFriendInfo != null) {
			holder.group_heads.setVisibility(View.GONE);
			holder.img_group_of_lord.setVisibility(View.GONE);
			final AfFriendInfo afFriendInfo =  obFriendInfo.afFriendInfo;
			char c = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afFriendInfo).toUpperCase());
			if (childPosition == 0) {
				holder.textSort.setVisibility(View.VISIBLE);
				holder.textSort.setText(c + "");
			} else {
				AfFriendInfo afPre = (AfFriendInfo) child.get(groupPosition).get(childPosition - 1).afFriendInfo;
				char lastCatalog = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afPre).toUpperCase());
				if (c == lastCatalog) {
					holder.textSort.setVisibility(View.INVISIBLE);
				} else {
					holder.textSort.setVisibility(View.VISIBLE);
					holder.textSort.setText(c + "");
				}
			}
			String displayName = CommonUtils.getRealDisplayName(afFriendInfo);
			if (TextUtils.isEmpty(displayName)) {
				if (!TextUtils.isEmpty(afFriendInfo.afId)) {
					displayName = afFriendInfo.afId.substring(1);
				}
			}
			holder.title.setText(displayName);
//			if (!CommonUtils.showHeadImage(afFriendInfo.afId, holder.icon, afFriendInfo.sex)) {
				//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
				ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead()
						, Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);
//			}
			holder.icon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (afFriendInfo != null && ((afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) || afFriendInfo.isAddFriend)) {
						return;
					}
					if (CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)) {
						Bundle bundle = new Bundle();
						bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
						Intent intent = new Intent(mContext, MyProfileActivity.class);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					} else {
						toProfile(afFriendInfo);
					}

				}
			});
			holder.mParent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// // TODO: 2015/12/24
					if(mFragmentSendFriendCallBack != null){
						mFragmentSendFriendCallBack.fragmentCallBack(true,afFriendInfo);
					}
				}
			});
		}
		else if(obFriendInfo != null && obFriendInfo.afGrpInfo != null){
			holder.icon.setVisibility(View.GONE);
			final AfGrpProfileInfo infos = obFriendInfo.afGrpInfo;
			holder.textSort.setVisibility(View.INVISIBLE);
			holder.title.setText(infos.name);
			holder.group_heads.setTag(infos);
			holder.group_heads.setVisibility(View.VISIBLE);// show group head
			if (infos != null && CacheManager.getInstance().getMyProfile().afId.equals(infos.admin)) {
				holder.img_group_of_lord.setVisibility(View.VISIBLE);
			} else {
				holder.img_group_of_lord.setVisibility(View.GONE);
			}
			holder.icon.setVisibility(View.GONE);// hide friend head
			setGroupAvatar(infos, holder);
			holder.mParent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// // TODO: 2015/12/24
					if(mFragmentSendFriendCallBack != null){
						mFragmentSendFriendCallBack.fragmentCallBack(false,infos);
					}
				}
			});
		}
		return arg3;
	}

	/** 设置群组头像
	 * @param infos
	 * @param holder
	 */
	private void setGroupAvatar(AfGrpProfileInfo infos, ViewHolderChild holder) {
		holder.group_head_1.setImageResource(R.drawable.head_male2);
		holder.group_head_2.setImageResource(R.drawable.head_male2);
		holder.group_head_3.setImageResource(R.drawable.head_male2);
		ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
		imageViews.add(holder.group_head_1);
		imageViews.add(holder.group_head_2);
		imageViews.add(holder.group_head_3);
//        ImageLoader.getInstance().displayImage(imageViews, infos );
		ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
	}


	/**
	 * 跳转到好友信息界面
	 *
	 * @param afFriendInfo
	 */
	private void toProfile(final AfFriendInfo afFriendInfo) {
		Intent intent = new Intent(mContext, ProfileActivity.class);
		AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
		intent.putExtra(JsonConstant.KEY_PROFILE, info);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		intent.putExtra(JsonConstant.KEY_AFID, info.afId);
		intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
		mContext.startActivity(intent);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 * 根据分类列的索引号获得该序列的首个位置
	 *
	 * @param sectionIndex
	 * @return
	 */
	public int getPositionForSection(int sectionIndex) {
		for (int k = 0;k < group.size();k++)
			for (int i = 0; i < child.get(k).size(); i++) {
				MainAfFriendInfo objfriendInfo = child.get(k).get(i);
				if(objfriendInfo != null && objfriendInfo.afFriendInfo != null) {
					AfFriendInfo friendInfo = objfriendInfo.afFriendInfo;
					String sortStr = CommonUtils.getRealDisplayName(friendInfo).toUpperCase();
					char firstChar = searchFilter.getAlpha(sortStr);
					if (firstChar == sectionIndex) {
						return i;
					}
				}
			}
		return -1;
	}

	public void setList(List<List<MainAfFriendInfo>> child) {

		notifyDataSetChanged();
	}

	class ViewHolder{
		public ImageView img_group;
		public TextView title;
		public TextView group_member_count;
	}

	private class ViewHolderChild {
		ImageView icon;
		TextView title;
		TextView textSort;
		TextView iconAdd;
		RelativeLayout mParent;
		public RelativeLayout group_heads;

		/**
		 * small head image 01
		 */
		public ImageView group_head_1;

		/**
		 * small head image 02
		 */
		public ImageView group_head_2;

		/**
		 * small head image 03
		 */
		public ImageView group_head_3;

		public ImageView img_group_of_lord;
	}
}
