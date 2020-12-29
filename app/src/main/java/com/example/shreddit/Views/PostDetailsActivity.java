package com.example.shreddit.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.shreddit.Models.Comment;
import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.PostDetailsViewModel;
import com.example.shreddit.ViewModels.PostDetailsViewModelFactory;
import com.example.shreddit.ViewModels.SubViewModel;
import com.example.shreddit.Views.Adapters.CommentAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;
import com.example.shreddit.databinding.ActivityPostDetailsBinding;
import com.example.shreddit.databinding.ActivityTextPostBinding;
import com.example.shreddit.databinding.FragmentSubsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {

    private ActivityPostDetailsBinding binding;
    private PostDetailsViewModel postDetailsViewModel;
    private CommentAdapter adapter;

    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        post = (Post) getIntent().getSerializableExtra("post");
        binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.boardName.setText(post.getBoard());
        binding.authorName.setText("Posted by "+post.getAuthor());
        binding.commentsLbl.setText(post.getComments()+"");
        binding.title.setText(post.getTitle());
        binding.upvotesLbl.setText(post.getUpvotes()+"");
        switch (post.getType()){
            case "image":
                binding.postImage.setVisibility(View.VISIBLE);
                binding.postLink.setVisibility(View.GONE);
                binding.postText.setVisibility(View.GONE);
                binding.postVideo.setVisibility(View.GONE);
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                imageLoader.displayImage(post.getPostImg(),binding.postImage, options);
                break;
            case "video":
                binding.postImage.setVisibility(View.GONE);
                binding.postLink.setVisibility(View.GONE);
                binding.postText.setVisibility(View.GONE);
                binding.postVideo.setVisibility(View.VISIBLE);
                binding.postVideo.setVideoPath(post.getLink());
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(binding.postVideo);
                binding.postVideo.setMediaController(mediaController);
                binding.postVideo.start();
                break;
            case "text":
                binding.postImage.setVisibility(View.GONE);
                binding.postLink.setVisibility(View.GONE);
                binding.postText.setVisibility(View.VISIBLE);
                binding.postVideo.setVisibility(View.GONE);
                binding.postText.setText(post.getSelfText());
                break;
            case "link":
                binding.postImage.setVisibility(View.GONE);
                binding.postLink.setVisibility(View.VISIBLE);
                binding.postText.setVisibility(View.GONE);
                binding.postVideo.setVisibility(View.GONE);
                binding.postLink.getSettings().setJavaScriptEnabled(true);
                binding.postLink.setWebViewClient(new WebViewController());
                binding.postLink.loadUrl(post.getLink());
                break;

        }
        RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);
        adapter = new CommentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        postDetailsViewModel = new ViewModelProvider(this, new PostDetailsViewModelFactory(this.getApplication(),post.getId())).get(PostDetailsViewModel.class);
        postDetailsViewModel.sendAdapter(adapter,binding.progressBarSubs);
        binding.progressBarSubs.setVisibility(View.VISIBLE);
        adapter.setComments(postDetailsViewModel.getAllComments());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = binding.commentBox.getText().toString();
                if(!comment.isEmpty()){
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.commentBtn.setVisibility(View.GONE);
                    binding.commentBox.setText("");
                    Comment newComment = new Comment("","","",comment,0,new Date().getTime()/1000);
                    postDetailsViewModel.insert(newComment, new MyCallbackInterface() {
                        @Override
                        public void onAuthFinished(String result) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.commentBtn.setVisibility(View.VISIBLE);
                            if(result.equals("success")){
                                //Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
                                binding.commentsLbl.setText((Integer.parseInt(binding.commentsLbl.getText().toString())+1)+"");
                            }
                            else{
                                Toast.makeText(PostDetailsActivity.this, "Error: "+result, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(PostDetailsActivity.this, "Please add a comment first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postDetailsViewModel.clean();
    }
}