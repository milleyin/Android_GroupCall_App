package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CallRecordRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("afid,用于查询通话记录")
    private String afid;

    @DecimalMin("0")
    @CodeGenField("0:打电话消费，1:赠送，2:充值,默认99:所有")
    private Integer status = 99;

    @DecimalMin("0")
    @DecimalMax("1000")
    @CodeGenField("分页，开始页,默认为0")
    private Integer start = 0;

    @DecimalMin(value="1")
    @DecimalMax(value="50")
    @CodeGenField("分页，返回多少条，默认为10")
    private Integer limit = 10;

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "CallRecordRequest{" +
                "afid='" + afid + '\'' +
                ", status=" + status +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }

}
