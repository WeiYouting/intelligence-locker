package com.wyt.intelligencelocker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author WeiYouting
 * @create 2022/9/27 11:00
 * @Email Wei.youting@qq.com
 *
 * 线程池配置
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 每秒需要多少个线程处理
     * tasks/(1/taskcost)
     */
    private int corePoolSize = 3;

    /**
     * 线程池维护线程最大数量
     * (max(tasks) - queueCapacity)/(1/taskcost)
     */
    private int maxPoolSize = 3;

    /**
     * 缓存队列
     * (coreSizePool/tsakcost)*responsetime
     */
    private int queueCapacity = 10;

    /**
     * 允许的空闲时间
     */
    private int keepAlive = 120;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置线程核心数
        executor.setCorePoolSize(corePoolSize);
        //设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //设置队列容量
        executor.setQueueCapacity(queueCapacity);
        //设置允许空闲时间
        executor.setKeepAliveSeconds(keepAlive);
        //设置线程名称
        executor.setThreadNamePrefix("thread-");
        //等待任务结束再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
