package com.example.shreddit.ViewModels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PostDetailsViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mPostId;


    public PostDetailsViewModelFactory(Application application, String postId) {
        mApplication = application;
        mPostId = postId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PostDetailsViewModel(mApplication, mPostId);
    }
}
