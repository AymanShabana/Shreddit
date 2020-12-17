package com.example.shreddit.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.InitialModel;
import com.example.shreddit.Views.Initial.RegisterFragment;
import com.google.firebase.auth.FirebaseUser;

public class InitialViewModel extends AndroidViewModel {
    private InitialModel mModel;
    public InitialViewModel(@NonNull Application application){
        super(application);
        mModel = InitialModel.getInstance(application);
    }
    public void loginUser(String emailTxt, String passTxt, RegisterFragment.MyCallbackInterface success){ mModel.loginUser(emailTxt, passTxt, success);}
    public void register(String username, String emailTxt, String passTxt, RegisterFragment.MyCallbackInterface success){ mModel.register(username,emailTxt, passTxt, success);}
    public void logout(){ mModel.logout();}
    public FirebaseUser getUser(){ return mModel.getUser();}
}
