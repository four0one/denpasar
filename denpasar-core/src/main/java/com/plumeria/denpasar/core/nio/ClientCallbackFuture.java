package com.plumeria.denpasar.core.nio;

import com.plumeria.denpasar.core.ServiceRequest;
import com.plumeria.denpasar.core.ServiceRoute;
import com.plumeria.denpasar.util.ByteReader;
import com.plumeria.denpasar.util.SerializeUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by chenwei on 2016/12/14.
 */
public class ClientCallbackFuture implements Callable {

    private ServiceRequest request;

    private ServiceRoute serviceRoute;

    public ClientCallbackFuture(ServiceRequest request, ServiceRoute serviceRoute) {
        this.request = request;
        this.serviceRoute = serviceRoute;
    }

    @Override
    public Object call() throws Exception {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress(serviceRoute.getHost(), serviceRoute.getPort()));
        ByteBuffer buffer = ByteBuffer.wrap(SerializeUtil.serialize(request));
        sc.write(buffer);
        sc.shutdownOutput();

        //读取服务端返回
        byte[] bytes = ByteReader.readToByte(sc);
        Object o = SerializeUtil.deserialize(bytes);
        sc.close();
        return o;
    }
}
