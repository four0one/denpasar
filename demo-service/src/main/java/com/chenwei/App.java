package com.chenwei;


import com.chenwei.denpasar.core.nio.ServerDispatcher;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
//        ServerDispatcher dispatcher = new ServerDispatcher();
//        dispatcher.listen(8281);

        ServerDispatcher dispatcher = new ServerDispatcher();
        dispatcher.listen(8281);
    }
}
