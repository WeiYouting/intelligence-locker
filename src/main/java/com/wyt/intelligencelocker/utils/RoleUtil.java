package com.wyt.intelligencelocker.utils;

import com.wyt.intelligencelocker.dao.UserMapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/9/28 15:50
 * @Email Wei.youting@qq.com
 * 获取用户权限工具类
 */
@Component
public class RoleUtil {

    @Resource
    private  UserMapper userMapper;

    public  Integer getRoleByPhone(String phone){
        return userMapper.selectRoleByPhone(phone);
    }



}
