package com.iweb.demo.eduservice.controller;


import com.iweb.commonutils.R;
import com.iweb.demo.eduservice.client.VodClient;
import com.iweb.demo.eduservice.entity.EduVideo;
import com.iweb.demo.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-24
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //注入VODClient 实现服务调用
    @Autowired
    private VodClient vodClient;


    //根据小节id查询小节
    @GetMapping("/getVideo/{id}")
    public R getVideo(@PathVariable("id") String id){
        EduVideo video = videoService.getById(id);
        return R.ok().data("video",video);

    }

    //添加小节
    @PostMapping("/addVideo")
    public R  addVideo(@RequestBody EduVideo eduVideo){
        boolean save = videoService.save(eduVideo);
        return R.ok();
    }

    // 删除小节，删除对应阿里云里面的视频（这是单个删除）
    @DeleteMapping("/deleteVideo/{id}")
    public R  deleteVideo(@PathVariable("id") String id){

        //根据小节id得到视频id，在调用方法实现视频删除
        EduVideo eduVideo = videoService.getById(id);

        //判断小节里面是否有视频id
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            //根据视频id，远程调用实现视频删除
            R r = vodClient.removeAlyVideo(videoSourceId);
        }

        //后删视频小节
        boolean save = videoService.removeById(id);
        return R.ok();
    }

    //修改小节
    @PostMapping("/updateVideo")
    public R updateChapter(@RequestBody EduVideo eduVideo){
        boolean flag = videoService.updateById(eduVideo);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }
}

