package com.wyt.intelligencelocker.meta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author WeiYouting
 * @create 2022/10/9 14:13
 * @Email Wei.youting@qq.com
 *
 * 查询订单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryOrderRequest {

    private String orderId;

    private String rider;

    private String customer;

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    private String province;

    private String city;

    private String site;

    private Integer lockerId;

    private String note;


}
