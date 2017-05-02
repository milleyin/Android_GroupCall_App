package com.afmobi.palmchat.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.ShareBroadcastResultEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfNearByGpsInfo;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfBroadCastTagInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfCommentInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.AfResponseComm.AfPulishInfo;
import com.core.Consts;
import com.core.test;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
/**
 * 发送广播
 * @author xiaolong
 *
 */
public class BroadcastUtil implements AfHttpResultListener{

	private static BroadcastUtil broadcastUtil;
	private Context context; 
	static String TAG  = BroadcastUtil.class.getName();
	private BroadcastUtil(){
	}
	
	public static BroadcastUtil getInstance(){ 
		if(broadcastUtil == null){
			broadcastUtil = new BroadcastUtil();
		}
		return broadcastUtil;
	}
	
	
	/**
	 * send msg
	 * @param context
	 * @param message
	 */
	public void sendBroadcastMsg(Context context,String message, byte purview, String tag){
		this.context = context;
		double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
		double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		long sid = System.currentTimeMillis();
		AfChapterInfo afChapterInfo = toAfChapterInfoByMSG(null, sid , message, purview, tag, lat, lon); 
		afChapterInfo._id = ClientSendData_AfDBBCChapterInsert(afChapterInfo);
		if (lat == 4.9E-324) {
			lat = 0.0d;
		}
		if ( lon == 4.9E-324) {
			lon = 0.0d;
		}
		saveCache(afChapterInfo);
		int http_id = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgPublishEX("BR_TYPE_TEXT", message,  Consts.BR_TYPE_TEXT, purview, info.afId, 0, lon, lat, info.city, info.region,info.country, tag,PalmchatApp.getOsInfo().getCountryCode(),null,null,null , afChapterInfo ,this);
		sendUpdateBroadcastForResult(afChapterInfo);
//		test.readDbDataTest(afChapterInfo.afid);
	}
	/**
	 * 转发一个广播到NewAround
	 * @param context
	 *
	 * @param message
	 * @param purview
	 * @param tag
	 */
	public void forwardBroadcast(Context context,AfChapterInfo share_Info,String message, byte purview, String tag){
		this.context = context;
		double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
		double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		long sid = System.currentTimeMillis();
		AfChapterInfo afChapterInfo = toAfChapterInfoByMSG(null, sid , message, purview, tag, lat, lon); 
		if(TextUtils.isEmpty(message)){
			afChapterInfo.share_flag= DefaultValueConstant.BROADCAST_SHARE_NOCOMMENT;
		}else{
			afChapterInfo.share_flag= DefaultValueConstant.BROADCAST_SHARE_COMMENT;
		}
		afChapterInfo.share_mid=share_Info.mid;
		afChapterInfo.share_info=share_Info;
		afChapterInfo._id = ClientSendData_AfDBBCChapterInsert(afChapterInfo);
		saveCache(afChapterInfo);
		
		if (lat == 4.9E-324) {
			lat = 0.0d;
		}
		if ( lon == 4.9E-324) {
			lon = 0.0d;
		} 
		 PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgPublishEX("BR_TYPE_TEXT", message,
				TextUtils.isEmpty(message)?Consts.BR_TYPE_SHARE: Consts.BR_TYPE_SHARE_TEXT , purview, info.afId, 0, lon, lat, info.city, info.region,info.country, tag,PalmchatApp.getOsInfo().getCountryCode(),null,share_Info.mid,null , afChapterInfo ,this);
		sendUpdateBroadcastForResult(afChapterInfo); 
	}
	/**
	 * 发送图文
	 * @param context
	 * @param message
	 * @param picturePathList
	 * @param purview  权限  是所有人可见 还是朋友可见 还是粉丝可见
	 * @param tag
	 * 
	 */
	public void sendBroadcastPictureAndText(Context context, String message, List<AfMFileInfo> picturePathList, byte purview, String tag,String pic_rule){
		long sid = System.currentTimeMillis();
		double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
		double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng()); 
		byte type  = Consts.BR_TYPE_IMAGE;
		if (TextUtils.isEmpty(message)) {
			type  = Consts.BR_TYPE_IMAGE;
		}else {
			type  = Consts.BR_TYPE_IMAGE_TEXT;
		}
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		AfChapterInfo afChapterInfo = toAfChapterInfoByAfMFile(null, sid, message, picturePathList, purview, type, tag, lat, lon,pic_rule);
		afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
		afChapterInfo._id = ClientSendData_AfDBBCChapterInsert(afChapterInfo);
		afChapterInfo.list_mfile = AfDBBCMFileInsert(Consts.DATA_FROM_LOCAL, afChapterInfo._id, picturePathList, Consts.URL_TYPE_IMG);
		PalmchatLogUtils.println("----flag:afChapterInfo._id:"+afChapterInfo._id);
		if (lat == 4.9E-324) {
			lat = 0.0d;
		}
		if ( lon == 4.9E-324) {
			lon = 0.0d;
		}
		saveCache(afChapterInfo);  
		//发送之前再根据当前排布情况对缩图进行重新计算，因为原图情况和缩图情况可能是不一致的
		AdapterBroadcastUitls.getImageRule_Size_cut(afChapterInfo,ImageUtil.DISPLAYW );//计算各个图片的宽高和裁切
		
		PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgPublishEX(String.valueOf(type) , message, type, purview, info.afId, picturePathList.size(), lon, lat, info.city, info.region,info.country, tag ,PalmchatApp.getOsInfo().getCountryCode(), null, null,afChapterInfo.pic_rule, afChapterInfo, this);
		sendUpdateBroadcastForResult(afChapterInfo);
	}
	
	/**
	 * 发送失败后的重发
	 * @param context
	 * @param afChapterInfo
	 */
	public void resendBroadcast(Context context, AfChapterInfo afChapterInfo){
		this.context = context;
		long sid = System.currentTimeMillis();
		
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo profileInfo = cm.getMyProfile();
		
		double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
		double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
		afChapterInfo.time = AfNearByGpsInfo.getsendTime(sid);
		afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
		resend(afChapterInfo, profileInfo, lon, lat);
		
	}
	
	/**
	 * 发送语音+文字
	 * @param context
	 * @param message
	 * @param voicePathList
	 * @param purview
	 * @param tag
	 */
	public void sendBroadcastVoiceAndText(Context context,String message,List<AfMFileInfo> voicePathList, byte purview, String tag){
		this.context = context;
		long sid = System.currentTimeMillis();
		double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
		double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
		byte type  = Consts.BR_TYPE_VOICE;
		if (TextUtils.isEmpty(message)) {
			type  = Consts.BR_TYPE_VOICE;
		}else {
			type  = Consts.BR_TYPE_VOICE_TEXT;
		}
		CacheManager cm = CacheManager.getInstance();
		AfProfileInfo info = cm.getMyProfile();
		AfChapterInfo afChapterInfo = toAfChapterInfoByAfMFile(null, sid, message, voicePathList, purview, type, tag, lat, lon,"");
		afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
		if(!voicePathList.isEmpty()){
			afChapterInfo.desc=String.valueOf(voicePathList.get(0).recordTime);
		} 
		afChapterInfo._id = ClientSendData_AfDBBCChapterInsert(afChapterInfo);
		afChapterInfo.list_mfile = AfDBBCMFileInsert(Consts.DATA_FROM_LOCAL, afChapterInfo._id, voicePathList, Consts.URL_TYPE_VOICE);

		if (lat == 4.9E-324) {
			lat = 0.0d;
		}
		if ( lon == 4.9E-324) {
			lon = 0.0d;
		}
		
		saveCache(afChapterInfo);
		int http_id = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgPublishEX(String.valueOf( type)  , message,  type, purview, info.afId, voicePathList.size(), lon, lat, info.city, info.region,info.country, tag, PalmchatApp.getOsInfo().getCountryCode(),null,null,null,afChapterInfo,this);
		sendUpdateBroadcastForResult(afChapterInfo);
	}


	/**
	 * 当一个附件发送成功，把发送成功的附件 根据URL MD5作为缓存路径 复制
	 * @param result
	 * @param user_data
     */
	private void copyFileToCacheDiskAfterSend(Object user_data,Object result){
		if (user_data != null&& user_data instanceof AfMFileInfo&& result!=null&&result instanceof String) {
			AfMFileInfo _fileInfo=(AfMFileInfo)user_data;
			String _url=(String)result;//返回文件的URL路径 /images/2015/11/5/f8cd21233d69d7c3ba5e74dda7ff3475_600_500.jpg
			_fileInfo.url=_url;
			if(Consts.URL_TYPE_VOICE==_fileInfo.url_type){//声音
				String pathOriginalDestination= RequestConstant.VOICE_CACHE
						+ PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress( _url)
						+AdapterBroadcastUitls.DOWNLOAD_SUCCESS_SUFFIX;
//				File outFile_original = new File(pathOriginalDestination);
				FileUtils.copyToImg(_fileInfo.local_img_path, pathOriginalDestination);//outFile_original.getAbsolutePath());
			}else if(Consts.URL_TYPE_IMG==_fileInfo.url_type){//图片模式
				//复制缩图
				String _strThumb=CacheManager.getInstance().getThumb_url_sending(_fileInfo,_url, _fileInfo.isSingle);
				if(_strThumb!=null&&!TextUtils.isEmpty(_fileInfo.local_thumb_path)){
					String pathThumbDestination= RequestConstant.IMAGE_UIL_CACHE +  PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(_strThumb);
//					File outFile = new File(pathThumbDestination);
					FileUtils.copyToImg(_fileInfo.local_thumb_path , pathThumbDestination);//outFile.getAbsolutePath());
					//						FileUtils.delete(_fileInfo.local_thumb_path);先不删除 所以注释掉 因为还可能有发送失败 重试的情况
				}
				//复制原图
				String pathOriginalDestination= RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress( _url);
//				File outFile_original = new File(pathOriginalDestination);
				FileUtils.copyToImg(_fileInfo.local_img_path, pathOriginalDestination);//outFile_original.getAbsolutePath());
//						FileUtils.delete(_fileInfo.local_img_path);先不删除 所以注释掉 因为还可能有发送失败 重试的情况
			}
		}
	}
	@Override
	public void
	AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		if (code == Consts.REQ_CODE_SUCCESS) { 
			switch (flag) {
			case Consts.REQ_BCMEDIA_UPLOAD://广播附件 图片 语音 等发送成功  如果有多张图 这里会返回多次 当返回成功数==附件数 就设置为发广播成功
				update_Status(user_data, AfMessageInfo.MESSAGE_SENT, code,result);
				copyFileToCacheDiskAfterSend(user_data,result);
				break;
			case Consts.REQ_BCMEDIA_UPLOAD_DP: //带图的广播第一步，失败
				update_Status(user_data, AfMessageInfo.MESSAGE_UNSENT, code,result);
				break;
				
			case Consts.REQ_BROADCAST_MSG_SEND:
			case Consts.REQ_BROADCAST_MSG_SEND_BY_CITY:
			case Consts.REQ_BCMSG_PUBLISH://发布广播
				
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_SUCC);
				if(user_data != null){// Send text attachments
					AfResponseComm afResponseComm = (AfResponseComm) result;
					AfPulishInfo afPulishInfo = (AfPulishInfo) afResponseComm.obj;
					AfChapterInfo afChapterInfo = (AfChapterInfo) user_data;
					AfChapterInfo resend_afchapterinfo = CacheManager.getInstance().getBC_resend_HashMap().get(afChapterInfo._id);
					List<AfMFileInfo> picturePathList = afChapterInfo.list_mfile;
					AfMFileInfo mediaParams;
					afChapterInfo.mstoken = afPulishInfo.mstoken;
					afChapterInfo.mid = afPulishInfo.mid;
					resend_afchapterinfo.mid = afPulishInfo.mid;
//					PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterUpdateMidByID(afChapterInfo.mid, afChapterInfo._id);
					PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterUpdateMstokenByID(afChapterInfo.mstoken, afChapterInfo._id);//update BC mstoken
				 
					if (picturePathList != null && picturePathList.size() > 0 ) {
						 
						for (int i = 0; i < picturePathList.size(); i++) {
							mediaParams = picturePathList.get(i);
							mediaParams.isSingle=(picturePathList.size()==1);//是否是单个附件模式
								int media_seq =(i+1);
								if (mediaParams != null) {
									if (mediaParams.url_type == Consts.URL_TYPE_VOICE ) {
										if(mediaParams.recordTime<1&&!TextUtils.isEmpty(afChapterInfo.desc)){//从数据读取的语音长度 因为语音长度是保存在desc的 所以如果没的话 就要从desc取
											  try {
												  mediaParams.recordTime = Integer.valueOf(afChapterInfo.desc);
						                        } catch (Exception e) { 
						                        } 
										}
										if( mediaParams.recordTime <2){//加个保护，从Y3 5.2.20的版本看到有出异常导致recordTime为0的情况，
											mediaParams.recordTime=2;
										}
										new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_VOICE);
//										MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_VOICE);
										PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMediaUploadEx(mediaParams.local_img_path, media_seq, Consts.MEDIA_TYPE_AMR, afPulishInfo.mstoken, mediaParams.recordTime, mediaParams.getResize(),mediaParams.getCut(),null, mediaParams, BroadcastUtil.this);
									}else if (mediaParams.url_type == Consts.URL_TYPE_IMG ){
										new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_PIC);
//										MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_PIC);
										PalmchatLogUtils.println("----flag:mediaParams aid:"+mediaParams.aid);
										PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMediaUploadEx(mediaParams.local_img_path, media_seq, Consts.MEDIA_TYPE_JPG, afPulishInfo.mstoken, mediaParams.recordTime, mediaParams.getResize(),mediaParams.getCut(),mediaParams.getPicFilterForSend(), mediaParams, BroadcastUtil.this);
									}
								}
						}
					} else {// send text
						update_Status(afChapterInfo, AfMessageInfo.MESSAGE_SENT, code,result);
						
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_TEXT);
//						MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_TEXT);
					}	
					if( isShareBroadcast(afChapterInfo)){//如果是分享成功
						EventBus.getDefault().post(new ShareBroadcastResultEvent(  true,0));
					}
				}
			break;
			case Consts.REQ_BROADCAST_MEDIA_UPLOAD://广播多媒体发成功
				if(user_data != null){
					AfNearByGpsInfo afNearByGpsInfo = (AfNearByGpsInfo) user_data;
					if(Consts.BR_TYPE_IMAGE_TEXT == afNearByGpsInfo.type){
						afNearByGpsInfo.status = AfMessageInfo.MESSAGE_SENT; 
						
					}else if(Consts.BR_TYPE_VOICE_TEXT == afNearByGpsInfo.type){
						afNearByGpsInfo.status = AfMessageInfo.MESSAGE_SENT; 
					}
				}
				break; 
			}  
		} else {
			if (code == Consts.REQ_CODE_189) {//解析附件错误
				 PalmchatApp.getApplication().mAfCorePalmchat.AfHttpCancel(httpHandle);
			}
			if( flag==Consts.REQ_BCMSG_PUBLISH){
			 if(user_data!=null&&user_data instanceof AfChapterInfo){// 
					AfChapterInfo afChapterInfo = (AfChapterInfo) user_data; 
					EventBus.getDefault().post(new ShareBroadcastResultEvent( false,code));
					if( isShareBroadcast(afChapterInfo)){//如果是分享失败  
						 //通知分享广播界面 分享已经失败了
						 
					
						 if(  code==Consts.REQ_CODE_201||
			        		code==Consts. REQ_CODE_BROADCAST_SHARE_ERROR  
							){//如果是分享失败  并且 是因为原文广播不存在  那就删除发送失败的广播  同时删除原文那条广播
								sendUpdate_delect_BroadcastList(afChapterInfo );
								if(afChapterInfo.share_info!=null) {
									sendUpdate_delect_BroadcastList(afChapterInfo.share_info);
								}
							  return ;	
						 }
					 }
			 }
			}
			if(code == Consts.REQ_CODE_ILLEGAL_WORD){//  相关语句中含有非法词汇 
				 Consts.getInstance().showToast(context, code, flag, http_code); 
			}
			update_Status(user_data, AfMessageInfo.MESSAGE_UNSENT,code,result);//发送或分享失败了 出现重试入口
		}
	}

	/**
	 * 发广播通知 各个页面这个广播被删除了
	 *
	 * @param afChapterInfo
	 */
	private void sendUpdate_delect_BroadcastList(AfChapterInfo afChapterInfo) {
		afChapterInfo.eventBus_action = Constants.UPDATE_DELECT_BROADCAST;
		EventBus.getDefault().post(afChapterInfo);
	}
	
	public static AfChapterInfo toAfChapterInfoByMSG(String title , long currentTime,String message, byte  purview, String tag, double lat, double lng){
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		AfChapterInfo afChapterInfo = new AfChapterInfo();
		afChapterInfo.time = CacheManager.getInstance().getsendTime(currentTime)+"";
		afChapterInfo.afid = myAfProfileInfo.afId;
		afChapterInfo.mid = myAfProfileInfo.afId+"_" + getRandom_mid(currentTime);
		afChapterInfo.title = title;
		afChapterInfo.content = message;
		afChapterInfo.country = myAfProfileInfo.country;
		afChapterInfo.tag = tag;
		afChapterInfo.purview = purview;
		afChapterInfo.total_like = 0;
		afChapterInfo.type =  Consts.BR_TYPE_TEXT;
		afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
		afChapterInfo.total_comment = 0;
		afChapterInfo.profile_Info = AfProfileInfo.profileToFriend(myAfProfileInfo);
		afChapterInfo.ds_type =  Consts.DATA_FROM_LOCAL;
		afChapterInfo.lat = String.valueOf(lat);
		afChapterInfo.lng = String.valueOf(lng);
		if(!TextUtils.isEmpty(tag)){
			String[] _tags=tag.split(",");
			for(int i=0;i<_tags.length;i++){
				afChapterInfo.set_tag_list(0, 0, _tags[i], null, 0, 0, 0, false);
			}
		}
		return afChapterInfo;
	}
	
	public static AfChapterInfo toAfChapterInfoByAfMFile(String title ,long currentTime,String message,List<AfMFileInfo> picturePathList, byte  purview, byte type, String tag, double lat, double lng,String pic_rule){
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		AfChapterInfo afChapterInfo = new AfChapterInfo();
		afChapterInfo.afid = myAfProfileInfo.afId;
		afChapterInfo.mid = myAfProfileInfo.afId+"_" + getRandom_mid(currentTime);
		afChapterInfo.title = title;
		afChapterInfo.content = message;
		afChapterInfo.country = myAfProfileInfo.country;
		afChapterInfo.purview = purview;
		afChapterInfo.total_like = 0;
		afChapterInfo.tag = tag;
		afChapterInfo.list_mfile = (ArrayList<AfMFileInfo>) picturePathList;
		afChapterInfo.type = type;
		afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
		afChapterInfo.total_comment = 0;
		afChapterInfo.time = CacheManager.getInstance().getsendTime(currentTime)+"";
		afChapterInfo.profile_Info = AfProfileInfo.profileToFriend(myAfProfileInfo);
		afChapterInfo.lat = String.valueOf(lat);
		afChapterInfo.lng = String.valueOf(lng);
		afChapterInfo.ds_type =  Consts.DATA_FROM_LOCAL;
		afChapterInfo.pic_rule=pic_rule;
		if(!TextUtils.isEmpty(tag)){
			String[] _tags=tag.split(",");
			for(int i=0;i<_tags.length;i++){
				afChapterInfo.set_tag_list(0, 0, _tags[i], null, 0, 0, 0,false);
			}
		}
		return afChapterInfo;
	}
	
	public static AfChapterInfo toAfChapterInfoByAfMFile(String title ,long currentTime,String message,List<AfMFileInfo> picturePathList, byte  purview, byte type, String tag, double lat, double lng, List<AfCommentInfo> aInfos){
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		AfChapterInfo afChapterInfo = new AfChapterInfo();
		afChapterInfo.time = CacheManager.getInstance().getsendTime(currentTime)+"";
		afChapterInfo.afid = myAfProfileInfo.afId;
		afChapterInfo.mid = myAfProfileInfo.afId+"_" + getRandom_mid(currentTime);
		afChapterInfo.title = title;
		afChapterInfo.content = message;
		afChapterInfo.country = myAfProfileInfo.country;
		afChapterInfo.purview = purview;
		afChapterInfo.total_like = 0;
		afChapterInfo.tag = tag;
		afChapterInfo.list_mfile = (ArrayList<AfMFileInfo>) picturePathList;
		afChapterInfo.list_comments = (ArrayList<AfCommentInfo>) aInfos;
		afChapterInfo.type = type;
		afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
		afChapterInfo.total_comment = 0;
		afChapterInfo.profile_Info = AfProfileInfo.profileToFriend(myAfProfileInfo);
		afChapterInfo.lat = String.valueOf(lat);
		afChapterInfo.lng = String.valueOf(lng);
		return afChapterInfo;
	}


	public static AfMFileInfo toAfMFileInfoByMSG(String local_img_path, String local_thumb_path, int cid, byte url_type){
		AfMFileInfo afMFileInfo = new AfMFileInfo();
		afMFileInfo.aid = cid;
		afMFileInfo.local_img_path = local_img_path;
		afMFileInfo.local_thumb_path = local_thumb_path;
		afMFileInfo.url = "";
		afMFileInfo.url_type = url_type;
		afMFileInfo.status = AfMessageInfo.MESSAGE_SENTING;
		return afMFileInfo;
	}
	
	public static AfLikeInfo toAfLikeinfo(int _id, int aid, String like_id, String time,String afid, int status, AfFriendInfo afFriendInfo){
		AfLikeInfo afLikeInfo = new AfLikeInfo(_id, aid, like_id, time, afid, status, afFriendInfo.sex, afFriendInfo.age, afFriendInfo.name, afFriendInfo.signature, afFriendInfo.region, afFriendInfo.head_img_path,afFriendInfo.user_class,afFriendInfo.identify);
		return afLikeInfo;
	}
	
	public static AfCommentInfo toAfCommentInfo(int _id, int aid, String to_afid, String comment_id, String time,String afid,String comment, int status, AfFriendInfo afFriendInfo,String atto_sname){
		AfCommentInfo afCommentInfo = new AfCommentInfo(_id, aid, comment_id, time, afid, to_afid, comment, status, afFriendInfo.sex, afFriendInfo.age, afFriendInfo.name, afFriendInfo.signature, afFriendInfo.region, afFriendInfo.head_img_path,to_afid,atto_sname,afFriendInfo.user_class,afFriendInfo.identify);
		return afCommentInfo;
	}


	private void sendUpdateBroadcastForResult(AfChapterInfo afChapterInfo) {
		// TODO Auto-generated method stub
		afChapterInfo.eventBus_action=Constants.UPDATE_BROADCAST_MSG;
		EventBus.getDefault().post(afChapterInfo);
		/*Intent intent = new Intent(Constants.UPDATE_BROADCAST_MSG);
		intent.putExtra(Constants.BROADCAST_MSG_OBJECT, afChapterInfo);
		PalmchatApp.getApplication().sendBroadcast(intent);*/
	}
	/**
	 * 判断一个广播是否是分享的 
	 * @param afChapterInfo
	 * @return
	 */
	public static boolean isShareBroadcast(AfChapterInfo afChapterInfo){
		return afChapterInfo.share_flag!=DefaultValueConstant.BROADCAST_NOTSHARE&&
			!TextUtils.isEmpty( afChapterInfo.share_mid);
	}
	/**
	 * insert client send data
	 * 处理自己发广播 写入缓存数据库
	 * @param afChapterInfo
	 * @return
	 */
	public int ClientSendData_AfDBBCChapterInsert(AfChapterInfo afChapterInfo){
		byte ds_type = Consts.DATA_FROM_LOCAL;
		String mid = afChapterInfo.mid;
		byte type = (byte) afChapterInfo.getType();
		PalmchatLogUtils.println("AfDBBCChapterInsert, type ="+ type);
		afChapterInfo._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterInsert(ds_type, type, mid, afChapterInfo.title, afChapterInfo.time, afChapterInfo.content,  afChapterInfo.country, afChapterInfo.purview, afChapterInfo.afid, afChapterInfo.tag, 0, 0, AfMessageInfo.MESSAGE_SENTING , afChapterInfo.lat,afChapterInfo.lng, afChapterInfo.profile_Info,afChapterInfo.desc,afChapterInfo.share_mid, afChapterInfo.tagvip_pa,afChapterInfo.pic_rule, afChapterInfo.share_flag, afChapterInfo.aid,afChapterInfo.share_del,afChapterInfo.content_flag);
		if(isShareBroadcast(afChapterInfo)&&afChapterInfo.share_info!=null){
			saveDBShareInfo(afChapterInfo._id,(byte)afChapterInfo.ds_type, afChapterInfo.share_info, afChapterInfo.share_mid);
		}
		if( afChapterInfo.list_tags.size()>0){//保存tags列表
			afChapterInfo.list_tags=ServerData_AfDBListTagAttrInsert(ds_type, afChapterInfo._id, afChapterInfo.list_tags );
		}
		return afChapterInfo._id;
	}
	
	/**
	 * insert service data
	 * @param afChapterInfo
	 * @return
	 */
	public static AfChapterInfo ServerData_AfDBBCChapterInsert(byte ds_type, AfChapterInfo afChapterInfo){
//		byte ds_type = Consts.DATA_FROM_SERVER;
		String mid = afChapterInfo.mid;
		byte type = (byte) afChapterInfo.getType(); 
//		home data
		if (ds_type == Consts.DATA_HOME_PAGE) {
			afChapterInfo._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChaptersInsert(ds_type, type, mid, afChapterInfo.title, afChapterInfo.time, afChapterInfo.content,  afChapterInfo.country, afChapterInfo.purview, afChapterInfo.afid, afChapterInfo.tag, afChapterInfo.total_like, afChapterInfo.total_comment, AfMessageInfo.MESSAGE_SENTING , afChapterInfo.lat,afChapterInfo.lng, afChapterInfo.pos, afChapterInfo.profile_Info, afChapterInfo.desc,afChapterInfo.share_mid, afChapterInfo.tagvip_pa,afChapterInfo.pic_rule, afChapterInfo.share_flag,afChapterInfo.aid,afChapterInfo.share_del,afChapterInfo.content_flag);
			
//			broadcast data
		} else {
			afChapterInfo._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterInsert(ds_type, type, mid, afChapterInfo.title, afChapterInfo.time, afChapterInfo.content,  afChapterInfo.country, afChapterInfo.purview, afChapterInfo.afid, afChapterInfo.tag, afChapterInfo.total_like, afChapterInfo.total_comment, AfMessageInfo.MESSAGE_SENTING , afChapterInfo.lat,afChapterInfo.lng, afChapterInfo.profile_Info, afChapterInfo.desc,afChapterInfo.share_mid, afChapterInfo.tagvip_pa,afChapterInfo.pic_rule, afChapterInfo.share_flag,afChapterInfo.aid,afChapterInfo.share_del,afChapterInfo.content_flag);
		}
		
		
		PalmchatLogUtils.println("AfDBBCChapterInsert, _id= "+afChapterInfo._id+", content="+afChapterInfo.content);
		if (afChapterInfo._id > 0) {
			int mfile_size = afChapterInfo.list_mfile.size();
			if (mfile_size > 0) {
				afChapterInfo.list_mfile = ServerData_AfDBBCMFileInsert(ds_type, afChapterInfo._id, afChapterInfo.list_mfile);
			}
			int list_comments_size = afChapterInfo.list_comments.size();
			if (list_comments_size > 0) {
				afChapterInfo.list_comments = ServerData_AfDBListAfCommentInofInsert(ds_type, afChapterInfo._id, afChapterInfo.list_comments);
			}
			int list_likes_size = afChapterInfo.list_likes.size();
			if (list_likes_size > 0) {
				afChapterInfo.list_likes = ServerData_AfDBListAfLikeInfoInsert(ds_type, afChapterInfo._id, afChapterInfo.list_likes);
			}
			if( afChapterInfo.list_tags.size()>0){//保存tags列表
				afChapterInfo.list_tags=ServerData_AfDBListTagAttrInsert(ds_type, afChapterInfo._id, afChapterInfo.list_tags );
			}
			//------------如果是分享 把分享的文章写入到数据库
			if(isShareBroadcast(afChapterInfo)&&afChapterInfo.share_info!=null){
				saveDBShareInfo(afChapterInfo._id,ds_type,afChapterInfo.share_info,afChapterInfo.share_mid);
			 }
		} 
		return afChapterInfo;
	}
	/**
	 * 保存分享的广播中的分享内容
	 * @param ds_type
	 * @param share_info  这个参数一定是要带分享的广播内容 afChapterInfo.share_info 
	 */
	private static void saveDBShareInfo(int aid,int ds_type,AfChapterInfo share_info,String share_mid){
		    ds_type=  ds_type|Consts.AFMOBI_DATA_SOURCE_TYPE_SHARE_BASE ; 
		 	share_info._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterInsert(ds_type,
				(byte)share_info.getType(), share_mid, share_info.title,
				share_info.time, share_info.content,  share_info.country, share_info.purview, share_info.afid, share_info.tag, 
				share_info.total_like, share_info.total_comment,
				share_info.status , share_info.lat,share_info.lng, share_info.profile_Info, 
				share_info.desc,null, share_info.tagvip_pa,share_info.pic_rule, 0, aid,share_info.share_del,share_info.content_flag);

		 	if (share_info._id > 0) {
				int mfile_size = share_info.list_mfile.size();
				if (mfile_size > 0) {
					share_info.list_mfile = ServerData_AfDBBCMFileInsert(ds_type, share_info._id, share_info.list_mfile);
			     }
		 	}
		 	if( share_info.list_tags.size()>0){//保存tags列表
		 		share_info.list_tags=ServerData_AfDBListTagAttrInsert(ds_type, share_info._id, share_info.list_tags );
			}
				
	}
	/**
	 * 附件写入数据库
	 * @param ds_type
	 * @param aid
	 * @param picturePathList
	 * @param urlType
	 * @return
	 */
	public ArrayList<AfMFileInfo> AfDBBCMFileInsert(int ds_type, int aid, List<AfMFileInfo> picturePathList, byte urlType){
		ArrayList<AfMFileInfo> afMFileInfos_list = new ArrayList<AfMFileInfo>();
		int size=picturePathList.size();
		AfMFileInfo mediaParams=null;
		for (int i = size -1; i >= 0; i--) {
			mediaParams=picturePathList.get(i);
			mediaParams.aid = aid;
			if (aid > 0) {
				if (mediaParams.url_type == Consts.URL_TYPE_IMG ) {
					mediaParams._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCMFileInsert(ds_type, aid,  mediaParams.local_img_path, mediaParams.local_thumb_path, "", "", Consts.URL_TYPE_IMG, AfMessageInfo.MESSAGE_SENTING);
				}else {
					mediaParams._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCMFileInsert(ds_type, aid,  mediaParams.local_img_path, "", "", "", Consts.URL_TYPE_VOICE, AfMessageInfo.MESSAGE_SENTING);
				}
				PalmchatLogUtils.e("afMFileInfo._id ", mediaParams._id +"|aid="+aid);
				afMFileInfos_list.add(0,mediaParams);
			}
		}
		return afMFileInfos_list;
	}
	
	public static ArrayList<AfMFileInfo> ServerData_AfDBBCMFileInsert(int ds_type, int aid, List<AfMFileInfo> picturePathList){
		ArrayList<AfMFileInfo> afMFileInfos_list = new ArrayList<AfMFileInfo>();
		int size = picturePathList.size();
		AfMFileInfo mediaParams ;
		for (int i = size -1; i >= 0; i--) {
			mediaParams = picturePathList.get(i);
			mediaParams.aid = aid;
			mediaParams._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCMFileInsert(ds_type, aid,  "", "", mediaParams.url, mediaParams.thumb_url, mediaParams.url_type, AfMessageInfo.MESSAGE_SENT);
			PalmchatLogUtils.e("afMFileInfo._id ", mediaParams._id +"|aid="+aid);
			afMFileInfos_list.add(0, mediaParams);
		}
		return afMFileInfos_list;
	}
	/**
	 * 写入某广播的点赞列表
	 * @param ds_type
	 * @param aid
	 * @param listAfInfos
	 * @return
	 */
	public static ArrayList<AfLikeInfo> ServerData_AfDBListAfLikeInfoInsert(int ds_type, int aid, List<AfLikeInfo> listAfInfos){
		ArrayList<AfLikeInfo> aflikeinfo_list = new ArrayList<AfLikeInfo>();
		int size = listAfInfos.size();
		AfLikeInfo afLikeInfo ;
		for (int i = size -1; i >= 0; i--) {
			afLikeInfo = listAfInfos.get(i);
			afLikeInfo.aid = aid;
			afLikeInfo._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCLikeInsert(ds_type, aid, afLikeInfo.like_id, afLikeInfo.time, afLikeInfo.afid, AfMessageInfo.MESSAGE_SENT, afLikeInfo.profile_Info);
			PalmchatLogUtils.e("afLikeInfo._id ", afLikeInfo._id +"|aid="+aid);
			aflikeinfo_list.add(0, afLikeInfo);
		}
		return aflikeinfo_list;
	}
	/**
	 * 写入某广播的评论到数据库
	 * @param ds_type
	 * @param aid
	 * @param listAfCommentInfo
	 * @return
	 */
	public static ArrayList<AfCommentInfo> ServerData_AfDBListAfCommentInofInsert(int ds_type, int aid, List<AfCommentInfo> listAfCommentInfo){
		ArrayList<AfCommentInfo> afCommentInfo_list = new ArrayList<AfCommentInfo>();
		int size = listAfCommentInfo.size();
		AfCommentInfo afCommentInfo ;
		for (int i = size -1; i >= 0; i--) {
			afCommentInfo = listAfCommentInfo.get(i);
			afCommentInfo.aid = aid;
			afCommentInfo._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCCommentInsert(ds_type, aid, afCommentInfo.comment_id, afCommentInfo.time,afCommentInfo.afid, afCommentInfo.to_afid, afCommentInfo.comment, AfMessageInfo.MESSAGE_SENT, afCommentInfo.profile_Info);
			PalmchatLogUtils.e("afCommentInfo._id ", afCommentInfo._id +"|aid="+aid);
			afCommentInfo_list.add(0, afCommentInfo);
		}
		return afCommentInfo_list;
	}
	
	/**
	 * tags列表的写入数据库
	 * @param aid
	 * @param listAfTagInfo
	 * @return
	 */
	public static ArrayList<AfBroadCastTagInfo> ServerData_AfDBListTagAttrInsert( int ds_type,int aid, List<AfBroadCastTagInfo> listAfTagInfo){
		ArrayList<AfBroadCastTagInfo> afTagInfo_list = new ArrayList<AfBroadCastTagInfo>();
		int size = listAfTagInfo.size();
		AfBroadCastTagInfo afTagInfo ;
		for (int i =0; i < size; i++) {
			afTagInfo = listAfTagInfo.get(i);
			afTagInfo.aid = aid;
			afTagInfo.datasource_type=ds_type;
			afTagInfo._id = PalmchatApp.getApplication().mAfCorePalmchat.AfDbBCTagAttrInsert(afTagInfo ); 
			afTagInfo_list.add(  afTagInfo);
		}
		return afTagInfo_list;
	}
	
	/**
	 * 中间件返回发送结果 在这里进行处理
	 * @param user_data
	 * @param status
	 * @param code
	 */
	public void update_Status(Object user_data,int status , int code,Object result){
		if (user_data != null) {
			if (user_data instanceof AfMFileInfo) {
				AfMFileInfo afMFileInfo = (AfMFileInfo) user_data;
				int aid = afMFileInfo.aid;
				if(CacheManager.getInstance().getBC_resend_HashMap().containsKey(aid)){
					AfChapterInfo resend_afchapterinfo = CacheManager.getInstance().getBC_resend_HashMap().get(aid);
					if (code == Consts.REQ_CODE_MSTOKE_FAILURE) {
						resend_afchapterinfo.cur_count++; 
						resend_afchapterinfo.cur_count++;
						if(resend_afchapterinfo.count == resend_afchapterinfo.cur_count){//s
							resend_afchapterinfo.mstoken = "";
							PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterUpdateMstokenByID(resend_afchapterinfo.mstoken, aid);//update BC mstoken
							AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
							double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
							double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
							resend(resend_afchapterinfo, profileInfo, lon, lat);
						} 
					}else if(code == Consts.REQ_CODE_189){  //解析附件错误
							if (Consts.URL_TYPE_VOICE==afMFileInfo.url_type ) {
//								new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_VOICE);
//								MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_VOICE);
								PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMediaUploadEx(afMFileInfo.local_img_path, 1, Consts.MEDIA_TYPE_AMR, resend_afchapterinfo.mstoken, afMFileInfo.recordTime, afMFileInfo.getResize(),afMFileInfo.getCut(),null, afMFileInfo, BroadcastUtil.this);
							}else if (Consts.URL_TYPE_IMG==afMFileInfo.url_type ){
//								new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_PIC);
//								MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_PIC);
								PalmchatLogUtils.println("----flag:mediaParams aid:"+afMFileInfo.aid);
								PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMediaUploadEx(afMFileInfo.local_img_path, 1, Consts.MEDIA_TYPE_JPG, resend_afchapterinfo.mstoken, afMFileInfo.recordTime, afMFileInfo.getResize(),afMFileInfo.getCut(),afMFileInfo.getPicFilterForSend() , afMFileInfo, BroadcastUtil.this);
							} 
					}else {
						resend_afchapterinfo.cur_count++;
						if(status == AfMessageInfo.MESSAGE_UNSENT){
							resend_afchapterinfo.status = AfMessageInfo.MESSAGE_UNSENT;
						}
						//数量一致 则发送完成
						if(resend_afchapterinfo.count == resend_afchapterinfo.cur_count){//send update broadcast 
							if(resend_afchapterinfo.status == AfMessageInfo.MESSAGE_SENTING){
								//send success update broadcast
								PalmchatLogUtils.e(TAG,"----flag:send success update broadcast by aid:"+aid);
								PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterUpdateStatusByID(AfMessageInfo.MESSAGE_SENT, aid);//Annex all the DB broadcast update successful state
								resend_afchapterinfo.status = AfMessageInfo.MESSAGE_SENT;
								sendUpdateBroadcastForResult(resend_afchapterinfo);
								CacheManager.getInstance().getBC_resend_HashMap().remove(aid);//Annex all the corresponding data successfully in the remove cache
							}else{
								//send failure update broadcast
								PalmchatLogUtils.e(TAG,"----flag:send failure update broadcast by aid:"+aid);
								PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterUpdateStatusByID(AfMessageInfo.MESSAGE_UNSENT, aid);
								resend_afchapterinfo.status = AfMessageInfo.MESSAGE_UNSENT;
								sendUpdateBroadcastForResult(resend_afchapterinfo);
							}
						}
						int index = ByteUtils.indexOfAfMFileInfo(resend_afchapterinfo.list_mfile, afMFileInfo._id);
						if (index != -1) {
							//Update the cache state
							PalmchatLogUtils.e(TAG,"----flag:Update the cache state:"+status+",index="+index);
							resend_afchapterinfo.list_mfile.get(index).status = status;
						}
					}
				}
				PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCMfileUpdateStatusByID(status, afMFileInfo._id);//Annex status update
			}else if (user_data instanceof AfChapterInfo) { //Send text updates state and 4096/-code
				AfChapterInfo afChapterInfo = (AfChapterInfo) user_data;
				AfChapterInfo resend_afchapterinfo = CacheManager.getInstance().getBC_resend_HashMap().get(afChapterInfo._id);
				 
				PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterUpdateStatusByID(status, afChapterInfo._id);
				resend_afchapterinfo.status = status;
				sendUpdateBroadcastForResult(resend_afchapterinfo);
				if (status == AfMessageInfo.MESSAGE_SENT//如果发成功了、或转发成功了  那就从失败列表里删除
					 ) { 
					CacheManager.getInstance().getBC_resend_HashMap().remove(afChapterInfo._id);//Annex all the corresponding data successfully in the remove cache
				}else{
					if ( code==Consts.REQ_CODE_201||code==Consts. REQ_CODE_BROADCAST_SHARE_ERROR) {//如果转发失败 且失败原因为为广播已删除。那也从列表里删除. 从数据库删除，如果是网络原因 则可以重试
						CacheManager.getInstance().getBC_resend_HashMap().remove(afChapterInfo._id);//Annex all the corresponding data successfully in the remove cache
						PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);//从数据库删除
					} 
				}
			}
		}
	}
	
	/**
	 * 保存到发送
	 * @param afChapterInfo
	 */
	public static void saveCache(AfChapterInfo afChapterInfo){
		boolean is_exists = CacheManager.getInstance().getBC_resend_HashMap().containsKey(afChapterInfo._id);
		if(!is_exists){
			afChapterInfo.count = afChapterInfo.list_mfile.size();
			afChapterInfo.cur_count = 0;
//			afChapterInfo.status = AfMessageInfo.MESSAGE_SENTING;
			CacheManager.getInstance().getBC_resend_HashMap().put(afChapterInfo._id, afChapterInfo);
			PalmchatLogUtils.e(TAG, "saveCache,afChapterInfo._id="+afChapterInfo._id);
			
		}
	}
	
	public void resend(AfChapterInfo afChapterInfo,AfProfileInfo profileInfo, double lon, double lat){
		String mstoken = afChapterInfo.mstoken;
		afChapterInfo.ds_type =  Consts.DATA_FROM_LOCAL;
		List<AfMFileInfo> picturePathList = afChapterInfo.list_mfile;
		PalmchatLogUtils.println("-----resend:list_mfile.size="+picturePathList.size());
//		if (picturePathList.size() < 1) {
//			if (afChapterInfo.count > 0) {
//			 AfResponseComm afResponseComm = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterFindByID(afChapterInfo._id);
//			}
//		}
		saveCache(afChapterInfo);
		if(isShareBroadcast(afChapterInfo)&&afChapterInfo.share_info!=null){//如果是分享的
//			forwardBroadcast(context, afChapterInfo.share_info, afChapterInfo.content, afChapterInfo.purview, afChapterInfo.tag);
			AfProfileInfo info = CacheManager.getInstance().getMyProfile();
			 PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgPublishEX("BR_TYPE_TEXT", afChapterInfo.content,
						TextUtils.isEmpty(afChapterInfo.content)?Consts.BR_TYPE_SHARE: Consts.BR_TYPE_SHARE_TEXT , 
								afChapterInfo.purview,info.afId, 0, lon, lat, info.city, info.region, info.country, afChapterInfo.tag,PalmchatApp.getOsInfo().getCountryCode(),null,afChapterInfo.share_info.mid,null , afChapterInfo ,this);
				
		}else if (!TextUtils.isEmpty(mstoken)) {
//			List<AfMFileInfo> resned_picturePathList = CacheManager.getInstance().getFailure_BC_ListAfMFile(picturePathList);
			AfMFileInfo mediaParams;
			for (int i = 0; i < picturePathList .size(); i++) {
				mediaParams = picturePathList.get(i);
				if (mediaParams.status == AfMessageInfo.MESSAGE_UNSENT) {//多张广播图里面 发广播失败后 把失败的那几张图片重新发
				int media_seq =(i+1);
				boolean is_exists = CacheManager.getInstance().getBC_resend_HashMap().containsKey(mediaParams.aid);
				AfChapterInfo tempAfChapterInfo = CacheManager.getInstance().getBC_resend_HashMap().get(mediaParams.aid);
				int fatlure_count = CacheManager.getInstance().getFailure_BC_ListAfMFile_count(picturePathList);
				if (mediaParams != null) {
					if(is_exists){
						tempAfChapterInfo.count = fatlure_count;
						tempAfChapterInfo.cur_count = 0;
						tempAfChapterInfo.status = AfMessageInfo.MESSAGE_SENTING; 
//						CacheManager.getInstance().getBC_resend_HashMap().put(mediaParams.aid, afChapterInfo);
					}
					if (afChapterInfo.type == Consts.BR_TYPE_VOICE_TEXT || afChapterInfo.type == Consts.BR_TYPE_VOICE) {
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_VOICE);
//						MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_VOICE);
						PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMediaUploadEx(mediaParams.local_img_path, media_seq, Consts.MEDIA_TYPE_AMR, mstoken, mediaParams.recordTime,mediaParams.getResize(),mediaParams.getCut(),null, mediaParams, BroadcastUtil.this);
					}else {
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SD_BCM_PIC);
//						MobclickAgent.onEvent(context, ReadyConfigXML.SD_BCM_PIC);
						PalmchatLogUtils.println("----flag:mediaParams aid:"+mediaParams.aid);
						PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMediaUploadEx(mediaParams.local_img_path, media_seq, Consts.MEDIA_TYPE_JPG, mstoken, mediaParams.recordTime,mediaParams.getResize(),mediaParams.getCut(), mediaParams.getPicFilterForSend(), mediaParams, BroadcastUtil.this);
					}
				}
				}
			}
		}else {
//			saveCache(afChapterInfo);
			int http_id = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgPublishEX(String.valueOf( afChapterInfo.type) , afChapterInfo.content, afChapterInfo.type, afChapterInfo.purview, afChapterInfo.afid, picturePathList.size(), lon, lat, profileInfo.city, profileInfo.region,profileInfo.country, afChapterInfo.tag ,PalmchatApp.getOsInfo().getCountryCode(), null,null,afChapterInfo.pic_rule, afChapterInfo, this);
		}
		sendUpdateBroadcastForResult(afChapterInfo);
	}
	
	public ArrayList<AfMFileInfo> checkAfMfileInfo(AfChapterInfo afChapterInfo,ArrayList<AfMFileInfo> picturePathList){
		if (afChapterInfo.count != picturePathList.size()) {
			AfResponseComm afResponseComm = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCChapterFindByID(afChapterInfo._id);
			if (afResponseComm != null) {
				AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
				if (afPeoplesChaptersList != null) {
					if ( afPeoplesChaptersList.list_chapters != null) {
						AfChapterInfo db_AfChapterInfo = afPeoplesChaptersList.list_chapters.get(0);
						picturePathList = db_AfChapterInfo.list_mfile;
					}
				}
			}
		}
		return picturePathList;
	}
	
	public static String getRandom_mid(long sid) {
		// TODO Auto-generated method stub
		int id = -(new Random().nextInt(2000000000));
		return id +"_"+ sid;
	}

}



