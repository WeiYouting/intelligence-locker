package com.wyt.intelligencelocker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/9/19 12:24
 * @Email Wei.youting@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;
    @NotBlank(message = "手机号码不能为空")
    @Length(max = 15,min = 11,message = "手机号码格式不正确")
    private String phone;

    private String name;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Integer role;

    private Date registerTime;

    private Date lastOnlineTime;

    private Double integral;
}
