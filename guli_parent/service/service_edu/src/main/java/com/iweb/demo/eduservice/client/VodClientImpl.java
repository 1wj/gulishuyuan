package com.iweb.demo.eduservice.client;

import com.iweb.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;


//用来进行兜底 如果正确的话不会执行  熔断器

@Component
public class VodClientImpl implements VodClient {
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
