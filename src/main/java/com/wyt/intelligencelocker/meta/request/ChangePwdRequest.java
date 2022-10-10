package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/9/27 16:01
 * @Email Wei.youting@qq.com
 *
 * 修改密码
 */
@Data

public class ChangePwdRequest {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

    private String oldPwd;

    @Pattern(regexp = RegexUtil.REGEX_PASSWORD, message = RegexUtil.PASSWORD_MESSAGE)
    private String newPwd;

}
