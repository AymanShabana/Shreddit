package com.example.shreddit.ViewModels;

import android.app.Application;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostRepo;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.PostAdapter;

import java.util.List;

public class PostViewModel extends AndroidViewModel {
    private PostRepo mRepository;
    private List<Post> mAllPosts;
    private List<Post> mAllBoardPosts;
    public PostViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PostRepo(application);
        mAllPosts = mRepository.getAllPosts();
    }
    public List<Post> getAllPosts() {
        mAllPosts = mRepository.getAllPosts();
        return mAllPosts;
    }
    //public void insert(Post post) { mRepository.insert(post); }
    public void updatePost(Post post){ mRepository.updatePost(post);}
    public void deletePosts(Post... posts){ mRepository.deletePosts(posts);}

    public void sendAdapter(PostAdapter adapter, ProgressBar progressBarSubs) {
        mRepository.sendAdapter(adapter, progressBarSubs);
    }
    public void sendAdapterBoard(BoardAdapter adapter, ProgressBar progressBarSubs) {
        mRepository.sendAdapterBoard(adapter, progressBarSubs);
    }
    public List<Post> getAllBoardPosts(String board) {
        mAllBoardPosts = mRepository.getAllPostsBoard(board);
        return mAllBoardPosts;
    }
    public List<Post> getPagedPosts(int page) {
        mAllPosts = mRepository.getPagedPosts(page);
        return mAllPosts;
    }

}
