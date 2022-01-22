package com.diefesson.rmi.server;

import com.diefesson.rmi.client.Client;
import com.diefesson.rmi.data.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    void receiveMessage(Message message) throws RemoteException;

    void addClient(Client client) throws RemoteException;
}
