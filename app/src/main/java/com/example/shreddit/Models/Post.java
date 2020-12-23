package com.example.shreddit.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "post_table")
public class Post {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "board")
    private String board;

    @ColumnInfo(name = "board_icon")
    private String boardIcon;

    @ColumnInfo(name = "post_img")
    private String postImg;

    @ColumnInfo(name = "self_text")
    private String selfText;

    @NonNull
    @ColumnInfo(name = "upvotes")
    private int upvotes;

    @NonNull
    @ColumnInfo(name = "comments")
    private int comments;

    @NonNull
    @ColumnInfo(name = "created")
    private long created;

    @NonNull
    @ColumnInfo(name = "link")
    private String link;

    @NonNull
    @ColumnInfo(name = "author")
    private String author;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    public Post(String id, @NonNull String title, @NonNull String board, String boardIcon, String postImg,String selfText, int upvotes, int comments, long created, String link, String author, String type) {
        this.id = id;
        this.title = title;
        this.board = board;
        this.boardIcon = boardIcon;
        this.postImg = postImg;
        this.selfText = selfText;
        this.upvotes = upvotes;
        this.comments = comments;
        this.created = created;
        this.link = link;
        this.author = author;
        this.type = type;
    }
    public Post(){}

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setBoard(@NonNull String board) {
        this.board = board;
    }

    public void setBoardIcon(String boardIcon) {
        this.boardIcon = boardIcon;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public void setSelfText(String selfText) {
        this.selfText = selfText;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setLink(@NonNull String link) {
        this.link = link;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getId() {
        return id;
    }
    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getBoard() {
        return board;
    }

    public String getBoardIcon() {
        return boardIcon;
    }

    public String getPostImg() {
        return postImg;
    }
    public String getSelfText() {
        return selfText;
    }
    public String getType() {
        return type;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getComments() {
        return comments;
    }

    public String getLink() {
        return link;
    }
    public String getAuthor() {
        return author;
    }
    public long getCreated() {
        return created;
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

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", board='" + board + '\'' +
                ", boardIcon='" + boardIcon + '\'' +
                ", postImg='" + postImg + '\'' +
                ", upvotes=" + upvotes +
                ", comments=" + comments +
                ", created=" + created +
                ", link='" + link + '\'' +
                ", type='" + type + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}