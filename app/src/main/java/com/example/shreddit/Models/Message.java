package com.example.shreddit.Models;

import java.io.Serializable;

public class Message implements Serializable, Comparable<Message> {

    private String id;
    private String icon_img;
    private String sender;
    private String message;
    private long timestamp;

    public Message(String id, String icon_img, String sender,String message, long timestamp) {
        this.id = id;
        this.icon_img = icon_img;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public String getIcon_img() {
        return icon_img;
    }

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Message message) {
        return (int) (this.timestamp - message.timestamp);
    }
}
