package com.example.shreddit.Views.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.ViewModels.PostViewModel;
import com.example.shreddit.Views.Adapters.PostAdapter;
import com.example.shreddit.Views.PostDetailsActivity;
import com.example.shreddit.databinding.FragmentHomeBinding;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.alhazmy13.mediapicker.Video.VideoActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PostViewModel mPostViewModel;
    private PostAdapter adapter;
    private FragmentHomeBinding binding;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
       // View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new PostAdapter(getContext(),new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                //Toast.makeText(ViewRequestsActivity.this, request.getRequesterId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PostDetailsActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostViewModel = new ViewModelProvider(this).get(PostViewModel.class);
//        Log.i("LOG_RESPONSE_HF",mPostViewModel.getAllPosts().get(0).toString());
        mPostViewModel.sendAdapter(adapter,binding.progressBarSubs);
        binding.progressBarSubs.setVisibility(View.VISIBLE);
        adapter.setPosts(mPostViewModel.getAllPosts());

//        mPostViewModel.getAllPosts().observe(getActivity(), new Observer<List<Post>>() {
//            @Override
//            public void onChanged(@Nullable final List<Post> posts) {
//                // Update the cached copy of the words in the adapter.
//                adapter.setPosts(posts);
//            }
//        });

        return view;
    }
    public interface VolleyCallback{
        void onSuccess(List<Post> result);
    }

}