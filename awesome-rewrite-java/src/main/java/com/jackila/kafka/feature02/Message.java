package com.jackila.kafka.feature02;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * @Author: jackila
 * @Date: 17:14 2020-08-18
 */
public class Message {

    public static final Integer CrcOffset = 0;
    public static final Integer CrcLength = 4;
    public static final Integer MagicOffset = CrcOffset + CrcLength;
    public static final Integer MagicLength = 1;
    public static final Integer AttributesOffset = MagicOffset + MagicLength;
    public static final Integer AttributesLength = 1;
    // Only message format version 1 has the timestamp field.
    public static final Integer TimestampOffset = AttributesOffset + AttributesLength;
    public static final Integer TimestampLength = 8;
    public static final Integer KeySizeOffset_V0 = AttributesOffset + AttributesLength;
    public static final Integer KeySizeOffset_V1 = TimestampOffset + TimestampLength;
    public static final Integer KeySizeLength = 4;
    public static final Integer KeyOffset_V0 = KeySizeOffset_V0 + KeySizeLength;
    public static final Integer KeyOffset_V1 = KeySizeOffset_V1 + KeySizeLength;
    public static final Integer ValueSizeLength = 4;


    public static final Integer MinMessageOverhead = KeyOffset_V0 + ValueSizeLength;

    //static
    final static int MessageSizeLength = 4;
    final static int OffsetLength = 8;
    final static int LogOverhead = MessageSizeLength + OffsetLength;



    final ByteBuffer buffer;
    private final Optional<Long> wrapperMessageTimestamp;
    private final Optional<TimestampType> wrapperMessageTimestampType;

    public Message(ByteBuffer buffer, Optional<Long> wrapperMessageTimestamp, Optional<TimestampType> wrapperMessageTimestampType) {
        this.buffer = buffer;
        this.wrapperMessageTimestamp = wrapperMessageTimestamp;
        this.wrapperMessageTimestampType = wrapperMessageTimestampType;
    }


}
