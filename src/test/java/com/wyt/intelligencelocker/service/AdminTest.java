package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/10/2 23:17
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class AdminTest {

    @Resource
    AdminServiceImpl adminService;

    @Test
    public void test1(){
        adminService.getAllUsers(3);
    }
}
