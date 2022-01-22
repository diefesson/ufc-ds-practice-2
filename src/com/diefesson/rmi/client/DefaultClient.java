package com.diefesson.rmi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.diefesson.rmi.data.Message;
import com.diefesson.rmi.data.MessageListener;
import com.diefesson.rmi.data.PrintMessageListener;
import com.diefesson.rmi.server.Server;

public class DefaultClient extends UnicastRemoteObject implements Client {

    private final transient String username;
    private transient Server server;
    private transient MessageListener onMessage;

    public DefaultClient(String username) throws RemoteException {
        this.username = username;
        onMessage(new PrintMessageListener());
    }

    public void bindServer(Server server) throws RemoteException {
        this.server = server;
        server.addClient(this);
    }

    public void sendMessage(String content) throws RemoteException {
        server.receiveMessage(new Message(username, content));
    }

    public void onMessage(MessageListener onMessage) {
        this.onMessage = onMessage;
    }

    @Override
    public void receiveMessage(Message message) throws RemoteException {
        onMessage.call(message);
    }

}
