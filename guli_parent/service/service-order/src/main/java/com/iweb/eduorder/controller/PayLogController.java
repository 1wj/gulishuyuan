package com.iweb.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iweb.commonutils.R;
import com.iweb.eduorder.entity.Order;
import com.iweb.eduorder.service.OrderService;
import com.iweb.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-09
 */
@RestController
@RequestMapping("/eduorder/paylog")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @Autowired
    private OrderService orderService;

    //1.生成微信支付二维码接口
    //参数是订单号
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable("orderNo") String orderNo){
        Map map= payLogService.createNative(orderNo);
        System.out.println("***返回二维码map集合:  "+map);
        return R.ok().data(map);
    }

    //2.查询订单支付状态
    //参数：订单号，根据 订单号查询 支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable("orderNo") String orderNo){
        Map<String,String> map=payLogService.queryPayStatus(orderNo);
        System.out.println("---查询订单状态："+map);
        if(map==null){
            return R.error().message("支付出错了");
        }
        //如果返回值不为空，通过map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表，并 更新订单表订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }

    //3.根据课程id和用户id查询订单表中订单状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,
                               @PathVariable("memberId") String memberId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        if(count>0){   //已经支付
            return  true;
        }else {
            return false;
        }

    }

}

