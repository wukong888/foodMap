package com.marketing.system.job;

import com.marketing.system.entity.UserInfo;
import com.marketing.system.service.UserInfoService;
import com.marketing.system.util.ApiResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
//@EnableScheduling
@PropertySource("classpath:application.properties")
public class TestJob extends BatchProperties.Job {
    private static final Logger logger = Logger.getLogger(TestJob.class);

    @Autowired
    private UserInfoService userInfo;

    @Scheduled(cron = "${jobs.schedule}")
    public ApiResult<UserInfo> handleOrderStatus() {
        logger.info("开始执行定时任务");

        UserInfo user = userInfo.selectByPrimaryKey(Long.valueOf("2"));
        ApiResult<UserInfo> result = null;
        result = new ApiResult<UserInfo>(200,"xxxx",user,null);
        logger.info("结束执行定时任务");
        logger.info("结果："+result.getData());
        logger.info("结果2："+user.getLoginName());
        return result;

    }
}
