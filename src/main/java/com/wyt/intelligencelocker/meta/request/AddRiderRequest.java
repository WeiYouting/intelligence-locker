package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/10/9 16:20
 * @Email Wei.youting@qq.com
 * 添加骑手
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRiderRequest {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

}
