package com.wyt.intelligencelocker.entity.Vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/10/9 13:44
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class OrderVo {

    private String orderId;

    private String rider;

    private String customer;

    private String status;

    private String generationTime;

    private String completionTime;

    private String province;

    private String city;

    private String site;

    private String address;

    private Integer lockerId;

    private String note;

}
