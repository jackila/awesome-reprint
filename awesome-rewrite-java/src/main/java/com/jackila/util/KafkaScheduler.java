package com.jackila.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jackila
 * @Date: 09:39 2020-09-03
 */
public class KafkaScheduler implements Scheduler {

    private ScheduledThreadPoolExecutor executor = null;
    private final AtomicInteger schedulerThreadId = new AtomicInteger(0);

    private final Integer threads;
    private final String threadNamePrefix;
    private Boolean daemon = true;

    public KafkaScheduler(Integer threads, String threadNamePrefix) {
        this.threads = threads;
        this.threadNamePrefix = threadNamePrefix;
    }

    public KafkaScheduler(Integer threads, String threadNamePrefix, Boolean daemon) {
        this.threads = threads;
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public void startup() {

        synchronized(this){
            if(isStarted()){
                throw new IllegalStateException("this schduler has already been started");
            }

            executor = new ScheduledThreadPoolExecutor(threads);
            executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executor.setThreadFactory(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    return Utils.newThread(threadNamePrefix + schedulerThreadId.getAndIncrement(),runnable,daemon);
                }
            });

        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        //debug()
        final ScheduledThreadPoolExecutor cachedExcutor = this.executor;
        if(cachedExcutor != null){
            synchronized (this){
                cachedExcutor.shutdown();
                this.executor = null;
            }
            cachedExcutor.awaitTermination(1, TimeUnit.DAYS);
        }
    }

    @Override
    public boolean isStarted() {
        synchronized (this){
            return executor != null;
        }

    }

    @Override
    public void schedule(String name, Callable callable, long delay, long period, TimeUnit unit) {

        //debug
        synchronized (this){
            ensureRunning();
            Runnable runnable = CoreUtils.runnable(new Runnable() {
                @Override
                public void run() {
                    try {
                        callable.call();
                    } catch (Exception e) {
                        //error()
                    } finally {
                        //trace
                    }
                }
            });
            if(period >= 0){
                executor.scheduleAtFixedRate(runnable, delay, period, unit);
            }else{
                executor.schedule(runnable, delay, unit);
            }
        }
    }

    private void ensureRunning() {
        if(!isStarted()){
            throw new IllegalStateException("kafka scheduler is not running");
        }
    }
}
