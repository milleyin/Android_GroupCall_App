package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.innerApi.response.GetPalmchatProfileResponse;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jtool.apiclient.ApiClient.Api;

@Repository
public class GetPalmchatProfileApi {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private String getPalmchatProfileUrl;

    public String getPhoneCountryCode(String afid) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("afid", removePrefix(afid));

        String responseString = Api().param(params).post(getPalmchatProfileUrl);

        GetPalmchatProfileResponse response = JSON.parseObject(responseString, GetPalmchatProfileResponse.class);
        log.debug("根据afid获取profile：" + afid + ",返回：" + response);
        String phoneCountryCode = response.getPhoneCountryCode();
        if(StringUtils.isNotEmpty(phoneCountryCode)) {
            return phoneCountryCode;
        }

        return null;
    }

    public GetPalmchatProfileResponse sent(String afid) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("afid", removePrefix(afid));

        String responseString = Api().param(params).post(getPalmchatProfileUrl);

        GetPalmchatProfileResponse response = JSON.parseObject(responseString, GetPalmchatProfileResponse.class);
        log.debug("根据afid获取profile：" + afid + ",返回：" + response);

        return response;
    }

    // 去掉afid 前的a
    private String removePrefix(String afid) {
        String prefix = afid.substring(0, 1);
        try {
            new Integer(prefix);
        } catch (Exception e){
            return afid.substring(1);
        }
        return afid;
    }

}
