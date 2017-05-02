package com.core.cache;

import java.util.List;

import android.text.TextUtils;

import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfGrpProfileInfo;
import com.core.AfPalmchat;
import com.core.Consts;


public class GrpCacheSortList extends CacheSortList<AfGrpProfileInfo> implements AbsCacheSortList<AfGrpProfileInfo> {

	
	GrpCacheSortList(AfPalmchat core, byte sort) {
		super(core, sort);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<AfGrpProfileInfo> getList() {
		// TODO Auto-generated method stub
		return super.cacheGetList();
	}

	@Override
	public int size(boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if(cacheFlag){
			return super.cacheGetSize();
		}
		return 0;
	}

	@Override
	public void clear(boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if( cacheFlag ){
			super.cacheClear();
		}
		if(dbFlag){
			
		}
	}

	@Override
	public boolean remove(AfGrpProfileInfo element, boolean dbFlag,
			boolean cacheFlag) {
		// TODO Auto-generated method stub
        boolean exist;
		
		if(cacheFlag){
			exist = super.cacheRemove(element);
		}else{
			exist = null != super.cacheSearch(element.afid);
		}
		if( exist  && dbFlag){ //delete database
			mAfPalmchat.AfDbGrpProfileOpr(element, (byte)Consts.REQ_TYPE_REMOVE);
		}
		return false;
	}

	@Override
	public AfGrpProfileInfo search(AfGrpProfileInfo element, boolean dbFlag,
			boolean cacheFlag) {
		// TODO Auto-generated method stub
		if( cacheFlag){
			return super.cacheSearch(getObjectKey(element));
		}
		return null;
	}

	@Override
	public int saveOrUpdate(AfGrpProfileInfo element, boolean dbFlag,
			boolean cacheFlag) {
		// TODO Auto-generated method stub
		
		boolean exist ;
		 exist = null != super.cacheSearch(element.afid);		
		
		int ret;
		
		if( exist ){
			ret = update(element, dbFlag, cacheFlag);
		}else{
			ret = insert(element, dbFlag, cacheFlag);
		}
		return ret;
	}

	@Override
	public int update(AfGrpProfileInfo element, boolean dbFlag,
			boolean cacheFlag) {
		// TODO Auto-generated method stub
		if(cacheFlag){
			super.cacheUpdate(element);
		}
		if(dbFlag){
			mAfPalmchat.AfDbGrpProfileOpr(element,Consts.REQ_TYPE_UPDATE);
		}
		return 0;
	}

	@Override
	public int insert(AfGrpProfileInfo element, boolean dbFlag,
			boolean cacheFlag) {
		
		// TODO Auto-generated method stub
	   // remove(element, dbFlag, cacheFlag);
		
		if( cacheFlag){
			super.cacheInsert(element);
		}
		if(dbFlag){
			return mAfPalmchat.AfDbGrpProfileOpr(element, Consts.REQ_TYPE_INSERT);
		}
		return 0;
	}

	@Override
	public List<AfGrpProfileInfo> getList(int page, int size) {
		return null;
	}

	@Override
	protected String getObjectKey(AfGrpProfileInfo elem) {
		// TODO Auto-generated method stub
		return elem.afid;
	}

	@Override
	protected int compareObjectKey(AfGrpProfileInfo one,
			AfGrpProfileInfo another) {
	// TODO Auto-generated method stub	
		char key1, key2;	
		String str1, str2;
		
		str1 = TextUtils.isEmpty(one.sig) ? one.name: one.sig;
		str2 = TextUtils.isEmpty(another.sig) ? another.name: another.sig;
		key1 = CommonUtils.getSortKey(str1);
		key2 = CommonUtils.getSortKey(str2);		
		if( key1 == key2)
		{
			if( null == str1)
			{
				return -1;
			}
			if( null == str2)
			{
				return 1;
			}
			return str1.compareTo(str2);
		}
		return (key1 - key2);
	}

	@Override
	public AfGrpProfileInfo searchNoCheckName(String afid, boolean dbFlag, boolean cacheFlag) {
		return null;
	}
}