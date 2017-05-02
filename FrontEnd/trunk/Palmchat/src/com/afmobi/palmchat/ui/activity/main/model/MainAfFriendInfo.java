package com.afmobi.palmchat.ui.activity.main.model;

import java.io.Serializable;
import java.util.List;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

public class MainAfFriendInfo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2226319402540513362L;
	
	public AfFriendInfo afFriendInfo;
	public AfMessageInfo afMsgInfo;
	
	public AfGrpProfileInfo afGrpInfo;
	
	
	public static MainAfFriendInfo getMainAfFriendInfoFromAfID(String afid) {
		
		if(afid != null) {
		List<MainAfFriendInfo> mList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD).getList();
		
		for(MainAfFriendInfo info : mList) {
			if(info != null && info.afFriendInfo != null) {
			if(afid.equals(info.afFriendInfo.afId)) {
				return info;
			}
		}
		}
		
		}
		
		return null;
	}
	
	public static MainAfFriendInfo getFreqInfoFromAfID(String afid) {
		
		if(afid != null) {
		List<MainAfFriendInfo> mList = CacheManager.getInstance().getCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_FRD_REQ).getList();
		
		for(MainAfFriendInfo info : mList) {
			if(info != null && info.afFriendInfo != null) {
			if(afid.equals(info.afFriendInfo.afId)) {
				return info;
			}
			}
		}
		}
		return null;
	}
}
