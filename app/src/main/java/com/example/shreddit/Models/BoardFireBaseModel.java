package com.example.shreddit.Models;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Initial.InitialActivity;
import com.example.shreddit.Views.Initial.RegisterFragment;
import com.example.shreddit.Views.SubAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardFireBaseModel {
    public ProgressBar progressBar;
    private DatabaseReference mRootRef;
    //FirebaseFirestore db;
    private static BoardFireBaseModel INSTANCE;
    private static Context mContext;
    private List<Board> mBoardList;
    SubAdapter subAdapter;
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
        //this.db = FirebaseFirestore.getInstance();
        this.mBoardList = new ArrayList<Board>();
        mRootRef.child("Boards").addValueEventListener(new ValueEventListener() {
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
//        this.db.collection("Boards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Map<String,Object> board = document.getData();
//
//                    }
//                }
//            }
//        });
    }
    public void insert(Board board, MyCallbackInterface cb) {
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


    public List<Board> getAllBoards() {
        if(!mBoardList.isEmpty() && progressBar!=null)
            progressBar.setVisibility(View.GONE);
        if(subAdapter!=null){
            subAdapter.notifyDataSetChanged();
            subAdapter.setBoards(mBoardList);
        }
        return mBoardList;
    }
}
