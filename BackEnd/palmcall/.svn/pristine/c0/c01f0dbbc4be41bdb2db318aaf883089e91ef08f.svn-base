package com.afmobi.palmcall.task;

import com.af.taskhandle.TaskHelperBo;
import com.afmobi.palmcall.dao.RechargePalmCoinFailDAO;
import com.afmobi.palmcall.model.RechargePalmCoinFail;
import com.afmobi.palmcall.service.RechargePalmCoinService;
import com.jtool.support.log.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Repository
public class RechargePalmCoinFailTaskJob {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private RechargePalmCoinFailDAO rechargePalmCoinFailDAO;

	@Resource
	private RechargePalmCoinService rechargePalmCoinService;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	/*
	<logicInfo>
		1:每10分钟触发一次，查询数据库有没有充值PalmCoin失败的记录<br/>
		2:先查询redis里有没有定时器在执行，有就不执行了，防止重复充值<br/>
		3:充值成功后，删除数据库中的失败记录<br/>
	</logicInfo>
	 */
	@Scheduled(cron="0 */10 * * * ?")
	public void run() {
		//定时器没有经过Filter,所以手动加上_logId方便查询日志
		LogHelper.setLogId(UUID.randomUUID().toString());
		LogHelper.setProjectName("PalmCall");

		log.debug("执行充值PalmCoin失败的定时器");
		boolean isRun = TaskHelperBo.isRun(redisTemplate, "taskRechargePalmCoinFail", 10 * 60);
		if(!isRun) {
			return;
		}

		List<RechargePalmCoinFail> list = rechargePalmCoinFailDAO.selectList();
		for (RechargePalmCoinFail fail : list) {
			boolean isSuccess = rechargePalmCoinService.rechargePalmCoinAmount(fail.getAfid(),
					fail.getAmount(), fail.getPhonecountrycode());

			if (isSuccess) {
				rechargePalmCoinFailDAO.delete(fail.getId());
			}
		}

	}

}
