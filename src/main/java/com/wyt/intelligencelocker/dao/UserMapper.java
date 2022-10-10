package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.AddIntegralDo;
import com.wyt.intelligencelocker.entity.Do.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/9/19 9:59
 * @Email Wei.youting@qq.com
 */

@Mapper
@Repository
public interface UserMapper {

    Integer insertUser(UserDo user);

    Integer selectUserNum(@Param("tel") String tel);

    UserDo selectByPhoneUser(@Param("tel") String tel);

    Integer selectNumberByPhones(@Param("tels") List<String> tels);

    List<UserDo> selectAllUser();

    UserDo selectUserByPhone(@Param("tel") String tel);

    Integer updatePasswordByPhone(@Param("tel") String tel,@Param("pwd") String pwd);

    Integer selectRoleByPhone(@Param("tel") String tel);

    Integer updateNameByPhone(@Param("tel") String tel,@Param("name") String name);

    Integer updateManager(@Param("tels") List<String> tels);

    List<Integer> selectRoleByPhones(@Param("tels") List<String> tels);

    List<String> selectAllAdmin();

    Integer updateLastOnlineTimeByPhoneInteger(@Param("tel") String tel, @Param("time")Date date);

    Integer updateUserRole(@Param("role") Integer role,@Param("tel") String phone);

    Integer updateIntegral(AddIntegralDo ids);
}
