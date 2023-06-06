package com.example.autosms;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    Button drawerRegister;
    ActivityResultLauncher<Intent> launcher;
    FirebaseAuth mAuth;
    private static final int REQUEST_PERMISSIONS_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request runtime permissions
        requestPermissions();

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
        drawerRegister = findViewById(R.id.loginButtonDrawerRegister);
        headerProfileImage = findViewById(R.id.imageViewHeaderProfileImage);
        drawerProfileImage = findViewById(R.id.imageViewProfile);
        drawerName = findViewById(R.id.textViewDrawerName);
        drawerEmail = findViewById(R.id.textViewDrawerEmail);

        mAuth = FirebaseAuth.getInstance();

        updateTextViewTitleHeader("Active Replys");
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                launcher.launch(intent);
            }
        });

        headerProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        drawerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                launcher.launch(intent);
            }
        });

        drawerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
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
                launcher.launch(intent);
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
                logout();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("email")) { //User logs in
                            String email = data.getStringExtra("email");
                            if (!email.isEmpty()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    drawerName.setText(user.getDisplayName());
                                    drawerEmail.setText(user.getEmail());
                                    closeDrawer();
                                    changeToLoggedIn();
                                }
                            }
                        } else if (data != null && data.hasExtra("backup_completed")) {
                            // We need to load active fragment again to refresh the RecyclerView
                            showSelectedPage(R.id.sent);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showSelectedPage(R.id.active);
                                }
                            }, 200); // Delay to give time for the UI change
                        }
                    }
                });
    }

    public void changeToLoggedIn() {
        loginButton.setVisibility(View.INVISIBLE);
        drawerLogin.setVisibility(View.INVISIBLE);
        drawerRegister.setVisibility(View.INVISIBLE);
        headerProfileImage.setVisibility(View.VISIBLE);
        drawerProfileImage.setVisibility(View.VISIBLE);
        drawerName.setVisibility(View.VISIBLE);
        drawerEmail.setVisibility(View.VISIBLE);
        logoutMenu.setVisibility(View.VISIBLE);
        backupMenu.setVisibility(View.VISIBLE);
    }

    private void showPopupMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.popup_header_image, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.backup) {
                    // Open Backup Page
                    Intent intent = new Intent(MainActivity.this, Backup.class);
                    launcher.launch(intent);
                    return true;
                } else if (id == R.id.logout) {
                    // Proceed to logout
                    logout();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
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

    public void editProfile(View view) {}

    public void language(View view) {}

    public void logout() {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CALL_LOG
        };

        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Denied");
        builder.setMessage("This app needs all the requested permissions to function properly. Please grant the permissions from the device settings.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

}