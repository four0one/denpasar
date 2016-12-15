package com.chenwei.denpasar.core.nio;

import com.chenwei.denpasar.core.InitContext;
import com.chenwei.denpasar.core.MethodHandler;
import com.chenwei.denpasar.core.ServiceRequest;
import com.chenwei.denpasar.util.ByteReader;
import com.chenwei.denpasar.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by chenwei on 2016/12/9.
 */
public class ServerDispatcher {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private InitContext initContext;

    public ServerDispatcher() {
        initContext = new InitContext();
    }

    public void listen(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 300);
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.keys();
                //选择器的keyset不带remove方法，因此将set放入集合中做迭代
                List<SelectionKey> listKey = new ArrayList<>();
                listKey.addAll(keys);
                ListIterator<SelectionKey> iterator = listKey.listIterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = ssc.accept();
                        if (socketChannel != null) {
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        byte[] bytes = ByteReader.readToByte(sc);
                        if (bytes.length == 0) {
                            continue;
                        }
                        ServiceRequest serviceMeta = (ServiceRequest) SerializeUtil.deserialize(bytes);
//                        log.debug("服务:" + serviceMeta.getServiceName());
//                        log.debug("方法:" + serviceMeta.getMethodName());
                        Object result = dispatch(serviceMeta);
                        ByteBuffer allocate = ByteBuffer.wrap(SerializeUtil.serialize(result));
                        sc.write(allocate);
                        sc.shutdownOutput();
                        sc.close();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Object dispatch(ServiceRequest serviceMeta) {
        MethodHandler handler = initContext.getHandler(serviceMeta.getServiceName(), serviceMeta.getMethodName());
        return handler.handle(serviceMeta.getArgs());
    }

}
