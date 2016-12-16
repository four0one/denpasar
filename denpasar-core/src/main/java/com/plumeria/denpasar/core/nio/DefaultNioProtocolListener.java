package com.plumeria.denpasar.core.nio;

import com.plumeria.denpasar.core.InitContext;
import com.plumeria.denpasar.server.Protocol;
import com.plumeria.denpasar.server.ProtocolListener;

/**
 * Created by chenwei on 2016/12/16.
 */
public class DefaultNioProtocolListener implements ProtocolListener {

    private Protocol protocol;

    public DefaultNioProtocolListener(Protocol protocol) {
        this.protocol = protocol;
    }


    @Override
    public void listen(InitContext context) {
        ServerDispatcher dispatcher = new ServerDispatcher(context);
        dispatcher.listen(protocol.getPort());
    }
}
