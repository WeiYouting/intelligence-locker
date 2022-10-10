package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;

/**
 * @Author WeiYouting
 * @create 2022/10/10 9:06
 * @Email Wei.youting@qq.com
 */
public interface ICustomerService {
    Result queryAllOrder(Integer index);

    Result queryUnfinshed();

    Result get();
}
