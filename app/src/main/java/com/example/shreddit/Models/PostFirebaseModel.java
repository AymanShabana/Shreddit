package com.example.shreddit.Models;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Views.Adapters.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostFirebaseModel {
    public ProgressBar progressBar;
    private DatabaseReference mRootRef;
    //FirebaseFirestore db;
    private static PostFirebaseModel INSTANCE;
    private static Context mContext;
    private List<Post> mPostList;
    PostAdapter adapter;
    public static PostFirebaseModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (PostFirebaseModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PostFirebaseModel();
                }
            }
        }
        return INSTANCE;
    }

    public PostFirebaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
        //this.db = FirebaseFirestore.getInstance();
        this.mPostList = new ArrayList<Post>();
        mRootRef.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    Log.i("Posts",post.toString());
                    mPostList.add(post);
                }
                if(progressBar!=null)
                    progressBar.setVisibility(View.GONE);
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                    adapter.setPosts(mPostList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    public List<Post> getAllPosts() {
        if(!mPostList.isEmpty() && progressBar!=null)
            progressBar.setVisibility(View.GONE);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
            adapter.setPosts(mPostList);
        }
        return mPostList;
    }

}
