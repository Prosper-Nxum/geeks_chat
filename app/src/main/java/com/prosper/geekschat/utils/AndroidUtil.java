package com.prosper.geekschat.utils;

import android.content.Context;
import android.widget.Toast;

public class AndroidUtil {

    //show toast message
    public static  void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}
