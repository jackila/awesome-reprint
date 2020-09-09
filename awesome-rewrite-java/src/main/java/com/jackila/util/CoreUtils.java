package com.jackila.util;

/**
 * @Author: jackila
 * @Date: 11:02 2020-09-03
 */
public class CoreUtils {

    public static Runnable runnable (Runnable runnable){
        return runnable;
    }

    public static Thread newThread(String name,Boolean daemon,Runnable runnable){

        return Utils.newThread(name, runnable(runnable), daemon);
    }

}
