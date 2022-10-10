package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/9/21 15:41
 * @Email Wei.youting@qq.com
 *
 * 发送短信验证码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMSCodeRequest {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

}
