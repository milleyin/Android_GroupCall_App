package com.afmobi.palmchat.eventbusmodel;

import com.core.AfMessageInfo;

public class SendImageEvent {
	private AfMessageInfo m_AfMessageInfo;
	private int m_progress;
	private boolean m_isFinished;
	private Object m_result;
	public SendImageEvent(AfMessageInfo afMessageInfo,int progress,boolean isFinished,Object result){
		m_AfMessageInfo=afMessageInfo;
		m_progress=progress;
		m_isFinished=isFinished;
		m_result=result;
	}
	public AfMessageInfo getAfMessageInfo(){
		return m_AfMessageInfo;
	}
	public int getProgress(){
		return m_progress;
	}
	public boolean isFinished(){
		return m_isFinished;
	}
	public Object getResult(){
		return m_result;
	}
}
