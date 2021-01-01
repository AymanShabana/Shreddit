package com.example.shreddit.Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.ChatAdapter;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ChatFireBaseModel {
    public ProgressBar progressBar;
    private DatabaseReference mRootRef;
    private static ChatFireBaseModel INSTANCE;
    private static Context mContext;
    private List<Chat> mChatList1;
    private List<Chat> mChatList2;
    private List<Chat> mChatList;
    public ChatAdapter adapter;
    public static ChatFireBaseModel getInstance(final Context context) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (ChatFireBaseModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChatFireBaseModel();
                }
            }
        }
        return INSTANCE;
    }

    public ChatFireBaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
        this.mChatList1 = new ArrayList<Chat>();
        this.mChatList2 = new ArrayList<Chat>();
        this.mChatList = new ArrayList<Chat>();
        mRootRef.child("Chats").orderByChild("name1_c").equalTo(UserFirebaseModel.mUser.getUsername().toUpperCase()).limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChatList1.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    mChatList1.add(chat);
                }
                if((!mChatList1.isEmpty() || !mChatList2.isEmpty()) && progressBar!=null)
                    progressBar.setVisibility(View.GONE);
                if(adapter!=null){
                    mChatList.clear();
                    mChatList.addAll(mChatList1);
                    mChatList.addAll(mChatList2);
                    Collections.sort(mChatList);
                    adapter.setChats(mChatList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        mRootRef.child("Chats").orderByChild("name2_c").equalTo(UserFirebaseModel.mUser.getUsername().toUpperCase()).limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChatList2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    mChatList2.add(chat);
                }
                if((!mChatList1.isEmpty() || !mChatList2.isEmpty()) && progressBar!=null)
                    progressBar.setVisibility(View.GONE);
                if(adapter!=null){
                    mChatList.clear();
                    mChatList.addAll(mChatList1);
                    mChatList.addAll(mChatList2);
                    Collections.sort(mChatList);
                    adapter.setChats(mChatList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    public void insert(Chat chat, MyCallbackInterface cb) {
        final boolean[] exists = {false};
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("name1",chat.getName1());
        map.put("name1_c",chat.getName1_c());
        map.put("name2",chat.getName2());
        map.put("name2_c",chat.getName2_c());
        map.put("lastMessage",chat.getLastMessage());
        map.put("userImage1",chat.getUserImage1());
        map.put("userImage2",chat.getUserImage2());
        map.put("lastUpdated",chat.getLastUpdated());
        mRootRef.child("Chats").orderByChild("name1_c").equalTo(chat.getName1_c())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Chat inner = dataSnapshot.getValue(Chat.class);
                            if(inner.getName2_c().equals(chat.getName2_c())){
                                cb.onAuthFinished("alreadyExists");
                                exists[0] =true;
                                break;
                            }
                        }
                        if(exists[0]){}
                        else{
                            mRootRef.child("Chats").orderByChild("name2_c").equalTo(chat.getName1_c())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Chat inner = dataSnapshot.getValue(Chat.class);
                                                if(inner.getName1_c().equals(chat.getName2_c())){
                                                    cb.onAuthFinished("alreadyExists");
                                                    exists[0] =true;
                                                    break;
                                                }
                                            }
                                            if(exists[0]){}
                                            else {
                                                mRootRef.child("Users").orderByChild("username_c").equalTo(chat.getName2_c())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()) {
                                                                    String chatId = mRootRef.child("Chats").push().getKey();
                                                                    map.put("id",chatId);

                                                                    mRootRef.child("Chats").child(chatId).setValue(map);
                                                                    cb.onAuthFinished("success");
                                                                }
                                                                else{
                                                                    cb.onAuthFinished("User does not exist.");
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

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public List<Chat> getAllChats() {
        if((!mChatList1.isEmpty() || !mChatList2.isEmpty()) && progressBar!=null)
            progressBar.setVisibility(View.GONE);
        if(adapter!=null ){
            mChatList.clear();
            mChatList.addAll(mChatList1);
            mChatList.addAll(mChatList2);
            Collections.sort(mChatList);
            adapter.setChats(mChatList);
        }
        return mChatList;
    }

}
