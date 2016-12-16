package com.plumeria.denpasar.core.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Created by chenwei on 2016/12/14.
 */
public class ClientSelector {

    private Selector selector;

    private static ClientSelector clientSelector;

    private ClientSelector() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerChannel(SelectableChannel selectableChannel) {
        try {
            selectableChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public static ClientSelector newInstance() {
        if (clientSelector == null) {
            synchronized (ClientSelector.class) {
                if (clientSelector == null) {
                    clientSelector = new ClientSelector();
                }
            }
        }
        return clientSelector;
    }

}
