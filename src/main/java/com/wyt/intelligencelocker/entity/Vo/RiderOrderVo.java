package com.wyt.intelligencelocker.entity.Vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author WeiYouting
 * @create 2022/10/9 17:25
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class RiderOrderVo {

    private String orderId;

    private String customer;

    private String status;

    private String generationTime;

    private String province;

    private String city;

    private String site;

    private String address;

    private String note;


}
