package com.diefesson.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.diefesson.rmi.data.Message;

public interface Client extends Remote {
    void receiveMessage(Message message) throws RemoteException;
}
