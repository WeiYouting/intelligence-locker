package com.wyt.intelligencelocker.meta.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/9/20 13:42
 * @Email Wei.youting@qq.com
 *
 * 枚举用户权限
 */
public enum UserRoleEnum {
    ADMIN(1, "系统管理员"),
    MANAGER(2, "站点管理员"),
    RIDER(3, "骑手"),
    USER(4, "普通用户");


    private Integer code;
    private String msg;

    private static Map<Integer, UserRoleEnum> unumMap = new HashMap<>();

    static {
        for (UserRoleEnum v : UserRoleEnum.values()) {
            unumMap.put(v.getCode(), v);
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

    UserRoleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserRoleEnum getBycode(Integer code){
        return unumMap.get(code);
    }

    UserRoleEnum() {

    }
}
