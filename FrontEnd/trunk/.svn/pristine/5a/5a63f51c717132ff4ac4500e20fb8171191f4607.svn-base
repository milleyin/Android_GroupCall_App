package com.afmobi.palmchat.ui.activity.friends.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

public class FollowAdapter extends BaseAdapter implements SectionIndexer {

	private ArrayList<AfFriendInfo> mList = new ArrayList<AfFriendInfo>();
	private Context mContext;
	private SearchFilter searchFilter = SearchFactory.getSearchFilter(SearchFactory.DEFAULT_CODE);

	/**
	 * 是否分享广播
	 */
	private boolean mIsShareBrd;
	/**
	 * 是否为好友列表
	 */
	private boolean mIsContact;

	private int mContactCount;

	private int mFriendCount = 0;
	public FollowAdapter(Context context, List<AfFriendInfo> list) {
		mContext = context;
		mList.addAll(list);
	}


	public FollowAdapter(Context context, List<AfFriendInfo> list, boolean isShareBrd, boolean isContact) {
		mContext = context;
		mList.addAll(list);
		mIsShareBrd = isShareBrd;
		mIsContact = isContact;
	}
	public void setList(List<AfFriendInfo> list) {
		if (list != null && list.size() > 0) {
			mList.clear();
			mList.addAll(list);
		}

	}
	public void setListForDelete(List<AfFriendInfo> list) {
		if (list != null) {
			mList.clear();
			mList.addAll(list);
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public AfFriendInfo getItem(int position) {
		// TODO Auto-generated method stub
		if(position < 0)
			return null;
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View arg3, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (arg3 == null) {
			arg3 = LayoutInflater.from(mContext).inflate(R.layout.contacts_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) arg3.findViewById(R.id.icon);
			holder.title = (TextView) arg3.findViewById(R.id.title);
			holder.textSort = (TextView) arg3.findViewById(R.id.friend_sort);
			holder.iconAdd = (TextView) arg3.findViewById(R.id.add_friend);
			holder.vParent = arg3.findViewById(R.id.parent);
			arg3.setTag(holder);
		} else {
			holder = (ViewHolder) arg3.getTag();
		}
		final AfFriendInfo afFriendInfo = mList.get(position);
		holder.icon.setVisibility(View.VISIBLE);
		if(mIsContact) {
			char c = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afFriendInfo).toUpperCase());
			if (position == 0) {
				holder.textSort.setVisibility(View.VISIBLE);
				holder.textSort.setText(c + "");
			} else {
				AfFriendInfo afPre = mList.get(position - 1);
				char lastCatalog = searchFilter.getAlpha(CommonUtils.getRealDisplayName(afPre).toUpperCase());
				if (c == lastCatalog) {
					holder.textSort.setVisibility(View.INVISIBLE);
				} else {
					holder.textSort.setVisibility(View.VISIBLE);
					holder.textSort.setText(c + "");
				}
			}
		}
		else{
			holder.textSort.setVisibility(View.GONE);
		}
		String displayName = CommonUtils.getRealDisplayName(afFriendInfo);
		if (TextUtils.isEmpty(displayName)) {
			if (!TextUtils.isEmpty(afFriendInfo.afId)) {
				displayName = afFriendInfo.afId.substring(1);
			}
		}
		holder.title.setText(displayName);
//		if (!CommonUtils.showHeadImage(afFriendInfo.afId, holder.icon, afFriendInfo.sex)) {
			// 调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
			ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);
//		}
		if (!mIsShareBrd) { // 分享广播时，不需要点击头像的事件
			holder.icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (afFriendInfo != null && afFriendInfo.isAddFriend) {
						return;
					}
					if (afFriendInfo.afId != null && afFriendInfo.afId.startsWith("r")) {
						toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
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
		}
		if (afFriendInfo.isAddFriend) {
			holder.iconAdd.setVisibility(View.VISIBLE);
		} else {
			holder.iconAdd.setVisibility(View.GONE);
		}
		holder.iconAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri smsUri = Uri.parse("smsto:" + afFriendInfo.user_msisdn);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
				intent.putExtra("sms_body", mContext.getString(R.string.default_invite_sms).replace("*", CacheManager.getInstance().getMyProfile().name).replace("-", CacheManager.getInstance().getMyProfile().showAfid()));
				mContext.startActivity(intent);
			}
		});
		/*if(mIsContact && !mIsShareBrd){
			if(position == getCount() - 1){
				for (AfFriendInfo afFriend : mList){
					if(afFriend.isAddFriend){
						mContactCount++;
					}
				}
				mFriendCount = getCount() - mContactCount;
				String msg = mContext.getString(R.string.friend_contacts_count_text);
				String tip = msg.replace("XXXX",mFriendCount+"").replace("YYYY",mContactCount+"");
				holder.tvFriendCount.setText(tip);
				mContactCount = 0;
				holder.tvFriendCount.setVisibility(View.VISIBLE);
			}
			else {
				holder.tvFriendCount.setVisibility(View.GONE);
			}
		}*/
		return arg3;
	}

	private void toChatting(AfFriendInfo affFriendInfo, String afid, String name) {
		// enter public account chat
		if (afid.startsWith(DefaultValueConstant._R) && !CommonUtils.isSystemAccount(affFriendInfo.afId)) {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
			// MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_P_PBL);
		} else {
			// heguiming 2013-12-04
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIST_T_P);
			// MobclickAgent.onEvent(context, ReadyConfigXML.LIST_T_P);

		}

		Intent intent = new Intent();
		if (afid != null && afid.startsWith(DefaultValueConstant._R)) {
			intent.setClass(mContext, AccountsChattingActivity.class);
		} else {
			intent.setClass(mContext, Chatting.class);
		}

		intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
		intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, affFriendInfo.alias);
		intent.putExtra(JsonConstant.KEY_FRIEND, affFriendInfo);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(intent);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据分类列的索引号获得该序列的首个位置
	 *
	 * @param sectionIndex
	 * @return
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			AfFriendInfo friendInfo = mList.get(i);
			String sortStr = CommonUtils.getRealDisplayName(friendInfo).toUpperCase();
			char firstChar = searchFilter.getAlpha(sortStr);
			if (firstChar == sectionIndex) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 通过该项的位置，获得所在分类组的索引号
	 *
	 * @param position
	 *            在当前的列表的第几个
	 * @return
	 */
	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		AfFriendInfo friendInfo = mList.get(position);
		String sortStr = CommonUtils.getRealDisplayName(friendInfo).toUpperCase();
		char firstChar = searchFilter.getAlpha(sortStr);
		return firstChar;
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

	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView textSort;
		TextView iconAdd;
		//TextView tvFriendCount;
		View vParent;
	}

}