package com.iweb.demo.eduservice.controller;


import com.iweb.commonutils.R;
import com.iweb.demo.eduservice.entity.subject.OneSubject;
import com.iweb.demo.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;


    //添加课程分类
    //获取上传过来文件，把文件内容读出来
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){
        //上传过来的文件
        subjectService.saveSubject(file,subjectService);
        return R.ok();
    }

    //课程分类列表(树形)
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list=subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

