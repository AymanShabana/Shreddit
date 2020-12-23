package com.example.shreddit.ViewModels;

import android.app.Application;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostRepo;
import com.example.shreddit.Models.SubRepo;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Initial.RegisterFragment;
import com.example.shreddit.Views.SubAdapter;

import java.util.List;

public class SubViewModel extends AndroidViewModel {
    private SubRepo mRepository;
    private List<Board> mAllBoards;
    public SubViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SubRepo(application);
        //mAllBoards = mRepository.getAllBoards();
    }
    public List<Board> getAllBoards() { return mRepository.getAllBoards(); }
    public void insert(Board board, MyCallbackInterface success) { mRepository.insert(board,success); }
    public void updateBoard(Board board){ mRepository.updateBoard(board);}
    public void deleteBoards(Board... boards){ mRepository.deleteBoards(boards);}

    public void sendAdapter(SubAdapter adapter, ProgressBar progressBarSubs) {
        mRepository.sendAdapter(adapter, progressBarSubs);
    }
}
