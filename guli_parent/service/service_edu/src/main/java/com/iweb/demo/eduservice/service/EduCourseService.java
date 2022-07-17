package com.iweb.demo.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.demo.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iweb.demo.eduservice.entity.frontvo.CourseFrontVo;
import com.iweb.demo.eduservice.entity.frontvo.CourseWebVo;
import com.iweb.demo.eduservice.entity.vo.CourseInfoVo;
import com.iweb.demo.eduservice.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void removeCourse(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
