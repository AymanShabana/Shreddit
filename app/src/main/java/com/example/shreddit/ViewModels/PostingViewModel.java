package com.example.shreddit.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostingModel;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.google.firebase.auth.FirebaseAuth;

public class PostingViewModel extends AndroidViewModel {
    private PostingModel postingModel;
    private FirebaseAuth auth;
    public PostingViewModel(@NonNull Application application) {
        super(application);
        postingModel = PostingModel.getInstance(application);
        auth = FirebaseAuth.getInstance();
    }
    public void insert(Post post, MyCallbackInterface success) {
        post.setAuthor(UserFirebaseModel.mUser.getUsername());
        new PostingViewModel.insertAsyncTask(postingModel,success).execute(post);
    }

    private static class insertAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostingModel mAsyncPostingModel;
        private MyCallbackInterface cb;
        public insertAsyncTask(PostingModel model, MyCallbackInterface success) {
            mAsyncPostingModel = model;
            cb = success;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            mAsyncPostingModel.insertPost(posts[0],cb);
            return null;
        }

    }

}
