package com.example.autosms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class CheckMessageSent extends AppCompatActivity {
    ImageView exitMessage;
    TextView messageTitle;
    TextView messageTime;
    TextView messageNumber;
    EditText messageSent;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_message_sent);

        exitMessage = findViewById(R.id.exitMessage);
        messageTitle = findViewById(R.id.textViewMessageTitle);
        messageTime = findViewById(R.id.textViewMessageTime);
        messageNumber = findViewById(R.id.textViewMessageNumber);
        messageSent = findViewById(R.id.editTextMessageSent);
        messageSent.setEnabled(false);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String number = intent.getStringExtra("number");
        String message = intent.getStringExtra("message");

        messageTitle.setText(title);
        messageTime.setText(time);
        messageNumber.setText(number);
        messageSent.setText(message);

        exitMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
