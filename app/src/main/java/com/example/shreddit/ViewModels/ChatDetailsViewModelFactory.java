package com.example.shreddit.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatDetailsViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mChatId;


    public ChatDetailsViewModelFactory(Application application, String chatId) {
        mApplication = application;
        mChatId = chatId;
        Log.i("chatid","ChatDetailsViewModelFactory:"+chatId);
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ChatDetailsViewModel(mApplication, mChatId);
    }
}
