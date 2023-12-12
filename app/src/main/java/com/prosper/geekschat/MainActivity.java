package com.prosper.geekschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;

    ChatFragment chatFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search_btn);

        //Search a user
        searchButton.setOnClickListener(v -> {

        });

        //Items upon being selected (Navigation between Chat and Profile Screen)
        bottomNavigationView.setOnItemSelectedListener(item ->
        {
            if(item.getItemId()==R.id.menu_chat){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,chatFragment).commit();
            }
            if(item.getItemId()==R.id.menu_profile){
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,profileFragment).commit();
            }
            return true;

        });

        bottomNavigationView.setSelectedItemId(R.id.menu_chat);

    }
}