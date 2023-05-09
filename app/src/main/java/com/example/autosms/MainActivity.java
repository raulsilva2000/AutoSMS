package com.example.autosms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ActiveFragment activeFragment = new ActiveFragment();
    NewAutoSMSFragment newAutoSMSFragment = new NewAutoSMSFragment();
    SentMessagesFragment sentMessagesFragment = new SentMessagesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.active){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();
                    return true;
                } else if (id == R.id.autoSMS) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, newAutoSMSFragment).commit();
                    return true;
                } else if (id == R.id.sent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, sentMessagesFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}