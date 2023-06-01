package com.example.autosms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MissedCallService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");

        //aqui vamos ter percorrer as respostas automáticas do data.json
        // Step 1: Read JSON data from the file
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            reader.close();

            // Step 2: Parse JSON data into objects of AutoSMS class
            Gson gson = new Gson();
            AutoSMS[] autoSMSArray = gson.fromJson(jsonData.toString(), AutoSMS[].class); //Get Active AutoSMS Replys

            SmsManager smsManager = SmsManager.getDefault();

            // GET SIM CARDS

            ArrayList<String> simCardsFromPhone = new ArrayList<>();

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if (telephonyManager != null) {
                SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
                List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                if (subscriptionInfoList != null && !subscriptionInfoList.isEmpty()) {
                    for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                        String simCardNumber = subscriptionInfo.getNumber();
                        simCardsFromPhone.add(simCardNumber);
                    }
                }
            }

            Log.d("DADOS DO SIM CARDS", simCardsFromPhone.toString());

            // GET CONTACTS LIST

            ArrayList<String> contactsList = new ArrayList<>();

            // Define the projection (columns) you want to retrieve from the ContactsContract API
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

            // Query the contacts using the ContactsContract API
            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );

            // Iterate through the cursor to extract contact numbers
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(number);
                }
                cursor.close();
            }

            // GET CURRENT DAY OF THE WEEK

            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            switch (dayOfWeek) {
                case Calendar.MONDAY:
                    dayOfWeek = 0;
                    break;
                case Calendar.TUESDAY:
                    dayOfWeek = 1;
                    break;
                case Calendar.WEDNESDAY:
                    dayOfWeek = 2;
                    break;
                case Calendar.THURSDAY:
                    dayOfWeek = 3;
                    break;
                case Calendar.FRIDAY:
                    dayOfWeek = 4;
                    break;
                case Calendar.SATURDAY:
                    dayOfWeek = 5;
                    break;
                case Calendar.SUNDAY:
                    dayOfWeek = 6;
                    break;
            }

            // GET CURRENT TIME

            // Get the current time in the "hh:mm" format
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
            String currentTimeString = formatTime.format(new Date());

            // VALIDATION

            for (AutoSMS reply : autoSMSArray) {

                // Parse the start and end times to Date objects
                Date startTime = formatTime.parse(reply.getTimeFrom());
                Date endTime = formatTime.parse(reply.getTimeTo());

                // Parse the current time to a Date object
                Date currentTime = formatTime.parse(currentTimeString);
                assert currentTime != null;

                String message = reply.getMessage();

                if(reply.getSimCards().get(0).equals("allSimCards")){ //All SIM Cards
                    if(reply.getNumbers().get(0).equals("unknownNumbers")){ //Unknown Numbers
                        if(!contactsList.contains(phoneNumber)){ //se a lista de contactos nao tiver o numero, entao envia SMS
                            if(reply.getDays()[dayOfWeek].equals(true)){ //se dia atual estiver selecionado
                                if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                }
                            }
                        }
                    } else if(reply.getNumbers().get(0).equals("anyNumber")){ //Any Number
                        if(reply.getDays()[dayOfWeek].equals(true)) {
                            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                        }
                    } else { //Specific Contacts
                        if(reply.getDays()[dayOfWeek].equals(true)){ //se dia atual estiver selecionado
                            if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                            }
                        }
                    }
                } else { //Specific Cards
                    for (String card : reply.getSimCards()){
                        if (simCardsFromPhone.contains(card)){ //Card from Phone is same as current selected card from AutoSMS reply
                            if(reply.getNumbers().get(0).equals("unknownNumbers")){ //Unknown Numbers
                                if(!contactsList.contains(phoneNumber)){ //se a lista de contactos nao tiver o numero, entao envia SMS
                                    if(reply.getDays()[dayOfWeek].equals(true)) {
                                        if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                            break;
                                        }
                                    }
                                }
                            } else if(reply.getNumbers().get(0).equals("anyNumber")){ //Any Number
                                if(reply.getDays()[dayOfWeek].equals(true)) {
                                    if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                        break;
                                    }
                                }
                            } else { //Specific Contacts
                                if(reply.getDays()[dayOfWeek].equals(true)) {
                                    if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        // Start the service as a foreground service
        startForeground(1, createNotification()); // Pass a unique notification ID and the notification object

        return START_STICKY;
    }

    private Notification createNotification() {
        // Create a notification channel if targeting API level 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Reply Service is Active")
                .setSmallIcon(R.drawable.autosms_logo);

        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
