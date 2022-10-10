package com.wyt.intelligencelocker.meta.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author WeiYouting
 * @create 2022/10/9 10:07
 * @Email Wei.youting@qq.com
 *
 * 创建订单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderRequest {

    private Integer siteId;

    private String note;

}
