package com.example.shreddit.Views.Postings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostingViewModel;
import com.example.shreddit.ViewModels.SubViewModel;
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
                binding.progressBar.setVisibility(View.VISIBLE);
                String board = binding.subName.getText().toString();
                String title = binding.titleTxt.getText().toString();
                String link = binding.linkTxt.getText().toString();
                Post post = new Post("",title,board,"https://","https://","",0,0,new Date().getTime()/1000,link,"","link");
                postingViewModel.insert(post,new MyCallbackInterface(){
                    @Override
                    public void onAuthFinished(String result) {
                        binding.progressBar.setVisibility(View.GONE);
                        if(result.equals("success")){
                            //Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            //Snackbar.make(binding.parentLayout, "Error: "+result, Snackbar.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
    }
}