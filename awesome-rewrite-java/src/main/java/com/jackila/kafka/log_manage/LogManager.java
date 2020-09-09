package com.jackila.kafka.log_manage;

import com.jackila.kafka.log_manage.log.CleanerConfig;
import com.jackila.kafka.log_manage.log.LogConfig;
import com.jackila.kafka.server.BrokerState;
import com.jackila.util.Scheduler;
import com.jackila.util.Threadsafe;
import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * @Author: jackila
 * @Date: 09:35 2020-09-01
 */
@Threadsafe
@Data
public class LogManager {
    //properties
    private final File[] logDirs;
    private final Map<String, LogConfig> topicConfig;
    private final LogConfig defaultConfig;
    private final CleanerConfig cleanerConfig;

    private Integer ioThreads;
    private final Long flushCheckMs;
    private final Long flushCheckPointMs;
    private final Long retentionCheckMs;

    private Scheduler scheduler;
    private final BrokerState brokerState;
}
