package com.wyt.intelligencelocker.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.controller.util.PageResult;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.OrderMapper;
import com.wyt.intelligencelocker.dao.SiteMapper;
import com.wyt.intelligencelocker.entity.Do.OrderDo;
import com.wyt.intelligencelocker.entity.Do.SiteDo;
import com.wyt.intelligencelocker.entity.Vo.OrderVo;
import com.wyt.intelligencelocker.meta.enums.OrderStatusEnum;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.request.AddOrderRequest;
import com.wyt.intelligencelocker.meta.request.QueryOrderRequest;
import com.wyt.intelligencelocker.meta.request.QuerySiteRequest;
import com.wyt.intelligencelocker.service.IOrderService;
import com.wyt.intelligencelocker.utils.JwtUtil;
import com.wyt.intelligencelocker.utils.OrderUtil;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:34
 * @Email Wei.youting@qq.com
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private final Integer PAGE_NUM = 10;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    @CheckPermission(UserRoleEnum.USER)
    public Result querySite(QuerySiteRequest request) {
        // 根据地址查询附近站点
        List<SiteDo> siteDos = siteMapper.queryByDetail(request);
        return new Result(ResultCodeEnum.QUERY_SITE_SUCCESS, siteDos);
    }

    @Override
    @CheckPermission(UserRoleEnum.USER)
    @Transactional(rollbackFor = Exception.class)
    public Result createOrder(AddOrderRequest request) {
        // 获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 生成订单对象
        String orderId = OrderUtil.getOrderCode(userPhone);
        OrderDo order = OrderDo.builder().orderId(orderId)
                .customer(userPhone)
                .status(OrderStatusEnum.CREATE.getCode())
                .generationTime(new Date())
                .note(request.getNote())
                .siteId(request.getSiteId())
                .build();

        // 数据持久化
        if (!(orderMapper.insertOrder(order) > 0)) {
            throw new GlobalException(ResultCodeEnum.PLACE_ORDER_FAIL);
        }

        return new Result(ResultCodeEnum.PLACE_ORDER_SUCCESS, orderId);
    }

    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result queryAllOrder(Integer index) {
        // 使用pagehelper分页
        PageHelper.startPage(index, PAGE_NUM);

        // 获取所有订单和站点信息
        List<OrderDo> orderList = orderMapper.queryAllOrder();
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

        // 封装返回数据
        PageResult result = PageResult.builder().pages(page.getPages())
                .pageSize(page.getPageSize())
                .pageIndex(page.getPageNum())
                .startRow(page.getStartRow())
                .endRow(page.getEndRow())
                .total(page.getTotal())
                .data(data).build();

        return new Result(ResultCodeEnum.QUERY_ORDER_SUCCESS, result);
    }

    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result query(QueryOrderRequest request) {
        ArrayList<OrderVo> data = new ArrayList<>();
        ArrayList<OrderDo> orderList = new ArrayList<>();

        // 若订单时间不为空则筛选订单
        if (request.getStartTime() != null || request.getEndTime() != null) {
            Date createStart = null;
            Date createEnd = null;
            try {
                createStart = request.getStartTime() == null ? simpleDateFormat.parse("1970-1-1 00:00:00") : simpleDateFormat.parse(request.getStartTime());
                createEnd = request.getEndTime() == null ? new Date() : simpleDateFormat.parse(request.getEndTime());
            } catch (ParseException e) {
                throw new GlobalException(ResultCodeEnum.DATE_FORMAT_ERROR);
            }
            orderList.addAll(orderMapper.queryByGenerationTime(createStart, createEnd));
        }

        // 按条件查询
        if (StrUtil.isNotBlank(request.getOrderId())) {
            orderList.add(orderMapper.queryByOrderId(request.getOrderId()));
        }

        if (StrUtil.isNotBlank(request.getProvince())) {
            List<SiteDo> siteDos = siteMapper.queryByProvince(request.getProvince());
            List<Integer> idList = siteDos.stream().map(site -> site.getId()).collect(Collectors.toList());
            orderList.addAll(orderMapper.queryBySiteIds(idList));
        }

        if (StrUtil.isNotBlank(request.getCity())) {
            List<SiteDo> siteDos = siteMapper.queryByCity(request.getCity());
            List<Integer> idList = siteDos.stream().map(site -> site.getId()).collect(Collectors.toList());
            orderList.addAll(orderMapper.queryBySiteIds(idList));
        }

        if (StrUtil.isNotBlank(request.getSite())) {
            List<SiteDo> siteDos = siteMapper.queryBySiteName(request.getSite());
            List<Integer> idList = siteDos.stream().map(site -> site.getId()).collect(Collectors.toList());
            orderList.addAll(orderMapper.queryBySiteIds(idList));
        }

        if (StrUtil.isNotBlank(request.getRider())) {
            orderList.addAll(orderMapper.queryByRider(request.getRider()));
        }

        if (StrUtil.isNotBlank(request.getCustomer())) {
            orderList.addAll(orderMapper.queryByCustomer(request.getCustomer()));
        }

        if (ObjUtil.isNotEmpty(request.getStatus())) {
            orderList.addAll(orderMapper.queryByStatus(request.getStatus()));
        }

        // 获取所有站点
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        // 站点信息转为map key为站点id
        Map<String, SiteDo> siteMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));
        // 去重 Do -> Vo
        orderList.stream().distinct().forEach(order -> {
            SiteDo site = siteMap.get(order.getSiteId().toString());
            data.add(
                    OrderVo.builder()
                            .orderId(order.getOrderId())
                            .rider(order.getRider() == null ? "暂无骑手接单" : order.getRider())
                            .customer(order.getCustomer())
                            .status(OrderStatusEnum.getByCode(order.getStatus()).getMsg())
                            .generationTime(simpleDateFormat.format(order.getGenerationTime()))
                            .completionTime(order.getCompletionTime() == null ? "订单暂未完成" : simpleDateFormat.format(order.getCompletionTime()))
                            .province(site.getProvince())
                            .city(site.getCity())
                            .site(site.getSiteName())
                            .address(site.getSiteAddress())
                            .lockerId(order.getLockerId())
                            .note(order.getNote())
                            .build());
        });


        return new Result(ResultCodeEnum.QUERY_ORDER_SUCCESS, data);
    }

}
