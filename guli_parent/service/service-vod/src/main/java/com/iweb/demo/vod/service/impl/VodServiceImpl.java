package com.iweb.demo.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;

import com.iweb.demo.vod.service.VodService;
import com.iweb.demo.vod.utils.ConstantVodUtils;
import com.iweb.demo.vod.utils.InitVodClient;
import com.iweb.servicebase.exception.GuLiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadAlyiVideo(MultipartFile file) {

        try {
            String accessKeyId= ConstantVodUtils.KEY_ID;
            String accessKeySecret=ConstantVodUtils.KEY_SECRET;

            //上传文件原始名称
            String fileName=file.getOriginalFilename();

            //上传后文件的名称  01
            String title = fileName.substring(0, fileName.lastIndexOf("."));

            //上传文件输入流
            InputStream inputStream=file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId=null;

            if (response.isSuccess()) {
            videoId=response.getVideoId();

           } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId=response.getVideoId();
           }

            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //在阿里云中删除多个视频
    @Override
    public void removeVideoList(List videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request= new DeleteVideoRequest();

            //向request对象中设置视频id
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
             request.setVideoIds(videoIds);

            //调用初始化对象的方法来实现删除
            client.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuLiException(20001,"删除云端视频失败");
        }
    }

}
