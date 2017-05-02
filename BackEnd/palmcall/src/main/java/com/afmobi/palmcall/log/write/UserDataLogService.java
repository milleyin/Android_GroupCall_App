package com.afmobi.palmcall.log.write;

import com.afmobi.palmcall.log.model.UserDataLog;
import com.afmobi.palmcall.log.model.UserInfo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserDataLogService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserInfoService userInfoService;

    @Async("apiExecutor")
    public void writeLog(String afid, int status) {
        UserDataLog userDataLog = new UserDataLog();
        userDataLog.setAfid(afid);
        userDataLog.setStatus(status);

        UserInfo userInfo = userInfoService.getUserInfoByRedis(afid);
        userDataLog.setSex(userInfo.getSex());
        userDataLog.setAge(userInfo.getAge());
        userDataLog.setCountry(userInfo.getCountry());
        userDataLog.setPlatform(userInfo.getPlatform());
        userDataLog.setVersion(userInfo.getVersion());

        userDataLog.setCurrentTime(System.currentTimeMillis());

        log.info(JSON.toJSONString(userDataLog));
    }

}
