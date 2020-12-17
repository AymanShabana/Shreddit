package com.example.shreddit.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    @Insert
    void insert(Post post);

    @Delete
    void deletePosts(Post... posts);

    @Update
    public void updatePost(Post post);

    @Query("DELETE FROM post_table")
    void deleteAll();

    @Query("SELECT * from post_table ORDER BY created DESC")
    LiveData<List<Post>> getAllPosts();

}
