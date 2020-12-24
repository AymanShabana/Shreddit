package com.example.shreddit.Views.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shreddit.R;
import com.example.shreddit.ViewModels.InitialViewModel;
import com.example.shreddit.Views.Initial.InitialActivity;
import com.example.shreddit.Views.Postings.ImagePostActivity;
import com.example.shreddit.Views.Postings.LinkPostActivity;
import com.example.shreddit.Views.Postings.TextPostActivity;
import com.example.shreddit.Views.Postings.VideoPostActivity;
import com.example.shreddit.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Fragment selectorFragment;
    private Fragment homeFragment;
    BottomSheetBehavior sheetBehavior;
    private Fragment subsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.post_bottom_sheet));
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFragment()).commit();
        binding.navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    if(homeFragment == null)
                        homeFragment = new HomeFragment();
                    selectorFragment = homeFragment;
                    break;
                case R.id.action_subs:
                    if(subsFragment == null)
                        subsFragment = new SubsFragment();
                    selectorFragment = subsFragment;
                    break;
                case R.id.action_post:
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                case R.id.action_notif:
                    selectorFragment = new NotifFragment();
                    break;
                case R.id.action_chat:
                    selectorFragment = new ChatFragment();
                    break;
            }
            if(selectorFragment!=null)
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectorFragment).commit();
            return true;
        });
        ImageView btmSheetClose = findViewById(R.id.bottom_sheet_close);
        btmSheetClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        openLinkPost(findViewById(R.id.link_icon));
        openLinkPost(findViewById(R.id.link_lbl));
        openImagePost(findViewById(R.id.image_icon));
        openImagePost(findViewById(R.id.image_lbl));
        openVideoPost(findViewById(R.id.video_icon));
        openVideoPost(findViewById(R.id.video_lbl));
        openTextPost(findViewById(R.id.text_icon));
        openTextPost(findViewById(R.id.text_lbl));

        //delete this
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitialViewModel mInitialViewModel = new ViewModelProvider(MainActivity.this).get(InitialViewModel.class);
                mInitialViewModel.logout();
                Intent i = new Intent(getApplicationContext(), InitialActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
    public void openLinkPost(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LinkPostActivity.class);
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                startActivity(i);
            }
        });
    }
    public void openTextPost(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TextPostActivity.class);
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                startActivity(i);
            }
        });
    }
    public void openImagePost(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ImagePostActivity.class);
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                startActivity(i);
            }
        });
    }
    public void openVideoPost(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VideoPostActivity.class);
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                startActivity(i);
            }
        });
    }
}