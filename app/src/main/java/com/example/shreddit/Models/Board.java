package com.example.shreddit.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "board_table")
public class Board {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "header_img")
    private String header_img;

    @ColumnInfo(name = "icon_img")
    private String icon_img;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "subscribers")
    private int subscribers;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created")
    private long created;

    public Board(@NonNull String id, @NonNull String name, String title, String header_img, String icon_img, String color, int subscribers, String description, long created) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.header_img = header_img;
        this.icon_img = icon_img;
        this.color = color;
        this.subscribers = subscribers;
        this.description = description;
        this.created = created;
    }
    public Board(){}

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader_img() {
        return header_img;
    }

    public String getIcon_img() {
        return icon_img;
    }

    public String getColor() {
        return color;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public String getDescription() {
        return description;
    }

    public long getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", header_img='" + header_img + '\'' +
                ", icon_img='" + icon_img + '\'' +
                ", color='" + color + '\'' +
                ", subscribers=" + subscribers +
                ", description='" + description + '\'' +
                ", created=" + created +
                '}';
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeader_img(String header_img) {
        this.header_img = header_img;
    }

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
