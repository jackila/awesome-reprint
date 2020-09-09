package com.jackila.kafka.feature02;

import com.jackila.kafka.feature02.message.ByteBufferMessageSet;
import com.jackila.kafka.feature02.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @Author: jackila
 * @Date: 17:05 2020-08-18
 */
public abstract class MessageSet implements Iterable<MessageAndOffset>{


    public static Integer MessageSizeLength = 4;
    public static Integer OffsetLength = 8;
    public static Integer  LogOverhead = MessageSizeLength + OffsetLength;
    public static ByteBufferMessageSet  Empty = new ByteBufferMessageSet(ByteBuffer.allocate(0));

    protected static Integer messageSetSize(Message[] messages) {
        return Arrays.stream(messages).mapToInt(x->
            x.size() + LogOverhead
        ).sum();
    }

    protected static MagicAndTimestamp magicAndLargestTimestamp(Message[] messages) {
        Byte firstMagicValue = messages[0].magic();
        Long largestTimestamp = Message.NoTimestamp;
        for (Message message : messages) {
            if(!message.magic().equals(firstMagicValue)){
                throw new IllegalStateException("Messages in the same message set must have same magic value");
            }

            if (firstMagicValue > Message.MagicValue_V0) {
                largestTimestamp = Math.max(largestTimestamp, message.timestamp());
            }
        }
        return new MagicAndTimestamp(firstMagicValue, largestTimestamp);
    }


    public abstract Integer writerTo(GatheringByteChannel channel,Long offset,Integer maxSize);

    @Override
    public abstract Iterator<MessageAndOffset> iterator();

    protected abstract boolean isMagicValueInAllWrapperMessages(byte expectedMagicValue);

    public abstract Integer sizeInBytes();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName() + "(");
        Iterator<MessageAndOffset> iter = this.iterator();
        int i = 0;
        while(iter.hasNext() && i < 100) {
            MessageAndOffset message = iter.next();
            builder.append(message);
            if(iter.hasNext()) {
                builder.append(", ");
            }
            i += 1;
        }
        if(iter.hasNext()) {
            builder.append("...");
        }
        builder.append(")");
        return builder.toString();
    }

    @Data
    @AllArgsConstructor
    protected static class MagicAndTimestamp{
        private Byte magic;
        private Long timestamp;
    }
}
