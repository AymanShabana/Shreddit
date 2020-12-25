package com.example.shreddit.Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.CommentAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailsFirebaseModel {
    public ProgressBar progressBar;
    private DatabaseReference mRootRef;
    public static PostDetailsFirebaseModel INSTANCE;
    private static Context mContext;
    private List<Comment> mCommentList;
    private String postId;
    CommentAdapter adapter;
    public static PostDetailsFirebaseModel getInstance(final Context context,String postId) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (PostDetailsFirebaseModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PostDetailsFirebaseModel(postId);
                }
            }
        }
        return INSTANCE;
    }

    public PostDetailsFirebaseModel(String postId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
        this.mCommentList = new ArrayList<Comment>();
        this.postId = postId;
        mRootRef.child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCommentList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    Log.i("Comments",comment.toString());
                    mCommentList.add(comment);
                }
                if(progressBar!=null)
                    progressBar.setVisibility(View.GONE);
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                    adapter.setComments(mCommentList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void insert(Comment comment, MyCallbackInterface cb) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("author",comment.getAuthor());
        map.put("comment",comment.getComment());
        map.put("upvotes",comment.getUpvotes());
        map.put("icon_img",comment.getIcon_img());
        map.put("created",comment.getCreated());
        mRootRef.child("Posts").child(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            String commentId = mRootRef.child("Comments").child(postId).push().getKey();
                            map.put("id",commentId);

                            mRootRef.child("Comments").child(postId).child(commentId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        else{
                            cb.onAuthFinished("Post does not exist.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public List<Comment> getAllComments() {
        if(!mCommentList.isEmpty() && progressBar!=null)
            progressBar.setVisibility(View.GONE);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
            adapter.setComments(mCommentList);
        }
        return mCommentList;
    }

}
