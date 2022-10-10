package com.wyt.intelligencelocker.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/10/9 16:40
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class RiderTest {

    @Resource
    private RiderMapper riderMapper;

    @Test
    void test1(){
        Integer integer = riderMapper.countByPhone("18659728314");
        System.out.println(integer);
    }

}
