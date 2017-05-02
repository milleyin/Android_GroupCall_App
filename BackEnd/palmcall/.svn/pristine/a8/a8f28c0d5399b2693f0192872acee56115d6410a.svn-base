package com.afmobi.palmcall.outerApi.request;

import com.jtool.annotation.AvailableValues;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddOrDelBatchUserHotRequest {

    @NotNull
    @Size(min = 1, max = 1000)
    @CodeGenField("多个afid以逗号分开,如:a1010701,a1010702")
    private String afids;

    @NotNull
    @AvailableValues(values={"0", "1"})
    @CodeGenField("0：删除<br/>1：添加")
    private String type;

    @DecimalMin("0")
    @CodeGenField("水军用户初始化接听次数，默认为1000次")
    private Integer amount = 1000;

    public String getAfids() {
        return afids;
    }

    public void setAfids(String afids) {
        this.afids = afids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AddOrDelBatchUserHotRequest{" +
                "afids='" + afids + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }

}
