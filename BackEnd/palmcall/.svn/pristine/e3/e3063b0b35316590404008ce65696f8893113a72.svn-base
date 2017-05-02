package com.afmobi.palmcall.log.model;

public class CallUploadLog {

    private String callerId; //拨打方

    private String receiverId; //接听方

    private String afid; //上传头像或语音或免打扰设置的afid

    //类型标识 0关闭免打扰，1开启免打扰，
    // 2上传头像，3上传语音,
    // 4拨打方剩余分钟数不足,5对方正在通话中，6对方在免打扰时间,7拨打方正在通话列表，服务器扣费中
    // 菊风服务器通知我的 8开始A打B 9B开始响铃 10B接通电话,11挂电话
    private int status;

    private int callDuration = 0;

    private String hangUpAfid = "other";

    private String sex;

    private String age;

    private String country;

    private String platform;

    private String version;

    private long currentTime; //写入时间戳

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }

    public String getHangUpAfid() {
        return hangUpAfid;
    }

    public void setHangUpAfid(String hangUpAfid) {
        this.hangUpAfid = hangUpAfid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "CallUploadLog{" +
                "callerId='" + callerId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", afid='" + afid + '\'' +
                ", status=" + status +
                ", callDuration=" + callDuration +
                ", hangUpAfid='" + hangUpAfid + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", country='" + country + '\'' +
                ", platform='" + platform + '\'' +
                ", version='" + version + '\'' +
                ", currentTime=" + currentTime +
                '}';
    }

}
