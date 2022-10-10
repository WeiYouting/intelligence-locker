package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.*;

import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/1 19:31
 * @Email Wei.youting@qq.com
 */
public interface IAdminService {

    Result getAllUsers(Integer index);

    Result getUser(QueryUser request);

    Result addManager(AddManagerRequest request);

    Result queryManager(QueryManagerRequest request);

    Result distribution(ChangeDistributionRequest request);

    Result removeManager(RemoveManagerRequest request);

    Result getAllManagers(Integer index);
}
