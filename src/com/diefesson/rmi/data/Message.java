package com.diefesson.rmi.data;

import java.io.Serializable;

public class Message implements Serializable {
    public final String username;
    public final String content;

    public Message(String username, String content) {
        this.username = username;
        this.content = content;
    }
}
