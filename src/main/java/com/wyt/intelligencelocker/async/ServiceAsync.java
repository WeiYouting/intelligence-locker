package com.wyt.intelligencelocker.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author WeiYouting
 * @create 2022/9/27 10:42
 * @Email Wei.youting@qq.com
 *
 * 异步操作
 */
@Component
@Slf4j
public class ServiceAsync {

    // 使用线程池异步发送验证码
    @Async("taskExecutor")
    public void sendSMSCode(String code) {
        log.info("短信验证码发送成功：" + code);
    }


}
