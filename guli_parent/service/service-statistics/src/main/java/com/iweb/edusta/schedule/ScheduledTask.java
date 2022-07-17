package com.iweb.edusta.schedule;

import com.iweb.edusta.service.StatisticsDailyService;
import com.iweb.edusta.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//定时任务
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;
    //0/5 * * * * ? 表示每隔5秒执行一次这个方法
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1(){
        System.out.println("*****************************task1执行了");
    }

    //在每天凌晨1点，把前一天数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ? ")//最多只能放6位
    public void task2(){
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
