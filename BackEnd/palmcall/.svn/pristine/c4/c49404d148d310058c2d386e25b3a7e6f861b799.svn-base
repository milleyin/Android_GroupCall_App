package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Record {

    @CodeGenField("拨打电话给谁的afid")
    private String receiverafid;

    @NotNull
    @CodeGenField("消费分钟数")
    private Integer minutes;

    @NotNull
    @CodeGenField("状态0打电话消费，1赠送，2充值")
    private Integer status;

    @NotNull
    @CodeGenField("发生时间戳")
    private Date addtime;

    public String getReceiverafid() {
        return receiverafid;
    }

    public void setReceiverafid(String receiverafid) {
        this.receiverafid = receiverafid;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "Record{" +
                "receiverafid='" + receiverafid + '\'' +
                ", minutes=" + minutes +
                ", status=" + status +
                ", addtime=" + addtime +
                '}';
    }

}
