package com.afmobi.palmcall.innerApi.api;

import com.jtool.http.WebGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SourceDownloadApi {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private String showPicDownloadUrl;

	public Map<String, Object> sent(String afid, String pixel, String sn)
			throws IOException {

		Map<String, String> params = new HashMap<String, String>();
		params.put("afid", afid);
		params.put("pixel", pixel);
		params.put("sn", sn);

		log.debug(params.toString());

		byte[] content = {};

		int i = 0;

		do {
			content = WebGet.sentAndReturnBytes(showPicDownloadUrl, params);
			i++;
		} while (content.length > 0 && i < 5);

		params.put("url", showPicDownloadUrl);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sent", params.toString());
		result.put("content", content);

		return result;
	}
	
}
