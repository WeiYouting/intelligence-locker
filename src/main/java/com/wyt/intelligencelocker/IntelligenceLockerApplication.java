package com.wyt.intelligencelocker;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@EnableCaching
@SpringBootApplication
@EnableAsync
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.wyt")
@EnableWebMvc
public class IntelligenceLockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligenceLockerApplication.class, args);
    }

}
