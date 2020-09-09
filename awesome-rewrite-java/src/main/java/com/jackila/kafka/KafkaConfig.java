package com.jackila.kafka;

/**
 * @Author: jackila
 * @Date: 16:59 2020-09-01
 */
public class KafkaConfig {

    public static class Defaults {
        // zk config

        //general config


        //log config
        public static Long LogSegmentBytes = 1 * 1024 * 1024 * 1024L;

        public static long LogRollHours = 24 * 7;
        public static long LogRollJitterHours = 0;
        public static Long LogFlushIntervalMessages = Long.MAX_VALUE;
        public static Long LogFlushSchedulerIntervalMs = Long.MAX_VALUE;
        public static Long LogLogRetentionBytes = -1L;
        public static Long LogRetentionHours = 24 * 7L;
    }
}
