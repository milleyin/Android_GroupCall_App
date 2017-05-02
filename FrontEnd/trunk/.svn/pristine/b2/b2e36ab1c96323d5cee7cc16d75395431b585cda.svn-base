package com.afmobi.palmchat.ui.activity.main.model;

import java.io.Serializable;
import com.core.AfFriendInfo;

public class HomeGroupItemInfo implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -3779467703269332750L;

	// 主界面左侧group1	本地图片的icon id
	public int icon_id;
	
	public String afId;
	
	public String title;
	public String content;
	public byte gender;
	
	
	public int _id;
	public String head_img_path;
	public int type;
	public String name;
	public String status;
	public String region;
	public String signature; 
	public String user_msisdn;
	public String sex; 
	public int age; 
//	public long user_data;
	
	public int sid; //sequeue
	
	public static AfFriendInfo ToAfFriendInfo(HomeGroupItemInfo item) {
		
		AfFriendInfo info = new AfFriendInfo();
		
		info._id = item._id;
		info.afId = item.afId;
		info.head_img_path = item.head_img_path;
		info.type = item.type;
		info.name = item.title;
		info.status = item.status;
		info.region = item.region;
		info.signature = item.content;
		info.user_msisdn = item.user_msisdn;
	    info.sex = item.gender;
	    info.age = item.age;
	    info.sid = item.sid;
	    
		
		return info;
	}
	
}
