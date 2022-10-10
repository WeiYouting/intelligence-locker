package com.wyt.intelligencelocker.meta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author WeiYouting
 * @create 2022/10/2 20:16
 * @Email Wei.youting@qq.com
 *
 * 查询站点管理员
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryManagerRequest {

    private String phone;

    private String province;

    private String city;

    private String siteName;

}
