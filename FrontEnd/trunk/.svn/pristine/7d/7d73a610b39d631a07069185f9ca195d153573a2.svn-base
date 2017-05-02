package com.afmobi.palmchat.ui.activity.friends.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.friends.LocalSearchActivity;
import com.afmobi.palmchat.ui.activity.friends.model.LocalSearchAdapterItem;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ImageLoader;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class LocalSearchAdapter extends BaseAdapter {

	public static final int ITEM_TYPE_RECENT_RECORD = 0;
	public static final int ITEM_TYPE_FRIENDS = 1;
	private ArrayList<LocalSearchAdapterItem> mList = new ArrayList<LocalSearchAdapterItem>();
//	private ListViewAddOn listViewAddOn = new ListViewAddOn();
	private Context mContext;
	private String mText;
	private boolean isSendtoFriend;

	public LocalSearchAdapter(Context context,boolean isSendtoFriend) {
		mContext = context;
		this.isSendtoFriend = isSendtoFriend;
	}

	public void setList(ArrayList<LocalSearchAdapterItem> list) {
		mList.clear();
		mList.addAll(list);
	}

	public void setSearchText(String text) {
		mText = text;
	}

	public void clearList() {
		mList.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
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
			arg3 = LayoutInflater.from(mContext).inflate(
					R.layout.home_group_item2, null);
			holder = new ViewHolder();
			holder.viewParent = arg3.findViewById(R.id.parent);
			holder.icon = (ImageView) arg3.findViewById(R.id.icon);
			holder.group_heads = (RelativeLayout) arg3.findViewById(R.id.group_heads);

			holder.group_head_1 = (ImageView) arg3.findViewById(R.id.group_head_1);
			holder.group_head_2 = (ImageView) arg3.findViewById(R.id.group_head_2);
			holder.group_head_3 = (ImageView) arg3.findViewById(R.id.group_head_3);
			holder.gender = (ImageView) arg3.findViewById(R.id.gender);
			holder.title = (TextView) arg3.findViewById(R.id.title);
			holder.content = (TextView) arg3.findViewById(R.id.content);
			holder.group_num = (TextView) arg3.findViewById(R.id.group_num);


			holder.textSort = (TextView) arg3.findViewById(R.id.friend_sort);
			holder.newFlag = (TextView)arg3.findViewById(R.id.tv_new);
			holder.itemTop = (TextView)arg3.findViewById(R.id.item_top);
			holder.wrapHead = arg3.findViewById(R.id.wraphead);
			holder.divideLine = arg3.findViewById(R.id.divide_line);
			holder.addFriend = arg3.findViewById(R.id.add_friend);
			arg3.setTag(holder);
		} else {
			holder = (ViewHolder) arg3.getTag();
		}

		LocalSearchAdapterItem item = mList.get(position);

		holder.newFlag.setVisibility(View.GONE);
		holder.group_num.setVisibility(View.GONE);
		holder.gender.setVisibility(View.GONE);
		holder.content.setVisibility(View.GONE);
		holder.textSort.setVisibility(View.GONE);
		holder.divideLine.setVisibility(View.GONE);
		holder.addFriend.setVisibility(View.GONE);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.wrapHead.getLayoutParams();
		params.setMargins(ImageUtil.dip2px(mContext, 10), 0, 0, 0);

		if (item.mItemType == ITEM_TYPE_RECENT_RECORD) {

			if (item.isFirstItem) {
				holder.itemTop.setVisibility(View.VISIBLE);
				holder.itemTop.setText(R.string.chats);
			} else {
				holder.itemTop.setVisibility(View.GONE);
			}

			final MainAfFriendInfo recentInfo = item.mItemInfoRecents;
			if (MessagesUtils.isPrivateMessage(recentInfo.afMsgInfo.type) || MessagesUtils.isSystemMessage(recentInfo.afMsgInfo.type)) {
				holder.group_num.setVisibility(View.GONE);
				holder.group_heads.setVisibility(View.GONE);
				holder.icon.setVisibility(View.VISIBLE);

				final AfFriendInfo afFriendInfo = recentInfo.afFriendInfo;
				String displayName = CommonUtils.getRealDisplayName(afFriendInfo);
				if (TextUtils.isEmpty(displayName)) {
					if(!TextUtils.isEmpty(afFriendInfo.afId)) {
						displayName = afFriendInfo.afId.substring(1);

					}
				}

				 holder.title.setText(displayName);

				if (mText != null && displayName != null && displayName.toLowerCase().contains(mText.toLowerCase())) {
					 SpannableStringBuilder spannable = new SpannableStringBuilder(displayName);
					 int index = displayName.toLowerCase().indexOf(mText.toLowerCase());
					 spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.log_blue)), index, index + mText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					 holder.title.setText(spannable);
				}


//				if (!CommonUtils.showHeadImage(afFriendInfo.afId, holder.icon, afFriendInfo.sex)) {
					//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
					ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead()
							, Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);
