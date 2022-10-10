package com.wyt.intelligencelocker.meta.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/9/22 9:46
 * @Email Wei.youting@qq.com
 *
 * 枚举账号状态
 */
public enum UserStatusEnum {
    NORMAL(1, "账号正常"),
    FREEZE(2, "账号被冻结"),
    CANCELLATION(3, "账号已注销");


    private Integer code;
    private String msg;


    private static Map<Integer, UserStatusEnum> enumMap = new HashMap<>();

    static {
        for (UserStatusEnum v : UserStatusEnum.values()) {
            enumMap.put(v.getCode(), v);
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    UserStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserStatusEnum getByCode(Integer code){
        return enumMap.get(code);
    }

    UserStatusEnum() {

    }
}
