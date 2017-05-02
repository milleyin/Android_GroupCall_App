package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AddOrDelBatchUserHotResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("添加或删除失败的afid和原因列表")
    private List<String> afidList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getAfidList() {
        return afidList;
    }

    public void setAfidList(List<String> afidList) {
        this.afidList = afidList;
    }

    @Override
    public String toString() {
        return "AddOrDelBatchUserHotResponse{" +
                "code='" + code + '\'' +
                ", afidList=" + afidList +
                '}';
    }

}
