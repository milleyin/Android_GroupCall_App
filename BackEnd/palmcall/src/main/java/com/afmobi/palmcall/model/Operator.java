package com.afmobi.palmcall.model;

import java.util.Date;

public class Operator {
    private int id;

    private String afid;

    private int amount; //接听次数

    private int callnumber; //被呼叫次数

    private int leftminutes; //剩余分钟数


    private int starttime; //免打扰开始时间，小时

    private int endtime; //免打扰结束时间，小时

    private int open; //免打扰开1 关0


    private String coverImgPath; //封面图片路径

    private String coverTs; //封面图片最新版本号，时间戳

    private String audioPath; //语音路径

    private String audioTs; //语音最新版本号，时间戳

    private int audioDuration; //语音时长


    private String sex; //性别

    private String birthdate; //生日

    private String name; //昵称

    private Date addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCallnumber() {
        return callnumber;
    }

    public void setCallnumber(int callnumber) {
        this.callnumber = callnumber;
    }

    public int getLeftminutes() {
        return leftminutes;
    }

    public void setLeftminutes(int leftminutes) {
        this.leftminutes = leftminutes;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getCoverImgPath() {
        return coverImgPath;
    }

    public void setCoverImgPath(String coverImgPath) {
        this.coverImgPath = coverImgPath;
    }

    public String getCoverTs() {
        return coverTs;
    }

    public void setCoverTs(String coverTs) {
        this.coverTs = coverTs;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioTs() {
        return audioTs;
    }

    public void setAudioTs(String audioTs) {
        this.audioTs = audioTs;
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(int audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", afid='" + afid + '\'' +
                ", amount=" + amount +
                ", callnumber=" + callnumber +
                ", leftminutes=" + leftminutes +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", open=" + open +
                ", coverImgPath='" + coverImgPath + '\'' +
                ", coverTs='" + coverTs + '\'' +
                ", audioPath='" + audioPath + '\'' +
                ", audioTs='" + audioTs + '\'' +
                ", audioDuration=" + audioDuration +
                ", sex='" + sex + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", name='" + name + '\'' +
                ", addtime=" + addtime +
                '}';
    }

}
