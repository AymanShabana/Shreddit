package com.example.shreddit.Models;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.shreddit.Views.HomeFragment;

import java.util.List;

public class PostRepo {
    private PostDao mPostDao;
    private LiveData<List<Post>> mAllPosts;
    public PostRepo(Application application) {
        PostDB db = PostDB.getDatabase(application);
        mPostDao = db.postDao();
        mAllPosts = mPostDao.getAllPosts();
    }
    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }
    public void insert (Post post) {
        new insertAsyncTask(mPostDao).execute(post);
    }
    public void updatePost(Post post){ mPostDao.updatePost(post);}
    public void deletePosts(Post... posts){ mPostDao.deletePosts(posts);}

    private static class insertAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDao mAsyncTaskDao;
        public insertAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            mAsyncTaskDao.insert(posts[0]);
            return null;
        }
    }


}
