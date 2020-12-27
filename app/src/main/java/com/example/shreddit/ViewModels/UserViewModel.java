package com.example.shreddit.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostingModel;
import com.example.shreddit.Models.User;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserViewModel extends AndroidViewModel {
    private UserFirebaseModel model;
    public UserViewModel(@NonNull Application application) {
        super(application);
        model = UserFirebaseModel.getInstance(application);
    }
    private User getUser(){
        return model.getUser();
    }

    public void sendBinding(ActivityMainBinding binding) {
        model.binding = binding;
    }
}
