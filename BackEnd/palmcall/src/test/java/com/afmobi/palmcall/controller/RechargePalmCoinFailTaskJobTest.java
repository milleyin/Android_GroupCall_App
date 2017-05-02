package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.task.RechargePalmCoinFailTaskJob;
import org.junit.Test;

import javax.annotation.Resource;

public class RechargePalmCoinFailTaskJobTest extends CommonControllerTest {

	@Resource
	private RechargePalmCoinFailTaskJob rechargePalmCoinFailTaskJob;

	//执行充值失败的定时器
	@Test
	public void test定时器处理充值失败的记录() {
		try {
			rechargePalmCoinFailTaskJob.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 定时器里判断 有没有定时器在执行
	 {
		 boolean isRun = rechargePalmCoinService.isTaskRunByRedis();
		 if (isRun) { //有定时器在执行 则直接返回
		 	return;
		 }

	     //执行一些操作后..........

		 rechargePalmCoinService.delTaskRunByRedis();//删除定时器
	 }

	 //判断有没有定时器在执行，如果有就不执行，用于处理充值PalmCoin失败的情况
	 public boolean isTaskRunByRedis() {
		 lock.lock();
		 try {
			 String flag = redisTemplate.opsForValue().get(taskRechargePalmCoin);
			 if (StringUtils.isNotEmpty(flag)) { //有定时器在执行
			 return true;
		 }

	     //没有就添加一个
		 redisTemplate.opsForValue().set(taskRechargePalmCoin, "true", 1, TimeUnit.HOURS);
		 } finally {
		 	lock.unlock();
		 }

		 return false;
	 }

	 //删除定时器
	 public void delTaskRunByRedis() {
		 lock.lock();
		 try {
		 	redisTemplate.delete(taskRechargePalmCoin);
		 } finally {
		 	lock.unlock();
		 }
	 }

	 */

}
