package com.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AfMutualFreindInfo {
	/***********************************************************************************************************************/
 	// the thrid app type
 	/***********************************************************************************************************************/
	public static final byte AFMOBI_THRID_APP_FACEBOOK = 	(0X1);  				/*facebook*/
	public static final byte AFMOBI_THRID_APP_BBM = 		(0X1 << 1);  			/*bbm*/
	public static final byte AFMOBI_THRID_APP_TWITTER = 	(0X1 << 2);  			/*twitter*/
	public static final byte AFMOBI_THRID_APP_GOOGLE = 		(0X1 << 3);  			/*google+*/
	public static final byte AFMOBI_THRID_APP_LINKEDI = 	(0X1 << 4);  			/*linkedi*/
	
	public String afid;
	public String name;
	public byte sex;
	public int age;
	public String sign;
	public String head_img_path;
	public List<String> comm_frds;
	// is add to my friend ?  heguiming
	public boolean isAddFriend;
	public byte type;   //source type
	
	public AfMutualFreindInfo(){
		
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
	
	public static AfProfileInfo mutualFriendToProfile(AfMutualFreindInfo f) {
		AfProfileInfo dto = new AfProfileInfo();
		dto.afId=f.afid;
		dto.name=f.name;
		dto.age=f.age;
		dto.sex=f.sex;
		dto.signature=f.sign;
		dto.head_img_path=f.head_img_path;
		dto.alias=f.name;
		return dto;
	}
	
	//native used
	private void set(String afid, String name, String sign, String head_img_path, byte sex, int age, String []frds, byte type){
		this.afid = afid;
		this.name = name;
		this.sign = sign;
		this.head_img_path = head_img_path;
		this.sex = sex;
		this.age = age;
		this.type = type;
		
		if( null != frds){
			comm_frds =  Arrays.asList(frds);
		}
	}
	
}
