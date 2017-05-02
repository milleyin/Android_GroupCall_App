package com.afmobi.palmchat.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 使用ExecutorService来创建线程池并把一些频繁的任务加入线程池处理， 如收消息
 * @modify xiaolong
 *
 */
public class ThreadPool {
	
//	private static ThreadPool threadPool=null;
	private ExecutorService pool=null;
//	private ExecutorService poolGif=null;
	
	private ExecutorService poolReady=null;
	
//	public ExecutorService getPoolGif() {
//		return poolGif;
//	}

	public ThreadPool() {
		
		if(pool==null) {
			
			pool = Executors.newFixedThreadPool(1);
			poolReady = Executors.newFixedThreadPool(1);
		}
	}
	
/*	public static ThreadPool getPoolInstance() {
		
		if(threadPool==null) {
			threadPool = new ThreadPool();
		}
		
		return threadPool;
	}*/
	
	/**
	 * add thread to pool
	 * @param r
	 */
	public void execute(Runnable r) {
		
		pool.execute(r);	
		
//		System.out.println("queued count:"+((ThreadPoolExecutor)pool).getQueue().size());
		
	}
	
	/*public void executeGif(Runnable r) {
		
		poolGif.execute(r);	
		
//		System.out.println("queued count:"+((ThreadPoolExecutor)pool).getQueue().size());
		
	}*/
	
public void executeReady(Runnable r) {
	
	poolReady.execute(r);	
		
//		System.out.println("queued count:"+((ThreadPoolExecutor)pool).getQueue().size());
		
	}
	
	
	/**
	 * get thread pool queue size
	 * @return queued count
	 */
	public int getQueuedCount() {
		
		return ((ThreadPoolExecutor)pool).getQueue().size();
		
	}
	
	
//	public int getQueuedGifCount() {
//		
//		return ((ThreadPoolExecutor)poolGif).getQueue().size();
//		
//	}
	
	
	
}
