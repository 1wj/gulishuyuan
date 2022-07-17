package com.iweb.demo.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.iweb.commonutils.R;
import com.iweb.demo.vod.service.VodService;
import com.iweb.demo.vod.utils.ConstantVodUtils;
import com.iweb.demo.vod.utils.InitVodClient;
import com.iweb.servicebase.exception.GuLiException;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("/uploadAlyiVideo")
    public R uploadAlyiVideo(MultipartFile file){
        String videoId=vodService.uploadAlyiVideo(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频id删除阿里云视频（一次删除一个小节）
    @DeleteMapping("/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id){
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request= new DeleteVideoRequest();
            //向request对象中设置视频id
            request.setVideoIds(id);
            //调用初始化对象的方法来实现删除
            client.getAcsResponse(request);
            return  R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuLiException(20001,"删除云端视频失败");
        }

    }

    //删除多个阿里云视频的方法(一次删除多个小节)
    @DeleteMapping("/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeVideoList(videoIdList);
        return R.ok();
    }


    //根据视频id获取视频凭证
    @GetMapping("/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable("id") String id){
        try {
            DefaultAcsClient client=InitVodClient.initVodClient(ConstantVodUtils.KEY_ID,
                    ConstantVodUtils.KEY_SECRET);

            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            request.setVideoId(id);

            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //获得凭证
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuLiException(20001,"获取凭证失败");

        }
    }

}
