package com.afmobi.palmcall.innerApi.response;

import com.alibaba.fastjson.annotation.JSONField;

public class PalmCallStatus {

    private String afid;

    @JSONField(name="OnlineStatus")
    private String onlineStatus; //需等于0 不在线  1在线

    @JSONField(name="JustalkID")
    private String justalkID; //afid对应的palmcall id  JustalkID必须不为空

    @JSONField(name="JustalkStatus")
    private String justalkStatus; //需要等于init

    private String errmsg;  //当该afid不存在时的返回值

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getJustalkID() {
        return justalkID;
    }

    public void setJustalkID(String justalkID) {
        this.justalkID = justalkID;
    }

    public String getJustalkStatus() {
        return justalkStatus;
    }

    public void setJustalkStatus(String justalkStatus) {
        this.justalkStatus = justalkStatus;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "PalmCallStatus{" +
                "afid='" + afid + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", justalkID='" + justalkID + '\'' +
                ", justalkStatus='" + justalkStatus + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

}
