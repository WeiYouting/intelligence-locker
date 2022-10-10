package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/9/29 13:04
 * @Email Wei.youting@qq.com
 *
 * 修改用户名
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeNameRequest {

    @Pattern(regexp = RegexUtil.REGEX_USERNAME,message = RegexUtil.USERNAME_MESSAGE)
    private String name;

}
