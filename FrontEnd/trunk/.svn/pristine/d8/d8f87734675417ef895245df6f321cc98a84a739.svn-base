package com.core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.AfPalmchat;

import android.text.TextUtils;


public abstract class CacheSortList<E>{
	public static final  byte ASCENDING_ORDER = 0; 		//升序
	public static final  byte DESCEDNING_ORDER = 1;		//降序	
	public static final  byte DIS_ORDER = 2;			//不需要排序，直接放置到最前面
	
	private static final int CACHE_INIT_SIZE = 10;

	private byte mSortType = ASCENDING_ORDER; 
	private List<E> mList;
	private Map<String, E> mHashMap;
	final protected AfPalmchat mAfPalmchat;	
	protected Object mObject = new Object();
	protected abstract String getObjectKey(E elem);
	protected abstract int compareObjectKey(E one, E another);
	CacheSortList(AfPalmchat core, byte sort){
		mHashMap = new HashMap<String, E>(2*CACHE_INIT_SIZE);
		mList = new ArrayList<E>(CACHE_INIT_SIZE);
		mSortType = sort; 
		mAfPalmchat = core;
	}
	
	protected List<E> cacheGetList(){
		synchronized (mObject) {
			return new ArrayList<E>(mList);
		}
	}
	protected int cacheGetSize(){
		synchronized (mObject) {
			return mList.size();	
		}				
	}
	
	protected void cacheClear(){
		synchronized (mObject) {
			mHashMap.clear();
			mList.clear();
		}		
	}
	
	protected boolean cacheRemove(E elem){
		synchronized (mObject){
			String key = getObjectKey(elem);
			if( null != key){
				Object search = mHashMap.get(key);			
				if( null != search){
					mHashMap.remove(key);
					mList.remove(search);				
					return true;
				}
			}
			return false;			
		}
	}	
	protected E cacheSearch(String key){
		synchronized (mObject) {
			return mHashMap.get(key);
		}		
	}
	
	protected boolean cacheInsert(E elem) {
		synchronized (mObject){
			String key = getObjectKey(elem);
			if( TextUtils.isEmpty(key)){
				return false;
			}
			Object search = mHashMap.get(key);	
			if( null != search){
				mHashMap.remove(key);
				mList.remove(search);
			}
			mHashMap.put(key, elem);
			cacheBinartIndertSort(elem);
			return true;
		}

	}

	protected boolean cacheUpdate(E elem) {
		
		return cacheInsert(elem);
	}

//	public void cacheAddSortList(E []list, boolean clear){
//		if( clear ){
//			cacheClear();
//		}
//		if(null != list){
//			for(E cur:list){
//				mHashMap.put(getObjectKey(cur), cur);
//				mList.add(cur);
//			}		
//		}
//	}
	
	protected boolean cacheSaveOrUpdate(E elem) {
		
		return cacheInsert(elem);
	}
	
	public int insert(List<E> list){
		synchronized (mObject) {
			int sum = 0;
			cacheClear();
			if( null != list){
				for( E elem:list){
					String key = getObjectKey(elem);
					if( !TextUtils.isEmpty(key)){
						mHashMap.put(key, elem);
						mList.add(elem);
						sum++;
					}				
				}				
			}
			return sum;			
		}
	}
	private void cacheBinartIndertSort(E element){
		if( DIS_ORDER == mSortType){
			mList.add(0, element);
		}else{
			int start = 0, mid;
			int end = mList.size() - 1;
			E cur;
			
			if( mSortType == DESCEDNING_ORDER){
				while( start <= end){
					mid = (start + end) >> 1;
					cur = mList.get(mid);
					if(compareObjectKey(element, cur)  > 0){
						end = mid - 1;
					}else{
						start = mid + 1;
					}
				}
			}else{
				while( start <= end){
					mid = (start + end) >> 1;
					cur = mList.get(mid);
					if(compareObjectKey(element, cur)  < 0){
						end = mid - 1;
					}else{
						start = mid + 1;
					}
				}
			}
			if( start < 0 || start >= mList.size() ){
				mList.add(element);
			}else{
				mList.add(start, element);
			}
		}
	}
}
