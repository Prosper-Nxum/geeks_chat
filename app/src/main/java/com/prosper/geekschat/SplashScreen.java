package com.prosper.geekschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.prosper.geekschat.login.PhoneLogin;
import com.prosper.geekschat.utils.FirebaseUtil;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Handler with a Method to move from the slash screen to the login
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseUtil.isLoggedIn()){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                }else{
                    startActivity(new Intent(SplashScreen.this,PhoneLogin.class));
                }
                finish();
            }
        },1000);

    }
}