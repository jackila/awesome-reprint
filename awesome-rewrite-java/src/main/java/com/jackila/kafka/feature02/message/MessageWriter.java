package com.jackila.kafka.feature02.message;

import com.jackila.kafka.feature02.TimestampType;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author jackila
 * @Author: jackila
 * @Date: 15:35 2020-08-26
 */
public class MessageWriter extends BufferingOutputStream {


    public MessageWriter(Integer segmentSize) {
        super(segmentSize);
    }

    public void write(
            Byte[] key,
            Message.CompressionEnum codec,
            Long timestamp,
            TimestampType timestampType,
            Byte magicValue
            ){

    }
}

@Data
class BufferingOutputStream extends OutputStream {



    public BufferingOutputStream(Integer segmentSize) {
        this.segmentSize = segmentSize;
    }

    private Integer segmentSize;

    private Integer filled = 0;
    public Integer size(){
        return filled;
    }

    @Override
    public void write(int b) throws IOException {

    }

    public void writeTo(ByteBuffer buffer) {
        //segment usage
    }
}
