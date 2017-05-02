package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.innerApi.response.*;
import com.alibaba.fastjson.JSON;
import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.dreamhead.moco.Moco.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:/application-context.xml"})
public abstract class CommonControllerTest {
	//extends AbstractTransactionalJUnit4SpringContextTests 继承这个类，测试插入到数据库数据会回滚

	private Log log = LogFactory.getLog(this.getClass());

	public static final String getProfileUri = "/UserCenter/user/userBriefInfo";
	public static final String rechargeUri = "/ProfitServer/campaign";
	public static final String checkPalmCallTokenUri = "/cpctoken/";
	public static final String updatePalmCallTokenUri = "/upctoken/";

	private Runner runner;

	@Resource
	private WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

		HttpServer server = httpserver(8090);

		GetPalmchatProfileResponse profile = new GetPalmchatProfileResponse();
		profile.setIsBindPhone("T");
		profile.setPhone("123456789");
		profile.setPhoneCountryCode("234");
		profile.setName("beyond");
		profile.setSex("M");
		profile.setBirthdate("1989-08-15");

		GetPalmchatProfileResponse profile1 = new GetPalmchatProfileResponse();
		profile1.setCode("-2");

		RechargePalmCoinResponse recharge = new RechargePalmCoinResponse();
		recharge.setCode("0");

		RechargePalmCoinResponse recharge2 = new RechargePalmCoinResponse();
		recharge2.setCode("-99");

		CheckPCTokenResponse checkPCTokenResponse = new CheckPCTokenResponse();
		checkPCTokenResponse.setAfid("a1010701");

		UpdatePCTokenResponse updatePCTokenResponse = new UpdatePCTokenResponse();
		updatePCTokenResponse.setCode("0");
		updatePCTokenResponse.setPctoken("01Dn2j7i463QMWtLwp2sxyrIohd2OYjLgDXq");

		server.post(and(by(uri(getProfileUri)), eq(form("afid"), "1010674"))).response(JSON.toJSONString(profile1));
		server.post(and(by(uri(getProfileUri)))).response(JSON.toJSONString(profile));
		server.post(and(by(uri(rechargeUri)), eq(form("afid"), "a1010675"))).response(JSON.toJSONString(recharge2));
		server.post(and(by(uri(rechargeUri)), eq(form("afid"), "a1010710"))).response(JSON.toJSONString(recharge));
		server.post(and(by(uri(rechargeUri)))).response(JSON.toJSONString(recharge));
		checkPCTokenResponse.setCode("-65");
		server.post(and(by(uri(checkPalmCallTokenUri)), eq(form("session"), "019973947f47d"))).response(JSON.toJSONString(checkPCTokenResponse));
		checkPCTokenResponse.setCode("-76");
		server.post(and(by(uri(checkPalmCallTokenUri)), eq(form("session"), "019973947f47f"))).response(JSON.toJSONString(checkPCTokenResponse));
		checkPCTokenResponse.setCode("-66");
		server.post(and(by(uri(checkPalmCallTokenUri)), eq(form("pctoken"), "01Dn2j7i463QMWtLwp2sxyrIohd2OYjLgDXr"))).response(JSON.toJSONString(checkPCTokenResponse));
		checkPCTokenResponse.setCode("0");
		server.post(and(by(uri(checkPalmCallTokenUri)))).response(JSON.toJSONString(checkPCTokenResponse));
		server.post(and(by(uri(updatePalmCallTokenUri)))).response(JSON.toJSONString(updatePCTokenResponse));

