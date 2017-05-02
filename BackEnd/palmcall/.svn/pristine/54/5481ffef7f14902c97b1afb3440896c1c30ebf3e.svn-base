package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class CheckCallableResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("查询结果: 0代表可打; -1代表caller欠费; -2receiver在通话中; " +
            "-4服务器处理扣费，请稍后再试,-5对方此时在免打扰时间")
    private Integer result;

    @NotNull
    @CodeGenField("打电话的人的剩余分钟数")
    private Integer leftMinutes = 0;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Integer leftMinutes) {
        this.leftMinutes = leftMinutes;
    }

    @Override
    public String toString() {
        return "CheckCallableResponse{" +
                "code='" + code + '\'' +
                ", result=" + result +
                ", leftMinutes=" + leftMinutes +
                '}';
    }

}
