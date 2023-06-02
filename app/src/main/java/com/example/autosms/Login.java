package com.example.autosms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Objects;

public class Login extends AppCompatActivity {
    ImageView exitLogin;
    EditText userEmail, userPassword;
    Button loginButton;
    TextView clickRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initialize editTexts and button
        exitLogin = findViewById(R.id.exitLogin);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.loginButton);
        clickRegister = findViewById(R.id.clickRegister);

        mAuth = FirebaseAuth.getInstance();

        //Click listener on login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting data from our editTexts
                String userEmailInput = userEmail.getText().toString();
                String userPasswordInput = userPassword.getText().toString();

                // Checking if the entered text is empty or not
                if (TextUtils.isEmpty(userEmailInput) && TextUtils.isEmpty(userPasswordInput)) {
                    Toast.makeText(Login.this, "Please enter user email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // calling a method to login our user
                    loginUser(userEmailInput, userPasswordInput);
                }
            }
        });

        //Click listener on text to switch to register
        clickRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loginUser(String userEmailInput, String userPasswordInput) {
        // Login an existing user
        mAuth.signInWithEmailAndPassword(userEmailInput, userPasswordInput)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, redirect to main activity
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // User is logged in, start main activity
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("email", userEmailInput);
                            setResult(RESULT_OK, intent);
                            finish();
                            Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Login failed, handle the error
                        Toast.makeText(Login.this, "Wrong credentials!", Toast.LENGTH_LONG).show();
                    }
                });
    }


}