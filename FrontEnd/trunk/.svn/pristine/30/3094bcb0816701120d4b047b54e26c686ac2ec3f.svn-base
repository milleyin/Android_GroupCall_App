package com.afmobi.palmchat.ui.activity.chattingroom.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomMainFragment;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.util.DialogUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StringUtil;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfChatroomMemberInfo;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;


public class ChattingRoomPersonAdapter extends BaseAdapter implements AfHttpResultListener {
	private Context mContext;
	private List<AfChatroomMemberInfo> mList;
	private int type;
	
	
	public static final int LAST_ENTRY = 1;
	public static final int COMMON_ENTRY = 2;
	public void setType(int type) {
		this.type = type;
	}

	public List<AfChatroomMemberInfo> getList() {
		return mList;
	}

	private LayoutInflater inflater;  
	private AfPalmchat mAfCorePalmchat;
	private DialogUtils mDialog;

	OnItemLongClick onItemLongClick;
    public void setOnItemClick(OnItemLongClick onItemLongClick){
    	this.onItemLongClick = onItemLongClick;
    }
    
    public OnItemLongClick getOnItemClick(){
    	return onItemLongClick;
    }
    
	public ChattingRoomPersonAdapter(Context context,List<AfChatroomMemberInfo> list){
		this.mContext = context;
		mList = list;
		inflater = LayoutInflater.from(context);
		mDialog = new DialogUtils();
		mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;
	}
	
	private void showDialog(Context context) {
		if (context instanceof BaseFragmentActivity) {
			BaseFragmentActivity activity = (BaseFragmentActivity)context;
			activity.showProgressDialog(R.string.Sending);
		} else if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity)context;
			activity.showProgressDialog(R.string.Sending);
		} else {
			mDialog.showCustomProgressDialog(context, context.getString(R.string.Sending));
		}
	}
	
	private void dismissDialog(Context context) {
		if (context instanceof BaseFragmentActivity) {
			BaseFragmentActivity activity = (BaseFragmentActivity)context;
			activity.dismissProgressDialog();
		} else if (context instanceof BaseActivity) {
			BaseActivity activity = (BaseActivity)context;
			activity.dismissProgressDialog();
		} else {
			mDialog.dismissCustomProgressDialog();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public AfChatroomMemberInfo getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_chatting_room_person_list, null);
            holder = new ViewHolder();  
            holder.imageView = (ImageView)convertView.findViewById(R.id.icon);
            holder.name = (TextView)convertView.findViewById(R.id.title);
            holder.sign = (TextView)convertView.findViewById(R.id.content);
            holder.age = (TextView)convertView.findViewById(R.id.txtage);
            holder.vImageViewAdd = (ImageView)convertView.findViewById(R.id.add_btn);
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }
		
		final AfChatroomMemberInfo c = mList.get(position);
		AfFriendInfo afFriendInfo = new AfFriendInfo();
		afFriendInfo.name = c.name;
		afFriendInfo.afId = c.afid;
		afFriendInfo.age = c.age;
		afFriendInfo.sex = c.sex;
		afFriendInfo.head_img_path = c.head_img_path;
//		holder.imageView.set
		holder.name.setText(c.name);
		String HeadPortrait = c.head_img_path;
		PalmchatLogUtils.d("==","房间成员列表人员HeadPortrait:"+HeadPortrait +"用户名："+c.name);
		String signStr = c.sign == null ? mContext.getString(R.string.default_status) : c.sign;
		CharSequence text = EmojiParser.getInstance(mContext).parse(signStr, ImageUtil.dip2px(mContext, 18));
		holder.sign.setText(text);
		holder.age.setBackgroundResource(Consts.AFMOBI_SEX_MALE == c.sex ?  R.drawable.bg_sexage_male : R.drawable.bg_sexage_female);
		holder.age.setCompoundDrawablesWithIntrinsicBounds(Consts.AFMOBI_SEX_MALE == c.sex ? R.drawable.icon_sexage_boy : R.drawable.icon_sexage_girl,0,0,0);
		holder.age.setText(c.age + "");
