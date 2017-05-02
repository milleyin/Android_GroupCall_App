package com.core.cache;

import java.util.Date;
import java.util.List;

import com.afmobi.palmchat.ui.activity.main.model.MainAfFriendInfo;
import com.core.AfPalmchat;
import com.core.Consts;



public class MsgCacheSortList extends CacheSortList<MainAfFriendInfo> implements AbsCacheSortList<MainAfFriendInfo>  {

	private int mDbType;	
	
	MsgCacheSortList(AfPalmchat core, byte type, byte sort) {
		// TODO Auto-generated constructor stub
		super(core, sort);
		if( Consts.AFMOBI_FRIEND_TYPE_RECENT_RECORD == type ){
			mDbType = Consts.AFMOBI_DB_TYPE_RECENT_MESSAGE;
		}else{
			mDbType = Consts.AFMOBI_DB_TYPE_FRIEND_REQ;
		}
	}

	@Override
	public List<MainAfFriendInfo> getList() {
		// TODO Auto-generated method stub
		List<MainAfFriendInfo> result = super.cacheGetList();
		return result;
	}

	@Override
	public int size(boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if( cacheFlag){
			return super.cacheGetSize();		
		}
		return -1;
	}

	@Override
	public void clear(boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if(cacheFlag ){
			super.cacheClear();
		}
		if( dbFlag ){
			//mAfPalmchat.AfDbRecentMsgRemove(db_type, elem)
		}
	}

	@Override
	public boolean remove(MainAfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		boolean ret = false;
		
		if(cacheFlag ){
			ret = super.cacheRemove(element);	
		}
		if( dbFlag ){
			ret = 0 != mAfPalmchat.AfDbRecentMsgRemove(mDbType, element.afMsgInfo);
		}
		return ret;
	}

	@Override
	public MainAfFriendInfo search(MainAfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if( cacheFlag){
			return super.cacheSearch(getObjectKey(element));
		}
		return null;
	}

	@Override
	public int saveOrUpdate(MainAfFriendInfo element, boolean dbFlag,
			boolean cacheFlag) {
		// TODO Auto-generated method stub
		int ret= 0;
		if( cacheFlag ){
			
		}else{
			boolean exist = null != super.cacheSearch(element.afMsgInfo.getKey());	
			if( exist ){
				ret = update(element, dbFlag, cacheFlag);
			}else{
				ret = insert(element, dbFlag, cacheFlag);
			}
		}	

		return ret;
	}

	@Override
	public int update(MainAfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if(cacheFlag){
			super.cacheUpdate(element);
		}
		
		return 0;
	}

	@Override
	public int insert(MainAfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		int id = 0;
		
		if(cacheFlag ){
			super.cacheInsert(element);
		}
		if( dbFlag ){
			mAfPalmchat.AfDbRecentMsgRemove(mDbType, element.afMsgInfo);
			id = mAfPalmchat.AfDbRecentMsgInsert(mDbType, element.afMsgInfo);
		}
		return id;
	}

	@Override
	public List<MainAfFriendInfo> getList(int page, int size) {
		return null;
	}

	@Override
	protected String getObjectKey(MainAfFriendInfo elem) {
		// TODO Auto-generated method stub		
		return elem.afMsgInfo.getKey();
	}

	@Override
	protected int compareObjectKey(MainAfFriendInfo one, MainAfFriendInfo another) {
		// TODO Auto-generated method stub
		return new Date(one.afMsgInfo.client_time).compareTo(new Date(another.afMsgInfo.client_time));
	}

	@Override
	public MainAfFriendInfo searchNoCheckName(String afid, boolean dbFlag, boolean cacheFlag) {
		return null;
	}
}
