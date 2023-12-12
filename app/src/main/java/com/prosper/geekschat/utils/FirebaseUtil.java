package com.prosper.geekschat.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    //Method that will keep the user login when the app is restarted instead of starting as user is already logged in
    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }



    //Method to retrieve the user if from firestore and return
    public static String currentUserId()
    {
        return FirebaseAuth.getInstance().getUid();
    }

    //To store the users in the firebase
    public static DocumentReference currentUserDetails()
    {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

}
