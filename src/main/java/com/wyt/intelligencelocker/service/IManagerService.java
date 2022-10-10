package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.AddRiderRequest;
import com.wyt.intelligencelocker.meta.request.QueryRiderRequest;
import com.wyt.intelligencelocker.meta.request.RemoveRiderRequest;

/**
 * @Author WeiYouting
 * @create 2022/10/9 16:22
 * @Email Wei.youting@qq.com
 */
public interface IManagerService {

    Result addRider(AddRiderRequest request);

    Result removeRider(RemoveRiderRequest request);

    Result queryAll();

    Result queryByPhone(QueryRiderRequest request);
}
