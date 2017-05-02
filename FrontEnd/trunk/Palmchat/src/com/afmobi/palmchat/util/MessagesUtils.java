package com.afmobi.palmchat.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.RefreshChatsListEvent;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.eventbusmodel.BlockFriendEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.friends.ContactsFriendsFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.core.AfAttachImageInfo;
import com.core.AfAttachPAMsgInfo;
import com.core.AfAttachVoiceInfo;
import com.core.AfChatroomDetail;
import com.core.AfFriendInfo;
import com.core.AfGrpNotifyMsg;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;

import de.greenrobot.event.EventBus;

public class MessagesUtils {
	public static final String TEXT_MESSAGE = "TEXT_MESSAGE";
	public static final String VOICE_MESSAGE = "VOICE_MESSAGE";
	public static final String IMAGE_MESSAGE = "IMAGE_MESSAGE";
	public static final String CAMERA_MESSAGE = "CAMERA_MESSAGE";
	public static final String GROUP_TEXT_MESSAGE = "GROUP_TEXT_MESSAGE";
	public static final String GROUP_VOICE_MESSAGE = "GROUP_VOICE_MESSAGE";
	public static final String GROUP_IMAGE_MESSAGE = "GROUP_IMAGE_MESSAGE";
	public static final String GROUP_CAMERA_MESSAGE = "GROUP_CAMERA_MESSAGE";
	public static final String CHATTING_ROOM_TEXT_MESSAGE = "CHATTING_ROOM_TEXT_MESSAGE";
	public static final String CHATTING_ROOM_IMAGE_MESSAGE = "CHATTING_ROOM_IMAGE_MESSAGE";
	public static final String CHATTING_ROOM_VOICE_MESSAGE = "CHATTING_ROOM_VOICE_MESSAGE";
	public static final String CHATTING_ROOM_CAMERA_MESSAGE = "CHATTING_ROOM_CAMERA_MESSAGE";

	public static final String FRIEND_REQ_CALLBACK = "FRIEND_REQ_CALLBACK";
	public static final String SEND_GIFT_REQ_CALLBACK = "SEND_GIFT_REQ_CALLBACK";

	public final static int ACTION_INSERT = 0;
	public final static int ACTION_UPDATE = 1;
	public final static int ACTION_REMOVE = 2;
	public final static int EDIT_TEXT_CHANGE = 1;
	public final static int MSG_VOICE = 8;
	public final static int MSG_VOICE_RESEND = 10;
	public final static int SEND_VOICE = 9;
	public final static int MSG_PICTURE = 5;
	public final static int MSG_ERROR_TIP = 100;
	public static final int CAMERA = 1;
	public static final int PICTURE = 2;
	public static final int MSG_CARD = 3;
	public static final int MSG_RESEND_CARD = 55;
	public static final int MSG_TEXT = 6;
	public static final int MSG_GIF = 7;
	public static final int MSG_EMOTION = 66;
	public static final int MSG_TOO_SHORT = -2;
	public static final int SEND_IMAGE = 22;
	public static final int FAIL_TO_LOAD_PICTURE = -3;
	public static final int MSG_CAMERA_NOTIFY = 33;
	public static final int MSG_SET_STATUS = 887;
	public final static int MSG_IMAGE = 888;
	public final static int MSG_FORWARD = 889;
	public static final int MSG_ERROR_OCCURRED = -99;

	public final static int ADD_CHATS_FRD_REQ_SENT = 0;
	public final static int ADD_CHATS_PHONE_BOOK_IMPORT = 1;
	public final static int ADD_CHATS_FOLLOW = 2;

	public static final String FOLLOW_MSG = "1";
	public static final String DEL_FOLLOW_MSG = "2";
	public static final String DEL_BE_FOLLOWED_MSG = "3";

	public final static int MSG_SHARE_BROADCAST_OR_TAG = 40;

	/**
	 * true received false send
	 *
	 * @param status
	 * @return is it received message
	 */
	public static boolean isReceivedMessage(int status) {
		return status < AfMessageInfo.MESSAGE_STATUS_SEPERATOR;
	}

	/**
	 * true private chat  false
	 *
	 * @param type
	 */
	public static boolean isPrivateMessage(int type) {
		return 0 != (type & AfMessageInfo.MESSAGE_TYPE_MASK_PRIV);
	}

