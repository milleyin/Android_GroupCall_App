package com.core.param;

import java.util.ArrayList;

import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.Consts;

public class AfCacheParam {
	//friend, block friend and stranger friend
	public ArrayList<ArrayList<AfFriendInfo>>frd_cache;
	public ArrayList<AfGrpProfileInfo>grp;
	public ArrayList<AfGrpProfileInfo>public_grp;
	public ArrayList<AfMessageInfo>recent_msg;
	public ArrayList<AfMessageInfo>frd_req_msg;
	public ArrayList<AfFriendInfo>public_account;
	public ArrayList<AfFriendInfo>follow_master;
	public ArrayList<AfFriendInfo>follow_passive;
	
	
	public boolean hasNewPb;
	
	public int msg;
	
	public AfCacheParam(){
		int i = 0;
		frd_cache = new ArrayList<ArrayList<AfFriendInfo>>();
		for(i = Consts.AFMOBI_FRIEND_TYPE_FF; i <= Consts.AFMOBI_FRIEND_TYPE_STRANGER; i++){
			frd_cache.add(new ArrayList<AfFriendInfo>());
		}
		grp = new  ArrayList<AfGrpProfileInfo>();
		public_grp = new  ArrayList<AfGrpProfileInfo>();
		recent_msg = new  ArrayList<AfMessageInfo>();
		frd_req_msg = new  ArrayList<AfMessageInfo>();
		public_account = new  ArrayList<AfFriendInfo>();
		follow_master = new  ArrayList<AfFriendInfo>();
		follow_passive = new  ArrayList<AfFriendInfo>();
	}
	private void add(byte type, Object []elem){		
		if( null == elem){
			return;
		}
		if( type >= Consts.AFMOBI_FRIEND_TYPE_FF && type <= Consts.AFMOBI_FRIEND_TYPE_STRANGER ){
			ArrayList<AfFriendInfo> tmp = frd_cache.get(type);
			AfFriendInfo []result = (AfFriendInfo [])elem;
			for(AfFriendInfo cur:result){
				tmp.add(cur);
			}
		}else if( type == Consts.AFMOBI_FRIEND_TYPE_GROUP){
			AfGrpProfileInfo []result = (AfGrpProfileInfo [])elem;
			for(AfGrpProfileInfo cur:result){
				grp.add(cur);
			}			
		}else if( type == Consts.AFMOBI_FRIEND_TYPE_PUBLIC_GROUP){
			AfGrpProfileInfo []result = (AfGrpProfileInfo [])elem;
			for(AfGrpProfileInfo cur:result){
				public_grp.add(cur);
			}			
		}else if(type == Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD){
			AfMessageInfo []result = (AfMessageInfo [])elem;
			for(AfMessageInfo cur:result){
				recent_msg.add(cur);
			}			
		}else if(type == Consts.AFMOBI_FRIEND_TYPE_FRD_REQ){
			AfMessageInfo []result = (AfMessageInfo [])elem;
			for(AfMessageInfo cur:result){
				frd_req_msg.add(cur);
			}			
		}else if( type == Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT){
			AfFriendInfo []result = (AfFriendInfo [])elem;
			for(AfFriendInfo cur:result){
				public_account.add(cur);
			}
		}else if( type == Consts.AFMOBI_FOLLOW_TYPE_MASTER ){
			AfFriendInfo []result = (AfFriendInfo [])elem;
			for(AfFriendInfo cur:result){
				follow_master.add(cur);
			}
		}else if(type == Consts.AFMOBI_FOLLOW_TYPE_PASSIVE){
		    AfFriendInfo []result = (AfFriendInfo [])elem;
			for(AfFriendInfo cur:result){
				follow_passive.add(cur);
			}
		}
	}

}
