package com.jackila.util;

import java.util.regex.Pattern;

/**
 * @Author: jackila
 * @Date: 09:59 2020-09-02
 */
public class Utils {

    private static final Pattern HOST_PORT_PATTERN = Pattern.compile(".*?\\[?([0-9a-zA-Z\\-%.:]*)\\]?:([0-9]+)");

    public static final String NL = System.getProperty("line.separator");

    public static Thread newThread(String name, Runnable runnable, Boolean daemon) {
        Thread thread = new Thread(runnable, name);
        thread.setDaemon(daemon);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //log.error("")
            }
        });
        return thread;
    }
}
