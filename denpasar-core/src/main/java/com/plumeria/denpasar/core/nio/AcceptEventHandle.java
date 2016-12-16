package com.plumeria.denpasar.core.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by chenwei on 2016/12/16.
 */
public class AcceptEventHandle implements ChannelEventHandle{

    public AcceptEventHandle(){

    }

    @Override
    public void handle(SelectionKey key,ChannelEvent event) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = ssc.accept();
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                socketChannel.register(key.selector(), SelectionKey.OP_READ);
            }
        }
    }
}
