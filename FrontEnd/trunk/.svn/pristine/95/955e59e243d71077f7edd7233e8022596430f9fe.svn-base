package com.afmobi.palmchat.eventbusmodel;

/**
 * Created by Transsion on 2016/6/30.
 */
public class EventPalmcallNotication {
    /**中间件呼叫类型*/
    private int callType;
    /**justalk通话状态*/
    private String callState;
    /**call*/
    private int callId;
    /**extroinfo*/
    private String extroInfo;
    /**已经通话时间*/
    private long talkedTime;

    public EventPalmcallNotication(String callState,int callType, int callId, String extroInfo) {
        this.callState = callState;
        this.callType = callType;
        this.callId = callId;
        this.extroInfo = extroInfo;
    }

    public EventPalmcallNotication(int callType, int callId, String extroInfo) {
        this.callType = callType;
        this.callId = callId;
        this.extroInfo = extroInfo;
    }

    public EventPalmcallNotication(String callState, int callId){
        this.callState = callState;
        this.callId = callId;
    }

    public EventPalmcallNotication(String callState,int callType, int callId,long talkedTime) {
        this.callState = callState;
        this.callType = callType;
        this.callId = callId;
        this.talkedTime =talkedTime;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public int getCallId() {
        return callId;
    }

    public void setCallId(int callId) {
        this.callId = callId;
    }

    public void setTalkedTime(long talkedTime) {
        this.talkedTime = talkedTime;
    }

    public long getTalkedTime() {
        return talkedTime;
    }

    public String getExtroInfo() {
        return extroInfo;
    }

    public void setExtroInfo(String extroInfo) {
        this.extroInfo = extroInfo;
    }

    public String getCallState() {
        return callState;
    }

    public void setCallState(String callState) {
        this.callState = callState;
    }
}
