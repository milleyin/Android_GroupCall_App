package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.GiftMinutesDAO;
import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.log.write.UserDataLogService;
import com.afmobi.palmcall.log.write.UserInfoService;
import com.afmobi.palmcall.model.GiftMinutes;
import com.afmobi.palmcall.outerApi.request.FetchCallableListRequest;
import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.outerApi.response.FetchCallableListResponse;
import com.afmobi.palmcall.service.MinutesService;
import com.afmobi.palmcall.service.OperatorService;
import com.afmobi.palmcall.util.CallOperatorHelper;
import com.afmobi.palmcall.util.RequestIPUtil;
import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.support.log.LogHelper;
import com.jtool.validator.ParamBeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class FetchCallableListController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperatorService operatorService;

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private GiftMinutesDAO giftMinutesDAO;

    @Resource
    private MinutesService minutesService;

    @Resource
    private CallOperatorHelper callOperatorHelper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserDataLogService userDataLogService;

    /*
	<logicInfo>
		1:查询用户afid不存在时，保存到数据库， 并初始化给 0剩余分钟数<br/>
		2:根据gift=1和countrycode时 查询是否赠送过，没有就根据国家码赠送分钟，有就不在赠送<br/>
		3:返回此用户的剩余分钟数<br/>
		4:每次打乱水军列表顺序<br/>
	</logicInfo>
	 */

    @CodeGenApi(name = "(内)获取可打电话的列表", docSeq = 1.0, description = "获取可打电话的列表")
    @CodeGenRequest(FetchCallableListRequest.class)
    @CodeGenResponse(FetchCallableListResponse.class)
    @ResponseBody
    @RequestMapping(value = "/fetchCallableList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String fetchCallableList(HttpServletRequest request, FetchCallableListRequest fetchCallableListRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("获取可打电话的列表request：" + JSON.toJSONString(fetchCallableListRequest));
        //检查请求参数
        validateRequestParam(fetchCallableListRequest);
        String afid = fetchCallableListRequest.getAfid();
        LogHelper.setLogUserId(afid);
        int gift = fetchCallableListRequest.getGift();
        String countrycode = fetchCallableListRequest.getCountrycode();

        //统计日志用到 保存用户信息到redis
        String sex = fetchCallableListRequest.getSex();
        String age = fetchCallableListRequest.getAge();
        String country = fetchCallableListRequest.getCountry();
        String platform = fetchCallableListRequest.getPlatform();
        String version = fetchCallableListRequest.getVersion();
        userInfoService.addUserInfoToRedis(afid, sex, age, country, platform, version);

        FetchCallableListResponse fetchCallableListResponse = new FetchCallableListResponse();
        fetchCallableListResponse.setCode("0");

        //是否赠送分钟数
        boolean isGiftSuccess = giftMinutes(gift, afid, countrycode);
        if (isGiftSuccess) {
            fetchCallableListResponse.setGiftMinutes(giftMins(countrycode));//返回赠送了多少分钟数
            userDataLogService.writeLog(afid, 1); //记录日志 首次访问
        } else {
            userDataLogService.writeLog(afid, 2); //记录日志 多次访问
        }

        //剩余分钟数
        fetchCallableListResponse.setLeftMinutes(fixLeftMinutes(afid));

        List<CallableItem> callableItemList = operatorService.getCallableAllByRedis();
        CallableItem my = operatorService.getCallableByRedis(afid);
        callableItemList.remove(my); //删除自己

        //打乱顺序
        Collections.shuffle(callableItemList);
        fetchCallableListResponse.setList(callableItemList);

        log.debug("获取可打电话的列表response：" + JSON.toJSONString(fetchCallableListResponse));
        return JSON.toJSONString(fetchCallableListResponse);
    }

    //是否赠送分钟数
    private boolean giftMinutes(int gift, String afid, String countrycode) {
        if (gift == 1) { //gift=1查询是否赠送过
            boolean isGift = giftMinutesDAO.hasGiftMinutes(afid);
            if (!isGift) { //数据库不存在，则赠送分钟数
                long add = 0;
                if (!operatorDAO.hasOperator(afid)) { //数据库不存在此用户
                    //普通用户 初始接听次数 剩余分钟数0 不开启免打扰
                    add = callOperatorHelper.addOperatorToDB(afid, 0, 0 + giftMins(countrycode), 0, 23, 7);//赠送分钟
                } else {
                    add = operatorDAO.addLeftminutes(afid, giftMins(countrycode));//赠送分钟
                }

                if (add > 0) { //赠送成功后 记录下来
                    GiftMinutes o = new GiftMinutes();
                    o.setAfid(afid);
                    o.setAddtime(new Date());
                    giftMinutesDAO.addGiftMinutes(o);
                }

                minutesService.delLeftMinutesByRedis(afid);//清除redis缓存
                callOperatorHelper.addCallRecordToDB(afid, null, giftMins(countrycode), 1);//分钟数记录

                return true;//赠送成功
            }
        }

        return false;
    }

    //根据不同国家码赠送分钟数
    private int giftMins(String countryCode) {
        Map<String, Integer> map = new HashMap<>();
        map.put("00000", 10); //默认时 赠送10分钟
        map.put("234", 10); //赠送10分钟
        map.put("254", 10);

        int mins = 10;
        try {
            mins = map.get(countryCode);
        } catch (Exception e) {
            //国家码没有对应时 报空指针
        }

        return mins;
    }

    //处理剩余分钟数
    private int fixLeftMinutes(String afid) {
        //查询redis的剩余分钟数
        int minutes = minutesService.getLeftMinutesByRedis(afid);
        if (minutes > 0) {
            return minutes;
        }

        //如果剩余分钟数 <=0时 去查询数据库有没有记录
        if (!operatorDAO.hasOperator(afid)) { //数据库不存在此用户
            //普通用户初始接听次数 剩余分钟数0 不开启免打扰
            callOperatorHelper.addOperatorToDB(afid, 0, 0, 0, 23, 7);

            minutesService.delLeftMinutesByRedis(afid);//清除redis缓存
            return 0;
        }

        return 0;
    }

    private void validateRequestParam(FetchCallableListRequest fetchCallableListRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(fetchCallableListRequest)) {
            throw new ParamException(fetchCallableListRequest.toString());
        }
    }

}
