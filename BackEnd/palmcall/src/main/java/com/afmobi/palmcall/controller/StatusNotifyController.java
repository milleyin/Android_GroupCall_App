package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.NowCallUserDAO;
import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.innerApi.api.PushPalmcallMsgApi;
import com.afmobi.palmcall.innerApi.request.StatusNotifyRequest;
import com.afmobi.palmcall.innerApi.response.StatusNotifyResponse;
import com.afmobi.palmcall.log.write.CallUploadLogService;
import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.service.MinutesService;
import com.afmobi.palmcall.service.OperatorService;
import com.afmobi.palmcall.service.RechargePalmCoinService;
import com.afmobi.palmcall.util.CallOperatorHelper;
import com.alibaba.fastjson.JSON;
import com.jtool.support.log.LogHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
public class StatusNotifyController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperatorService operatorService;

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private PushPalmcallMsgApi pushPalmcallMsgApi;

    @Resource
    private CallOperatorHelper callOperatorHelper;

    @Resource
    private MinutesService minutesService;

    @Resource
    private RechargePalmCoinService rechargePalmCoinService;

    @Resource
    private NowCallUserDAO nowCallUserDAO;

    @Resource
    private CallUploadLogService callUploadLogService;

    /*
	<logicInfo>
		1:菊风服务器回调后处理可通话列表 和 正在通话列表<br/>
		2:被叫用户累计接电话次数+1, 不管接没接通  被呼叫的人呼叫次数+1<br/>
		3:当通话时长>0推送PalmCall消息给主被叫用户<br/>
		4:扣除主叫用户的剩余分钟数<br/>
		5:菊风服务器回调通知种类：<br/>
            A打B时：<br/>
            回调A joined, 回调B trying<br/>
            当B没接通 A直接挂掉时：回调A call released by<br/>
            当B没接通 B直接挂掉时：回调B call released by<br/>
            当B接通时   回调B joined<br/>
 	        接通后当A挂掉时   回调A call released by<br/>
	        接通后当B挂掉时   回调B call released by<br/>
            超时无人接听 call released by (CallServer)<br/>
            不在线时  call released by (CallServer)<br/>
        6:给被叫用户通话分钟数充值成PalmCoin,当被叫用户没有绑定手机号时不做任何处理<br/>
        7:通话结束时，把CallID保存到redis,因为菊风回调顺序不定：<br/>
            可能先回调挂断  在回调创建通话 导致用户又放回正在通话列表，一直不能打电话<br/>
            CallID表示一次通话，CallID回调挂断后保存到redis,同一CallID还有joined和trying时就不管<br/>
	</logicInfo>
	 */

    //菊风服务器回调
    @RequestMapping(value = "/statusNotify")
    @ResponseBody
    public String statusNotify(HttpServletRequest httpServletRequest) throws IOException {

        log.debug("请求到达statusNotify");

        log.debug("Authorization header:" + httpServletRequest.getHeader("Authorization"));

        String json = IOUtils.toString(httpServletRequest.getInputStream(), "UTF-8");

        StatusNotifyRequest request = JSON.parseObject(json, StatusNotifyRequest.class);

        log.debug("statusMotifyRequest:" + JSON.toJSONString(request));

        callableBack(request);

        StatusNotifyResponse response = new StatusNotifyResponse();
        response.setTid(request.getTid());
        response.setRet(true);

        log.debug("返回给菊风服务器Response:" + JSON.toJSONString(response));

        return JSON.toJSONString(response);
    }

    //菊风服务器回调后的处理
    private void callableBack(StatusNotifyRequest request) {
        String callerId = request.getIn().getParams().getCallerAccountId(); //主叫用户的 palmcall ID
        String callerAfid = fixAfid(callerId);
        LogHelper.setLogUserId(callerAfid);
        log.debug("主叫用户:callerId:" + callerId + "==afid:" + callerAfid);

        String receiverId = request.getIn().getParams().getCalleeAccountId(); //被叫用户的 palmcall ID
        String receiverAfid = fixAfid(receiverId);
        log.debug("被叫用户:receiverId:" + receiverId + "==afid:" + receiverAfid);

        String status = request.getIn().getStatus();
        String statusAfid = fixAfid(status);
        log.debug("通话状态:" + status + "==afid:" + statusAfid);

        String callSecond = request.getIn().getParams().getCallTalkingDuration();
        fixCallRequest(status, statusAfid, callerAfid, receiverAfid, request.getIn().getCallId(), callSecond);

        //推送PalmCall消息 和 扣除分钟数
        if (!"0".equals(callSecond)) { //通话时长为0秒时 不推送消息  也不扣除分钟数
            //扣除分钟数
            int minutes = fixMinutes(Integer.parseInt(callSecond));
            int reduce = operatorDAO.reduceLeftminutes(callerAfid, minutes);//扣除主叫用户剩余分钟数
            minutesService.delLeftMinutesByRedis(callerAfid);//清除redis缓存
            String rechargeFlag = null;
            if (reduce == 1) { //扣除主叫用户剩余分钟数 成功
                callOperatorHelper.addCallRecordToDB(callerAfid, receiverAfid, minutes, 0);//消费分钟数记录
                //给被叫用户通话分钟数 充值成PalmCoin
                log.debug("给用户充值PalmCoin:" + receiverAfid + "==minutes:" + minutes);
                rechargeFlag = rechargePalmCoinService.rechargePalmCoin(receiverAfid, minutes);
            }

            //推送PalmCall消息
            sendPalmCallMessage(callerAfid, receiverAfid, request, rechargeFlag);
        }

    }

    //处理通话请求 正在通话 和 可接线人员列表
    private void fixCallRequest(String status, String statusAfid, String callerAfid,
                                String receiverAfid, long callId, String callSecond) {
        CallableItem callerItem;
        CallableItem receiverItem;

        if (status.contains("joined")) {
            if (statusAfid.equals(callerAfid)) { //加入的是主叫用户，表示主叫用户开始去呼叫别人
                log.debug("创建呼叫通话----------" + statusAfid);

                callUploadLogService.writeLog(callerAfid, receiverAfid, null, 8, 0, null); //记录日志

                /*
                出现不回调问题,创建呼叫时，A joined回调了，B trying没回调，之后call released by也没回调取消
                导致A用户保存到正在通话列表，一直出不来。改进:trying回调时，把A和B保存到正在通话列表
                */

                return;
            }
            if (statusAfid.equals(receiverAfid)) { //加入的是被叫用户，表示通话开始了
                log.debug("开始通话了----------" + statusAfid);

                callUploadLogService.writeLog(callerAfid, receiverAfid, null, 10, 0, null); //记录日志

                //被叫用户 累计接电话次数+1
                operatorDAO.updateAmount(receiverAfid, 1);

                if (!isJoinNowCall(callId)) {
                    return;
                }

                receiverItem = operatorService.getCallableByRedis(receiverAfid);
                //加入到通话中列表  被叫加入
                operatorService.addNowCallByRedis(receiverItem);
                //将被叫人员剔除可打电话列表
                operatorService.delCallableByRedis(receiverAfid);

                return;
            }

        }

        if (status.contains("trying")) {
            log.debug("有人呼叫我了----------" + statusAfid);

            callUploadLogService.writeLog(callerAfid, receiverAfid, null, 9, 0, null); //记录日志

            //被呼叫的人 次数+1
            operatorDAO.updateCallnumber(receiverAfid, 1);

            if (!isJoinNowCall(callId)) {
                return;
            }

            callerItem = operatorService.getCallableByRedis(callerAfid);
            //加入到通话中列表  主叫加入
            operatorService.addNowCallByRedis(callerItem);
            //将主叫人员剔除可打电话列表
            operatorService.delCallableByRedis(callerAfid);

            //加入到通话中列表的用户，主叫用户 保存到数据库，通话结束后删除
            nowCallUserDAO.addNowCallUser(callerAfid);

            if (!isJoinNowCall(callId)) {
                return;
            }

            receiverItem = operatorService.getCallableByRedis(receiverAfid);
            //加入到通话中列表  被叫加入
            operatorService.addNowCallByRedis(receiverItem);
            //将被叫人员剔除可打电话列表
            operatorService.delCallableByRedis(receiverAfid);

            //加入到通话中列表的用户，被叫用户 保存到数据库，通话结束后删除
            nowCallUserDAO.addNowCallUser(receiverAfid);

            return;
        }

        if (status.contains("call released by") || status.contains("left")) {
            log.debug("通话结束了----------中断用户:" + statusAfid);

            if (!"0".equals(callSecond)) { //通话时长大于0秒时
                callUploadLogService.writeLog(callerAfid, receiverAfid, null, 11,
                        fixMinutes(Integer.parseInt(callSecond)), statusAfid); //记录日志
            } else {
                callUploadLogService.writeLog(callerAfid, receiverAfid, null, 11,
                        0, statusAfid); //记录日志
            }

            callerItem = operatorService.getNowCallByRedis(callerAfid);
            receiverItem = operatorService.getNowCallByRedis(receiverAfid);
            //删除正在通话中列表  主被叫都删除掉
            operatorService.delNowCallByRedis(callerAfid);
            operatorService.delNowCallByRedis(receiverAfid);

            //把接线员放回可接电话列表
            operatorService.addCallableByRedis(callerItem);
            operatorService.addCallableByRedis(receiverItem);

            //通话结束后  把CallID保存到redis
            operatorService.addCallIdByRedis(callId);

            //删除数据库保存的正在通话用户 主被叫都删除掉
            nowCallUserDAO.deleteNowCallUser(callerAfid);
            nowCallUserDAO.deleteNowCallUser(receiverAfid);

            return;
        }

    }

    //判断 CallID存在表示此通话已经结束了  说明回调顺序不对
    private boolean isJoinNowCall(long callId) {
        boolean isCallId = operatorService.isCallIdExistByRedis(callId);
        if (isCallId) {
            return false;
        }

        return true;//可以加入正在通话列表
    }

    //将通话时长秒转化为分钟数
    private int fixMinutes(int second) {
        int  min = (int)TimeUnit.SECONDS.toMinutes(second);
        int secondTemp = second % 60;
        if (secondTemp > 0) {
            min += 1;
        }

        return min;
    }

    //处理palmcall ID 找到对应afid
    private String fixAfid(String palmcallID) {
        if (!palmcallID.contains("@")) {//当没有@字符时
            return null;
        }
        //用户palmcall ID
        String id = palmcallID.substring(palmcallID.indexOf(":") + 1, palmcallID.indexOf("@"));//截取出id
        String afid;
        if (id.length() <= 10) { //表示IOS用的Palmcall ID就是afid
            afid = id;
        } else {
            String temp = id.substring(10, 20); //android的Palmcall ID从第10位开始截取出10位
            afid = "a" + Long.parseLong(temp);  //去掉前面为0的补位
        }

        return afid;
    }

    //PalmCall 发送通话信息给主叫和被叫用户
    private void sendPalmCallMessage(String callerAfid, String receiverAfid,
                                         StatusNotifyRequest request, String rechargeFlag) {
        String callSecond = request.getIn().getParams().getCallTalkingDuration();
        int minutes = fixMinutes(Integer.parseInt(callSecond));
        String msg = "Your last call was " + minutes + " minutes. ";
        try {
            if (StringUtils.isNotEmpty(callerAfid)) {
                int leftMinutes = minutesService.getLeftMinutesByRedis(callerAfid);
                String callerMsg = msg + "You have " + leftMinutes + " minutes remaining.";
                pushPalmcallMsgApi.sentMsg(callerAfid, callerMsg, LogHelper.getLogId());
            }

            if (StringUtils.isNotEmpty(receiverAfid)) {
                String receiverMsg;
                if (StringUtils.isNotEmpty(rechargeFlag)) {
                    if ("notPhoneCountryCode".equals(rechargeFlag)) {
                        receiverMsg = msg;
                        pushPalmcallMsgApi.sentMsg(receiverAfid, receiverMsg, LogHelper.getLogId());
                    } else if ("amountError".equals(rechargeFlag)) {
                        receiverMsg = msg;
                        pushPalmcallMsgApi.sentMsg(receiverAfid, receiverMsg, LogHelper.getLogId());
                    } else {
                        String [] flag = rechargeFlag.split("-");
                        if ("success".equals(flag[0])) {
                            receiverMsg = msg + "Which earned " + flag[1] + ".";
                            pushPalmcallMsgApi.sentMsg(receiverAfid, receiverMsg, LogHelper.getLogId());
                        } else {
                            receiverMsg = msg;
                            pushPalmcallMsgApi.sentMsg(receiverAfid, receiverMsg, LogHelper.getLogId());
                        }
                    }
                } else {
                    pushPalmcallMsgApi.sentMsg(receiverAfid, msg, LogHelper.getLogId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
