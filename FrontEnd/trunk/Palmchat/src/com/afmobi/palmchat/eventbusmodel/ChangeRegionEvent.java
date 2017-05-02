package com.afmobi.palmchat.eventbusmodel;

public class ChangeRegionEvent {
	private boolean isChangePalmGuessFlag;//是否更新Palmguess入口
	private int palmguessFlag=0;//标志
	public ChangeRegionEvent(boolean isChangePalmguessFlag,int flag){
		this.isChangePalmGuessFlag=isChangePalmguessFlag;
		palmguessFlag=flag;
	}
	public int getPalmguessFlag(){
		return palmguessFlag;
	}
	public boolean isChangePalmguessFlag(){
		return isChangePalmGuessFlag;
	}
}