//		holder.roomName.setSelected(true);  
		
		//WXL 20151015 调UIL的显示头像方法,统一图片管理
		ImageManager.getInstance().DisplayAvatarImage(holder.imageView, afFriendInfo.getServerUrl(),
				afFriendInfo.getAfidFromHead(),Consts.AF_HEAD_MIDDLE, afFriendInfo.sex,afFriendInfo.getSerialFromHead(),null);
		boolean isAdd = false;
		isAdd = c.isAddFriend;

		holder.vImageViewAdd.setVisibility(isAdd ? View.GONE : View.VISIBLE);
		holder.vImageViewAdd.setOnClickListener(new View.OnClickListener() {
//			add friend
			@Override
			public void onClick(View v) {
				AfFriendInfo friend = AfChatroomMemberInfo.ChatroomMemberInfoToFriend(c);
				//判断是否已经加入黑名单
				if(null!= CacheManager.getInstance().getFriendsCacheSortListEx(
						Consts.AFMOBI_FRIEND_TYPE_BF).search(friend, false, true)) {
					ToastManager.getInstance().show(mContext, R.string.beblocked);
					return;
				}
				showDialog(mContext);
				
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CR_ADD_SUCC);
				
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.CR_ADD_SUCC);

				MessagesUtils.addStranger2Db(friend);
				
				mAfCorePalmchat.AfHttpSendMsg(c.afid,
						System.currentTimeMillis(), mContext.getString(R.string.want_to_be_friend).replace("{$targetName}", CacheManager.getInstance().getMyProfile().name),
						Consts.MSG_CMMD_FRD_REQ, c.afid, ChattingRoomPersonAdapter.this);
				if (!TextUtils.isEmpty(c.afid) && (!CacheManager.getInstance().getClickableMap().containsKey(c.afid + CacheManager.follow_suffix) || !CacheManager.getInstance().getClickableMap().get(c.afid + CacheManager.follow_suffix))) {
					// 判断是否已经关注过了，关注后不执行AddFollw();
					boolean isFollow = CommonUtils.isFollow(c.afid);
					if (!isFollow && !CacheManager.getInstance().getClickableMap().get(c.afid + CacheManager.follow_suffix) || isFollow && CacheManager.getInstance().getClickableMap().get(c.afid + CacheManager.follow_suffix)) {
						AddFollw(c,isFollow);
					}
				}
				
			}
		});

		holder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AfFriendInfo afFriendInfo = AfFriendInfo.FriendInfoToAfChatroomMemberInfo(c);
				String myAfId = CacheManager.getInstance().getMyProfile().afId;
				if(myAfId != null && myAfId.equals(afFriendInfo.afId)) {
					toMyProfile(afFriendInfo);
				} else {
					toProfile(afFriendInfo);
				}
			}
		});
		return convertView;
	}

	/** 判断是否点击关注按钮
	 * @param info
	 */
	public void AddFollw(final AfChatroomMemberInfo info,boolean isFollow) {
		if (!isFollow) {
			Update_IsFollow(info.afid);
			AfFriendInfo afFriendInfo = AfChatroomMemberInfo.ChatroomMemberInfoToFriend(info);
			mAfCorePalmchat.AfHttpFollowCmd(Consts.AFMOBI_FOLLOW_MASTRE, info.afid, Consts.HTTP_ACTION_A, afFriendInfo, ChattingRoomPersonAdapter.this);
			MessagesUtils.addStranger2Db(afFriendInfo);
		}
	}
	public void Update_IsFollow(String mFriendAfid) {
		if (CacheManager.getInstance().getClickableMap().containsKey(mFriendAfid + CacheManager.follow_suffix)) {
			if (CacheManager.getInstance().getClickableMap().get(mFriendAfid + CacheManager.follow_suffix)) {
				CacheManager.getInstance().getClickableMap().put(mFriendAfid + CacheManager.follow_suffix, false);
			} else {
				CacheManager.getInstance().getClickableMap().put(mFriendAfid + CacheManager.follow_suffix, true);
			}
		}
	}
	private class ViewHolder {
		ImageView imageView;
		TextView name;
		TextView sign;
		TextView age;
		ImageView vImageViewAdd;
		
	}

	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		switch (flag) {
		// 添加好友成功
		case Consts.REQ_FRIEND_LIST:

			if (code == Consts.REQ_CODE_SUCCESS) {
				if (user_data != null && user_data instanceof String) {
					final String afid = (String) user_data;
					if (!StringUtil.isNullOrEmpty(afid)) {
						MessagesUtils.addMsg2Chats(mAfCorePalmchat, afid, MessagesUtils.ADD_CHATS_FRD_REQ_SENT);
							if (null != mList) {
								int size = mList.size();
								for (int i = 0; i < size; i++) {
									AfChatroomMemberInfo info = mList.get(i);
									if (afid.equals(info.afid)) {
										info.isAddFriend = true;
										break;
									}
								}
							}
					}
					
					ToastManager.getInstance().show(mContext,
							mContext.getString(R.string.add_friend_req));
					notifyDataSetChanged();
					dismissDialog(mContext);
				}
				
			} else {
				dismissDialog(mContext);
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.req_failed));
			}

			break;
		case Consts.REQ_MSG_SEND:
			if (code == Consts.REQ_CODE_SUCCESS) {
				if (user_data != null && user_data instanceof String) {
					final String afid = (String) user_data;
					mAfCorePalmchat.AfHttpFriendOpr("all", afid,
							Consts.HTTP_ACTION_A, Consts.FRIENDS_MAKE,
							(byte) Consts.AFMOBI_FRIEND_TYPE_FF, null, afid,
							ChattingRoomPersonAdapter.this);
				}

			} else {
				dismissDialog(mContext);
				ToastManager.getInstance().show(mContext,
						mContext.getString(R.string.req_failed));
			}
			break;
			case Consts.REQ_FLAG_FOLLOW_LIST: // 关注 或取消关注
				AfFriendInfo afFriend = (AfFriendInfo) user_data;
				boolean isFollow = CommonUtils.isFollow(afFriend.afId);
				if (isFollow) {// 取消关注一个人
					MessagesUtils.onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afFriend);
				} else {// 关注一个人
					MessagesUtils.onAddFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afFriend);
					//MessagesUtils.addMsg2Chats(mAfPalmchat, afid, MessagesUtils.ADD_CHATS_FOLLOW);
					ToastManager.getInstance().show(mContext, R.string.followed);
				}
				Update_IsFollow(afFriend.afId);
				break;
		}
	}
	private void toMyProfile(AfFriendInfo afFriendInfo) {
		if(mContext != null) {
			Bundle bundle = new Bundle();
			bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
			Intent intent = new Intent(mContext, MyProfileActivity.class);
			intent.putExtras(bundle);
			mContext.startActivity(intent);
		}
	}


	private void toProfile(AfFriendInfo afFriendInfo) {
		if(mContext != null) {
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.CR_T_PF);
//			MobclickAgent.onEvent(mContext, ReadyConfigXML.CR_T_PF);
			Intent intent = new Intent(mContext, ProfileActivity.class);
			AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
			intent.putExtra(JsonConstant.KEY_PROFILE, info);
			intent.putExtra(JsonConstant.KEY_FLAG, true);
			intent.putExtra(JsonConstant.KEY_AFID, info.afId);
			intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
			mContext.startActivity(intent);
		}
	}
}
