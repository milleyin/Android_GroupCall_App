package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.innerApi.response.RechargePalmCoinResponse;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jtool.apiclient.ApiClient.Api;

@Repository
public class RechargePalmCoinApi {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private String MinutesRechargePalmCoinUrl;

	public RechargePalmCoinResponse sent(String afid, int amount, String phoneCountryCode) throws IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("afid", afid);
		params.put("type", "business");
		params.put("appId", "1");
		params.put("countryCode", phoneCountryCode);
		params.put("sence", "PalmCall");
		params.put("timestamp", System.currentTimeMillis());
		params.put("amount", amount);

		String responseString = Api().param(params).post(MinutesRechargePalmCoinUrl);
		log.debug("充值PalmCoin：" + afid + ",返回：" + responseString);
		return JSON.parseObject(responseString, RechargePalmCoinResponse.class);
	}

	//判断是否充值成功
	public boolean isRechargeSuccess(RechargePalmCoinResponse response) {
		String code = response.getCode();
		if(code == null) {
			return false;
		} else if(!"0".equals(code)) {
			switch (code) {
				case "-3":
					return  false;
				case "-99":
					return  false;
				default:
					return  false;
			}
		}

		return true;
	}

}
