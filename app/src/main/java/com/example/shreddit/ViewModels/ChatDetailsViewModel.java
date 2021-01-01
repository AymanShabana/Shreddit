package com.example.shreddit.ViewModels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.shreddit.Models.ChatDetailsFirebaseModel;
import com.example.shreddit.Models.Comment;
import com.example.shreddit.Models.Message;
import com.example.shreddit.Models.PostDetailsFirebaseModel;
import com.example.shreddit.Models.PostDetailsRepo;
import com.example.shreddit.Models.PostFirebaseModel;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Adapters.CommentAdapter;
import com.example.shreddit.Views.Adapters.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatDetailsViewModel extends AndroidViewModel {
    private ChatDetailsFirebaseModel chatDetailsFirebaseModel;
    private FirebaseAuth auth;
    private String chatId;
    public ChatDetailsViewModel(@NonNull Application application, String mChatId) {
        super(application);
        Log.i("chatid","ChatDetailsViewModel:"+mChatId);
        chatDetailsFirebaseModel = ChatDetailsFirebaseModel.getInstance(application,mChatId);
        this.chatId = mChatId;
        auth = FirebaseAuth.getInstance();
    }
    public List<Message> getAllMessages() { return chatDetailsFirebaseModel.getAllMessages(); }
    public void insert(Message message, MyCallbackInterface success) {
        message.setSender(UserFirebaseModel.mUser.getUsername());
        new ChatDetailsViewModel.insertAsyncTask(chatDetailsFirebaseModel,success).execute(message);
    }

    public void sendAdapter(MessageAdapter adapter, ProgressBar progressBarSubs) {
        chatDetailsFirebaseModel.adapter = adapter;
        chatDetailsFirebaseModel.progressBar = progressBarSubs;
    }

    public void clean() {
        ChatDetailsFirebaseModel.INSTANCE=null;
    }

    private static class insertAsyncTask extends AsyncTask<Message, Void, Void> {
        private ChatDetailsFirebaseModel mAsyncPostingModel;
        private MyCallbackInterface cb;
        public insertAsyncTask(ChatDetailsFirebaseModel model, MyCallbackInterface success) {
            mAsyncPostingModel = model;
            cb = success;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            mAsyncPostingModel.insert(messages[0],cb);
            return null;
        }

    }

}
