package com.iweb.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.commonutils.JwtUtils;
import com.iweb.commonutils.MD5;
import com.iweb.educenter.entity.UcenterMember;
import com.iweb.educenter.entity.vo.RegisterVo;
import com.iweb.educenter.mapper.UcenterMemberMapper;
import com.iweb.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iweb.servicebase.exception.GuLiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-11-30
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuLiException(20001,"登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember moblieMember = baseMapper.selectOne(wrapper);
        if(moblieMember==null){//没有这个手机号
            throw new GuLiException(20001,"登录失败");
        }

        //判断密码
        if(!MD5.encrypt(password).equals(moblieMember.getPassword())){
            throw new GuLiException(20001,"登录失败");
        }

        //判断用户是否禁用
        if(moblieMember.getIsDisabled()){
            throw new GuLiException(20001,"登录失败");
        }

        //登录成功  返回token值 使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(moblieMember.getId(), moblieMember.getNickname());
        return jwtToken;
    }

    //注册的数据
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String mobile = registerVo.getMobile();
        String code = registerVo.getCode();     //验证码
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
             ||StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)){
            throw new GuLiException(20001,"注册失败");
        }

        //判断验证码
        //获取Redis中的验证码
        String rediscode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(rediscode)){
            throw new GuLiException(20001,"注册失败");
        }

        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuLiException(20001,"注册失败");
        }

        //添加数据到数据库中
        UcenterMember member=new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("https://edu-20211122.oss-cn-hangzhou.aliyuncs.com/2021/11/24/02.jpeg");//用户不禁用
        baseMapper.insert(member);

    }

    //微信扫描
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }


    //查询某一天注册人数
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);

    }
}
