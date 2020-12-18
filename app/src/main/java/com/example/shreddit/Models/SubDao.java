package com.example.shreddit.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubDao {
    @Insert
    void insert(Board board);

    @Delete
    void deleteBoards(Board... boards);

    @Update
    public void updateBoard(Board board);

    @Query("DELETE FROM board_table")
    void deleteAll();

    @Query("SELECT * from board_table ORDER BY name ASC")
    LiveData<List<Board>> getAllBoards();

}