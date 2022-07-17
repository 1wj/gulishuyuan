package com.iweb.eduorder.service;

import com.iweb.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-09
 */
public interface OrderService extends IService<Order> {

    String createOrders(String courseId, String memberId);
}
