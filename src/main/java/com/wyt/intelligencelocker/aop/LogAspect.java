package com.wyt.intelligencelocker.aop;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.exception.ParameterException;
import com.wyt.intelligencelocker.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @Author WeiYouting
 * @create 2022/9/20 13:52
 * @Email Wei.youting@qq.com
 * <p>
 * 统计业务层日志
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Resource
    private JwtUtil jwtUtil;

    // 声明切入点
    @Pointcut(value = "execution(* com.wyt.intelligencelocker.service.impl..*(..))")
    public void serviceLog() {
    }

    /**
     * @param joinPoint 前置通知
     *                  记录请求信息
     *                  ip地址 url路径 请求参数 请求方法及用户信息
     */
    @Before(value = "serviceLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        // 日志记录ip 请求路径和请求方法
        log.info(StrUtil.format("IP:{}", request.getRemoteAddr()));
        log.info(StrUtil.format("URL:{}\t{}", request.getRequestURL().toString(), request.getMethod()));
        // 获取用户token 若为空 则不记录操作人
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            try {
                // 判断参数是否为空 不为空则记录参数
                String user = jwtUtil.getInfo(token).get("username").toString();
                String logInfo = "";
                if (ArrayUtil.isNotEmpty(args)) {
                    logInfo = StrUtil.isNotBlank(user) ? StrUtil.format("User:{}\targs:{}", user, args) : StrUtil.format("args:{}", args);
                } else {
                    logInfo = StrUtil.isNotBlank(user) ? StrUtil.format("User:{}", user) : logInfo;
                }
                log.info(logInfo);
            } catch (Exception e) {
                throw new GlobalException(ResultCodeEnum.FAIL);
            }
        }
    }

    /**
     * @param ret 后置通知
     *            记录返回信息
     */
    @AfterReturning(returning = "ret", pointcut = "serviceLog()")
    public void doAfterReturning(Object ret) {
        if (ret instanceof Result) {
            Result res = (Result) ret;
            log.info("Response:{}\r\n", res.getMsg());
        } else {
            log.info("Response:{}\r\n", ret);
        }
    }

    /**
     * @param ex 异常通知
     */
    @AfterThrowing(throwing = "ex", pointcut = "serviceLog()")
    public void doThrowing(Throwable ex) {
        if (ex instanceof GlobalException) {
            //  通过ResultCodeEnum的code获取错误信息
            log.warn("Error:{}\r\n", ResultCodeEnum.getByCode(Integer.parseInt(ex.getMessage())).getMsg());
        } else if (ex instanceof ParameterException) {
            log.warn("Error:{}\r\n", ex.getMessage());
        }
    }
}
