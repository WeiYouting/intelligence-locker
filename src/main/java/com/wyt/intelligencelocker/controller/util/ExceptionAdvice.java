package com.wyt.intelligencelocker.controller.util;


import meta.enums.ResultCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author WeiYouting
 * @create 2022/9/12 18:06
 * @Email Wei.youting@qq.com
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public Result doException(Exception e){
        return new Result(ResultCode.REGISTER_FAIL,e.getMessage());
    }

}
