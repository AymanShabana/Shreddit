package com.example.shreddit.Models;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.PostAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PostFirebaseModel {
    public ProgressBar progressBar;
    public ProgressBar progressBarBoard;
    public BoardAdapter adapterBoard;
    private DatabaseReference mRootRef;
    //FirebaseFirestore db;
    private static PostFirebaseModel INSTANCE;
    private static Context mContext;
    private List<Post> mPostList;
    private List<Post> mBoardPostList;
    private ValueEventListener listener;
    String mBoard;
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
        this.mBoardPostList = new ArrayList<Post>();
        mRootRef.child("Posts").limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    Log.i("Posts",post.toString());
                    mPostList.add(post);
                }
                Collections.sort(mPostList);
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
        listener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBoardPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mBoardPostList.add(post);
                }
                Collections.sort(mBoardPostList);
                if(progressBarBoard!=null)
                    progressBarBoard.setVisibility(View.GONE);
                if(adapterBoard!=null){
                    adapterBoard.notifyDataSetChanged();
                    adapterBoard.setPosts(mBoardPostList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

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
    public List<Post> getAllPostsBoard(String board) {
        try{
            mRootRef.child("Posts").orderByChild("board").equalTo(mBoard).removeEventListener(listener);
        }catch (Exception e){

        }
        mBoard=board;
        mRootRef.child("Posts").orderByChild("board").equalTo(mBoard)
                .addValueEventListener(listener);
        if(!mBoardPostList.isEmpty() && progressBarBoard!=null)
            progressBarBoard.setVisibility(View.GONE);
        if(adapterBoard!=null){
            adapterBoard.notifyDataSetChanged();
            adapterBoard.setPosts(mBoardPostList);
        }
        return mBoardPostList;
    }

    public static void upvotePost(String postId, MyCallbackInterface cb){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("Posts").child(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            mRootRef.child("Votes").child(postId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.child(FirebaseAuth.getInstance().getUid()).exists()){
                                                Map<String, Object> map  = (Map) snapshot.getValue();
                                                //Log.i("SelectedVideo",map.get(FirebaseAuth.getInstance().getUid())+"");
                                                switch (Integer.parseInt(map.get(FirebaseAuth.getInstance().getUid())+"")){
                                                    case 0:
                                                        mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(1);
                                                        mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Post post = snapshot.getValue(Post.class);
                                                                post.setUpvotes(post.getUpvotes()+1);
                                                                mRootRef.child("Posts").child(postId).setValue(post);
                                                                cb.onAuthFinished(""+post.getUpvotes());
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                        break;
                                                    case 1:
                                                        mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(0);
                                                        mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Post post = snapshot.getValue(Post.class);
                                                                post.setUpvotes(post.getUpvotes()-1);
                                                                mRootRef.child("Posts").child(postId).setValue(post);
                                                                cb.onAuthFinished(""+post.getUpvotes());

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        break;
                                                    case 2:
                                                        mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(1);
                                                        mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Post post = snapshot.getValue(Post.class);
                                                                post.setUpvotes(post.getUpvotes()+2);
                                                                mRootRef.child("Posts").child(postId).setValue(post);
                                                                cb.onAuthFinished(""+post.getUpvotes());
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                        break;
                                                }
                                            }
                                            else {
                                                mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(1);
                                                mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        Post post = snapshot.getValue(Post.class);
                                                        post.setUpvotes(post.getUpvotes()+1);
                                                        mRootRef.child("Posts").child(postId).setValue(post);
                                                        cb.onAuthFinished(""+post.getUpvotes());
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public static void downvotePost(String postId, MyCallbackInterface cb){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("Posts").child(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            mRootRef.child("Votes").child(postId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.child(FirebaseAuth.getInstance().getUid()).exists()){
                                                Map<String, Object> map  = (Map) snapshot.getValue();
                                                switch (Integer.parseInt(map.get(FirebaseAuth.getInstance().getUid())+"")){
                                                    case 0:
                                                        mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(2);
                                                        mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Post post = snapshot.getValue(Post.class);
                                                                post.setUpvotes(post.getUpvotes()-1);
                                                                mRootRef.child("Posts").child(postId).setValue(post);
                                                                cb.onAuthFinished(""+post.getUpvotes());
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                        break;
                                                    case 1:
                                                        mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(2);
                                                        mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Post post = snapshot.getValue(Post.class);
                                                                post.setUpvotes(post.getUpvotes()-2);
                                                                mRootRef.child("Posts").child(postId).setValue(post);
                                                                cb.onAuthFinished(""+post.getUpvotes());

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        break;
                                                    case 2:
                                                        mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(0);
                                                        mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Post post = snapshot.getValue(Post.class);
                                                                post.setUpvotes(post.getUpvotes()+1);
                                                                mRootRef.child("Posts").child(postId).setValue(post);
                                                                cb.onAuthFinished(""+post.getUpvotes());
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                        break;
                                                }
                                            }
                                            else {
                                                mRootRef.child("Votes").child(postId).child(FirebaseAuth.getInstance().getUid()).setValue(2);
                                                mRootRef.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        Post post = snapshot.getValue(Post.class);
                                                        post.setUpvotes(post.getUpvotes()-1);
                                                        mRootRef.child("Posts").child(postId).setValue(post);
                                                        cb.onAuthFinished(""+post.getUpvotes());
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public static void voteExists(String postId, MyCallbackInterface cb){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("Votes").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getUid()).exists()) {
                    Map<String, Object> map  = (Map) snapshot.getValue();
                    cb.onAuthFinished(map.get(FirebaseAuth.getInstance().getUid())+"");

                }
                else{
                    cb.onAuthFinished("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
