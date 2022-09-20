package meta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author WeiYouting
 * @create 2022/9/19 13:44
 * @Email Wei.youting@qq.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Integer id;

    @NotBlank(message = "手机号码格式错误")
    @Length(max = 13,min = 11,message = "手机号码格式错误")
    private String phone;

    private String name;

    @NotBlank(message = "密码格式错误")
    private String password;

    private Integer role;

}
