package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.AcceptOrderRequest;
import com.wyt.intelligencelocker.meta.request.QueryOrderRiderRequest;
import com.wyt.intelligencelocker.meta.request.UpdateOrderStatusRequest;

/**
 * @Author WeiYouting
 * @create 2022/10/9 17:17
 * @Email Wei.youting@qq.com
 */
public interface IRiderService {
    Result queryOrder(QueryOrderRiderRequest request);

    Result updateStatus(UpdateOrderStatusRequest request);

    Result accept(AcceptOrderRequest request);
}
