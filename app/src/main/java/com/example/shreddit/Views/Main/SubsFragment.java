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

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Post;
import com.example.shreddit.R;
import com.example.shreddit.Utils.Keyboard;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.SubViewModel;
import com.example.shreddit.Views.Adapters.PostAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;
import com.example.shreddit.Views.BoardActivity;
import com.example.shreddit.Views.PostDetailsActivity;
import com.example.shreddit.databinding.FragmentSubsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSubsBinding binding;
    private SubViewModel mSubViewModel;
    private SubAdapter adapter;

    public SubsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubsFragment newInstance(String param1, String param2) {
        SubsFragment fragment = new SubsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSubsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView recyclerView = view.findViewById(R.id.sub_recycler_view);
        adapter = new SubAdapter(getContext(),new SubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Board board) {
                //Toast.makeText(ViewRequestsActivity.this, request.getRequesterId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), BoardActivity.class);
                intent.putExtra("board", board.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubViewModel = new ViewModelProvider(this).get(SubViewModel.class);
        mSubViewModel.sendAdapter(adapter,binding.progressBarSubs);
        binding.progressBarSubs.setVisibility(View.VISIBLE);
        adapter.setBoards(mSubViewModel.getAllBoards());

        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.dismissKeyboard(getActivity());
                binding.progressBar.setVisibility(View.VISIBLE);
                String name = binding.subname.getText().toString();
                Board board = new Board("",name, "", "", "", "", 0, "", new Date().getTime()/1000);
                mSubViewModel.insert(board,new MyCallbackInterface(){
                    @Override
                    public void onAuthFinished(String result) {
                        binding.progressBar.setVisibility(View.GONE);
                        if(result.equals("success")){
                            Snackbar.make(binding.parentLayout, "Board created successfully.", Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            Snackbar.make(binding.parentLayout, "Error: "+result, Snackbar.LENGTH_LONG).show();
                        }
                    }

                });
            }
        });

        return view;

    }

}