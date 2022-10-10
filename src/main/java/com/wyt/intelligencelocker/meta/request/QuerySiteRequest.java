package com.wyt.intelligencelocker.meta.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:30
 * @Email Wei.youting@qq.com
 *
 * 查询站点
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuerySiteRequest {

    private String province;

    private String city;

    private String address;


}
