package com.example.autosms;

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Register extends AppCompatActivity {
    private EditText userEmail, userName, userPassword, userConfirmPassword;
    private Button registerButton;
    private TextView clickLogin;
    private ImageView backRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Initialize editTexts and button
        userEmail = findViewById(R.id.emailRegister);
        userName = findViewById(R.id.nameRegister);
        userPassword = findViewById(R.id.passwordRegister);
        userConfirmPassword = findViewById(R.id.passwordConfirmRegister);
        registerButton = findViewById(R.id.registerButton);
        clickLogin = findViewById(R.id.clickLogin);
        backRegister = findViewById(R.id.backRegister);

        //Click listener on login button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting data from our editTexts
                String userEmailInput = userEmail.getText().toString();
                String userNameInput = userName.getText().toString();
                String userPasswordInput = userPassword.getText().toString();
                String userConfirmPasswordInput = userConfirmPassword.getText().toString();

                // Checking if the entered text is empty or not
                if (TextUtils.isEmpty(userEmailInput) || TextUtils.isEmpty(userNameInput) || TextUtils.isEmpty(userPasswordInput) || TextUtils.isEmpty(userConfirmPasswordInput)) {
                    Toast.makeText(Register.this, "No empty fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (userPasswordInput != userConfirmPasswordInput){
                        Toast.makeText(Register.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    }
                }

                // calling a method to register our user
                registerUser(userEmailInput, userNameInput, userPasswordInput);
            }
        });

        //Click listener on text to switch to login
        clickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        //Click listener on back arrow to switch to login
        backRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String userEmailInput, String userNameInput, String userPasswordInput) {
        //register code
    }


}