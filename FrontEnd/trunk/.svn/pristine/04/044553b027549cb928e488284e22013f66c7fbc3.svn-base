package com.core;

import android.text.TextUtils;

import java.io.Serializable;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.model.HomeGroupItemInfo;
import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;
import com.core.AfResponseComm.AfPeopleInfo;


public  class AfFriendInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2631765556328286029L;
	
	public static final String MALE = "M";
	public static final String FEMALE = "F";
	
    /**

     * 是否是邀请图
     * 0 : 是  1 不是 

     * 是否是邀请图
     * 0 : 1不是 2:群发成员

     */
    public int invitePhoto = 1;

    /**
     * 是否是群0 ：不 1：是
     */
    public int isMaster = 0;
	
	
	public int _id;
	public String afId;
	public String head_img_path;
	public int type;
	public String name;
	public String status;
	public String region;
	public String signature;
	public String alias;
	public String user_msisdn;
	public byte sex;
	public int age;
	public int sid; //sequeue
	public boolean is_new_contact;
	// is add to my friend ?   heguiming
	public boolean isAddFriend;
	
	public boolean isFollow;
	public int follow_type;
	
	public String phone; /* bind phone */
	public String phone_cc;
	
	public int user_class = 1;		/*1 : normal use 2: public account*/
	
	public boolean identify = false;

    public boolean follow_opr;		/*public account : follow (0:yes ,1:not)*/
    public boolean unfollow_opr;		/*public account : unfollow (0:yes ,1:not)*/
	
	public AfFriendInfo(){
		
	}
	
	public String getAfidFromHead() {
		if (head_img_path != null) {
			String [] ss = head_img_path.split(",");
			if (ss.length >= 1) {
				return ss[0];
			}
		}
		return afId;
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
	
	public static AfProfileInfo friendToProfile(AfFriendInfo f) {
		AfProfileInfo dto = new AfProfileInfo();
		dto.afId=f.afId;
		dto.name=f.name;
		dto.age=f.age;
		dto.sex=f.sex;
		dto.signature=f.signature;
		dto.user_msisdn=f.user_msisdn;
		dto.head_img_path=f.head_img_path;
		dto.alias=f.name;
		dto.phone = f.phone;
		dto.phone_cc = f.phone_cc;
		dto.user_class = f.user_class;
		dto.identify = f.identify;
		//dto.setFromFriend(true);
		return dto;
	}
	
	public static AfGrpProfileItemInfo ProfileItemInfoToFriend(AfFriendInfo info) {
		AfGrpProfileItemInfo dto = new AfGrpProfileItemInfo();
	    dto.afid=info.afId;
	    dto.name=info.name;
	    dto.sex=info.sex;
	    dto.age = info.age;
	    dto.alias=info.alias;
		return dto;
	}
	
	public static AfFriendInfo FriendInfoToGrpProfileItemInfo(AfGrpProfileItemInfo dto) {
		AfFriendInfo info = new AfFriendInfo();
	    info.afId = dto.afid;
	    info.name = dto.name;
	    info.sex = dto.sex;
	    info.signature = dto.signature;
	    info.head_img_path = dto.head_image_path;
		return info;
	}
	
	public static HomeGroupItemInfo friendToHomeGroupItemInfo(AfFriendInfo f) {

		HomeGroupItemInfo gItemInfo2 = new HomeGroupItemInfo();


		gItemInfo2._id =  f._id;
		gItemInfo2.afId = f.afId;
		gItemInfo2.head_img_path = f.head_img_path;
		gItemInfo2.type = f.type ;
	    gItemInfo2.title = f.name ;
	    gItemInfo2.status = f.status;
	    gItemInfo2.region = f.region;
	    gItemInfo2.content = f.signature;
	    gItemInfo2.user_msisdn = f.user_msisdn;
	    gItemInfo2.gender = f.sex;
	    gItemInfo2.age = f.age;
	    gItemInfo2.sid = f.sid;
	
	    
	    return gItemInfo2;
	}
	
	public static AfFriendInfo FriendInfoToAfChatroomMemberInfo(AfChatroomMemberInfo dto) {
		AfFriendInfo info = new AfFriendInfo();
	    info.afId = dto.afid;
	    info.name = dto.name;
	    info.sex = dto.sex;
	    info.signature = dto.sign;
	    info.age = dto.age;
		return info;
	}
	
	public static AfProfileInfo giftInfoToProfile(AfGiftInfo afGiftInfo) {
		AfProfileInfo dto = new AfProfileInfo();
		dto.afId=afGiftInfo.afid;
		dto.name=afGiftInfo.name;
		dto.age=afGiftInfo.age;
		dto.sex=afGiftInfo.sex;
		dto.head_img_path=afGiftInfo.head_image_path;
		dto.alias=afGiftInfo.name;
		//dto.setFromFriend(true);
		return dto;
	}
	
	
	public static AfProfileInfo PeopleInfoToProfile(AfPeopleInfo peopleInfo) {
		AfProfileInfo dto = new AfProfileInfo();
		dto.afId = peopleInfo.afid;
		dto.name = peopleInfo.name;
		dto.age = peopleInfo.age;
		dto.sex = peopleInfo.sex;
		dto.head_img_path = peopleInfo.head_img_path;
		//dto.setFromFriend(true);
		return dto;
	}
}