package com.wyt.intelligencelocker.service;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.exception.ParameterException;
import com.wyt.intelligencelocker.meta.request.*;


import javax.servlet.http.HttpServletRequest;

/**
 * @Author WeiYouting
 * @create 2022/9/19 10:12
 * @Email Wei.youting@qq.com
 */

public interface IUserService {

    Result register(RegisterRequest request) throws GlobalException, ParameterException;

    Result login(LoginRequest request) throws GlobalException, ParameterException;

    Result sendCode(SMSCodeRequest request) throws GlobalException;

    Result changePwd(ChangePwdRequest request);

    Result resetPwd(ResetPwdRequest request);

    Result logout(HttpServletRequest request);

    Result changeName(ChangeNameRequest request);

}
