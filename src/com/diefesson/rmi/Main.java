package com.diefesson.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

import com.diefesson.rmi.server.DefaultServer;
import com.diefesson.rmi.ui.ChatFrame;

public class Main {

    public static void main(String[] args) {
        var argsList = Arrays.asList(args);
        if (argsList.contains("--help")) {
            System.out.println("use --server to start server, no args to start client");
        } else if (argsList.contains("--server")) {
            startServer();
        } else {
            startClient();
        }

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
        var connectFrame = new ChatFrame();
        connectFrame.setVisible(true);
    }

}