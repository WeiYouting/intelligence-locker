package com.wyt.intelligencelocker.controller;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.AddOrderRequest;
import com.wyt.intelligencelocker.meta.request.QueryOrderRequest;
import com.wyt.intelligencelocker.meta.request.QuerySiteRequest;
import com.wyt.intelligencelocker.service.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:26
 * @Email Wei.youting@qq.com
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 根据地址查询站点
     * @param request
     * @return
     */
    @PostMapping("/querySite")
    public Result querySite(@RequestBody QuerySiteRequest request){
        return orderService.querySite(request);
    }

    /**
     * 创建订单
     * @param request
     * @return
     */
    @PostMapping("/create")
    public Result createOrder(@RequestBody AddOrderRequest request){
        return orderService.createOrder(request);
    }

    /**
     * 查询所有订单
     * @return
     */
    @PostMapping("/queryAll/{index}")
    public Result queryAllOrder(@PathVariable Integer index){
        return orderService.queryAllOrder(index);
    }

    /**
     * 按条件查询订单
     * @param request
     * @return
     */
    @PostMapping("/query")
    public Result queryOrder(@RequestBody QueryOrderRequest request){
        return orderService.query(request);
    }

}
