package com.example.shreddit.Models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.shreddit.Views.Initial.InitialActivity;
import com.example.shreddit.Views.Initial.RegisterFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class BoardFireBaseModel {
    private DatabaseReference mRootRef;
    private static BoardFireBaseModel INSTANCE;
    private static Context mContext;

    public static BoardFireBaseModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (BoardFireBaseModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BoardFireBaseModel();
                }
            }
        }
        return INSTANCE;
    }

    public BoardFireBaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
    }
    public void insert(Board board, RegisterFragment.MyCallbackInterface cb) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("name",board.getName());
        map.put("title",board.getTitle());
        map.put("header_img",board.getHeader_img());
        map.put("icon_img",board.getIcon_img());
        map.put("color",board.getColor());
        map.put("subscribers",board.getSubscribers());
        map.put("description",board.getDescription());
        map.put("created",board.getCreated());
        String boardId = mRootRef.child("Boards").push().getKey();
        map.put("id",boardId);

        mRootRef.child("Boards").child(boardId).setValue(map);
        mRootRef.child("Moderation").child(boardId).child(FirebaseAuth.getInstance().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                cb.onAuthFinished("success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cb.onAuthFinished(e.getLocalizedMessage());
            }
        });
    }



}
