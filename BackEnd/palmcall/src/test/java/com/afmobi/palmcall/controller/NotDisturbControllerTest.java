package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.GetNotDisturbResponse;
import com.afmobi.palmcall.outerApi.response.SettingNotDisturbResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NotDisturbControllerTest extends CommonControllerTest {

	//----------------------------------获取免打扰时间--------------------------------------
	@Test
	public void test获取免打扰参数全为空() throws Exception {
		String json = requestContentStringGetWhere("/getNotDisturb", new HashMap<>());
		GetNotDisturbResponse response = JSON.parseObject(json, GetNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test获取免打扰() throws Exception {
		String json = requestContentStringGetWhere("/getNotDisturb?afid=a1010701", new HashMap<>());
		GetNotDisturbResponse response = JSON.parseObject(json, GetNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getOpen() >= 0);
	}

	@Test
	public void test获取免打扰Afid不存在时() throws Exception {
		String time = ("" + System.currentTimeMillis()).substring(6);
		String afid = "a" + time;
		String json = requestContentStringGetWhere("/getNotDisturb?afid=" + afid, new HashMap<>());
		GetNotDisturbResponse response = JSON.parseObject(json, GetNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getOpen() >= 0);
	}

	@Test
	public void test获取免打扰只允许内网访问() throws Exception {
		String json = requestContentStringGet("/getNotDisturb?afid=a1010701", new HashMap<>());
		GetNotDisturbResponse response = JSON.parseObject(json, GetNotDisturbResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

	//----------------------------------设置免打扰时间--------------------------------------
	@Test
	public void test设置免打扰Afid不存在时() throws Exception {
		Map<String, Object> params = makeParam();
		String time = ("" + System.currentTimeMillis()).substring(6);
		String afid = "a" + time;
		params.put("afid", afid);
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test设置免打扰开启() throws Exception {
		Map<String, Object> params = makeParam();
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test设置免打扰全为默认值() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afid", "a1010701");
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test设置免打扰startTime和endTime相同错误() throws Exception {
		Map<String, Object> params = makeParam();
		params.put("endTime", 8);
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test设置免打扰endTime参数错误() throws Exception {
		Map<String, Object> params = makeParam();
		params.put("endTime", 25);
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test设置免打扰startTime参数错误() throws Exception {
		Map<String, Object> params = makeParam();
		params.put("startTime", -1);
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test设置免打扰open参数错误() throws Exception {
		Map<String, Object> params = makeParam();
		params.put("open", 2);
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test设置免打扰afid参数为空() throws Exception {
		Map<String, Object> params = makeParam();
		params.remove("afid");
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse  response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test设置免打扰参数全为空() throws Exception {
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", new HashMap<>());
		SettingNotDisturbResponse response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test设置免打扰只允许内网访问() throws Exception {
		String json = requestContentString("/settingOrUpdateNotDisturb", new HashMap<>());
		SettingNotDisturbResponse response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

	private Map<String, Object> makeParam () {
		Map<String, Object> params = new HashMap<>();
		params.put("afid", "a1010701");
		params.put("open", 1);
		params.put("startTime", 8);
		params.put("endTime", 20);
		return params;
	}

}
