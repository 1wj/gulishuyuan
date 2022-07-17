package com.iweb.demo.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.iweb.demo.oss.service.OssService;
import com.iweb.demo.oss.utils.ConstantPropertiedUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像的方法
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        //工具类获取值
        String endpoint = ConstantPropertiedUtils.END_POINT;
        String accessKeyId = ConstantPropertiedUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiedUtils.KEY_SECRET;
        String bucketName = ConstantPropertiedUtils.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取 上传文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1.在文件名称里面添加随机唯一值,避免覆盖
           /* String uuid= UUID.randomUUID().toString().replaceAll("-","");
            fileName=uuid+fileName;*/

            //2.把文件按照日期进行分类 //2019/11/11/01.jpg
            String dataPath = new DateTime().toString("yyyy/MM/dd");
            fileName=dataPath+"/"+fileName;

            //使用oss实现上传
            //第一个参数：backet名称
            //第二个参数 上传到oss文件路径和文件名称  /aa/bb/1.jpg
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把文件上传之后的路径返回  https://edu-20211122.oss-cn-hangzhou.aliyuncs.com/2.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
