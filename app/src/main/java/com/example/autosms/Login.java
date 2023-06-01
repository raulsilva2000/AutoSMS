package com.example.autosms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText userEmail, userPassword;
    private Button loginButton;
    private TextView clickRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initialize editTexts and button
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.loginButton);
        clickRegister = findViewById(R.id.clickRegister);

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
                }

                // calling a method to login our user
                loginUser(userEmailInput, userPasswordInput);
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
    }

    private void loginUser(String userEmailInput, String userPasswordInput) {
        Log.d("credentials", userEmailInput + " " + userPasswordInput);
        if (userEmailInput.equals("raul@gmail.com") && userPasswordInput.equals("1234")) {
            // User login and passing that user to new activity
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("email", userEmailInput);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            // display a toast message when user enters wrong credentials
            Toast.makeText(Login.this, "Wrong credentials!", Toast.LENGTH_LONG).show();
        }
    }


}