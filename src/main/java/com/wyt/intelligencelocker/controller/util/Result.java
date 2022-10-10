package com.wyt.intelligencelocker.controller.util;


import lombok.Data;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;

import java.io.Serializable;

/**
 * @Author WeiYouting
 * @create 2022/9/11 20:43
 * @Email Wei.youting@qq.com
 *
 * 封装响应信息
 */

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    //状态码
    private Integer code;

    //响应消息
    private String msg;

    //响应数据
    private Object data;

    public Result() {

    }

    public Result(ResultCodeEnum resultCode, Object data) {
        this.code = resultCode.getCode();
        this.data = data;
        this.msg = resultCode.getMsg();
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result(ResultCodeEnum resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }
}
