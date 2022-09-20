package com.wyt.intelligencelocker.service;

import meta.exception.GlobalException;
import meta.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author WeiYouting
 * @create 2022/9/19 12:47
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class UserTest {

    @Autowired
    private IUserService iUserService;

    @Test
    void insert() throws GlobalException {
        RegisterRequest user = new RegisterRequest();
//        User user = new User();
//        user.setName("张三");
        user.setPassword("0101001");
        user.setPhone("18033902728");
//        user.setLastOnlineTime(new Date());
//        user.setRegisterTime(new Date());
//        user.setRole(1);
//        user.setIntegral(10.0);
        iUserService.register(user);

//        user.setPhone("1110");
//        user.setPassword("110");
//        iUserService.register(user);

    }

}
