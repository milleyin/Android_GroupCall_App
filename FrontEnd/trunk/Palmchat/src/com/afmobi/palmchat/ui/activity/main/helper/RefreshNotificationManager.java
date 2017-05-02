package com.afmobi.palmchat.ui.activity.main.helper;

import java.util.LinkedList;

import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.AfMessageInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 消息通知栏队列刷新管理类  安卓顶部通知栏
 * 
 * 用于页面刷新频繁的队列刷新
 * 
 * @author 何桂明 
 * @version [Android Megafon 2012-8-27]
 */
public class RefreshNotificationManager {
	
    private static final String TAG = "RefreshQueueManager";
    
	/**
	 * 刷新队列
	 */
	private LinkedList<int[]> refreshQueue = new LinkedList<int[]>();
	
	/**
	 * 所有界面刷新的集合
	 */
//	private List<RefreshListener> refreshListenerList = new ArrayList<RefreshListener>();
	
	/**
	 * 单例对象
	 */
	private static RefreshNotificationManager instance;
	
	/**
	 * 是否正在刷新
	 */
	private boolean isRefreshing = false;

	public void setRefreshing(boolean isRefreshing){
		this.isRefreshing = isRefreshing;
	}
	private RefreshNotificationManager() {
		
		
		
	}
	
	/**
	 * 得到实例对像
	 * @return RefreshQueueManager
	 */
	public static synchronized RefreshNotificationManager getInstence() {
		synchronized (RefreshNotificationManager.class) {
			if (null == instance) {
				instance = new RefreshNotificationManager();
			}
		}
		return instance;
	}
	
	/**
	 * 发送刷新请求
	 */
	public void sendRefreshRequest(Context context, int[] unread, AfMessageInfo afMsg) {
		refreshQueue.offer(unread);
		refresh(context, afMsg);
	} 

	/**
	 * 刷新页面
	 */
	private synchronized void refresh(Context context, AfMessageInfo afMsg) {
		if (!isRefreshing) {
			if (!refreshQueue.isEmpty()) {
				isRefreshing = true;
				int [] unread = refreshQueue.getLast();
				refreshQueue.clear();
//				hanlder.obtainMessage(Constants.SHOW_NOTIFICATION, unread[0], unread[1], afMsg).sendToTarget();
				CommonUtils.showNoticefacation(context, unread[0], unread[1], afMsg);
			}
		}
	}
	
	/**
	 * 上一个刷新完成可以执行下一个刷新动作
	 */
	public void nextRefresh(Context context, AfMessageInfo afMsg) {
		isRefreshing = false;
		refresh(context, afMsg);
	}
	
	/**
	 * 添加监听
	 * @param [listener] 监听对象
	 */
//	public void addListener(RefreshListener listener) {
//		refreshListenerList.add(listener);
//	}
	
	/**
	 * 移除监听
	 * @param [listener] 监听对象
	 */
//	public void removeListener(RefreshListener listener) {
//		refreshListenerList.remove(listener);
//	}
	
	/**
	 * 刷新调用接口
	 * @author 何桂明
	 * @version [Android Megafon 2012-8-27]
	 */
	public interface RefreshListener {
		void startRefresh();
	}

}
