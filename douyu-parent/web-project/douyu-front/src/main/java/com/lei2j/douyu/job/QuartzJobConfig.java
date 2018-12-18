package com.lei2j.douyu.job;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * Created by lei2j on 2018/11/17.
 */
@Configuration
public class QuartzJobConfig {

    private static final String DEFAULT_METHOD = "job";

    @Bean("douyuConnectionJobDetail")
    public MethodInvokingJobDetailFactoryBean createDouyuConntectionJobBean(DouyuConnectJob douyuConnectJob){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setGroup("group");
        bean.setBeanName("DouyuConntectionJob");
        bean.setConcurrent(false);
        bean.setTargetObject(douyuConnectJob);
        bean.setTargetMethod("connectRooms");
//        bean.setTargetMethod(DEFAULT_METHOD);
        return bean;
    }

    @Bean("checkConnectedRoomsJobDetail")
    public MethodInvokingJobDetailFactoryBean createCheckConnectedRoomsJJobBean(DouyuConnectJob douyuConnectJob){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setGroup("group");
        bean.setBeanName("DouyuConntectionJob");
        bean.setConcurrent(false);
        bean.setTargetObject(douyuConnectJob);
        bean.setTargetMethod("checkConnectedRooms");
//        bean.setTargetMethod(DEFAULT_METHOD);
        return bean;
    }

    @Bean("douyuConnectionCronTrigger")
    public CronTriggerFactoryBean cronDouyuConntectionJobTriggerBean(@Qualifier("douyuConnectionJobDetail") MethodInvokingJobDetailFactoryBean jobDetail){
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setBeanName("douyuConnectionCronTrigger");
        triggerFactoryBean.setGroup("group");
        triggerFactoryBean.setDescription("定时获取斗鱼热门主播房间弹幕");
        triggerFactoryBean.setJobDetail(jobDetail.getObject());
        triggerFactoryBean.setCronExpression("0 0/30 * * * ?");
        return triggerFactoryBean;
    }

    @Bean("checkConnectedRoomsCronTrigger")
    public CronTriggerFactoryBean cronCheckConnectedRoomsTriggerBean(@Qualifier("checkConnectedRoomsJobDetail") MethodInvokingJobDetailFactoryBean jobDetail){
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setBeanName("douyuConnectionCronTrigger");
        triggerFactoryBean.setGroup("group");
        triggerFactoryBean.setDescription("定时检查已连接房间");
        triggerFactoryBean.setJobDetail(jobDetail.getObject());
        triggerFactoryBean.setCronExpression("0 0 * * * ?");
        return triggerFactoryBean;
    }
}
