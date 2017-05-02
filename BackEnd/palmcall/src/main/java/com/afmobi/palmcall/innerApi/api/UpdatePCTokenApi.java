package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.exception.NotFindSessionException;
import com.afmobi.palmcall.exception.NotValidePCtokenException;
import com.afmobi.palmcall.exception.NotValideSessionException;
import com.afmobi.palmcall.innerApi.response.UpdatePCTokenResponse;
import com.afmobi.palmcall.util.ControlServerHostSelectHelper;
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
public class UpdatePCTokenApi {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private String updatePalmCallTokenUri;
	
	@Resource
	private ControlServerHostSelectHelper controlServerHostSelectHelper;

	public UpdatePCTokenResponse sent(String session, String pctoken)
			throws NotValideSessionException, NotValidePCtokenException, NotFindSessionException, IOException {

		Map<String, String> params = new HashMap<>();
		params.put("session", session);
		params.put("pctoken", pctoken);

		String csHost = controlServerHostSelectHelper.selectHostByBtoken(pctoken);
		String responseString = Api().param(params).post(csHost + updatePalmCallTokenUri);
		log.debug("改变pctoken,返回：" + responseString);

		UpdatePCTokenResponse updatePCTokenResponse = JSON.parseObject(responseString, UpdatePCTokenResponse.class);
		validateResponse(updatePCTokenResponse);

		return updatePCTokenResponse;
	}

	private void validateResponse(UpdatePCTokenResponse response)
			throws NotFindSessionException, NotValidePCtokenException, NotValideSessionException {
		String code = response.getCode();
		if(code == null) {
			throw new RuntimeException("code为null:" + JSON.toJSONString(response));
		} else if(!"0".equals(code)) {
			switch (code) {
				case "-65":
					throw new NotFindSessionException();
				case "-66":
					throw new NotValidePCtokenException();
				case "-76":
					throw new NotValideSessionException();
				default:
					throw new RuntimeException("没有匹配的返回值：" + response.toString());
			}
		}
	}

}
