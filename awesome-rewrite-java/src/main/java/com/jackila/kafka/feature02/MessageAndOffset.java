package com.jackila.kafka.feature02;

/**
 * @Author: jackila
 * @Date: 17:07 2020-08-18
 */
public class MessageAndOffset {

    private final Message message;
    private final long offset ;

    public MessageAndOffset(Message message, long offset) {
        this.message = message;
        this.offset = offset;
    }
}
