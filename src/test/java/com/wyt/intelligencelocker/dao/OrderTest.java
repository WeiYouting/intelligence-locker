package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.meta.request.QuerySiteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:54
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class OrderTest {

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void test1(){
        QuerySiteRequest req = QuerySiteRequest.builder().province("福建").city("福州").address("甘").build();
        System.out.println(siteMapper.queryByDetail(req));
    }

    @Test
    void test2(){
        System.out.println(orderMapper.countByOrderIdInteger("IL20221009110018167560674227594311"));
    }

}
