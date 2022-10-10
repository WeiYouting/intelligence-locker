package com.wyt.intelligencelocker.utils;

/**
 * @Author WeiYouting
 * @create 2022/9/28 10:54
 * @Email Wei.youting@qq.com
 * 正则工具类
 */
public class RegexUtil {

    public static final String REGEX_MOBILE = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
    public static final String MOBILE_MESSAGE = "手机号码格式不正确";

    public static final String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
    public static final String PASSWORD_MESSAGE = "密码长度6至16位,且需要包含数字、字母";

    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{4,16}$";
    public static final String USERNAME_MESSAGE = "用户名长度4至16位（字母，数字，下划线，减号）";

}
