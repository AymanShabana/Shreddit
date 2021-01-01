package com.example.shreddit.Models;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.widget.ProgressBar;

import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.PostAdapter;

import java.util.List;

public class PostRepo {
    private PostDao mPostDao;
    private PostFirebaseModel fireBaseModel;
    private List<Post> mAllPosts;
    private List<Post> mAllBoardPosts;
    private ConnectivityManager cm;

    public PostRepo(Application application) {
        PostDB db = PostDB.getDatabase(application);
        mPostDao = db.postDao();
        //mAllPosts = mPostDao.getAllPosts();
        fireBaseModel = PostFirebaseModel.getInstance(application);
        cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

    }
    public void updatePost(Post post){ mPostDao.updatePost(post);}
    public void deletePosts(Post... posts){ mPostDao.deletePosts(posts);}

    public List<Post> getAllPosts() {
        if(Build.VERSION.SDK_INT >= 23){
            Network activeNetwork = cm.getActiveNetwork();
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
            boolean validated = networkCapabilities == null
                    || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            if(validated) {
                mAllPosts = fireBaseModel.getAllPosts();
            }
            else{
                //mAllPosts = mPostDao.getAllPosts();
            }
        }
        mAllPosts = fireBaseModel.getAllPosts();
        return mAllPosts;
    }
    public List<Post> getAllPostsBoard(String board) {
        mAllBoardPosts = fireBaseModel.getAllPostsBoard(board);
        return mAllBoardPosts;
    }

    public void sendAdapter(PostAdapter adapter, ProgressBar progressBarSubs) {
        fireBaseModel.adapter = adapter;
        fireBaseModel.progressBar = progressBarSubs;
    }

    public void sendAdapterBoard(BoardAdapter adapter, ProgressBar progressBarSubs) {
        fireBaseModel.adapterBoard = adapter;
        fireBaseModel.progressBarBoard = progressBarSubs;

    }

    public List<Post> getPagedPosts(int page) {
        return fireBaseModel.getPagedPosts(page);
    }
}
