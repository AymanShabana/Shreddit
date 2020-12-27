package com.example.shreddit.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.SearchView;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.ViewModels.PostViewModel;
import com.example.shreddit.ViewModels.SearchViewModel;
import com.example.shreddit.Views.Adapters.BoardAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;
import com.example.shreddit.databinding.ActivityBoardBinding;
import com.example.shreddit.databinding.ActivitySearchBinding;

import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private String search;
    private SubAdapter subAdapter;
    private BoardAdapter postAdapter;
    private SearchViewModel searchViewModel;
    final int VOICE_RECOGNITION = 2;
    Intent intentVR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = getIntent().getStringExtra("search").toUpperCase();
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RecyclerView recyclerView = binding.postRecyclerView;
        postAdapter = new BoardAdapter(getApplicationContext(),new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                //Toast.makeText(ViewRequestsActivity.this, request.getRequesterId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PostDetailsActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.sendAdapterPosts(postAdapter,binding.progressBarPosts);
        binding.progressBarPosts.setVisibility(View.VISIBLE);

        RecyclerView recyclerViewSubs = binding.subRecyclerView;
        subAdapter = new SubAdapter(getApplicationContext(),new SubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Board board) {
                //Toast.makeText(ViewRequestsActivity.this, request.getRequesterId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                intent.putExtra("board", board.getName());
                startActivity(intent);
            }
        });
        recyclerViewSubs.setAdapter(subAdapter);
        recyclerViewSubs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchViewModel.sendAdapter(subAdapter,binding.progressBarSubs);
        binding.progressBarSubs.setVisibility(View.VISIBLE);


        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                postAdapter.setPosts(searchViewModel.getAllPosts(s.toUpperCase()));
                subAdapter.setBoards(searchViewModel.getAllBoards(s.toUpperCase()));
                return false;
            }
        });
        postAdapter.setPosts(searchViewModel.getAllPosts(search));
        subAdapter.setBoards(searchViewModel.getAllBoards(search));
        intentVR = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentVR.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentVR.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak!");
        intentVR.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intentVR.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

        binding.mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intentVR, VOICE_RECOGNITION);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION) {
            if (resultCode == RESULT_OK) {

                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                float[] confidence = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
                postAdapter.setPosts(searchViewModel.getAllPosts(results.get(0).toUpperCase()));
                subAdapter.setBoards(searchViewModel.getAllBoards(results.get(0).toUpperCase()));
            }
        }
    }

}