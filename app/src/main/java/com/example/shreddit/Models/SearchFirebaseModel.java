package com.example.shreddit.Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;
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

public class SearchFirebaseModel {
    public ProgressBar progressBar;
    public ProgressBar progressBarPosts;
    private DatabaseReference mRootRef;
    public static SearchFirebaseModel INSTANCE;
    private static Context mContext;
    private List<Board> mBoardList;
    private List<Post> mPostList;
    SubAdapter subAdapter;
    BoardAdapter postAdapter;
    public static SearchFirebaseModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (SearchFirebaseModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SearchFirebaseModel();
                }
            }
        }
        return INSTANCE;
    }

    public SearchFirebaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
        this.mBoardList = new ArrayList<Board>();
        this.mPostList = new ArrayList<Post>();
    }

    public List<Board> getAllBoards(String queryText) {
        mRootRef.child("Boards").orderByChild("name_c").startAt(queryText)
                .endAt(queryText+"\uf8ff").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBoardList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Board board = dataSnapshot.getValue(Board.class);
                    Log.i("Boards",board.toString());
                    mBoardList.add(board);
                }
                if(progressBar!=null)
                    progressBar.setVisibility(View.GONE);
                if(subAdapter!=null){
                    subAdapter.notifyDataSetChanged();
                    subAdapter.setBoards(mBoardList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        if(!mBoardList.isEmpty() && progressBar!=null)
            progressBar.setVisibility(View.GONE);
        if(subAdapter!=null){
            subAdapter.notifyDataSetChanged();
            subAdapter.setBoards(mBoardList);
        }
        return mBoardList;
    }
    public List<Post> getAllPosts(String queryText) {
        mRootRef.child("Posts").orderByChild("title_c").startAt(queryText)
                .endAt(queryText+"\uf8ff").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mPostList.add(post);
                }
                if(progressBarPosts!=null)
                    progressBarPosts.setVisibility(View.GONE);
                if(postAdapter!=null){
                    postAdapter.notifyDataSetChanged();
                    postAdapter.setPosts(mPostList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        if(!mPostList.isEmpty() && progressBarPosts!=null)
            progressBarPosts.setVisibility(View.GONE);
        if(postAdapter!=null){
            postAdapter.notifyDataSetChanged();
            postAdapter.setPosts(mPostList);
        }
        return mPostList;
    }

}
