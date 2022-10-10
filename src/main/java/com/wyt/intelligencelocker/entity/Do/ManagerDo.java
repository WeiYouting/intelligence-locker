package com.wyt.intelligencelocker.entity.Do;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/10/2 20:36
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class ManagerDo {

    private Integer id;

    @Pattern(regexp = RegexUtil.REGEX_MOBILE,message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

    private Integer siteId;
}
