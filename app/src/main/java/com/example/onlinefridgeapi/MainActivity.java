package com.example.onlinefridgeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.onlinefridgeapi.user.UserFragment;
import com.example.onlinefridgeapi.recipes.RecipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private UserFragment userFragment;
    private RecipeFragment recipeFragment;
    private BottomNavigationView mainNavigation;

    private FragmentManager fragmentManager;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userFragment = UserFragment.newInstance();
        recipeFragment = RecipeFragment.newInstance();
        mainNavigation = findViewById(R.id.mainNavigation);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().setReorderingAllowed(true).add(R.id.mainFragment, recipeFragment).commit();

        addNavigationListener();
    }

    private void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.mainFragment, fragment).commit();
    }

    @SuppressLint("NonConstantResourceId")
    private void addNavigationListener() {
        mainNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ingredients:
                    changeFragment(userFragment);
                    return true;
                case R.id.recipes:
                    changeFragment(recipeFragment);
                    return true;
                default:
                    return false;
            }
        });
    }
}