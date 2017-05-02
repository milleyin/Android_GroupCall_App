package com.afmobi.palmcall.innerApi.api;

import com.afmobi.palmcall.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jtool.apiclient.ApiClient.Api;

@Repository
public class PushPalmcallMsgApi {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private String palmcallPushUrl;

	@Async("apiExecutor")
	public void sentMsg(String afid, String msg, String logId) throws IOException {
		Map<String, String> params = new HashMap<>();
		params.put("pType", "4");
		params.put("sender", "r99920061");//palmcall公众账号
		params.put("receiver", afid);
		//command(消息类型) + base64后{"content":"msg","url":""}，在url编码(封装的Api()已经做了url编码)
		String message = "LkBBMTM=" + Base64Util.encode("{\"content\":\"" + msg + "\",\"url\":\"\"}");
		params.put("msg", message);

		String responseString = Api().logId(logId).param(params).post(palmcallPushUrl);
		log.debug("发送PalmCall推送消息给：" + afid + ",返回：" + responseString);

	}

}
