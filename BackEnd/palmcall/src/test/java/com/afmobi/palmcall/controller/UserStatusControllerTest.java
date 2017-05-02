package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.outerApi.response.AddOrDelBatchUserHotResponse;
import com.afmobi.palmcall.outerApi.response.UpdateUserStatusResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserStatusControllerTest extends CommonControllerTest {

	//----------------------------------批量添加或删除水军用户--------------------------------------
	@Test
	public void test批量添加或删除水军用户添加一个成功() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		params.put("type", "1");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户删除一个成功() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		params.put("type", "0");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户没有绑定手机号() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010674");
		params.put("type", "1");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getAfidList().size() > 0);
	}

	@Test
	public void test批量添加或删除水军用户已经存在() throws Exception {
		test批量添加或删除水军用户添加一个成功();

		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		params.put("type", "1");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getAfidList().size() > 0);

		test批量添加或删除水军用户删除一个成功();
	}

	@Test
	public void test批量添加或删除水军用户添加多个成功() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010101,a1010102");
		params.put("type", "1");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户删除多个成功() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010101,a1010102,a1010674");
		params.put("type", "0");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户amount参数错误() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		params.put("type", "1");
		params.put("amount", "-1");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户type参数错误() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		params.put("type", "2");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户type参数为空() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afids", "a1010701");
		String json = requestContentStringWhere("/addOrDelBatchUserHot", params);
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户参数全为空() throws Exception {
		String json = requestContentStringWhere("/addOrDelBatchUserHot", new HashMap<>());
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test批量添加或删除水军用户只允许内网访问() throws Exception {
		String json = requestContentString("/addOrDelBatchUserHot", new HashMap<>());
		AddOrDelBatchUserHotResponse response = JSON.parseObject(json, AddOrDelBatchUserHotResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

	//----------------------------------修改水军在线状态--------------------------------------
	@Test
	public void test修改在线状态正确返回() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afid", "a1010701");
		String json = requestContentStringWhere("/updateUserStatus", params);
		UpdateUserStatusResponse response = JSON.parseObject(json, UpdateUserStatusResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test修改在线状态用户不存在时() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("afid", "a1010701111");
		String json = requestContentStringWhere("/updateUserStatus", params);
		UpdateUserStatusResponse response = JSON.parseObject(json, UpdateUserStatusResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test修改在线状态参数为空() throws Exception {
		String json = requestContentStringWhere("/updateUserStatus", new HashMap<>());
		UpdateUserStatusResponse  response = JSON.parseObject(json, UpdateUserStatusResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test修改在线状态只允许内网访问() throws Exception {
		String json = requestContentString("/updateUserStatus", new HashMap<>());
		UpdateUserStatusResponse response = JSON.parseObject(json, UpdateUserStatusResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

}
