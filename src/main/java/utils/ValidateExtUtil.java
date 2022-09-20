package utils;

import meta.request.RegisterRequest;
import meta.exception.GlobalException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author WeiYouting
 * @create 2022/9/19 13:11
 * @Email Wei.youting@qq.com
 */
public class ValidateExtUtil {
    //正则校验手机号 密码
    public static final String REGEX_MOBILE = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
    public static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";


    private static Validator validation = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(true).buildValidatorFactory().getValidator();

    public static <T> void validate(T obj) throws GlobalException {
        Set<ConstraintViolation<T>> constraintViolations = validation.validate(obj);
        if (constraintViolations.size() > 0) {
            ConstraintViolation<T> item = constraintViolations.iterator().next();
            throw new GlobalException(String.format("参数校验失败:[%s] %s", item.getPropertyPath(), item.getMessage()));
        }
    }

    public static void validateUser(RegisterRequest obj) throws GlobalException {

        if (!obj.getPhone().matches(REGEX_MOBILE)) {
            throw new GlobalException("手机号码格式不正确");
        }
        if (!obj.getPassword().matches(REGEX_PASSWORD)) {
            throw new GlobalException("密码格式不正确！ 必须包含数字和字母 长度6至16位");
        }

    }
}


