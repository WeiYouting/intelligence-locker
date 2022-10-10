package com.wyt.intelligencelocker.entity.Do;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/9/29 13:26
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOperationDo {

    private Integer id;

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

    private String operationInfo;

    private String operationIp;

    private String operationUser;

    private Date operationTime;

}
