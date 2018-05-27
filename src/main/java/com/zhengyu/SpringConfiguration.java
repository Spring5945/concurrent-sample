package com.zhengyu;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class SpringConfiguration {

    AtomicInteger threadNo = new AtomicInteger(1);

    @Bean("SpringThreadPool")
    public ThreadPoolTaskExecutor springThreadPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(5);
        threadPoolTaskExecutor.setThreadFactory(r -> {
            Thread thread = new Thread();
            thread.setDaemon(false);
            thread.setName("mySpringThreadPoolTask-" + threadNo);
            return thread;
        });
        threadPoolTaskExecutor.setRejectedExecutionHandler((r, executor) -> System.out.println("拒绝策略报警,spring线程池超过最大线程数"));
        threadPoolTaskExecutor.setThreadNamePrefix("myThread");
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        return threadPoolTaskExecutor;
    }


    @Bean("NativeThreadPoolExecutor")
    public ThreadPoolExecutor myThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5),
                r -> new Thread(r, "ThreadPool thread: " + threadNo.getAndIncrement()),
                (r, executor) -> System.out.println("拒绝策略报警")
        );
        return threadPoolExecutor;
    }

}
