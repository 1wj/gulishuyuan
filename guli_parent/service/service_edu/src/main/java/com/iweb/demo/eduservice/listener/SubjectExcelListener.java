package com.iweb.demo.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.demo.eduservice.entity.EduSubject;
import com.iweb.demo.eduservice.entity.excel.SubjectData;
import com.iweb.demo.eduservice.service.EduSubjectService;
import com.iweb.servicebase.exception.GuLiException;


//监听器SubjectExcelListener 不能交给spring进行管理，需要自己new 不能注入其他对象
//不能实现数据库操作
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    private EduSubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //一行一行读取Excel中的内容 表头除外
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (null==subjectData){
            throw new GuLiException(20001,"文件数据空");
        }

        //第一个值一级分类，第二个值二级分类
        EduSubject eduSubjectOne = this.existOne(subjectService, subjectData.getOneSubjectName());
        if(null==eduSubjectOne){
            eduSubjectOne=new EduSubject();
            eduSubjectOne.setParentId("0");
            eduSubjectOne.setTitle(subjectData.getOneSubjectName());//一级分类名称
            subjectService.save(eduSubjectOne);
        }

        //获取一级分类的id
        String pid = eduSubjectOne.getId();
        //判断二级分类是否重复
        EduSubject eduSubjectTwo = this.existTwo(subjectService, subjectData.getTwoSubjectName(),pid);
        if(null==eduSubjectTwo){
            eduSubjectTwo=new EduSubject();
            eduSubjectTwo.setParentId(pid);
            eduSubjectTwo.setTitle(subjectData.getTwoSubjectName());//二级分类名称
            subjectService.save(eduSubjectTwo);
        }

    }

    //判断一级分类不能重复添加
    private EduSubject existOne(EduSubjectService subjectService,String name){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwo(EduSubjectService subjectService,String name,String id){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",id);
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
