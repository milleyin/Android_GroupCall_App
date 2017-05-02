package com.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfResponseComm.AfPeopleInfo;


public  class AfProfileInfo extends AfFriendInfo {
	public List<String> serials = new ArrayList<String>();
	public String attr1;	//old password of updating by imei
	public String attr2;	//new password of updating by imei
	public boolean is_new_user;  /*if afid ?, if public account, identification*/
	public boolean is_default_pwd; //send notification of updating by imei
	public String religion;
	public String local_img_path; 
	public String birth; 
	public String country;
	public String city; 
	public String profession; 
	public String hobby; /*if afid hobby, if public account, introduce*/
	public String school; /*if afid, school, if public account, url*/	
	public boolean is_bind_email; 
 	public boolean is_bind_phone; /* bind phone */
//	public boolean has_secure_answer;
	public String email; 
	public String secureAnswer;
	public String imsi;  /*if afid, imsi, if public account, people count*/	
	public String user_msisdn;
	
	public String fdsn;					/*friend list sn*/
	public String pbsn;					/*phonebook list sn*/
	public String blsn;                 // blocklist sn
	public String wall_paper;			/*wall paper ID*/
	public int gpsn;					/*if afid, group list sn, if public account, public account type*/
	public int gmax;				/*maximun create group numbers*/
	public int gmaxman;				/*max people in per group*/
	public int level;				/*personal level*/
	public int credit;				/*personal credit*/
	public int coin;				/*personal coin*/	
	public AfDatingInfo dating;     /*dating relative info*/
	public boolean has_new_public;		/*has new public, if public account*/
	
	public boolean isChange;		// is changed
	public boolean isPhoneChange;		// is phone changed
	
	public int login_type;
    public int follow_count;     // follow count
    public int followers_count;    //followers count
    public String lat;					/*lat*/
	public String lng;					/*lng*/
	
	public String fbid;					/*facebookid*/
	
	//public ArrayList<PayInfo> payInfo = new ArrayList<PayInfo>();
	public   String fanclub_url;
	public int palmguess_flag;
	public int palmguess_version; // palmguess有新活动录入并通过审核后，会更新此版本号。缓存时间5分钟
	public int airtime;          // ”1”表示开启airtime充值，0表示关闭
	public String recharge_intro_url; // 充值说明url
	public String              error_url; // 失败状态页面 URL
	public String              order_list_url; // 订单历史记录页面 URL
	public String              order_detail_url; // 订单详情页面 URL
	public String              password_url; // 设置，修改，重置密码页面URL
	public ArrayList<PalmCoinMenuItemInfo> palmcoin_menu_list = new ArrayList<PalmCoinMenuItemInfo>();
	public ArrayList<PalmCoinMenuItemInfo> palmcoin_walletlayout_list = new ArrayList<PalmCoinMenuItemInfo>();
	public ArrayList<PalmCoinMenuItemInfo> palmcoin_recharge = new ArrayList<PalmCoinMenuItemInfo>();
	
	
	public int type1;				/*public account : type1(tag1)*/
	public int type2;				/*public account : type2(tag2)*/
	public int type3;				/*public account : type3(tag3)*/


	public String telephone;		/*public account : telephone*/
	public String address;			/*public account : address*/
	public String hours;			/*public account : hours*/
	
	public String justalkId;       // palmcallId

    public int betway = 0;       // betway模块开关，"1"表示可用，"0"表示不可用
	
	public String recommenderID; // 推荐人ID
	
//	typedef struct _AFMOBI_PALMCOIN_MENU_ITEM_INFO_
//	{
//		CHAR*  name;      // 显示名称
//		CHAR*  icon;      // 菜单项左侧 icon URL
//		CHAR*  url;       // 入口URL,若为空则表示下一页为客户端内容
//		INT32S status;    // "1"表示可用，"2"表示维护升级中
//		INT32S type;      //  AFMOBI_PALMCOIN_MENU_TYPE_MENU,AFMOBI_PALMCOIN_MENU_TYPE_WALLETLAYOUT_MENU,AFMOBI_PALMCOIN_MENU_TYPE_WALLETLAYOUT_RECHARGEURL
//	}AFMOBI_PALMCOIN_MENU_ITEM_INFO, *AFMOBI_PALMCOIN_MENU_ITEM_INFO_PTR;
	
