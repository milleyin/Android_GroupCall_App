package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.innerApi.response.StatusNotifyResponse;
import com.afmobi.palmcall.outerApi.response.CheckCallableResponse;
import com.afmobi.palmcall.outerApi.response.SettingNotDisturbResponse;
import com.afmobi.palmcall.util.CallOperatorHelper;
import com.afmobi.palmcall.util.DateUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckCallableControllerTest extends CommonControllerTest {

	@Resource
	private CallOperatorHelper callOperatorHelper;

	@Resource
	private OperatorDAO operatorDAO;

	@Test
	public void test主叫用户剩余分钟数不足() throws Exception {
		String afid = "a1010666";
		if (!operatorDAO.hasOperator(afid)) {
			callOperatorHelper.addOperatorToDB(afid, 0, 0, 0, 23, 7);
		}

		Map<String, Object> params = makeParam();
		params.put("callerId", afid);
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == -1);
	}

	@Test
	public void test打给普通用户正确返回可打电话() throws Exception {
		Map<String, Object> params = makeParam();
		params.put("receiverId", "a1010777");
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == 0);
		Assert.assertTrue(response.getLeftMinutes() >= 0);
	}

	@Test
	public void test正确返回可打电话() throws Exception {
		Map<String, Object> params = makeParam();
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == 0);
		Assert.assertTrue(response.getLeftMinutes() >= 0);
	}

	//-4主叫用户正在通话列表
	@Test
	public void test主叫用户正在通话列表() throws Exception {
		把主叫用户加入正在通话列表();//主叫用户正在通话列表
		Thread.sleep(3000);

		Map<String, Object> params = makeParam();
		params.put("callerId", "a1010701");
		params.put("receiverId", "a1010110");
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == -4);

		挂断电话();//挂断电话
		Thread.sleep(3000);
	}

	//主叫用户正在通话列表
	public void 把主叫用户加入正在通话列表() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" +callId+ ",\"params\":{\"CallAnswerTime\":\"1466499513\",\"CallAppId\":\"1\",\"CallId\":\"96108510824380\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499493\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\"},\"status\":\"user ([username:a1010674@100931.cloud.justalk.com]) trying\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" +tid+ "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//正在通话
	@Test
	public void test被呼叫用户正在通话中() throws Exception {
		取消设置免打扰();
		加入正在通话列表();//加入正在通话列表
		Thread.sleep(3000);

		Map<String, Object> params = makeParam();
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == -2);

		挂断电话();//挂断电话
		Thread.sleep(3000);
	}

	//挂断电话
	public void 挂断电话() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1466499513\",\"CallAppId\":\"1\",\"CallId\":\"96108510824380\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499493\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\"},\"status\":\"call released by ([username:a1010701@100931.cloud.justalk.com])\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" +tid+ "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//加入正在通话列表
	public void 加入正在通话列表() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1466499513\",\"CallAppId\":\"1\",\"CallId\":\"96108510824380\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499493\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\"},\"status\":\"user ([username:a1010701@100931.cloud.justalk.com]) trying\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" +tid+ "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	@Test
	public void test参数错误被呼叫用户为空() throws Exception {
		Map<String, Object> params = makeParam();
		params.remove("receiverId");
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse  response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	@Test
	public void test参数错误主叫用户为空() throws Exception {
		Map<String, Object> params = makeParam();
		params.remove("callerId");
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse  response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	//查询是否可通话
	@Test
	public void test参数全为空错误() throws Exception {
		String json = requestContentStringWhere("/checkCallable", new HashMap<>());
		CheckCallableResponse  response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("-3", response.getCode());
	}

	private Map<String, Object> makeParam () {
		Map<String, Object> params = new HashMap<>();
		params.put("callerId", "a1010674");
		params.put("receiverId", "a1010701");
		return params;
	}

	@Test
	public void test没有设置免打扰时() throws Exception {
		Map<String, Object> params = makeParam();
		String time = ("" + System.currentTimeMillis()).substring(6);
		String afid = "a" + time;
		params.put("receiverId", afid);
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse  response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == 0);
		Assert.assertTrue(response.getLeftMinutes() >= 0);
	}

	@Test
	public void test用户在免打扰时间() throws Exception {
		设置免打扰();
		Thread.sleep(3000);

		Map<String, Object> params = makeParam();
		String json = requestContentStringWhere("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("0", response.getCode());
		Assert.assertTrue(response.getResult() == -5);

		取消设置免打扰();
		Thread.sleep(3000);
	}

	public void 设置免打扰() throws Exception {
		Map<String, Object> params = makeParamNotDisturb();
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	public void 取消设置免打扰() throws Exception {
		Map<String, Object> params = makeParamNotDisturb();
		params.put("open", 0);
		String json = requestContentStringWhere("/settingOrUpdateNotDisturb", params);
		SettingNotDisturbResponse response = JSON.parseObject(json, SettingNotDisturbResponse.class);

		Assert.assertEquals("0", response.getCode());
	}

	@Test
	public void test只允许内网访问() throws Exception {
		Map<String, Object> params = makeParam();
		String json = requestContentString("/checkCallable", params);
		CheckCallableResponse response = JSON.parseObject(json, CheckCallableResponse.class);

		Assert.assertEquals("-8404", response.getCode());
	}

	private Map<String, Object> makeParamNotDisturb () {
		Calendar calendar = Calendar.getInstance();
		int nowHour = DateUtil.changeToZeroTimeZone(calendar).get(Calendar.HOUR_OF_DAY);
		Map<String, Object> params = new HashMap<>();
		params.put("afid", "a1010701");
		params.put("open", 1);
		params.put("startTime", nowHour);
		params.put("endTime", 24);
		return params;
	}

}
