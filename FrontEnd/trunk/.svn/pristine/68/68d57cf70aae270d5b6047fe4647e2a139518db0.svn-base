package com.core.cache;

import java.util.List;


public interface AbsCacheSortList<E>{
	public List<E> getList();
	public int size(boolean dbFlag, boolean cacheFlag);
	public void clear(boolean dbFlag, boolean cacheFlag);
	public boolean remove(E element , boolean dbFlag, boolean cacheFlag);
	public E search(E element, boolean dbFlag, boolean cacheFlag);
	public int saveOrUpdate(E element, boolean dbFlag, boolean cacheFlag);
	public int update(E element, boolean dbFlag, boolean cacheFlag);
	public int insert(E element, boolean dbFlag, boolean cacheFlag);
	public int insert(List<E> list);
	public List<E> getList(int page,int size);
	public E searchNoCheckName(String afid, boolean dbFlag, boolean cacheFlag);
}