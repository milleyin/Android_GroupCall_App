package com.afmobi.palmcall.log.write;

import com.afmobi.palmcall.log.model.CallUploadLog;
import com.afmobi.palmcall.log.model.UserInfo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CallUploadLogService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserInfoService userInfoService;

    @Async("apiExecutor")
    public void writeLog(String callerId, String receiverId, String afid, int status,
                         int callDuration, String hangUpAfid) {
        CallUploadLog callUploadLog = new CallUploadLog();
        callUploadLog.setCallerId(callerId);
        callUploadLog.setReceiverId(receiverId);
        callUploadLog.setAfid(afid);
        callUploadLog.setStatus(status);
        callUploadLog.setCallDuration(callDuration);
        callUploadLog.setHangUpAfid(hangUpAfid);

        UserInfo userInfo;
        if (status < 4) {
            userInfo = userInfoService.getUserInfoByRedis(afid);
        } else {
            userInfo = userInfoService.getUserInfoByRedis(callerId);
        }

        callUploadLog.setSex(userInfo.getSex());
        callUploadLog.setAge(userInfo.getAge());
        callUploadLog.setCountry(userInfo.getCountry());
        callUploadLog.setPlatform(userInfo.getPlatform());
        callUploadLog.setVersion(userInfo.getVersion());

        callUploadLog.setCurrentTime(System.currentTimeMillis());

        log.info(JSON.toJSONString(callUploadLog));
    }

}
