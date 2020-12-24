package com.example.shreddit.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Comment;
import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostDetailsRepo;
import com.example.shreddit.Models.PostingModel;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostDetailsViewModel extends AndroidViewModel {
    private PostDetailsRepo postDetailsRepo;
    private FirebaseAuth auth;
    public PostDetailsViewModel(@NonNull Application application,String postId) {
        super(application);
        postDetailsRepo = new PostDetailsRepo(application,postId);
        auth = FirebaseAuth.getInstance();
    }
    public List<Comment> getAllComments() { return postDetailsRepo.getAllComments(); }
    public void insert(Comment comment, MyCallbackInterface success) {
        comment.setAuthor(auth.getCurrentUser().getUid());
        new PostDetailsViewModel.insertAsyncTask(postDetailsRepo,success).execute(comment);
    }

    private static class insertAsyncTask extends AsyncTask<Comment, Void, Void> {
        private PostDetailsRepo mAsyncPostingModel;
        private MyCallbackInterface cb;
        public insertAsyncTask(PostDetailsRepo model, MyCallbackInterface success) {
            mAsyncPostingModel = model;
            cb = success;
        }

        @Override
        protected Void doInBackground(Comment... comments) {
            mAsyncPostingModel.insertPost(comments[0],cb);
            return null;
        }

    }

}
