package com.example.shreddit.Models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class InitialModel {
    private FirebaseAuth auth;
    private DatabaseReference mRootRef;
    private static InitialModel INSTANCE;
    private static Context mContext;

    public static InitialModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (InitialModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InitialModel();
                }
            }
        }
        return INSTANCE;
    }

    public InitialModel() {
        this.auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
    }
    public void loginUser(String emailTxt, String passTxt, MyCallbackInterface success) {

        auth.signInWithEmailAndPassword(emailTxt,passTxt)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        success.onAuthFinished("success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success.onAuthFinished(e.getLocalizedMessage());
                    }
                });
    }
    public void register(String username, String emailTxt, String passTxt, MyCallbackInterface success) {
        auth.createUserWithEmailAndPassword(emailTxt,passTxt)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        map.put("username",username);
                        map.put("email",emailTxt);
                        map.put("id",auth.getCurrentUser().getUid());
                        map.put("bio","");
                        map.put("imageUrl","default");
                        mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    success.onAuthFinished("success");
                                }
                                else{
                                    success.onAuthFinished(task.getException().getLocalizedMessage());
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        success.onAuthFinished(e.getLocalizedMessage());
                    }
                });
    }
    public void logout(){
        auth.signOut();
    }
    public FirebaseUser getUser(){
        return auth.getCurrentUser();
    }

}
