package com.fyp.javaidsgreenhouse.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handle the splash screen transition.

        UserModelClass userModelClass = SharedPrefManager.getInstance(this).getUser();
        if (userModelClass != null) {
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(this, LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
