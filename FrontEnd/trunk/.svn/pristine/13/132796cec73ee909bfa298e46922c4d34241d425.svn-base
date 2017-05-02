package com.afmobi.palmchat.ui.activity.main.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.listener.FragmentSendFriendCallBack;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.groupchat.EditGroupActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.cb.RefreshStateListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.AccountsChattingActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.DateUtil;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
//import com.afmobi.palmchat.util.image.ImageLoader;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachPalmCoinSysInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfStoreProdList.AfProdProfile;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class MyGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<MainAfFriendInfo> list = new ArrayList<MainAfFriendInfo>();

	private LayoutInflater inflater;  
	RefreshStateListener listener;
	public MyGridViewAdapter(Context context ) {
		this.context = context;
//		setAdapterData(list);
		this.inflater = LayoutInflater.from(context);
	}
	
	public void setAdapterData(List<MainAfFriendInfo> list) {
		this.list.clear();
		if (null != list) {
			this.list.addAll(list);
		}
		
	}
	
	public void setOnFreshListener(RefreshStateListener listener) {
		this.listener = listener;
	}
	
	public void setIsAnim(boolean isAnim) {
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MainAfFriendInfo getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
			return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
		
				convertView = inflater.inflate(R.layout.item_mainfragment_listview,null);
				holder.vImageViewPhoto = (ImageView) convertView.findViewById(R.id.image_chats_photo);  
	            holder.vTextViewName = (TextView) convertView.findViewById(R.id.textview_chats_name);
	            holder.vTextViewLastMessage = (TextView)convertView.findViewById(R.id.textview_chats_msg);
	            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
	            holder.count = (TextView)convertView.findViewById(R.id.count);
	            holder.unread = (TextView)convertView.findViewById(R.id.unread);
	            holder.time = (TextView)convertView.findViewById(R.id.tv_time);
	            holder.linefault_view = convertView.findViewById(R.id.linefault_view);
	            holder.linefull_view =  convertView.findViewById(R.id.linefull_view);
			    holder.public_account_img = (ImageView) convertView.findViewById(R.id.public_account_img);
//			group head 
			holder.group_heads = (RelativeLayout) convertView.findViewById(R.id.group_heads);
			holder.group_head_1 = ( ImageView) convertView.findViewById(R.id.group_head_1);
			holder.group_head_2 = ( ImageView) convertView.findViewById(R.id.group_head_2);
			holder.group_head_3 = ( ImageView) convertView.findViewById(R.id.group_head_3);
			holder.img_group_of_lord = (ImageView) convertView.findViewById(R.id.img_group_of_lord);
			holder.viewParent = convertView.findViewById(R.id.viewparent);  
			holder.noChats = convertView.findViewById(R.id.nochat);  
			
		/*	holder.group_head_1.setBorderColor(Color.WHITE);
			holder.group_head_2.setBorderColor(Color.WHITE);
			holder.group_head_3.setBorderColor(Color.WHITE);*/
            
            convertView.setTag(holder);  
             
        } else {  
        	
            holder = (ViewHolder) convertView.getTag();  
        }
			if (position == list.size() - 1) {

				holder.viewParent.setVisibility(View.GONE);
				holder.time.setVisibility(View.GONE);
				holder.noChats.setVisibility(View.VISIBLE);
				holder.linefull_view.setVisibility(View.GONE);
				//convertView.setOnClickListener(null);
				//convertView.setOnLongClickListener(null);

				return convertView;
			}
		
		holder.viewParent.setVisibility(View.VISIBLE);
		holder.time.setVisibility(View.VISIBLE);
		holder.noChats.setVisibility(View.GONE);
		holder.public_account_img.setVisibility(View.GONE);
//		recent chat list底层已排好序，正常显示即可
		final MainAfFriendInfo aff = (MainAfFriendInfo)list.get(position);
