package com.afmobi.palmchat.eventbusmodel;

public class EventLoginBackground {
	private boolean isShowPalmGuessNew;
	private boolean isSuccess;
	public EventLoginBackground(boolean isShowPalmGuessNew,boolean isSuccess){
		this.isShowPalmGuessNew= isShowPalmGuessNew;
		this.isSuccess=isSuccess;
	}
	public boolean getIsShowPalmGuessNew(){
		return isShowPalmGuessNew;
	}
	public boolean  getIsLoginSuccess(){
		return isSuccess;
	}
}
