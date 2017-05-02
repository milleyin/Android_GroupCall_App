package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.innerApi.response.PalmCallHostListResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.IOException;

import static com.jtool.apiclient.ApiClient.Api;

@Repository
public class PalmcallHostListApi {

    @Resource
    private String palmcallHostListUrl;

    public PalmCallHostListResponse sent() throws IOException {
        String responseString = Api().get(palmcallHostListUrl);
        return JSON.parseObject(responseString, PalmCallHostListResponse.class);
    }

    public void validateResponse(PalmCallHostListResponse response) throws BackEndException {
        String code = response.getCode();
        if(code == null) {
            throw new RuntimeException("code为null:" + JSON.toJSONString(response));
        } else if(!"0".equals(code)) {
            switch (code) {
                case "-99":
                    throw new BackEndException();
                default:
                    throw new RuntimeException("没有匹配的返回值：" + JSON.toJSONString(response));
            }
        }
    }

}
