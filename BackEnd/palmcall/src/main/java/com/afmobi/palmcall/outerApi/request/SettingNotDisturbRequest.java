package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SettingNotDisturbRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("afid")
    private String afid;

    @DecimalMin("0")
    @DecimalMax("1")
    @CodeGenField("1开启免打扰，0关闭,默认为0")
    private Integer open = 0;

    @DecimalMin("0")
    @DecimalMax("24")
    @CodeGenField("开始时间，小时，默认晚上11点。当startTime大于endTime时，表示跨天")
    private Integer startTime = 23;

    @DecimalMin(value="0")
    @DecimalMax(value="24")
    @CodeGenField("结束时间，小时，默认早上7点,注意开始时间和结束时间不能相同")
    private Integer endTime = 7;

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SettingNotDisturbRequest{" +
                "afid='" + afid + '\'' +
                ", open=" + open +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}
