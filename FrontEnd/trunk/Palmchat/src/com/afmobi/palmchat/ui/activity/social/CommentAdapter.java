package com.afmobi.palmchat.ui.activity.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.customview.CollapsibleTextView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfCommentInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class CommentAdapter extends BaseAdapter {
	public static final int BROADCAST_LIST = 0;
	public static final int BROADCAST_DETAIL = 1;
	ArrayList<AfCommentInfo> afInfos;
	LayoutInflater mInflater;
	private int type = 0;
	Context context;

	public void notifyDataSetChanged(List<AfCommentInfo> lAfCommentInfos, boolean isLoadMore) {
		if (!isLoadMore) {
			this.afInfos.clear();
		}
		if (null != lAfCommentInfos) {
			this.afInfos.addAll(lAfCommentInfos);
		}
		notifyDataSetChanged();
	}

	

	public void notifyDataSetChanged(List<AfCommentInfo> lAfCommentInfos) {
		this.afInfos.clear();
		if (null != lAfCommentInfos) {
			this.afInfos.addAll(lAfCommentInfos);
		}
		notifyDataSetChanged();
	}
	
	public CommentAdapter(Context context, int type) {
		this.afInfos = new ArrayList<AfCommentInfo>();
		this.mInflater = LayoutInflater.from(context);
		this.type = type;
		this.context = context;
	}

	public CommentAdapter(Context context, ArrayList<AfCommentInfo> afCommentInfos, int type) {
		this.afInfos = afCommentInfos;
		this.mInflater = LayoutInflater.from(context);
		this.type = type;
		this.context = context;
	}
//
//	public void notifyDataSetChanged(ArrayList<AfCommentInfo> afCommentInfos) {
//		this.afInfos.clear();
//		this.afInfos.addAll(afCommentInfos);
//		super.notifyDataSetChanged();
//	}

	public void addCommentToFirst(AfCommentInfo afCommentInfo) {
		this.afInfos.add(0, afCommentInfo);
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		int count = afInfos.size();
		if (type == BROADCAST_LIST && count > 3) {
			count = 3;
		} else {
			count = afInfos.size();
		}
		return count;
	}

	@Override
	public AfCommentInfo getItem(int position) {
		return afInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public ArrayList<AfCommentInfo> getCommentList() {
		return afInfos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.broadcast_comment_item, null);
			initTextView(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AfCommentInfo info = afInfos.get(position);
		bindView(viewHolder, info);
		return convertView;
	}

	private void initTextView(ViewHolder viewHolder, View convertView) {
		viewHolder.c_name = (TextView) convertView.findViewById(R.id.c_name);
		viewHolder.c_time = (TextView) convertView.findViewById(R.id.c_time);
		viewHolder.c_pa_icon = (ImageView) convertView.findViewById(R.id.pa_icon);
		viewHolder.c_countent = (TextView) convertView.findViewById(R.id.c_countent);
		viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
		viewHolder.img_head_empty = (ImageView) convertView.findViewById(R.id.img_head_empty);
		viewHolder.fl_head = (FrameLayout) convertView.findViewById(R.id.fl_head);
	}

	private void bindView(ViewHolder viewHolder, final AfCommentInfo info) {

		if (type == BROADCAST_DETAIL) {
			String str = CommonUtils.ToDBC(info.comment);
			CharSequence text = EmojiParser.getInstance(context).parse(str, ImageUtil.dip2px(context, 18));
			viewHolder.c_countent.setText(text );
			 
			viewHolder.c_time.setText( DateUtil.getTimeAgo(context, info.time));
			viewHolder.c_time.setVisibility(View.VISIBLE);
			if(info.profile_Info!=null){
				viewHolder.c_name.setText(info.profile_Info.name); 
				if(DefaultValueConstant.BROADCAST_PROFILE_PA== info.profile_Info.user_class){
					viewHolder.c_pa_icon.setVisibility(View.VISIBLE);
				}else{
					viewHolder.c_pa_icon.setVisibility(View.GONE);
				}
				if (TextUtils.isEmpty(info.profile_Info.getServerUrl())) { 
					viewHolder.img_head_empty.setVisibility(View.VISIBLE);
					viewHolder.img_head.setVisibility(View.GONE);
	
					if (info.profile_Info.sex == Consts.AFMOBI_SEX_MALE) {
						viewHolder.img_head_empty.setImageResource(R.drawable.head_male2);
					} else {
						viewHolder.img_head_empty.setImageResource(R.drawable.head_female2);
					}
	
				} else {
					viewHolder.img_head_empty.setVisibility(View.GONE);
					viewHolder.img_head.setVisibility(View.VISIBLE);
					ImageManager.getInstance().DisplayAvatarImage(viewHolder.img_head, info.profile_Info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, info.profile_Info.sex, info.profile_Info.getSerialFromHead(), null);
				}
			}
			
//			viewHolder.c_head_img.setVisibility(View.VISIBLE);
//			// WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
//			ImageManager.getInstance().DisplayAvatarImage(viewHolder.c_head_img, info.profile_Info.getServerUrl(), info.afid, Consts.AF_HEAD_MIDDLE, info.profile_Info.sex, info.profile_Info.getSerialFromHead(), null);
			viewHolder.c_name.setOnClickListener(new OnClickListener() { 
				@Override
				public void onClick(View v) {
					intoProfile(info);
				}
			});
			viewHolder.fl_head.setOnClickListener(new OnClickListener() { 
				@Override
				public void onClick(View v) {
					intoProfile(info);
				}
			});
		} else {
			String name = info.profile_Info.name;
			String conent = info.comment;
			String reply = context.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, "");
			String str = "";
			if (conent.contains(reply)) {
				str = CommonUtils.ToDBC(name + "" + conent);
			} else {
				str = CommonUtils.ToDBC(name + ":" + conent);
			}
			CharSequence text = EmojiParser.getInstance(context).parse(str, name, CommonUtils.dip2px(context, 16));
			// viewHolder.c_countent.setText(text);
			viewHolder.c_countent.setText(text );
			// viewHolder.c_countent.setMText(text,name);
			viewHolder.c_name.setText(info.profile_Info.name);
			viewHolder.c_name.setVisibility(View.GONE);
			viewHolder.fl_head.setVisibility(View.GONE);
			viewHolder.c_time.setVisibility(View.GONE);
			if(DefaultValueConstant.BROADCAST_PROFILE_PA== info.profile_Info.user_class){
				viewHolder.c_pa_icon.setVisibility(View.VISIBLE);
			}else{
				viewHolder.c_pa_icon.setVisibility(View.GONE);
			}
		}
	}

	class ViewHolder {
		TextView c_name;
		TextView c_time;
		ImageView c_pa_icon;
		TextView c_countent;
		ImageView img_head_empty,img_head;
		FrameLayout fl_head;
	}
	private void intoProfile(final AfCommentInfo info){

		AfProfileInfo profileInfo = AfProfileInfo.friendToProfile(info.profile_Info);
		profileInfo.afId = info.afid;
		if (profileInfo != null) {
			 
			if(DefaultValueConstant.BROADCAST_PROFILE_PA==profileInfo.user_class){
				Intent intent = new Intent(context, PublicAccountDetailsActivity.class);
				intent.putExtra("Info", profileInfo);
				context.startActivity(intent); 
			}else  if ( CacheManager.getInstance().getMyProfile().afId.equals(profileInfo.afId)) {
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_MMPF);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_MMPF);

				Bundle bundle = new Bundle();
				bundle.putString(JsonConstant.KEY_AFID, profileInfo.afId);
				Intent intent = new Intent(context, MyProfileActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);

			} else {

				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.BCM_T_PF);
//				MobclickAgent.onEvent(context, ReadyConfigXML.BCM_T_PF);

				Intent intent = new Intent(context, ProfileActivity.class);
				intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profileInfo);
				intent.putExtra(JsonConstant.KEY_FLAG, true);
				intent.putExtra(JsonConstant.KEY_AFID, profileInfo.afId);
				context.startActivity(intent);
			}
		}
	
	}
}