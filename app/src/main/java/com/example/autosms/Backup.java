package com.example.autosms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Backup extends AppCompatActivity {
    ImageView exitBackup;
    RecyclerView backupRecyclerView;
    BackupAdapter backupAdapter;
    Button exportButton;
    Button importButton;
    ArrayList<BackupItem> backupList;
    private StorageReference storageRef;
    private String uid;
    private List<AutoSMS> replys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup);

        exitBackup = findViewById(R.id.exitMessage);
        exportButton = findViewById(R.id.exportButton);
        backupRecyclerView = findViewById(R.id.backupRecyclerView);
        importButton = findViewById(R.id.importButton);

        // Initialize Firebase Storage
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        // Get uid of current user
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        backupList = new ArrayList<>();

        backupAdapter = new BackupAdapter(backupList);
        backupRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        backupRecyclerView.setAdapter(backupAdapter);

        // Obtain all previous backups from user, stored in database
        getJsonFileNames();

        //GET CURRENT ACTIVE REPLYS FROM DATA.JSON AND EXPORT IT TO DATABASE
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data.json file from internal storage
                File file = new File(getFilesDir(), "data.json");

                // Generate the new filename with the current date
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String newFileName = "" + currentDate + ".json";

                // Get ref of where to put the file in relation to current user
                StorageReference userDirectoryRef = storageRef.child("users/" + uid + "/" + newFileName);

                // Upload the file to Firebase Storage
                userDirectoryRef.putFile(Uri.fromFile(file))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // File uploaded successfully

                                // Get list of backups again
                                getJsonFileNames();

                                // You can handle the success event here
                                Toast.makeText(Backup.this, "New backup was created", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error occurred while uploading the file
                                // You can handle the failure event here
                                Toast.makeText(Backup.this, "Failed to create new backup: ", Toast.LENGTH_SHORT).show();
                            }
                         });
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupItem selectedBackup = backupAdapter.getSelectedItem();
                //GET JSON FILE FROM DATABASE AND UPLOAD IT TO ACTIVE REPLYS
                if (selectedBackup != null) {
                    String selectedBackupDate = selectedBackup.getDate();

                    // Create a reference to the specific JSON file in Firebase Storage
                    StorageReference fileRef = storageRef.child("users").child(uid).child(selectedBackupDate);

                    fileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // File downloaded successfully as a byte array
                            String jsonContent = new String(bytes, StandardCharsets.UTF_8);

                            try {
                                // First, read the selected backup and get the replys
                                Gson gsonFromBackup = new Gson();
                                AutoSMS[] autoSMSArrayFromBackup = gsonFromBackup.fromJson(jsonContent, AutoSMS[].class);

                                for (AutoSMS reply : autoSMSArrayFromBackup) {
                                    replys.add(new AutoSMS(reply.getTitle(), reply.getMessage(), reply.getSimCards(), reply.getNumbers(), reply.getDays(), reply.getTimeFrom(), reply.getTimeTo(), reply.getTimestamp()));
                                }

                                // Second, read data.json from internal storage and get current active replys
                                FileInputStream inputStream = openFileInput("data.json");
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                StringBuilder jsonData = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    jsonData.append(line);
                                }
                                reader.close();

                                Gson gson = new Gson();
                                AutoSMS[] autoSMSArray = gson.fromJson(jsonData.toString(), AutoSMS[].class);

                                for (AutoSMS reply : autoSMSArray) {
                                    replys.add(new AutoSMS(reply.getTitle(), reply.getMessage(), reply.getSimCards(), reply.getNumbers(), reply.getDays(), reply.getTimeFrom(), reply.getTimeTo(), reply.getTimestamp()));
                                }

                                // Convert the updated data structure to JSON
                                String updatedJson = gson.toJson(replys);

                                // Write the updated JSON to the file
                                FileOutputStream fos = openFileOutput("data.json", MODE_PRIVATE);
                                OutputStreamWriter osw = new OutputStreamWriter(fos);
                                osw.write(updatedJson);
                                osw.close();
                                fos.close();

                                Toast.makeText(Backup.this, "Backup loaded with success", Toast.LENGTH_SHORT).show();

                                Intent resultIntent = new Intent(Backup.this, MainActivity.class);
                                resultIntent.putExtra("backup_completed", true);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish(); //close backup activity
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while downloading the file
                            // You can handle the failure event here
                            Toast.makeText(Backup.this, "Failed to load backup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.d("Selected Backup", selectedBackupDate);
                } else {
                    Log.d("Selected Backup", "No backup selected");
                }
            }
        });

        exitBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getJsonFileNames() {
        // Create a reference to the users/uid directory in Firebase Storage
        StorageReference userDirectoryRef = storageRef.child("users").child(uid);

        // Fetch the list of files in the directory
        userDirectoryRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        // Clear the existing data
                        backupList.clear();

                        // Iterate through the file list
                        for (StorageReference fileRef : listResult.getItems()) {
                            // Get the file name
                            String fileName = fileRef.getName();

                            // Add the file name to the start of the list
                            backupList.add(0, new BackupItem(fileName));
                        }

                        // Notify the adapter of the data change
                        backupAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while fetching the file names
                        // You can handle the failure event here
                        Toast.makeText(Backup.this, "Failed to load backups", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
