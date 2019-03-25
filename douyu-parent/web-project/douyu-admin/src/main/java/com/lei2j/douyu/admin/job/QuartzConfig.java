package com.lei2j.douyu.admin.job;

import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by lei2j on 2018/11/18.
 */
@Configuration
public class QuartzConfig {

    private static ScheduledExecutorService executorService =
            new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory("Thread-douyu-job-%d", true, 10));

    @Bean
    public SchedulerFactoryBean schedulerFactory(@Qualifier("douyuConnectionCronTrigger") CronTriggerFactoryBean trigger1,
                                                 @Qualifier("checkConnectedRoomsCronTrigger") CronTriggerFactoryBean trigger2){
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setTaskExecutor(executorService);
        schedulerFactory.setStartupDelay(10);
        schedulerFactory.setTriggers(trigger1.getObject(),trigger2.getObject());
        return schedulerFactory;
    }
}
