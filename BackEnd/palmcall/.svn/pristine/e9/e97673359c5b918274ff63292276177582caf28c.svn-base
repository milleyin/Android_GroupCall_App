package com.afmobi.palmcall.outerApi.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.List;

public class FetchCallableListResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @NotNull
    @CodeGenField("获取可打电话的列表")
    private List<CallableItem> list;

    @NotNull
    @CodeGenField("剩余分钟数")
    private Integer leftMinutes = 0;

    @CodeGenField("赠送分钟数")
    private Integer giftMinutes;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CallableItem> getList() {
        return list;
    }

    public void setList(List<CallableItem> list) {
        this.list = list;
    }

    public Integer getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Integer leftMinutes) {
        this.leftMinutes = leftMinutes;
    }

    public Integer getGiftMinutes() {
        return giftMinutes;
    }

    public void setGiftMinutes(Integer giftMinutes) {
        this.giftMinutes = giftMinutes;
    }

    @Override
    public String toString() {
        return "FetchCallableListResponse{" +
                "code='" + code + '\'' +
                ", list=" + list +
                ", leftMinutes=" + leftMinutes +
                ", giftMinutes=" + giftMinutes +
                '}';
    }

}
