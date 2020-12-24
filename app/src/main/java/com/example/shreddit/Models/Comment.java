package com.example.shreddit.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "comment_table")
public class Comment {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "icon_img")
    private String icon_img;

    @ColumnInfo(name = "upvotes")
    private int upvotes;

    @ColumnInfo(name = "created")
    private long created;

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public Comment(@NonNull String id, @NonNull String author, String icon_img, int upvotes, long created) {
        this.id = id;
        this.author = author;
        this.icon_img = icon_img;
        this.upvotes = upvotes;
        this.created = created;
    }

    public Comment() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }

    public String getIcon_img() {
        return icon_img;
    }

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
    public String getTimeSinceCreation() {
        long s = (long) ((new Date().getTime() - created*1000)/1000.0f);//seconds
        String res = s+"s";
        if(s>=60) {
            s /= 60.0f;//minutes
            res = s+"m";
            if(s>=60) {
                s /= 60.0f;//hours
                res = s+"h";
                if(s>=24) {
                    s /= 24.0f;//days
                    res = s+"d";
                    if(s>=7){
                        s/= 7.0f;//weeks
                        res = s+"w";
                        if(s>=4){
                            s/=4.0f;//months
                            res = s+"m";
                            if(s>=12){
                                s/=12.0f;//years
                                res = s+"y";
                            }
                        }
                    }
                }
            }
        }
        return res;

    }

}
