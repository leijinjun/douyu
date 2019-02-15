package com.lei2j.douyu.service.impl;

import com.lei2j.douyu.service.ExecutorTaskService;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.*;

/**
 * Created by leijinjun on 2019/2/15.
 */
@Component
public class ExecutorTaskServiceImpl extends BaseServiceImpl implements ExecutorTaskService {

    @Override
    public void execute(Runnable task) {
        ThreadPoolInstance.EXECUTOR_SERVICE.execute(task);
    }

    @Override
    public <T> Future<Optional<T>> submit(Runnable task, Optional<T> result) {
        return ThreadPoolInstance.EXECUTOR_SERVICE.submit(task, result);
    }

    public static class ThreadPoolInstance {

        private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolInstance.class);

        final static int CORE_THREAD_SIZE = Runtime.getRuntime().availableProcessors() + 1;

        final static int MAX_THREAD_SIZE = CORE_THREAD_SIZE * 2 + 1;

        final static ExecutorService EXECUTOR_SERVICE = new
                ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, 30,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(100000), new
                DefaultThreadFactory("douyu-thread-pool-%d", true, 5),
                (task, executor) -> {
                    LOGGER.error("Task rejected:{}", task);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!executor.isShutdown()) {
                        executor.execute(task);
                    }
                });
    }
}
