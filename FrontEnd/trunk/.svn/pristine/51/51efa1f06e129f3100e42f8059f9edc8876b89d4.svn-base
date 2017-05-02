package com.afmobi.palmchat.util;

import android.os.Handler;
import android.view.View;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfMessageInfo;

public class StartTimer implements Runnable{
	public final static Handler timerHandler = new Handler();
	final int countTimer = 5000;//
	int privateTimer = 0;
	private AfMessageInfo afMessageInfo;
	
	private View viewShowFrame;
	public StartTimer(AfMessageInfo afMessageInfo,View textview) {
		// TODO Auto-generated constructor stub
		this.afMessageInfo = afMessageInfo;
		this.viewShowFrame = textview;
	}

	
	public static StartTimer startTimer(AfMessageInfo afMessageInfo,View textview) {
		// TODO Auto-generated method stub
		StartTimer s = new StartTimer(afMessageInfo,textview);
		afMessageInfo.setStartTimer(s);
//		s.run();
		return s;
	}
	TimerComplete timerComplete;
	public void setOnItemClick(TimerComplete timerComplete){
		this.timerComplete = timerComplete;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PalmchatLogUtils.e("privateTimer", privateTimer + "->" + privateTimer);
		if (privateTimer < countTimer) {
			viewShowFrame.setVisibility(View.VISIBLE);
			privateTimer += 1000;
			timerHandler.postDelayed(this, 1000);
			if(timerComplete != null){
				timerComplete.timerComplete(false);
			}
		} else {
			viewShowFrame.setVisibility(View.GONE);
			if(timerComplete != null){
				timerComplete.timerComplete(true);
			}
			privateTimer = 0;
			stopTimer(afMessageInfo);
		}
	}
	
	public void stopTimer(AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		timerHandler.removeCallbacks(afMessageInfo.getStartTimer());
		timerComplete = null;
	}
	
	public interface TimerComplete{
		void timerComplete(boolean isVisible);
	}
}
