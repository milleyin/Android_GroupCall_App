package com.afmobi.palmchat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobigroup.gphone.R;
 

public class OSInfo {
	private String imei;
	private String imsi;
	private String mcc;
	private String mnc;
	private String smsc;
	private String user;
	private String veri;
	private String osVersion;
	private boolean imsiFlag;
	private String ua;
	private String mver;
	private String cid;
 	private String countryCode;
	private String srcCountryCode;
	private int width;
	private int height;
	private String brand;
	private String country;
	private String city;
	private String state;
	private double lat;
	private double lng;
	private String cver;
	private String afid_mcc;
	
	public String getAfid_mcc() {
		if(PalmchatLogUtils.SHENZHEN_VERSION){
			return "460";
		}
		return afid_mcc;
	}

	public void setAfid_mcc(String afid_mcc) {
		
		this.afid_mcc = afid_mcc;
	}

	public String getCver() {
		return cver;
	}

	public void setCver(String cver) {
		this.cver = cver;
	}

	public String getBrand() {
		return brand;
	}
	public boolean isTecno(){
		if (!TextUtils.isEmpty(brand)) { 
			if (brand.equalsIgnoreCase("TECNO")||
					brand.endsWith("alps")||brand.equalsIgnoreCase("Infinix")
					||brand.equalsIgnoreCase("Spreadtrum")
					){
				return true;
			}
		} 
		return false;
	}
	public void setBrand(String brand) {
		this.brand = brand;
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public OSInfo() {
//		this.countryCode = "251";
		this.mcc = DefaultValueConstant.MCC;
		this.mnc = DefaultValueConstant.MNC;
		this.imsi =  DefaultValueConstant.IMSI;
		this.ua =  DefaultValueConstant.UA;
		this.mver =  DefaultValueConstant.MVER; 
		this.osVersion =  DefaultValueConstant.OS_VERSION;
		this.cid =  DefaultValueConstant.CID;
		this.smsc =  DefaultValueConstant.SMSC;
		this.width =  DefaultValueConstant.WIDTH;
		this.height =  DefaultValueConstant.HEIGHT;
	}
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getOsVersion() {
		return osVersion;  
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public boolean isImsiFlag() {
		return imsiFlag;
	}
	public void setImsiFlag(boolean imsiFlag) {
		this.imsiFlag = imsiFlag;
	}
	public String getImei() { 
		return imei;
	}
	public void setImei(String imei) {
			this.imei = imei;
	}
	public String getImsi() {
		/*if(PalmchatLogUtils.DEBUG){调试proxy版本用
			 	imsi="62150123456789";//"460";//62150
		}*/
		return imsi;
	}
	
	public void setImsi(String imsi) {
		if (imsi != null && imsi.length() >= 5) {
			this.imsi = imsi;
			mcc = imsi.substring(0 , 3);
			mnc = imsi.substring(3, 5);
			imsiFlag = true;
			PalmchatLogUtils.println("PalmchatApp  mcc  "+mcc+"  mnc  "+mnc);
		}
		PalmchatLogUtils.println("PalmchatApp  imsi  "+imsi);
	}
	
	public String getMcc() {
	 if(PalmchatLogUtils.SHENZHEN_VERSION){
			return "460";
		}
		if(mcc!=null&&mcc.equals("460")){
			return "";
		} 
		if(!TextUtils.isEmpty(mcc)){//如果MCC里有字母的 就返回""
			String regex=".*[a-zA-Z]+.*";
			Matcher m=Pattern.compile(regex).matcher(mcc);
			if(m.matches()){
				return "";
			}
		}
		return mcc; 
//		return "460"; 
	}
	
	
	public String getRealMcc() {
		return mcc;
	}
	
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getSmsc() {
		return smsc;
	}
	public void setSmsc(String smsc) {
		this.smsc = smsc;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getVeri() {
		return veri;
	}
	public void setVeri(String veri) {
		this.veri = veri;
	}
	public String getUa() {
		return ua;
	}
	public void setUa(String ua) {
		this.ua = ua;
		this.mver = ua;
		if (DefaultValueConstant.SDK.equalsIgnoreCase(ua)) {
			this.mcc = DefaultValueConstant.MCC;
			this.mver = brand;
		}
	}
	public void setMver(String mver) {
		this.mver = mver;
	}
	public String getMver() {
		return mver;
	}

	public void setCountryCode(String countryCode) {
		if (TextUtils.isEmpty(countryCode)) {
			this.countryCode = DefaultValueConstant.COUNTRY_CODE;
		} else {
			this.countryCode = countryCode;
		}
		
		srcCountryCode = countryCode;
	}
	
	public String getCountryCode2() {
		return srcCountryCode;
	}

	public String getCountryCode() {
		PalmchatLogUtils.println("getCountryCode  "+countryCode);
		return (countryCode == null || "".equals(countryCode)) ? DefaultValueConstant.COUNTRY_CODE : countryCode;
	}
	
	public String getCountry(Context context) {
		if (country == null) {
			return DefaultValueConstant.OVERSEA;//context.getString(R.string.oversea); 这里不能从R.string获取 不然会有多国语言问题
		}
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity(Context context) {
		if (city == null) {
			return DefaultValueConstant.OTHER;// context.getString(R.string.other);这里不能从R.string获取 不然会有多国语言问题
		}
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState(Context context) {
		if (state == null) {
			return DefaultValueConstant.OTHERS;//context.getString(R.string.others);这里不能从R.string获取 不然会有多国语言问题
		}
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public void setLatitude(double lat) {
		// TODO Auto-generated method stub
		this.lat = lat;
	}
	
	public double getLatitude() {
		// TODO Auto-generated method stub
		return lat;
	}

	public void setLongitude(double lng) {
		// TODO Auto-generated method stub
		this.lng = lng;
	}
	
	public double getLngtitude() {
		// TODO Auto-generated method stub
		return lng;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub'
		PalmchatLogUtils.println("imei  "+imei+"  imsi  "+imsi+"  mcc  "+mcc+"  mnc  "
				+"  smsc  "+smsc+"  user  "+user
				+"  veri  "+veri+"  osVersion  "+osVersion
				+"  imsiFlag  "+imsiFlag+"  ua  "+ua
				+"  mver  "+mver+"  cid  "+cid
				+"  countryCode  "+countryCode+"  width  "+width
				+"  height  "+height+"  brand  "+brand
				+"  country  "+country+"  city  "+city
				+"  state  "+state+"  lat  "+lat
				+"  lng  "+lng+"  cver  "+cver);
		return super.toString();
	}
	
}