package com.wyt.intelligencelocker.meta.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/10/9 10:26
 * @Email Wei.youting@qq.com
 */
public enum OrderStatusEnum {
    CREATE(1, "订单已创建"),
    RIDER(2, "骑手已接单"),
    DELIVERY(3, "配送中"),
    ARRIVE(4, "已送达站点"),
    COMPLETE(5, "订单已完成");

    private Integer code;
    private String msg;


    private static Map<Integer, OrderStatusEnum> statusMap = new HashMap<>();

    static {
        for (OrderStatusEnum v : OrderStatusEnum.values()) {
            statusMap.put(v.getCode(), v);
        }
    }

    public static OrderStatusEnum getByCode(Integer code) {
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

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    OrderStatusEnum() {

    }
}
