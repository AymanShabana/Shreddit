package com.example.shreddit.Views.Postings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostingViewModel;
import com.example.shreddit.ViewModels.SubViewModel;
import com.example.shreddit.Views.PostDetailsActivity;
import com.example.shreddit.databinding.ActivityLinkPostBinding;
import com.example.shreddit.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class LinkPostActivity extends AppCompatActivity {
    private ActivityLinkPostBinding binding;
    private PostingViewModel postingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_post);
        binding = ActivityLinkPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        postingViewModel = new ViewModelProvider(this).get(PostingViewModel.class);
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String board = binding.subName.getText().toString();
                String title = binding.titleTxt.getText().toString();
                String link = binding.linkTxt.getText().toString();
                if(board.isEmpty() || title.isEmpty() || link.isEmpty()){
                    Toast.makeText(LinkPostActivity.this, "Fill all the textboxes.", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.postBtn.setVisibility(View.GONE);
                Post post = new Post("",title,title.toUpperCase(),board,"https://","https://","",0,0,new Date().getTime()/1000,link, UserFirebaseModel.mUser.getUsername(),"link");
                postingViewModel.insert(post,new MyCallbackInterface(){
                    @Override
                    public void onAuthFinished(String result) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.postBtn.setVisibility(View.VISIBLE);
                        if(result.startsWith("success")){
                            //Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
                            String postId = result.split(":")[1];
                            Intent intent = new Intent(getApplicationContext(), PostDetailsActivity.class);
                            post.setId(postId);
                            intent.putExtra("post", post);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            binding.progressBar.setVisibility(View.GONE);
                            binding.postBtn.setVisibility(View.VISIBLE);
                            //Snackbar.make(binding.parentLayout, "Error: "+result, Snackbar.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
    }
}