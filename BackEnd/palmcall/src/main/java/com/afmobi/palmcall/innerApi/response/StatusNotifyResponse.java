package com.afmobi.palmcall.innerApi.response;

public class StatusNotifyResponse {

    private Long tid; //事务ID。您的HTTP服务器回复的响应中，该字段的值应与请求报文保持一致。
    private boolean ret;//true表示收到通知。

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    @Override
    public String toString() {
        return "StatusNotifyResponse{" +
                "tid=" + tid +
                ", ret=" + ret +
                '}';
    }

}
