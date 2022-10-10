package com.wyt.intelligencelocker.entity.Do;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/10/8 9:54
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class ManagerOperationDo {

    private Integer id;

    private String phone;

    private Integer siteId;

    private Integer newSiteId;

    private Date operationTime;

    private String operationUser;

    private String oerationIp;

}
