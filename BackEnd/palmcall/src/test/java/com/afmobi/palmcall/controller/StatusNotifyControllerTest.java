package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.NowCallUserDAO;
import com.afmobi.palmcall.innerApi.response.StatusNotifyResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

public class StatusNotifyControllerTest extends CommonControllerTest {

	@Resource
	private NowCallUserDAO nowCallUserDAO;

	//--------------------------------Android 打的--------------------
	//A打电话给B  菊风服务器通知A用户了 joined
	@Test
	public void test菊风回调A打B通知A创建通话ByAndroid() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1466499513\",\"CallAppId\":\"1\",\"CallId\":\"96108510824380\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499493\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\"},\"status\":\"user ([username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]) joined\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" +tid+ "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//菊风服务器通知B用户了 trying
	@Test
	public void test菊风回调A打B通知B有人呼叫他ByAndroid() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"0\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499877\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"user ([username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]) trying\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" +tid+ "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());

		Thread.sleep(3000);
		Assert.assertTrue(nowCallUserDAO.select().count() > 0);
	}

	//B用户接通电话了 菊风服务器通知B用户了 joined
	@Test
	public void test菊风回调A打B当B接通时通知B加入通话ByAndroid() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"0\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499877\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"user ([username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]) joined\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//通话结束 菊风服务器通知 call released by
	@Test
	public void test菊风回调A打B当B挂断时通知通话结束由B中断ByAndroid() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1468207287\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1468207255\",\"CallTalkingDuration\":\"13\",\"CalleeAccountId\":\"[username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:638e2142b1000101067458b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"call released by ([username:638e2142b1000101070158b185c488a0e8f14360b401830fa930a12@100931.cloud.justalk.com]) \"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Thread.sleep(3000);
		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());

		Thread.sleep(3000);
		Assert.assertTrue(nowCallUserDAO.select().count() == 0);
	}

	//--------------------------------IOS 打的--------------------
	//A打电话给B  菊风服务器通知A用户了 joined
	@Test
	public void test菊风回调A打B通知A创建通话ByIOS() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1466499513\",\"CallAppId\":\"1\",\"CallId\":\"96108510824380\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499493\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\"},\"status\":\"user ([username:a1010674@100931.cloud.justalk.com]) joined\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" +tid+ "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//菊风服务器通知B用户了 trying
	@Test
	public void test菊风回调A打B通知B有人呼叫他ByIOS() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"0\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499877\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:a1010674@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"user ([username:a1010701@100931.cloud.justalk.com]) trying\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());

	}

	//B用户接通电话了 菊风服务器通知B用户了 joined
	@Test
	public void test菊风回调A打B当B接通时通知B加入通话ByIOS() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"0\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1466499877\",\"CallTalkingDuration\":\"0\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:a1010674@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"user ([username:a1010701@100931.cloud.justalk.com]) joined\"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//通话结束 菊风服务器通知 call released by
	@Test
	public void test菊风回调A打B当B挂断时通知通话结束由B中断ByIOS() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1468207287\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1468207255\",\"CallTalkingDuration\":\"13\",\"CalleeAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:a1010674@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"call released by ([username:a1010674@100931.cloud.justalk.com]) \"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Thread.sleep(3000);
		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());

		Thread.sleep(3000);
		Assert.assertTrue(nowCallUserDAO.select().count() == 0);
	}

	//没有手机国家码
	@Test
	public void test菊风回调A打B当B挂断时扣除A剩余分钟和给B充值没绑定手机时ByIOS() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cmd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1468207287\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1468207255\",\"CallTalkingDuration\":\"13\",\"CalleeAccountId\":\"[username:a1010674@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:a1010701@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"call released by ([username:a1010701@100931.cloud.justalk.com]) \"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Thread.sleep(3000);
		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

	//充值失败时
	@Test
	public void test菊风回调A打B当B挂断时扣除A剩余分钟和给B充值失败时ByIOS() throws Exception {
		long callId = System.currentTimeMillis();
		long tid = System.currentTimeMillis();
		String params = "{\"cd\":\"statusNotify.OpenCall.CallEx\",\"in\":{\"callId\":" + callId + ",\"params\":{\"CallAnswerTime\":\"1468207287\",\"CallAppId\":\"1\",\"CallId\":\"96108535990238\",\"CallMediaType\":\"voice\",\"CallOrignateTime\":\"1468207255\",\"CallTalkingDuration\":\"13\",\"CalleeAccountId\":\"[username:a1010675@100931.cloud.justalk.com]\",\"CallerAccountId\":\"[username:a1010701@100931.cloud.justalk.com]\",\"ReleaseCallBy\":\"[username:a1010701@100931.cloud.justalk.com]\",\"ReleaseCallReason\":\"User Terminate:1002\"},\"status\":\"call released by ([username:a1010701@100931.cloud.justalk.com]) \"},\"oid\":\"OpenCallUser\",\"params\":{},\"tid\":" + tid + "}";
		String json = requestContentStringGet2("/statusNotify", params);
		StatusNotifyResponse response = JSON.parseObject(json, StatusNotifyResponse.class);

		Thread.sleep(3000);
		Assert.assertTrue(response.isRet());
		Assert.assertTrue(tid == response.getTid());
	}

}
