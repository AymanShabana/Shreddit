package com.example.shreddit.ViewModels;

import android.app.Application;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Chat;
import com.example.shreddit.Models.ChatFireBaseModel;
import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.PostFirebaseModel;
import com.example.shreddit.Models.User;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.ChatAdapter;
import com.example.shreddit.Views.Adapters.PostAdapter;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private UserFirebaseModel userFireBaseModel;
    private PostFirebaseModel postFireBaseModel;
    private List<Post> mAllPosts;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userFireBaseModel = UserFirebaseModel.getInstance(application);
        postFireBaseModel = PostFirebaseModel.getInstance(application);
    }
    public List<Post> getAllMyPosts() { return postFireBaseModel.getAllMyPosts(); }
    public void updateProfile(User user, MyCallbackInterface success) { userFireBaseModel.updateUser(user,success); }

    public void sendAdapter(PostAdapter adapter, ProgressBar progressBarSubs) {
        postFireBaseModel.myProgressBar=progressBarSubs;
        postFireBaseModel.myAdapter=adapter;
    }

}
