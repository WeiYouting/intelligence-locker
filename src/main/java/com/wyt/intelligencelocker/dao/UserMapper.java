package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author WeiYouting
 * @create 2022/9/19 9:59
 * @Email Wei.youting@qq.com
 */

@Mapper
@Repository
public interface UserMapper {

    Integer insertUser(User user);

    Integer selectUserNum(@Param("tel") String tel);

}
