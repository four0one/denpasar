package com.plumeria;


import com.plumeria.denpasar.core.nio.ServerDispatcher;

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
