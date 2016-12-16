package com.plumeria.denpasar.core.nio;

import com.plumeria.denpasar.core.InitContext;
import com.plumeria.denpasar.core.MethodHandler;
import com.plumeria.denpasar.core.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by chenwei on 2016/12/9.
 */
public class ServerDispatcher {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private InitContext initContext;

    private List<ChannelEventHandle> eventHandles = new ArrayList<>(4);

    private ChannelEvent channelEvent;

    public ServerDispatcher() {
        initContext = new InitContext();
        eventHandles.add(new AcceptEventHandle());
        eventHandles.add(new ReadAndWriteEventHandle());
        this.channelEvent = new ChannelEvent(this);
    }

    public ServerDispatcher(InitContext initContext) {
        this.initContext = initContext;
        eventHandles.add(new AcceptEventHandle());
        eventHandles.add(new ReadAndWriteEventHandle());
        this.channelEvent = new ChannelEvent(this);
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
                    //链式处理
                    for (ChannelEventHandle eventHandle : eventHandles) {
                        eventHandle.handle(key, channelEvent);
                    }
                    iterator.remove();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Object dispatch(ServiceRequest serviceMeta) {
        MethodHandler handler = initContext.getHandler(serviceMeta.getServiceName(), serviceMeta.getMethodName());
        return handler.handle(serviceMeta.getArgs());
    }


}
