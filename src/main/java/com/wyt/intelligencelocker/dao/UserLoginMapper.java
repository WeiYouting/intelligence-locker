package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.UserLoginDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author WeiYouting
 * @create 2022/10/8 9:06
 * @Email Wei.youting@qq.com
 */
@Repository
@Mapper
public interface UserLoginMapper {

    Integer insertRecord(UserLoginDo userLoginDo);

}
