package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/9/26 15:12
 * @Email Wei.youting@qq.com
 *
 * 查找用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUser {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

}
