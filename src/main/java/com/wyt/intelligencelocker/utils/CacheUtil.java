package com.wyt.intelligencelocker.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author WeiYouting
 * @create 2022/9/28 22:07
 * @Email Wei.youting@qq.com
 *
 * 缓存工具类
 */

@Component
public class CacheUtil {


    // 创建缓存 15分钟失效 token缓存
    @CreateCache(name = "tokenCache::", expire = 15 * 60, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.REMOTE)
    private Cache<String, String> tokenCache;

    public boolean tokenRemove(String phone) {
        try {
            return tokenCache.remove(phone);
        } catch (Exception e) {
            return false;
        }
    }

    public void tokenPut(String user, String token) {
        tokenCache.put(user, token);
    }

    public String tokenGet(String phone) {
        return tokenCache.get(phone);
    }




    // 缓存有效期120秒 验证码缓存
    @CreateCache(name = "SMSCache::", expire = 120, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.REMOTE)
    private Cache<String, String> SMSCache;

    public void SMSPut(String tel,String value){
        SMSCache.put(tel,value);
    }

    public String SMSGet(String phone) {
        return SMSCache.get(phone);
    }

    public boolean SMSRemove(String phone) {
        try {
            return SMSCache.remove(phone);
        } catch (Exception e) {
            return false;
        }
    }

}
