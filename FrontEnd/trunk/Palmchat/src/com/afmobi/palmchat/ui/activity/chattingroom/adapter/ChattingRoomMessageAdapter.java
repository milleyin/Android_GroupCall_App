package com.afmobi.palmchat.ui.activity.chattingroom.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomMainFragment;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnResendDialogListener;
import com.afmobi.palmchat.ui.customview.AppDialog.OnSelectButtonDialogListener;
import com.afmobi.palmchat.ui.customview.MyTextView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class ChattingRoomMessageAdapter extends BaseAdapter{
	
	private ArrayList<AfMessageInfo> listMsgs;
    private Context mContext;
    private LayoutInflater mInflater;
    private AfProfileInfo mAfProfileInfo;
	private String mAtMePrefix;
	private String mChatMsg;
	private  int index;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	/**
	 * 是否已经显示Menu
	 */
	boolean isShowMenu;
    public ChattingRoomMessageAdapter(Context context, ArrayList<AfMessageInfo> list) {
		mContext = context;
		this.listMsgs = list;
		mInflater = LayoutInflater.from(context);
		mAfProfileInfo = CacheManager.getInstance().getMyProfile();
		mAtMePrefix = "@" + mAfProfileInfo.name + ":";
	}
    
    private OnResendDialogListener onResendDialogListener;
    
	public void setOnResendDialogListener(
			OnResendDialogListener onResendDialogListener) {
		this.onResendDialogListener = onResendDialogListener;
	}

	public void setList(ArrayList<AfMessageInfo> list) {
		this.listMsgs = list;
	}

    public ArrayList<AfMessageInfo> getLists() {
		return listMsgs;
	}


    public int getCount() {
        return listMsgs.size();
    }

    public AfMessageInfo getItem(int position) {
        return listMsgs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    OnItemLongClick onItemLongClick;
    public void setOnItemClick(OnItemLongClick onItemLongClick){
    	this.onItemLongClick = onItemLongClick;
    }
    
    public OnItemLongClick getOnItemClick(){
    	return onItemLongClick;
    }
    
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AfMessageInfo entity = listMsgs.get(position);
    	//信息类型   AfMessageInfo.MESSAGE_TYPE_MASK & entity.type  (//信息来源  AfMessageInfo.MESSAGE_TYPE_MASK_GRP & entity.type != 0)
    	final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & entity.type;
    	final int status = entity.status;	
    	
    	final String name = entity.name;
    	final int age = entity.age;
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_chatting_room_text_hd, null);
			//receive message
			viewHolder.vRelativelayoutFromText = (RelativeLayout) convertView.findViewById(R.id.relativelayout_from_text);
			viewHolder.vTextViewSex = (TextView)convertView.findViewById(R.id.text_age);
			viewHolder.vTextViewFromName = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.vMyTextViewFromMsg = (MyTextView)convertView.findViewById(R.id.msg);
			
			//send message
			viewHolder.vRelativeLayoutToText = (RelativeLayout) convertView.findViewById(R.id.chatting_to_layout);
			viewHolder.vMyTextViewToMsg = (MyTextView)convertView.findViewById(R.id.msg2);
			viewHolder.vImageViewToStatusImg = (ImageView)convertView.findViewById(R.id.sending_status_text_img);
			viewHolder.vImageViewToStatus = (ProgressBar)convertView.findViewById(R.id.img_sending_status);

			//relativelayout_response
			viewHolder.vRelativeLayoutResponse = (RelativeLayout) convertView.findViewById(R.id.relativelayout_response);
			viewHolder.vTextViewShow = (TextView)convertView.findViewById(R.id.tv_show);
			viewHolder.vTextViewShow2 = (TextView)convertView.findViewById(R.id.tv_show2);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}


		if (MessagesUtils.isReceivedMessage(status)){//收到消息
			PalmchatLogUtils.println("ChattingRoomMessageAdapter  position  "+position+" getView  "+entity.client_time);
			switch (msgType) {
			case AfMessageInfo.MESSAGE_CHATROOM_NORMAL:
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD:
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT:
				viewHolder.vTextViewFromName.setText(entity.name);
				if(msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD || msgType ==  AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT){
					mChatMsg = entity.msg;
					if(TextUtils.isEmpty(mChatMsg)){//置顶消息为空的话 就设置为空格
						mChatMsg=" ";
					}else if(mChatMsg.startsWith("@")){
						index = mChatMsg.indexOf(":");
						if(index > 1) {
							mAtMePrefix = mChatMsg.substring(0, index + 1);
							mChatMsg = mChatMsg.substring(index + 1, mChatMsg.length());
						}
					}
					CharSequence text = new SpannableStringBuilder(Html.fromHtml("<font color='#ff554c'>" + mAtMePrefix + "</FONT>")).append(EmojiParser.getInstance(mContext).parse(mChatMsg));
					viewHolder.vMyTextViewFromMsg.setText(text);
				}else{
					CharSequence text = EmojiParser.getInstance(mContext).parse(entity.msg);
					viewHolder.vMyTextViewFromMsg.setText(text);
				}
				CommonUtils.setUrlSpanLink(mContext,viewHolder.vMyTextViewFromMsg);
				viewHolder.vMyTextViewFromMsg.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						showLongClickItem(status, msgType, entity, v, position);
						return true;
					}
				});
				viewHolder.vMyTextViewFromMsg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (onItemLongClick != null) {
							onItemLongClick.onItemClick(ChattingRoomMainFragment.ACTION_LONG_CLICK, entity.name, entity.fromAfId, entity.sex);
						}
					}
				});
				viewHolder.vTextViewFromName.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						toProfileForAfid(entity.fromAfId);

					}
				});
				viewHolder.vTextViewFromName.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if (onItemLongClick != null) {
							onItemLongClick.onItemClick(ChattingRoomMainFragment.ACTION_LONG_CLICK, entity.name, entity.fromAfId, entity.sex);
						}
						return true;
					}
				});
				viewHolder.vTextViewSex.setText(entity.age + "");
				viewHolder.vTextViewSex.setBackgroundResource(Consts.AFMOBI_SEX_MALE == entity.sex ? R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
				viewHolder.vTextViewSex.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == entity.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl, 0, 0, 0);
				PalmchatLogUtils.println("AfMessageInfo.MESSAGE_CHATROOM_NORMAL");
				break;
				
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_TOP:
				PalmchatLogUtils.println("AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_TOP");
				break;
				
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ENTRY:
				PalmchatLogUtils.println("AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ENTRY");
				String str = CommonUtils.replace(ReplaceConstant.TARGET_NMAE, name, mContext.getString(R.string.enter_room));
				str = CommonUtils.replace(ReplaceConstant.TARGET_AGE, age+"", str);
				showColorText(viewHolder.vTextViewShow, str);
				viewHolder.vTextViewShow2.setText("");
				viewHolder.vTextViewShow2.setVisibility(View.GONE);
				break;
			}
		}else{//发送消息
			/* AfProfileInfo myProfileInfo = CacheManager.getInstance().getMyProfile();
			 if (null != myProfileInfo) {
					//WXL 20151015 调UIL的显示头像方法,统一图片管理
					ImageManager.getInstance().DisplayAvatarImage(viewHolder.vImageViewMyPhoto, myProfileInfo.getServerUrl(),
							myProfileInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE, myProfileInfo.sex,myProfileInfo.getSerialFromHead(),null);
			 }*/
			mChatMsg = entity.msg;
			if(!TextUtils.isEmpty(entity.name)) {
				if (mChatMsg.startsWith(entity.name)) {
					mChatMsg = mChatMsg.substring(entity.name.length(), mChatMsg.length());
				}
				CharSequence text = new SpannableStringBuilder(Html.fromHtml("<font color='#ffec4c'>" + entity.name + "</FONT>")).append(EmojiParser.getInstance(mContext).parse(mChatMsg));
				viewHolder.vMyTextViewToMsg.setText(text);
			}
			else {
				CharSequence text = EmojiParser.getInstance(mContext).parse(mChatMsg);
				viewHolder.vMyTextViewToMsg.setText(text);
			}
			CommonUtils.setUrlSpanLink(mContext,viewHolder.vMyTextViewToMsg);
			viewHolder.vMyTextViewToMsg.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					showLongClickItem(status, msgType, entity, v, position);
					return true;
				}
			});
			//sending(entity.status ,viewHolder.vImageViewToStatus);
			showProgressBarOrNot(position, entity, msgType, status, viewHolder.vImageViewToStatusImg, viewHolder.vImageViewToStatus);
		}
		
		showOrDisappear(viewHolder,status,msgType);
		return convertView;
	}

	private void showProgressBarOrNot(final int position,final AfMessageInfo entity, final int msgType, final int status,ImageView img,ProgressBar progressBar) {
		if(AfMessageInfo.MESSAGE_SENTING == status){
			PalmchatLogUtils.println("position  "+position + "TIME  sending  "+dateFormat.format(new Date())+"  ");
			//showSend(viewHolder.imageViewSendingStautsImage);
			showProgressBar(img,progressBar);
		}else if(AfMessageInfo.MESSAGE_UNSENT == status){
			PalmchatLogUtils.println("position  "+position + "TIME  fail  "+dateFormat.format(new Date())+"  ");
			disappearProgressBar(img,progressBar);

			showSendFail(img, status, msgType, entity, position);

//								showSendFail(viewHolder.imageViewSendingStautsImage);
		}else{
			PalmchatLogUtils.println("position  "+position + "TIME  success  "+dateFormat.format(new Date())+"  ");
			disappearProgressBar(img,progressBar);
			showSendSuccessed(img);
		}
	}

	void showLongClickItem(int status ,int msgType,AfMessageInfo entity,View v,int position ){
		if(status == AfMessageInfo.MESSAGE_UNSENT){
			if (msgType == AfMessageInfo.MESSAGE_CHATROOM_NORMAL || msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD
					|| msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT) {
				longClick( mContext, R.array.chatroom_msg_op, entity , v , position);
			}
		}else{
			if(status == AfMessageInfo.MESSAGE_SENTING){//sendding
				return;
			}
			if (msgType == AfMessageInfo.MESSAGE_CHATROOM_NORMAL || msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD
					|| msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT) {
				longClick( mContext, R.array.chatroom_msg_op3, entity , v , position);
			}
		}
	}

	void showResendClickItem(int status ,int msgType,AfMessageInfo entity,View v,int position ){
		if(status == AfMessageInfo.MESSAGE_UNSENT){
			if (msgType == AfMessageInfo.MESSAGE_CHATROOM_NORMAL || msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD
					|| msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT) {
				longClick( mContext, R.array.chatroom_msg_op2, entity , v , position);
			}
		}
	}
	
	
	private void longClick(final Context context ,final int arrayId ,final AfMessageInfo afMessageInfo , final View v , final int position) {
		if(isShowMenu)
			return;
		isShowMenu = true;
		final String[] contentAry = context.getResources().getStringArray(arrayId);
		String title = context.getResources().getString(R.string.operate);
		
//		AppDialog appDialog = new AppDialog(mContext);
//		appDialog.createResend(mContext, entity, onResendDialogListener);
//		appDialog.show();
		
		AppDialog appDialog = new AppDialog(context);
		appDialog.createSelectBtnDialog(context, title, contentAry, new OnSelectButtonDialogListener() {
			@Override
			public void onCancelButtonClick(int selectIndex) {
				// 按照这种方式做删除操作，这个if内的代码有bug，实际代码中按需操作
				final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
				if (contentAry[selectIndex].equals(context.getResources().getString(R.string.resend))) {
					switch (msgType) {
					case AfMessageInfo.MESSAGE_CHATROOM_NORMAL:
					case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD:
						case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT:
						listMsgs.remove(position);
						afMessageInfo.action = MessagesUtils.ACTION_UPDATE;
						onResendDialogListener.onResendButtonClick(afMessageInfo);
						break;
					}
					
				} else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.copy))) {
					CommonUtils.copy((TextView) v,context);
				} else if (contentAry[selectIndex].equals(context.getResources().getString(R.string.delete))) {
					listMsgs.remove(afMessageInfo);
					notifyDataSetChanged();
					((GroupChatActivity) context).delete(afMessageInfo);
					
					if(msgType == AfMessageInfo.MESSAGE_VOICE && VoiceManager.getInstance().getPlayer().isPlaying()){
						VoiceManager.getInstance().pause();
					}
				}
			}
		});
		appDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isShowMenu = false;
			}
		});
		appDialog.show();
	}
	
	private void toProfileForAfid(final String afid) {
		AfProfileInfo myProfileInfo = CacheManager.getInstance().getMyProfile();
		if (afid.equals(myProfileInfo.afId)) {
			Bundle bundle = new Bundle();
			bundle.putString(JsonConstant.KEY_AFID, myProfileInfo.afId);
			bundle.putBoolean(JsonConstant.KEY_FROM_CHATTINGROOM_TO_PROFILE, true);
			Intent intent = new Intent(mContext, MyProfileActivity.class);
			intent.putExtras(bundle);
			mContext.startActivity(intent);
		} else {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CR_T_PF);
			
//			MobclickAgent.onEvent(mContext, ReadyConfigXML.CR_T_PF);
			
			Intent intent = new Intent(mContext, ProfileActivity.class);
			intent.putExtra(JsonConstant.KEY_FLAG, true);
			intent.putExtra(JsonConstant.KEY_AFID, afid);
			intent.putExtra(JsonConstant.KEY_FROM_CHATTINGROOM_TO_PROFILE, true);
			AfProfileInfo info = new AfProfileInfo();
			info.afId = afid;
			intent.putExtra(JsonConstant.KEY_PROFILE, info);
			
			mContext.startActivity(intent);
		}
	} 
	
	
	private void showColorText(final TextView tv_show,final String content) {
		if(tv_show != null){
//			tv_show.setText(Html.fromHtml(content));//
			tv_show.setText(content);
		}
		PalmchatLogUtils.e("showColorText", "tv_show  "+tv_show);
	}
	
	private void sending(int status ,View v) {
		if (v != null) {
			switch (status) {
			case AfMessageInfo.MESSAGE_UNSENT:
				//showSendFail(v);
				break;
			case AfMessageInfo.MESSAGE_SENT:
				showSendSuccessed(v);
				break;
			case AfMessageInfo.MESSAGE_SENTING:
				showSend(v);
				break;
//			case AfMessageInfo.MESSAGE_UNSENT:
//				showSendFail(v);
//				break;
			}
		}
		
	}
	
	 private void showProgressBar(ImageView imageViewSendingStautsImage,
				ProgressBar progressbarStatusImage) {
			// TODO Auto-generated method stub
			imageViewSendingStautsImage.setVisibility(View.GONE);
			progressbarStatusImage.setVisibility(View.VISIBLE);
		}
	    
	    private void disappearProgressBar(ImageView imageViewSendingStautsImage,
				ProgressBar progressbarStatusImage){
	    	imageViewSendingStautsImage.setVisibility(View.VISIBLE);
			progressbarStatusImage.setVisibility(View.GONE);
	    }
    
     Handler handler = new Handler();
	 void showSend(final View view) {
		view.setVisibility(View.VISIBLE);
		handler.post(new Runnable() {
			@Override
			public void run() {
				animDrawable = ((AnimationDrawable) mContext.getResources().getDrawable(R.anim.playing_from_voice_frame));
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
					view.setBackgroundDrawable(animDrawable);
				}else{
					view.setBackground(animDrawable);
				}
				if (animDrawable != null && !animDrawable.isRunning()) {
					animDrawable.setOneShot(false);
					animDrawable.start();
				}
			}
		});
	 }

	 private AnimationDrawable animDrawable;
	
	 private void showSendSuccessed(final View view) {
		view.setVisibility(View.GONE);
//		view.setBackgroundResource(R.drawable.msg_send_delivered);
	 }
		
	 private void showSendFail(final View view, final int status, final int msgType, final AfMessageInfo entity, final int position) {
			view.setVisibility(View.VISIBLE);
			view.setBackgroundResource(R.drawable.msg_fail_icon);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showResendClickItem(status, msgType, entity, view, position);
				}
			});
	}
		
	 private void showOrDisappear(ViewHolder viewHolder,int status,int msgType) {
    	if(MessagesUtils.isReceivedMessage(status)){//收到
    		switch (msgType) {
			case AfMessageInfo.MESSAGE_CHATROOM_NORMAL:
				viewHolder.vRelativelayoutFromText.setVisibility(View.VISIBLE);
	    		viewHolder.vRelativeLayoutToText.setVisibility(View.GONE);
	    		viewHolder.vRelativeLayoutResponse.setVisibility(View.GONE);
				break;
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD:
				case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT:
				viewHolder.vRelativelayoutFromText.setVisibility(View.VISIBLE);
	    		viewHolder.vRelativeLayoutToText.setVisibility(View.GONE);
	    		viewHolder.vRelativeLayoutResponse.setVisibility(View.GONE);
				break;
				
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_TOP:
				viewHolder.vRelativelayoutFromText.setVisibility(View.VISIBLE);
	    		viewHolder.vRelativeLayoutToText.setVisibility(View.GONE);
	    		viewHolder.vRelativeLayoutResponse.setVisibility(View.GONE);
				break;
				
			case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ENTRY:
				viewHolder.vRelativelayoutFromText.setVisibility(View.GONE);
	    		viewHolder.vRelativeLayoutToText.setVisibility(View.GONE);
	    		viewHolder.vRelativeLayoutResponse.setVisibility(View.VISIBLE);
				break;
			}
    	}else{
    		viewHolder.vRelativelayoutFromText.setVisibility(View.GONE);
    		viewHolder.vRelativeLayoutToText.setVisibility(View.VISIBLE);
    		viewHolder.vRelativeLayoutResponse.setVisibility(View.GONE);
    	}
	}
	
	static class ViewHolder{
		
		//receive message
		RelativeLayout vRelativelayoutFromText;//relativelayout_from_text
		TextView vTextViewSex;//imageview_sex_sign
		TextView vTextViewFromName;//tv_name
		MyTextView vMyTextViewFromMsg;//msg
		
		
		//send message
		RelativeLayout vRelativeLayoutToText;//chatting_to_layout
		
		MyTextView vMyTextViewToMsg;//msg2

		View linear_sending_status_text;
		ImageView vImageViewToStatusImg;//img_sending_status_img
		ProgressBar vImageViewToStatus;//img_sending_status

		//relativelayout_response
		RelativeLayout vRelativeLayoutResponse;
		TextView vTextViewShow;//tv_show
		TextView vTextViewShow2;//tv_show2
		
		
		
	}
}
