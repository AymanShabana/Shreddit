package com.example.shreddit.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.ViewModels.PostViewModel;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.PostAdapter;
import com.example.shreddit.databinding.ActivityBoardBinding;
import com.example.shreddit.databinding.ActivityPostDetailsBinding;
import com.example.shreddit.databinding.FragmentHomeBinding;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BoardActivity extends AppCompatActivity {
    private PostViewModel mPostViewModel;
    private BoardAdapter adapter;
    private ActivityBoardBinding binding;
    private String board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        board = getIntent().getStringExtra("board");
        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.boardName.setText(board);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        // View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_board);
        adapter = new BoardAdapter(getApplicationContext(),new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                //Toast.makeText(ViewRequestsActivity.this, request.getRequesterId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PostDetailsActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mPostViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        mPostViewModel.sendAdapterBoard(adapter,binding.progressBarSubs);
        binding.progressBarSubs.setVisibility(View.VISIBLE);
        adapter.setPosts(mPostViewModel.getAllBoardPosts(board));

    }
}