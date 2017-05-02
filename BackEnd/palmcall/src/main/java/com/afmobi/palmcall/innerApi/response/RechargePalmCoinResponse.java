package com.afmobi.palmcall.innerApi.response;

public class RechargePalmCoinResponse {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RechargePalmCoinResponse{" +
                "code='" + code + '\'' +
                '}';
    }

}
