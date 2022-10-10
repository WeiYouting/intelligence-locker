package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.AddOrderRequest;
import com.wyt.intelligencelocker.meta.request.QueryOrderRequest;
import com.wyt.intelligencelocker.meta.request.QuerySiteRequest;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:34
 * @Email Wei.youting@qq.com
 */
public interface IOrderService {
    Result querySite(QuerySiteRequest request);

    Result createOrder(AddOrderRequest request);

    Result queryAllOrder(Integer index);

    Result query(QueryOrderRequest request);
}
