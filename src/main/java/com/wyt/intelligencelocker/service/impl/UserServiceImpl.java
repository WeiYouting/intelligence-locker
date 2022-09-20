package com.wyt.intelligencelocker.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.UserMapper;
import com.wyt.intelligencelocker.entity.User;
import com.wyt.intelligencelocker.service.IUserService;
import meta.enums.ResultCode;
import meta.request.LoginRequest;
import meta.request.RegisterRequest;
import meta.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ValidateExtUtil;

import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/9/19 10:13
 * @Email Wei.youting@qq.com
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result register(RegisterRequest request) throws GlobalException {
        ValidateExtUtil.validate(request);
        ValidateExtUtil.validateUser(request);
        if (checkUserExists(request.getPhone())) {
            throw new GlobalException("该手机号已被注册");
        }
        String pwd = SecureUtil.md5(request.getPassword());
        User user = new User(null, request.getPhone(), request.getName(), pwd, request.getRole(), new Date(), new Date(), 0.0);
        if (userMapper.insertUser(user) > 0) {
            return new Result(ResultCode.REGISTER_SUCCESS);
        }

        return new Result(ResultCode.REGISTER_FAIL);
    }

    @Override
    public boolean checkUserExists(String tel) {
        return userMapper.selectUserNum(tel) > 0;
    }

    @Override
    public Result login(LoginRequest request) throws GlobalException {
//        ValidateExtUtil.validate(request);
        StrUtil.isBlank(request.getPassword());

        return null;
    }
}