//		私聊
		if(MessagesUtils.isPrivateMessage(aff.afMsgInfo.type) || MessagesUtils.isSystemMessage(aff.afMsgInfo.type)) {
			if(MessagesUtils.isSystemMessage(aff.afMsgInfo.type))
			{
				holder.public_account_img.setVisibility(View.VISIBLE);
			}
			else{
				holder.public_account_img.setVisibility(View.GONE);
			}
		final AfFriendInfo affFriendInfo = aff.afFriendInfo;
		
		holder.vImageViewPhoto.setVisibility(View.VISIBLE);
		holder.group_heads.setVisibility(View.GONE);
		holder.img_group_of_lord.setVisibility(View.GONE);

		if (affFriendInfo != null) {
//			 if (!CommonUtils.showHeadImage(affFriendInfo.afId, holder.vImageViewPhoto, affFriendInfo.sex)) {
				//调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
				ImageManager.getInstance().DisplayAvatarImage(holder.vImageViewPhoto, affFriendInfo.getServerUrl(),
						affFriendInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE,affFriendInfo.sex,affFriendInfo.getSerialFromHead(),null);
//			}
		}
		
        holder.vTextViewName.setText(CommonUtils.getRealDisplayName(affFriendInfo));
        
        
        holder.vImageViewPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			/*	if(!isShowNoReadCount) //发送朋友分享广播时，不需要此功能
					return;*/
//				palmchat team
				if (affFriendInfo != null && affFriendInfo.afId != null && affFriendInfo.afId.startsWith("r")) {
					if(!affFriendInfo.unfollow_opr) {
						if (!CommonUtils.isSystemAccount(affFriendInfo.afId)) {
							toPublicAccountDetail(affFriendInfo);
						}
					}else { //不能unfollow的账号进入聊天页面 如paymentservice
						toAccountsChatting(affFriendInfo);
					}

				} else if (affFriendInfo != null && affFriendInfo.afId != null) {
					toProfile(affFriendInfo);
				}
				
			}
		});

