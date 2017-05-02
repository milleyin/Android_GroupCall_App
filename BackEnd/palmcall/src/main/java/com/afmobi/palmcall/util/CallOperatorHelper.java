package com.afmobi.palmcall.util;

import com.afmobi.palmcall.dao.CallRecordDAO;
import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.innerApi.api.GetPalmchatProfileApi;
import com.afmobi.palmcall.innerApi.response.GetPalmchatProfileResponse;
import com.afmobi.palmcall.model.CallRecord;
import com.afmobi.palmcall.model.Operator;
import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.service.SourceDownloadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Repository
public class CallOperatorHelper {

	@Resource
	private OperatorDAO operatorDAO;

	@Resource
	private CallRecordDAO callRecordDAO;

	@Resource
	private GetPalmchatProfileApi getPalmchatProfileApi;

	@Resource
	private SourceDownloadService sourceDownloadService;

	/*
	<logicInfo>
		1:验证是否在免打扰时间范围内<br/>
		2:约定：客户端设置免打扰时间，传过来的是0时区的时间,保存到数据库也是0时区<br/>
		3:获取系统时间，转为0时区时间后再去比较<br/>
	</logicInfo>
	 */

	//验证是否在免打扰时间范围内
	public boolean isNotDisturb(Optional<Operator> not) {
		//如果数据库存在  且 open=1时
		if (not.isPresent() && not.get().getOpen() == 1) {
			int startTime = not.get().getStarttime();
			int endTime = not.get().getEndtime();
			Calendar calendar = DateUtil.localTimeToZeroTimeZone();
			int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
			//当startTime大于endTime时，表示跨天,
			//比如20点到8点不能打扰 反过来8点到20点之间的可以打扰
			if (startTime > endTime) {
				if (nowHour > endTime && nowHour < startTime) {
					return false; //可以打扰
				} else {
					return true; //不可以打扰
				}
			} else {
				if (nowHour >= startTime && nowHour <= endTime) {
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	//保存用户到数据库
	public long addOperatorToDB(String afid, int amount, int leftMinutes,
								int open, int startTime, int endTime) {
		Operator o = new Operator();
		o.setAfid(afid);
		o.setAmount(amount);//初始接听次数
		o.setCallnumber(0);//被呼叫次数
		o.setLeftminutes(leftMinutes);//剩余分钟数

		o.setOpen(open); //免打扰开1 关0
		o.setStarttime(startTime); //免打扰开始时间，小时
		o.setEndtime(endTime); //免打扰结束时间，小时

		String coverImgPath = "";
		String sex = "F";
		String birthdate = "1992-02-28";
		String name = "";
		try {
			GetPalmchatProfileResponse response = getPalmchatProfileApi.sent(afid);
			if (StringUtils.isEmpty(response.getCode())) {
				sex = response.getSex();
				birthdate = response.getBirthdate();
				name = response.getName();

				coverImgPath = fixLocalImgPathToName(response.getLocalImgPath());
				//下载图片到本地
				sourceDownloadService.downloadMidAvatar(response.getLocalImgPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (StringUtils.isNotEmpty(coverImgPath)) {
			o.setCoverTs(System.currentTimeMillis() + "");
		}
		o.setCoverImgPath(coverImgPath);//封面图片路径
		o.setSex(sex);
		o.setBirthdate(birthdate);
		o.setName(name);

		o.setAddtime(new Date());
		return operatorDAO.addOperator(o);
	}

	private String fixLocalImgPathToName(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		String[] strs = source.split(",");
		if (strs == null || strs.length < 2) {
			return null;
		}

		String afid = strs[0];

		String imgName = afid + ".jpg";
		return imgName;
	}

	//保存用户分钟数记录到数据库
	public void addCallRecordToDB(String afid, String receiverAfid, int minutes, int status) {
		CallRecord o = new CallRecord();
		o.setAfid(afid);
		o.setMinutes(minutes);
		o.setStatus(status);
		o.setReceiverafid(receiverAfid);
		o.setAddtime(new Date());
		callRecordDAO.addCallRecord(o);
	}

	//加入可接线人员列表
	public CallableItem fixCallable(String afid, Optional<Operator> operator) {
		int amount = 0; //接听次数
		String sex = "F";
		String birthdate = "1992-02-28";
		String name = "";
		String mediaDesc = "0";//是否有语音简介: 0没有, 1有
		int duration = 0; //语音时长
		String audioTs = null;
		String coverTs = null;

		if (operator.isPresent()) {
			amount = operator.get().getAmount();
			duration = operator.get().getAudioDuration();
			sex = operator.get().getSex();
			birthdate = operator.get().getBirthdate();
			name = operator.get().getName();
			audioTs = operator.get().getAudioTs();
			coverTs = operator.get().getCoverTs();

			if (StringUtils.isNotEmpty(operator.get().getAudioPath())) {
				mediaDesc = "1";
			}
		}

		//当前可用沟通类型.(A:代表audio音频; V:代表video视频)
		List<String> callableType = new ArrayList<>();
		callableType.add("A");

		CallableItem callableItem = new CallableItem();
		callableItem.setAfid(afid);
		callableItem.setAnsweringTimes(amount);
		callableItem.setAvailableType(callableType);
		//是否有语音简介: 0没有, 1有
		callableItem.setHasMediaDesc(mediaDesc);
		callableItem.setAudioDuration(duration);
		callableItem.setAudioTs(audioTs);
		callableItem.setCoverTs(coverTs);

		callableItem.setSex(sex);
		callableItem.setName(name);
		callableItem.setAge(BirthdayToAgeUtil.getAgeByBirthday(birthdate));
		return callableItem;
	}
	
}