//				}

				holder.viewParent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(!isSendtoFriend) {
							toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);

							PalmchatApp.getApplication().mAfCorePalmchat.AfRecentMsgSetUnread(recentInfo.afMsgInfo.getKey(), 0);
							recentInfo.afMsgInfo.unReadNum = 0;
						}
						else{
							((LocalSearchActivity) mContext).sendBroadcastToFriend(true,afFriendInfo);
						}
					}

				});
				if(isSendtoFriend) {
					holder.viewParent.setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							((LocalSearchActivity) mContext).sendBroadcastToFriend(true, afFriendInfo);
							return true;
						}
					});
				}
			} else if (MessagesUtils.isGroupChatMessage(recentInfo.afMsgInfo.type)) {

				holder.group_heads.setVisibility(View.VISIBLE);
				holder.icon.setVisibility(View.GONE);

				String displayName = recentInfo.afGrpInfo.name;
				holder.title.setText(displayName);

				if (mText != null && displayName != null && displayName.toLowerCase().contains(mText.toLowerCase())) {
					 SpannableStringBuilder spannable = new SpannableStringBuilder(displayName);
					 int index = displayName.toLowerCase().indexOf(mText.toLowerCase());
					 spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.log_blue)), index, index + mText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					 holder.title.setText(spannable);
				}

				setGroupAvatar(recentInfo.afGrpInfo, holder);

				holder.viewParent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(!isSendtoFriend) {
							toGroupChat(recentInfo.afGrpInfo);

							PalmchatApp.getApplication().mAfCorePalmchat.AfRecentMsgSetUnread(recentInfo.afMsgInfo.getKey(), 0);
							recentInfo.afMsgInfo.unReadNum = 0;
						}
						else{
							((LocalSearchActivity) mContext).sendBroadcastToFriend(false,recentInfo.afGrpInfo);
						}
					}

				});
				if(isSendtoFriend) {
					holder.viewParent.setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							((LocalSearchActivity) mContext).sendBroadcastToFriend(false, recentInfo.afGrpInfo);
							return true;
						}
					});
				}
			}

		} else if (item.mItemType == ITEM_TYPE_FRIENDS) {

			holder.group_num.setVisibility(View.GONE);
			holder.group_heads.setVisibility(View.GONE);
			holder.icon.setVisibility(View.VISIBLE);

			if (item.isFirstItem) {
				holder.itemTop.setVisibility(View.VISIBLE);
				holder.itemTop.setText(R.string.contacts);
			} else {
				holder.itemTop.setVisibility(View.GONE);
			}

			final AfFriendInfo afFriendInfo = item.mItemInfoFriends;

			String displayName = CommonUtils.getRealDisplayName(afFriendInfo);
			if (TextUtils.isEmpty(displayName)) {
				if(!TextUtils.isEmpty(afFriendInfo.afId)) {
					displayName = afFriendInfo.afId.substring(1);

				}
			}

			holder.title.setText(displayName);

			if (mText != null && displayName != null && displayName.toLowerCase().contains(mText.toLowerCase())) {
				 SpannableStringBuilder spannable = new SpannableStringBuilder(displayName);
				 int index = displayName.toLowerCase().indexOf(mText.toLowerCase());
				 spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.log_blue)), index, index + mText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				 holder.title.setText(spannable);
			}

//			if (!CommonUtils.showHeadImage(afFriendInfo.afId, holder.icon, afFriendInfo.sex)) {
				//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
				ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(), afFriendInfo.getAfidFromHead()
						, Consts.AF_HEAD_MIDDLE, afFriendInfo.sex, afFriendInfo.getSerialFromHead(), null);

