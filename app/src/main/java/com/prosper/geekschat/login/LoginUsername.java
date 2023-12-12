package com.prosper.geekschat.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.prosper.geekschat.MainActivity;
import com.prosper.geekschat.R;
import com.prosper.geekschat.model.UserModel;
import com.prosper.geekschat.utils.FirebaseUtil;

public class LoginUsername extends AppCompatActivity {

    String phoneNumber;
    EditText usernameInput;
    Button startGeeking;
    ProgressBar progressBar;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        phoneNumber = getIntent().getExtras().getString("phone");
        getUsername();

        usernameInput = findViewById(R.id.login_username);
        startGeeking = findViewById(R.id.start_geeking_btn);
        progressBar =findViewById(R.id.login_progress_bar);

        startGeeking.setOnClickListener(v ->
        {
            setUsername();
        });
    }

    //To set the username upon logging in if exist
    void setUsername(){

        String username = usernameInput.getText().toString();
        if(username.isEmpty() || username.length()<3){
            usernameInput.setError("Username length should be at least 3 chars");
            return;
        }
        setInProgress(true);
        if(userModel!=null){
            userModel.setUsername(username);
        }else{
            userModel = new UserModel(phoneNumber,username, Timestamp.now(),FirebaseUtil.currentUserId());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(task -> {
            setInProgress(false);
            if(task.isSuccessful()){
                Intent intent = new Intent(LoginUsername.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
            }
        });

    }

    //Retrieve the username
    private void getUsername()
    {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            if(task.isSuccessful()){
                userModel =    task.getResult().toObject(UserModel.class);
                if(userModel!=null){
                    usernameInput.setText(userModel.getUsername());
                }
            }
        });


    }

    void setInProgress(boolean inProgress)
    {
        if (inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            startGeeking.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            startGeeking.setVisibility(View.VISIBLE);
        }
    }
}