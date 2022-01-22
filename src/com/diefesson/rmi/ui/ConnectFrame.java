package com.diefesson.rmi.ui;

import javax.swing.*;

import com.diefesson.rmi.Consts;
import com.diefesson.rmi.client.DefaultClient;
import com.diefesson.rmi.server.Server;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;

public class ConnectFrame extends JFrame {

    private JTextField usernameField;
    private JTextField urlField;
    private JButton connectButton;
    private JLabel errorLabel;

    public ConnectFrame() {
        setupComponents();
        setupEvents();
        pack();
    }

    private void setupComponents() {
        setLocationByPlatform(true);

        var usernameLabel = new JLabel("username:");
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        usernameField = new JTextField(Consts.DEFAULT_USERNAME);
        usernameField.setColumns(40);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        var usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.EAST);

        var urlLabel = new JLabel("url:");
        urlLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        urlField = new JTextField(Consts.DEFAULT_URL);
        urlField.setColumns(40);
        urlField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        var urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));
        urlPanel.add(urlLabel);
        urlPanel.add(urlField);

        var fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        fieldsPanel.add(usernamePanel);
        fieldsPanel.add(urlPanel);

        connectButton = new JButton("connect");
        connectButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 50));

        errorLabel = new JLabel(" ");
        connectButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        var content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(fieldsPanel, BorderLayout.CENTER);
        content.add(connectButton, BorderLayout.EAST);
        content.add(errorLabel, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        connectButton.addActionListener((ActionEvent e) -> connect());
    }

    private void connect() {
        var url = urlField.getText();
        var username = usernameField.getText();
        try {
            var server = (Server) Naming.lookup(url);
            var client = new DefaultClient(username);
            client.bindServer(server);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

}
