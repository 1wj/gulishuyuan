package com.iweb.edusta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.commonutils.R;
import com.iweb.edusta.client.UcenterClient;
import com.iweb.edusta.entity.StatisticsDaily;
import com.iweb.edusta.mapper.StatisticsDailyMapper;
import com.iweb.edusta.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-13
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {

        //添加记录之前删除表中相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //远程调用得到某一天注册人数
        R r = ucenterClient.countRegister(day);
        int countRegister = (int) r.getData().get("countRegister");

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(countRegister);//注册人数
        daily.setDateCalculated(day);//统计日期

        daily.setLoginNum(RandomUtils.nextInt(200, 500));  //登录人数
        daily.setVideoViewNum(RandomUtils.nextInt(200, 500)); //
        daily.setCourseNum(RandomUtils.nextInt(200, 500));

        //添加统计数据
        baseMapper.insert(daily);
    }

    //2.图表显示，返回两个部分数据，日期json数组，数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        //根据条件查询数据
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);

        wrapper.select("date_calculated",type);
        wrapper.orderByAsc("date_calculated");

        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //前端要求 数组json结构， 对应后端是list
        List<String> data_list=new ArrayList<>();
        List<Integer> number_list=new ArrayList<>();

        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            //封装日期list集合
            data_list.add(daily.getDateCalculated());
            switch (type){
                case "login_num":
                    number_list.add(daily.getLoginNum());
                    break;
                case "register_num":
                    number_list.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    number_list.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    number_list.add(daily.getCourseNum());
                    break;
                default:
                    break;

            }
        }
        //把封装后的集合封装到map中
        Map<String,Object> map=new HashMap<>();
        map.put("data_list",data_list);
        map.put("number_list",number_list);

        return map;
    }
}
