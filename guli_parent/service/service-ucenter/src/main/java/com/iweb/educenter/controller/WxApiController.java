package com.iweb.educenter.controller;



import com.google.gson.Gson;
import com.iweb.commonutils.JwtUtils;
import com.iweb.commonutils.R;
import com.iweb.educenter.entity.UcenterMember;
import com.iweb.educenter.service.UcenterMemberService;
import com.iweb.educenter.utils.ConstantWxUtils;
import com.iweb.educenter.utils.HttpClientUtils;
import com.iweb.servicebase.exception.GuLiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller  //注意这个不能是restController ,要重定向而不是一个json对象
@RequestMapping("/api/ucenter/wx")  //注意这看看是否需要改回去？ucenter
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //2.获取扫描人信息，添加数据
    @GetMapping("/callback")
    public String callback(String code,String state){
        try {
        //1.获取code值，也称为临时票据，类似于验证码

        //2.拿着code 请求微信固定地址，得到两个值 accsess_token和openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数：id 秘钥和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);

            //请求这个拼接好的地址，得到返回两个值，access_token 和 openID
            //使用HTTPClient模拟浏览器发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来两个值，，access_token 和 openID
            //把accessTokenInfo字符串转换成map集合，根据map里面的key获取对应值
            Gson gson=new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) map.get("access_token");
            String openid = (String) map.get("openid");



        //4.把扫描人信息添加到数据库里面
            //根据openID判断表里是否存在相同微信信息
            UcenterMember member=memberService.getOpenIdMember(openid);
            if(member==null){

                //3. 拿着accsess_token和openid 再去请求微信固定地址，获取扫描人信息
                //访问微信的资源服务器 ，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl,
                        access_token,
                        openid);
                //使用HTTPClient模拟浏览器发送请求，得到用户信息
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userInfo字符串扫描人信息
                Gson gson1=new Gson();
                HashMap map2 = gson1.fromJson(userInfo, HashMap.class);
                String nickname = (String) map2.get("nickname");
                String headimgurl = (String) map2.get("headimgurl");


                member=new UcenterMember();
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openid);
                memberService.save(member);
            }

            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuLiException(20001,"登录失败");
        }

    }




    //生成二维码
    @GetMapping("/login")

    public String getWxCode(){
        //固定地址，后面拼接参数
       /*   拼接太慢了不用
        String url=" https://open.weixin.qq.com/connect/qrconnect?appid="+
                ConstantWxUtils.WX_OPEN_APP_ID+
                "";*/

        // 微信开放平台授权baseUrl %s是占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl=ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl=URLEncoder.encode(redirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //设置%s里面值
        String url = String.format(baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu");
        return "redirect:"+url;
       // return R.ok().data("lianjie",url);
    }

}

