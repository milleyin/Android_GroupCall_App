package com.afmobi.palmcall.service;

import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class OperatorService {

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	private static final String callableMap = "callableMap";

	private static final String callableMapTemp = "callableMapTemp";

	private static final String nowCallAfid = "nowCallAfid_";

	private static final String callId = "callId_";

	private static final Lock lock = new ReentrantLock();

	//------------------------------可接线人员列表---------------------------------------
	public void addCallableByRedis(CallableItem callable) {
		if (callable == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.opsForHash().put(callableMap, callable.getAfid(), JSON.toJSONString(callable));
		} finally {
			lock.unlock();
		}
	}

	public void delCallableByRedis(String afid) {
		if (afid == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.opsForHash().delete(callableMap, afid);
		} finally {
			lock.unlock();
		}
	}

	public CallableItem getCallableByRedis(String afid) {
		if (afid == null) {
			return null;
		}

		lock.lock();
		try {
			CallableItem item = null;
			String json = (String)redisTemplate.opsForHash().get(callableMap, afid);
			if (null != json) {
				item = JSON.parseObject(json, CallableItem.class);
			}

			return item;
		} finally {
			lock.unlock();
		}
	}

	public List<CallableItem> getCallableAllByRedis() {
		lock.lock();
		try {
			List<CallableItem> callableItems = new ArrayList<>();
			Map<Object, Object> jsonMap = redisTemplate.opsForHash().entries(callableMap);
			for (Object afid : jsonMap.keySet()) {
				String json = (String)jsonMap.get(afid);
				CallableItem item = JSON.parseObject(json, CallableItem.class);
				callableItems.add(item);
			}

			return callableItems;
		} finally {
			lock.unlock();
		}
	}

	//------------------------------临时 可接线人员列表---------------------------------------
	public void addCallableTempByRedis(CallableItem callable) {
		if (callable == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.opsForHash().put(callableMapTemp, callable.getAfid(), JSON.toJSONString(callable));
		} finally {
			lock.unlock();
		}
	}

	public void renameMapByRedis() {
		lock.lock();
		try {
			redisTemplate.rename(callableMapTemp, callableMap);
			redisTemplate.delete(callableMapTemp);
		} finally {
			lock.unlock();
		}
	}

	//------------------------------正在通话人员列表---------------------------------------
	public void addNowCallByRedis(CallableItem callable) {
		if (callable == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.opsForValue().set(nowCallAfid + callable.getAfid(),
					JSON.toJSONString(callable), 1, TimeUnit.DAYS);
		} finally {
			lock.unlock();
		}
	}

	public void delNowCallByRedis(String afid) {
		if (afid == null) {
			return;
		}

		lock.lock();
		try {
			redisTemplate.delete(nowCallAfid + afid);
		} finally {
			lock.unlock();
		}
	}

	public CallableItem getNowCallByRedis(String afid) {
		if (afid == null) {
			return null;
		}

		lock.lock();
		try {
			CallableItem item = null;
			String json = redisTemplate.opsForValue().get(nowCallAfid + afid);
			if (null != json) {
				item = JSON.parseObject(json, CallableItem.class);
			}

			return item;
		} finally {
			lock.unlock();
		}
	}

	//--------------保存菊风服务器一次通话结束回调的CallID----回调不是按顺序通知的-------------
	public void addCallIdByRedis(long callid) {
		lock.lock();
		try {
			redisTemplate.opsForValue().set(callId + callid, "1", 2, TimeUnit.HOURS);
		} finally {
			lock.unlock();
		}
	}

	public boolean isCallIdExistByRedis(long callid) {
		lock.lock();
		try {
			String idString = redisTemplate.opsForValue().get(callId + callid);
			if (null != idString) {
				return true;
			}

			return false;
		} finally {
			lock.unlock();
		}
	}

}
