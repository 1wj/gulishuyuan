package com.iweb.demo.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


//加的这个参数是指加入了数据库依赖，但没配置数据库的url之类的，（不需要数据库）会引起启动报错
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient   //nacos注册
@ComponentScan(basePackages = {"com.iweb"})
public class OssApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class,args);
    }
}
