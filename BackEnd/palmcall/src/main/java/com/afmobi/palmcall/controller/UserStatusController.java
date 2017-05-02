package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.dao.UserHotDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.innerApi.api.GetPalmchatProfileApi;
import com.afmobi.palmcall.model.Operator;
import com.afmobi.palmcall.model.UserHot;
import com.afmobi.palmcall.outerApi.request.AddOrDelBatchUserHotRequest;
import com.afmobi.palmcall.outerApi.request.UpdateUserStatusRequest;
import com.afmobi.palmcall.outerApi.response.AddOrDelBatchUserHotResponse;
import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.outerApi.response.UpdateUserStatusResponse;
import com.afmobi.palmcall.service.OperatorService;
import com.afmobi.palmcall.util.CallOperatorHelper;
import com.afmobi.palmcall.util.RequestIPUtil;
import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.support.log.LogHelper;
import com.jtool.validator.ParamBeanValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserStatusController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private CallOperatorHelper callOperatorHelper;

    @Resource
    private OperatorService operatorService;

    @Resource
    private UserHotDAO userHotDAO;

    @Resource
    private GetPalmchatProfileApi getPalmchatProfileApi;

    @CodeGenApi(name = "(内)(亮哥)修改水军用户在线状态", docSeq = 11.0, description = "分析长连接日志文件后，调用修改水军用户在线状态")
    @CodeGenRequest(UpdateUserStatusRequest.class)
    @CodeGenResponse(UpdateUserStatusResponse.class)
    @ResponseBody
    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateUserStatus(HttpServletRequest request, UpdateUserStatusRequest updateUserStatusRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("修改水军用户在线状态request：" + JSON.toJSONString(updateUserStatusRequest));
        //检查请求参数
        validateRequestParam(updateUserStatusRequest);

        String afid = updateUserStatusRequest.getAfid();
        LogHelper.setLogUserId(afid);

        CallableItem item = operatorService.getCallableByRedis(afid);
        if (null == item) { //不在可接电话列表里
            Optional<Operator> operator = operatorDAO.getOperator(afid);
            if (operator.isPresent()) {//数据库存在
                //是否在免打扰时间范围内
                if (!callOperatorHelper.isNotDisturb(operator)) {
                    //在线人员  加入可接线人员列表
                    item = callOperatorHelper.fixCallable(afid, operator);
                    operatorService.addCallableByRedis(item);
                }
            }
        }

        UpdateUserStatusResponse updateUserStatusResponse = new UpdateUserStatusResponse();
        updateUserStatusResponse.setCode("0");

        log.debug("修改水军用户在线状态response：" + JSON.toJSONString(updateUserStatusResponse));
        return JSON.toJSONString(updateUserStatusResponse);
    }

    private void validateRequestParam(UpdateUserStatusRequest updateUserStatusRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(updateUserStatusRequest)) {
            throw new ParamException(updateUserStatusRequest.toString());
        }
    }

    @CodeGenApi(name = "(内)(亮哥)批量添加或删除水军用户", docSeq = 12.0, description = "批量添加水军用户，并设置接听次数为1000次,或删除水军用户")
    @CodeGenRequest(AddOrDelBatchUserHotRequest.class)
    @CodeGenResponse(AddOrDelBatchUserHotResponse.class)
    @ResponseBody
    @RequestMapping(value = "/addOrDelBatchUserHot", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String addOrDelBatchUserHot(HttpServletRequest request, AddOrDelBatchUserHotRequest addOrDelBatchUserHotRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("批量添加或删除水军用户request：" + JSON.toJSONString(addOrDelBatchUserHotRequest));
        //检查请求参数
        validateAddOrDelBatchUserHotRequestParam(addOrDelBatchUserHotRequest);

        int amount = addOrDelBatchUserHotRequest.getAmount();
        String afids = addOrDelBatchUserHotRequest.getAfids();
        LogHelper.setLogUserId(afids);
        String[] ids = afids.split(",");
        String type = addOrDelBatchUserHotRequest.getType();

        List<String> afidList = new ArrayList<>();
        for (String afid : ids) {
            if ("1".equals(type)) {
                if (!userHotDAO.hasUserHot(afid)) { //数据库不存在时
                    String phoneCountryCode = null;
                    try {
                        phoneCountryCode = getPalmchatProfileApi.getPhoneCountryCode(afid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (StringUtils.isEmpty(phoneCountryCode)) { //没有绑定手机国家码时,不添加为水军
                        afidList.add(afid + "==没有绑定手机号");
                        continue;
                    }

                    UserHot userHot = new UserHot();
                    userHot.setAfid(afid);
                    userHot.setAddtime(new Date());
                    userHotDAO.addUserHot(userHot); //增加水军用户

                    operatorDAO.initAmountAndCallnumber(afid, amount, 0); //初始化接听次数 被呼叫次数
                } else {
                    afidList.add(afid + "==数据库已存在");
                }
            } else {
                userHotDAO.deleteUserHot(afid);
            }
        }

        AddOrDelBatchUserHotResponse addOrDelBatchUserHotResponse = new AddOrDelBatchUserHotResponse();
        addOrDelBatchUserHotResponse.setCode("0");
        addOrDelBatchUserHotResponse.setAfidList(afidList);

        log.debug("批量添加或删除水军用户response：" + JSON.toJSONString(addOrDelBatchUserHotResponse));
        return JSON.toJSONString(addOrDelBatchUserHotResponse);
    }

    private void validateAddOrDelBatchUserHotRequestParam(AddOrDelBatchUserHotRequest addOrDelBatchUserHotRequest)
            throws ParamException {
        if (ParamBeanValidator.isNotValid(addOrDelBatchUserHotRequest)) {
            throw new ParamException(addOrDelBatchUserHotRequest.toString());
        }
    }

}
