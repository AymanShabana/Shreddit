package com.example.shreddit.Models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostingModel {
    private DatabaseReference mRootRef;
    private static PostingModel INSTANCE;
    private static Context mContext;

    public static PostingModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (InitialModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PostingModel();
                }
            }
        }
        return INSTANCE;
    }

    public PostingModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
    }

    public void insertPost(Post post, MyCallbackInterface cb){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("title",post.getTitle());
        map.put("title_c",post.getTitle_c());
        map.put("board",post.getBoard());
        map.put("boardIcon",post.getBoardIcon());
        map.put("postImg",post.getPostImg());
        map.put("selfText",post.getSelfText());
        map.put("type",post.getType());
        map.put("upvotes",post.getUpvotes());
        map.put("comments",post.getComments());
        map.put("author",post.getAuthor());
        map.put("link",post.getLink());
        map.put("created",post.getCreated());
        mRootRef.child("Boards").orderByChild("name").equalTo(post.getBoard())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String postId = mRootRef.child("Posts").push().getKey();
                            map.put("id",postId);

                            mRootRef.child("Posts").child(postId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    cb.onAuthFinished("success:"+postId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    cb.onAuthFinished(e.getLocalizedMessage());
                                }
                            });
                        }
                        else{
                            cb.onAuthFinished("Board does not exit.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
