package com.iweb.edusta.controller;


import com.iweb.commonutils.R;
import com.iweb.edusta.service.StatisticsDailyService;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-13
 */
@RestController
@RequestMapping("/edusta/sta")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;


    //1.统计某一天注册人数,生成统计数据
    @PostMapping("/registerCount/{day}")
    public R registerCount(@PathVariable("day") String day){
        statisticsDailyService.registerCount(day);
        return R.ok();
    }

    //2.图表显示，返回两个部分数据，日期json数组，数量json数组
    @GetMapping("/showDate/{type}/{begin}/{end}")
    public R showDate(@PathVariable("type") String type,
                      @PathVariable("begin") String begin,
                      @PathVariable("end") String end){
        Map<String, Object> map= statisticsDailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }

}

