package com.afmobi.palmchat.ui.activity.social;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 未读已读广播点赞或评论 列表适配器
 * @author Transsion
 *
 */
public class BrdNotificationfAdapter extends BaseAdapter {
	
	/**上下文*/
	private Context mContext;
	/**布局加载器*/
	private LayoutInflater inflater;
	/**显示数据列表*/
	private ArrayList<BroadcastNotificationData> mAdapterList = new ArrayList<BroadcastNotificationData>();
    @SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**未读条数*/
    private int mUnReadTotal;
    /**当前显示列表*/
	private HashMap<Integer, Boolean> mPositionClickMap = new HashMap<Integer, Boolean>();
	public BrdNotificationfAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
	}
	
	/**
	 * 设置数据列表
	 * @param list
	 */
	public void setAdapterList(ArrayList<BroadcastNotificationData> list,int unReadTotal) {
		if (list != null) {
			mUnReadTotal = unReadTotal;
			mAdapterList.clear();
			mAdapterList.addAll(list);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mAdapterList?0:mAdapterList.size();
	}

	@Override
	public BroadcastNotificationData getItem(int position) {
		// TODO Auto-generated method stub
		return mAdapterList.get(getRealPosition(position));
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.brd_notification_item, null);
			holder = new ViewHolder(); 
			
			holder.textview_name = (TextView)convertView.findViewById(R.id.text_name );
			holder.imgview_type = (ImageView)convertView.findViewById(R.id.imageView_type);

			holder.imgview_like = (ImageView)convertView.findViewById(R.id.img_like);
			
			holder.original_type = (RelativeLayout)convertView.findViewById(R.id.layout_right);
			
			holder.textview_original_content = (TextView)convertView.findViewById(R.id.textView_original_content);
			
			holder.content = (TextView)convertView.findViewById(R.id.text_content);
			holder.time = (TextView)convertView.findViewById(R.id.text_date);
			holder.head = (ImageView)convertView.findViewById(R.id.img_photo);
			holder.linefault_view = convertView.findViewById(R.id.linefault_view);
			holder.linefull_view =  convertView.findViewById(R.id.linefull_view);
			holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.brd_notification_layout);
			convertView.setTag(holder); 
		} else {
			holder = (ViewHolder) convertView.getTag();  
		}
		
		bintSetLine(holder, position);
		if(position<mUnReadTotal) {
			if(mPositionClickMap.containsKey(position)){
				holder.relativeLayout.setBackgroundResource(R.drawable.uilist);
			} else {
				holder.relativeLayout.setBackgroundResource(R.drawable.brd_notification_unread_item_selector);
			}
			
		} else {
			holder.relativeLayout.setBackgroundResource(R.drawable.uilist);
		}
		final BroadcastNotificationData itemInfo = mAdapterList.get(getRealPosition(position));

		String name = TextUtils.isEmpty(itemInfo.name) ? itemInfo.afid.replace("a", "") : itemInfo.name;
		if (itemInfo.isAggregate) {//如果是聚合的消息，如大于10条合并到一起的
			holder.content.setText(itemInfo.displayContent);
			holder.textview_name.setText(name);
			holder.imgview_like.setVisibility(View.GONE);
			holder.original_type .setVisibility(View.GONE);	
			
		} else {
			holder.textview_name.setText(name);
			//如果是like类型，是没有带 original_content和original_type
			if (itemInfo.type == BroadcastNotificationData.ITEM_TYPE_LIKE) {
				holder.imgview_like.setVisibility(View.VISIBLE);
			} else {
				holder.imgview_like.setVisibility(View.GONE);
			}
			CharSequence text = EmojiParser.getInstance(mContext).parse(itemInfo.msg);
			holder.content.setText(text);
			if (!TextUtils.isEmpty(itemInfo.original_content)) {//如果原文不为空的 就显示原文
				holder.original_type.setVisibility(View.VISIBLE);
				holder.textview_original_content.setVisibility(View.VISIBLE);
				holder.textview_original_content.setText(EmojiParser.getInstance(mContext).parse(itemInfo.original_content));
					if(Build.VERSION.SDK_INT == 23){//6.0系统textview显示表情会显示不完整
						String contentText = holder.textview_original_content.getText().toString();
						PalmchatLogUtils.i("BrdNotificationfAdapter","contenttext:"+contentText);
						boolean lean = isString(contentText);//判断是否有表情显示
						boolean bool = false;
						boolean chinese = isChinese(contentText);//判断是否是中文字符
						int mantissa = 30;
						if(chinese){//如果是中文的话，截取字数就除于2
							mantissa = mantissa/2;
						}
						if(contentText.length()>mantissa){
							String result = contentText.substring(0,mantissa);
							 bool = isString(result);//判断截取的是不是纯文本信息。
						}
						if(!(lean && bool)){//判断文本是否有表情，有就不进去设置字符显示。
							PalmchatLogUtils.i("BrdNotificationfAdapter===","contenttext:"+contentText);
							holder.textview_original_content.setEms(8);
							holder.textview_original_content.setEllipsize(TextUtils.TruncateAt.END);
							holder.textview_original_content.setGravity(Gravity.CENTER);
							holder.textview_original_content.setText(EmojiParser.getInstance(mContext).parse(contentText));
						}else{//有表情和文本显示的
							holder.textview_original_content.setGravity(Gravity.LEFT);
						}
					}else{
						holder.textview_original_content.setEms(8);
						holder.textview_original_content.setGravity(Gravity.CENTER);
						holder.textview_original_content.setEllipsize(TextUtils.TruncateAt.END);
					}
				holder.imgview_type.setVisibility(View.GONE);
			} else {
				holder.textview_original_content.setVisibility(View.GONE);
				if (itemInfo.original_type == BroadcastNotificationData.ORIGINAL_TYPE_IMG_TEXT ||
						itemInfo.original_type == BroadcastNotificationData.ORIGINAL_TYPE_IMG) {//如果是图片类
					holder.imgview_type.setVisibility(View.VISIBLE);
					holder.imgview_type.setImageResource(R.drawable.ic_pic_broadcast_notifcation);
				} else if (itemInfo.original_type == BroadcastNotificationData.ORIGINAL_TYPE_VOICE_TEXT ||
						itemInfo.original_type == BroadcastNotificationData.ORIGINAL_TYPE_VOICE) {//如果是语音类
					holder.imgview_type.setVisibility(View.VISIBLE);
					holder.imgview_type.setImageResource(R.drawable.ic_voice_broadcast_notifcation);
				}
			}
		}
		if(DefaultValueConstant.BROADCAST_PROFILE_PA== itemInfo.user_class){
			holder.textview_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_broadcast_list_comment_public_account, 0, 0, 0);
			holder.textview_name.setCompoundDrawablePadding(10);
		}else{
			holder.textview_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); 
		}
		String time = (dateFormat.format(new Date(itemInfo.ts*1000)));
	/*	if(null!=time){
			String and = DateUtil.getTimeMonth(mContext,time);
			if(null!=and){
				holder.time.setText(and);
			} else {
				holder.time.setText("");
			}
		}*/
		holder.time.setText(DateUtil.getTimeAgo( mContext,time));
		//WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
		 ImageManager.getInstance().DisplayAvatarImage(holder.head, itemInfo.getServerUrl()
				  ,itemInfo.getAfidFromHead() ,Consts.AF_HEAD_MIDDLE, itemInfo.sex,itemInfo.getSerialFromHead(),null);  
		
		holder.head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//			    onclick
				AfProfileInfo profile = BroadcastNotificationData.PeopleInfoToBrdNotifyData(itemInfo);
				if(DefaultValueConstant.BROADCAST_PROFILE_PA==itemInfo.user_class){
					Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
					intent.putExtra("Info", profile);
					mContext.startActivity(intent); 
				}else if (CacheManager.getInstance().getMyProfile().afId.equals(itemInfo.afid)) {
				 	Bundle bundle = new Bundle();
					bundle.putString(JsonConstant.KEY_AFID, itemInfo.afid);
					Intent intent = new Intent(mContext, MyProfileActivity.class);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
					 
				} else {
					 
					Intent intent = new Intent(mContext, ProfileActivity.class);
				
					intent.putExtra(JsonConstant.KEY_PROFILE, (Serializable) profile);
					intent.putExtra(JsonConstant.KEY_FLAG, true);
					intent.putExtra(JsonConstant.KEY_AFID, itemInfo.afid);
					mContext.startActivity(intent);
				}
			}
		});
		
		return convertView;

	}

	/**
	 * 判断是否有表情
	 * @param s
	 * @return
     */
	public boolean isString(String s){
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(s);
		return matcher.find();
	}

	/**
	 * 判断是否有中文
	 * @param s
	 * @return
     */
	public boolean isChinese(String s){
		Pattern pattern = Pattern.compile("[^\\x00-\\xff]");
		Matcher matcher = pattern.matcher(s);
		return  matcher.find();
	}
	
	private void bintSetLine(ViewHolder viewHolder, int position){
		if (position == (getCount()-1)) {

			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);

		} else {
	
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);

		}

	}
	
	/**
	 * 获取真正的位置
	 */
	private int getRealPosition(int position) {
		int realPosition = 0;
		if(position<mUnReadTotal) {
			realPosition = mUnReadTotal-position-1;
		}
		else {
			realPosition = mAdapterList.size()-1-(position-mUnReadTotal);
		}
		return realPosition;
	}
	
	/**
	 * 局部刷新
	 */
	public void updateItemView(int position,ListView listView) {
		ViewGroup view = (ViewGroup) listView.getChildAt(position - listView.getFirstVisiblePosition());
		mPositionClickMap.put(position, true);
		if(null!=view) {
			RelativeLayout relayout = (RelativeLayout)(view.findViewById(R.id.brd_notification_layout));
			relayout.setBackgroundResource(R.drawable.uilist);
		}
	}

	/**
	 * 
	 * @author Transsion
	 *
	 */
	private class ViewHolder {
		/**名字*/
		TextView textview_name;
		/**原文字内容*/
		TextView textview_original_content;
		/**图片*/
		ImageView imgview_type;
		/**评论内容*/
		TextView content;
		/**时间*/
		TextView time;
		/**头像*/
		ImageView head;
		/***/
		TextView name;
		View linefault_view;
		View linefull_view;
		
		RelativeLayout original_type;
		/**点赞*/
		ImageView imgview_like;
		/**Item跟布局*/
		RelativeLayout relativeLayout;
	}

}
