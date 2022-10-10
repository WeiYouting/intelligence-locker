package com.wyt.intelligencelocker.entity.Do;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/10/8 9:04
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class UserLoginDo {

    private Integer id;

    private String phone;

    private String loginIp;

    private Date loginTime;

}
