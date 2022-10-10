package com.wyt.intelligencelocker.aop;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.utils.CacheUtil;
import com.wyt.intelligencelocker.utils.JwtUtil;
import com.wyt.intelligencelocker.utils.RoleUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @Author WeiYouting
 * @create 2022/9/28 15:28
 * @Email Wei.youting@qq.com
 *
 * 用户操作鉴权
 */
@Component
@Aspect
public class PermissionAspect {


    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RoleUtil roleUtil;

    @Resource
    private CacheUtil cacheUtil;

    @Pointcut(value = "@annotation(com.wyt.intelligencelocker.annotation.CheckPermission)")
    public void permission() {
    }

    // 用户token
    String token = null;

    // 用户手机号
    String user = null;

    /**
     * @param joinPoint 前置通知鉴权 若未获取到用户或权限不足时抛出异常
     */
    @Before(value = "permission()")
    public void checkPermission(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        token = request.getHeader("token");
        try {
            // 通过token获取用户
            user = StrUtil.isBlank(token) ? "" : jwtUtil.getInfo(token).get("username").toString();
            // 获取自定义鉴权注解
            CheckPermission permission = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(CheckPermission.class);
            // 若未获取到用户 则抛出异常
            if (StrUtil.isNotBlank(user)) {
                //获取用户的权限
                Integer role = roleUtil.getRoleByPhone(user);
                Integer methodRole = permission.value().getCode();
                //若用户权限为空 或未达到方法标注的权限 则抛出权限不足异常
                if (ObjUtil.isEmpty(role) || role.compareTo(methodRole) > 0) {
                    throw new GlobalException(ResultCodeEnum.DONT_HAVE_PERMISSION);
                }
            } else {
                throw new GlobalException(ResultCodeEnum.TOKEN_INVALID);
            }
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.DONT_HAVE_PERMISSION);
        }
    }

    /**
     * @param ret 后置通知
     *            token续签
     */
    @AfterReturning(returning = "ret", pointcut = "permission()")
    public void doAfterReturning(Object ret) {
        cacheUtil.tokenPut(user, token);
    }

}

