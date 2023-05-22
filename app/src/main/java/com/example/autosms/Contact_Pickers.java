package com.example.autosms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Contact_Pickers extends Activity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ListView lvContacts;
    private Button btnConfirm;
    private Button btnSelectAll;
    private TextView tvSelectedContacts;

    private List<Contact> contacts;
    private List<Contact> selectedContacts;
    private ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContacts = findViewById(R.id.lvContacts);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        tvSelectedContacts = findViewById(R.id.tvSelectedContacts);

        contacts = new ArrayList<>();
        selectedContacts = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.list_item_contact, contacts);
        lvContacts.setAdapter(adapter);

        // Set a click listener for the ListView items
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected contact
                Contact contact = (Contact) parent.getItemAtPosition(position);

                // Toggle the selection status of the contact
                contact.setSelected(!contact.isSelected());

                // Update the list view
                adapter.notifyDataSetChanged();

                // Update the selected contacts list
                if (contact.isSelected()) {
                    selectedContacts.add(contact);
                } else {
                    selectedContacts.remove(contact);
                }

                // Update the selected contacts text view
                updateSelectedContactsTextView();
            }
        });

        // Set a click listener for the Select All button
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the selection status of all contacts
                for (Contact contact : contacts) {
                    contact.setSelected(true);
                    selectedContacts.add(contact);
                }

                // Update the list view
                adapter.notifyDataSetChanged();

                // Update the selected contacts text view
                updateSelectedContactsTextView();
            }
        });

        // Set a click listener for the Confirm button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something with the selected contacts
                String message = "Selected Contacts:";
                for (Contact contact : selectedContacts) {
                    message += "\n" + contact.getName() + " (" + contact.getPhoneNumber() + ")";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Check if the READ_CONTACTS permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Permission has already been granted
            importContacts();
        }
    }

