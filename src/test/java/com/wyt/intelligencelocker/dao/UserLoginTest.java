package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.UserLoginDo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/10/8 9:12
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class UserLoginTest {

    @Resource
    UserLoginMapper userLoginMapper;

    @Test
    void test1() {
        UserLoginDo login = UserLoginDo.builder()
                .loginIp("127.0.0.1")
                .phone("18033902728")
                .loginTime(new Date()).build();
        System.out.println(userLoginMapper.insertRecord(login));;
    }

}
