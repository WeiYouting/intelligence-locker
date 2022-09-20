package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;
import meta.exception.GlobalException;
import meta.request.LoginRequest;
import meta.request.RegisterRequest;

/**
 * @Author WeiYouting
 * @create 2022/9/19 10:12
 * @Email Wei.youting@qq.com
 */
public interface IUserService {

    Result register(RegisterRequest request) throws GlobalException;

    boolean checkUserExists(String tel);

    Result login(LoginRequest request) throws GlobalException;

}
