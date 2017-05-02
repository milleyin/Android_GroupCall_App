package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.CallRecordResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class CallRecordControllerTest extends CommonControllerTest {

	@Test
	public void test查询充值记录() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701&status=2", new HashMap<>());
		CallRecordResponse response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getRecords().size() >= 0);
	}

	@Test
	public void test查询打电话消费记录() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701&status=0", new HashMap<>());
		CallRecordResponse response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getRecords().size() >= 0);
	}

	@Test
	public void test查询赠送记录() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701&status=1", new HashMap<>());
		CallRecordResponse response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getRecords().size() >= 0);
	}

	@Test
	public void test查询的afid不存在() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a111111", new HashMap<>());
		CallRecordResponse response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getRecords().size() == 0);
	}

	@Test
	public void test分页返回5条记录() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701&start=5&limit=5", new HashMap<>());
		CallRecordResponse response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getRecords().size() <=5);
	}

	@Test
	public void test正确返回记录() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701", new HashMap<>());
		CallRecordResponse response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getRecords().size() >= 0);
	}

	@Test
	public void testLimit参数错误() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701&limit=60", new HashMap<>());
		CallRecordResponse  response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void testStart参数错误() throws Exception {
		String json = requestContentStringGetWhere("/callRecord?afid=a1010701&start=-1", new HashMap<>());
		CallRecordResponse  response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test参数全为空() throws Exception {
		String json = requestContentStringGetWhere("/callRecord", new HashMap<>());
		CallRecordResponse  response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test只允许内网访问() throws Exception {
		String json = requestContentStringGet("/callRecord", new HashMap<>());
		CallRecordResponse  response = JSON.parseObject(json, CallRecordResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

}
