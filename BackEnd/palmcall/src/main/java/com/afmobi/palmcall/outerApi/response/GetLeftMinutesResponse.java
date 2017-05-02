package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class GetLeftMinutesResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("剩余分钟数")
    private Integer leftMinutes = 0;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Integer leftMinutes) {
        this.leftMinutes = leftMinutes;
    }

    @Override
    public String toString() {
        return "GetLeftMinutesResponse{" +
                "code='" + code + '\'' +
                ", leftMinutes=" + leftMinutes +
                '}';
    }

}
