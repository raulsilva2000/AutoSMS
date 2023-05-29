package com.example.autosms;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Contact_Pickers extends Activity {
    private ArrayList<Contact> contacts;
    private ContactAdapter contactAdapter;
    private TextView cancelButton;
    private Button button_deselect_all;
    private static final int REQUEST_CODE_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_pickers);

        ListView contactsListView = findViewById(R.id.list_view_simCards);
        Button confirmButton = findViewById(R.id.button_confirm);
        Button selectAllButton = findViewById(R.id.button_select_all);
        cancelButton = findViewById(R.id.cancelButton);
        button_deselect_all = findViewById(R.id.button_deselect_all);

        contacts = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contacts);
        contactsListView.setAdapter(contactAdapter);

        // Check if the READ_CONTACTS permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_READ_CONTACTS);
        } else {
            // Permission is already granted, proceed with loading contacts
            loadContacts();
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder selectedNumbers = new StringBuilder();
                for (Contact contact : contacts) {
                    if (contact.isSelected()) {
                        selectedNumbers.append(contact.getNumber()).append(", ");
                    }
                }
                if (selectedNumbers.length() > 0) {
                    selectedNumbers.setLength(selectedNumbers.length() - 2); // Remove last comma and space
                }

                Intent intent = new Intent();
                intent.putExtra("selectedContacts", selectedNumbers.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_deselect_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Contact contact : contacts) {
                        contact.setSelected(true);
                    }
                    contactAdapter.notifyDataSetChanged();
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with loading contacts
                loadContacts();
            } else {
                // Permission denied, handle accordingly (e.g., show an error message)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadContacts() {
        // Query the contacts from the device
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                Contact contact = new Contact(name, number);
                contacts.add(contact);
            }
            cursor.close();
        }

        contactAdapter.notifyDataSetChanged();
    }
}

