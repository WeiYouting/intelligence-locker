package com.wyt.intelligencelocker.meta.exception;

import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;

/**
 * @Author WeiYouting
 * @create 2022/9/22 23:23
 * @Email Wei.youting@qq.com
 *
 * 参数错误异常
 */
public class ParameterException extends RuntimeException {
    private String msg;
    private Object obj;

    public ParameterException(Object obj, String msg) {
        this.msg = msg;
        this.obj = obj;
    }

    public ParameterException(String msg) {
        super(msg);
    }

    public ParameterException(ResultCodeEnum status) {
        super(status.getCode().toString());
    }

    public ParameterException() {

    }

    public Object getObj() {
        return obj;
    }


}
