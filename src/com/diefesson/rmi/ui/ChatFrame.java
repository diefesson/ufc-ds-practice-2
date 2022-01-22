package com.diefesson.rmi.ui;

import com.diefesson.rmi.Consts;
import com.diefesson.rmi.Strings;
import com.diefesson.rmi.client.DefaultClient;
import com.diefesson.rmi.data.Message;
import com.diefesson.rmi.server.Server;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatFrame extends JFrame {

    private JTextField usernameField;
    private JTextField urlField;
    private JButton connectButton;
    private JLabel errorLabel;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    private DefaultClient client;
    private boolean connected;

    public ChatFrame() {
        setupComponents();
        setupEvents();
        pack();
        setLocationByPlatform(true);
        setResizable(false);
        setConnected(false);
    }

    private void setupComponents() {
        var content = getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(setupTopComponents());
        content.add(new JSeparator());
        content.add(setupBottomComponents());
    }

    private JPanel setupTopComponents() {
        var usernameLabel = new JLabel(Strings.USERNAME);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        var urlLabel = new JLabel(Strings.URL);
        urlLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        var labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.add(usernameLabel);
        labelsPanel.add(Box.createVerticalStrut(5));
        labelsPanel.add(urlLabel);

        usernameField = new JTextField(Consts.DEFAULT_USERNAME);
        usernameField.setColumns(40);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        urlField = new JTextField(Consts.DEFAULT_URL);
        urlField.setColumns(40);
        urlField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        var fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(Box.createVerticalStrut(5));
        fieldsPanel.add(urlField);

        var connectForm = new JPanel();
        connectForm.setLayout(new BoxLayout(connectForm, BoxLayout.X_AXIS));
        connectForm.add(labelsPanel);
        connectForm.add(fieldsPanel);

        connectButton = new JButton(Strings.CONNECT);

        errorLabel = new JLabel(" ");

        var top = new JPanel();
        var topLayout = new BorderLayout();
        topLayout.setHgap(3);
        topLayout.setVgap(3);
        top.setLayout(topLayout);
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(connectForm, BorderLayout.CENTER);
        top.add(connectButton, BorderLayout.EAST);
        top.add(errorLabel, BorderLayout.SOUTH);
        return top;
    }

    private JPanel setupBottomComponents() {
        chatArea = new JTextArea();
        chatArea.setColumns(72);
        chatArea.setRows(20);
        chatArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatArea.setEditable(false);

        var chatScroll = new JScrollPane(
                chatArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        messageField = new JTextField();
        messageField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        sendButton = new JButton(Strings.SEND);

        var messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        messagePanel.add(messageField);
        messagePanel.add(Box.createHorizontalStrut(5));
        messagePanel.add(sendButton);

        var bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottom.add(chatScroll);
        bottom.add(Box.createVerticalStrut(5));
        bottom.add(messagePanel);
        return bottom;
    }

    private void setupEvents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        connectButton.addActionListener(this::onConnectButton);
        sendButton.addActionListener(this::onSendButton);
        messageField.addActionListener(this::onSendButton);
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
        usernameField.setEnabled(!connected);
        urlField.setEnabled(!connected);
        connectButton.setText((connected) ? Strings.DISCONNECT : Strings.CONNECT);
        messageField.setEnabled(connected);
        sendButton.setEnabled(connected);
        if (connected) {
            errorLabel.setText("");
        }
    }

    private void connect() {
        var url = urlField.getText();
        var username = usernameField.getText();
        try {
            var server = (Server) Naming.lookup(url);
            client = new DefaultClient(username);
            client.onMessage(this::onMessage);
            client.bindServer(server);
            setConnected(true);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            UnicastRemoteObject.unexportObject(client, true);
            setConnected(false);
        } catch (NoSuchObjectException e) {
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onConnectButton(ActionEvent event) {
        if (connected) {
            disconnect();
        } else {
            connect();
        }
    }

    private void onSendButton(ActionEvent event) {
        var content = messageField.getText();
        try {
            client.sendMessage(content);
            messageField.setText("");
        } catch (RemoteException e) {
            client.onMessage(m -> {
            });
            setConnected(false);
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onMessage(Message m) {
        chatArea.append(String.format("%s: %s%n", m.username, m.content));
        if (chatArea.getLineCount() > 200) {
            try {
                var end = chatArea.getLineEndOffset(0);
                chatArea.replaceRange("", 0, end);
            } catch (BadLocationException e) {
                // Estranho usar essa exceção... e ainda ser do tipo obrigatoria
                e.printStackTrace();
            }
        }
    }

}
