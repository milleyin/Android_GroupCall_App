package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.innerApi.response.PalmCallGetStatusBatchResponse;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jtool.apiclient.ApiClient.Api;

@Repository
public class PalmcallGetStatusBatchApi {

    @Resource
    private String palmcallGetStatusBatchUrl;

    public PalmCallGetStatusBatchResponse sent(List<String> list) throws IOException {
        String afids = StringUtils.join(list, ",");

        Map<String, String> params = new HashMap<String, String>();
        params.put("afid", afids);

        String responseString = Api().param(params).post(palmcallGetStatusBatchUrl);
        return JSON.parseObject(responseString, PalmCallGetStatusBatchResponse.class);
    }

    public void validateResponse(PalmCallGetStatusBatchResponse response) throws ParamException, BackEndException {
        String code = response.getCode();
        if(code == null) {
            throw new RuntimeException("code为null:" + JSON.toJSONString(response));
        } else if(!"0".equals(code)) {
            switch (code) {
                case "-3":
                    throw new ParamException(response.toString());
                case "-99":
                    throw new BackEndException();
                default:
                    throw new RuntimeException("没有匹配的返回值：" + JSON.toJSONString(response));
            }
        }
    }

}
