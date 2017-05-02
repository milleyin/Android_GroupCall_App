package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.exception.BackEndException;
import com.afmobi.palmcall.exception.InnerAccessInterfaceException;
import com.afmobi.palmcall.exception.ParamException;
import com.afmobi.palmcall.model.Operator;
import com.afmobi.palmcall.outerApi.request.GetBatchProfileRequest;
import com.afmobi.palmcall.outerApi.request.GetProfileRequest;
import com.afmobi.palmcall.outerApi.response.GetBatchProfileResponse;
import com.afmobi.palmcall.outerApi.response.GetProfileResponse;
import com.afmobi.palmcall.outerApi.response.Profile;
import com.afmobi.palmcall.util.BirthdayToAgeUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperatorDAO operatorDAO;

    @CodeGenApi(name = "(内)批量查询profile信息", docSeq = 10.0, description = "批量查询profile信息")
    @CodeGenRequest(GetBatchProfileRequest.class)
    @CodeGenResponse(GetBatchProfileResponse.class)
    @ResponseBody
    @RequestMapping(value = "/getBatchProfile", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getBatchProfile(HttpServletRequest request, GetBatchProfileRequest getBatchProfileRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("批量查询profile信息request：" + JSON.toJSONString(getBatchProfileRequest));
        //检查请求参数
        validateBatchRequestParam(getBatchProfileRequest);

        String afids = getBatchProfileRequest.getAfids();
        LogHelper.setLogUserId(afids);
        String[] ids = afids.split(",");

        List<Profile> list = new ArrayList<>();
        for (String afid : ids) {
            Profile profile = new Profile();
            profile.setAfid(afid);
            Optional<Operator> operator = operatorDAO.getOperator(afid);
            if (operator.isPresent()) {
                profile.setName(operator.get().getName());
                profile.setSex(operator.get().getSex());
                profile.setAge(BirthdayToAgeUtil.getAgeByBirthday(operator.get().getBirthdate()));
                profile.setAudioDuration(operator.get().getAudioDuration());
                profile.setAnsweringTimes(operator.get().getAmount());
                //当前可用沟通类型.(A:代表audio音频; V:代表video视频)
                List<String> callableType = new ArrayList<>();
                callableType.add("A");
                profile.setAvailableType(callableType);

                profile.setCoverTs(operator.get().getCoverTs());
                profile.setAudioTs(operator.get().getAudioTs());

                list.add(profile);
            }
        }

        GetBatchProfileResponse getBatchProfileResponse = new GetBatchProfileResponse();
        getBatchProfileResponse.setCode("0");
        getBatchProfileResponse.setList(list);

        log.debug("批量查询profile信息response：" + JSON.toJSONString(getBatchProfileResponse));
        return JSON.toJSONString(getBatchProfileResponse);
    }

    private void validateBatchRequestParam(GetBatchProfileRequest getBatchProfileRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(getBatchProfileRequest)) {
            throw new ParamException(getBatchProfileRequest.toString());
        }
    }

    @CodeGenApi(name = "(内)查询profile信息", docSeq = 9.0, description = "查询profile信息")
    @CodeGenRequest(GetProfileRequest.class)
    @CodeGenResponse(GetProfileResponse.class)
    @ResponseBody
    @RequestMapping(value = "/getProfile", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getProfile(HttpServletRequest request, GetProfileRequest getProfileRequest)
            throws ParamException, InnerAccessInterfaceException, BackEndException {
        // 判断是内网还是外网访问
        if (!RequestIPUtil.getWhere(request)) {
            throw new InnerAccessInterfaceException();
        }

        log.debug("查询profile信息request：" + JSON.toJSONString(getProfileRequest));
        //检查请求参数
        validateRequestParam(getProfileRequest);

        String afid = getProfileRequest.getAfid();
        LogHelper.setLogUserId(afid);

        GetProfileResponse getProfileResponse = new GetProfileResponse();
        getProfileResponse.setCode("0");
        getProfileResponse.setAfid(afid);
        Optional<Operator> operator = operatorDAO.getOperator(afid);
        if (operator.isPresent()) {
            getProfileResponse.setName(operator.get().getName());
            getProfileResponse.setSex(operator.get().getSex());
            getProfileResponse.setAge(BirthdayToAgeUtil.getAgeByBirthday(operator.get().getBirthdate()));
            getProfileResponse.setAudioDuration(operator.get().getAudioDuration());
            getProfileResponse.setAnsweringTimes(operator.get().getAmount());
            //当前可用沟通类型.(A:代表audio音频; V:代表video视频)
            List<String> callableType = new ArrayList<>();
            callableType.add("A");
            getProfileResponse.setAvailableType(callableType);

            getProfileResponse.setCoverTs(operator.get().getCoverTs());
            getProfileResponse.setAudioTs(operator.get().getAudioTs());
        } else {
            getProfileResponse.setErrorCode("-1");//afid不存在
        }

        log.debug("查询profile信息response：" + JSON.toJSONString(getProfileResponse));
        return JSON.toJSONString(getProfileResponse);
    }

    private void validateRequestParam(GetProfileRequest getProfileRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(getProfileRequest)) {
            throw new ParamException(getProfileRequest.toString());
        }
    }

}
