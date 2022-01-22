package com.diefesson.rmi.data;

@FunctionalInterface
public interface MessageListener {
    void call(Message message);
}
