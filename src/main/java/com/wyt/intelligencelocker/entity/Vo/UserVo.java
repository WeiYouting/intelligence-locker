package com.wyt.intelligencelocker.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WeiYouting
 * @create 2022/9/29 13:49
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {

    private String phone;

    private String name;

    private String role;

    private String registerTime;

    private String lastOnlineTime;

    private Double integral;

    private String status;

}
