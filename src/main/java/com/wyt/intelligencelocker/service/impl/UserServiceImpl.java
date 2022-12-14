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
     * ????????????
     *
     * @param request
     * @return
     * @throws GlobalException
     * @throws ParameterException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result register(RegisterRequest request) throws GlobalException, ParameterException {
        // ????????????
        ValidateExtUtil.validate(request);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        // ?????????????????????????????????
        if (!request.getCode().equals(codeUtil.check(request.getPhone()))) {
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }

        // ??????????????????????????????
        if (checkUserExists(request.getPhone())) {
            throw new GlobalException(ResultCodeEnum.PHONE_REGISTERED);
        }

        // ??????????????????????????? ?????????????????????
        if (StrUtil.isBlank(request.getName())) {
            request.setName(RandomCodeUtil.randomUserName());
        }

        // ??????????????? ??????????????????
        String pwd = SecureUtil.md5(request.getPassword());
        UserDo user = UserDo.builder().phone(request.getPhone()).name(request.getName()).password(pwd)
                .role(UserRoleEnum.USER.getCode())
                .registerTime(new Date())
                .lastOnlineTime(new Date())
                .integral(0.0)
                .status(UserStatusEnum.NORMAL.getCode())
                .build();

        // ??????????????????????????????
        if (!(userMapper.insertUser(user) > 0)) {
            throw new GlobalException(ResultCodeEnum.REGISTER_FAIL);
        }

        // ????????????
        UserOperationDo info = UserOperationDo.builder().phone(request.getPhone()).operationInfo("????????????")
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(request.getPhone())
                .operationTime(new Date()).build();

        // ??????????????????????????????
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // ???????????????????????????
        cacheService.SMSRemove(request.getPhone());
        return new Result(ResultCodeEnum.REGISTER_SUCCESS);
    }

    /**
     * ??????????????????????????????
     *
     * @param tel
     * @return
     */
    public boolean checkUserExists(String tel) {
        return userMapper.selectUserNum(tel) > 0;
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     * @throws GlobalException
     * @throws ParameterException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(LoginRequest request) throws GlobalException, ParameterException {
        // ????????????
        ValidateExtUtil.validate(request);

        // ??????ip??????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = requestAttributes.getRequest().getRemoteAddr();

        // ??????????????????????????????????????????
        if (StrUtil.isBlank(request.getPassword()) && StrUtil.isBlank(request.getCode())) {
            throw new GlobalException(ResultCodeEnum.PASSWORD_CODE_NULL);
        }

        UserDo user = null;
        // ???????????????????????? ??????????????????????????????
        if (StrUtil.isBlank(request.getPassword())) {

            // ?????????????????????????????????
            if (!request.getCode().equals(codeUtil.check(request.getPhone()))) {
                throw new GlobalException(ResultCodeEnum.CODE_ERROR);
            }

            // ??????????????????????????? ???????????????????????????(??????????????? ?????????)
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

                //??????????????????????????????
                if (!(userMapper.insertUser(user) > 0)) {
                    return new Result(ResultCodeEnum.LOGIN_FAIL);
                }

                // ??????????????????
                UserLoginDo loginInfo = UserLoginDo.builder()
                        .phone(request.getPhone())
                        .loginIp(ip)
                        .loginTime(new Date()).build();

                if (!(userLoginMapper.insertRecord(loginInfo) > 0)){
                    throw new GlobalException(ResultCodeEnum.FAIL);
                }

                return new Result(ResultCodeEnum.LOGIN_REGISTER_SUCCESS);
            }

            // ??????????????????
            UserLoginDo loginInfo = UserLoginDo.builder()
                    .phone(request.getPhone())
                    .loginIp(ip)
                    .loginTime(new Date()).build();

            if (!(userLoginMapper.insertRecord(loginInfo) > 0)){
                throw new GlobalException(ResultCodeEnum.FAIL);
            }
            return new Result(ResultCodeEnum.LOGIN_SUCCESS);
        }

        // ??????????????????
        user = userMapper.selectByPhoneUser(request.getPhone());

        // ????????????????????????
        if (ObjectUtil.isEmpty(user)) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // ?????????????????????????????????
        if (!SecureUtil.md5(request.getPassword()).equals(user.getPassword())) {
            throw new GlobalException(ResultCodeEnum.PASSWORD_ERROR);
        }

        // ??????????????????
        if (!(userMapper.updateLastOnlineTimeByPhoneInteger(request.getPhone(), new Date()) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // ??????????????????
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
     * ?????????????????????
     *
     * @param request
     * @return
     * @throws GlobalException
     */
    @Override
    public Result sendCode(SMSCodeRequest request) throws GlobalException {
        // ????????????
        ValidateExtUtil.validate(request);

        // ?????????????????????????????????????????? ???????????? ???????????????
        if (StrUtil.isNotBlank(codeUtil.check(request.getPhone()))) {
            return new Result(ResultCodeEnum.SEND_CODE_ALREADY);
        }

        // ?????????????????????????????????????????????redis
        String code = codeUtil.getCode(request.getPhone());

        // ???????????????????????????
        serviceAsync.sendSMSCode(code);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
        return new Result(ResultCodeEnum.SEND_CODE_SUCCESS);
    }


    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changePwd(ChangePwdRequest request) {
        //????????????
        ValidateExtUtil.validate(request);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        // ????????????????????????
        UserDo user = userMapper.selectByPhoneUser(request.getPhone());
        if (ObjectUtil.isEmpty(user)) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // ??????????????????????????? ???????????????????????????????????? ?????????????????????????????????
        String oldPwd = user.getPassword();
        if (StrUtil.isNotBlank(oldPwd)) {
            if (!(oldPwd.equals(SecureUtil.md5(request.getOldPwd())))) {
                throw new GlobalException(ResultCodeEnum.PASSWORD_ERROR);
            }

            //??????????????????????????????
            if (!(userMapper.updatePasswordByPhone(request.getPhone(), SecureUtil.md5(request.getNewPwd())) > 0)) {
                throw new GlobalException(ResultCodeEnum.CHANGE_PASSWORD_FAIL);
            }

            UserOperationDo info = UserOperationDo.builder().phone(request.getPhone()).operationInfo("????????????")
                    .operationIp(httpServletRequest.getRemoteAddr())
                    .operationUser(request.getPhone())
                    .operationTime(new Date()).build();

            // ??????????????????????????????
            if (!(userOperationMapper.insertInfo(info) > 0)) {
                throw new GlobalException(ResultCodeEnum.FAIL);
            }

            // ??????token??????
            cacheService.tokenRemove(request.getPhone());
            return new Result(ResultCodeEnum.CHANGE_PASSWORD_SUCCESS);
        }

        // ??????????????? ????????????
        if (!(userMapper.updatePasswordByPhone(request.getPhone(), SecureUtil.md5(request.getNewPwd())) > 0)) {
            return new Result(ResultCodeEnum.CHANGE_PASSWORD_FAIL);
        }

        // ??????token??????
        cacheService.tokenRemove(request.getPhone());
        return new Result(ResultCodeEnum.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result resetPwd(ResetPwdRequest request) {
        // ????????????
        ValidateExtUtil.validate(request);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();

        // ??????redis???????????????????????????????????????????????????
        if (!(request.getCode().equals(codeUtil.check(request.getPhone())))) {
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }

        // ??????????????????????????????
        if (!(userMapper.updatePasswordByPhone(request.getPhone(), SecureUtil.md5(request.getPassword())) > 0)) {
            throw new GlobalException(ResultCodeEnum.CHANGE_PASSWORD_FAIL);
        }

        UserOperationDo info = UserOperationDo.builder().phone(request.getPhone())
                .operationInfo("????????????")
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(request.getPhone()).operationTime(new Date()).build();

        // ??????????????????????????????
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // ??????token?????? ???????????????
        cacheService.tokenRemove(request.getPhone());
        cacheService.SMSRemove(request.getPhone());
        return new Result(ResultCodeEnum.CHANGE_PASSWORD_SUCCESS);
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @Override
    public Result logout(HttpServletRequest request) {
        String token = "";
        String phone = "";
        try {
            // ??????token????????????
            if (StrUtil.isBlank(token = request.getHeader("token"))) {
                throw new GlobalException(ResultCodeEnum.TOKEN_INVALID);
            }

            // ???????????????
            phone = jwtUtil.getInfo(token).get("username").toString();

            // ??????token??????
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
        // ????????????
        ValidateExtUtil.validate(request);

        // ???????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String token = httpServletRequest.getHeader("token");
        String phone = jwtUtil.getInfo(token).get("username").toString();

        // ???????????????????????????
        UserDo user = userMapper.selectUserByPhone(phone);

        // ?????????????????????????????????
        if (!(userMapper.updateNameByPhone(phone, request.getName()) > 0)) {
            throw new GlobalException(ResultCodeEnum.CHANGE_NAME_FAIL);
        }

        UserOperationDo info = UserOperationDo.builder().phone(phone)
                .operationInfo(StrUtil.format(UserOperationUtil.renameTemplate, user.getName(), request.getName()))
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(phone).operationTime(new Date()).build();

        // ??????????????????????????????
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        return new Result(ResultCodeEnum.CHANGE_NAME_SUCCESS);
    }
}
