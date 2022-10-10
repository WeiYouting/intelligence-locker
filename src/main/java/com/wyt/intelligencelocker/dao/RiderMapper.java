package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.RiderDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:16
 * @Email Wei.youting@qq.com
 */
@Repository
@Mapper
public interface RiderMapper {

    Integer insertRider(RiderDo rider);

    Integer countByPhone(@Param("tel") String tel);

    Integer removeByPhone(@Param("tel") String phone);

    List<RiderDo> queryAllRider();

    RiderDo queryByPhone(@Param("tel") String phone);
}
