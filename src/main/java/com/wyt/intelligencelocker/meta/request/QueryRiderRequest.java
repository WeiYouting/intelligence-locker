package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/10/10 13:55
 * @Email Wei.youting@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRiderRequest {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

}