	/**
	 * palmchat team message
	 *
	 * @param type
	 * @return
	 */
	public static boolean isSystemMessage(int type) {
		return 0 != (type & AfMessageInfo.MESSAGE_TYPE_SYSTEM);
	}


	/**
	 * true  false
	 *
	 * @param type
	 */
	public static boolean isChattingRoomMessage(int type) {
		return 0 != (type & AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM);
	}


	/**
	 * true  false
	 *
	 * @param type
	 * @return
	 */
	public static boolean isGroupChatMessage(int type) {
		return 0 != (type & AfMessageInfo.MESSAGE_TYPE_MASK_GRP);
	}


	/**
	 * chatroom at me msg
	 *
	 * @param type
	 * @return
	 */
	public static boolean isChatroomAtMeMessage(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD;
	}

	/**
	 * @param type
	 * @return
	 */
	public static boolean isFriendReqMessage(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_FRIEND_REQ;
	}


	/**
	 * @param type
	 * @return
	 */
	public static boolean isFriendReqSucMessage(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS;
	}


	public static boolean isFollowMessage(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_FOLLOW;
	}


	public static boolean isBrdNotification(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_BC50;
	}

	public static boolean isFlowerMsg(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_FLOWER;
	}

	/**
	 * 判断服务端下发的是否为Predict开奖消息
	 *
	 * @param type: 消息类型
	 * @return true是Predict开奖消息
	 */
	public static boolean isPredictNotification(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_LOTTERY;
	}

	/**
	 * 判断服务端下发的是否为Predict充值成功消息
	 *
	 * @param type: 消息类型
	 * @return true是Predict充值成功消息
	 */
	public static boolean isPredictPayNotification(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_PAYMENT;
	}

	/**
	 * 判断服务端下发的是否为Predict派奖消息
	 *
	 * @param type: 消息类型
	 * @return true是Predict派奖消息
	 */
	public static boolean isPredictPrizeNotification(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_PRIZE;
	}

	/**
	 * zhh
	 * 判断服务端下发的是否为coins改变消息
	 *
	 * @param type: 消息类型
	 * @return true是coins改变消息
	 */
	public static boolean isPayCoinChange(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_PAY_SYS_NOTIFY;
	}


	/**
	 * @param type
	 * @return
	 */
	public static boolean isCardMessage(int type) {
		final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
		return msgTypeNew == AfMessageInfo.MESSAGE_CARD;
	}

	public static boolean isGroupSystemMessage(int type) {
		if (isGroupChatMessage(type)) {
			final int msgTypeNew = AfMessageInfo.MESSAGE_TYPE_MASK & type;
			return (AfMessageInfo.MESSAGE_GRP_SYS_MIN <= msgTypeNew && AfMessageInfo.MESSAGE_GRP_SYS_MAX >= msgTypeNew);
		}
		return false;
	}


