package com.wyt.intelligencelocker.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import javax.annotation.Resource;

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
    
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${email.from}")
    private String from;

    @Value("${email.to}")
    private String to;

    private String subject = "【智慧储柜】";

    private String context = "您的验证码是:";


    // 使用线程池异步发送验证码
    @Async("taskExecutor")
    public void sendSMSCode(String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from + "(智慧储柜项目组)");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(context + code);
        javaMailSender.send(simpleMailMessage);
        log.info("短信验证码发送成功：" + code);
    }


}
