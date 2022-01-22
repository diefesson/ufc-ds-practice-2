package com.diefesson.rmi.data;

public class PrintMessageListener implements MessageListener {

    @Override
    public void call(Message message) {
        System.out.printf("%s: %s%n", message.username, message.content);
    }

}
