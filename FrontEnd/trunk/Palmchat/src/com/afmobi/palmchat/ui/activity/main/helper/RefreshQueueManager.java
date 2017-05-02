package com.afmobi.palmchat.ui.activity.main.helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

/**
 * Chats 消息列表 页面队列刷新管理类
 * 
 * 用于页面刷新频繁的队列刷新
 * 
 * @author 何桂明 
 * @version [Android Megafon 2012-8-27]
 */
public class RefreshQueueManager {
	
    private static final String TAG = "RefreshQueueManager";
    
	/**
	 * 刷新队列
	 */
	private LinkedList<String> refreshQueue = new LinkedList<String>();
	
	/**
	 * 所有界面刷新的集合
	 */
	private List<RefreshListener> refreshListenerList = new ArrayList<RefreshListener>();
	
	/**
	 * 单例对象
	 */
	private static RefreshQueueManager instance;
	
	/**
	 * 是否正在刷新
	 */
	private boolean isRefreshing = false;
	
	/**
	 * 构造器私有
	 */
	private RefreshQueueManager() {
		
	}
	
	/**
	 * 得到实例对像
	 * @return RefreshQueueManager
	 */
	public static synchronized RefreshQueueManager getInstence() {
		synchronized (RefreshQueueManager.class) {
			if (null == instance) {
				instance = new RefreshQueueManager();
			}
		}
		return instance;
	}
	
	/**
	 * 发送刷新请求
	 */
	public void sendRefreshRequest() {
		refreshQueue.offer("");
		System.out.println("RefreshQueueManager  sendRefreshRequest");
		refresh();
	}

	/**
	 * 刷新页面
	 */
	private synchronized void refresh() {
		if (!isRefreshing) {
			System.out.println("RefreshQueueManager  refresh");
			if (!refreshQueue.isEmpty()) {
				isRefreshing = true;
				refreshQueue.clear();
				for (int i = 0; i < refreshListenerList.size(); i++) {
					refreshListenerList.get(i).startRefresh();
				}
			}
		}
	}
	
	/**
	 * 上一个刷新完成可以执行下一个刷新动作
	 */
	public void nextRefresh() {
		isRefreshing = false;
		refresh();
	}
	
	/**
	 * 添加监听
	 * @param [listener] 监听对象
	 */
	public void addListener(RefreshListener listener) {
		refreshListenerList.add(listener);
	}
	
	/**
	 * 移除监听
	 * @param [listener] 监听对象
	 */
	public void removeListener(RefreshListener listener) {
		refreshListenerList.remove(listener);
	}
	
	/**
	 * 刷新调用接口
	 * @author 何桂明
	 * @version [Android Megafon 2012-8-27]
	 */
	public interface RefreshListener {
		void startRefresh();
	}

}
