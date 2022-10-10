package com.wyt.intelligencelocker.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.OrderMapper;
import com.wyt.intelligencelocker.dao.SiteMapper;
import com.wyt.intelligencelocker.dao.UserMapper;
import com.wyt.intelligencelocker.entity.Do.AddIntegralDo;
import com.wyt.intelligencelocker.entity.Do.OrderDo;
import com.wyt.intelligencelocker.entity.Do.SiteDo;
import com.wyt.intelligencelocker.entity.Vo.OrderVo;
import com.wyt.intelligencelocker.meta.enums.OrderStatusEnum;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;
import com.wyt.intelligencelocker.service.ICustomerService;
import com.wyt.intelligencelocker.utils.JwtUtil;
import com.wyt.intelligencelocker.utils.LockerUtil;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author WeiYouting
 * @create 2022/10/10 9:07
 * @Email Wei.youting@qq.com
 */
@Service
public class CustomerServiceImpl implements ICustomerService {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private final Integer PAGE_NUM = 10;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @CheckPermission(UserRoleEnum.USER)
    public Result queryAllOrder(Integer index) {
        PageHelper.startPage(index, PAGE_NUM);

        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 查询该账号的所有订单
        List<OrderDo> orderList = orderMapper.queryByPhone(userPhone);
        Page page = (Page) orderList;

        // 获取站点信息集合 key为站点id value为站点对象
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        Map<String, SiteDo> siteMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // 视图模型转换
        ArrayList<OrderVo> data = new ArrayList<>();
        orderList.stream().forEach(order -> {
            SiteDo site = siteMap.get(order.getSiteId().toString());
            data.add(
                    OrderVo.builder().orderId(order.getOrderId())
                            .rider(order.getRider() == null ? "暂无骑手接单" : order.getRider())
                            .customer(order.getCustomer())
                            .status(OrderStatusEnum.getByCode(order.getStatus()).getMsg())
                            .generationTime(simpleDateFormat.format(order.getGenerationTime()))
                            .completionTime(order.getCompletionTime() == null ? "订单暂未完成" : simpleDateFormat.format(order.getCompletionTime()))
                            .site(site.getSiteName())
                            .province(site.getProvince())
                            .city(site.getCity())
                            .address(site.getSiteAddress())
                            .lockerId(order.getLockerId())
                            .note(order.getNote())
                            .build());
        });

        return new Result(ResultCodeEnum.QUERY_ORDER_SUCCESS, data);
    }

    @Override
    @CheckPermission(UserRoleEnum.USER)
    public Result queryUnfinshed() {
        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 查询未完成的订单
        List<OrderDo> orderList = orderMapper.queryByPhoneByNotStatus(OrderStatusEnum.COMPLETE.getCode(), userPhone);

        // 获取站点信息
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        Map<String, SiteDo> siteMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // 视图模型转换
        ArrayList<OrderVo> data = new ArrayList<>();
        orderList.stream().forEach(order -> {
            SiteDo site = siteMap.get(order.getSiteId().toString());
            data.add(
                    OrderVo.builder().orderId(order.getOrderId())
                            .rider(order.getRider() == null ? "暂无骑手接单" : order.getRider())
                            .customer(order.getCustomer())
                            .status(OrderStatusEnum.getByCode(order.getStatus()).getMsg())
                            .generationTime(simpleDateFormat.format(order.getGenerationTime()))
                            .completionTime(order.getCompletionTime() == null ? "订单暂未完成" : simpleDateFormat.format(order.getCompletionTime()))
                            .site(site.getSiteName())
                            .province(site.getProvince())
                            .city(site.getCity())
                            .address(site.getSiteAddress())
                            .lockerId(order.getLockerId())
                            .note(order.getNote())
                            .build());
        });

        return new Result(ResultCodeEnum.QUERY_ORDER_SUCCESS, data);
    }

    @Override
    @CheckPermission(UserRoleEnum.USER)
    @Transactional(rollbackFor = Exception.class)
    public Result get() {
        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        List<Integer> data = new ArrayList<>();

        // 获取所有订单
        List<OrderDo> orderDos = orderMapper.queryByPhone(userPhone);

        // 查询已到达站点的订单
        orderDos.forEach(order -> {
            if (order.getStatus().compareTo(OrderStatusEnum.ARRIVE.getCode()) == 0) {

                // 获取柜号并取出
                if (LockerUtil.get(order.getLockerId())) {

                    // 更新订单状态
                    orderMapper.updateStatus(order.getOrderId(), OrderStatusEnum.COMPLETE.getCode(), new Date(), order.getLockerId(), order.getRider());

                    //增加积分
                    userMapper.updateIntegral(AddIntegralDo.builder().phone(order.getRider()).integral(10.0).build());
                    userMapper.updateIntegral(AddIntegralDo.builder().phone(order.getCustomer()).integral(5.0).build());
                    data.add(order.getLockerId());
                }
            }
        });


        return new Result(ResultCodeEnum.GET_SUCCESS, data);
    }
}
