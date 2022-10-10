package com.wyt.intelligencelocker.utils;

import com.wyt.intelligencelocker.meta.exception.ParameterException;
import org.hibernate.validator.HibernateValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author WeiYouting
 * @create 2022/9/19 13:11
 * @Email Wei.youting@qq.com
 * 校验参数工具类
 */
public class ValidateExtUtil {

    private static Validator validation = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(true).buildValidatorFactory().getValidator();

    public static <T> void validate(T obj) throws ParameterException {
        Set<ConstraintViolation<T>> constraintViolations = validation.validate(obj);
        if (constraintViolations.size() > 0) {
            ConstraintViolation<T> item = constraintViolations.iterator().next();
            throw new ParameterException(item.getMessage());
        }
    }
}


