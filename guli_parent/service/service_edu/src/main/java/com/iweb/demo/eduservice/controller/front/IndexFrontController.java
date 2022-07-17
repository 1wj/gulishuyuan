package com.iweb.demo.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.commonutils.R;
import com.iweb.demo.eduservice.entity.EduCourse;
import com.iweb.demo.eduservice.entity.EduTeacher;
import com.iweb.demo.eduservice.service.EduCourseService;
import com.iweb.demo.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
//@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    //查询前8条热门课程，查询前4条名师
    @GetMapping("/index")
    public R index(){
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 8");
        List<EduCourse> courselist = courseService.list(queryWrapper);

        QueryWrapper<EduTeacher> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.orderByDesc("id");
        queryWrapper2.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(queryWrapper2);
        return R.ok().data("courselist",courselist).data("teacherList",teacherList);
    }

}
