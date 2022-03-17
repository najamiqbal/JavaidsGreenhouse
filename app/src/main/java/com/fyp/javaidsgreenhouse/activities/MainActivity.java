package com.fyp.javaidsgreenhouse.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.adapters.ViewPagerAdapter;
import com.fyp.javaidsgreenhouse.fragments.CartFragment;
import com.fyp.javaidsgreenhouse.fragments.HomeFragment;
import com.fyp.javaidsgreenhouse.fragments.ProfileFragment;
import com.fyp.javaidsgreenhouse.utils.NonSwappableViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.fyp.javaidsgreenhouse.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    Toolbar toolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);



    }

    HomeFragment homeFragment = new HomeFragment();
    CartFragment cartFragment = new CartFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, homeFragment).commit();
                return true;

            case R.id.navigation_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, cartFragment).commit();
                return true;

            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, profileFragment).commit();
                return true;
        }
        return false;
    }
}