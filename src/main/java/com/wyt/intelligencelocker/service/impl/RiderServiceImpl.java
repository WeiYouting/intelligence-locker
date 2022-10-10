package com.wyt.intelligencelocker.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.OrderMapper;
import com.wyt.intelligencelocker.dao.SiteMapper;
import com.wyt.intelligencelocker.entity.Do.OrderDo;
import com.wyt.intelligencelocker.entity.Do.SiteDo;
import com.wyt.intelligencelocker.entity.Vo.RiderOrderVo;
import com.wyt.intelligencelocker.meta.enums.OrderStatusEnum;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.request.AcceptOrderRequest;
import com.wyt.intelligencelocker.meta.request.QueryOrderRiderRequest;
import com.wyt.intelligencelocker.meta.request.UpdateOrderStatusRequest;
import com.wyt.intelligencelocker.service.IRiderService;
import com.wyt.intelligencelocker.utils.CommonUtil;
import com.wyt.intelligencelocker.utils.JwtUtil;
import com.wyt.intelligencelocker.utils.LockerUtil;
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
 * @create 2022/10/9 17:17
 * @Email Wei.youting@qq.com
 */
@Service
public class RiderServiceImpl implements IRiderService {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private JwtUtil jwtUtil;


    @Override
    @CheckPermission(UserRoleEnum.RIDER)
    public Result queryOrder(QueryOrderRiderRequest request) {
        //查询城市所有的站点
        List<SiteDo> siteDoList = siteMapper.queryByCity(request.getCity());

        // 站点信息转为map key为站点id
        Map<String, SiteDo> siteMap = siteDoList.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // 获取该城市所有站点的id
        List<Integer> siteIds = siteDoList.stream().map(site -> site.getId()).collect(Collectors.toList());

        // 获取所有订单
        List<OrderDo> orderList = orderMapper.queryBySiteIdByStatus(OrderStatusEnum.CREATE.getCode(), siteIds);

        // Do -> Vo
        List<RiderOrderVo> data = new ArrayList<>();
        orderList.forEach(order -> {
            SiteDo site = siteMap.get(order.getSiteId().toString());
            data.add(
                    RiderOrderVo.builder()
                            .orderId(order.getOrderId())
                            .customer(CommonUtil.desensitizedPhoneNumber(order.getCustomer()))
                            .status(OrderStatusEnum.getByCode(order.getStatus()).getMsg())
                            .generationTime(simpleDateFormat.format(order.getGenerationTime()))
                            .province(site.getProvince())
                            .city(site.getCity())
                            .address(site.getSiteAddress())
                            .site(site.getSiteName())
                            .note(order.getNote())
                            .build());
        });

        return new Result(ResultCodeEnum.QUERY_ORDER_SUCCESS, data);
    }

    @Override
    @CheckPermission(UserRoleEnum.RIDER)
    @Transactional(rollbackFor = Exception.class)
    public Result updateStatus(UpdateOrderStatusRequest request) {
        ArrayList<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.DELIVERY.getCode());
        statusList.add(OrderStatusEnum.ARRIVE.getCode());

        // 判断订单状态是否合法
        if (!(statusList.contains(request.getStatus()))) {
            throw new GlobalException(ResultCodeEnum.UPDATE_ORDER_FORMAT_ERROR);
        }

        // 查询订单是否存在
        if (orderMapper.countByOrderIdInteger(request.getOrderId()) == null) {
            throw new GlobalException(ResultCodeEnum.NOT_HAVA_ORDER);
        }

        // 获取订单对象
        OrderDo orderDo = orderMapper.queryByOrderId(request.getOrderId());

        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 判断是否为该订单的骑手
        if (!(userPhone.equals(orderDo.getRider()))) {
            throw new GlobalException(ResultCodeEnum.NOT_HAVA_PERMISSION);
        }

        // 查询订单是否已完成
        Integer status = orderDo.getStatus();
        if (status.compareTo(OrderStatusEnum.COMPLETE.getCode()) == 0 || status.compareTo(OrderStatusEnum.ARRIVE.getCode()) == 0) {
            throw new GlobalException(ResultCodeEnum.ORDER_FINISHED);
        }

        // 插入数据
        Date time = request.getStatus().compareTo(OrderStatusEnum.COMPLETE.getCode()) == 0 ? new Date() : null;
        Integer lockerId = request.getStatus().compareTo(OrderStatusEnum.ARRIVE.getCode()) == 0 ? LockerUtil.put() : null;
        if (!(orderMapper.updateStatus(request.getOrderId(), request.getStatus(), time, lockerId, null) > 0)) {
            throw new GlobalException(ResultCodeEnum.UPDATE_ORDER_STATUS_FAIL);
        }

        return new Result(ResultCodeEnum.UPDATE_ORDER_STATUS_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CheckPermission(UserRoleEnum.RIDER)
    public Result accept(AcceptOrderRequest request) {
        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 判断订单是否存在
        OrderDo orderDos = orderMapper.queryByOrderId(request.getOrderId());
        if (ObjUtil.isEmpty(orderDos)) {
            throw new GlobalException(ResultCodeEnum.NOT_HAVA_ORDER);
        }

        synchronized (this) {
            // 判断订单是否被接
            if (orderDos.getStatus().compareTo(OrderStatusEnum.CREATE.getCode()) != 0) {
                if (!(orderDos.getRider().equals(userPhone))) {
                    throw new GlobalException(ResultCodeEnum.ORDER_ACCEPTED);
                }
                throw new GlobalException(ResultCodeEnum.ACCEPT_ALREADY);
            }

            if (!(orderMapper.updateStatus(request.getOrderId(), OrderStatusEnum.RIDER.getCode(), null, null, userPhone) > 0)) {
                throw new GlobalException(ResultCodeEnum.ACCEPT_FAIL);
            }
        }

        return new Result(ResultCodeEnum.ACCEPT_SUCCESS);
    }

}
