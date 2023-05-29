package com.example.autosms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewAutoSMSFragment extends Fragment {
    EditText title;
    EditText message;
    Spinner spinnerSimCards;
    Spinner spinnerNumbers;
    CheckBox checkBoxNewMon;
    CheckBox checkBoxNewTue;
    CheckBox checkBoxNewWed;
    CheckBox checkBoxNewThu;
    CheckBox checkBoxNewFri;
    CheckBox checkBoxNewSat;
    CheckBox checkBoxNewSun;
    TextView selectedContactsNumber;
    TextView selectedSIMCards;
    Button buttonNewCreate;
    String[] phoneNumbers;
    String[] simCards;
    Button addSimCards;
    Button addContacts;
    String totalSelectedContacts;
    String totalSelectedCards;
    List<String> selectedSimCardsList;
    List<String> selectedNumbersList;

    private ActivityResultLauncher<Intent> launcher;

    private int selectedSpinnerPosition = 0; // Store the selected position here

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Reset the values in your fragment here
        //spinnerSimCards.setSelection(0);
        //spinnerNumbers.setSelection(0);
        // Reset other views and variables as needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.newautosms, container, false);

        spinnerSimCards = view.findViewById(R.id.spinnerSimCards);
        selectedSIMCards = view.findViewById(R.id.selectedSIMCards);

        spinnerNumbers = view.findViewById(R.id.spinnerNumbers);
        selectedContactsNumber = view.findViewById(R.id.selectedContacts);

        title = view.findViewById(R.id.editTextNewTitle);
        message = view.findViewById(R.id.editTextNewMessage);

        addSimCards = view.findViewById(R.id.buttonNewAddCards);
        addContacts = view.findViewById(R.id.buttonNewAddContacts);

        buttonNewCreate = view.findViewById(R.id.buttonNewCreate);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("selectedContacts")) {
                            String resultData = data.getStringExtra("selectedContacts");

                            phoneNumbers = resultData.split(", ");
                            String totalSelected = phoneNumbers.length + " selected";

                            totalSelectedContacts = totalSelected;
                            selectedContactsNumber.setText(totalSelected);
                            selectedNumbersList = Arrays.asList(phoneNumbers);
                        } else if (data != null && data.hasExtra("selectedSimCards")) {
                            String resultData = data.getStringExtra("selectedCards");
                            if(resultData == null){
                                Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
                            } else {
                                simCards = resultData.split(", ");
                                String totalSelected = simCards.length + " selected";

                                totalSelectedCards = totalSelected;
                                selectedSIMCards.setText(totalSelected);
                                selectedSimCardsList = Arrays.asList(simCards);
                            }
                        }
                    }
                }
        );

        spinnerNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("Specific Contacts")) {
                    addContacts.setVisibility(View.VISIBLE);
                    selectedContactsNumber.setText(totalSelectedContacts);
                }
                else {
                    addContacts.setVisibility(View.INVISIBLE);
                    selectedContactsNumber.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Contact_Pickers.class);
                launcher.launch(intent);
            }
        });

        spinnerSimCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("Specific Cards")) {
                    addSimCards.setVisibility(View.VISIBLE);
                    selectedSIMCards.setText(totalSelectedCards);
                }
                else {
                    addSimCards.setVisibility(View.INVISIBLE);
                    selectedSIMCards.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        addSimCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SIMCard_Pickers.class);
                launcher.launch(intent);
            }
        });

        buttonNewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the JSON file update logic here

                // Read the existing JSON file, update the data structure, and write the updated JSON back to the file
                try {
                    FileInputStream fis = getContext().openFileInput("data.json");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    isr.close();
                    fis.close();

                    String existingJson = stringBuilder.toString();

                    // Parse the existing JSON into a data structure
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<AutoSMS>>() {}.getType();
                    List<AutoSMS> replys = gson.fromJson(existingJson, listType);

                    Boolean[] days = new Boolean[]{false, false, false, false, false, false, false};

                    // Check which CheckBoxs are checked
                    if(checkBoxNewMon.isChecked()){
                        days[0] = true;
                    } else if (checkBoxNewTue.isChecked()) {
                        days[1] = true;
                    } else if (checkBoxNewWed.isChecked()) {
                        days[2] = true;
                    } else if (checkBoxNewThu.isChecked()) {
                        days[3] = true;
                    } else if (checkBoxNewFri.isChecked()) {
                        days[4] = true;
                    } else if (checkBoxNewSat.isChecked()) {
                        days[5] = true;
                    } else if (checkBoxNewSun.isChecked()) {
                        days[6] = true;
                    }

                    // Update the data structure (e.g., add, remove, or modify elements)
                    replys.add(new AutoSMS(title.getText().toString(),
                            message.getText().toString(),
                            Arrays.asList(simCards),
                            Arrays.asList(phoneNumbers),
                            days,
                            "",
                            System.currentTimeMillis()));

                    // Convert the updated data structure to JSON
                    String updatedJson = gson.toJson(replys);

                    // Write the updated JSON to the file
                    FileOutputStream fos = getContext().openFileOutput("data.json", getContext().MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    osw.write(updatedJson);
                    osw.close();
                    fos.close();
                    Log.d("JSON Update", "JSON file updated successfully.");
                } catch (IOException e) {
                    Log.e("JSON Update", "Error updating JSON file: " + e.getMessage());
                }
            }
        });

        return view;
    }
}