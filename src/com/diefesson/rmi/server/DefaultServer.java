package com.diefesson.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import com.diefesson.rmi.client.Client;
import com.diefesson.rmi.data.Message;

public class DefaultServer extends UnicastRemoteObject implements Server {

    private transient HashSet<Client> clients;

    public DefaultServer() throws RemoteException {
        clients = new HashSet<>();
    }

    @Override
    public void receiveMessage(Message message) throws RemoteException {
        for (var client : clients) {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                clients.remove(client);
            }
        }
    }

    @Override
    public void addClient(Client client) throws RemoteException {
        clients.add(client);
        System.out.println("client count: " + clients.size());
    }
}
