package meta.enums;

/**
 * @Author WeiYouting
 * @create 2022/9/11 20:52
 * @Email Wei.youting@qq.com
 */

public enum ResultCode {
    REGISTER_SUCCESS(1000,"注册成功"),
    REGISTER_FAIL(1001,"注册失败");


    private Integer code;

    private String msg;

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

    ResultCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
