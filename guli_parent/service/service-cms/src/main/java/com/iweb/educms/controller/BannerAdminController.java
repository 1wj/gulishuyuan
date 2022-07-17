package com.iweb.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.commonutils.R;
import com.iweb.educms.entity.CrmBanner;
import com.iweb.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/educms/banneradmin")  //后台系统中的接口
//@CrossOrigin

//TODO 后台页面自己做
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //1.分页查询banner
    @GetMapping("/pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable("page") long page,
                        @PathVariable("limit") long limit){
        Page<CrmBanner> pageBanner=new Page<>(page,limit);
        bannerService.page(pageBanner,null);
        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());

    }

    //根据id查询banner
    @GetMapping("/get/{id}")
    public R get(@PathVariable("id") String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    //增加banner
    @PostMapping("/save")
    public R save(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    //修改banner
    @PutMapping("/update")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }

   //删除banner
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") String id) {
        bannerService.removeById(id);
        return R.ok();
    }

}

