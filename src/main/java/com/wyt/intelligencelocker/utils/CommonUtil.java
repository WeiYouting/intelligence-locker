package com.wyt.intelligencelocker.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @Author WeiYouting
 * @create 2022/10/9 17:24
 * @Email Wei.youting@qq.com
 */
public class CommonUtil {

    public static String desensitizedPhoneNumber(String phoneNumber){
        if(StrUtil.isNotEmpty(phoneNumber)){
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }

}
