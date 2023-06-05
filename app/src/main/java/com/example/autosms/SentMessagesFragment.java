package com.example.autosms;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SentMessagesFragment extends Fragment {
    private SentMessagesAdapter messagesAdapter;
    private RecyclerView recyclerViewMessages;
    private List<SentMessage> messagesList = new ArrayList<>();
    Spinner sortSpinner;
    TextView total;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sent_messages, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sortSpinner = view.findViewById(R.id.spinner_filters);
        total = view.findViewById(R.id.textViewTotal);

        initRecyclerview(view);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("First sent")) {
                    messagesAdapter.sortItemsByCreation(position);
                } else if(selectedOption.equals("Last sent")) {
                    messagesAdapter.sortItemsByCreation(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void initRecyclerview(View view) {
        //First clear messagesList to load again the data from file
        messagesList.clear();

        try {
            // Step 1: Check if "messages.json" file exists
            File file = new File(getContext().getFilesDir(), "messages.json");
            if (!file.exists()) {
                // File doesn't exist, create it with an empty array
                FileOutputStream outputStream = getContext().openFileOutput("messages.json", Context.MODE_PRIVATE);
                outputStream.write("[]".getBytes());
                outputStream.close();
            }

            // Step 2: Read JSON data from the file
            FileInputStream inputStream = getContext().openFileInput("messages.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            reader.close();

            // Step 3: Parse JSON data into objects of SentMessage class
            Gson gson = new Gson();
            SentMessage[] messagesArray = gson.fromJson(jsonData.toString(), SentMessage[].class);
            for (SentMessage message : messagesArray) {
                messagesList.add(new SentMessage(message.getTitle(), message.getTime(), message.getNumber(), message.getMessage()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerViewMessages = view.findViewById(R.id.recyclerView);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.VERTICAL, false));

        messagesAdapter = new SentMessagesAdapter(messagesList, requireContext());
        recyclerViewMessages.setAdapter(messagesAdapter);

        String totalMessages = "Total: " + messagesAdapter.getItemCount();
        total.setText(totalMessages);
    }
}