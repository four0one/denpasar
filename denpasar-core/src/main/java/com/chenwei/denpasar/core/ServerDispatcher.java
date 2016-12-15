package com.chenwei.denpasar.core;

import com.chenwei.denpasar.util.ByteReader;
import com.chenwei.denpasar.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenwei on 2016/12/9.
 */
public class ServerDispatcher {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);

    private InitContext initContext;

    public ServerDispatcher() {
        initContext = new InitContext();
    }

    public void listen(int port) {
        try {
            ServerSocket server = new ServerSocket(port, 100, InetAddress.getByName("127.0.0.1"));
            DispatcherProcessor processor;
            while (true) {
                log.debug("启动成功服务端");
                Socket accept = server.accept();
                processor = new DispatcherProcessor(accept, initContext);
                fixedThreadPool.execute(processor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
