package com.plumeria.denpasar.core.nio;

import com.plumeria.denpasar.core.ServiceRequest;
import com.plumeria.denpasar.util.ByteReader;
import com.plumeria.denpasar.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by chenwei on 2016/12/16.
 */
public class ReadAndWriteEventHandle implements ChannelEventHandle {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(SelectionKey key, ChannelEvent event) throws IOException {
        if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            byte[] bytes = ByteReader.readToByte(sc);
            if (bytes.length != 0) {
                ServiceRequest serviceMeta = (ServiceRequest) SerializeUtil.deserialize(bytes);
                log.debug("服务:" + serviceMeta.getServiceName());
                log.debug("方法:" + serviceMeta.getMethodName());
                Object result = event.dispatch(serviceMeta);
                ByteBuffer allocate = ByteBuffer.wrap(SerializeUtil.serialize(result));
                sc.write(allocate);
                sc.shutdownOutput();
                sc.close();
            }
        }
    }
}
