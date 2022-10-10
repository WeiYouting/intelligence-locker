package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.ManagerOperationDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author WeiYouting
 * @create 2022/10/8 9:53
 * @Email Wei.youting@qq.com
 */
@Repository
@Mapper
public interface ManagerOperationMapper {

    Integer insertInfo(ManagerOperationDo managerOperationDo);

}
