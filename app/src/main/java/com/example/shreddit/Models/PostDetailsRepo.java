package com.example.shreddit.Models;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.widget.ProgressBar;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.CommentAdapter;
import com.example.shreddit.Views.Adapters.PostAdapter;

import java.util.List;

public class PostDetailsRepo {
    private PostDetailsFirebaseModel fireBaseModel;
    private List<Comment> mAllComments;
    private ConnectivityManager cm;
    public PostDetailsRepo(Application application, String postId) {
        fireBaseModel = PostDetailsFirebaseModel.getInstance(application,postId);
        cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    public List<Comment> getAllComments() {
        if(Build.VERSION.SDK_INT >= 23){
            Network activeNetwork = cm.getActiveNetwork();
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
            boolean validated = networkCapabilities == null
                    || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            if(validated) {
                mAllComments = fireBaseModel.getAllComments();
            }
            else{
                //mAllPosts = mPostDao.getAllPosts();
            }
        }
        mAllComments = fireBaseModel.getAllComments();
        return mAllComments;
    }

    public void insert(Comment comment, MyCallbackInterface success) {
        fireBaseModel.insert(comment,success);
    }

    public void sendAdapter(CommentAdapter adapter, ProgressBar progressBarSubs) {
        fireBaseModel.adapter = adapter;
        fireBaseModel.progressBar = progressBarSubs;
    }

}
