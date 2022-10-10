package com.wyt.intelligencelocker.controller.util;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.wyt.intelligencelocker.utils.CacheUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author WeiYouting
 * @create 2022/9/18 19:47
 * @Email Wei.youting@qq.com
 *
 * 生成随机短信验证码
 * 使用redis缓存、校验换证码
 */
@Component
public class CodeUtil {

    private static final String[] patch = {"00000", "0000", "0000", "000", "00", "0", ""};

    @Resource
    private CacheUtil cacheService;

    /**
     * @param tel
     * @return 生成验证码 key为手机号码 value为验证码 存入redis
     */
    //    @CachePut(value = "code",key = "#tel")
    public String getCode(String tel) {
        int hash = tel.hashCode();
        int key = 20020908;
        long result = hash ^ key;
        long nowTime = System.currentTimeMillis();
        result = result ^ nowTime;
        long code = result % 1000000;
        code = code < 0 ? -code : code;
        String codeStr = code + "";
        int len = codeStr.length();
        String cacheCode = patch[len] + codeStr;
        cacheService.SMSPut(tel, cacheCode);
        return cacheCode;
    }

    /**
     * @param tel
     * @return 通过手机号码获取缓存中的value
     */
    //    @Cacheable(value = "code",key = "#tel")
    public String check(String tel) {
        return cacheService.SMSGet(tel);
    }
}