	public static synchronized int insertFreqMsg(Context context, AfMessageInfo element, boolean dbFlag, boolean cacheFlag) {

		AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(element.fromAfId);
		if (afFriendInfo != null) {
			MainAfFriendInfo friend = new MainAfFriendInfo();
			friend.afFriendInfo = afFriendInfo;
			friend.afMsgInfo = element;


			if (MessagesUtils.isFriendReqSucMessage(element.type)) {
				afFriendInfo.type = Consts.AFMOBI_FRIEND_TYPE_FF;
				AfPalmchat mAfCorePalmchat = ((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat;

				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).saveOrUpdate(afFriendInfo, true, true);


				String aa = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,
						afFriendInfo.name, context.getString(R.string.frame_toast_friend_req_success));

				friend.afMsgInfo.msg = aa;

				int _id = mAfCorePalmchat.AfDbMsgInsert(element);
				friend.afMsgInfo._id = _id;
				insertMsg(friend.afMsgInfo, false, true);
				CacheManager.getInstance()
						.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(friend, true, true);

				CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FRD_REQ).insert(friend, dbFlag, cacheFlag);

			} else if (MessagesUtils.isFriendReqMessage(element.type)) {
				CacheManager.getInstance()
						.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(friend, dbFlag, cacheFlag);

//				friend.afMsgInfo._id = _id; 
//				CacheManager.getInstance()
//				.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).update(friend, false, true);

				CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FRD_REQ).insert(friend, dbFlag, cacheFlag);

//				follow
			} else if (MessagesUtils.isFollowMessage(element.type)) {

				PalmchatLogUtils.println("--ddd isFollowMessage " + element.msg);
				PalmchatLogUtils.i("follow","messageUtils:insertFreqMsg");
//				someone follow you
				if (FOLLOW_MSG.equals(element.msg)) {
					onAddFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE, afFriendInfo);
//				cancel follow you
				} else if (DEL_FOLLOW_MSG.equals(element.msg)) {
					onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE, afFriendInfo);
					FolllowerUtil.getInstance().remove(element.fromAfId);
//					cancel be followed you
				} else if (DEL_BE_FOLLOWED_MSG.equals(element.msg)) {
					onDelFollowSuc(Consts.AFMOBI_FOLLOW_TYPE_MASTER, afFriendInfo);
				}

			}

			return Constants.DB__SUC;
		} else {
			return Constants.DB__FAI;
		}

	}

	public static void deleteMessageFromDb(Context context, AfMessageInfo afMessageInfo) {
		if (MessagesUtils.isPrivateMessage(afMessageInfo.type)) {
			((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat.AfDbMsgRmove(afMessageInfo);
		} else {
			((PalmchatApp) context.getApplicationContext()).mAfCorePalmchat.AfDbGrpMsgRmove(afMessageInfo);
		}
	}
	
	public static int queryFriend(String afid) {

		AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afid);
		
		if(afFriendInfo != null) {
			return Constants.DB__SUC;
		} else {
			 return Constants.DB__FAI;
		}
	}
	
/**
 * 	set recent msg unread count
 * @param key
 * @param unread
 */
	public static void setUnreadMsg(String key, int unread) {
	
		if(key != null) {
			List<MainAfFriendInfo> mList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();
			for(MainAfFriendInfo info : mList) {
				if(info != null && info.afMsgInfo != null) {
					if(key.equals(info.afMsgInfo.getKey())) {
						info.afMsgInfo.unReadNum = unread;
						break;
					}
					
				}
				
				
			}
		
		}
	}
	
	/**
	 * set recent msg sending status
	 * @param afMessageInfo
	 * @param status
	 */
	public static void setRecentMsgStatus(AfMessageInfo afMessageInfo, int status) {
		MainAfFriendInfo mainAf = new MainAfFriendInfo();
		mainAf.afMsgInfo = afMessageInfo;
		MainAfFriendInfo searchResult = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).search(mainAf, false, true);
	
		if(searchResult != null) {
		AfMessageInfo resultMsg = searchResult.afMsgInfo;
		if(resultMsg != null) {
			if(resultMsg._id == afMessageInfo._id) {
				resultMsg.status = status;
			}
		}
		
		}
			
	}

	
	public static int insertMsg(AfMessageInfo element, boolean dbFlag, boolean cacheFlag) {

		if(isPrivateMessage(element.type) || isSystemMessage(element.type)) {
		
	AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(element.getKey());
	
		if(afFriendInfo != null) {
			
			MainAfFriendInfo friend = new MainAfFriendInfo(); 
			friend.afFriendInfo = afFriendInfo;
			friend.afMsgInfo = element;
			
			CacheManager.getInstance()
			.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(friend, dbFlag, cacheFlag);
			
			PalmchatLogUtils.println("--xxxvvvv insertMsg " + CacheManager.getInstance()
			.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).size(false, true));
			
			return Constants.DB__SUC;
		} else {
			PalmchatLogUtils.println("--xxxvvvv insertMsg DB__FAI");
			 return Constants.DB__FAI;
		}
	
		} else if(isGroupChatMessage(element.type)) {
			
			AfGrpProfileInfo afGrpInfo = new AfGrpProfileInfo();
			afGrpInfo.afid = element.getKey();
			AfGrpProfileInfo result = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).search(afGrpInfo, dbFlag, cacheFlag);
			
			if(result != null ) {
				MainAfFriendInfo grpInfo = new MainAfFriendInfo(); 
				grpInfo.afGrpInfo = result;
				grpInfo.afMsgInfo = element;
				
				CacheManager.getInstance()
				.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(grpInfo, dbFlag, cacheFlag);
				
				return Constants.DB__SUC;
			} else {
				return Constants.DB__FAI;
			}
			
			
		}
		
		 return Constants.DB__SUC;
	}
	
	public static AfGrpProfileInfo dispatchGrpNotifyMsg(AfGrpNotifyMsg grpNotifyMsg, boolean dbFlag, boolean cacheFlag) {
		
		int type = grpNotifyMsg.type | AfMessageInfo.MESSAGE_TYPE_MASK_GRP;
		
		AfGrpProfileInfo grpInfo = new AfGrpProfileInfo();
		grpInfo.afid = grpNotifyMsg.gid;
		AfGrpProfileInfo afGrpInfo = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).search(grpInfo, dbFlag, cacheFlag);
		
