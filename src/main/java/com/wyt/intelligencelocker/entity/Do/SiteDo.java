package com.wyt.intelligencelocker.entity.Do;

import lombok.Builder;
import lombok.Data;

/**
 * @Author WeiYouting
 * @create 2022/10/2 20:32
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class SiteDo {

    private Integer id;

    private String siteName;

    private String siteAddress;

    private String province;

    private String city;



}
