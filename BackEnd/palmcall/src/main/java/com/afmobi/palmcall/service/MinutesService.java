package com.afmobi.palmcall.service;

import com.afmobi.palmcall.dao.OperatorDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class MinutesService {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Resource
	private OperatorDAO operatorDAO;

	private static final String leftMinutes = "leftMinutes_";

	private static final Lock lock = new ReentrantLock();

	//------------------------------保存剩余分钟数，方便频繁获取---------------------------------------
	public void addLeftMinutesByRedis(String afid, int minutes) {
		if (afid == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.opsForValue().set(leftMinutes + afid, Integer.toString(minutes), 10, TimeUnit.MINUTES);
		} finally {
			lock.unlock();
		}
	}

	public int getLeftMinutesByRedis(String afid) {
		if (afid == null) {
			return 0;
		}

		lock.lock();
		try {
			int min = 0;
			String minutes = redisTemplate.opsForValue().get(leftMinutes + afid);

			if (StringUtils.isEmpty(minutes)) {//redis不存在时 查询数据库，每次剩余分钟数变更都清除redis缓存
				min = operatorDAO.getLeftMinutes(afid);//查询数据库

				addLeftMinutesByRedis(afid, min); //保存到redis
			} else {
				min = Integer.parseInt(minutes);
			}

			return min;
		} finally {
			lock.unlock();
		}
	}

	public void delLeftMinutesByRedis(String afid) {
		if (afid == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.delete(leftMinutes + afid);
		} finally {
			lock.unlock();
		}
	}

}
