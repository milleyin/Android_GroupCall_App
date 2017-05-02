package com.core.param;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;

public class AfRegInfoParam {
	public String cc;    
	public String imei;
	public String imsi;
	public byte sex;
	public String region;
	public String birth;
	public String phone_or_email;
	public String name;
	public String country;
	public String city;
	public String password;
	public String user_ip;
	public String voip;
	public String language;
	public String smscode;
	public String fb_token; // facebook token
	public String recommenderID; // 推荐人ID
	public AfRegInfoParam(){
		language = CommonUtils.getLocalLanguage();
		PalmchatLogUtils.println("AfRegInfoParam  language  "+language);
	}
	
}
