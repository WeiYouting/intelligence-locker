package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.UserOperationDo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/9/29 14:02
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class OperationTest {

    @Resource
    private UserOperationMapper userOperationMapper;

    @Test
    void test1(){
        UserOperationDo info1 = UserOperationDo.builder().phone("11111111").operationInfo("test").operationIp("192.168.1.1")
                .operationUser("22222222").operationTime(new Date()).build();

        userOperationMapper.insertInfo(info1);
    }



}
