package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.ManagerDo;
import org.apache.catalina.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/2 20:37
 * @Email Wei.youting@qq.com
 */
@Repository
@Mapper
public interface ManagerMapper {

    Integer insertManager(@Param("managerDoList")List<ManagerDo> managerDoList);

    List<ManagerDo> selectAllManager();

    ManagerDo queryByPhone(@Param("tel") String tel);

    Integer updateSiteIdByPhone(ManagerDo managerDo);

    Integer deleteByPhone(@Param("tel") String tel);

    List<ManagerDo> queryBySiteIds(@Param("idList")List<Integer> idList);
}
