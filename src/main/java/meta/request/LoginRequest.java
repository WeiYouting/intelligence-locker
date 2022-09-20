package meta.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author WeiYouting
 * @create 2022/9/19 17:22
 * @Email Wei.youting@qq.com
 */

@Data
public class LoginRequest {

    @NotBlank
    private String phone;
    private String password;
    private String code;
}
