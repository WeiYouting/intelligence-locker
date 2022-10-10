package com.wyt.intelligencelocker.meta.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/10/9 16:29
 * @Email Wei.youting@qq.com
 */
public enum RiderLevelEnum {
    ROOKIE(1, "新星骑手"),
    GOOD(2, "优秀骑手"),
    KING(3, "王者骑手");
    private Integer code;
    private String msg;


    private static Map<Integer, RiderLevelEnum> statusMap = new HashMap<>();

    static {
        for (RiderLevelEnum v : RiderLevelEnum.values()) {
            statusMap.put(v.getCode(), v);
        }
    }

    public static RiderLevelEnum getByCode(Integer code) {
        return statusMap.get(code);
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

    RiderLevelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
