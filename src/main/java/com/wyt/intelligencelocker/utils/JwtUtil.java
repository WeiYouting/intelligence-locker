package com.wyt.intelligencelocker.utils;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/9/22 9:25
 * @Email Wei.youting@qq.com
 */
@Component
public class JwtUtil {

    @Resource
    private CacheUtil cacheService;

    /**
     * 过期5分钟(不做处理 交由redis控制)
     */
//    private static final long EXPIRE_TIME = 5 * 60 * 1000;
//    private static final long EXPIRE_TIME = 30 * 1000;

    /**
     * jwt密钥
     */
    private static final String SECRET = "jwt_secret";

    /**
     * 生成jwt字符串，五分钟后过期  JWT(json web token)
     *
     * @param userId
     * @param info,Map的value只能存放值的类型为：Map，List，Boolean，Integer，Long，Double，String and Date
     * @return
     */
    public String sign(String userId, Map<String, String> info) {
        try {
//            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    //将userId保存到token里面
                    .withAudience(userId)
                    //存放自定义数据
                    .withClaim("info", info)
                    //五分钟后token过期
//                    .withExpiresAt(date)
                    //token的密钥
                    .sign(algorithm);
            //将token存入redis key为手机号码 value为token 过期时间15分钟
            cacheService.tokenPut(info.get("username"), token);
            return token;
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }
    }

    /**
     * 根据token获取userId
     *
     * @param token
     * @return
     */
    public String getUserId(String token) throws GlobalException {
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return userId;
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.LOGIN_EXPIRE, e.getMessage());
        }
    }

    /**
     * 根据token获取自定义数据info
     *
     * @param token
     * @return
     */
    public Map<String, Object> getInfo(String token) {
        try {
            return JWT.decode(token).getClaim("info").asMap();
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public boolean checkSign(String token, String phone) {
        try {
//            Algorithm algorithm = Algorithm.HMAC256(SECRET);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .build();
//            verifier.verify(token);

            // 从缓存中获取token并于传入的token对比
            String cacheToken = cacheService.tokenGet(phone);
            if (StrUtil.isNotEmpty(cacheToken) && cacheToken.equals(token)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }
    }
}