//		group profile cache exist
		if(afGrpInfo != null) {
			
			return afGrpInfo;
		} else {
			 return null;
		}
		
	}

	/** 更新分享Tag的PostNumber
	 * @param messageInfo
	 */
	public static void updateShareTagMsgPostNumber(AfMessageInfo messageInfo,int attachId) {
		AfPalmchat mAfCorePalmchat = ((PalmchatApp)PalmchatApp.getApplication().getApplicationContext()).mAfCorePalmchat;
		
		AfAttachPAMsgInfo afAttachPAMsgInfo = (AfAttachPAMsgInfo) messageInfo.attach;
		if(afAttachPAMsgInfo != null) {
			 mAfCorePalmchat.AfDbAttachPAMsgUpdate(afAttachPAMsgInfo);
		}
	}
	
	
	public static AfGrpProfileInfo searchGrpInfo(AfMessageInfo afMessageInfo, boolean dbFlag, boolean cacheFlag) {
		
//		int type = grpNotifyMsg.type | AfMessageInfo.MESSAGE_TYPE_MASK_GRP;
		
		AfGrpProfileInfo grpInfo = new AfGrpProfileInfo();
		grpInfo.afid = afMessageInfo.toAfId;
		AfGrpProfileInfo afGrpInfo = CacheManager.getInstance().getGrpCacheSortList(Consts.AFMOBI_FRIEND_TYPE_GROUP).search(grpInfo, dbFlag, cacheFlag);
		
//		group profile cache exist
		if(afGrpInfo != null) {
			
			return afGrpInfo;
		} else {
			 return null;
		}
		
	}
	
	/**
	 * 好友请求后删除最近联系人表(屏蔽了)和好友消息表
	 * @param element
	 * @param dbFlag
	 * @param cacheFlag
	 */
	public static void removeFreqMsg(AfMessageInfo element, boolean dbFlag, boolean cacheFlag) {
		
//		AfFriendInfo afFriendInfo = CacheManager.getInstance()
//				.searchAllFriendInfo(element.getKey());
//		
//		if(afFriendInfo != null) {
//		} 
//		friend.afFriendInfo = afFriendInfo;
		
		MainAfFriendInfo friend = new MainAfFriendInfo(); 
			friend.afMsgInfo = element;
		//删除最近联系人表	
//		CacheManager.getInstance() 
//			.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).remove(friend, dbFlag, cacheFlag);
			
		//删除好友请求消息表	
		CacheManager.getInstance()
			.getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FRD_REQ).remove(friend, dbFlag, cacheFlag);
	
	}
	
	public static void removeMsg(AfMessageInfo element, boolean dbFlag, boolean cacheFlag) {
		
		if(element != null) {
		
			MainAfFriendInfo friend = new MainAfFriendInfo(); 
			friend.afMsgInfo = element;
			
			CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).remove(friend, dbFlag, cacheFlag);
		
		}
	}
	
	public static boolean isLastMsg(AfMessageInfo afMessageInfo) {
		MainAfFriendInfo friend = new MainAfFriendInfo(); 
		friend.afMsgInfo = afMessageInfo;
		MainAfFriendInfo searchResult = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).search(friend, false, true);
		
		if(searchResult != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized static void onAddFriendSuc(AfFriendInfo af) {
		if (af != null) {
		af.type = Consts.AFMOBI_FRIEND_TYPE_FF;
		CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).saveOrUpdate(af, true, true);

		}
		
	}
	
	public synchronized static void onDelFriendSuc(AfFriendInfo af) {
		if (af != null) {
		af.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER;
		CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_STRANGER).saveOrUpdate(af, true, true);
		CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).remove(af, false, true);
		}
	}
	
	/**
	 *  
	 * @param type  Consts.AFMOBI_FOLLOW_TYPE_MASTER or
		            Consts.AFMOBI_FOLLOW_TYPE_PASSIVE
	 * @param af
	 */
	public synchronized static void onAddFollowSuc(byte type, AfFriendInfo af) {
		if (af != null) {
			
			AfFriendInfo following = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).search(af, false, true);
			AfFriendInfo follower = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).search(af, false, true);
