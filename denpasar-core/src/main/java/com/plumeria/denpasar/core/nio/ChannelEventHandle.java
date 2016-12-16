package com.plumeria.denpasar.core.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by chenwei on 2016/12/16.
 * 服务端通道处理接口
 */
public interface ChannelEventHandle {

    void handle(SelectionKey key,ChannelEvent event) throws IOException;

}
