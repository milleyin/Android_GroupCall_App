package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.outerApi.request.GetLeftMinutesRequest;
import com.afmobi.palmcall.outerApi.request.RechargeLeftMinutesRequest;
import com.afmobi.palmcall.outerApi.response.GetLeftMinutesResponse;
import com.afmobi.palmcall.outerApi.response.RechargeLeftMinutesResponse;
import com.afmobi.palmcall.service.MinutesService;
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

@Controller
public class MinutesController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MinutesService minutesService;

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private CallOperatorHelper callOperatorHelper;

    @CodeGenApi(name = "(内)查询剩余分钟数", docSeq = 3.0, description = "查询剩余分钟数")
    @CodeGenRequest(GetLeftMinutesRequest.class)
    @CodeGenResponse(GetLeftMinutesResponse.class)
    @ResponseBody
    @RequestMapping(value = "/getLeftMinutes", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getLeftMinutes(HttpServletRequest request, GetLeftMinutesRequest getLeftMinutesRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("查询剩余分钟数request：" + JSON.toJSONString(getLeftMinutesRequest));
        //检查请求参数
        validateRequestParam(getLeftMinutesRequest);

        String afid = getLeftMinutesRequest.getAfid();
        LogHelper.setLogUserId(afid);
        int minutes = minutesService.getLeftMinutesByRedis(afid);

        GetLeftMinutesResponse cetLeftMinutesResponse = new GetLeftMinutesResponse();
        cetLeftMinutesResponse.setCode("0");
        cetLeftMinutesResponse.setLeftMinutes(minutes);

        log.debug("查询剩余分钟数response：" + JSON.toJSONString(cetLeftMinutesResponse));
        return JSON.toJSONString(cetLeftMinutesResponse);
    }

    private void validateRequestParam(GetLeftMinutesRequest getLeftMinutesRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(getLeftMinutesRequest)) {
            throw new ParamException(getLeftMinutesRequest.toString());
        }
    }

    /*
	<logicInfo>
		1:充值分钟数 当afid不存在时，保存到数据库 并初始化给 0+充值分钟数<br/>
		2:afid存在时，直接增加充值分钟数<br/>
		3:当充值失败时，保存到数据库，让定时器去处理<br/>
	</logicInfo>
	 */

    @CodeGenApi(name = "(内)(波哥)充值分钟数", docSeq = 4.0, description = "波哥调用充值分钟数")
    @CodeGenRequest(RechargeLeftMinutesRequest.class)
    @CodeGenResponse(RechargeLeftMinutesResponse.class)
    @ResponseBody
    @RequestMapping(value = "/rechargeLeftMinutes", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String rechargeLeftMinutes(HttpServletRequest request, RechargeLeftMinutesRequest rechargeLeftMinutesRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("充值分钟数request：" + JSON.toJSONString(rechargeLeftMinutesRequest));
        //检查请求参数
        validateRechargeRequestParam(rechargeLeftMinutesRequest);
        String afid = rechargeLeftMinutesRequest.getAfid();
        LogHelper.setLogUserId(afid);
        int minutes = rechargeLeftMinutesRequest.getMinutes();

        if (operatorDAO.hasOperator(afid)) { //用户存在时 增加剩余分钟数
            int add = operatorDAO.addLeftminutes(afid, minutes);
            if (add != 1) { //增加失败 抛异常
                throw new BackEndException();
            }
        } else {
            long add = callOperatorHelper.addOperatorToDB(afid, 0, 0 + minutes, 0, 23, 7);
            if (add <= 0) { //插入数据库时 返回的PrimaryKeyID 如果<=0 抛异常
                throw new BackEndException();
            }
        }

        minutesService.delLeftMinutesByRedis(afid); //清除redis缓存
        callOperatorHelper.addCallRecordToDB(afid, null, minutes, 2);//充值分钟数记录

        RechargeLeftMinutesResponse rechargeLeftMinutesResponse = new RechargeLeftMinutesResponse();
        rechargeLeftMinutesResponse.setCode("0");

        log.debug("充值分钟数response：" + JSON.toJSONString(rechargeLeftMinutesResponse));
        return JSON.toJSONString(rechargeLeftMinutesResponse);
    }

    private void validateRechargeRequestParam(RechargeLeftMinutesRequest rechargeLeftMinutesRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(rechargeLeftMinutesRequest)) {
            throw new ParamException(rechargeLeftMinutesRequest.toString());
        }
    }

}