//		  群聊
		} else if(MessagesUtils.isGroupChatMessage(aff.afMsgInfo.type)) {
			
			final AfGrpProfileInfo afGrpInfo = aff.afGrpInfo;
			
			if (null != afGrpInfo) {
				holder.vTextViewName.setText(afGrpInfo.name);
				ImageView[] imageviews = {holder.group_head_1,holder.group_head_2,holder.group_head_3};
				setGroupAvatar(imageviews, afGrpInfo, holder);
			}
			
			holder.vImageViewPhoto.setVisibility(View.GONE);
			holder.group_heads.setVisibility(View.VISIBLE);
			if (aff.afGrpInfo != null) {
				 AfProfileInfo myProfile= CacheManager.getInstance().getMyProfile();
				if(myProfile!=null&&myProfile.afId!=null&&aff!=null&&aff.afGrpInfo!=null&&myProfile.afId.equals(aff.afGrpInfo.admin)){
//				holder.group_heads.setBackgroundResource(R.drawable.groupbg_add);
					holder.img_group_of_lord.setVisibility(View.VISIBLE);
				}else{
//				holder.group_heads.setBackgroundResource(R.drawable.groupbg_addother);
					holder.img_group_of_lord.setVisibility(View.GONE);
				}
			}
			
			holder.group_heads.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				/*	if(!isShowNoReadCount) //发送朋友分享广播时，不需要此功能
						return;*/
					if (Consts.AFMOBI_GRP_STATUS_EXIT == afGrpInfo.status) {
						return;
					}
					Intent intent = new Intent();
					intent.setClass(context, EditGroupActivity.class);
					Bundle b = new Bundle();
					b.putString("come_page", "multichat_page");
					b.putString(GroupChatActivity.BundleKeys.ROOM_ID, afGrpInfo.afid);
					if (!StringUtil.isEmpty(afGrpInfo.name, true)) {
						b.putString(GroupChatActivity.BundleKeys.ROOM_NAME, afGrpInfo.name);
					} else {
						b.putString(GroupChatActivity.BundleKeys.ROOM_NAME, afGrpInfo.afid);
					}
					intent.putExtras(b);
					try {
						context.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			
		/*	//click item
			holder.viewParent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
						AfPalmchat mAfCorePalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;

						if (aff.afMsgInfo != null) {
//						纠正chats最近一条消息显示不准确问题
							AfMessageInfo[] recentDataArray = mAfCorePalmchat.AfDbRecentMsgGetRecord(AfMessageInfo.MESSAGE_TYPE_MASK_GRP, aff.afMsgInfo.getKey(), 0, 1);
							if (null != recentDataArray && recentDataArray.length > 0) {
								aff.afMsgInfo = recentDataArray[0];
								PalmchatLogUtils.println("--- ddd MyGridViewAdapter-- recentDataArray:" + recentDataArray[0].msg);
							}


//					进入聊天界面，将此会话的未读数设为0
							mAfCorePalmchat.AfRecentMsgSetUnread(aff.afMsgInfo.getKey(), 0);
							aff.afMsgInfo.unReadNum = 0;
							//【步骤】账号A、B互为好友； 账号A操作：创建一个群组 且B为群成员，创群成功后不修改群名称，直接在群组group1中发消息 10条--->账号B显示未读消息数10. 然后账号A修改该群名称， 再发一条消息 【结果】B账号的未读消息数变成1.（只显示修改群名称之后的未读消息数了）
							CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).update(aff,false,true);
						}
						if(aff != null) {
							toGroupChat(aff.afGrpInfo);
						}

						CommonUtils.cancelNoticefacation(context.getApplicationContext());
				}
			});*/
		}
		
        if(aff.afMsgInfo != null) {
			CharSequence text = EmojiParser.getInstance(context).parse(chatsDisplayMsg(aff.afMsgInfo), ImageUtil.dip2px(context, 13));
			holder.vTextViewLastMessage.setText(text);

//        holder.time.setText(dateFormat.format(new Date(aff.afMsgInfo.client_time)));//(CommonUtils.getRealChatDate(context, System.currentTimeMillis(), aff.afMsgInfo.client_time));
			holder.time.setText(DateUtil.dateChangeStrForChats(new Date(aff.afMsgInfo.client_time), PalmchatApp.getApplication()));
			holder.count.setVisibility(View.GONE);
//        未读消息数
			if (aff.afMsgInfo.unReadNum > 0) {
				holder.unread.setVisibility(View.VISIBLE);
				holder.unread.setText(aff.afMsgInfo.unReadNum > 99 ? "99+" : aff.afMsgInfo.unReadNum + DefaultValueConstant.EMPTY);
			} else {

				holder.unread.setVisibility(View.GONE);
				holder.unread.setText(DefaultValueConstant.EMPTY);

			}

			int status = aff.afMsgInfo.status;
//      sent msg
			if (!MessagesUtils.isReceivedMessage(status)) {
				switch (status) {
					case AfMessageInfo.MESSAGE_SENT:
					case AfMessageInfo.MESSAGE_SENT_AND_READ:
						holder.count.setVisibility(View.GONE);
//  			holder.count.setBackgroundResource(R.drawable.msgico_none);
						break;

					case AfMessageInfo.MESSAGE_SENTING:
						holder.count.setVisibility(View.VISIBLE);
						holder.count.setBackgroundResource(R.drawable.msgico_sending);
						break;

					case AfMessageInfo.MESSAGE_UNSENT:
						holder.count.setVisibility(View.VISIBLE);
						holder.count.setBackgroundResource(R.drawable.msgico_failed);
						break;

					default:
						holder.count.setVisibility(View.GONE);
						break;
				}
			}
		}
        bintSetLine(holder, position);
		return convertView;
		
	}
	
	
	private void setGroupAvatar(ImageView[] imageviews, AfGrpProfileInfo infos, ViewHolder holder) {
		
		holder.group_head_1.setImageResource(R.drawable.head_male2);
		holder.group_head_2.setImageResource(R.drawable.head_male2);
		holder.group_head_3.setImageResource(R.drawable.head_male2);
//		holder.group_head_4.setImageResource(R.drawable.head_male);
		
		ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
		imageViews.add(holder.group_head_1);
		imageViews.add(holder.group_head_2);
		imageViews.add(holder.group_head_3);
//		imageViews.add(holder.group_head_4);
		
//		ImageLoader.getInstance().displayImage(imageViews, infos);
		ImageManager.getInstance().displayGroupHeadImage(imageViews, infos);
	}
	
	

	
	private void toProfile(final AfFriendInfo afFriendInfo) {
		Intent intent = new Intent(context, ProfileActivity.class);
		AfProfileInfo info =AfFriendInfo.friendToProfile(afFriendInfo);
		intent.putExtra(JsonConstant.KEY_PROFILE, info);
		//请求新的profile资料
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		intent.putExtra(JsonConstant.KEY_AFID, info.afId);
		intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
		intent.putExtra(JsonConstant.KEY_FROM_XX_PROFILE, ProfileActivity.CHAT_TO_PROFILE);
		context.startActivity(intent);
	}


	private void toAccountsChatting(AfFriendInfo affFriendInfo) {
		Intent intent = new Intent();
	    intent.setClass(context,AccountsChattingActivity.class);
	  	intent.putExtra(JsonConstant.KEY_FROM_UUID, affFriendInfo.afId);
		intent.putExtra(JsonConstant.KEY_FROM_NAME, affFriendInfo.name);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, affFriendInfo.alias);
		intent.putExtra(JsonConstant.KEY_FRIEND, affFriendInfo);
	    context.startActivity(intent);
	}

	private String chatsDisplayMsg(AfMessageInfo msg) {
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & msg.type;

		String displayMsg = null;

		switch (msgType) {
		case AfMessageInfo.MESSAGE_TEXT:
			displayMsg = msg.msg;
			break;
		case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
			displayMsg = context.getString(R.string.msg_custom_emoticons);

			break;
		case AfMessageInfo.MESSAGE_VOICE:
			displayMsg = context.getString(R.string.msg_voice);
			break;

		case AfMessageInfo.MESSAGE_EMOTIONS:
			displayMsg = context.getString(R.string.msg_custom_emoticons);
			break;
		case AfMessageInfo.MESSAGE_IMAGE:
			displayMsg = context.getString(R.string.msg_image);
			break;

		case AfMessageInfo.MESSAGE_CARD:
			displayMsg = context.getString(R.string.name_card);
			break;
		case AfMessageInfo.MESSAGE_BROADCAST_SHARE_TAG:
			displayMsg = msg.msg;
			break;
		case AfMessageInfo.MESSAGE_BROADCAST_SHARE_BRMSG:
			displayMsg = context.getString(R.string.msg_share_broadcast);
			break;
		case AfMessageInfo.MESSAGE_FLOWER:
			displayMsg = context.getString(R.string.sent_flower_msg).replace("XXXX", msg.msg);
			break;
		case AfMessageInfo.MESSAGE_PAY_SYS_NOTIFY: // zhh,B31消息
			/**zhh 之前纠正最近一条消息显示不正确引发的问题，导致列表消息被覆盖显示不正确，暂时弥补*/
			AfAttachPalmCoinSysInfo afAttachPalmCoinSysInfo = (AfAttachPalmCoinSysInfo) (msg.attach);
			if (afAttachPalmCoinSysInfo != null) { // coin余额有改变
				displayMsg = afAttachPalmCoinSysInfo.header;
			}else
				displayMsg = msg.msg;
			break;

		default:
			displayMsg = msg.msg;
			break;
		}

		return displayMsg;
	}
	
	private class ViewHolder {
		ImageView vImageViewPhoto;
		TextView vTextViewName,vTextViewLastMessage;  
		View viewParent;
		View noChats;
		
		ProgressBar progress;
		
		
		TextView time;
		
		/**
		 * 气泡
		 */
		TextView count;
		
		/**
		 * 未读数
		 */
		TextView unread;
		
		
//		群头像
		public View group_heads;

		/**
		 * 小头像01
		 */
		public  ImageView group_head_1;

		/**
		 * 小头像02
		 */
		public  ImageView group_head_2;

		/**
		 * 小头像03
		 */
		public  ImageView group_head_3;

		/**
		 * 小头像04
		 */
//		public ImageView group_head_4;

		/**
		 * 群主标识
		 */
		public ImageView img_group_of_lord;

		/**
		 * 公众帐号图标
		 */
		public ImageView public_account_img;
		View linefull_view;
		View linefault_view;
	}
	
	public void bintSetLine(ViewHolder viewHolder, int position){
		
		if (position == (list.size() - 2)) {
			viewHolder.linefault_view.setVisibility(View.GONE);
			viewHolder.linefull_view.setVisibility(View.VISIBLE);
		}else {
			viewHolder.linefault_view.setVisibility(View.VISIBLE);
			viewHolder.linefull_view.setVisibility(View.GONE);
		}
	}
	
	private void toPublicAccountDetail(
			final AfFriendInfo afFriendInfo) {
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_PBL_PF);
//		MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_PBL_PF);
		Intent intent = new Intent(context, PublicAccountDetailsActivity.class);
		intent.putExtra("Info", afFriendInfo);
		context.startActivity(intent);
	}
}