	public final static int  AFMOBI_PALMCOIN_MENU_TYPE_MENU = 1;
	public final static int  AFMOBI_PALMCOIN_MENU_TYPE_WALLETLAYOUT_MENU = 2;
	public final static int  AFMOBI_PALMCOIN_MENU_TYPE_WALLETLAYOUT_RECHARGEURL = 3;
	public class PalmCoinMenuItemInfo implements Serializable {
		private static final long serialVersionUID = -2194190107492258615L;
		public String name;
		public String icon;
		public String url;
		public int status;
		public int type;
		public PalmCoinMenuItemInfo(){}
	}
	public void setPalmCoinMenuItemInfo(String name,String icon,String url,int status, int type)
	{
		PalmCoinMenuItemInfo info = new PalmCoinMenuItemInfo();
		info.name = name;
		info.icon = icon;
		info.url = url;
		info.status = status;
		info.type = type;
		
		if(type == AFMOBI_PALMCOIN_MENU_TYPE_MENU)
		{
			palmcoin_menu_list.add(info);
		}
		else if(type == AFMOBI_PALMCOIN_MENU_TYPE_WALLETLAYOUT_MENU)
		{
			palmcoin_walletlayout_list.add(info);
		}
		else if(type == AFMOBI_PALMCOIN_MENU_TYPE_WALLETLAYOUT_RECHARGEURL)
		{
			palmcoin_recharge.add(info);
		}
		
	}
	public void setAttr1(String attr1) {
		if (attr1 != null && attr1.length() > 0) {
			this.serials.clear();
			String [] ss = attr1.split(",");
			for (String s : ss) {
				this.serials.add(s);
			}
		}
		this.attr1 = attr1;
	}

	public String password;
	public String third_account;
	public int login_status = AfLoginInfo.AFMOBI_LOGIN_INFO_NORMAL;
	
	public List<String> getSerials() {
		if (this.serials == null) {
			this.serials = new ArrayList<String>();
		}
		return this.serials;
	}
	
	public void serialToAttr1()
	{
		StringBuffer sb = new StringBuffer();
		if (serials != null) {
			for (int i = 0; i <  this.serials.size(); i++) {
				String serial = serials.get(i);
				if (i == this.serials.size() - 1) {
					sb.append(serial);
				} else {
					sb.append(serial);
					sb.append(",");
				}
			}
		}
		
		this.attr1 = sb.toString();
	}
	
	public AfProfileInfo(){
		dating  = new AfDatingInfo();
	}
	public void setDating(AfDatingInfo dating){
		this.dating = dating;
	}
	
//	public class PayInfo{
//		public String code;
//		public String paytypename;
//		public String sname;
//		public int extra;
//		public String front_url;
//		public String front_url1;
//		public PayInfo(){}
//	}
//	public void setPayInfo(String code,String paytypename,String sname,int extra,String f_url,String f_url1)
//	{
//		PayInfo info = new PayInfo();
//		info.code=code;
//		info.paytypename=paytypename;
//		info.sname=sname;
//		info.extra=extra;
//		info.front_url=f_url;
//		info.front_url1=f_url1;
//		payInfo.add(info);
//	}
	public String getBirthNew(Context context) {
		String arrays[] = getShowDate();
		if (arrays != null) {
			if(null != arrays && 3 == arrays.length){
				String year = arrays[0];
				String month = arrays[1];
				String day = arrays[2];
				if (year != null && year.length() == 4) {
					return day+"-"+month+"-"+year;
				}
				return year+"-"+month+"-"+day;
			}
		}
		return "5-1-1990";
	}
	
	
	public String getUploadBirth(Context context) {
		String arrays[] = getShowDate();
		if (arrays != null) {
			String year = arrays[0];
			String month = arrays[1];
			String day = arrays[2];
			if (year != null && year.length() == 4) {
				return year + "-" + month + "-" + day;
			}
			return day + "-" + month + "-" + year;
		}
		return "1990-1-5";
	}
	
	/**
	 * add by zhh
	 * @param context
	 * @param birth
	 * @return
	 */
	public static String getUploadBirth(Context context,String birth) {
		String result;
		if(!"".equals(birth) && null != birth){
			String[] arrays = birth.split("-");
			if (arrays != null && arrays.length == 3) {
				String year = arrays[0];
				String month = arrays[1];
				String day = arrays[2];
				if (year != null && year.length() == 4) {
					return year + "-" + month + "-" + day;
				}
				return day + "-" + month + "-" + year;
			}
		}
		return "1990-1-5";
	}
	
	public String[] getShowDate() {
		String birthNew = birth;
		if(!"".equals(birthNew) && null != birthNew){
			String[] arrays = birthNew.split("-");
			if (arrays != null && arrays.length == 3) {
				return arrays;
			}
		}
		return null;
	}
	
	public int getYear() {
		String arrays[] = getShowDate();
		String year = "1990";
		if (arrays != null) {
			String tmp = arrays[0];
			if (tmp != null && tmp.length() == 4) {
				year = tmp;
			} else {
				year = arrays[2];
			}
		}
		return Integer.parseInt(year);
	}
	
