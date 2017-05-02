package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RechargeLeftMinutesRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("充值用户的afid")
    private String afid;

    @NotNull
    @DecimalMin("1")
    @CodeGenField("充值分钟数")
    private Integer minutes;

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "RechargeLeftMinutesRequest{" +
                "afid='" + afid + '\'' +
                ", minutes=" + minutes +
                '}';
    }

}