//			AfFriendInfo friend  =CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).search(af, false, true);
			AfPalmchat mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
			 
			if (type == Consts.AFMOBI_FOLLOW_TYPE_MASTER) {
				 
			if (follower != null) {
				af.follow_type = Consts.AFMOBI_FOLLOW_ALL;
			} else {
				af.follow_type = Consts.AFMOBI_FOLLOW_MASTRE;
			}
		
//		    update db
			boolean isUpdatedb = mAfCorePalmchat.AfDbProfileUpdateFollowtype(af.follow_type, af.afId);
			
			
//			update cache
			CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).saveOrUpdate(af, !isUpdatedb, true);
			
			 } else if (type == Consts.AFMOBI_FOLLOW_TYPE_PASSIVE) {
				 if (following != null) {
						af.follow_type = Consts.AFMOBI_FOLLOW_ALL;
					} else {
						af.follow_type = Consts.AFMOBI_FOLLOW_PASSIVE;
					}
	
//		     update db
			boolean isUpdatedb = mAfCorePalmchat.AfDbProfileUpdateFollowtype(af.follow_type, af.afId);
			 
//			 update cache
			 CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).saveOrUpdate(af, !isUpdatedb, true);

				FolllowerUtil.getInstance().add(af.afId);
			 }
			 
		}

	}

	/**
	 *
	 * @param type  Consts.AFMOBI_FOLLOW_TYPE_MASTER or
	Consts.AFMOBI_FOLLOW_TYPE_PASSIVE
	 * @param af
	 */
	public synchronized static void onAddFollower(byte type, AfFriendInfo af,boolean dbFlag,boolean cacheFlag) {
		if (af != null) {

			AfFriendInfo following = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).search(af, false, true);
			AfFriendInfo follower = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).search(af, false, true);
