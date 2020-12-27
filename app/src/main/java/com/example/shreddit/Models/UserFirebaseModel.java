package com.example.shreddit.Models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.shreddit.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFirebaseModel {
    public ActivityMainBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference mRootRef;
    private static UserFirebaseModel INSTANCE;
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public User getUser(){
        return mUser;
    }

}
