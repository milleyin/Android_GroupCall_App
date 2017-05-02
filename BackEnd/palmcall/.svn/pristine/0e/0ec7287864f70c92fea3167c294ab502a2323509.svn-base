package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetNotDisturbRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("afid")
    private String afid;

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    @Override
    public String toString() {
        return "GetNotDisturbRequest{" +
                "afid='" + afid + '\'' +
                '}';
    }

}
