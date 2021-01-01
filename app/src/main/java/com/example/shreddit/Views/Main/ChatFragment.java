package com.example.shreddit.Views.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreddit.Models.Board;
import com.example.shreddit.Models.Chat;
import com.example.shreddit.Models.UserFirebaseModel;
import com.example.shreddit.R;
import com.example.shreddit.Utils.Keyboard;
import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.ViewModels.ChatViewModel;
import com.example.shreddit.ViewModels.SubViewModel;
import com.example.shreddit.Views.Adapters.ChatAdapter;
import com.example.shreddit.Views.Adapters.SubAdapter;
import com.example.shreddit.Views.BoardActivity;
import com.example.shreddit.Views.ChatDetailsActivity;
import com.example.shreddit.Views.PostDetailsActivity;
import com.example.shreddit.databinding.FragmentChatBinding;
import com.example.shreddit.databinding.FragmentSubsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentChatBinding binding;
    private ChatViewModel mChatViewModel;
    private ChatAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView recyclerView = view.findViewById(R.id.chat_recycler_view);
        adapter = new ChatAdapter(getContext(),new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat chat) {
                //Toast.makeText(getContext(), "Clicked on "+chat.getName2(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ChatDetailsActivity.class);
                Log.i("chatid",chat.toString());
                intent.putExtra("chat", chat);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        mChatViewModel.sendAdapter(adapter,binding.progressBar);
        binding.progressBar.setVisibility(View.VISIBLE);
        adapter.setChats(mChatViewModel.getAllChats());

        binding.startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.dismissKeyboard(getActivity());
                if(binding.username.getText().toString().toUpperCase().equals(UserFirebaseModel.mUser.getUsername_c())) {
                    Toast.makeText(getContext(), "You can't start a chat with yourself.", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                String name = binding.username.getText().toString();
                binding.username.setText("");
                Chat chat = new Chat("", UserFirebaseModel.mUser.getUsername(),name, "", "", "",  new Date().getTime()/1000);
                mChatViewModel.insert(chat,new MyCallbackInterface(){
                    @Override
                    public void onAuthFinished(String result) {
                        binding.progressBar.setVisibility(View.GONE);
                        if(result.equals("success")){
                            Toast.makeText(getContext(), "Chat started successfully", Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals("alreadyExists")){
                            Toast.makeText(getContext(), "Chat already exists", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        return view;
    }
}