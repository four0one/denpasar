package com.plumeria.denpasar.core;

import com.plumeria.denpasar.util.ByteReader;
import com.plumeria.denpasar.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by chenwei on 2016/12/9.
 */
public class DispatcherProcessor implements Runnable {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Socket socket;

    private InitContext initContext;


    public DispatcherProcessor() {

    }

    public DispatcherProcessor(Socket socket, InitContext initContext) {
        this.socket = socket;
        this.initContext = initContext;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            log.debug("获得输入流成功");
            byte[] bytes = ByteReader.readToByte(inputStream);
            ServiceRequest serviceMeta = ServiceRequest.class.cast(SerializeUtil.deserialize(bytes));
            log.debug("服务:" + serviceMeta.getServiceName());
            log.debug("方法:" + serviceMeta.getMethodName());
            Object result = dispatch(serviceMeta);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(SerializeUtil.serialize(result));
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object dispatch(ServiceRequest serviceMeta) {
        MethodHandler handler = initContext.getHandler(serviceMeta.getServiceName(), serviceMeta.getMethodName());
        return handler.handle(serviceMeta.getArgs());
    }

}
