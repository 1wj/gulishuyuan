package com.iweb.demo.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.demo.eduservice.client.VodClient;
import com.iweb.demo.eduservice.entity.EduVideo;
import com.iweb.demo.eduservice.mapper.EduVideoMapper;
import com.iweb.demo.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入VodClient
    @Autowired
    private VodClient vodClient;


    //根据课程id  删除全部小节（批量删除）
    // TODO 删除小节，对应删除视频文件
    @Override
    public int removeVideoByCourseID(String courseId) {
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper();
        wrapperVideo.eq("course_id",courseId);
       // wrapperVideo.select("video_source_id");//指定查询的列
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);


        //之所以遍历是因为上面那个集合里面是一个个对象，虽然每个对象里面只有一个值，但其他的为null
        List<String> videoIds=new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                videoIds.add(videoSourceId);
            }
        }

        //根据多个视频id删除视频
        if(videoIds.size()>0){
            vodClient.deleteBatch(videoIds);
        }


        QueryWrapper<EduVideo> wrapper=new QueryWrapper();
        wrapper.eq("course_id",courseId);
        int delete = baseMapper.delete(wrapper);
        return delete;
    }
}
