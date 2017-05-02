package com.afmobi.palmcall.log.write;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.log.model.UserInfo;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class UserInfoService {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Resource
	private OperatorDAO operatorDAO;

	private static final String userInformation = "userInformation_";

	private static final Lock lock = new ReentrantLock();

	//------------------------------保存用户信息---------------------------------------
	@Async("apiExecutor")
	public void addUserInfoToRedis(String afid, String sex, String age, String country,
								   String platform, String version) {
		if (null == afid) {
			return;
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setAfid(afid);
		userInfo.setSex(sex);
		userInfo.setAge(age);
		userInfo.setCountry(country);
		userInfo.setPlatform(platform);
		userInfo.setVersion(version);

		lock.lock();
		try {
			redisTemplate.opsForValue().set(userInformation + afid, JSON.toJSONString(userInfo), 30, TimeUnit.MINUTES);
		} finally {
			lock.unlock();
		}
	}

	public UserInfo getUserInfoByRedis(String afid) {
		if (afid == null) {
			return new UserInfo();
		}

		lock.lock();
		try {
			UserInfo userInfo;
			String json = redisTemplate.opsForValue().get(userInformation + afid);
			if (null != json) {
				userInfo = JSON.parseObject(json, UserInfo.class);
			} else {
				userInfo = new UserInfo();
			}

			return userInfo;
		} finally {
			lock.unlock();
		}
	}

}
