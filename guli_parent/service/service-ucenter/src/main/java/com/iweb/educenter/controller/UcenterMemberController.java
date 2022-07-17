package com.iweb.educenter.controller;


import com.iweb.commonutils.JwtUtils;
import com.iweb.commonutils.R;
import com.iweb.commonutils.ordervo.UcenterMemberOrder;
import com.iweb.educenter.entity.UcenterMember;
import com.iweb.educenter.entity.vo.RegisterVo;
import com.iweb.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member){//里面是手机号和密码
        //返回token值，使用jwt生成
        String token=memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo){//里面是手机号和密码

        memberService.register(registerVo); //返回token值，使用jwt生成
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return  R.ok().data("userInfo",member);
    }

    //根据用户id获取用户信息
    @PostMapping("/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id){


        UcenterMember member = memberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }


    //查询某一天注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day){
        Integer count=memberService.countRegisterDay(day);
        return R.ok().data("countRegister",count);
    }
}

