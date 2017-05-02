package com.afmobi.palmchat.eventbusmodel;

public class ModifyPictureListEvent {
	public static final int DELETE=1;
	private int mType;//1为删除
	private int mIndex;//操作的索引
	public ModifyPictureListEvent(int type,int index){
		mType=type;
		mIndex=index;
	}
	public int getIndex(){
		return mIndex;
	}
	public int getType(){
		return mType;
	}
}