//			AfFriendInfo friend  =CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).search(af, false, true);
			AfPalmchat mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;

			if (type == Consts.AFMOBI_FOLLOW_TYPE_MASTER) {

				if (follower != null) {
					af.follow_type = Consts.AFMOBI_FOLLOW_ALL;
				} else {
					af.follow_type = Consts.AFMOBI_FOLLOW_MASTRE;
				}

//		    update db
				boolean isUpdatedb = mAfCorePalmchat.AfDbProfileUpdateFollowtype(af.follow_type, af.afId);


//			update cache
				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).saveOrUpdate(af, isUpdatedb, true);

			} else if (type == Consts.AFMOBI_FOLLOW_TYPE_PASSIVE) {
				if (following != null) {
					af.follow_type = Consts.AFMOBI_FOLLOW_ALL;
				} else {
					af.follow_type = Consts.AFMOBI_FOLLOW_PASSIVE;
				}

//		     update db
				boolean isUpdatedb = mAfCorePalmchat.AfDbProfileUpdateFollowtype(af.follow_type, af.afId);

//			 update cache
				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).saveOrUpdate(af, isUpdatedb, cacheFlag);

			}

		}

	}
	
	/**
	 *  
	 * @param type  Consts.AFMOBI_FOLLOW_TYPE_MASTER or
		            Consts.AFMOBI_FOLLOW_TYPE_PASSIVE
	 * @param af
	 */
	public synchronized static void onDelFollowSuc(byte type, AfFriendInfo af) {
		boolean isDeleteDb = false;
		if (af != null) {
			AfFriendInfo following = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).search(af, false, true);
			AfFriendInfo follower = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).search(af, false, true);
			AfFriendInfo friend = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).search(af, false, true);
			AfPalmchat mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;

			if (type == Consts.AFMOBI_FOLLOW_TYPE_MASTER) {

//			  update db
				if (following != null && follower != null) {
					mAfCorePalmchat.AfDbProfileUpdateFollowtype(Consts.AFMOBI_FOLLOW_PASSIVE, af.afId);
				} else if (follower == null && friend != null) {
					mAfCorePalmchat.AfDbProfileUpdateFollowtype(Consts.AFMOBI_FOLLOW_UNKNOWN, af.afId);
				} else if (friend == null) {
					isDeleteDb = true;
				}

//			  update cache
				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).remove(af, isDeleteDb, true);

			} else if (type == Consts.AFMOBI_FOLLOW_TYPE_PASSIVE) {


//			  update db
				if (following != null && follower != null) {
					mAfCorePalmchat.AfDbProfileUpdateFollowtype(Consts.AFMOBI_FOLLOW_MASTRE, af.afId);
				} else if (following == null && friend != null) {
					mAfCorePalmchat.AfDbProfileUpdateFollowtype(Consts.AFMOBI_FOLLOW_UNKNOWN, af.afId);

				} else if (friend == null) {
					isDeleteDb = true;
				}

//			  update cache
				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).remove(af, isDeleteDb, true);

			}
			if (isDeleteDb) {
				insertStranger(af);
			}
		}

	}
	/**
	 * 把一个人加入黑名单的操作 ，删除好友 删除Follower 删除Following
	 * @param af
	 */
	public synchronized static void onAddBlockSuc(AfFriendInfo af) {
		if (af != null) { 
			AfFriendInfo following = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).search(af, false, true);
			AfFriendInfo follower = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).search(af, false, true);
			 AfPalmchat afCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat; 
			af.type=Consts.AFMOBI_FRIEND_TYPE_BF;
			
			//从数据库中把一个人加为黑名单并从好友列表中删除
			afCorePalmchat.AfDbProfileUpdateType(Consts.AFMOBI_FRIEND_TYPE_BF,af.afId);
			//从缓存列表中把一个好友删除
			CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).remove(af, false, true);
			CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).insert( af, false, true);
			if(following!=null){//不管是关注还是有被关注 那就把状态取消掉 从这2个列表消失
				/**表示Following删除掉加入黑名单的人员*/
				CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_MASTER).remove(af, false, true);
				/**判断粉丝列表是否在列表名单中*/
				if(follower!=null){
					/**粉丝列表存在要被Block的人员名单的话还是设置为粉丝*/
					afCorePalmchat.AfDbProfileUpdateFollowtype(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE, af.afId);
			 	}else{
					/**粉丝列表不存在的话设置为没有任何的关系*/
					afCorePalmchat.AfDbProfileUpdateFollowtype(Consts.AFMOBI_FOLLOW_UNKNOWN, af.afId);
				}
//				if(follower!=null){//following缓存删除
//					 CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FOLLOW_TYPE_PASSIVE).remove(af, false, true);
//			 	}
			}
			EventBus.getDefault().post(new BlockFriendEvent());
			//删除联系人列表和私聊消息
            final AfMessageInfo[] recentDataArray = afCorePalmchat
                    .AfDbRecentMsgGetRecord(
                            AfMessageInfo.MESSAGE_TYPE_MASK_PRIV,
                            af.afId, 0, Integer.MAX_VALUE);
            if (null != recentDataArray && recentDataArray.length > 0) {
                for (AfMessageInfo messageInfo : recentDataArray) {
                	afCorePalmchat.AfDbMsgRmove(messageInfo);
                    MessagesUtils.removeMsg(messageInfo, true, true);
                }
                afCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_PRIV, af.afId);
