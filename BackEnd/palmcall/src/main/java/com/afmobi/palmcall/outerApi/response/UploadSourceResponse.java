package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class UploadSourceResponse {

    @NotNull
    @CodeGenField("状态码, 0：正确")
    private String code;

    @NotNull
    @CodeGenField("palmcall新令牌")
    private String pctoken;

    @NotNull
    @CodeGenField("图片或语音版本，时间戳")
    private String ts;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPctoken() {
        return pctoken;
    }

    public void setPctoken(String pctoken) {
        this.pctoken = pctoken;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "UploadSourceResponse{" +
                "code='" + code + '\'' +
                ", pctoken='" + pctoken + '\'' +
                ", ts='" + ts + '\'' +
                '}';
    }

}
