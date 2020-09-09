package com.jackila.kafka.log_manage.log;

import com.jackila.kafka.KafkaConfig;
import lombok.Data;

import java.util.Map;

/**
 * @Author: jackila
 * @Date: 16:56 2020-09-01
 */
@Data
public class LogConfig {

    public static class Defaults{
        public static Long SegmentSize = KafkaConfig.Defaults.LogSegmentBytes;
        public static Long SegmentMs = KafkaConfig.Defaults.LogRollHours * 60 * 60 * 1000L;
        public static Long SegmentJitterMs = KafkaConfig.Defaults.LogRollJitterHours * 60 * 60 * 1000L;
        public static Long FlushInterval = KafkaConfig.Defaults.LogFlushIntervalMessages;
        public static Long FlushMs = KafkaConfig.Defaults.LogFlushSchedulerIntervalMs;
        public static Long RetentionSize = KafkaConfig.Defaults.LogLogRetentionBytes;
        public static Long RetentionMs = KafkaConfig.Defaults.LogRetentionHours * 60 * 60 * 1000L;


    }

    private Map props;
}
