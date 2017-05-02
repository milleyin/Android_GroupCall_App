package com.core;

import java.io.Serializable;
import java.util.ArrayList;

import com.core.AfGrpProfileInfo.AfGrpProfileItemInfo;

import android.text.TextUtils;

public class AfGrpListInfo {

	public static class AfGrpItemInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String title;
		public String content;
		public String groupid;
		public String name;
		public String head_image_path;
		public int version;
		public boolean is_admin;
		public String sig;
		public String notice;
		
		public AfGrpItemInfo(){}
		public AfGrpItemInfo(String groupid, String name, String head_image_path, String sig, int version, boolean is_admin){
			this.groupid = groupid;
			this.name = name;
			this.head_image_path = head_image_path;
			this.version  = version;
			this.is_admin = is_admin;
			this.sig = sig;
		}
		
		public static AfGrpProfileInfo GrpItemToProfileItem(AfGrpItemInfo f) {
			AfGrpProfileInfo dto = new AfGrpProfileInfo();
		    dto.afid =f.groupid;
		    dto.name=f.name;
		    dto.version=f.version;
		    dto.head_image_path=f.head_image_path;		    
			//dto.setFromFriend(true);
			return dto;
		}
		
		
	}
	

	public int group_count;
	public int group_version;
	public int group_count_public;
	public int group_version_public;
	public ArrayList<AfGrpItemInfo> members = new ArrayList<AfGrpItemInfo>();
	public ArrayList<AfGrpItemInfo> members_public = new ArrayList<AfGrpItemInfo>();
	
	public AfGrpListInfo(){
		
	}
	public AfGrpListInfo(int group_count, int group_version){
		this.group_count = group_count;
		this.group_version = group_version;
	}
	public void add(String afid, String name, String head_image_path, String sig,  int version, boolean is_admin){
		if(!TextUtils.isEmpty(afid)){
			members.add(new AfGrpItemInfo(afid, name, head_image_path, sig, version, is_admin));
		}
	}
	
	public void add_public(String afid, String name, String head_image_path, String sig,  int version, boolean is_admin, String notice){
		if(!TextUtils.isEmpty(afid)){
			AfGrpItemInfo itemInfo = new AfGrpItemInfo(afid, name, head_image_path, sig, version, is_admin);
			itemInfo.notice = notice;
			members_public.add(itemInfo);
		}
	}
}
