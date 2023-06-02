package com.example.autosms;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ActiveFragment activeFragment = new ActiveFragment();
    NewAutoSMSFragment newAutoSMSFragment = new NewAutoSMSFragment();
    SentMessagesFragment sentMessagesFragment = new SentMessagesFragment();
    DrawerLayout drawerLayout;
    ImageView menuIcon;
    ImageView closeMenu;
    ConstraintLayout homeMenu;
    ConstraintLayout newAutoSMS;
    ConstraintLayout messagesMenu;
    ConstraintLayout editProfileMenu;
    ConstraintLayout backupMenu;
    ConstraintLayout languageMenu;
    ConstraintLayout logoutMenu;
    ImageView headerProfileImage;
    ImageView drawerProfileImage;
    TextView drawerName;
    TextView drawerEmail;
    TextView loginButton;
    Button drawerLogin;
    ActivityResultLauncher<Intent> launcher;
    FirebaseAuth mAuth;

    private static final int REQUEST_PERMISSIONS_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        drawerLayout = findViewById(R.id.DrawerLayoutMain);

        menuIcon = findViewById(R.id.openDrawerMenu);
        closeMenu = findViewById(R.id.imageView21);
        homeMenu = findViewById(R.id.homeMenu);
        newAutoSMS = findViewById(R.id.newautosmsMenu);
        messagesMenu = findViewById(R.id.messagesMenu);
        editProfileMenu = findViewById(R.id.editprofileMenu);
        backupMenu = findViewById(R.id.backupMenu);
        languageMenu = findViewById(R.id.languageMenu);
        logoutMenu = findViewById(R.id.logoutMenu);
        loginButton = findViewById(R.id.textViewLoginButton);
        drawerLogin = findViewById(R.id.loginButtonDrawerLogin);
        headerProfileImage = findViewById(R.id.imageViewHeaderProfileImage);
        drawerProfileImage = findViewById(R.id.imageViewDrawerProfileImage);
        drawerName = findViewById(R.id.textViewDrawerName);
        drawerEmail = findViewById(R.id.textViewDrawerEmail);

        mAuth = FirebaseAuth.getInstance();

        updateTextViewTitleHeader("Active Replys");
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();

        // Request runtime permissions
        requestPermissions();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(MainActivity.this, Login.class);
                launcher.launch(intent);
            }
        });

        drawerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(MainActivity.this, Login.class);
                launcher.launch(intent);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.active){
                    updateTextViewTitleHeader("Active Replys");
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();
                    return true;
                } else if (id == R.id.autoSMS) {
                    updateTextViewTitleHeader("New AutoSMS Reply");
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, newAutoSMSFragment).commit();
                    return true;
                } else if (id == R.id.sent) {
                    updateTextViewTitleHeader("Sent Messages");
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, sentMessagesFragment).commit();
                    return true;
                }
                return false;
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClickDrawerMenu(view);
            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                closeDrawer();
            }
        });

        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dashboard(view);
            }
        });

        newAutoSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                newAutoSMS(view);
            }
        });

        messagesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sentMessages(view);
            }
        });

        editProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editProfile(view);
            }
        });

        backupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Backup.class);
                startActivity(intent);
            }
        });

        languageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                language(view);
            }
        });

        logoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                logout(view);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("email")) {
                            String email = data.getStringExtra("email");
                            if (!email.isEmpty()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    drawerName.setText(user.getDisplayName());
                                    drawerEmail.setText(user.getEmail());
                                    changeToLoggedIn();
                                }
                            }
                        }
                    }
                });
    }

    public void changeToLoggedIn() {
        loginButton.setVisibility(View.INVISIBLE);
        drawerLogin.setVisibility(View.INVISIBLE);
        headerProfileImage.setVisibility(View.VISIBLE);
        drawerProfileImage.setVisibility(View.VISIBLE);
        drawerName.setVisibility(View.VISIBLE);
        drawerEmail.setVisibility(View.VISIBLE);
        logoutMenu.setVisibility(View.VISIBLE);
    }

    public void updateTextViewTitleHeader(String text) {
        TextView textTitleHeader = findViewById(R.id.textViewTitleHeader);
        textTitleHeader.setText(text);
    }

    public void ClickDrawerMenu(View view) {
        openDrawer(drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void dashboard(View view) {
        showSelectedPage(R.id.active);
    }

    public void newAutoSMS(View view) {
        showSelectedPage(R.id.autoSMS);
    }

    public void sentMessages(View view) {
        showSelectedPage(R.id.sent);
    }

    public void editProfile(View view) {

    }

    public void language(View view) {

    }

    public void logout(View view) {
        logoutMenu((this));
    }

    private void logoutMenu(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void showSelectedPage(int id){
        if(id == R.id.active){
            updateTextViewTitleHeader("Active Replys");
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();
        } else if(id == R.id.autoSMS) {
            updateTextViewTitleHeader("New AutoSMS Reply");
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, newAutoSMSFragment).commit();
        } else if(id == R.id.sent){
            updateTextViewTitleHeader("Sent Messages");
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, sentMessagesFragment).commit();
        }

        bottomNavigationView.setSelectedItemId(id);
        closeDrawer();
    }

    private void requestPermissions() {
        //Request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS}, REQUEST_PERMISSIONS_CODE);
        }

        //Request permissions again if not accepted
        while (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            tryAgain();
        }
    }

    private void tryAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs PHONE, SMS permissions to work.");
        builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions();
            }
        });
        builder.setNegativeButton("CLOSE APP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

}