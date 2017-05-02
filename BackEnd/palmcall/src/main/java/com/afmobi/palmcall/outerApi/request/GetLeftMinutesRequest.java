package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetLeftMinutesRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("afid,用于查询返回剩余分钟数")
    private String afid;

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    @Override
    public String toString() {
        return "GetLeftMinutesRequest{" +
                "afid='" + afid + '\'' +
                '}';
    }

}
