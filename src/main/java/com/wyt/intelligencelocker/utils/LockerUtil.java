package com.wyt.intelligencelocker.utils;

import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WeiYouting
 * @create 2022/10/10 10:06
 * @Email Wei.youting@qq.com
 */
public class LockerUtil {

    private static Integer INITIALCAPACITY = 50;

    private static Map<Integer, Boolean> lockerMap = new HashMap(INITIALCAPACITY);

    static {
        for (int i = 0; i < INITIALCAPACITY; i++) {
            lockerMap.put(i, Boolean.FALSE);
        }
    }

    public static synchronized Integer put() {
        Integer key = getNum();
        if (key == null) {
            throw new GlobalException(ResultCodeEnum.LOCKER_FULL);
        }
        lockerMap.put(key, Boolean.TRUE);
        return key + 1;
    }

    private static Integer getNum() {
        for (Map.Entry<Integer, Boolean> entry : lockerMap.entrySet()) {
            if (entry.getValue().equals(Boolean.FALSE)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static synchronized boolean get(Integer id) {
        if (lockerMap.get(id - 1).equals(Boolean.FALSE)) {
            throw new GlobalException(ResultCodeEnum.LOCKER_NULL);
        }
        lockerMap.put(id - 1, Boolean.FALSE);
        return true;
    }


}
