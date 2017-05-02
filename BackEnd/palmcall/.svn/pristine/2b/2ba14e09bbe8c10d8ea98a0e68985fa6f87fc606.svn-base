package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.exception.NotFindSessionException;
import com.afmobi.palmcall.exception.NotValidePCtokenException;
import com.afmobi.palmcall.exception.NotValideSessionException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.innerApi.response.CheckPCTokenResponse;
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
public class CheckPCTokenApi {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private String checkPalmCallTokenUri;
	
	@Resource
	private ControlServerHostSelectHelper controlServerHostSelectHelper;

	public CheckPCTokenResponse sent(String session, String pctoken)
			throws NotValideSessionException, NotValidePCtokenException, NotFindSessionException,
			IOException, ParamException {

		Map<String, String> params = new HashMap<>();
		params.put("session", session);
		params.put("pctoken", pctoken);

		String csHost = controlServerHostSelectHelper.selectHostByBtoken(pctoken);
		String responseString = Api().param(params).post(csHost + checkPalmCallTokenUri);
		log.debug("验证session和pctoken,返回：" + responseString);

		CheckPCTokenResponse checkPCTokenResponse = JSON.parseObject(responseString, CheckPCTokenResponse.class);
		validateResponse(checkPCTokenResponse);

		return checkPCTokenResponse;
	}

	private void validateResponse(CheckPCTokenResponse response)
			throws NotFindSessionException, NotValidePCtokenException, NotValideSessionException, ParamException {
		String code = response.getCode();
		if(code == null) {
			throw new RuntimeException("code为null:" + JSON.toJSONString(response));
		} else if(!"0".equals(code)) {
			switch (code) {
				case "-3":
					throw new ParamException("检查palmcall token参数错误");
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
