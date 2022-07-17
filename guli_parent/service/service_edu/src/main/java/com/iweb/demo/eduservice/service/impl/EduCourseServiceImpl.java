package com.iweb.demo.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.commonutils.R;
import com.iweb.demo.eduservice.entity.EduCourse;
import com.iweb.demo.eduservice.entity.EduCourseDescription;
import com.iweb.demo.eduservice.entity.EduTeacher;
import com.iweb.demo.eduservice.entity.frontvo.CourseFrontVo;
import com.iweb.demo.eduservice.entity.frontvo.CourseWebVo;
import com.iweb.demo.eduservice.entity.vo.CourseInfoVo;
import com.iweb.demo.eduservice.entity.vo.CoursePublishVo;
import com.iweb.demo.eduservice.mapper.EduCourseMapper;
import com.iweb.demo.eduservice.service.EduChapterService;
import com.iweb.demo.eduservice.service.EduCourseDescriptionService;
import com.iweb.demo.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iweb.demo.eduservice.service.EduVideoService;
import com.iweb.servicebase.exception.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表添加课程基本信息
        //将CourseInfoVo转换成EduCourse对象
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(0==insert){
            throw new GuLiException(20001,"添加课程信息失败");
        }

        //获取添加之后课程id
        String cid = eduCourse.getId();

        //2.向课程简介表添加课程简介
        EduCourseDescription courseDescription=new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,courseDescription);

        //设置描述id就是课程id
        courseDescription.setId(cid);

        courseDescriptionService.save(courseDescription);
        return cid;
    }

    //根据课程id查询课程基本信息（回显）
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
       //查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //2.查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    //修改课程
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.修改课程表
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if(update==0){
            throw  new GuLiException(20001,"修改课程信息失败");
        }
        //2.修改描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoVo.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        boolean b = courseDescriptionService.updateById(courseDescription);
        if(!b){
            throw  new GuLiException(20001,"修改课程描述失败");
        }
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper 只能用baseMapper 不能用this
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {



        //1根据课程id删除小节
        int i1=videoService.removeVideoByCourseID(courseId);

        //2根据课程id删除章节
        int i2=chapterService.removeChapterByCourseID(courseId);

        //3根据课程id删除描述
        boolean b = courseDescriptionService.removeById(courseId);

        //4根据课程id删除课程本身
        int i = baseMapper.deleteById(courseId);
        if(i==0){
            throw new GuLiException(20001,"删除课程失败");
        }


    }

    //条件查询带分页
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper=new QueryWrapper();

        //判断条件值是否为空，不为空拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){//一级分类
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){//二级分类
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){ //关注度 也就是销售量
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {//创建日期 最新
            wrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {//价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(coursePage, wrapper);

        List<EduCourse> records = coursePage.getRecords();
        long current = coursePage.getCurrent();
        long pages = coursePage.getPages();
        long size = coursePage.getSize();
        long total = coursePage.getTotal();

        boolean hasNext = coursePage.hasNext();//是否有下一页
        boolean hasPrevious = coursePage.hasPrevious();//是否有上一页

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //根据课程id，编写SQL语句查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
       return baseMapper.getBaseCourseInfo(courseId);
    }
}
