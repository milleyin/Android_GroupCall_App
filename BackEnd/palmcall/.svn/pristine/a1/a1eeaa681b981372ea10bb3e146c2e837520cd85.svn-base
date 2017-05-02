package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class GetNotDisturbResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("1开启免打扰，0关闭。默认为0")
    private Integer open = 0;

    @NotNull
    @CodeGenField("开始时间，小时,默认晚上11点。当startTime大于endTime时，表示跨天")
    private Integer startTime = 23;

    @NotNull
    @CodeGenField("结束时间，小时,默认早上7点")
    private Integer endTime = 7;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "GetNotDisturbResponse{" +
                "code='" + code + '\'' +
                ", open=" + open +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}
