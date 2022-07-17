package com.iweb.demo.eduservice.service;

import com.iweb.demo.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iweb.demo.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoBuCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    //根据课程id删除章节
    int removeChapterByCourseID(String courseId);
}
