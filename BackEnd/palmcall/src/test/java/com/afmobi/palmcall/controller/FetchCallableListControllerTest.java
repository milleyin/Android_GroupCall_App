package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.outerApi.response.FetchCallableListResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class FetchCallableListControllerTest extends CommonControllerTest {

	//获取接线人员列表,第一次赠送时
	@Test
	public void test用户不存在数据库且第一次赠送分钟() throws Exception {
		String time = ("" + System.currentTimeMillis()).substring(6);
		String afid = "a" + time;
		String json = requestContentStringGetWhere("/fetchCallableList?afid=" + afid + "&gift=1", new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getGiftMinutes() > 0);
		Assert.assertNotNull(response.getList());
		if (response.getList().size() > 0) {
			Assert.assertNotNull(response.getList().get(0).getAfid());
		}
	}

	//获取接线人员列表,用户不存在 不赠送时
	@Test
	public void test用户不存在数据库且不赠送分钟() throws Exception {
		String time = ("" + System.currentTimeMillis()).substring(6);
		String afid = "a" + time;
		String json = requestContentStringGetWhere("/fetchCallableList?afid=" + afid, new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertNotNull(response.getList());
		if (response.getList().size() > 0) {
			Assert.assertNotNull(response.getList().get(0).getAfid());
		}

		用户存在数据库且赠送分钟国家码为234(afid);
	}

	//用户存在且没赠送过，传国家码为234
	public void 用户存在数据库且赠送分钟国家码为234(String afid) throws Exception {
		String json = requestContentStringGetWhere("/fetchCallableList?afid=" + afid + "&gift=1&countrycode=234", new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getGiftMinutes() > 0);
		Assert.assertNotNull(response.getList());
		if (response.getList().size() > 0) {
			Assert.assertNotNull(response.getList().get(0).getAfid());
		}
	}

	//已经赠送过分钟数
	@Test
	public void test已经赠送过分钟数在传1赠送时() throws Exception {
		String json = requestContentStringGetWhere("/fetchCallableList?afid=a1010701&gift=1", new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getLeftMinutes() >= 0);
		Assert.assertNotNull(response.getList());
		if (response.getList().size() > 0) {
			Assert.assertNotNull(response.getList().get(0).getAfid());
		}
		for (CallableItem item : response.getList()) {
			Assert.assertNotEquals("a1010701", item.getAfid());
		}
	}

	@Test
	public void test用户存在数据库且不赠送分钟() throws Exception {
		String json = requestContentStringGetWhere("/fetchCallableList?afid=a1010701", new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getLeftMinutes() >= 0);
		Assert.assertNotNull(response.getList());
		if (response.getList().size() > 0) {
			Assert.assertNotNull(response.getList().get(0).getAfid());
		}
	}

	@Test
	public void test参数全为空() throws Exception {
		String json = requestContentStringGetWhere("/fetchCallableList", new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test只允许内网访问() throws Exception {
		String json = requestContentStringGet("/fetchCallableList", new HashMap<>());
		FetchCallableListResponse response = JSON.parseObject(json, FetchCallableListResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

}
