package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author WeiYouting
 * @create 2022/10/8 9:33
 * @Email Wei.youting@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDistributionRequest {

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

    @NotNull(message = "站点不能为空")
    private Integer siteId;

}
