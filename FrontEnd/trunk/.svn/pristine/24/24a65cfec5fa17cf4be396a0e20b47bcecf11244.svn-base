package com.afmobi.palmchat.ui.activity.social;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;

public class NearbyStarListAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<AfProfileInfo> mList;
	private int type;
	private LayoutInflater inflater;  
	private AfPalmchat mAfCorePalmchat;
	private Typeface mFontFace; 
	
	public static final int LAST_ENTRY = 1;
	public static final int COMMON_ENTRY = 2;
	
	public NearbyStarListAdapter(Context context, List<AfProfileInfo> list, Typeface fontFace) {
		this.mContext = context;
		mList = list;
		mFontFace = fontFace;
		inflater = LayoutInflater.from(context);
		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public AfProfileInfo getItem(int position) {
		// TODO Auto-generated method stub
		if(LAST_ENTRY == type){
			return mList.get(position);
		}
		int pos = --position;
		if(pos < 0) return null; 
		return mList.get(pos);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(LAST_ENTRY == type){
			return position;
		}
		return --position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nearby_star_list_item, null);
			holder = new ViewHolder(); 
			
			    holder.age = (TextView)convertView.findViewById(R.id.text_age);
			    holder.range = (TextView)convertView.findViewById(R.id.text_range);
			    holder.head = (ImageView)convertView.findViewById(R.id.img_photo);
	            holder.name = (TextView)convertView.findViewById(R.id.text_name);
	            holder.sign = (TextView)convertView.findViewById(R.id.text_sign);
	            holder.distance = (TextView)convertView.findViewById(R.id.text_distance);
	            holder.time = (TextView)convertView.findViewById(R.id.text_time);
	            holder.score = (TextView)convertView.findViewById(R.id.text_score);
	            holder.fire = (ImageView)convertView.findViewById(R.id.img_fire);
	            convertView.setTag(holder); 
		} else {
			  holder = (ViewHolder) convertView.getTag();  
		}
		
		switch (position) {
		case 0:
			holder.range.setTextSize(14);
			holder.score.setTextSize(13);
			holder.range.setTextColor(mContext.getResources().getColor(R.color.look_around_range1));
			holder.score.setTextColor(mContext.getResources().getColor(R.color.look_around_range1));
			holder.fire.setBackgroundResource(R.drawable.icon_fire123);
			break;
			

		case 1:
			holder.range.setTextSize(14);
			holder.score.setTextSize(13);
			holder.range.setTextColor(mContext.getResources().getColor(R.color.look_around_range2));
			holder.score.setTextColor(mContext.getResources().getColor(R.color.look_around_range2));
			holder.fire.setBackgroundResource(R.drawable.icon_fire123);
			break;
			

		case 2:
			holder.range.setTextSize(14);
			holder.score.setTextSize(13);
			holder.range.setTextColor(mContext.getResources().getColor(R.color.look_around_range3));
			holder.score.setTextColor(mContext.getResources().getColor(R.color.look_around_range3));
			holder.fire.setBackgroundResource(R.drawable.icon_fire123);
			break;

		default:
			holder.range.setTextSize(13);
			holder.score.setTextSize(12);
			holder.range.setTextColor(mContext.getResources().getColor(R.color.look_around_range_normal));
			holder.score.setTextColor(mContext.getResources().getColor(R.color.look_around_range_normal));
			holder.fire.setBackgroundResource(R.drawable.icon_fire_4);
			break;
		}
		
		AfProfileInfo profile = mList.get(position);
		
		holder.range.setText(String.valueOf(position + 1));
		
		holder.score.setText(String.valueOf(profile.dating.charm_level));
		holder.name.setText(profile.name);
		
		
		String signStr = profile.signature == null ? mContext.getString(R.string.default_status) : profile.signature;
		CharSequence realSign = EmojiParser.getInstance(mContext).parse(signStr, ImageUtil.dip2px(mContext, 13));
		holder.sign.setText(realSign);
		holder.age.setText(String.valueOf(profile.age));
		holder.age.setBackgroundResource(Consts.AFMOBI_SEX_MALE ==profile.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
		holder.age.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE ==profile.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl,0,0,0);
		
		holder.range.setTypeface(mFontFace);
		holder.score.setTypeface(mFontFace);
		
		//WXL 20151015 调UIL的显示头像方法,统一图片管理
		ImageManager.getInstance().DisplayAvatarImage(holder.head, profile.getServerUrl(),
				profile.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,  profile.sex,profile.getSerialFromHead(),null); 	 
		
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView age;
		TextView range;
		ImageView head;
		TextView name;
		TextView sign;
		TextView distance;
		TextView time;
		TextView score;
		ImageView fire;
		
	}

}
