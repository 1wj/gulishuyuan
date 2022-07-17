package com.iweb.demo.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.demo.eduservice.entity.EduChapter;
import com.iweb.demo.eduservice.entity.EduVideo;
import com.iweb.demo.eduservice.entity.chapter.ChapterVo;
import com.iweb.demo.eduservice.entity.chapter.VideoVo;
import com.iweb.demo.eduservice.mapper.EduChapterMapper;
import com.iweb.demo.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iweb.demo.eduservice.service.EduVideoService;
import com.iweb.servicebase.exception.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;


    @Override
    public List<ChapterVo> getChapterVideoBuCourseId(String courseId) {
        //1根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter=new QueryWrapper();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2根据课程id查询课程重面所有的小节
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper();
        wrapperChapter.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //最终集合
        List<ChapterVo> finalList=new ArrayList<>();

        //3遍历查询章书list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo=new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            //创建集合，用于封装章节的小节
            List<VideoVo> videoList=new ArrayList<>();

            //4遍历查询小节list集合，进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {
                EduVideo eduVideo = eduVideoList.get(m);
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }


    @Override
    public boolean deleteChapter(String chapterId) {
        //如果章节下面有小节就不删了
        QueryWrapper<EduVideo> wrapper=new QueryWrapper();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if(count>0){
            //不删
            throw new GuLiException(20001,"有小节不能删除");
        }else{
            //删除章节
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }


    }

    //根据课程id删除章节
    @Override
    public int removeChapterByCourseID(String courseId) {
        QueryWrapper<EduChapter> wrapper=new QueryWrapper();
        wrapper.eq("course_id",courseId);
        int delete = baseMapper.delete(wrapper);
        return delete;

    }
}
