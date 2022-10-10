package com.wyt.intelligencelocker.meta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author WeiYouting
 * @create 2022/10/10 9:37
 * @Email Wei.youting@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {

    private String orderId;

    private Integer status;

}
