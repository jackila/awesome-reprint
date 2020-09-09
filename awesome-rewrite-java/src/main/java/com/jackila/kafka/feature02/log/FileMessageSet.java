package com.jackila.kafka.feature02.log;

import com.jackila.kafka.KafkaException;
import com.jackila.kafka.feature02.Message;
import com.jackila.kafka.feature02.MessageSet;
import com.jackila.kafka.feature02.OffsetPosition;
import com.jackila.kafka.feature02.TransportLayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jackila
 * @Date: 17:00 2020-08-18
 */
public class FileMessageSet {



    private volatile File file;
    private FileChannel channel;
    private int start;
    private int end;
    private boolean isSlice;

    private final AtomicInteger _size = isSlice ? new AtomicInteger(end - start) : new AtomicInteger((int)Math.min(channel.size(), end) - start);


    public FileMessageSet() throws IOException {
        /* if this is not a slice, update the file pointer to the end of the file */
        if (!isSlice)
            /* set the file position to the last byte in the file */
            channel.position(Math.min(channel.size(), end));

    }

    public FileMessageSet( File file) throws IOException {

        this(file,FileMessageSet.openChannel(file, false, false, 0, false));
    }

    public FileMessageSet( File file,FileChannel channel) throws IOException {

        this(file,channel,0,Integer.MAX_VALUE,false);
    }

    public FileMessageSet( File file,boolean fileAlreadyExists,Integer initFileSize,boolean preallocate) throws IOException {

        this(file,FileMessageSet.openChannel(file, false, fileAlreadyExists, initFileSize, preallocate),0,
                !fileAlreadyExists && preallocate  ? 0 : Integer.MAX_VALUE,
                false);
    }

    public FileMessageSet(File file,Boolean mutable) throws IOException {
        this(file,FileMessageSet.openChannel(file, mutable, false, 0, false));
    }

    public FileMessageSet( File file,FileChannel channel,int start,int end) throws IOException {

        this(file,channel,start,end,false);
    }


    public FileMessageSet( File file,FileChannel channel,int start,int end,boolean isSlice) throws IOException {

        this.file = file;
        this.channel = FileMessageSet.openChannel(file, true,false,0,false);

        this.channel = channel;
        this.start = start;
        this.end = end;
        this.isSlice = isSlice;
    }

    public static FileChannel openChannel(File file,boolean mutable,boolean fileAlreadyExists,int initFileSize,boolean preallocate) throws IOException {

        if (mutable) {
            if (fileAlreadyExists) {
                return new RandomAccessFile(file, "rw").getChannel();
            } else {
                if (preallocate) {
                    final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.setLength(initFileSize);
                    return randomAccessFile.getChannel();

                }
                else {
                    return new RandomAccessFile(file, "rw").getChannel();
                }
            }
        }
        else {
            return new FileInputStream(file).getChannel();
        }
    }


    /**
     * read
     */
    public FileMessageSet read(Integer position,Integer size) throws IOException {
        //check

        //
        return new FileMessageSet(file,
                channel,
                start = this.start + position,
                end = Math.min(this.start + position + size, _size.get()));
    }

    public OffsetPosition searchFor(Long targetOffset, Integer startPosition) throws IOException {


        Integer position = startPosition;
        final ByteBuffer buffer = ByteBuffer.allocate(MessageSet.LogOverhead);
        final Integer size = _size.get();

        while(position + MessageSet.LogOverhead < size){

            buffer.rewind();
            channel.read(buffer, position);
            buffer.rewind();

            long offset = buffer.getLong();
            if(offset >= targetOffset){
                return new OffsetPosition(offset, position);
            }
            Integer messageSize = buffer.getInt();
            if(messageSize < Message.MinMessageOverhead){
                //throw excption
                throw new IllegalStateException("Invalid message size: " + messageSize);
            }
            position += MessageSet.LogOverhead + messageSize;
        }
        return null;
    }

    public Integer writeTo(GatheringByteChannel destChannel,Long writePosition,Integer size) throws IOException {

        Integer newSize = (int)Math.min(channel.size(), end) - start;
        if(newSize < _size.get()){
            throw new KafkaException(String.format("Size of FileMessageSet %s has been truncated during write: old size %d, new size %d",file.getAbsolutePath(), _size.get(), newSize));
        }

        Long position = start + writePosition;
        Integer count = Math.min(size, _size.get());


        Integer bytesTransferred = 0;
        if(destChannel instanceof TransportLayer){

            bytesTransferred = (int)((TransportLayer) destChannel).transferFrom(channel, position, count);
        }else{
            bytesTransferred = (int)channel.transferTo(position, count, destChannel);
        }

        return bytesTransferred;
    }

    boolean isMagicValueInAllWrapperMessages(Byte expectedMagicValue) {

        return true;
    }

}
