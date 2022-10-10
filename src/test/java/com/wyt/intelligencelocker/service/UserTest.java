package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.exception.ParameterException;
import com.wyt.intelligencelocker.meta.request.RegisterRequest;
import com.wyt.intelligencelocker.utils.RoleUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/9/19 12:47
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class UserTest {

    @Resource
    private IUserService iUserService;

    @Resource
    private RoleUtil roleUtil;

    @Test
    void insert() throws GlobalException, ParameterException {
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

    @Test
    void testRole() {
        Integer roleByPhone = roleUtil.getRoleByPhone("1803390272");
        System.out.println(roleByPhone);
    }

    @Test
    void register() {
        RegisterRequest registerRequest = new RegisterRequest();

        for (int i = 0; i < 100; i++) {
            Long phone = 18033902799l + i;
            registerRequest.setPhone(phone.toString());
            registerRequest.setPassword("admin123");
            iUserService.register(registerRequest);
        }

    }

}
