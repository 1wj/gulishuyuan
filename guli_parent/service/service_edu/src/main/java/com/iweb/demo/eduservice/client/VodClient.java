package com.iweb.demo.eduservice.client;

import com.iweb.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodClientImpl.class)  //在被调用者 VOD配置里面去复制  调用的服务的名称
                        // fallback是熔断机制，只有出问题时才会走接口实现类
@Component
public interface VodClient {

    //定义调用方法的路径 (记住这得是全路径)

    //1.根据视频id删除阿里云视频  @PathVariable里面一定要加参数
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
     R removeAlyVideo(@PathVariable("id") String id);

    //2.删除多个阿里云视频的方法(一次删除多个小节)
    @DeleteMapping("/eduvod/video/delete-batch")
     R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