//			}
			if(afFriendInfo.isAddFriend){
				holder.addFriend.setVisibility(View.VISIBLE);
			}
			else{
				holder.addFriend.setVisibility(View.GONE);
			}
			holder.addFriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Uri smsUri = Uri.parse("smsto:" + afFriendInfo.user_msisdn);
					Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
					intent.putExtra("sms_body", mContext.getString(R.string.default_invite_sms).replace("*", CacheManager.getInstance().getMyProfile().name).replace("-", CacheManager.getInstance().getMyProfile().showAfid()));
					mContext.startActivity(intent);
				}
			});

			holder.viewParent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(afFriendInfo.isAddFriend)
						return;
					if(!isSendtoFriend){
						toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
					}
					else{
						((LocalSearchActivity) mContext).sendBroadcastToFriend(true, afFriendInfo);
					}
				}

			});
			if(isSendtoFriend) {
				holder.viewParent.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						((LocalSearchActivity) mContext).sendBroadcastToFriend(true, afFriendInfo);
						return true;
					}
				});
			}
		}

		return arg3;
	}

	private void toChatting(AfFriendInfo affFriendInfo, String afid, String name) {
//		enter public account chat

        //判断是否已经加入黑名单
        if(null!= CacheManager.getInstance().getFriendsCacheSortListEx(
        		Consts.AFMOBI_FRIEND_TYPE_BF).search(affFriendInfo, false, true)) {
            ToastManager.getInstance().show(mContext, R.string.beblocked);
            return;
        }
		if(afid.startsWith(DefaultValueConstant._R) && !CommonUtils.isSystemAccount(affFriendInfo.afId)){
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_P_PBL);
//			MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_P_PBL);
		} else {
			// heguiming 2013-12-04
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIST_T_P);
//			MobclickAgent.onEvent(mContext, ReadyConfigXML.LIST_T_P);

		}

		Intent intent = new Intent();
		if (afid != null && afid.startsWith(DefaultValueConstant._R)) {
			intent.setClass(mContext, AccountsChattingActivity.class);
		} else {
			intent.setClass(mContext,Chatting.class);
		}
		//Intent intent = new Intent(mContext, Chatting.class);
		intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
		intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, affFriendInfo.alias);
		intent.putExtra(JsonConstant.KEY_FRIEND, affFriendInfo);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(intent);
	}


	private void toGroupChat(AfGrpProfileInfo groupListItem) {

		Bundle bundle = new Bundle();
		bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
		if(!"".equals(groupListItem.afid)||null!=groupListItem.afid){
			bundle.putString(GroupChatActivity.BundleKeys.ROOM_ID,
					groupListItem.afid);
		}
		if(!"".equals(groupListItem.name)||null!=groupListItem.name){
			bundle.putString(
					GroupChatActivity.BundleKeys.ROOM_NAME,
					groupListItem.name);
		}

		HelpManager.getInstance(mContext).jumpToPage(
				GroupChatActivity.class, bundle, false, 0,
				false);

	}


	private void setGroupAvatar(AfGrpProfileInfo infos, ViewHolder holder) {

		holder.group_head_1.setImageResource(R.drawable.head_male2);
		holder.group_head_2.setImageResource(R.drawable.head_male2);
		holder.group_head_3.setImageResource(R.drawable.head_male2);
//		holder.group_head_4.setImageResource(R.drawable.head_male);

		ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
		imageViews.add(holder.group_head_1);
		imageViews.add(holder.group_head_2);
		imageViews.add(holder.group_head_3);
//		imageViews.add(holder.group_head_4);

//		ImageLoader.getInstance().displayImage(imageViews, infos );
		ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
	}

	private class ViewHolder {

		TextView itemTop;
		TextView group_num;
		ImageView icon;
		ImageView gender;
		TextView title;
		TextView content;
		TextView textSort;
		View viewParent;
		TextView newFlag;
		View wrapHead;

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

		/**
		 * small head image 04
		 */
		public ImageView group_head_4;

		public ImageView group_avatar_shadow;
		public View divideLine;
		public View addFriend;
	}

}
