package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/9/19 13:44
 * @Email Wei.youting@qq.com
 *
 * 用户注册
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

    private String name;

    @Pattern(regexp = RegexUtil.REGEX_PASSWORD, message = RegexUtil.PASSWORD_MESSAGE)
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;


}
