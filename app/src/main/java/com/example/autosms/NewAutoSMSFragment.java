package com.example.autosms;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    TimePicker timePickerFrom;
    TimePicker timePickerTo;
    ActivityResultLauncher<Intent> launcher;
    String timeFrom;
    String timeTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        checkBoxNewMon = view.findViewById(R.id.checkBoxNewMon);
        checkBoxNewTue = view.findViewById(R.id.checkBoxNewTue);
        checkBoxNewWed = view.findViewById(R.id.checkBoxNewWed);
        checkBoxNewThu = view.findViewById(R.id.checkBoxNewThu);
        checkBoxNewFri = view.findViewById(R.id.checkBoxNewFri);
        checkBoxNewSat = view.findViewById(R.id.checkBoxNewSat);
        checkBoxNewSun = view.findViewById(R.id.checkBoxNewSun);

        timePickerFrom = view.findViewById(R.id.timePickerFrom);
        timePickerTo = view.findViewById(R.id.timePickerTo);

        // Configure the TimePicker
        timePickerFrom.setIs24HourView(true); // Set to 24-hour mode
        timePickerTo.setIs24HourView(true); // Set to 24-hour mode

        buttonNewCreate = view.findViewById(R.id.buttonNewCreate);

        //GET SIM CARDS AND PHONE NUMBERS
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("selectedContacts")) {
                            String resultData = data.getStringExtra("selectedContacts");
                            Log.d("DADOS DO resultData", resultData);
                            if(!resultData.isEmpty()){
                                phoneNumbers = resultData.split(", ");
                                String totalSelected = phoneNumbers.length + " selected";

                                totalSelectedContacts = totalSelected;
                                selectedContactsNumber.setText(totalSelected);
                                selectedNumbersList = Arrays.asList(phoneNumbers);
                            } else{
                                selectedContactsNumber.setText(getString(R.string.none_selected));
                                selectedNumbersList = null;
                            }

                        } else if (data != null && data.hasExtra("selectedSimCards")) {
                            String resultData = data.getStringExtra("selectedSimCards");

                            if(!resultData.isEmpty()) {
                                simCards = resultData.split(", ");
                                String totalSelected = simCards.length + " selected";

                                totalSelectedCards = totalSelected;
                                selectedSIMCards.setText(totalSelected);
                                selectedSimCardsList = Arrays.asList(simCards);
                            } else {
                                selectedSIMCards.setText(getString(R.string.none_selected));
                                selectedSimCardsList = null;
                            }
                        }
                    }
                }
        );

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


        //CREATE NEW AUTOSMS REPLY
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

                    //Check which spinner option is selected
                    if(spinnerSimCards.getSelectedItemPosition() == 0){ //check if option selected is "All SIM Cards"
                        selectedSimCardsList = new ArrayList<>(Arrays.asList("allSimCards"));
                    }

                    if(spinnerNumbers.getSelectedItemPosition() == 0){ //check if option selected is "Unknown Numbers"
                        selectedNumbersList = new ArrayList<>(Arrays.asList("unknownNumbers"));
                    } else if(spinnerNumbers.getSelectedItemPosition() == 1){ //check if option selected is "Any Number"
                        selectedNumbersList = new ArrayList<>(Arrays.asList("anyNumber"));
                    }

                    // Check which CheckBoxs are checked
                    Boolean[] days = new Boolean[]{checkBoxNewMon.isChecked(), checkBoxNewTue.isChecked(), checkBoxNewWed.isChecked(), checkBoxNewThu.isChecked(), checkBoxNewFri.isChecked(), checkBoxNewSat.isChecked(), checkBoxNewSun.isChecked()};

                    // Get the selected hour and minute from the TimePicker
                    int hourFrom, minuteFrom, hourTo, minuteTo;

                    hourFrom = timePickerFrom.getHour();
                    minuteFrom = timePickerFrom.getMinute();

                    hourTo = timePickerTo.getHour();
                    minuteTo = timePickerTo.getMinute();

                    // Format the hour and minute values as "00:00" format
                    timeFrom = String.format(Locale.getDefault(), "%02d:%02d", hourFrom, minuteFrom);
                    timeTo = String.format(Locale.getDefault(), "%02d:%02d", hourTo, minuteTo);

                    // Update the data structure (e.g., add, remove, or modify elements)
                    replys.add(new AutoSMS(title.getText().toString(),
                            message.getText().toString(),
                            selectedSimCardsList,
                            selectedNumbersList,
                            days,
                            timeFrom,
                            timeTo,
                            System.currentTimeMillis()));

                    // Convert the updated data structure to JSON
                    String updatedJson = gson.toJson(replys);

                    // Write the updated JSON to the file
                    FileOutputStream fos = getContext().openFileOutput("data.json", getContext().MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    osw.write(updatedJson);
                    osw.close();
                    fos.close();
                    Toast.makeText(getContext(), "New AutoSMS Reply Created", Toast.LENGTH_SHORT).show();

                    // Reset Values after creating new autosms reply
                    title.setText("");
                    message.setText("");
                    spinnerSimCards.setSelection(0);
                    spinnerNumbers.setSelection(0);
                    checkBoxNewMon.setChecked(false);
                    checkBoxNewTue.setChecked(false);
                    checkBoxNewWed.setChecked(false);
                    checkBoxNewThu.setChecked(false);
                    checkBoxNewFri.setChecked(false);
                    checkBoxNewSat.setChecked(false);
                    checkBoxNewSun.setChecked(false);

                } catch (IOException e) {
                    Log.e("JSON Update", "Error updating JSON file: " + e.getMessage());
                }
            }
        });

        return view;
    }
}