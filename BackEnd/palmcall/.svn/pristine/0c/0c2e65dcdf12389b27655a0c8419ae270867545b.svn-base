package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetProfileResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("用户afid")
    private String afid;

    @CodeGenField("当afid不存在时返回此字段，-1表示afid不存在")
    private String errorCode;

    @NotNull
    @CodeGenField("累计接电话次数")
    private Integer answeringTimes;

    @NotNull
    @CodeGenField("当前可用沟通类型.(A:代表audio音频; V:代表video视频)")
    private List<String> availableType;

    @CodeGenField("语音版本号，时间戳, 此字段不存在或为空时表示不存在版本")
    private String audioTs;

    @CodeGenField("语音时长")
    private Integer audioDuration;

    @CodeGenField("封面图片版本号，时间戳, 此字段不存在或为空时表示不存在版本")
    private String coverTs;

    @NotNull
    @CodeGenField("性别")
    private String sex;

    @NotNull
    @CodeGenField("年龄")
    private Integer age;

    @NotNull
    @CodeGenField("昵称")
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getAnsweringTimes() {
        return answeringTimes;
    }

    public void setAnsweringTimes(Integer answeringTimes) {
        this.answeringTimes = answeringTimes;
    }

    public List<String> getAvailableType() {
        return availableType;
    }

    public void setAvailableType(List<String> availableType) {
        this.availableType = availableType;
    }

    public String getAudioTs() {
        return audioTs;
    }

    public void setAudioTs(String audioTs) {
        this.audioTs = audioTs;
    }

    public Integer getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(Integer audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getCoverTs() {
        return coverTs;
    }

    public void setCoverTs(String coverTs) {
        this.coverTs = coverTs;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GetProfileResponse{" +
                "code='" + code + '\'' +
                ", afid='" + afid + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", answeringTimes=" + answeringTimes +
                ", availableType=" + availableType +
                ", audioTs='" + audioTs + '\'' +
                ", audioDuration=" + audioDuration +
                ", coverTs='" + coverTs + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

}