	public int getMonth() {
		String arrays[] = getShowDate();
		if (arrays != null) {
			String month = arrays[1];
			int m = Integer.parseInt(month);
			return m;
		}
		return 1;
	}
	
	public int getDay() {
		String arrays[] = getShowDate();
		String day = "5";
		if (arrays != null) {
			String tmp = arrays[0];
			if (tmp != null && tmp.length() == 4) {
				day = arrays[2];
			} else {
				day = tmp;
			}
		}
		return Integer.parseInt(day);
	}
	
	public String showAfid() {
		if(CommonUtils.isEmpty(afId)){
			return "";
		}
		return afId.substring(1);
	}
	
	public static AfFriendInfo profileToFriend(AfProfileInfo info) {
		AfFriendInfo dto = new AfFriendInfo();
		if (info != null) {
			dto.afId=info.afId;
			dto.name=info.name;
			dto.age=info.age;
			dto.sex=info.sex;
			dto.signature=info.signature;
			dto.user_msisdn=info.user_msisdn;
			dto.head_img_path=info.head_img_path;
		}
		return dto;
	}
	
	public static AfFriendInfo NearByPeople2Profile(AfPeopleInfo people) {
		AfFriendInfo profile = new AfFriendInfo();
		if (people != null) {
		profile.afId = people.afid;
		profile.sex= people.sex;
		profile.age = people.age;
		profile.name = people.name;
		profile.sex= people.sex;
		profile.signature = people.sign;
		profile.head_img_path= people.head_img_path;
		}
		return profile;
	}


	@Override
	public String toString() {
		return "AfProfileInfo [alias=" + alias + ", attr1=" + attr1
				+ ", attr2=" + attr2 + ", is_new_user=" + is_new_user
				+ ", is_default_pwd=" + is_default_pwd + ", religion="
				+ religion + ", local_img_path=" + local_img_path + ", birth="
				+ birth + ", country=" + country + ", city=" + city
				+ ", profession=" + profession + ", hobby=" + hobby
				+ ", school=" + school + ", is_bind_email=" + is_bind_email
				+ ", is_bind_phone=" + is_bind_phone + ", email=" + email
				+ ", phone=" + phone + ", imsi=" + imsi + ", user_msisdn="
				+ user_msisdn + ", password=" + password + ", _id=" + _id
				+ ", afId=" + afId + ", head_img_path=" + head_img_path
				+ ", type=" + type + ", name=" + name + ", status=" + status
				+ ", region=" + region + ", signature=" + signature + ", sex="
				+ sex + ", age=" + age + "]";
	}

	/**
	 * get a copy of this profile
	 * @return
	 */
	public AfProfileInfo deepClone() {
		ObjectOutputStream oo = null;
		ObjectInputStream oi = null;
		try {
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bo);
			oo.writeObject(this);
			ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
			oi = new ObjectInputStream(bi);
			return((AfProfileInfo)oi.readObject());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(oo != null) {
					oo.close();
				}
				if(oi != null) {
					oi.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				
			}
		}
	}

	public String getShowHobby(Context context) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(hobby)){
			return null;
		}
		final String[] ch = context.getResources().getStringArray(R.array.hobby);
		final String[] ch_en = context.getResources().getStringArray(R.array.hobby_en);
		int index = indexOf(ch_en, hobby);
		if(index != -1){
			return ch[index];
		}else{
			index = indexOf(ch, hobby);
			if(index != -1){
				return ch[index];
			}
		}
		return null;
	} 
	
	public String getShowProfession(Context context) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(profession)){
			return null;
		}
		final String[] ch = context.getResources().getStringArray(R.array.profession);
		final String[] ch_en = context.getResources().getStringArray(R.array.profession_en);
		int index = indexOf(ch_en, profession);
		if(index != -1){
			return ch[index];
		}else{
			index = indexOf(ch, profession);
			if(index != -1){
				return ch[index];
			}
		}
		return null;
	} 
	
	public String getShowReligion(Context context) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(religion)){
			return null;
		}
		final String[] ch = context.getResources().getStringArray(R.array.religion);
		final String[] ch_en = context.getResources().getStringArray(R.array.religion_en);
		int index = indexOf(ch_en, religion);
		if(index != -1){
			return ch[index];
		}else{
			index = indexOf(ch, religion);
			if(index != -1){
				return ch[index];
			}
		}
		return null;
	} 
	
	static int indexOf(final String[] ch, String target) {
		if (!TextUtils.isEmpty(target)) {
			for (int i = 0; i < ch.length; i++) {
				if (target.equals(ch[i])) {
					return i;
				}
			}
		}
		return -1;
	}
}
