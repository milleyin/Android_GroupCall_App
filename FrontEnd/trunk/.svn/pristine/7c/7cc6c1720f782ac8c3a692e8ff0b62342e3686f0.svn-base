package com.afmobi.palmchat.ui.activity.social;

import java.io.Serializable;

import com.core.AfData;
import com.core.AfData.BcNotify;
import com.core.AfProfileInfo;
import com.core.AfResponseComm.AfPeopleInfo;

public class BroadcastNotificationData implements Serializable {
	/**
	 * 广播通知的类型
	 */
	public static final int ITEM_TYPE_LIKE = 0;
	public static final int ITEM_TYPE_COMMENT = 1;
	public static final int ITEM_TYPE_FRIENS_NEW = 2;//5.1 add Friend circle notification
	
	/**
	 * 原文类型  //0  普通文字   //1  图片+文字 //2  语音+文字  //3  图片  //4  语音
	 */
	public static final int ORIGINAL_TYPE_TEXT=0;//
	public static final int ORIGINAL_TYPE_IMG_TEXT=1;
	public static final int ORIGINAL_TYPE_VOICE_TEXT=2;
	public static final int ORIGINAL_TYPE_IMG=3;
	public static final int ORIGINAL_TYPE_VOICE=4;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4547339037298365210L;
	
    /**
     * custom data
     */
	public boolean isAggregate;
	public String displayContent;
	
	
	/**
	 * read or unread
	 */
	public int status;
	
	public static final int STATUS_UNREAD = 0;
	public static final int STATUS_READ = 1;
	
	/**
	 * server response data
	 */
	public String afid;
	public String mid;
	public long ts;
	public String msg;
	public int type;
	
	
	public byte sex;
	public int age;
	public String name;
	public String sign;
	public String region;
	public String head_img_path;
	public String at_safid;//@XX的id
	public String at_sname;//@XX的name
	public byte original_type;//原文类型
	public String original_content;//原文内容
	
	public int user_class;//用户类型  为2为公众账号
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
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (afid != null) {
			return afid.equals(((BroadcastNotificationData)o).afid);
		} else {
			return super.equals(o);
		}
	}
	
	public static AfProfileInfo PeopleInfoToBrdNotifyData(BroadcastNotificationData peopleInfo) {
		AfProfileInfo dto = new AfProfileInfo();
		dto.afId = peopleInfo.afid;
		dto.name = peopleInfo.name;
		dto.age = peopleInfo.age;
		dto.sex = peopleInfo.sex;
		dto.head_img_path = peopleInfo.head_img_path;
		dto.user_class=peopleInfo.user_class;
		//dto.setFromFriend(true);
		return dto;
	}
	
	public static BroadcastNotificationData AfData2BrdNotifyData(BcNotify bcNotify) {
		BroadcastNotificationData sd = new BroadcastNotificationData();
		if (bcNotify != null) {
			sd.afid = bcNotify.afId;
			sd.mid = bcNotify.mid;
			sd.type = bcNotify.type;
			sd.status = bcNotify.status;
			sd.ts = bcNotify.server_time;
			sd.msg = bcNotify.msg;
			sd.name = bcNotify.name;
			sd.age = bcNotify.age;
			sd.sex = bcNotify.sex;
			sd.sign = bcNotify.sign;
			sd.region = bcNotify.region;
			sd.head_img_path = bcNotify.head_img_path;
//			sd.original_type=bcNotify.o
			sd.at_safid=bcNotify.safid;
			sd.at_sname=bcNotify.name;//@XX的name
			sd.original_type=bcNotify.content_type;//原文类型
			sd.original_content=bcNotify.content;//原文内容
//			sd.user_class=bcNotify.user_class;
		}
		
		return sd;
	}

}
