package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.log.write.CallUploadLogService;
import com.afmobi.palmcall.model.Operator;
import com.afmobi.palmcall.outerApi.request.GetNotDisturbRequest;
import com.afmobi.palmcall.outerApi.request.SettingNotDisturbRequest;
import com.afmobi.palmcall.outerApi.response.GetNotDisturbResponse;
import com.afmobi.palmcall.outerApi.response.SettingNotDisturbResponse;
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
import java.util.Optional;

@Controller
public class NotDisturbController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private CallOperatorHelper callOperatorHelper;

    @Resource
    private CallUploadLogService callUploadLogService;

    /*
	<logicInfo>
		1:设置或修改免打扰时间约定：客户端传过来的是0时区的时间<br/>
		2:获取免打扰时间约定：服务器下发的也是0时区时间，需客户端自己转成对应时区时间<br/>
	</logicInfo>
	 */

    @CodeGenApi(name = "(内)设置或修改免打扰", docSeq = 6.0, description = "当不存在时设置免打扰时间，存在时修改")
    @CodeGenRequest(SettingNotDisturbRequest.class)
    @CodeGenResponse(SettingNotDisturbResponse.class)
    @ResponseBody
    @RequestMapping(value = "/settingOrUpdateNotDisturb", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String settingOrUpdateNotDisturb(HttpServletRequest request, SettingNotDisturbRequest settingNotDisturbRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("免打扰设置request：" + JSON.toJSONString(settingNotDisturbRequest));
        //检查请求参数
        validateRequestParam(settingNotDisturbRequest);
        String afid = settingNotDisturbRequest.getAfid();
        LogHelper.setLogUserId(afid);
        int open = settingNotDisturbRequest.getOpen();
        int startTime = settingNotDisturbRequest.getStartTime();
        int endTime = settingNotDisturbRequest.getEndTime();

        callUploadLogService.writeLog(null, null, afid, open, 0, null); //记录日志

        addOrUpdateNotDisturb(afid, open, startTime, endTime);

        SettingNotDisturbResponse response = new SettingNotDisturbResponse();
        response.setCode("0");

        log.debug("免打扰设置response：" + JSON.toJSONString(response));
        return JSON.toJSONString(response);
    }

    private void validateRequestParam(SettingNotDisturbRequest settingNotDisturbRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(settingNotDisturbRequest)) {
            throw new ParamException(settingNotDisturbRequest.toString());
        }

        if (settingNotDisturbRequest.getStartTime() == settingNotDisturbRequest.getEndTime()) {
            throw new ParamException(settingNotDisturbRequest.toString());
        }
    }

    private void addOrUpdateNotDisturb(String afid, int open, int startTime, int endTime) {
        if (operatorDAO.hasOperator(afid)) { //数据库存在时 修改
            operatorDAO.updateNotDisturb(afid, open, startTime, endTime);
        } else { //剩余分钟数0
            callOperatorHelper.addOperatorToDB(afid, 0, 0, open, startTime, endTime);
        }
    }

    @CodeGenApi(name = "(内)获取免打扰时间", docSeq = 7.0, description = "获取免打扰时间和状态")
    @CodeGenRequest(GetNotDisturbRequest.class)
    @CodeGenResponse(GetNotDisturbResponse.class)
    @ResponseBody
    @RequestMapping(value = "/getNotDisturb", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getNotDisturb(HttpServletRequest request, GetNotDisturbRequest getNotDisturbRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("获取免打扰时间request：" + JSON.toJSONString(getNotDisturbRequest));
        //检查请求参数
        validateGetNotDisturbRequestParam(getNotDisturbRequest);
        String afid = getNotDisturbRequest.getAfid();
        LogHelper.setLogUserId(afid);

        GetNotDisturbResponse response = new GetNotDisturbResponse();
        response.setCode("0");

        Optional<Operator> not = operatorDAO.getOperator(afid);
        if (not.isPresent()) { //数据库存在
            response.setOpen(not.get().getOpen());
            response.setStartTime(not.get().getStarttime());
            response.setEndTime(not.get().getEndtime());
        }

        log.debug("获取免打扰时间response：" + JSON.toJSONString(response));
        return JSON.toJSONString(response);
    }

    private void validateGetNotDisturbRequestParam(GetNotDisturbRequest getNotDisturbRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(getNotDisturbRequest)) {
            throw new ParamException(getNotDisturbRequest.toString());
        }
    }

}
