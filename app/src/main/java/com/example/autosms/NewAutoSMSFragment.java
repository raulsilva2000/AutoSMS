package com.example.autosms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class NewAutoSMSFragment extends Fragment {

    Spinner spinner;
    TextView selectedContactsNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.newautosms, container, false);

        spinner = view.findViewById(R.id.spinner);
        selectedContactsNumber = view.findViewById(R.id.selectedContacts);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("Specific Contacts")) {
                    Intent intent = new Intent(getActivity(), Contact_Pickers.class);
                    launcher.launch(intent);
                }
                else {
                    selectedContactsNumber.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        return view;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("selectedContacts")) {
                        String resultData = data.getStringExtra("selectedContacts");

                        String[] phoneNumbers = resultData.split(", ");
                        String totalSelected = String.valueOf(phoneNumbers.length) + " selected";

                        selectedContactsNumber.setText(totalSelected);
                    }
                }
            });


}