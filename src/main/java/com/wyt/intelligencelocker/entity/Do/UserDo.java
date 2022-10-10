package com.wyt.intelligencelocker.entity.Do;

import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/9/19 12:24
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDo implements Serializable {

    private Integer id;

    @Pattern(regexp = RegexUtil.REGEX_MOBILE, message = RegexUtil.MOBILE_MESSAGE)
    private String phone;

    private String name;

    private String password;

    private Integer role;

    private Date registerTime;

    private Date lastOnlineTime;

    private Double integral;

    private Integer status;
}
