package com.core;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.core.AfGrpListInfo.AfGrpItemInfo;

public class AfGrpProfileInfo implements Serializable {

	private static final String TAG = AfGrpProfileInfo.class.getCanonicalName();

	public static class AfGrpProfileItemInfo implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		// Master
		public static final int IS_MASTER = 1;
		// Not Master
		public static final int NOT_MASTER = 0;
		
		public String afid;
		public String name;
		public String head_image_path;
		public byte sex;
	    public int invitePhoto = 1;//0表示是邀请图标，1表示不是邀请图标
	    public int age;
	    
	    public String signature;
	    
	    // 备注
	    public String alias;

	    /**
	     * 是否是群主?0 ：不是? 1：是
	     */
	    public int isMaster = 0;
		
		public AfGrpProfileItemInfo(){
		}
		
		public AfGrpProfileItemInfo(String afid, String name, String head_image_path, byte sex){
			this.afid = afid;
			this.name = name;
			this.head_image_path = head_image_path;
			this.sex = sex;
		}
		
		public String getAfidFromHead() {
			if (head_image_path != null) {
				String [] ss = head_image_path.split(",");
				if (ss.length >= 1) {
					return ss[0];
				}
			}
			return afid;
		}
		
		public String getSerialFromHead() {
			if (head_image_path != null) {
				String [] ss = head_image_path.split(",");
				if (ss.length >= 2) {
					return ss[1];
				}
			}
			return "";
		}
		
		public String getServerUrl() {
			if (head_image_path != null) {
				String [] ss = head_image_path.split(",");
				if (ss.length >= 3) {
					return ss[2].replaceAll("/d", "");
				}
			}
			return "";
		}
		
		
		public static AfProfileInfo grpProfileToProfile(AfGrpProfileItemInfo info) {
			AfProfileInfo profile = new AfProfileInfo();
			profile.afId = info.afid;
			profile.name = info.name;
			profile.head_img_path = info.head_image_path;
			profile.signature = info.signature;
			profile.sex = info.sex;
			return profile;
		}
	}
	
	public ArrayList<AfGrpProfileItemInfo> members =  new  ArrayList<AfGrpProfileItemInfo>();	//members 
	public AfGrpProfileInfo(){
	}
	
	public int _id;
	public String admin;						//admin afid
	public String afid;							//gid	
	public String name;							//group name
	public int create_time;						//group create time
	public int version;							//version code
	public int server_version;                  //server version
	public String head_image_path;				//head image url path
	public String wall;							//wallpaper url path
	public int max_count;						//max members count
	public int count;		 				    //current members count
	public int sid;	
	public boolean tips = true;						//notify tips
	public String sig;                         //群签名，保存在本地
	public int status = Consts.AFMOBI_GRP_STATUS_ACTIVE;							
	
	public String country;              //The country that the group belongs to
	public String state;                //The state that the group belongs to
	public String city;                 //The city that the group belongs to
	public String tag;                  //The group category
	public String lng;                  //The group Longitude
	public String lat;                  //The group Latitude
	public String notice;               //The group broadcast
	public int gtype;			         //group type: private=0  public=1
	
	public int isRemoved = 0;
	
	/**
	 * 在圈子中，没有被移出圈子
	 */
	public static int isNotRemoved = 0;
	
	/**
	 * 群聊消息提醒状态 0：开启 (默认) 1：关闭
	 */
	public int messageRemindIsOpenOrClose = 0;
	

	
	/**
	 * 圈子能容纳的成员数
	 */
	public int maxSize = 0;//seq
	
	public void add(String afid, String name, String head_image_path, byte sex){
		if( !TextUtils.isEmpty(afid) ){
			members.add(new AfGrpProfileItemInfo(afid, name, head_image_path, sex));
		}
	}
	
	public static AfGrpItemInfo ProfileItemToGrpItem(AfGrpProfileInfo f) {
		AfGrpItemInfo dto = new AfGrpItemInfo();
	    dto.groupid =f.afid;
	    dto.name=f.name;
	    dto.version=f.version;
	    dto.head_image_path=f.head_image_path;		    
		//dto.setFromFriend(true);
		return dto;
	}
	
	public static AfGrpProfileItemInfo FriendInfoItemToGrpProfileItemInfo(AfFriendInfo afFriendInfo) {
		AfGrpProfileItemInfo afGrpProfileItemInfo = new AfGrpProfileItemInfo();
		afGrpProfileItemInfo.afid = afFriendInfo.afId;
		afGrpProfileItemInfo.name = afFriendInfo.name;
		afGrpProfileItemInfo.head_image_path = afFriendInfo.head_img_path;
		afGrpProfileItemInfo.sex = afFriendInfo.sex;
		afGrpProfileItemInfo.signature = afFriendInfo.signature;
		afGrpProfileItemInfo.age = afFriendInfo.age;
//	    public int invitePhoto = 1;//0表示是邀请图标，1表示不是邀请图标
		return afGrpProfileItemInfo;
	}
	
	private String getToString(){
		if( null != members){
			boolean first = true;
			ArrayList<AfGrpProfileItemInfo> tmp = new ArrayList<AfGrpProfileItemInfo>(members);
			StringBuilder sb = new StringBuilder();
			for(AfGrpProfileItemInfo item:tmp){
				if( !TextUtils.isEmpty(item.afid)){
					if( first ){
						first = false;					
					}else{
						sb.append(',');
					}	
					sb.append(item.afid);
				}			
			}
			tmp = null;
			return sb.toString();
		}
		return null;
	}
	//public ArrayList<AfGrpProfileItemInfo> members =  new  ArrayList<AfGrpProfileItemInfo>();	//members 
	
	
}
