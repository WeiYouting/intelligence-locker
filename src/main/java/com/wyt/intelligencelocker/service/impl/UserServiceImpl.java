package com.wyt.intelligencelocker.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alicp.jetcache.anno.Cached;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.async.ServiceAsync;
import com.wyt.intelligencelocker.controller.util.CodeUtil;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.UserLoginMapper;
import com.wyt.intelligencelocker.dao.UserMapper;
import com.wyt.intelligencelocker.dao.UserOperationMapper;
import com.wyt.intelligencelocker.entity.Do.UserDo;
import com.wyt.intelligencelocker.entity.Do.UserLoginDo;
import com.wyt.intelligencelocker.entity.Do.UserOperationDo;
import com.wyt.intelligencelocker.entity.Vo.UserVo;
import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;
import com.wyt.intelligencelocker.meta.enums.UserStatusEnum;
import com.wyt.intelligencelocker.meta.exception.ParameterException;
import com.wyt.intelligencelocker.meta.request.*;
import com.wyt.intelligencelocker.service.IUserService;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/9/19 10:13
 * @Email Wei.youting@qq.com
 */

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ServiceAsync serviceAsync;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserOperationMapper userOperationMapper;

    @Resource
    private CodeUtil codeUtil;

    @Resource
    private UserLoginMapper userLoginMapper;

    @Resource
    private CacheUtil cacheService;

    /**
     * 用户注册
     *
     * @param request
     * @return
     * @throws GlobalException
     * @throws ParameterException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result register(RegisterRequest request) throws GlobalException, ParameterException {
        // 校验参数
        ValidateExtUtil.validate(request);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        // 判断短信验证码是否正确
        if (!request.getCode().equals(codeUtil.check(request.getPhone()))) {
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }

        // 判断用户是否已经存在
        if (checkUserExists(request.getPhone())) {
            throw new GlobalException(ResultCodeEnum.PHONE_REGISTERED);
        }

        // 判断用户名是否为空 若空则随机生成
        if (StrUtil.isBlank(request.getName())) {
            request.setName(RandomCodeUtil.randomUserName());
        }

        // 对密码加密 并插入数据库
        String pwd = SecureUtil.md5(request.getPassword());
        UserDo user = UserDo.builder().phone(request.getPhone()).name(request.getName()).password(pwd)
                .role(UserRoleEnum.USER.getCode())
                .registerTime(new Date())
                .lastOnlineTime(new Date())
                .integral(0.0)
                .status(UserStatusEnum.NORMAL.getCode())
                .build();

        // 判断插入数据是否成功
        if (!(userMapper.insertUser(user) > 0)) {
            throw new GlobalException(ResultCodeEnum.REGISTER_FAIL);
        }

        // 记录信息
        UserOperationDo info = UserOperationDo.builder().phone(request.getPhone()).operationInfo("注册成功")
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(request.getPhone())
                .operationTime(new Date()).build();

        // 判断操作是否记录成功
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // 清除短信验证码缓存
        cacheService.SMSRemove(request.getPhone());
        return new Result(ResultCodeEnum.REGISTER_SUCCESS);
    }

    /**
     * 检测用户是否已经存在
     *
     * @param tel
     * @return
     */
    public boolean checkUserExists(String tel) {
        return userMapper.selectUserNum(tel) > 0;
    }

    /**
     * 用户登录
     *
     * @param request
     * @return
     * @throws GlobalException
     * @throws ParameterException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(LoginRequest request) throws GlobalException, ParameterException {
        // 校验参数
        ValidateExtUtil.validate(request);

        // 获取ip地址
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = requestAttributes.getRequest().getRemoteAddr();

        // 校验密码和验证码是否同时为空
        if (StrUtil.isBlank(request.getPassword()) && StrUtil.isBlank(request.getCode())) {
            throw new GlobalException(ResultCodeEnum.PASSWORD_CODE_NULL);
        }

        UserDo user = null;
        // 判断密码是否为空 为空则使用验证码登录
        if (StrUtil.isBlank(request.getPassword())) {

            // 校验短信验证码是否正确
            if (!request.getCode().equals(codeUtil.check(request.getPhone()))) {
                throw new GlobalException(ResultCodeEnum.CODE_ERROR);
            }

            // 根据手机号查询用户 若不存在则自动注册(随机用户名 空密码)
            user = userMapper.selectByPhoneUser(request.getPhone());
            if (ObjectUtil.isEmpty(user)) {
                user = UserDo.builder().phone(request.getPhone())
                        .name(RandomCodeUtil.randomUserName())
                        .password("")
                        .role(UserRoleEnum.USER.getCode())
                        .registerTime(new Date())
                        .lastOnlineTime(new Date())
                        .integral(0.0)
                        .status(UserStatusEnum.NORMAL.getCode()).build();

                //判断用户是否注册成功
                if (!(userMapper.insertUser(user) > 0)) {
                    return new Result(ResultCodeEnum.LOGIN_FAIL);
                }

                // 记录用户登录
                UserLoginDo loginInfo = UserLoginDo.builder()
                        .phone(request.getPhone())
                        .loginIp(ip)
                        .loginTime(new Date()).build();

                if (!(userLoginMapper.insertRecord(loginInfo) > 0)){
                    throw new GlobalException(ResultCodeEnum.FAIL);
                }

                return new Result(ResultCodeEnum.LOGIN_REGISTER_SUCCESS);
            }

            // 记录用户登录
            UserLoginDo loginInfo = UserLoginDo.builder()
                    .phone(request.getPhone())
                    .loginIp(ip)
                    .loginTime(new Date()).build();

            if (!(userLoginMapper.insertRecord(loginInfo) > 0)){
                throw new GlobalException(ResultCodeEnum.FAIL);
            }
            return new Result(ResultCodeEnum.LOGIN_SUCCESS);
        }

        // 使用密码登录
        user = userMapper.selectByPhoneUser(request.getPhone());

        // 判断用户是否注册
        if (ObjectUtil.isEmpty(user)) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // 判断用户名密码是否正确
        if (!SecureUtil.md5(request.getPassword()).equals(user.getPassword())) {
            throw new GlobalException(ResultCodeEnum.PASSWORD_ERROR);
        }

        // 更新上线时间
        if (!(userMapper.updateLastOnlineTimeByPhoneInteger(request.getPhone(), new Date()) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // 记录用户登录
        UserLoginDo loginInfo = UserLoginDo.builder()
                .phone(request.getPhone())
                .loginIp(ip)
                .loginTime(new Date()).build();

        if (!(userLoginMapper.insertRecord(loginInfo) > 0)){
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        return new Result(ResultCodeEnum.LOGIN_SUCCESS);
    }

    /**
     * 发送短信验证码
     *
     * @param request
     * @return
     * @throws GlobalException
     */
    @Override
    public Result sendCode(SMSCodeRequest request) throws GlobalException {
        // 校验参数
        ValidateExtUtil.validate(request);

        // 判断缓存是否已经存在该手机号 若已存在 则抛出异常
        if (StrUtil.isNotBlank(codeUtil.check(request.getPhone()))) {
            return new Result(ResultCodeEnum.SEND_CODE_ALREADY);
        }

        // 通过工具类获取随机验证码并存入redis
        String code = codeUtil.getCode(request.getPhone());

        // 异步发送短信验证码
        serviceAsync.sendSMSCode(code);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
        return new Result(ResultCodeEnum.SEND_CODE_SUCCESS);
    }


    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changePwd(ChangePwdRequest request) {
        //校验参数
        ValidateExtUtil.validate(request);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        // 判断用户是否存在
        UserDo user = userMapper.selectByPhoneUser(request.getPhone());
        if (ObjectUtil.isEmpty(user)) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // 判断旧密码是否为空 若为空可以直接设置新密码 不为空则需要对比旧密码
        String oldPwd = user.getPassword();
        if (StrUtil.isNotBlank(oldPwd)) {
            if (!(oldPwd.equals(SecureUtil.md5(request.getOldPwd())))) {
                throw new GlobalException(ResultCodeEnum.PASSWORD_ERROR);
            }

            //判断密码是否更新成功
            if (!(userMapper.updatePasswordByPhone(request.getPhone(), SecureUtil.md5(request.getNewPwd())) > 0)) {
                throw new GlobalException(ResultCodeEnum.CHANGE_PASSWORD_FAIL);
            }

            UserOperationDo info = UserOperationDo.builder().phone(request.getPhone()).operationInfo("修改密码")
                    .operationIp(httpServletRequest.getRemoteAddr())
                    .operationUser(request.getPhone())
                    .operationTime(new Date()).build();

            // 判断记录信息是否成功
            if (!(userOperationMapper.insertInfo(info) > 0)) {
                throw new GlobalException(ResultCodeEnum.FAIL);
            }

            // 改密token失效
            cacheService.tokenRemove(request.getPhone());
            return new Result(ResultCodeEnum.CHANGE_PASSWORD_SUCCESS);
        }

        // 旧密码为空 直接修改
        if (!(userMapper.updatePasswordByPhone(request.getPhone(), SecureUtil.md5(request.getNewPwd())) > 0)) {
            return new Result(ResultCodeEnum.CHANGE_PASSWORD_FAIL);
        }

        // 改密token失效
        cacheService.tokenRemove(request.getPhone());
        return new Result(ResultCodeEnum.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 重置密码
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result resetPwd(ResetPwdRequest request) {
        // 校验参数
        ValidateExtUtil.validate(request);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        // 获取redis中的短信验证码并判断验证码是否正确
        if (!(request.getCode().equals(codeUtil.check(request.getPhone())))) {
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }

        // 判断更新密码是否成功
        if (!(userMapper.updatePasswordByPhone(request.getPhone(), SecureUtil.md5(request.getPassword())) > 0)) {
            throw new GlobalException(ResultCodeEnum.CHANGE_PASSWORD_FAIL);
        }

        UserOperationDo info = UserOperationDo.builder().phone(request.getPhone())
                .operationInfo("重置密码")
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(request.getPhone()).operationTime(new Date()).build();

        // 判断信息是否记录成功
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // 改密token失效 验证码失效
        cacheService.tokenRemove(request.getPhone());
        cacheService.SMSRemove(request.getPhone());
        return new Result(ResultCodeEnum.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @Override
    public Result logout(HttpServletRequest request) {
        String token = "";
        String phone = "";
        try {
            // 校验token是否为空
            if (StrUtil.isBlank(token = request.getHeader("token"))) {
                throw new GlobalException(ResultCodeEnum.TOKEN_INVALID);
            }

            // 获取手机号
            phone = jwtUtil.getInfo(token).get("username").toString();

            // 登出token失效
            if (cacheService.tokenRemove(phone)) {
                return new Result(ResultCodeEnum.LOGOUT_SUCCESS);
            }
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.TOKEN_INVALID);
        }

        return new Result(ResultCodeEnum.LOGOUT_FAIL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changeName(ChangeNameRequest request) {
        // 校验参数
        ValidateExtUtil.validate(request);

        // 获取手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String token = httpServletRequest.getHeader("token");
        String phone = jwtUtil.getInfo(token).get("username").toString();

        // 通过手机号查询用户
        UserDo user = userMapper.selectUserByPhone(phone);

        // 判断更新用户名是否成功
        if (!(userMapper.updateNameByPhone(phone, request.getName()) > 0)) {
            throw new GlobalException(ResultCodeEnum.CHANGE_NAME_FAIL);
        }

        UserOperationDo info = UserOperationDo.builder().phone(phone)
                .operationInfo(StrUtil.format(UserOperationUtil.renameTemplate, user.getName(), request.getName()))
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(phone).operationTime(new Date()).build();

        // 判断信息是否记录成功
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        return new Result(ResultCodeEnum.CHANGE_NAME_SUCCESS);
    }
}
