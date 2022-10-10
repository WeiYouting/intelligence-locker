package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.ManagerDo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/2 21:30
 * @Email Wei.youting@qq.com
 */
@SpringBootTest
public class ManagerTest {

    @Resource
    ManagerMapper managerMapper;

    @Test
    void  test1(){
        ArrayList<ManagerDo> managerDoArrayList = new ArrayList<ManagerDo>();
        managerDoArrayList.add(ManagerDo.builder().phone("18033902723").siteId(1).build());
        managerDoArrayList.add(ManagerDo.builder().phone("18033902724").siteId(2).build());
        managerDoArrayList.add(ManagerDo.builder().phone("18033902724").build());
        Integer integer = managerMapper.insertManager(managerDoArrayList);
        System.out.println(integer);
    }

    @Test
    void test2(){
        List<ManagerDo> managerDos = managerMapper.selectAllManager();
        System.out.println(managerDos);
    }
}
