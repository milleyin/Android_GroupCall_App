package com.core.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.text.TextUtils;

import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.Consts;

public class FriendCacheSortList extends CacheSortList<AfFriendInfo> implements AbsCacheSortList<AfFriendInfo>{

	
	private byte mCacheType;		//cache type
	int count;
	int startIndex;
	FriendCacheSortList(AfPalmchat core, byte type, byte sort) {
		// TODO Auto-generated constructor stub
		super(core, sort);
		mCacheType = type;
	}

	@Override
	protected String getObjectKey(AfFriendInfo elem) {
		// TODO Auto-generated method stub
		return elem.afId;
	}

	@Override
	protected int compareObjectKey(AfFriendInfo one, AfFriendInfo another) {
		// TODO Auto-generated method stub	
		char key1, key2;
		String str1, str2;
		if( Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT == mCacheType ) {
			 str1 =one.name;
			 str2 = another.name;
		}else{
			 str1 = TextUtils.isEmpty(one.alias) ?one.name: one.alias;
			 str2 = TextUtils.isEmpty(another.alias) ?another.name: another.alias;			
		}
		
	
		
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
			return str1.compareToIgnoreCase(str2);
		}		
		return (key1 - key2);
	}

	@Override
	public List<AfFriendInfo> getList() {
		// TODO Auto-generated method stub
		return super.cacheGetList();
	}

	@Override
	public int size(boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		if(cacheFlag){
			return super.cacheGetSize();
		}
		if( dbFlag){
			
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
	public boolean remove(AfFriendInfo element, boolean dbFlag,	boolean cacheFlag) {
		// TODO Auto-generated method stub
		boolean exist;
		
		if(cacheFlag){
			exist = super.cacheRemove(element);
		}else{
			exist = null != super.cacheSearch(element.afId);
		}
		if( exist  && dbFlag){ //delete database  中间件数据库中FF和BF Following 等都是一个数据表的， 一共用了2个字段 
			mAfPalmchat.AfDbProfileRemove(Consts.AFMOBI_DB_TYPE_FRIEND, element.afId);
		}
		return false;
	}

	@Override
	public AfFriendInfo search(AfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		AfFriendInfo afFriendInfo = null;
		if(element==null){
			return null;
		}
		if(cacheFlag){
			afFriendInfo = super.cacheSearch(element.afId);
		}
		
		if(dbFlag){
			afFriendInfo = mAfPalmchat.AfDbProfileGet(Consts.AFMOBI_DB_TYPE_FRIEND, element.afId);
		}
		if(afFriendInfo != null && !TextUtils.isEmpty(afFriendInfo.name)){
			return afFriendInfo;
		}
		return null;
	}

	@Override
	public int saveOrUpdate(AfFriendInfo element, boolean dbFlag,boolean cacheFlag) {
		// TODO Auto-generated method stubmFriendCacheSortList
//		boolean exist = null != super.cacheSearch(element.afId);	
//		int ret;
//		if( exist ){
//			ret = update(element, dbFlag, cacheFlag);
//		}else{
//			ret = insert(element, dbFlag, cacheFlag);
//		}
		int id = 0;
		if(element != null && element.afId != null && element.name != null) {
		if (cacheFlag) {
			super.cacheInsert(element);
		}
		
		if (dbFlag) {
			
			int followType = mAfPalmchat.AfDbProfileGetFollowtype(element.afId);
			element.follow_type = followType;// 非follow 非following的话那就是UnKnow
			//更新好友列表操作  数据库里的FF变味Unknow
			id = mAfPalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_FRIEND, element);
		}
		}
		return id;
	}

	@Override
	public int update(AfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		
		if(element != null && element.afId != null && element.name != null) {
			
		if(cacheFlag){
			super.cacheUpdate(element);
		}
		if(dbFlag && !TextUtils.isEmpty(element.name)){
			
			int followType = mAfPalmchat.AfDbProfileGetFollowtype(element.afId);
			element.follow_type = followType;
			
			mAfPalmchat.AfDbProfileUpdate(Consts.AFMOBI_DB_TYPE_FRIEND, element);
		}
		
		}
	
		return 0;
	}

	@Override
	public int insert(AfFriendInfo element, boolean dbFlag, boolean cacheFlag) {
		// TODO Auto-generated method stub
		int id = 0;
		
		if(element != null && element.afId != null && element.name != null) {
		
		if( cacheFlag){
			    super.cacheInsert(element);
		}
		if(dbFlag){
//			mAfPalmchat.AfDbProfileRemove(Consts.AFMOBI_DB_TYPE_FRIEND, element.afId);
			
			int followType = mAfPalmchat.AfDbProfileGetFollowtype(element.afId);
			element.follow_type = followType;
			
			id = mAfPalmchat.AfDbProfileInsert(Consts.AFMOBI_DB_TYPE_FRIEND, element);
			
			if(id == -1) {
				mAfPalmchat.AfDbProfileUpdate(Consts.AFMOBI_DB_TYPE_FRIEND, element);
			}
		}
		
		}
		
		return id;
	}

	@Override
	public List<AfFriendInfo> getList(int page, int size) {
		count = page * size;
		if(count > cacheGetSize())
		{
			count = cacheGetSize() - (page - 1) * size;
		}
		else{
			count = size;
		}
		if(count <= 0)
			return null;
		AfFriendInfo[] newArray = new AfFriendInfo[count];
		if(page == 1){
			startIndex = 0;
		}else {
			startIndex = (page - 1) * size;
		}
		System.arraycopy(cacheGetList().toArray(),startIndex,newArray,0,count);
		return Arrays.asList(newArray);
	}

	/** 根据afid查看当前profile，不需要判断是name是需要为空
	 * @param afid
	 * @param dbFlag
	 * @param cacheFlag
	 * @return
	 */
	@Override
	public AfFriendInfo searchNoCheckName(String afid, boolean dbFlag, boolean cacheFlag) {
		if(afid==null){
			return null;
		}
		if(cacheFlag){
			return super.cacheSearch(afid);
		}

		if(dbFlag){
			return mAfPalmchat.AfDbProfileGet(Consts.AFMOBI_DB_TYPE_FRIEND, afid);
		}
		return null;
	}
}
