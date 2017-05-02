package com.afmobi.palmcall.task;

import com.af.taskhandle.TaskHelperBo;
import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.model.Operator;
import com.afmobi.palmcall.outerApi.response.CallableItem;
import com.afmobi.palmcall.service.MinutesService;
import com.afmobi.palmcall.service.OperatorService;
import com.afmobi.palmcall.service.UserHotService;
import com.afmobi.palmcall.util.CallOperatorHelper;
import com.jtool.support.log.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CallOperatorTaskJob {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private UserHotService userHotService;

	@Resource
	private OperatorDAO operatorDAO;

	@Resource
	private OperatorService operatorService;

	@Resource
	private MinutesService minutesService;

	@Resource
	private CallOperatorHelper callOperatorHelper;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	/*
	<logicInfo>
		1:每30秒钟触发一次查询redis获取PalmCall水军名单<br/>
		2:接线员名单中除去 (正在通话列表中的)<br/>
		3:查询剩余下来的名单中 接线人是否在线<br/>
		4:获取出在线人员后 判断是否设置了免打扰时间<br/>
		5:数据库不存在时 接线员初始化接听次数1000 剩余分钟数0 保存到数据库<br/>
		6:整理出可接线人员列表保存到redis<br/>
	</logicInfo>
	 */
	@Scheduled(cron="*/30 * * * * ?")
	public void run() {
		//定时器没有经过Filter,所以手动加上_logId方便查询日志
		LogHelper.setLogId(UUID.randomUUID().toString());
		LogHelper.setProjectName("PalmCall");

		log.debug("执行整理水军列表定时器");
		boolean isRun = TaskHelperBo.isRun(redisTemplate, "taskGetCallableList", 30);
		if(!isRun) {
			return;
		}

		//获取PalmCall接线员名单
		List<String> receiversList = userHotService.getUserHotAllByRedis();
		log.debug("获取水军列表:" + receiversList);

		List<String> onlineList = isOnlineList(receiversList);
		fixOnlineList(onlineList);
	}

	//判断afid存在数据库 且不在免打扰时间范围内的
	private void fixOnlineList(List<String> list) {
		for (String afid : list) {
			if (!operatorDAO.hasOperator(afid)) { //数据库不存在此用户
				//水军初始接听次数1000  剩余分钟数0  不开启免打扰
				callOperatorHelper.addOperatorToDB(afid, 1000, 0, 0, 23, 7);

				minutesService.delLeftMinutesByRedis(afid);//清除redis缓存
			}

			//是否在免打扰时间范围内
			Optional<Operator> operator = operatorDAO.getOperator(afid);
			if (!callOperatorHelper.isNotDisturb(operator)) {
				//加入可接线人员列表
				CallableItem callableItem = callOperatorHelper.fixCallable(afid, operator);
				operatorService.addCallableTempByRedis(callableItem);
			}

		}

		//当清除所有接线员列表时，客户端有可能获取为空，先保存一个临时key,然后重命名
		operatorService.renameMapByRedis();
	}

	//水军列表中 除去正在通话列表中的  在去redis里那边查询在线状态
	private List<String> isOnlineList(List<String> list) {
		//先查询自己保存的 正在通话列表 有没有数据
		List<String> onlineList = new ArrayList<>();
		for (String afid : list) {
			CallableItem item = operatorService.getNowCallByRedis(afid);
			if (null == item) { //不在通话列表
				if (userHotService.isOnlineByRedis(afid)) { //在线
					onlineList.add(afid);
				}
			}
		}

		log.debug("不在通话列表且在线的水军用户:" + onlineList);

		return onlineList;
	}

}
