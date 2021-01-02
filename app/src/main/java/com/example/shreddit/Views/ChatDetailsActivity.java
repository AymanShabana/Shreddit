package com.example.shreddit.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shreddit.Models.Chat;
import com.example.shreddit.Models.Comment;
import com.example.shreddit.Models.Message;
import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.R;
import com.example.shreddit.Utils.API;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Utils.VolleySingleton;
import com.example.shreddit.ViewModels.ChatDetailsViewModel;
import com.example.shreddit.ViewModels.ChatDetailsViewModelFactory;
import com.example.shreddit.ViewModels.PostDetailsViewModel;
import com.example.shreddit.ViewModels.PostDetailsViewModelFactory;
import com.example.shreddit.Views.Adapters.CommentAdapter;
import com.example.shreddit.Views.Adapters.MessageAdapter;
import com.example.shreddit.databinding.ActivityChatDetailsBinding;
import com.example.shreddit.databinding.ActivityPostDetailsBinding;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.shreddit.Utils.API.FCM_API;
import static com.example.shreddit.Utils.API.serverKey;

public class ChatDetailsActivity extends AppCompatActivity {
    private ActivityChatDetailsBinding binding;
    private ChatDetailsViewModel chatDetailsViewModel;
    private MessageAdapter adapter;

    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        chat = (Chat) getIntent().getSerializableExtra("chat");
        Log.i("chatid","ChatDetailsActivity:"+chat.getId());
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if(chat.getName1_c().equalsIgnoreCase(UserFirebaseModel.mUser.getUsername_c())){
            binding.usersName.setText(chat.getName2());
        }
        else{
            binding.usersName.setText(chat.getName1());
        }
        if(chat.getUserImage1().startsWith("http")){
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            imageLoader.displayImage(chat.getUserImage1(),binding.topImg,options);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_msgs);
        adapter = new MessageAdapter(this,chat.getUserImage1());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatDetailsViewModel = new ViewModelProvider(this, new ChatDetailsViewModelFactory(this.getApplication(),chat.getId())).get(ChatDetailsViewModel.class);
        chatDetailsViewModel.sendAdapter(adapter,binding.progressBar);
        binding.progressBar.setVisibility(View.VISIBLE);
        adapter.recyclerView = recyclerView;
        adapter.setMessages(chatDetailsViewModel.getAllMessages());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.msgBox.getText().toString();
                if(!message.isEmpty()){
                    binding.progressBarSent.setVisibility(View.VISIBLE);
                    binding.chatBtn.setVisibility(View.GONE);
                    binding.msgBox.setText("");
                    Message newMessage = new Message("","","",message,new Date().getTime()/1000);
                    chatDetailsViewModel.insert(newMessage, new MyCallbackInterface() {
                        @Override
                        public void onAuthFinished(String result) {
                            binding.progressBarSent.setVisibility(View.GONE);
                            binding.chatBtn.setVisibility(View.VISIBLE);
                            if(result.equals("success")){
                                String topic ="";
                                if(UserFirebaseModel.mUser.getUsername_c().equalsIgnoreCase(chat.getName1_c())){
                                    topic = "/topics/"+chat.getName2_c();
                                }
                                else{
                                    topic = "/topics/"+chat.getName1_c();
                                }
                                JSONObject notification = new JSONObject();
                                JSONObject notificationBody = new JSONObject();
                                try {
                                    notificationBody.put("title", UserFirebaseModel.mUser.getUsername());
                                    notificationBody.put("message", message);
                                    notificationBody.put("id", chat.getId());
                                    notificationBody.put("name1", chat.getName1());
                                    notificationBody.put("name2", chat.getName2());
                                    notification.put("to", topic);
                                    notification.put("data", notificationBody);
                                } catch (JSONException e) {
                                    Log.e("TAG", "onCreate: " + e.getLocalizedMessage());
                                }

                                sendNotification(notification);

                            }
                            else{
                                Toast.makeText(ChatDetailsActivity.this, "Error: "+result, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(ChatDetailsActivity.this, "Please add a comment first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatDetailsViewModel.clean();
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("Authorization",API.serverKey);
                params.put("Content-Type", API.contentType);
                return params;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

}