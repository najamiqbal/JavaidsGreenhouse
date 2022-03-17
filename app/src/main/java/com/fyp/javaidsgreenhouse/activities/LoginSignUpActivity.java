package com.fyp.javaidsgreenhouse.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.fragments.LoginFragment;

public class LoginSignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_activity);
        LoginFragment loginFragment=new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, loginFragment, loginFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }
}
