package com.example.shreddit.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.shreddit.R;
import com.example.shreddit.ViewModels.InitialViewModel;
import com.example.shreddit.Views.Initial.InitialActivity;
import com.example.shreddit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Fragment selectorFragment;
    private Fragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFragment()).commit();
        binding.navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    if(homeFragment == null)
                        homeFragment = new HomeFragment();
                    selectorFragment = homeFragment;
                    break;
                case R.id.action_subs:
                    selectorFragment = new SubsFragment();
                    break;
                case R.id.action_post:
                    selectorFragment = new PostFragment();
                    break;
                case R.id.action_notif:
                    selectorFragment = new NotifFragment();
                    break;
                case R.id.action_chat:
                    selectorFragment = new ChatFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectorFragment).commit();
            return true;
        });

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
}