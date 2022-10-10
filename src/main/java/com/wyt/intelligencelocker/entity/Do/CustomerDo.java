package com.wyt.intelligencelocker.entity.Do;

import lombok.Builder;
import lombok.Data;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:10
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class CustomerDo {

    private Integer id;

    private String phone;

    private String province;

    private String city;

    private String address;

    private String level;

}
