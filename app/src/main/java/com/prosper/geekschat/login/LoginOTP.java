package com.prosper.geekschat.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prosper.geekschat.R;
import com.prosper.geekschat.utils.AndroidUtil;

import java.util.concurrent.TimeUnit;

public class LoginOTP extends AppCompatActivity {

    String phoneNumber;
    String verificationId;
    Long timeout = 60L;
    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    PhoneAuthProvider.ForceResendingToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up the layout, ui elements, and variables, passing IDs
        setContentView(R.layout.activity_login_otp);
        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.otp_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);


        //Printing back the number
        phoneNumber = getIntent().getExtras().getString("phone");
        //Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_LONG).show();

        //Sending the OTP
        sendOTP(phoneNumber, false);

        //Move onto the next
        nextBtn.setOnClickListener(v ->
        {
            String otp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            signIn(credential);
            setInProgress(true);
        });


    }

    //Function to send the OTP, progress, verifier, callbacks
    void sendOTP(String phoneNumber, boolean isResend)
    {
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
        PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
                .setTimeout(timeout, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(), "OTP Verification failed");
                        setInProgress(false);
                    }
                    //Function to verify the OTP received and resend the OTP
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(), "OTP Sent!");
                        setInProgress(false);
                    }
                });

        //Function to resend the OTP
        if(isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(token ).build());
        }
        else
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }



    }

    //Function to sign in the user with the OTP received and move to the next activity
    private void signIn(PhoneAuthCredential phoneAuthCredential)
    {
        setInProgress(true);
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(LoginOTP.this, LoginUsername.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                }
                else
                {
                    AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                }
            }
        });
    }

    void setInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            nextBtn.setVisibility(Button.VISIBLE);
        }
    }
}