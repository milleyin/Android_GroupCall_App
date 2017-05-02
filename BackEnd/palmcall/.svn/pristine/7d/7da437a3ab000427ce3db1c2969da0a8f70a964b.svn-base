package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.task.CallOperatorTaskJob;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class CallOperatorTaskJobTest extends CommonControllerTest {

	@Resource
	private CallOperatorTaskJob callOperatorTaskJob;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	//执行定时器
	@Test
	public void test定时器整理可接电话的水军列表() {
		try {
			redisTemplate.opsForValue().set("userHotStatus_" + "a1010701", "1", 15, TimeUnit.MINUTES);//在线

			callOperatorTaskJob.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
