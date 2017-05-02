package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.CallRecordDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.model.CallRecord;
import com.afmobi.palmcall.outerApi.request.CallRecordRequest;
import com.afmobi.palmcall.outerApi.response.CallRecordResponse;
import com.afmobi.palmcall.outerApi.response.Record;
import com.afmobi.palmcall.util.RequestIPUtil;
import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.support.log.LogHelper;
import com.jtool.validator.ParamBeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CallRecordController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CallRecordDAO callRecordDAO;

    @CodeGenApi(name = "(内)查询通话记录", docSeq = 5.0, description = "根据afid查询通话记录")
    @CodeGenRequest(CallRecordRequest.class)
    @CodeGenResponse(CallRecordResponse.class)
    @ResponseBody
    @RequestMapping(value = "/callRecord", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String callRecord(HttpServletRequest request, CallRecordRequest callRecordRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("查询通话记录request：" + JSON.toJSONString(callRecordRequest));
        //检查请求参数
        validateRequestParam(callRecordRequest);
        String afid = callRecordRequest.getAfid();
        LogHelper.setLogUserId(afid);
        int status = callRecordRequest.getStatus();
        int start = callRecordRequest.getStart();
        int limit = callRecordRequest.getLimit();

        List<Record> records = new ArrayList<>();
        List<CallRecord> list = callRecordDAO.selectCallRecordList(afid, start, limit, status);
        for (CallRecord call : list) {
            Record record = new Record();
            BeanUtils.copyProperties(call, record);
            records.add(record);
        }

        CallRecordResponse callRecordResponse = new CallRecordResponse();
        callRecordResponse.setCode("0");
        callRecordResponse.setRecords(records);

        log.debug("查询通话记录response：" + JSON.toJSONString(callRecordResponse));
        return JSON.toJSONString(callRecordResponse);
    }

    private void validateRequestParam(CallRecordRequest callRecordRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(callRecordRequest)) {
            throw new ParamException(callRecordRequest.toString());
        }
    }

}
