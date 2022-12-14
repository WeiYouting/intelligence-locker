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

        //??????????????????????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // ??????????????????????????????
        List<OrderDo> orderList = orderMapper.queryByPhone(userPhone);
        Page page = (Page) orderList;

        // ???????????????????????? key?????????id value???????????????
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        Map<String, SiteDo> siteMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // ??????????????????
        ArrayList<OrderVo> data = new ArrayList<>();
        orderList.stream().forEach(order -> {
            SiteDo site = siteMap.get(order.getSiteId().toString());
            data.add(
                    OrderVo.builder().orderId(order.getOrderId())
                            .rider(order.getRider() == null ? "??????????????????" : order.getRider())
                            .customer(order.getCustomer())
                            .status(OrderStatusEnum.getByCode(order.getStatus()).getMsg())
                            .generationTime(simpleDateFormat.format(order.getGenerationTime()))
                            .completionTime(order.getCompletionTime() == null ? "??????????????????" : simpleDateFormat.format(order.getCompletionTime()))
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
        //??????????????????????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // ????????????????????????
        List<OrderDo> orderList = orderMapper.queryByPhoneByNotStatus(OrderStatusEnum.COMPLETE.getCode(), userPhone);

        // ??????????????????
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        Map<String, SiteDo> siteMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // ??????????????????
        ArrayList<OrderVo> data = new ArrayList<>();
        orderList.stream().forEach(order -> {
            SiteDo site = siteMap.get(order.getSiteId().toString());
            data.add(
                    OrderVo.builder().orderId(order.getOrderId())
                            .rider(order.getRider() == null ? "??????????????????" : order.getRider())
                            .customer(order.getCustomer())
                            .status(OrderStatusEnum.getByCode(order.getStatus()).getMsg())
                            .generationTime(simpleDateFormat.format(order.getGenerationTime()))
                            .completionTime(order.getCompletionTime() == null ? "??????????????????" : simpleDateFormat.format(order.getCompletionTime()))
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
        //??????????????????????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        List<Integer> data = new ArrayList<>();

        // ??????????????????
        List<OrderDo> orderDos = orderMapper.queryByPhone(userPhone);

        // ??????????????????????????????
        orderDos.forEach(order -> {
            if (order.getStatus().compareTo(OrderStatusEnum.ARRIVE.getCode()) == 0) {

                // ?????????????????????
                if (LockerUtil.get(order.getLockerId())) {

                    // ??????????????????
                    orderMapper.updateStatus(order.getOrderId(), OrderStatusEnum.COMPLETE.getCode(), new Date(), order.getLockerId(), order.getRider());

                    //????????????
                    userMapper.updateIntegral(AddIntegralDo.builder().phone(order.getRider()).integral(10.0).build());
                    userMapper.updateIntegral(AddIntegralDo.builder().phone(order.getCustomer()).integral(5.0).build());
                    data.add(order.getLockerId());
                }
            }
        });


        return new Result(ResultCodeEnum.GET_SUCCESS, data);
    }
}
