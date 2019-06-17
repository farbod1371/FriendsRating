package com.example.elessar1992.friendsrating.Models;

/**
 * Created by elessar1992 on 6/11/19.
 */

public class ChatModel
{
    String message;
    String receiver;
    String sender;
    String timestamp;
    boolean seen;

    public ChatModel(){}

    public ChatModel(String message, String receiver, String sender, String timestamp, boolean seen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
