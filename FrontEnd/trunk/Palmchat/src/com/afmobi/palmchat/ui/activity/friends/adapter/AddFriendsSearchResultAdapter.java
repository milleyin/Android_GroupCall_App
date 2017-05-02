package com.afmobi.palmchat.ui.activity.friends.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class AddFriendsSearchResultAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AfFriendInfo> mList = new ArrayList<AfFriendInfo>();

	
	public AddFriendsSearchResultAdapter(Context context) {
		this.mContext = context;
		
	}
	
	public void setList(List<AfFriendInfo> list) {
		mList.clear();
		mList.addAll(list);
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public AfFriendInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.home_group_item2, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.gender = (ImageView) convertView.findViewById(R.id.gender);
			holder.isNew = (TextView) convertView.findViewById(R.id.tv_new);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.viewParent = convertView.findViewById(R.id.parent);
			holder.group_num = (TextView) convertView.findViewById(R.id.group_num);
			holder.groupHeadLayout = (RelativeLayout) convertView.findViewById(R.id.group_heads);
			holder.textSort = (TextView) convertView.findViewById(R.id.friend_sort);
			holder.divideLine = convertView.findViewById(R.id.divide_line);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.groupHeadLayout.setVisibility(View.GONE);
		holder.gender.setVisibility(View.GONE);
		holder.group_num.setVisibility(View.GONE);
		holder.isNew.setVisibility(View.GONE);
		holder.textSort.setVisibility(View.GONE);
		
		if (position == mList.size() - 1) {
			holder.divideLine.setVisibility(View.VISIBLE);
		} else {
			holder.divideLine.setVisibility(View.GONE);
		}
		
	final AfFriendInfo afFriendInfo = mList.get(position);
	
	holder.title.setText(CommonUtils.getRealDisplayName(afFriendInfo));
	
	holder.content.setText(afFriendInfo.signature);

	if (afFriendInfo.signature == null) {
		holder.content.setText(mContext.getResources().getString(
				R.string.default_signature));
	} else {
		CharSequence text = EmojiParser.getInstance(mContext).parse(afFriendInfo.signature, ImageUtil.dip2px(mContext, 18));
		holder.content.setText(text);
	}
	 
	//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
	ImageManager.getInstance().DisplayAvatarImage(holder.icon, afFriendInfo.getServerUrl(),afFriendInfo.getAfidFromHead()
			,Consts.AF_HEAD_MIDDLE,afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null); 
	

	holder.viewParent.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			if(!CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)){
//				toChatting(afFriendInfo, afFriendInfo.afId,
//						afFriendInfo.name);
//			}
			
			//gtf 2014-11-16 
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ID_T_PF);
//			MobclickAgent.onEvent(mContext, ReadyConfigXML.ID_T_PF);
			
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
	
	return convertView;
	
	}
	
	 private class ViewHolder {
		 RelativeLayout groupHeadLayout;
		 ImageView icon;
		 ImageView gender;
		 TextView title;
		 TextView content;
		 ImageView add_btn;
		 View viewParent;
		 TextView group_num;
		 TextView isNew;
		 TextView textSort;
		 View divideLine;
	}
	 
		private void toChatting(AfFriendInfo infos, String afid, String name) {
			// TODO Auto-generated method stub
			PalmchatLogUtils.println("toChatting");
			Intent intent = new Intent(mContext, Chatting.class);
			intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
			intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
			intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
			intent.putExtra(JsonConstant.KEY_FRIEND, infos);
			mContext.startActivity(intent);
		}
	
		private void toProfile(final AfFriendInfo afFriendInfo) {
			Intent intent = new Intent(mContext, ProfileActivity.class);
			AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
			intent.putExtra(JsonConstant.KEY_PROFILE, info);
			intent.putExtra(JsonConstant.KEY_FLAG, true);
			intent.putExtra(JsonConstant.KEY_AFID, info.afId);
			intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
			mContext.startActivity(intent);
		}

}
