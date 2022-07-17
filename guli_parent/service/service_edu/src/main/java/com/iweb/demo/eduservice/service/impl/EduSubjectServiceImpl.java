package com.iweb.demo.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.demo.eduservice.entity.EduSubject;
import com.iweb.demo.eduservice.entity.excel.SubjectData;
import com.iweb.demo.eduservice.entity.subject.OneSubject;
import com.iweb.demo.eduservice.entity.subject.TwoSubject;
import com.iweb.demo.eduservice.listener.SubjectExcelListener;
import com.iweb.demo.eduservice.mapper.EduSubjectMapper;
import com.iweb.demo.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {

        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        //1.查询所有一级分类
        QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneEduSubjects = baseMapper.selectList(wrapperOne);//this.list也行

        //2.查询所有二级分类
        QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoEduSubjects = baseMapper.selectList(wrapperTwo);//this.list也行

        //创建一个最终集合
        List<OneSubject> oneFinalSubjectList=new ArrayList<>();

        //3.封装一级分类
        //查询出来所有的一级分类list集合遍历，的到每个一级分类对象，获取每个一级分类对象值，
        //封装到要求的list集合里面
        for (int i = 0; i < oneEduSubjects.size(); i++) {
            EduSubject eduSubject = oneEduSubjects.get(i);
            OneSubject oneSubject=new OneSubject();
            BeanUtils.copyProperties(eduSubject,oneSubject);//使用了一个工具类,进行复制
            oneFinalSubjectList.add(oneSubject);

            //在一级分类循环遍历查询所有二级分类
            //创建list集合封装每一个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList=new ArrayList<>();
            for (int i1 = 0; i1 < twoEduSubjects.size(); i1++) {
                EduSubject tSubject = twoEduSubjects.get(i1);
                if(tSubject.getParentId().equals(eduSubject.getId())){
                    TwoSubject twoSubject=new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //把一级下面所有的二级分类放到一级里面
            oneSubject.setChildren(twoFinalSubjectList);
        }


        return oneFinalSubjectList;
    }
}
