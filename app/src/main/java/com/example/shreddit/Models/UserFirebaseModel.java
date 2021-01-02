package com.example.shreddit.Models;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.PostAdapter;
import com.example.shreddit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserFirebaseModel {
    public ActivityMainBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference mRootRef;
    public static UserFirebaseModel INSTANCE;
    private static Context mContext;
    public static User mUser;

    public static UserFirebaseModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (UserFirebaseModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserFirebaseModel();
                }
            }
        }
        return INSTANCE;
    }

    public UserFirebaseModel() {
        this.auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
        this.mRootRef.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser = snapshot.getValue(User.class);
                binding.username.setText(mUser.getUsername());
                binding.emailProfile.setText(mUser.getEmail());
                if(!mUser.getImageUrl().isEmpty()){
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .build();
                    imageLoader.displayImage(UserFirebaseModel.mUser.getImageUrl(), binding.userPicture, options);
                    imageLoader.displayImage(UserFirebaseModel.mUser.getImageUrl(), binding.profileImage, options);

                }
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+ UserFirebaseModel.mUser.getUsername_c());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public User getUser(){
        return mUser;
    }
    public void updateUser(User user, MyCallbackInterface myCallbackInterface){
        this.mRootRef.child("Users").child(auth.getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            myCallbackInterface.onAuthFinished("success");
                        }
                        else{
                            myCallbackInterface.onAuthFinished(task.getException().getLocalizedMessage());

                        }
                    }
                });
    }

}
