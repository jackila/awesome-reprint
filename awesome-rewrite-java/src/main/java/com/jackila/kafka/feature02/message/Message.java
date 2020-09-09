package com.jackila.kafka.feature02.message;

import com.jackila.kafka.feature02.TimestampType;
import lombok.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * @Author: jackila
 * @Date: 17:06 2020-08-24
 */
@Data
public class Message {

    /**
     * The current offset and size for all the fixed-length fields
     */
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

    public static final Long NoTimestamp = -1L;
    public static final Byte MagicValue_V0 = 0;
    public static final Byte MagicValue_V1 = 1;
    public static final Byte CurrentMagicValue = 1;


    private ByteBuffer buffer;
    private final Optional<Long> wrapperMessageTimestamp;
    private final Optional<TimestampType> wrapperMessageTimestampType;

    public Message(ByteBuffer byteBuffer, Optional<Long> wrapperMessageTimestamp, Optional<TimestampType> wrapperMessageTimestampType) {
        this.buffer = byteBuffer;
        this.wrapperMessageTimestamp = wrapperMessageTimestamp;
        this.wrapperMessageTimestampType = wrapperMessageTimestampType;
    }

    public Integer size(){
        return buffer.limit();
    }
    
    public Byte magic(){
        return buffer.get(MagicLength);
    }

    public Long timestamp(){
        if(magic().equals(MagicValue_V0)){
            return Message.NoTimestamp;
        }else if(wrapperMessageTimestamp.isPresent() && TimestampType.LOG_APPEND_TIME.equals(wrapperMessageTimestamp.get())){
            return wrapperMessageTimestamp.get();
        }else{
            return buffer.getLong(Message.TimestampOffset);
        }
    }

    enum CompressionEnum{


        DefaultCompressionCodec(1,"gzip"),
        GZIPCompressionCodec(1,"gzip"),
        NoCompressionCodec(0,"none");
        public final Integer codec;
        public final String name;

        CompressionEnum(Integer codec, String name) {
            this.codec = codec;
            this.name = name;
        }

    }

    interface CompressionCodec {

        Integer codec();

        String name();
    }

     interface BrokerCompressionCodec {
        String name();
    }

    static class DefaultCompressionCodec implements CompressionCodec,BrokerCompressionCodec{

        @Override
        public Integer codec() {
            return new GZIPCompressionCodec().codec();
        }

        @Override
        public String name() {
            return new GZIPCompressionCodec().name();
        }
    }

    static class GZIPCompressionCodec implements CompressionCodec,BrokerCompressionCodec{

        @Override
        public Integer codec() {
            return 1;
        }

        @Override
        public String name() {
            return "gzip";
        }
    }

    static class NoCompressionCodec implements CompressionCodec,BrokerCompressionCodec{

        @Override
        public Integer codec() {
            return 0;
        }

        @Override
        public String name() {
            return "none";
        }
    }


}
