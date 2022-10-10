package com.wyt.intelligencelocker.controller.util;


import cn.hutool.core.util.ObjUtil;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.exception.ParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author WeiYouting
 * @create 2022/9/12 18:06
 * @Email Wei.youting@qq.com
 *
 * 处理全局异常
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(GlobalException.class)
    public Result doGlobalException(GlobalException e) {
        Integer code = Integer.parseInt(e.getMessage());
        ResultCodeEnum resultCodeEnum = ResultCodeEnum.getByCode(code);
        if (ObjUtil.isNotEmpty(e.getObj())){
            return new Result(resultCodeEnum,e.getObj());
        }
        return new Result(resultCodeEnum);
    }

    @ExceptionHandler(ParameterException.class)
    public Result doParamterException(ParameterException e) {
        ResultCodeEnum result = ResultCodeEnum.PARAMETER_ERROR;
        result.setMsg(e.getMessage());
        return new Result(result);
    }

    @ExceptionHandler(Exception.class)
    public Result doException(Exception e) {
        return new Result(ResultCodeEnum.FAIL);
    }

}
