package com.jackila.jdk;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * 1.FileChannel的用法
 * 2.FileChannel的优点
 * 3.FileChannel在不同系统中的区别、实现（锁）
 *
 * @Author: jackila
 * @Date: 14:15 2020-08-19
 */
public class FIleChannelUsage {

    String fileName = "channle.txt";
    String content = "世界那么大，我想出去走走";

    @Test
    public void writeUsage() throws IOException {

        File file = new File("/Users/jackila/data/" + fileName);

        if(file.exists()){
            file.delete();
        }
        FileChannel channel = new FileOutputStream(file).getChannel();

        byte[] contentBytes = content.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(contentBytes);
        byteBuffer.flip();

        channel.write(byteBuffer);

        channel.close();
    }

    @Test
    public void readUsage() throws IOException {

        File file = new File("/Users/jackila/data/" + fileName);

        FileChannel channel = new FileInputStream(file).getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);

        byte[] bytes = new byte[byteBuffer.position()];
        byteBuffer.flip();
        byteBuffer.get(bytes);

        System.out.println(new String(bytes,"UTF-8"));

    }
}