//                PalmchatApp.getApplication().getApplicationContext().sendBroadcast(new Intent(Constants.REFRESH_CHATS_ACTION));
				EventBus.getDefault().post(new RefreshChatsListEvent());
            }
		}
		 
	}
	
	/**
	 * 将其从黑名单去除
	 * @param af
	 */
	public synchronized static void onUnBlockSuc(AfFriendInfo af) {
		af.type=Consts.AFMOBI_FRIEND_TYPE_STRANGER;
		AfPalmchat afCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		afCorePalmchat.AfDbProfileUpdateType(Consts.AFMOBI_FRIEND_TYPE_STRANGER,af.afId);
		CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_BF).remove( af, false, true);
		
	}
	
	
	public synchronized static void insertStranger(AfFriendInfo afF) {
		if (null != afF) {
			PalmchatLogUtils.println("AfFriendInfo afF  "+afF.afId);
			afF.type = Consts.AFMOBI_FRIEND_TYPE_STRANGER;
			CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_STRANGER).saveOrUpdate(afF, true, true);
		}
	}
	
	public synchronized static void insertFriend(AfFriendInfo afF) {
		PalmchatLogUtils.println("AfFriendInfo afF  "+afF.afId);
		afF.type = Consts.AFMOBI_FRIEND_TYPE_FF;
		CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FF).insert(afF, true, true);
	}
	
	
	//send textOrEmotion
	public static void sendTextOrEmotion(final int fromType,final String msg,final Handler mainHandler,final String targetId,final AfPalmchat mAfCorePalmchat) {
		String content = msg;
		if(content.length() > 0){
			getMessageInfoForText(fromType,AfMessageInfo.MESSAGE_TEXT,null,0,0,mainHandler,MSG_TEXT,ACTION_INSERT,null,targetId,mAfCorePalmchat,content);
		}
	}

	
	public static void getMessageInfoForText(final int fromType,final int msgType,final String fileName,
			final int fileSize,final int length,final Handler handler,final int handler_MsgType,
			final int action,final AfMessageInfo afMessageInfo,
			final String mFriendAfid,final AfPalmchat mAfCorePalmchat,final String content){
//		PalmchatLogUtils.println("getMessageInfoForText "+fileName+"  mVoiceName  "+mVoiceName);
		final AfMessageInfo messageInfo = new AfMessageInfo();//新增
		new Thread(new Runnable() {
			public void run() {
				switch (msgType) {
				case AfMessageInfo.MESSAGE_TEXT:
					if(action == MessagesUtils.ACTION_INSERT){
						messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
						messageInfo.toAfId = mFriendAfid;
						messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_TEXT;
						messageInfo.msg = content;
						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						
						messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);
					}else if(action == MessagesUtils.ACTION_UPDATE){
						afMessageInfo.client_time = System.currentTimeMillis();//afMessageInfo 更改�?
						afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
					}
					break;
				case AfMessageInfo.MESSAGE_VOICE:
					if(action == MessagesUtils.ACTION_INSERT){
						AfAttachVoiceInfo afAttachVoiceInfo = new AfAttachVoiceInfo();
						afAttachVoiceInfo.file_name =  fileName;
						afAttachVoiceInfo.file_size = fileSize;
						afAttachVoiceInfo.voice_len = length;
						afAttachVoiceInfo._id = mAfCorePalmchat.AfDbAttachVoiceInsert(afAttachVoiceInfo);
//						PalmchatLogUtils.println("getMessageInfoForText "+fileName+"  mVoiceName  "+mVoiceName);
						
						messageInfo.client_time = System.currentTimeMillis();
//						messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
						messageInfo.toAfId = mFriendAfid;
						messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_VOICE;
						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						messageInfo.attach = afAttachVoiceInfo;
						messageInfo.attach_id = afAttachVoiceInfo._id;
						messageInfo._id = mAfCorePalmchat.AfDbMsgInsert(messageInfo);
					}else if(action == MessagesUtils.ACTION_UPDATE){
						afMessageInfo.client_time = System.currentTimeMillis();
						afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						mAfCorePalmchat.AfDbMsgUpdate(afMessageInfo);
					}
					break;
				case AfMessageInfo.MESSAGE_IMAGE:
					AfAttachImageInfo afAttachImageInfo = new AfAttachImageInfo();
//					afAttachImageInfo.small_file_name = 
					
//					AfImageReqInfo param = new AfImageReqInfo();		
//					param.file_name = "s03.jpg";
//					param.path = "/sdcard/03.jpg";
////					param.send_msg = "a1902418";
//					param.recv_afid="a1902418";	
					
//					int handle =  mAfCorePalmchat.AfHttpSendImage(param, 0, this, this);
//					System.out.println("ywp: AfSendImageTest: handle = " + handle);
					break;
				default:
					break;
				}
				if(action == MessagesUtils.ACTION_INSERT){
					handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
					addToCache(messageInfo);
				}else if(action == MessagesUtils.ACTION_UPDATE){
					handler.obtainMessage(handler_MsgType, afMessageInfo).sendToTarget();
					addToCache(afMessageInfo);
				}
			}

		}).start();
		