		runner = Runner.runner(server);
		runner.start();
	}

	@After
	public void after() {
		runner.stop();
	}
	
	protected MockHttpServletRequestBuilder makePostByParams(String uri, Map<String, Object> params) {
		
		List<String> fileKeys = params.keySet().stream().filter(key -> params.get(key) instanceof MockMultipartFile).collect(Collectors.toList());
		
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder;
		
		if(fileKeys.size() == 0) {
			mockHttpServletRequestBuilder = post(uri);
		} else {
			MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = fileUpload(uri);
			fileKeys.stream().forEach(key -> mockMultipartHttpServletRequestBuilder.file((MockMultipartFile)params.get(key)));
			fileKeys.stream().forEach(key -> params.remove(key));
			mockHttpServletRequestBuilder = mockMultipartHttpServletRequestBuilder;
		}
		
		params.keySet().stream().forEach(e -> mockHttpServletRequestBuilder.param(e, params.get(e).toString()));
		
		System.out.println("request param:" + params);
		
		return mockHttpServletRequestBuilder;
	}

	protected MockHttpServletRequestBuilder makePostByParamsWhere(String uri, Map<String, Object> params) {

		List<String> fileKeys = params.keySet().stream().filter(key -> params.get(key) instanceof MockMultipartFile).collect(Collectors.toList());

		MockHttpServletRequestBuilder mockHttpServletRequestBuilder;

		if(fileKeys.size() == 0) {
			mockHttpServletRequestBuilder = post(uri).header("where", "kafdoafda#f658adf*2s37^w(ew%43sdf#afdaf2ksijuewehjhk&jihwe9");
		} else {
			MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = fileUpload(uri);
			fileKeys.stream().forEach(key -> mockMultipartHttpServletRequestBuilder.file((MockMultipartFile)params.get(key)));
			fileKeys.stream().forEach(key -> params.remove(key));
			mockHttpServletRequestBuilder = mockMultipartHttpServletRequestBuilder;
		}

		params.keySet().stream().forEach(e -> mockHttpServletRequestBuilder.param(e, params.get(e).toString()));

		System.out.println("request param:" + params);

		return mockHttpServletRequestBuilder;
	}

	protected MockHttpServletRequestBuilder makeGetByParams(String uri, Map<String, Object> params) {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(uri);
		params.keySet().stream().map(e -> mockHttpServletRequestBuilder.param(e, params.get(e).toString()));
		
		return mockHttpServletRequestBuilder;
	}

	protected MockHttpServletRequestBuilder makeGetByParamsWhere(String uri, Map<String, Object> params) {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(uri).header("where", "kafdoafda#f658adf*2s37^w(ew%43sdf#afdaf2ksijuewehjhk&jihwe9");
		params.keySet().stream().map(e -> mockHttpServletRequestBuilder.param(e, params.get(e).toString()));

		return mockHttpServletRequestBuilder;
	}

	protected MockHttpServletRequestBuilder makeGetByParams2(String uri, String params) {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(uri);
		mockHttpServletRequestBuilder.content(params);

		return mockHttpServletRequestBuilder;
	}

	protected String requestContentStringGet2(String uri, String params) throws UnsupportedEncodingException, Exception {
		log.debug("发送地址：" + uri);
		String result = this.mockMvc.perform(makeGetByParams2(uri, params)).andReturn().getResponse().getContentAsString();
		log.debug("返回数据：" + result);
		return result;
	}

	protected String requestContentStringGet(String uri, Map<String, ?> params) throws UnsupportedEncodingException, Exception {
		Map<String, Object> innerParams = new HashMap<>();
		for(String key : params.keySet()) {
			if(params.get(key) != null) {
				innerParams.put(key, params.get(key));
			}
		}
		log.debug("发送地址：" + uri);
		String result = this.mockMvc.perform(makeGetByParams(uri, innerParams)).andReturn().getResponse().getContentAsString();
		log.debug("返回数据：" + result);
		return result;
	}

	protected String requestContentStringGetWhere(String uri, Map<String, ?> params) throws UnsupportedEncodingException, Exception {
		Map<String, Object> innerParams = new HashMap<>();
		for(String key : params.keySet()) {
			if(params.get(key) != null) {
				innerParams.put(key, params.get(key));
			}
		}
		log.debug("发送地址：" + uri);
		String result = this.mockMvc.perform(makeGetByParamsWhere(uri, innerParams)).andReturn().getResponse().getContentAsString();
		log.debug("返回数据：" + result);
		return result;
	}

	protected String requestContentString(String uri, Map<String, ?> params) throws UnsupportedEncodingException, Exception {
		Map<String, Object> innerParams = new HashMap<>();
		for(String key : params.keySet()) {
			if(params.get(key) != null) {
				innerParams.put(key, params.get(key));
			}
		}
		log.debug("发送地址：" + uri);
		String result = this.mockMvc.perform(makePostByParams(uri, innerParams)).andReturn().getResponse().getContentAsString();
		log.debug("返回数据：" + result);
		return result;
	}

	protected String requestContentStringWhere(String uri, Map<String, ?> params) throws UnsupportedEncodingException, Exception {
		Map<String, Object> innerParams = new HashMap<>();
		for(String key : params.keySet()) {
			if(params.get(key) != null) {
				innerParams.put(key, params.get(key));
			}
		}
		log.debug("发送地址：" + uri);
		String result = this.mockMvc.perform(makePostByParamsWhere(uri, innerParams)).andReturn().getResponse().getContentAsString();
		log.debug("返回数据：" + result);
		return result;
	}

	protected String requestCode(String uri, Map<String, Object> params) throws Exception {
		String source = this.mockMvc.perform(makePostByParams(uri, params)).andReturn().getResponse().getContentAsString();
		return JSON.parseObject(source, String.class);
	}
	
	protected void assertCode(String uri, Map<String, Object> params, int code) throws Exception {
		Assert.assertEquals(code, requestCode(uri, params));
	}
	
}
