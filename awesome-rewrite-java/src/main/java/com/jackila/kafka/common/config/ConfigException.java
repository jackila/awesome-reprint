package com.jackila.kafka.common.config;

import com.jackila.kafka.KafkaException;

/**
 * @Author: jackila
 * @Date: 09:56 2020-09-02
 */
public class ConfigException extends KafkaException {

    private static final long serialVersionUID = 1L;

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String name, Object value) {
        this(name, value, null);
    }

    public ConfigException(String name, Object value, String message) {
        super("Invalid value " + value + " for configuration " + name + (message == null ? "" : ": " + message));
    }

}
