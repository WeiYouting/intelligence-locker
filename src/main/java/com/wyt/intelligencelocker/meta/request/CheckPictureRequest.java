package com.wyt.intelligencelocker.meta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author WeiYouting
 * @create 2022/9/21 13:28
 * @Email Wei.youting@qq.com
 *
 * 检测图片验证码
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPictureRequest {

    @NotBlank(message = "验证码不能为空")
    private String code;

}
