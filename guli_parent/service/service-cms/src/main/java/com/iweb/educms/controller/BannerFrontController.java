package com.iweb.educms.controller;


import com.iweb.commonutils.R;
import com.iweb.educms.entity.CrmBanner;
import com.iweb.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/educms/bannerfront") //前台系统中的接口
//@CrossOrigin

public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    @Cacheable(key = "'selectIndexList'",value = "banner") //redis 缓存注解
    //查询所有banner
    @GetMapping("/getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list=bannerService.selectAllBanner();
        return R.ok().data("list",list);
    }
}

