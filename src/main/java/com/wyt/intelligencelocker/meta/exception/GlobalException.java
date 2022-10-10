package com.wyt.intelligencelocker.meta.exception;

import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;

/**
 * @Author WeiYouting
 * @create 2022/9/19 13:50
 * @Email Wei.youting@qq.com
 *
 * 自定义全局异常
 */
public class GlobalException extends RuntimeException {

    private String msg;
    private Object obj;


    public GlobalException(String msg) {
        super(msg);
    }

    public GlobalException(ResultCodeEnum status) {
        super(status.getCode().toString());
    }
    public GlobalException(ResultCodeEnum status,Object obj) {
        super(status.getCode().toString());
        this.obj = obj;
    }

    public GlobalException() {

    }

    public Object getObj() {
        return obj;
    }

}
