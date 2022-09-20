package com.wyt.intelligencelocker.controller.util;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @Author WeiYouting
 * @create 2022/9/18 19:47
 * @Email Wei.youting@qq.com
 */
@Component
public class CodeUtil {
    private static final String[] patch = {"","00000", "0000", "000", "00", "0", "0"};

    public  String sendCode(String tel) {
        int hash = tel.hashCode();
        int key = 20020908;
        long result = hash ^ key;
        long nowTime = System.currentTimeMillis();
        result = result ^ nowTime;
        long code = result % 1000000;
        code = code < 0 ? -code : code;
        String codeStr = code + "";
        int len = codeStr.length();
        return patch[len] + codeStr;
    }

    @Cacheable(value = "code" ,key = "#tel")
    public String getCode(String tel){
        return null;
    }
}
