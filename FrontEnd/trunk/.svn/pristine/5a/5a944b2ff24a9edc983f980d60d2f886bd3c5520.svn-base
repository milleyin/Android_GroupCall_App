package com.afmobi.palmchat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.core.AfMessageInfo;

import android.app.Activity;
import android.os.Handler;

public class PopMessageManager {

	private HashMap<String, Integer> mLastMsgMap = new HashMap<String, Integer>();
	
	private ArrayList<Activity> mPopMessageActivity = new ArrayList<Activity>();
	private LinkedList<String> popMsgQueue = new LinkedList<String>();
	private boolean isPoping = false;
	
	private AfMessageInfo mFirstPopMsg;
	
	/**
	 * exit from which pop msg dialog
	 */
	private HashMap<String, Boolean> mExitPopMsg = new HashMap<String, Boolean>();
	
	public HashMap<String, Boolean> getmExitPopMsg() {
		return mExitPopMsg;
	}
	
	
	public AfMessageInfo getmFirstPopMsg() {
		return mFirstPopMsg;
	}

	public void setmFirstPopMsg(AfMessageInfo mFirstPopMsg) {
		this.mFirstPopMsg = mFirstPopMsg;
	}

	public void finishPrePopMessageActivity() {
		for(Activity activity : mPopMessageActivity) {
			activity.finish();
		}  
		
		mPopMessageActivity.clear(); 
	}
	
	public void addPopMessageActivity(Activity activity) {
		mPopMessageActivity.add(activity);
	}
	
	
	public synchronized void clearCount() {
		mLastMsgMap.clear();
	}
	
	
	public synchronized boolean popMsgDisplaying() {
		int size = mLastMsgMap.size();
		boolean isDisplaying = size > 0 ? true : false;
		return isDisplaying;
	}
	
	public void sendPopMsgRequest(Handler handler, AfMessageInfo afMsg) {
		popMsgQueue.offer("");
		popMsg(handler, afMsg);
	}
	 
	public void nextPopMsg(Handler handler, AfMessageInfo afMsg) {
		isPoping = false;
		popMsg(handler, afMsg);
	}

	
	private synchronized void popMsg(Handler handler, AfMessageInfo afMsg) {
		/*if (!isPoping) {
			if (!popMsgQueue.isEmpty()) {
				isPoping = true;
				popMsgQueue.clear();
			}
			}*/
			
				int count;
				if(mLastMsgMap.containsKey(afMsg.getKey())) {
					
					count = mLastMsgMap.get(afMsg.getKey());
					count ++;
					mLastMsgMap.put(afMsg.getKey(), count);
				} else {
					count = 1;
					mLastMsgMap.clear();
					mLastMsgMap.put(afMsg.getKey(), count);
				}
		
		
				PalmchatLogUtils.println("--rrr popMsg count:"+count);
				handler.obtainMessage(Constants.IS_BACKGROUND, count, 0, afMsg).sendToTarget();
	
	}
	
	public synchronized int getMsgCount(String key) {
		int count = 0;
		if(mLastMsgMap.containsKey(key)) {
			count = mLastMsgMap.get(key);
			
		}
		return count;
	}
	
}
