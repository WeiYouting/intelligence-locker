package com.wyt.intelligencelocker.entity.Vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author WeiYouting
 * @create 2022/10/8 12:52
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class ManagerVo {

    private String phone;

    private String siteName;

    private String province;

    private String city;

    private String siteAddress;

}
