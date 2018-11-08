package com.syswin.temail.media.bank.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Value("${app.mediabank.image.pool.core.size}")
    private int imageCorePoolSize;

    @Value("${app.mediabank.image.pool.max.size}")
    private int imageMaxPoolSize;

    @Value("${app.mediabank.image.pool.queue.capacity}")
    private int imageQueueCapacity;

    @Value("${app.mediabank.video.pool.core.size}")
    private int videoCorePoolSize;

    @Value("${app.mediabank.video.pool.max.size}")
    private int videoMaxPoolSize;

    @Value("${app.mediabank.video.pool.queue.capacity}")
    private int videoQueueCapacity;

    @Bean(name = "imageExecutor")
    public Executor imageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(imageCorePoolSize);
        executor.setMaxPoolSize(imageMaxPoolSize);
        executor.setQueueCapacity(imageQueueCapacity);
        executor.setThreadNamePrefix("ImageExecutorThread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(name = "videoExecutor")
    public Executor videoExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(videoCorePoolSize);
        executor.setMaxPoolSize(videoMaxPoolSize);
        executor.setQueueCapacity(videoQueueCapacity);
        executor.setThreadNamePrefix("VideoExecutorThread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}