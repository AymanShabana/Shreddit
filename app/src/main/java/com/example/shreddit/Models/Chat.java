package com.example.shreddit.Models;

import java.io.Serializable;

public class Chat implements Serializable, Comparable<Chat> {

    private String id;
    private String name1;
    private String name2;
    private String name1_c;
    private String name2_c;
    private String lastMessage;
    private String userImage1;
    private String userImage2;
    private long lastUpdated;

    public Chat() {
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public Chat(String id, String name1, String name2, String lastMessage, String userImage1, String userImage2, long lastUpdated) {
        this.id = id;
        this.name1 = name1;
        this.name1_c = name1.toUpperCase();
        this.name2 = name2;
        this.name2_c = name2.toUpperCase();
        this.lastMessage = lastMessage;
        this.userImage1 = userImage1;
        this.userImage2 = userImage2;
        this.lastUpdated = lastUpdated;
    }

    public String getName1_c() {
        return name1_c;
    }

    public void setName1_c(String name1_c) {
        this.name1_c = name1_c;
    }

    public String getName2_c() {
        return name2_c;
    }

    public void setName2_c(String name2_c) {
        this.name2_c = name2_c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserImage1() {
        return userImage1;
    }

    public void setUserImage1(String userImage1) {
        this.userImage1 = userImage1;
    }

    public String getUserImage2() {
        return userImage2;
    }

    public void setUserImage2(String userImage2) {
        this.userImage2 = userImage2;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(Chat chat) {
        return (int) (chat.lastUpdated - this.lastUpdated);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name1_c='" + name1_c + '\'' +
                ", name2_c='" + name2_c + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", userImage1='" + userImage1 + '\'' +
                ", userImage2='" + userImage2 + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
