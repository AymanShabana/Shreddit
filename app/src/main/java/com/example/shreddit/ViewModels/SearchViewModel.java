package com.example.shreddit.ViewModels;

import android.app.Application;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.SearchRepo;
import com.example.shreddit.Models.SubRepo;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private SearchRepo mRepository;
    private List<Board> mAllBoards;
    private List<Post> mAllPosts;
    public SearchViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SearchRepo(application);
    }
    public List<Board> getAllBoards(String s) { return mRepository.getAllBoards(s); }
    public List<Post> getAllPosts(String s) { return mRepository.getAllPosts(s); }

    public void sendAdapter(SubAdapter adapter, ProgressBar progressBarSubs) {
        mRepository.sendAdapter(adapter, progressBarSubs);
    }
    public void sendAdapterPosts(BoardAdapter adapter, ProgressBar progressBarSubs) {
        mRepository.sendAdapterPosts(adapter, progressBarSubs);
    }
}
