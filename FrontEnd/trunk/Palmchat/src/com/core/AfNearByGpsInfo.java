package com.core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.cache.CacheManager;

public class AfNearByGpsInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8507779159656066923L;
	
	public int _id;
	public String time;
	public String range;
	public String afid;
	public byte sex;
	public String name;
	public String head_img_path;
	public int age;	
	public String signature;  //signature
	public String content;   // broadcast message
	//	public String img_url;
	//	public String thumb_url;
	//	public String voice_url;
	public int status;
	public int type = -1;
	public int msg_id;
	
	public boolean voice_download_success = false;
	
	public boolean isAddFriend;
	public boolean isPlaying ;
	public boolean download_success = false ;
	

	public String url;
	public String resume_url;
	public String token;
	
	public NearbyPublicAccount[] nearbyPublicAccount;
	
	public AfNearByGpsInfo() {

	}
    
	 public AfNearByGpsInfo(int pid, String afid, byte sex, String name, String head_img_path, int age, String signature, String content,String time, String range,
			  int msg_id,int status, byte type, boolean is_friend, String url, String resume_url, String token){
		  this._id = pid;
		  this.afid = afid;
		  this.sex = sex;
		  this.name = name;
		  this.head_img_path = head_img_path;
		  this.age = age;
		  this.signature = signature;
		  this.content = content;
		  this.time = time;
		  this.range = range;
		  this.status = status;
		  this.type = type;
		  this.isAddFriend = is_friend;
		  this.url = url;
		  this.resume_url = resume_url;
		  this.token = token;
		  this.msg_id = msg_id;
	  }
	public String getSn() {
		if (head_img_path != null) {
			String[] s = head_img_path.split(",");
			if (s.length >= 3) {
				return s[1];
			}
		}
		return null;

	}
	
	public int getType() {
		/*if (null != this.img_url && !TextUtils.isEmpty(this.content)) {
			return Consts.BR_TYPE_IMAGE_TEXT;
		}else if (null != this.voice_url && !TextUtils.isEmpty(this.content)) {
			return Consts.BR_TYPE_VOICE_TEXT;
		}else if (null != this.img_url && TextUtils.isEmpty(this.content)) {
			return Consts.BR_TYPE_IMAGE;
		}else if (null != this.voice_url && TextUtils.isEmpty(this.content)) {
			return Consts.BR_TYPE_VOICE;
		}else*/ {
			return Consts.BR_TYPE_TEXT;
		}
		
	}

	public String getAfidFromHead() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 1) {
				return ss[0];
			}
		}
		return afid;
	}
	
	public String getSerialFromHead() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 2) {
				return ss[1];
			}
		}
		return "";
	}
	
	public String getServerUrl() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 3) {
				return ss[2].replaceAll("/d", "");
			}
		}
		return "";
	}

	public static AfProfileInfo NearByGpsInfoToProfile(AfNearByGpsInfo f) {
		AfProfileInfo dto = new AfProfileInfo();
		dto.afId = f.afid;
		dto.name = f.name;
		dto.age = f.age;
		dto.sex = f.sex;
		dto.head_img_path = f.head_img_path;
		dto.alias = f.name;
		// dto.setFromFriend(true);
		return dto;
	}

	@Override
	public String toString() {
		return "time:" + time + "-" + "range:" + range + "afid:" + afid + "-"
				+ "name:" + name;
	}
	
	public static AfNearByGpsInfo toAfNearByGpsInfoByMSG(long currentTime,String message){
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		AfNearByGpsInfo afNearByGpsInfo = new AfNearByGpsInfo();
		afNearByGpsInfo.time = getsendTime(currentTime)+"";
		afNearByGpsInfo.range = "0.01 km";
		afNearByGpsInfo.afid = myAfProfileInfo.afId;
		afNearByGpsInfo.sex = myAfProfileInfo.sex;
		afNearByGpsInfo.name = myAfProfileInfo.name;
		afNearByGpsInfo.head_img_path = myAfProfileInfo.head_img_path;
		afNearByGpsInfo.age = myAfProfileInfo.age;
		afNearByGpsInfo.signature = myAfProfileInfo.signature;
		afNearByGpsInfo.content = message;
		//afNearByGpsInfo.img_url = null;
		//afNearByGpsInfo.thumb_url = null;
		//afNearByGpsInfo.voice_url = null;
		afNearByGpsInfo.status =  AfMessageInfo.MESSAGE_SENTING;
		afNearByGpsInfo.msg_id = CommonUtils.getRandomId(currentTime); 
		return afNearByGpsInfo;
	}
	
	public static AfNearByGpsInfo toAfNearByGpsInfoByPictureAndText(long currentTime,String message,List<AfMFileInfo> picturePathList){
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		AfNearByGpsInfo afNearByGpsInfo = new AfNearByGpsInfo();
		afNearByGpsInfo.time = getsendTime(currentTime)+"";
		afNearByGpsInfo.range = "0.01 km";
		afNearByGpsInfo.afid = myAfProfileInfo.afId;
		afNearByGpsInfo.sex = myAfProfileInfo.sex;
		afNearByGpsInfo.name = myAfProfileInfo.name;
		afNearByGpsInfo.head_img_path = myAfProfileInfo.head_img_path;
		afNearByGpsInfo.age = myAfProfileInfo.age;
		afNearByGpsInfo.signature = myAfProfileInfo.signature;
		afNearByGpsInfo.content = message;
	//	afNearByGpsInfo.img_url = picturePathList.get(0).path;
		//afNearByGpsInfo.thumb_url = picturePathList.get(0).thumb_path;
	//	afNearByGpsInfo.voice_url = null;
		afNearByGpsInfo.status =  AfMessageInfo.MESSAGE_SENTING;
		afNearByGpsInfo.msg_id = CommonUtils.getRandomId(currentTime); 
		return afNearByGpsInfo;
	}
	
	public static AfNearByGpsInfo toAfNearByGpsInfoByVoiceAndText(long currentTime,String message,List<AfMFileInfo> voicePathList){
		AfProfileInfo myAfProfileInfo = CacheManager.getInstance().getMyProfile();
		AfNearByGpsInfo afNearByGpsInfo = new AfNearByGpsInfo();
		afNearByGpsInfo.time =  getsendTime(currentTime)+"";
		afNearByGpsInfo.range = "0.01 km";
		afNearByGpsInfo.afid = myAfProfileInfo.afId;
		afNearByGpsInfo.sex = myAfProfileInfo.sex;
		afNearByGpsInfo.name = myAfProfileInfo.name;
		afNearByGpsInfo.head_img_path = myAfProfileInfo.head_img_path;
		afNearByGpsInfo.age = myAfProfileInfo.age;
		afNearByGpsInfo.signature = myAfProfileInfo.signature;
		afNearByGpsInfo.content = message;
		//afNearByGpsInfo.img_url = null;
		//afNearByGpsInfo.thumb_url = null;
		//afNearByGpsInfo.voice_url = voicePathList.get(0).path;
		afNearByGpsInfo.status =  AfMessageInfo.MESSAGE_SENTING;
		afNearByGpsInfo.msg_id = CommonUtils.getRandomId(currentTime); 
		return afNearByGpsInfo;
	}
	
	public static String getsendTime(long currentTime){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(currentTime);
		return formatter.format(date);
	}
	
	public class NearbyPublicAccount{
		public int total;
		
		public AfProfileInfo info;
		public boolean online;   //online or not
		public int logout_time;  //the minute  from pre logout  to  now
		public String distance;
		public int pos;
		public NearbyPublicAccount(){}
	}
}
