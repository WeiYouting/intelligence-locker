package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.AddIntegralDo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/1 22:46
 * @Email Wei.youting@qq.com
 */

@SpringBootTest
public class UserTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void test2(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("18033902728");
        strings.add("18033902713");
        strings.add("18033902714");
        strings.add("213123213");
        System.out.println(userMapper.selectRoleByPhones(strings));

        System.out.println(userMapper.selectNumberByPhones(strings));
    }

    @Test
    void test1(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("18033902728");
        strings.add("18033902713");
        strings.add("18033902714");
//        strings.add("213123213");
        List<Integer> roleList = userMapper.selectRoleByPhones(strings);
        HashMap<String, Integer> roleMap = new HashMap<>();
        Iterator<Integer> iterator = roleList.iterator();
        strings.forEach(phone->{
            roleMap.put(phone,iterator.next());
        });

        System.out.println(roleMap);
    }

    @Test
    void test3(){
        List<String> strings = userMapper.selectAllAdmin();
        System.out.println(strings);
    }

    @Test
    void test4(){
        ArrayList<AddIntegralDo> integralDos = new ArrayList<>();
        integralDos.add(AddIntegralDo.builder().phone("18033902718").integral(10.0).build());
        integralDos.add(AddIntegralDo.builder().phone("18033902728").integral(5.0).build());
//        userMapper.updateIntegral(integralDos);
    }

}
