package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.UserOperationDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/9/29 13:23
 * @Email Wei.youting@qq.com
 */
@Mapper
@Repository
public interface UserOperationMapper {

   Integer insertInfo(UserOperationDo userOperationDo);

   Integer insertInfos(@Param("info") List<UserOperationDo> userOperationDo);

}
