package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CheckCallableRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("打电话的人的afid")
    private String callerId;

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("接电话的人的afid")
    private String receiverId;

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "CheckCallableRequest{" +
                "callerId='" + callerId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                '}';
    }

}
