package com.diefesson.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import com.diefesson.rmi.server.DefaultServer;
import com.diefesson.rmi.ui.ConnectFrame;

public class Main {

    public static void main(String[] args) {
        startServer();
        startClient();
    }

    private static void startServer() {
        try {
            LocateRegistry.createRegistry(Consts.DEFAULT_PORT);
            var server = new DefaultServer();
            Naming.rebind(Consts.DEFAULT_URL, server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startClient() {
        var connectFrame = new ConnectFrame();
        connectFrame.setVisible(true);
    }

}