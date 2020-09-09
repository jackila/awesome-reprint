package com.jackila.kafka.feature02;

import java.util.NoSuchElementException;

/**
 * @Author: jackila
 * @Date: 17:21 2020-08-18
 */
public enum TimestampType {

    NO_TIMESTAMP_TYPE(-1, "NoTimestampType"), CREATE_TIME(0, "CreateTime"), LOG_APPEND_TIME(1, "LogAppendTime");

    public final int id;
    public final String name;
    TimestampType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static TimestampType forAttributes(byte attributes){
        int timestampType = (attributes & Record.TIMESTAMP_TYPE_MASK) >> 3;
        return timestampType == 0 ? CREATE_TIME : LOG_APPEND_TIME;
    }


    public static TimestampType forName(String name) {
        for (TimestampType t : values())
            if (t.name.equals(name))
                return t;
        throw new NoSuchElementException("Invalid timestamp type " + name);
    }

    @Override
    public String toString() {
        return name;
    }
}
