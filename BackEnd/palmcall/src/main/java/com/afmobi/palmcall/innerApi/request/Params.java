package com.afmobi.palmcall.innerApi.request;

import com.alibaba.fastjson.annotation.JSONField;

public class Params {
    @JSONField(name="CallId")
    private String callId;

    @JSONField(name="CallAppId")
    private String callAppId;

    @JSONField(name="CallMediaType")
    private String callMediaType;

    @JSONField(name="ReleaseCallBy")
    private String releaseCallBy;

    @JSONField(name="CallAnswerTime")
    private String callAnswerTime;

    @JSONField(name="CalleeAccountId")
    private String calleeAccountId;

    @JSONField(name="CallerAccountId")
    private String callerAccountId;

    @JSONField(name="CallOrignateTime")
    private String callOrignateTime;

    @JSONField(name="ReleaseCallReason")
    private String releaseCallReason;

    @JSONField(name="CallTalkingDuration")
    private String callTalkingDuration;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallAppId() {
        return callAppId;
    }

    public void setCallAppId(String callAppId) {
        this.callAppId = callAppId;
    }

    public String getCallMediaType() {
        return callMediaType;
    }

    public void setCallMediaType(String callMediaType) {
        this.callMediaType = callMediaType;
    }

    public String getReleaseCallBy() {
        return releaseCallBy;
    }

    public void setReleaseCallBy(String releaseCallBy) {
        this.releaseCallBy = releaseCallBy;
    }

    public String getCallAnswerTime() {
        return callAnswerTime;
    }

    public void setCallAnswerTime(String callAnswerTime) {
        this.callAnswerTime = callAnswerTime;
    }

    public String getCalleeAccountId() {
        return calleeAccountId;
    }

    public void setCalleeAccountId(String calleeAccountId) {
        this.calleeAccountId = calleeAccountId;
    }

    public String getCallerAccountId() {
        return callerAccountId;
    }

    public void setCallerAccountId(String callerAccountId) {
        this.callerAccountId = callerAccountId;
    }

    public String getCallOrignateTime() {
        return callOrignateTime;
    }

    public void setCallOrignateTime(String callOrignateTime) {
        this.callOrignateTime = callOrignateTime;
    }

    public String getReleaseCallReason() {
        return releaseCallReason;
    }

    public void setReleaseCallReason(String releaseCallReason) {
        this.releaseCallReason = releaseCallReason;
    }

    public String getCallTalkingDuration() {
        return callTalkingDuration;
    }

    public void setCallTalkingDuration(String callTalkingDuration) {
        this.callTalkingDuration = callTalkingDuration;
    }

    @Override
    public String toString() {
        return "Params{" +
                "callId='" + callId + '\'' +
                ", callAppId='" + callAppId + '\'' +
                ", callMediaType='" + callMediaType + '\'' +
                ", releaseCallBy='" + releaseCallBy + '\'' +
                ", callAnswerTime='" + callAnswerTime + '\'' +
                ", calleeAccountId='" + calleeAccountId + '\'' +
                ", callerAccountId='" + callerAccountId + '\'' +
                ", callOrignateTime='" + callOrignateTime + '\'' +
                ", releaseCallReason='" + releaseCallReason + '\'' +
                ", callTalkingDuration='" + callTalkingDuration + '\'' +
                '}';
    }

}
