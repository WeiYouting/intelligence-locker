package meta.exception;

/**
 * @Author WeiYouting
 * @create 2022/9/19 13:50
 * @Email Wei.youting@qq.com
 */
public class GlobalException extends Exception{

    private String msg;
    private Object obj;

    public GlobalException(Object obj,String msg) {
        this.msg = msg;
        this.obj = obj;
    }
    public GlobalException(String msg) {
        super(msg);
    }

    public GlobalException(){

    }

    @Override
    public String toString() {
        return msg;
    }
}
