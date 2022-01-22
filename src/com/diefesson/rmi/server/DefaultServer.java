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
        var deadClients = new HashSet<>();
        for (var client : clients) {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                deadClients.add(client);
            }
        }
        clients.removeAll(deadClients);
    }

    @Override
    public void addClient(Client client) throws RemoteException {
        clients.add(client);
        receiveMessage(new Message("server", client.username() + " entrou no chat"));
        System.out.println("client count: " + clients.size());
    }
}
