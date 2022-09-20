package com.wyt.intelligencelocker.controller;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.service.IUserService;
import meta.exception.GlobalException;
import meta.request.LoginRequest;
import meta.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author WeiYouting
 * @create 2022/9/19 14:33
 * @Email Wei.youting@qq.com
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping
    public Result register(@RequestBody RegisterRequest request) throws GlobalException {
        return iUserService.register(request);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request) throws GlobalException {
        return iUserService.login(request);
    }

}
