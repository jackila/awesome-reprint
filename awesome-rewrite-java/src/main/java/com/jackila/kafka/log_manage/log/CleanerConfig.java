package com.jackila.kafka.log_manage.log;

import lombok.Data;

/**
 * @Author: jackila
 * @Date: 10:27 2020-09-02
 */
@Data
public class CleanerConfig {
    private Integer numThread = 1;
    private Long dedupeBufferSize = 4 * 1024 * 1024L;
    private Double dedupeBufferLoadFactor = 0.9d;
    private Integer ioBufferSize = 1024 * 1024;
    private Integer maxMessageSize = 32 * 1024 * 1024;
    private Double maxIoBytePerSecond = Double.MAX_VALUE;
    private Long backOffMs = 15 * 1000L;
    private Boolean enableCleaner = true;
    private String hashAlgorithm = "MD5";

    public CleanerConfig() {
    }
}
