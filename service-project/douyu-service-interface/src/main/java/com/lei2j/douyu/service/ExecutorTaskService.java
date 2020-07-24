package com.lei2j.douyu.service;

import java.util.concurrent.Future;

/**
 * Created by leijinjun on 2019/2/15.
 */
public interface ExecutorTaskService {

    void execute(Runnable task);

    <T> Future<T> submit(Runnable task,T result);
}
