package com.iweb.msm.controller;

import com.iweb.commonutils.R;
import com.iweb.msm.service.MsmService;
import com.iweb.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired  //spring 默认封装的Redis模板对象
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable("phone") String phone){
        //从Redis获取验证码，如果获取到直接返回
        String code1=redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code1)){
            return R.ok();
        }


        String code= RandomUtil.getFourBitRandom();
        Map<String,Object> param=new HashMap<>();
        param.put("code",code);
        //调用service发送短信的方法
        boolean isSend=msmService.send(param,phone);
        if(isSend){
            //发送成功。把发送成功的验证码放入Redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(phone,code, 5,TimeUnit.MINUTES);//设置有效时长5分钟
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }

    }
}
