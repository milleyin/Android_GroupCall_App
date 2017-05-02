package com.afmobi.palmcall.service;

import com.afmobi.palmcall.dao.RechargePalmCoinFailDAO;
import com.afmobi.palmcall.innerApi.api.GetPalmchatProfileApi;
import com.afmobi.palmcall.innerApi.api.RechargePalmCoinApi;
import com.afmobi.palmcall.innerApi.response.RechargePalmCoinResponse;
import com.afmobi.palmcall.model.RechargePalmCoinFail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RechargePalmCoinService {

	@Resource
	private RechargePalmCoinApi rechargePalmCoinApi;

	@Resource
	private GetPalmchatProfileApi getPalmchatProfileApi;

	@Resource
	private RechargePalmCoinFailDAO rechargePalmCoinFailDAO;

	public String rechargePalmCoin(String afid, int minutes) {
		String phoneCountryCode = null;
		try {
			phoneCountryCode = getPalmchatProfileApi.getPhoneCountryCode(afid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtils.isEmpty(phoneCountryCode)) { //没有绑定手机国家码时，不充值
			return "notPhoneCountryCode";
		}

		int amount = minutesToAmount(minutes, phoneCountryCode);
		if (amount <= 0) {
			return "amountError";
		}

		boolean isSuccess = rechargePalmCoinAmount(afid, amount, phoneCountryCode);
		if (!isSuccess) { //当充值失败时,保存到数据库，让定时器去执行
			RechargePalmCoinFail fail = new RechargePalmCoinFail();
			fail.setAfid(afid);
			fail.setAmount(amount);
			fail.setPhonecountrycode(phoneCountryCode);
			fail.setAddtime(new Date());
			rechargePalmCoinFailDAO.addRechargePalmCoinFail(fail);

			return "fail-" + amount;
		}

		return "success-" + amount;
	}

	public boolean rechargePalmCoinAmount(String afid, int amount, String phoneCountryCode) {
		boolean isSuccess = false;
		try {
			RechargePalmCoinResponse response = rechargePalmCoinApi.sent(afid, amount, phoneCountryCode);
			isSuccess = rechargePalmCoinApi.isRechargeSuccess(response);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isSuccess;
	}

	//分钟数换成多少PalmCoin
	private int minutesToAmount(int minutes, String countryCode) {
		Map<String, Integer> rateMap = new HashMap<>();//分钟数兑换比例 PalmCoin
		rateMap.put("234", 5);//1：5 比例
		rateMap.put("254", 5);//1：5 比例

		int rate = 0;
		try {
			rate = rateMap.get(countryCode);
		} catch (Exception e) {
			//国家码没有对应比例时 报空指针
		}

		int amount = minutes * rate;

		return amount;
	}

}
