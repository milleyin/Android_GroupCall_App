package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetBatchProfileResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("profile列表")
    private List<Profile> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Profile> getList() {
        return list;
    }

    public void setList(List<Profile> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "GetBatchProfileResponse{" +
                "code='" + code + '\'' +
                ", list=" + list +
                '}';
    }

}
