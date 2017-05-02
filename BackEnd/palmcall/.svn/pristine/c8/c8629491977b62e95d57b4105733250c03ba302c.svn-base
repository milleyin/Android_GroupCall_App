package com.afmobi.palmcall.service;

import com.afmobi.palmcall.dao.UserHotDAO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class UserHotService {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Resource
	private UserHotDAO userHotDAO;

	private static final String userHotList = "userHotList";

	private static final String userHotStatus = "userHotStatus_";

	private static final Lock lock = new ReentrantLock();

	//------------------------------水军列表---------------------------------------

	public List<String> addUserHotToRedis() {
		List<String> list = userHotDAO.selectUserHotList();
		if (list == null) {
			return new ArrayList<>();
		}

		lock.lock();
		try {
			redisTemplate.opsForValue().set(userHotList, JSON.toJSONString(list), 10, TimeUnit.MINUTES);
			return list;

		} finally {
			lock.unlock();
		}
	}

	public List<String> getUserHotAllByRedis() {
		lock.lock();
		try {
			List<String> userHots;
			String json = redisTemplate.opsForValue().get(userHotList);
			if (null != json) {
				userHots = JSON.parseObject(json, new TypeReference<List<String>>(){});
			} else {
				userHots = addUserHotToRedis();
			}

			return userHots;
		} finally {
			lock.unlock();
		}
	}

	//------------------------------水军的状态---------------------------------------

	public String getUserHotStatusByRedis(String afid) {
		if (afid == null) {
			return null;
		}

		lock.lock();
		try {
			return redisTemplate.opsForValue().get(userHotStatus + afid);
		} finally {
			lock.unlock();
		}
	}

	public boolean isOnlineByRedis(String afid) {
		return "1".equals(getUserHotStatusByRedis(afid)) ? true : false;
	}

}
