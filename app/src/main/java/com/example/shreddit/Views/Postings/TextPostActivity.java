package com.example.shreddit.Views.Postings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostingViewModel;
import com.example.shreddit.databinding.ActivityLinkPostBinding;
import com.example.shreddit.databinding.ActivityTextPostBinding;

import java.util.Date;

public class TextPostActivity extends AppCompatActivity {
    private ActivityTextPostBinding binding;
    private PostingViewModel postingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);
        binding = ActivityTextPostBinding.inflate(getLayoutInflater());
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
                String text = binding.textArea.getText().toString();
                if(board.isEmpty() || title.isEmpty() || text.isEmpty()){
                    Toast.makeText(TextPostActivity.this, "Fill all the textboxes.", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.postBtn.setVisibility(View.GONE);
                Post post = new Post("",title,title.toUpperCase(),board,"https://","https://",text,0,0,new Date().getTime()/1000,"","","text");
                postingViewModel.insert(post,new MyCallbackInterface(){
                    @Override
                    public void onAuthFinished(String result) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.postBtn.setVisibility(View.VISIBLE);
                        if(result.equals("success")){
                            //Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
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