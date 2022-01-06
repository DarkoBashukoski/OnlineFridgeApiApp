package com.example.onlinefridgeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.example.onlinefridgeapi.user.UserFragment;
import com.example.onlinefridgeapi.recipes.RecipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

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

        if (!internetIsConnected()) {
            createNetErrorDialog();
        }

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

    protected void createNetErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
                .setTitle("Unable to connect")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        (dialog, id) -> {
                            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(i);
                        }
                )
                .setNegativeButton("Cancel",
                        (dialog, id) -> this.finish()
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean internetIsConnected() {
        try {
            return (Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}