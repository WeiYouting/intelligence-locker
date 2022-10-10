package com.wyt.intelligencelocker.config;

import com.wyt.intelligencelocker.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

/**
 * @Author WeiYouting
 * @create 2022/9/22 10:24
 * @Email Wei.youting@qq.com
 * <p>
 * mvc拦截器   拦截请求 判断是否登录
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final String HEADER = "/user";

    // 配置不被拦截的请求
    private static final ArrayList<String> excludePath = new ArrayList<>();

    static {
        excludePath.add(HEADER + "/resetPwd");
        excludePath.add(HEADER + "/login");
        excludePath.add(HEADER + "/register");
        excludePath.add(HEADER + "/getCode");
        excludePath.add(HEADER + "/checkCode");
        excludePath.add(HEADER + "/getSMSCode");
        excludePath.add(HEADER + "/changePwd");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
