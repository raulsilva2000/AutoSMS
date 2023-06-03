package com.example.autosms;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActiveFragment extends Fragment {
    private AutoSMSAdapter autoSMSAdapter;
    private RecyclerView recyclerViewReplys;
    private List<AutoSMS> replys = new ArrayList<>();
    Spinner sortSpinner;
    TextView noReplys;
    ImageView arrowImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.active, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerview(view);

        sortSpinner = view.findViewById(R.id.spinner_filters);
        noReplys = view.findViewById(R.id.textViewNoReplys);

        arrowImageView = view.findViewById(R.id.arrowImageView);
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.arrow_animation);
        animatorSet.setTarget(arrowImageView);
        animatorSet.cancel();

        if(autoSMSAdapter.getItemCount() == 0) {
            noReplys.setVisibility(View.VISIBLE);
            arrowImageView.setVisibility(View.VISIBLE);
            animatorSet.start();

        }

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("First created")) {
                    autoSMSAdapter.sortItemsByCreation(position);
                } else if(selectedOption.equals("Last created")) {
                    autoSMSAdapter.sortItemsByCreation(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void initRecyclerview(View view) {
        //First clear replys Array to load again the data from file
        replys.clear();

        // Step 1: Check if "data.json" file exists
        File file = new File(getContext().getFilesDir(), "data.json");
        if (!file.exists()) {
            // File doesn't exist, create it with an empty array
            try {
                FileOutputStream outputStream = getContext().openFileOutput("data.json", Context.MODE_PRIVATE);
                outputStream.write("[]".getBytes());
                outputStream.close();
                Log.d("JSON File", "data.json created successfully.");
            } catch (IOException e) {
                Log.e("JSON File", "Error creating data.json file: " + e.getMessage());
            }
        }

        try {
            // Step 2: Read JSON data from the file
            FileInputStream inputStream = getContext().openFileInput("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            reader.close();

            // Step 2: Parse JSON data into objects of AutoSMS class
            Gson gson = new Gson();
            AutoSMS[] autoSMSArray = gson.fromJson(jsonData.toString(), AutoSMS[].class);
            for (AutoSMS reply : autoSMSArray) {
                replys.add(new AutoSMS(reply.getTitle(), reply.getMessage(), reply.getSimCards(), reply.getNumbers(), reply.getDays(), reply.getTimeFrom(), reply.getTimeTo(), reply.getTimestamp()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        recyclerViewReplys = view.findViewById(R.id.recyclerView);
        recyclerViewReplys.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.VERTICAL, false));

        autoSMSAdapter = new AutoSMSAdapter(replys, requireContext());
        recyclerViewReplys.setAdapter(autoSMSAdapter);
    }

}