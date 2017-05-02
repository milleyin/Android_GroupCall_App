package com.afmobi.palmchat.ui.activity.setting.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.CommonUtils; 
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfGiftInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
//import com.umeng.analytics.MobclickAgent;

public class GiftDetailAdapter extends BaseAdapter{
	private List<AfGiftInfo> list = new ArrayList<AfGiftInfo>();
	private Context context;
	
	private LayoutInflater inflater;  
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public GiftDetailAdapter(Context context,List<AfGiftInfo> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		return null==list?null:list.size();
	}

	public AfGiftInfo getLastMsg() {
		if(getCount() <= 0){
			return null;
		}
		return list.get(getCount()-1);
	}
	
	public AfGiftInfo getFirstMsg() {
		if(getCount() <= 0){
			return null;
		}
		return list.get(0);
	}
	
	@Override
	public AfGiftInfo getItem(int position) {
			return null==list?null:list.get(position);
	}

	@Override
	public long getItemId(int position) {
			return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_gift_received_history, null);
            holder = new ViewHolder();  
            holder.imageView = (ImageView)convertView.findViewById(R.id.friend_photo);
            holder.textViewSex = (TextView)convertView.findViewById(R.id.text_age);
            holder.textViewFriendName = (TextView)convertView.findViewById(R.id.friend_name);
            holder.textViewTime = (TextView)convertView.findViewById(R.id.tv_send_time);
            holder.textViewSign = (TextView)convertView.findViewById(R.id.friend_sign);
            
            holder.linefault_view = convertView.findViewById(R.id.linefault_view);
            holder.linefull_view =  convertView.findViewById(R.id.linefull_view);
            convertView.setTag(holder);  
        } else {
            holder = (ViewHolder) convertView.getTag();  
        }
		bintSetLine(holder, position);
		if(position < 0) return convertView;
		AfGiftInfo entity = list.get(position);
		holder.textViewSex.setText(entity.age+"");
		holder.textViewSex.setBackgroundResource(Consts.AFMOBI_SEX_MALE == entity.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
		holder.textViewSex.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == entity.sex ? R.drawable.icon_sexage_boy
				: R.drawable.icon_sexage_girl,0,0,0);
		holder.textViewFriendName.setText(entity.name);
		holder.textViewTime.setText(dateFormat.format(entity.time));
		
		String sign = context.getString(R.string.get_gift_history);//increased your [charmlevel] {$targetCharmLevelNum}
		sign = CommonUtils.replace(DefaultValueConstant.TARGET_GET_GIFT_NUM, entity.count+"", sign);
		sign = CommonUtils.replace(DefaultValueConstant.TARGET_CHARM_LEVLE_NUM, entity.charm_level+"", sign);
		CharSequence text = CommonUtils.replceGiftIcon(context, sign);
		holder.textViewSign.setText(text);
		
		//WXL 20151013 调UIL的显示头像方法,统一图片管理
		ImageManager.getInstance().DisplayAvatarImage(holder.imageView, entity.getServerUrl(),
				entity.afid,Consts.AF_HEAD_MIDDLE,entity.sex,entity.getSerialFromHead(),null); 
		/*AvatarImageInfo imageInfo = new AvatarImageInfo(entity.afid, entity.getSerialFromHead(), entity.name,
				Consts.AF_HEAD_MIDDLE, entity.getServerUrl(), entity.sex);
		com.afmobi.palmchat.util.image.ImageLoader.getInstance().displayImage(holder.imageView, imageInfo, false);*/
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AfGiftInfo afGiftInfo = getItem(position);
				toProfile(afGiftInfo);
			}
		});
		return convertView;
	}
	
	private void toProfile(final AfGiftInfo afGiftInfo) {
		
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GR_T_PF);
//		MobclickAgent.onEvent(context, ReadyConfigXML.GR_T_PF);
		
		Intent intent = new Intent(context, ProfileActivity.class);
		AfProfileInfo info =AfFriendInfo.giftInfoToProfile(afGiftInfo);
		intent.putExtra(JsonConstant.KEY_PROFILE, info);
		//请求新的profile资料
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		intent.putExtra(JsonConstant.KEY_AFID, info.afId);
		intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
		context.startActivity(intent);
	}

	private class ViewHolder {
		ImageView imageView;
		TextView textViewSex;  
		TextView textViewFriendName;  
		TextView textViewTime;  
		TextView textViewSign;  
		View linefault_view;  
		View linefull_view;  
		
	}
	
	public void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (getCount() - 1)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}
}
