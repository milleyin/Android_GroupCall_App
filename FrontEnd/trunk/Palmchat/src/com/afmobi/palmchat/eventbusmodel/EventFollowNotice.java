package com.afmobi.palmchat.eventbusmodel;

/**
 * Created by heguiming on 2016/7/28.
 */
public class EventFollowNotice {

    private String mAfid;//用户ID

    public EventFollowNotice(String afid){
        mAfid = afid;
    }
    public String getAfid(){
        return mAfid;
    }
}
