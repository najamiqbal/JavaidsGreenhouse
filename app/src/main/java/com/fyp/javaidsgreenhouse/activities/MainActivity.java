package com.fyp.javaidsgreenhouse.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.carteasy.v1.lib.Carteasy;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.adapters.ViewPagerAdapter;
import com.fyp.javaidsgreenhouse.fragments.CartFragment;
import com.fyp.javaidsgreenhouse.fragments.HomeFragment;
import com.fyp.javaidsgreenhouse.fragments.ProfileFragment;
import com.fyp.javaidsgreenhouse.utils.NonSwappableViewPager;
import com.google.android.material.badge.BadgeDrawable;
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

import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    Toolbar toolBar;
    BadgeDrawable badge;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setIcon(R.drawable.toolbarlogo);
        activity=this;

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        int menuItemId = bottomNavigationView.getMenu().getItem(1).getItemId();
        badge = bottomNavigationView.getOrCreateBadge(menuItemId);
        //badge.setNumber(2);
        getCartCount();


    }

    public void getCartCount() {
        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(MainActivity.this);
        int count=data.size();
        badge.setNumber(count);
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