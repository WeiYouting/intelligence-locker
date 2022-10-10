package com.wyt.intelligencelocker.annotation;

import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author WeiYouting
 * @create 2022/9/28 16:07
 * @Email Wei.youting@qq.com
 *
 * 自定义鉴权注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {

    UserRoleEnum value();

}
