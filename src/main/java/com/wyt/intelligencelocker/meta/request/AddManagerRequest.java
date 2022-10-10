package com.wyt.intelligencelocker.meta.request;

import com.wyt.intelligencelocker.entity.Do.ManagerDo;
import com.wyt.intelligencelocker.utils.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/1 19:43
 * @Email Wei.youting@qq.com
 *
 * 新增站点管理员
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddManagerRequest {

    @Valid
    private List<ManagerDo> userList;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
