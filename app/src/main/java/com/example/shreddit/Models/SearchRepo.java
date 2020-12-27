package com.example.shreddit.Models;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.widget.ProgressBar;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.CommentAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;

import java.util.List;

public class SearchRepo {
    private SearchFirebaseModel fireBaseModel;
    private List<Post> mAllPosts;
    private List<Board> mAllBoards;
    public SearchRepo(Application application) {
        fireBaseModel = SearchFirebaseModel.getInstance(application);

    }

    public List<Board> getAllBoards(String s) {
        mAllBoards = fireBaseModel.getAllBoards(s);
        return mAllBoards;
    }
    public List<Post> getAllPosts(String s) {
        mAllPosts = fireBaseModel.getAllPosts(s);
        return mAllPosts;
    }


    public void sendAdapter(SubAdapter adapter, ProgressBar progressBarSubs) {
        fireBaseModel.subAdapter = adapter;
        fireBaseModel.progressBar = progressBarSubs;
    }
    public void sendAdapterPosts(BoardAdapter adapter, ProgressBar progressBarSubs) {
        fireBaseModel.postAdapter = adapter;
        fireBaseModel.progressBarPosts = progressBarSubs;
    }

    public void clean() {
        SearchFirebaseModel.INSTANCE=null;
    }

}
