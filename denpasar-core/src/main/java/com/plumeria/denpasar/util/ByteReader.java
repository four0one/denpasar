package com.plumeria.denpasar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ByteReader {

    private static Logger log = LoggerFactory.getLogger(ByteReader.class);

    public static byte[] readToByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int read = inputStream.read(buf);
        while (read != -1) {
            outSteam.write(buf);
            buf = new byte[2048];
            read = inputStream.read(buf);
        }
        outSteam.flush();
        byte[] bytes = outSteam.toByteArray();
        outSteam.close();
        return bytes;
    }

    public static byte[] readToByte(SocketChannel sc) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = sc.read(buffer);
        while (read != -1) {
            buffer.flip();
            outSteam.write(buffer.array());
            buffer.clear();
            read = sc.read(buffer);
        }
        buffer.clear();
        byte[] bytes = outSteam.toByteArray();
        outSteam.close();
        return bytes;
    }

}
