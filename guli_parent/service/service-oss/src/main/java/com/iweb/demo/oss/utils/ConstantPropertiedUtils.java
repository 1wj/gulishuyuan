package com.iweb.demo.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口一个方法
//之所以实现这个接口是因为 实例化之后虽然变量被赋值了，但外部无法调用
@Component
public class ConstantPropertiedUtils implements InitializingBean {

    //读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;


    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    //定义公开静态常量
    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {//这个方法会在初试化之后执行
        END_POINT=endpoint;
        KEY_ID=keyId;
        KEY_SECRET=keySecret;
        BUCKET_NAME=bucketName;
    }
}
