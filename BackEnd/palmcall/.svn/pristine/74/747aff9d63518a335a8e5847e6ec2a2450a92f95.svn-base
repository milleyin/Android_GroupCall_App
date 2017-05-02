package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.CheckCallableResponse;
import com.afmobi.palmcall.outerApi.response.GetLeftMinutesResponse;
import com.afmobi.palmcall.outerApi.response.RechargeLeftMinutesResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MinutesControllerTest extends CommonControllerTest {

	//----------------------------------充值分钟数--------------------
	@Test
	public void test充值剩余分钟Afid不存在时() throws Exception {
		String time = ("" + System.currentTimeMillis()).substring(6);
		String afid = "a" + time;
		Map<String, Object> params = makeParam();
		params.put("afid", afid);
		String json = requestContentStringWhere("/rechargeLeftMinutes", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test充值剩余分钟Afid存在时() throws Exception {
		Map<String, Object> params = makeParam();
		String json = requestContentStringWhere("/rechargeLeftMinutes", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test充值分钟数小于等于0时() throws Exception {
		Map<String, Object> params = makeParam();
		params.put("minutes", "0");
		String json = requestContentStringWhere("/rechargeLeftMinutes", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test充值参数全为空() throws Exception {
		String json = requestContentStringWhere("/rechargeLeftMinutes", new HashMap<>());
		RechargeLeftMinutesResponse response = JSON.parseObject(json, RechargeLeftMinutesResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test充值只允许内网访问() throws Exception {
		String json = requestContentString("/rechargeLeftMinutes", new HashMap<>());
		RechargeLeftMinutesResponse response = JSON.parseObject(json, RechargeLeftMinutesResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

	private Map<String, Object> makeParam () {
		Map<String, Object> params = new HashMap<>();
		params.put("afid", "a1010701");
		params.put("minutes", "10");
		return params;
	}

	//----------------------------------查询剩余分钟数--------------------
	@Test
	public void test查询剩余分钟数Afid不存在时() throws Exception {
		String json = requestContentStringGetWhere("/getLeftMinutes?afid=a1222222", new HashMap<>());
		GetLeftMinutesResponse response = JSON.parseObject(json, GetLeftMinutesResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getLeftMinutes() >= 0);
	}

	@Test
	public void test查询剩余分钟数() throws Exception {
		String json = requestContentStringGetWhere("/getLeftMinutes?afid=a1010701", new HashMap<>());
		GetLeftMinutesResponse response = JSON.parseObject(json, GetLeftMinutesResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getLeftMinutes() >= 0);
	}

	@Test
	public void test查询剩余分钟数参数全为空() throws Exception {
		String json = requestContentStringGetWhere("/getLeftMinutes", new HashMap<>());
		GetLeftMinutesResponse response = JSON.parseObject(json, GetLeftMinutesResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test查询剩余分钟数只允许内网访问() throws Exception {
		String json = requestContentStringGet("/getLeftMinutes", new HashMap<>());
		GetLeftMinutesResponse response = JSON.parseObject(json, GetLeftMinutesResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

}
