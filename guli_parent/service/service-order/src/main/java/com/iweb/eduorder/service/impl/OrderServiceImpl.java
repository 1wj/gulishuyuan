package com.iweb.eduorder.service.impl;

import com.iweb.commonutils.ordervo.CourseWebVoOrder;
import com.iweb.commonutils.ordervo.UcenterMemberOrder;
import com.iweb.eduorder.client.EduClient;
import com.iweb.eduorder.client.UcenterClient;
import com.iweb.eduorder.entity.Order;
import com.iweb.eduorder.mapper.OrderMapper;
import com.iweb.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iweb.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //1.生成订单号的方法
    @Override
    public String createOrders(String courseId, String memberId) {

        //通过远程调用根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

        //通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //创建order对象，向order对象里面设置需要数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//生成订单号

        order.setCourseId(courseId);  //课程id
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());  //讲师名称
        order.setTotalFee(courseInfoOrder.getPrice());

        order.setMemberId(memberId);   //用户id
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());

        order.setStatus(0);         //支付状态  0未支付
        order.setPayType(1);        //支付类型  1微信

        baseMapper.insert(order);

        //返回订单号
        return order.getOrderNo();
    }
}
