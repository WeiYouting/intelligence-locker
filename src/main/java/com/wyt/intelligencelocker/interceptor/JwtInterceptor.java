package com.wyt.intelligencelocker.interceptor;

import cn.hutool.core.util.StrUtil;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author WeiYouting
 * @create 2022/9/22 10:02
 * @Email Wei.youting@qq.com
 *
 * 拦截器校验用户是否登录
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    // 执行方法前拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = "";
        String phone = "";
        // token为空则抛出异常
        if (StrUtil.isBlank(token = request.getHeader("token"))) {
            throw new GlobalException(ResultCodeEnum.TOKEN_INVALID);
        }

        // 获取手机号码失败抛出异常
        try {
            phone = jwtUtil.getInfo(token).get("username").toString();
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.TOKEN_INVALID);
        }

        //判断用户是否登录
        if (jwtUtil.checkSign(token, phone)) {
            return true;
        }
        // 若用户未登录 则抛出异常
        throw new GlobalException(ResultCodeEnum.LOGIN_EXPIRE);
    }
}
