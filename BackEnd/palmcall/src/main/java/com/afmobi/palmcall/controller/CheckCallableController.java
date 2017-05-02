package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.log.write.CallUploadLogService;
import com.afmobi.palmcall.model.Operator;
import com.afmobi.palmcall.outerApi.request.CheckCallableRequest;
import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.outerApi.response.CheckCallableResponse;
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
import java.io.IOException;
import java.util.Optional;

@Controller
public class CheckCallableController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperatorService operatorService;

    @Resource
    private CallOperatorHelper callOperatorHelper;

    @Resource
    private MinutesService minutesService;

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private CallUploadLogService callUploadLogService;

    /*
	<logicInfo>
		1:用户A打电话给用户B,先查询A的剩余分钟数，当用户A <=0分钟时，返回-1欠费,否则返回剩余分钟数<br/>
		2:查询B用户是否设置免打扰时间，如果在时间范围内返回-5免打扰<br/>
		4:查询B用户是否在正在通话列表中，如果在返回-2正在通话中<br/>
		5:然后去redis那边查询B用户是否在线，如果在线设置0可以打电话，不在线也不做处理，让客户端问菊风服务器在不在线<br/>
		6:检查A用户正在通话列表中时，返回-4,表示菊风服务器没有回调通知，最坏情况下，用户A一天自动从正在通话列表过期<br/>
	</logicInfo>
	 */

    @CodeGenApi(name = "(内)查询是否可通话", docSeq = 2.0, description = "查询用户A是否可以打电话给用户B")
    @CodeGenRequest(CheckCallableRequest.class)
    @CodeGenResponse(CheckCallableResponse.class)
    @ResponseBody
    @RequestMapping(value = "/checkCallable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String checkCallable(HttpServletRequest request, CheckCallableRequest checkCallableRequest)
            throws IOException, ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("查询是否可通话request：" + JSON.toJSONString(checkCallableRequest));
        //检查请求参数
        validateRequestParam(checkCallableRequest);

        String callerAfid = checkCallableRequest.getCallerId();
        LogHelper.setLogUserId(callerAfid);
        String receiverAfid = checkCallableRequest.getReceiverId();

        CheckCallableResponse checkCallableResponse = new CheckCallableResponse();
        checkCallableResponse.setCode("0");

        //callerAfid 剩余分钟数
        int minutes = minutesService.getLeftMinutesByRedis(callerAfid);
        if (minutes <= 0) { //剩余分钟数<=0时 表示欠费
            checkCallableResponse.setResult(-1);
            checkCallableResponse.setLeftMinutes(0);

            callUploadLogService.writeLog(callerAfid, receiverAfid, null, 4, 0, null); //记录日志
            log.debug("查询是否可通话response：" + JSON.toJSONString(checkCallableResponse));
            return JSON.toJSONString(checkCallableResponse);
        }

        checkCallableResponse.setLeftMinutes(minutes); //返回剩余分钟数

        //receiverAfid 是否设置免打扰
        Optional<Operator> operator = operatorDAO.getOperator(receiverAfid);
        if (callOperatorHelper.isNotDisturb(operator)){
            checkCallableResponse.setResult(-5); //免打扰

            callUploadLogService.writeLog(callerAfid, receiverAfid, null, 6, 0, null); //记录日志
            log.debug("查询是否可通话response：" + JSON.toJSONString(checkCallableResponse));
            return JSON.toJSONString(checkCallableResponse);
        }

        //receiverAfid 判断是否正在通话列表
        CallableItem receiverItem = operatorService.getNowCallByRedis(receiverAfid);
        if (null == receiverItem) { //不在通话列表
            checkCallableResponse.setResult(0);
        } else {
            checkCallableResponse.setResult(-2); //正在通话中

            callUploadLogService.writeLog(callerAfid, receiverAfid, null, 5, 0, null); //记录日志
            log.debug("查询是否可通话response：" + JSON.toJSONString(checkCallableResponse));
            return JSON.toJSONString(checkCallableResponse);
        }

        //检查callerId如果在正在通话列表时，表示菊风没有回调通知
        CallableItem callerItem = operatorService.getNowCallByRedis(callerAfid);
        if (null != callerItem) { //在通话列表
            checkCallableResponse.setResult(-4);

            callUploadLogService.writeLog(callerAfid, receiverAfid, null, 7, 0, null); //记录日志
        }

        log.debug("查询是否可通话response：" + JSON.toJSONString(checkCallableResponse));
        return JSON.toJSONString(checkCallableResponse);
    }

    private void validateRequestParam(CheckCallableRequest checkCallableRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(checkCallableRequest)) {
            throw new ParamException(checkCallableRequest.toString());
        }
    }

}
