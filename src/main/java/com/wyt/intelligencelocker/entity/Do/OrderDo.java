package com.wyt.intelligencelocker.entity.Do;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedNotifications;

import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:18
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDo {

    private Integer id;

    private String orderId;

    private String rider;

    private String customer;

    private Integer status;

    private Date generationTime;

    private Date completionTime;

    private Integer siteId;

    private String note;

    private Integer lockerId;
}
