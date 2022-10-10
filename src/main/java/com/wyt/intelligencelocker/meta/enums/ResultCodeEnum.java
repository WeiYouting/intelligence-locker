package com.wyt.intelligencelocker.meta.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/9/11 20:52
 * @Email Wei.youting@qq.com
 * <p>
 * 枚举请求状态
 */

public enum ResultCodeEnum {

    FAIL(60001, "网络繁忙，请稍后再试"),
    SUCCESS(60002, "请求成功"),

    PIC_CODE_SUCCESS(10000, "图片验证码正确"),
    PIC_CODE_NULL(10001, "图片验证码不能为空"),
    PIC_CODE_SEND_SUCCESS(1006, "图片验证码生成成功"),
    PIC_CODE_ERROR(10003, "图片验证码错误"),
    SEND_CODE_SUCCESS(10002, "发送短信验证码成功"),
    NULL_PIC_CODE(10005, "暂未获取验证码"),
    PARAMETER_ERROR(10007, ""),
    SEND_CODE_ALREADY(10009, "已发送验证码，请稍后再试"),

    REGISTER_SUCCESS(20000, "注册成功"),
    REGISTER_FAIL(20001, "注册失败"),
    LOGIN_SUCCESS(20002, "登录成功"),
    LOGIN_FAIL(20003, "登录失败"),
    LOGIN_REGISTER_SUCCESS(20004, "用户不存在，已注册成功"),
    PHONE_REGISTERED(20005, "该手机号已被注册"),
    PASSWORD_CODE_NULL(20007, "密码或验证码不能为空"),
    PHONE_NOT_REGSTER(20009, "用户不存在"),
    PASSWORD_ERROR(20011, "密码错误"),
    CODE_ERROR(20013, "验证码错误"),
    CHANGE_PASSWORD_SUCCESS(20006, "修改密码成功"),
    CHANGE_PASSWORD_FAIL(20015, "修改密码失败"),
    CHANGE_NAME_SUCCESS(20008, "修改用户名成功"),
    CHANGE_NAME_FAIL(20017, "修改用户名失败"),

    LOGIN_EXPIRE(30001, "token过期，请重新登录"),
    TOKEN_INVALID(30003, "token不存在，请重新登录"),
    DONT_HAVE_PERMISSION(30005, "您暂无权限"),
    ALREADY_LOGIN(30000, "您已成功登录 无需重复操作"),
    LOGOUT_SUCCESS(30002, "登出成功"),
    LOGOUT_FAIL(30007, "登出成功"),

    QUERY_ALL_USER_SUCCESS(40000, "查询所有用户成功"),
    QUERY_USER_SUCCESS(40002, "查询用户成功"),

    ADD_MANAGER_SUCCESS(50000, "添加站点管理员成功"),
    ADD_MANAGER_FAIL(50001, "添加站点管理员失败"),
    ADMIN_NOT_CHANGE(50003, "超级管理员不可变更权限"),
    ADD_MANAGER_ALREADY(50005, "该账号已是站点管理员"),
    NOT_MANAGER(50009, "该账号不是站点管理员"),
    DISTRIBUTION_SUCCESS(50002, "变更站点管理员成功"),
    DISTRIBUTION_FAIL(50007, "变更站点管理员失败"),
    CHANGE_SITE_SUCCESS(50004, "变更站点成功"),
    CHANGE_SITE_FAIL(50011, "变更站点失败"),
    NOT_HAVE_SITE(50013, "该站点不存在"),
    REMOVE_MANAGER_SUCCESS(50006, "移除站点管理员成功"),
    REMOVE_MANAGER_FAIL(50015, "移除站点管理员失败"),
    QUERY_MANAGER_SUCCESS(50008, "查询站点管理员成功"),
    ADD_RIDER_SUCCESS(50010, "添加骑手成功"),
    ADD_RIDER_FAIL(50017, "添加骑手失败"),
    RIDER_EXIST(50019, "该骑手已存在"),
    RIDER_NOT_EXIST(50021, "该骑手不存在"),
    REMOVE_RIDER_SUCCESS(50012,"删除骑手成功"),
    REMOVE_RIDER_FAIL(50023,"删除骑手失败"),


    QUERY_SITE_SUCCESS(60000, "查询附近站点成功"),
    QUERY_SITE_FAIL(60001, "查询附近站点失败"),
    PLACE_ORDER_SUCCESS(60002, "下单成功"),
    PLACE_ORDER_FAIL(60003, "下单失败"),
    QUERY_ORDER_SUCCESS(60004, "查询订单成功"),
    QUERY_ORDER_FAIL(60005, "查询订单失败"),
    DATE_FORMAT_ERROR(60007, "日期格式错误"),
    UPDATE_ORDER_STATUS_SUCCESS(60006, "更新订单状态成功"),
    UPDATE_ORDER_STATUS_FAIL(60009, "更新订单状态失败"),
    NOT_HAVA_PERMISSION(60023, "暂无权限修改订单状态"),
    UPDATE_ORDER_FORMAT_ERROR(60021, "订单状态不合法"),
    NOT_HAVA_ORDER(60011, "该订单不存在"),
    ORDER_FINISHED(60013, "该订单以完成"),
    ORDER_ACCEPTED(60015, "该订单已被其他骑手接单"),
    ACCEPT_SUCCESS(60008, "接单成功"),
    ACCEPT_FAIL(60017, "接单失败"),
    ACCEPT_ALREADY(60019, "已接单"),

    LOCKER_FULL(70001, "此站点储柜已满"),
    LOCKER_NULL(70003, "此柜为空"),
    GET_SUCCESS(70000, "取出成功");

    private Integer code;
    private String msg;


    private static Map<Integer, ResultCodeEnum> statusMap = new HashMap<>();

    static {
        for (ResultCodeEnum v : ResultCodeEnum.values()) {
            statusMap.put(v.getCode(), v);
        }
    }

    public static ResultCodeEnum getByCode(Integer code) {
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

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    ResultCodeEnum() {

    }
}
