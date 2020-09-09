package com.jackila.util;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 * 轮训任务、延迟任务
 * @Author: jackila
 * @Date: 10:36 2020-09-02
 */
public interface Scheduler {
    void startup();
    void shutdown() throws InterruptedException;
    boolean isStarted();

    void schedule(String name, Callable callable, long delay, long period, TimeUnit unit);
}