//		return messageInfo;
	}
	
	
	public static void addToCache(final AfMessageInfo messageInfo) {
		MessagesUtils.insertMsg(messageInfo, true, true);
//		CacheManager.getInstance().getRecentMsgCacheSortList(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).insert(messageInfo, true, true);
	}

	
	public static void setChatroomExit() {
		List<AfChatroomDetail> listChatrooms = CacheManager.getInstance().getAfChatroomDetails();
		if(listChatrooms != null) {
		for(AfChatroomDetail afchat : listChatrooms) {
			if(afchat != null) {
				 afchat.isEntry = false;
			}
		}
		}
		
	}
	
	public static void setChatroomIsEntry(String cid) {
		List<AfChatroomDetail> listChatrooms = CacheManager.getInstance().getAfChatroomDetails();
		if(listChatrooms != null) {
		for(AfChatroomDetail afchat : listChatrooms) {
			if(afchat != null && afchat.cid!= null) {
				if(afchat.cid.equals(cid)) {
					afchat.isEntry = true;
					break;
				}/* else {
					afchat.isEntry = false;
				}*/
			}
		}
		} 
	}
	
	
	
	public static void addStranger2Db(AfFriendInfo afFriendInfo) {
		if(afFriendInfo != null){
			AfFriendInfo localInfo = CacheManager.getInstance().searchAllFriendInfo(afFriendInfo.afId);
			if(localInfo == null) {
				insertStranger(afFriendInfo);
			}
		}
	}
	
	 
    /**
     * import phone book or send a friend request,generate a msg on chats
     * @param afPalmchat
     */
	public static AfMessageInfo addMsg2Chats(AfPalmchat afPalmchat, String afid, int tag) {
		 AfMessageInfo messageInfo = new AfMessageInfo();
			messageInfo.client_time = System.currentTimeMillis();
//			messageInfo.fromAfId = CacheManager.getInstance().getMyProfile().afId;
			messageInfo.fromAfId = afid;
			messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_PRIV | AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS;
			
			messageInfo.msg = "";
			switch (tag) {
			case ADD_CHATS_FRD_REQ_SENT:
				messageInfo.msg = PalmchatApp.getApplication().getString(R.string.add_friend_req);
				
				break;
				
			case ADD_CHATS_PHONE_BOOK_IMPORT:
				String name = null;
				AfFriendInfo friend = CacheManager.getInstance().searchAllFriendInfo(afid);
				if(friend != null) {
					 name = friend.alias == null ? friend.name : friend.alias;
					// name is empty,so afid
					if (StringUtil.isNullOrEmpty(name)) {
						name = friend.afId.replace("a", "");
					}
				}
				
				messageInfo.msg = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, 
						name, PalmchatApp.getApplication().getString(R.string.frame_toast_friend_req_success));
				break;
				
				
			case ADD_CHATS_FOLLOW:
				String name2 = null;
				AfFriendInfo friend2 = CacheManager.getInstance().searchAllFriendInfo(afid);
				if(friend2 != null) {
					 name2 = friend2.alias == null ? friend2.name : friend2.alias;
					// name is empty,so afid
					if (StringUtil.isNullOrEmpty(name2)) {
						name2 = friend2.afId.replace("a", "");
					}
				}
				
				messageInfo.msg = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, 
						name2, PalmchatApp.getApplication().getString(R.string.have_followed));
				
				break;

			default:
				break;
			}
			
			
			messageInfo.status = AfMessageInfo.MESSAGE_UNREAD;
			
			messageInfo._id = afPalmchat.AfDbMsgInsert(messageInfo);
			
			insertMsg(messageInfo, true, true);
			
			return messageInfo;
	}
	
}
