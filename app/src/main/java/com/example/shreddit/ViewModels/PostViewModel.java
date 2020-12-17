package com.example.shreddit.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostRepo;
import com.example.shreddit.Views.HomeFragment;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private PostRepo mRepository;
    private LiveData<List<Post>> mAllPosts;
    public PostViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PostRepo(application);
        mAllPosts = mRepository.getAllPosts();
    }
    public LiveData<List<Post>> getAllPosts() { return mAllPosts; }
    public void insert(Post post) { mRepository.insert(post); }
    public void updatePost(Post post){ mRepository.updatePost(post);}
    public void deletePosts(Post... posts){ mRepository.deletePosts(posts);}
}
