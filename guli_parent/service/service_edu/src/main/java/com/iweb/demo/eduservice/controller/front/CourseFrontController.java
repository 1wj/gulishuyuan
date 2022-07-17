package com.iweb.demo.eduservice.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.commonutils.JwtUtils;
import com.iweb.commonutils.R;
import com.iweb.commonutils.ordervo.CourseWebVoOrder;
import com.iweb.demo.eduservice.client.OrdersClient;
import com.iweb.demo.eduservice.entity.EduCourse;
import com.iweb.demo.eduservice.entity.chapter.ChapterVo;
import com.iweb.demo.eduservice.entity.frontvo.CourseFrontVo;
import com.iweb.demo.eduservice.entity.frontvo.CourseWebVo;
import com.iweb.demo.eduservice.service.EduChapterService;
import com.iweb.demo.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

    @Autowired
    OrdersClient ordersClient;

    //1.条件查询带分页查询课程
    @PostMapping("/getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable("page") long page,
                                 @PathVariable("limit") long limit,
                                 @RequestBody(required = false)CourseFrontVo courseFrontVo){
        Page<EduCourse> coursePage=new Page<>(page,limit);
        Map<String,Object> map=courseService.getCourseFrontList(coursePage,courseFrontVo);

        //返回分页所有数据
        return R.ok().data(map);
    }

    //2.课程详情信息的方法
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getChapterService(@PathVariable("courseId") String courseId,
                               HttpServletRequest request) {
        //根据课程id，编写SQL语句查询课程信息
        CourseWebVo courseWebVo=courseService.getBaseCourseInfo(courseId);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoBuCourseId(courseId);

        //3.根据课程id和用户id查询订单表中订单状态
        boolean buyCourse = ordersClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }

    //3.根据课程id获取课程信息
    @PostMapping("/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable("id") String id ){
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }

}
