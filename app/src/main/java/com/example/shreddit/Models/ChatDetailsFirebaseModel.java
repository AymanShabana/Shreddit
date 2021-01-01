package com.example.shreddit.Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.MessageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatDetailsFirebaseModel {
    public ProgressBar progressBar;
    private DatabaseReference mRootRef;
    public static ChatDetailsFirebaseModel INSTANCE;
    public static String otherName="";
    private static Context mContext;
    private List<Message> mMessageList;
    private String chatId;
    public MessageAdapter adapter;
    public static ChatDetailsFirebaseModel getInstance(final Context context,String mchatId) {
        mContext = context;
        if (INSTANCE == null) {
            synchronized (ChatDetailsFirebaseModel.class) {
                if (INSTANCE == null) {
                    Log.i("chatid","ChatDetailsFirebaseModel:"+mchatId);
                    INSTANCE = new ChatDetailsFirebaseModel(mchatId);
                }
            }
        }
        return INSTANCE;
    }

    public ChatDetailsFirebaseModel(String mchatId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRootRef = database.getReference();
        this.mMessageList = new ArrayList<Message>();
        this.chatId = mchatId;
        mRootRef.child("Messages").child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessageList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message Message = dataSnapshot.getValue(Message.class);
                    //Log.i("Messages",Message.toString());
                    mMessageList.add(Message);
                }
                if(progressBar!=null)
                    progressBar.setVisibility(View.GONE);
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                    adapter.setMessages(mMessageList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        mRootRef.child("Chats").child(chatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat.getName1_c().equalsIgnoreCase(UserFirebaseModel.mUser.getUsername_c())){
                            otherName = chat.getName2_c();
                        }
                        else{
                            otherName = chat.getName1_c();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void insert(Message Message, MyCallbackInterface cb) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("sender",Message.getSender());
        map.put("message",Message.getMessage());
        map.put("icon_img",Message.getIcon_img());
        map.put("timestamp",Message.getTimestamp());
        mRootRef.child("Chats").child(chatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            mRootRef.child("Chats").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Chat chat = snapshot.getValue(Chat.class);
                                    chat.setLastMessage(Message.getMessage());
                                    chat.setLastUpdated(Message.getTimestamp());
                                    mRootRef.child("Chats").child(chatId).setValue(chat);
                                    String MessageId = mRootRef.child("Messages").child(chatId).push().getKey();
                                    map.put("id",MessageId);

                                    mRootRef.child("Messages").child(chatId).child(MessageId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            cb.onAuthFinished("Chat does not exist.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public List<Message> getAllMessages() {
        if(!mMessageList.isEmpty() && progressBar!=null)
            progressBar.setVisibility(View.GONE);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
            adapter.setMessages(mMessageList);
        }
        return mMessageList;
    }

}
