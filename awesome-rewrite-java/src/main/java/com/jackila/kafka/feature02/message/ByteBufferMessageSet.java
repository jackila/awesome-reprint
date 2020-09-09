package com.jackila.kafka.feature02.message;

import com.jackila.kafka.feature02.MessageAndOffset;
import com.jackila.kafka.feature02.MessageSet;
import com.jackila.kafka.feature02.TimestampType;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * 1: consumer 通过包含序列化的message set 的byteBuffer 构造
 * 2: producer 给它一个消息列表以及与序列化格式有关的说明
 *
 * @Author: jackila
 * @Date: 09:12 2020-08-24
 */
@Data
public class ByteBufferMessageSet extends MessageSet  {

    private ByteBuffer buffer;

    public ByteBufferMessageSet(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public Integer writerTo(GatheringByteChannel channel, Long offset, Integer maxSize) {
        return null;
    }

    @Override
    public Iterator<MessageAndOffset> iterator() {
        return null;
    }

    @Override
    protected boolean isMagicValueInAllWrapperMessages(byte expectedMagicValue) {
        return false;
    }

    @Override
    public Integer sizeInBytes() {
        return null;
    }

    private static class OffsetAssigner{

        private List<Long> offsets;
        private int index = 0;

        public OffsetAssigner(List<Long> offset) {
            this.offsets = offset;
        }

        public Long nextAbsoluteOffset(){
            Long result = offsets.get(index);
            index += 1;
            return result;
        }

        public Long toInnerOffset(Long offset){
            return offset - offsets.get(0);
        }

    }

    private static OffsetAssigner apply(AtomicLong offsetCounter,Integer size){

        List<Long> ret = new ArrayList<Long>();
        for (Long i = 0L; i < size; i++) {
            ret.add(i);
        }
        return new OffsetAssigner(ret);
    }



    public static ByteBuffer create(OffsetAssigner offsetAssigner,
                                    Message.CompressionEnum compressionCodec,
                                    AtomicLong offsetCounter,
                                    Optional<Long> wrapperMessageTimestamp,
                                    TimestampType timestampType,
                                    Message... messages
                                    ){
        if(messages.length == 0){
            return MessageSet.Empty.getBuffer();
        }else if (compressionCodec == Message.CompressionEnum.NoCompressionCodec){
            ByteBuffer buffer = ByteBuffer.allocate(MessageSet.messageSetSize(messages));
            for (Message message : messages) {
                writeMessage(buffer, message, offsetAssigner.nextAbsoluteOffset());
            }
            buffer.rewind();
            return buffer;
        }else{

            MagicAndTimestamp magicAndTimestamp;
            if(wrapperMessageTimestamp.isPresent()){
                magicAndTimestamp = new MagicAndTimestamp(messages[0].magic(),wrapperMessageTimestamp.get());
            }else{
                magicAndTimestamp = MessageSet.magicAndLargestTimestamp(messages);
            }
            Long offset = -1L;

            //create buffer according object message
            MessageWriter messageWriter = new MessageWriter(Math.min(Math.max(MessageSet.messageSetSize(messages) / 2, 1024), 1 << 16));
            messageWriter.write(null,compressionCodec, magicAndTimestamp.getTimestamp(),timestampType,magicAndTimestamp.getMagic());
            ByteBuffer buffer = ByteBuffer.allocate(messageWriter.size() + MessageSet.LogOverhead);
            writeMessage(buffer, messageWriter, offset);
            buffer.rewind();
            return buffer;
            //return magicAndTimestamp;
        }
    }

    public static Iterator<MessageAndOffset> deepIterator(MessageAndOffset wrapperMessageAndOffset){

        return null;
    }

    private static void writeMessage(ByteBuffer buffer, Message message, Long offset) {
        buffer.putLong(offset);
        buffer.putInt(message.size());
        buffer.put(message.getBuffer());
        //why
        message.getBuffer().rewind();
    }

    private static void writeMessage(ByteBuffer buffer, MessageWriter messageWriter, Long offset) {
        buffer.putLong(offset);
        buffer.putInt(messageWriter.size());
        messageWriter.writeTo(buffer);
    }


    /**---------------------------------------------------------------------inner method---------------------------------------------------------------------------------**/
    public Integer writeFullyTo(GatheringByteChannel channel) throws IOException {
        buffer.mark();
        Integer written = 0;
        while (written < sizeInBytes()){
            written += channel.write(buffer);
        }
        buffer.reset();
        return written;
    }
}
