package com.diefesson.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import com.diefesson.rmi.client.DefaultClient;
import com.diefesson.rmi.server.DefaultServer;
import com.diefesson.rmi.server.Server;

public class Main {

    public static final int PORT = 3000;
    public static final String URL = "rmi://localhost:" + PORT + "/server";

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    private static void startServer() {
        try {
            LocateRegistry.createRegistry(PORT);
            var server = new DefaultServer();
            Naming.rebind(URL, server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startClient() {
        try {
            var server = (Server) Naming.lookup(URL);
            var client0 = new DefaultClient("client 0");
            var client1 = new DefaultClient("client 1");
            client0.bindServer(server);
            client1.bindServer(server);
            client0.sendMessage("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}