package com.example.autosms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NewAutoSMSFragment extends Fragment {
    EditText title;
    EditText message;
    Spinner spinnerSimCards;
    Spinner spinnerNumbers;
    Spinner spinnerTime;
    LinearLayout linearLayoutTime;
    CheckBox checkBoxNewMon;
    CheckBox checkBoxNewTue;
    CheckBox checkBoxNewWed;
    CheckBox checkBoxNewThu;
    CheckBox checkBoxNewFri;
    CheckBox checkBoxNewSat;
    CheckBox checkBoxNewSun;
    TextView selectedContactsNumber;
    Button buttonNewCreate;
    String[] phoneNumbers;
    List<String> simCards;
    Button addContacts;
    String totalSelectedContacts;
    String selectedSimCard;
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
        spinnerNumbers = view.findViewById(R.id.spinnerNumbers);
        selectedContactsNumber = view.findViewById(R.id.selectedContacts);
        spinnerTime = view.findViewById(R.id.spinnerTime);
        linearLayoutTime = view.findViewById(R.id.linearLayoutTime);
        title = view.findViewById(R.id.editTextNewTitle);
        message = view.findViewById(R.id.editTextNewReply);
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

        simCards = new ArrayList<>();

        //Load SIM Cards from phone to Spinner
        loadSIMCards();

        //GET SIM CARDS AND PHONE NUMBERS
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("selectedContacts")) {
                            String resultData = data.getStringExtra("selectedContacts");
                            Log.d("DADOS DO resultData", resultData);
                            if (!resultData.isEmpty()) {
                                phoneNumbers = resultData.split(", ");
                                String totalSelected = phoneNumbers.length + " selected";

                                totalSelectedContacts = totalSelected;
                                selectedContactsNumber.setText(totalSelected);
                                selectedNumbersList = Arrays.asList(phoneNumbers);
                            } else {
                                selectedContactsNumber.setText(getString(R.string.none_selected));
                                selectedNumbersList = null;
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

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("Specific Time")) {
                    linearLayoutTime.setVisibility(View.VISIBLE);
                    timePickerFrom.setVisibility(View.VISIBLE);
                    timePickerTo.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayoutTime.setVisibility(View.GONE);
                    timePickerFrom.setVisibility(View.GONE);
                    timePickerTo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });


        //CREATE NEW AUTOSMS REPLY
        buttonNewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read the existing JSON file
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

                    // Validation, check if there's a reply with the same title already
                    for(AutoSMS reply : replys){
                        if(reply.getTitle().equals(title.getText().toString())){
                            Toast.makeText(getContext(), "There's a reply with that title already! Please change it", Toast.LENGTH_SHORT).show();
                            throw new IOException();
                        }
                    }

                    //Check which spinner SIM CARD option is selected
                    selectedSimCard = spinnerSimCards.getSelectedItem().toString(); //String comes in format "SIM 1"
                    String simCardIndex = selectedSimCard.substring(selectedSimCard.length()-1); //Get last char
                    Log.d("CARTAO", simCardIndex);

                    //Check which spinner Numbers option is selected
                    if(spinnerNumbers.getSelectedItemPosition() == 0){ //check if option selected is "Unknown Numbers"
                        selectedNumbersList = new ArrayList<>(Arrays.asList("unknownNumbers"));
                    } else if(spinnerNumbers.getSelectedItemPosition() == 1){ //check if option selected is "Any Number"
                        selectedNumbersList = new ArrayList<>(Arrays.asList("anyNumber"));
                    } else if(spinnerNumbers.getSelectedItemPosition() == 2){ //check if option selected is "Specific Contacts"
                        // Validation, check if 0 specific contacts are selected
                        if (selectedNumbersList==null){
                            Toast.makeText(getContext(), "You need to select atleast 1 specific contact", Toast.LENGTH_SHORT).show();
                            throw new IOException();
                        }
                    }

                    // Check which CheckBoxs are checked
                    Boolean[] days = new Boolean[]{checkBoxNewMon.isChecked(), checkBoxNewTue.isChecked(), checkBoxNewWed.isChecked(), checkBoxNewThu.isChecked(), checkBoxNewFri.isChecked(), checkBoxNewSat.isChecked(), checkBoxNewSun.isChecked()};

                    // Validation, check if 0 days are selected
                    if(Arrays.equals(days, new Boolean[]{false, false, false, false, false, false, false})){
                        Toast.makeText(getContext(), "You need to select atleast 1 day", Toast.LENGTH_SHORT).show();
                        throw new IOException();
                    }

                    //Check which spinner Time option is selected
                    if(spinnerTime.getSelectedItemPosition() == 0){ //if option selected is "All day"
                        timeFrom = "24hours";
                        timeTo = "24hours";
                    } else { //if option selected is "Specific Time"
                        // Get the selected hour and minute from the TimePicker
                        int hourFrom, minuteFrom, hourTo, minuteTo;

                        hourFrom = timePickerFrom.getHour();
                        minuteFrom = timePickerFrom.getMinute();

                        hourTo = timePickerTo.getHour();
                        minuteTo = timePickerTo.getMinute();

                        // Format the hour and minute values as "00:00" format
                        timeFrom = String.format(Locale.getDefault(), "%02d:%02d", hourFrom, minuteFrom);
                        timeTo = String.format(Locale.getDefault(), "%02d:%02d", hourTo, minuteTo);
                    }

                    // Update the data structure (e.g., add, remove, or modify elements)
                    replys.add(0, new AutoSMS(title.getText().toString(),
                            message.getText().toString(),
                            simCardIndex,
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
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.showSelectedPage(R.id.active);

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

    private void loadSIMCards() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.from(getContext()).getActiveSubscriptionInfoList();
            if (subscriptionInfoList != null) {
                for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                    String simIndex = String.valueOf(subscriptionInfo.getSimSlotIndex()+1);

                    String displayName = "SIM " + simIndex;
                    Log.d("DADOS", displayName);
                    simCards.add(displayName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, simCards);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSimCards.setAdapter(adapter);
            }
        }
    }
}