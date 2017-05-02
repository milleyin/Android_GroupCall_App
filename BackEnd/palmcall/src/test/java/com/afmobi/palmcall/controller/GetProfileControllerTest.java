package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.GetBatchProfileResponse;
import com.afmobi.palmcall.outerApi.response.GetProfileResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GetProfileControllerTest extends CommonControllerTest {

	//----------------------------------查询单个用户的profile--------------------------------------
	@Test
	public void test查询单个用户存在正确返回记录() throws Exception {
		String json = requestContentStringGetWhere("/getProfile?afid=a1010701", new HashMap<>());
		GetProfileResponse  response = JSON.parseObject(json, GetProfileResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test查询单个用户不存在时() throws Exception {
		String json = requestContentStringGetWhere("/getProfile?afid=a1010000", new HashMap<>());
		GetProfileResponse  response = JSON.parseObject(json, GetProfileResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertEquals("-1", response.getErrorCode());
	}

	@Test
	public void test查询单个用户参数全为空() throws Exception {
		String json = requestContentStringGetWhere("/getProfile", new HashMap<>());
		GetProfileResponse  response = JSON.parseObject(json, GetProfileResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test查询单个用户只允许内网访问() throws Exception {
		String json = requestContentStringGet("/getProfile", new HashMap<>());
		GetProfileResponse response = JSON.parseObject(json, GetProfileResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

	//----------------------------------批量查询用户的profile--------------------------------------

	@Test
	public void test批量查询多用户正确返回记录() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701,a1010674,a1010702");
		String json = requestContentStringWhere("/getBatchProfile", params);
		GetBatchProfileResponse response = JSON.parseObject(json, GetBatchProfileResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getList().size() > 0);
	}

	@Test
	public void test批量查询多用户不存在时() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a101000,a1010111");
		String json = requestContentStringWhere("/getBatchProfile", params);
		GetBatchProfileResponse response = JSON.parseObject(json, GetBatchProfileResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getList().size() == 0);
	}

	@Test
	public void test批量查询一个用户存在正确返回记录() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		String json = requestContentStringWhere("/getBatchProfile", params);
		GetBatchProfileResponse response = JSON.parseObject(json, GetBatchProfileResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getList().size() > 0);
	}

	@Test
	public void test批量查询用户参数全为空() throws Exception {
		String json = requestContentStringWhere("/getBatchProfile", new HashMap<>());
		GetBatchProfileResponse  response = JSON.parseObject(json, GetBatchProfileResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test批量查询用户只允许内网访问() throws Exception {
		String json = requestContentString("/getBatchProfile", new HashMap<>());
		GetBatchProfileResponse response = JSON.parseObject(json, GetBatchProfileResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

}
