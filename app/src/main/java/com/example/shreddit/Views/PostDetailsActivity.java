package com.example.shreddit.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;

import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.databinding.ActivityPostDetailsBinding;
import com.example.shreddit.databinding.ActivityTextPostBinding;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PostDetailsActivity extends AppCompatActivity {

    private ActivityPostDetailsBinding binding;
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

    }
    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}