package com.iweb.demo.eduservice.service;

import com.iweb.demo.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
public interface EduVideoService extends IService<EduVideo> {

    int removeVideoByCourseID(String courseId);
}
