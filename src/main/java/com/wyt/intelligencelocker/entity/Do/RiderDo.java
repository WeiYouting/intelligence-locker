package com.wyt.intelligencelocker.entity.Do;

import lombok.Builder;
import lombok.Data;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:14
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class RiderDo {

    private Integer id;

    private String phone;

    private Integer orderNum;

    private Double comment;

    private Integer level;

}
