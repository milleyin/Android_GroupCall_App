package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetBatchProfileRequest {

    @NotNull
    @Size(min = 1, max = 10000)
    @CodeGenField("多个afid以逗号分开,如:a1010701,a1010702")
    private String afids;

    public String getAfids() {
        return afids;
    }

    public void setAfids(String afids) {
        this.afids = afids;
    }

    @Override
    public String toString() {
        return "GetBatchProfileRequest{" +
                "afids='" + afids + '\'' +
                '}';
    }

}
