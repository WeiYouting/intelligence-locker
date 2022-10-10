package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.SiteDo;
import com.wyt.intelligencelocker.meta.request.QuerySiteRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/2 20:31
 * @Email Wei.youting@qq.com
 */

@Mapper
@Repository
public interface SiteMapper {

    List<SiteDo> queryAllSite();

    List<SiteDo> queryByProvince(@Param("province") String province);

    List<SiteDo> queryByCity(@Param("city") String ciry);

    List<SiteDo> queryBySiteName(@Param("name") String siteName);

    List<SiteDo> queryByDetail(QuerySiteRequest request);
}
