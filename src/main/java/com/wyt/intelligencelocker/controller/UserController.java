package com.wyt.intelligencelocker.controller;

import cn.hutool.core.util.StrUtil;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.ParameterException;
import com.wyt.intelligencelocker.meta.request.*;
import com.wyt.intelligencelocker.service.IUserService;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.utils.CacheUtil;
import com.wyt.intelligencelocker.utils.JwtUtil;
import com.wyt.intelligencelocker.utils.RandomCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author WeiYouting
 * @create 2022/9/19 14:33
 * @Email Wei.youting@qq.com
 */

//处理跨域请求
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private IUserService iUserService;

    @Resource
    private RandomCodeUtil randomValidateCode;

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
    @PostMapping("/register")
    public Result register(@RequestBody RegisterRequest request) throws GlobalException, ParameterException {
        return iUserService.register(request);
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @PostMapping("/changePwd")
    public Result changePwd(@RequestBody ChangePwdRequest request) {
        return iUserService.changePwd(request);
    }

    /**
     * 重置密码
     *
     * @param request
     * @return
     */
    @PostMapping("/resetPwd")
    public Result resetPwd(@RequestBody ResetPwdRequest request) {
        return iUserService.resetPwd(request);
    }

    /**
     * 用户登录
     *
     * @param request
     * @return token
     * @throws GlobalException
     * @throws ParameterException
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request) throws GlobalException, ParameterException {
        Result result = iUserService.login(request);
        if (result.getCode().compareTo(ResultCodeEnum.LOGIN_SUCCESS.getCode()) == 0 || result.getCode().compareTo(ResultCodeEnum.LOGIN_REGISTER_SUCCESS.getCode()) == 0) {
            String userId = UUID.randomUUID().toString();
            HashMap<String, String> info = new HashMap<>();
            info.put("username", request.getPhone());
//            info.put("password",request.getPassword());
            String token = jwtUtil.sign(userId, info);
            result.setData(token);
            cacheService.SMSRemove(request.getPhone());
        }
        return result;
    }

    /**
     * 获取短信验证码
     *
     * @param request
     * @return
     * @throws GlobalException
     */
    @PostMapping("/getSMSCode")
    public Result getSMSCode(@RequestBody SMSCodeRequest request) throws GlobalException {
        return iUserService.sendCode(request);
    }

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @throws GlobalException
     */
    @PostMapping("/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        randomValidateCode.getRandomCode(request, response);//输出验证码图片方法

    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        return iUserService.logout(request);
    }

    /**
     * 校验图片验证码
     *
     * @param request
     * @param request session
     * @return
     * @throws GlobalException
     */
    @PostMapping(value = "/checkCode")
    public Result checkVerify(@RequestBody CheckPictureRequest request, HttpSession session) throws GlobalException {
        //从session中获取随机数
        String code = request.getCode();
        String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
        if (StrUtil.isBlank(random)) {
            throw new GlobalException(ResultCodeEnum.NULL_PIC_CODE);
        }
        if (StrUtil.isBlank(code)) {
            throw new GlobalException(ResultCodeEnum.PIC_CODE_NULL);
        }
        if (!random.equalsIgnoreCase(code)) {
            throw new GlobalException(ResultCodeEnum.PIC_CODE_ERROR);
        }
        return new Result(ResultCodeEnum.PIC_CODE_SUCCESS);
    }

    /**
     * 修改用户名
     * @param request
     * @return
     */
    @PostMapping("/changeName")
    public Result changeName(@RequestBody ChangeNameRequest request){
        return iUserService.changeName(request);
    }

}
