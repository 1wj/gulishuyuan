package com.iweb.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.iweb.msm.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    //发送短信的方法   (正规的如果阿里云的短信申请下拉就用这个)
   /* @Override
    public boolean send(Map<String, Object> param, String phone) {
        if(StringUtils.isEmpty(phone)) return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default",
                        "LTAI5tG9zHnSHg1nc1cBKcf8",
                        "xk13WnyYXti93KnsLyDLp2I2jyDuRo");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关参数
        request.putQueryParameter("PhoneNumbers",phone);//这个name必须是这个名字  手机号
        request.putQueryParameter("SignName","b2c谷粒在线教育网站");//申请阿里云 签名名称
        request.putQueryParameter("TemplateCode","SMS_228847317");//申请阿里云 模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));//验证码：这里必须传一个json数据，其他的不认


        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }

    }*/

    //发送短信的方法（备用的，短信没有申请下来，用于流程的）(有时间可以试试第三方)
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        if(StringUtils.isEmpty(phone)) return false;
        System.out.println("临时验证码 => ："+param.get("code")+" , 电话号码=》："+phone);
        return true;
    }
}
