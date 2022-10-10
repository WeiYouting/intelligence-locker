package com.wyt.intelligencelocker.controller;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.AcceptOrderRequest;
import com.wyt.intelligencelocker.meta.request.QueryOrderRiderRequest;
import com.wyt.intelligencelocker.meta.request.UpdateOrderStatusRequest;
import com.wyt.intelligencelocker.service.IRiderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/10/9 17:17
 * @Email Wei.youting@qq.com
 */
@RestController
@RequestMapping("/rider")
public class RiderController {

    @Resource
    private IRiderService riderService;

    /**
     * 根据城市查询可接订单
     *
     * @param request
     * @return
     */
    @PostMapping("/queryOrder")
    public Result queryOrder(@RequestBody QueryOrderRiderRequest request) {
        return riderService.queryOrder(request);
    }

    /**
     * 接单
     *
     * @param request
     * @return
     */
    @PostMapping("/accept")
    public Result accept(@RequestBody AcceptOrderRequest request) {
        return riderService.accept(request);
    }

    /**
     * 更新订单状态
     *
     * @param request
     * @return
     */
    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody UpdateOrderStatusRequest request) {
        return riderService.updateStatus(request);
    }

}
