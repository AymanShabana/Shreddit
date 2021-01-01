package com.example.shreddit.ViewModels;

import android.app.Application;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Chat;
import com.example.shreddit.Models.ChatFireBaseModel;
import com.example.shreddit.Models.SubRepo;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.ChatAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private ChatFireBaseModel chatFireBaseModel;
    private List<Chat> mAllChats;
    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatFireBaseModel = ChatFireBaseModel.getInstance(application);
    }
    public List<Chat> getAllChats() { return chatFireBaseModel.getAllChats(); }
    public void insert(Chat chat, MyCallbackInterface success) { chatFireBaseModel.insert(chat,success); }

    public void sendAdapter(ChatAdapter adapter, ProgressBar progressBarSubs) {
        chatFireBaseModel.progressBar=progressBarSubs;
        chatFireBaseModel.adapter = adapter;
    }
}
