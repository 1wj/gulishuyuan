package com.iweb.demo.eduservice.service;

import com.iweb.demo.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iweb.demo.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-11-23
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
