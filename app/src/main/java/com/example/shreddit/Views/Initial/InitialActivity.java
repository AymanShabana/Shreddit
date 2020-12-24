package com.example.shreddit.Views.Initial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.shreddit.R;
import com.example.shreddit.ViewModels.InitialViewModel;
import com.example.shreddit.Views.Main.MainActivity;

public class InitialActivity extends AppCompatActivity {
    private InitialViewModel mInitialViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        mInitialViewModel = new ViewModelProvider(this).get(InitialViewModel.class);
        if(mInitialViewModel.getUser()!=null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}