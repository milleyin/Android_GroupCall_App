package com.afmobi.palmchat.constant;


import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;

public class RequestType {
	public static String dispatchUrl = getMccDispateURL();
	public static final String SERVER_TEST = "TEST";
	public static final String SERVER_ONLINE = "ONLINE";
	public static final String SERVER = SERVER_ONLINE;  //
	public static final byte SEARCH_USER = 12;
	public static final byte STATE = 45;
	public static  String STATE_URL () {
		return dispatchUrl + "/totallist/";
	}
	
	public static final byte REGION_AND_CITY = 46;
	public static  String REGION_AND_CITY_URL () {
		return dispatchUrl + "/city/?country=";
	}
	
	public static final byte MY_COUNTRY_INFO = 47;
	public static  String MY_COUNTRY_INFO_URL () {
		return dispatchUrl + "/mycountry/?gps=1";// by the way get gps LAT LNG
	}
	
	public static final byte CHATTING_ROOM_PUT = 48;
	
	public static final byte MUTUAL_FRIENDS = 55;
	
	public static String getMccDispateURL() {
		dispatchUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfGetDispatchUrl();
		PalmchatLogUtils.e("getMccDispateURL  dispatchUrl  " ,dispatchUrl);
		return dispatchUrl;
	}
	
	public static String getDispateURL() {
		dispatchUrl = PalmchatApp.getApplication().mAfCorePalmchat.AfGetDispatchUrl();
		PalmchatLogUtils.e("getDispateURL  dispatchUrl  ",dispatchUrl);
		return dispatchUrl;
	}
	
}