package com.example.autosms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MissedCallReceiver extends BroadcastReceiver {
    private List<SentMessage> messagesList = new ArrayList<>();
    private Context context;
    String phoneNumber;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (phoneNumber != null && TelephonyManager.EXTRA_STATE_IDLE.equals(state)) { //if missed call
            // Call not picked up, start the service to send SMS
            sendingProcess();
        }
    }

    //Aqui vamos ter percorrer cada resposta automatica do data.json e enviar sms se a situacao corresponder a uma determinada resposta automatica
    private void sendingProcess() {
        // Step 1: Read JSON data from the file
        FileInputStream inputStream = null;
        try {
            inputStream = context.openFileInput("data.json");
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

            // GET CONTACTS LIST
            ArrayList<String> contactsList = new ArrayList<>();

            // Define the projection (columns) you want to retrieve from the ContactsContract API
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

            // Query the contacts using the ContactsContract API
            Cursor cursor = context.getContentResolver().query(
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

            Date startTime = null;
            Date endTime = null;
            Date currentTime = null;

            boolean allday = false; // represents option All day (24hours) is selected

            // VALIDATION

            for (AutoSMS reply : autoSMSArray) {  //ir a cada resposta automatica e ver se responde a chamada perdida
                if (!reply.getTimeFrom().equals("24hours")) { //if time is not equal to 24hours then go get timeFrom and timeTo
                    startTime = formatTime.parse(reply.getTimeFrom());
                    endTime = formatTime.parse(reply.getTimeTo());
                    currentTime = formatTime.parse(currentTimeString);
                } else {
                    allday = true;
                }

                String message = reply.getMessage(); // message to send

                // CODE TO SEE IF CURRENT REPLY WILL RESPOND TO THIS MISSED CALL

                if ((reply.getNumbers().get(0).equals("unknownNumbers") && !contactsList.contains(phoneNumber))
                        || reply.getNumbers().get(0).equals("anyNumber")
                        || reply.getNumbers().contains(phoneNumber)) {

                    if (reply.getDays()[dayOfWeek] && (allday || (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime))) {
                        sendMessage(reply.getTitle(), phoneNumber, message, reply.getSimCard());
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String replyTitle, String phoneNumber, String messageToSend, String simSlotIndex) {
        //Register the sent message to messages.json
        try {
            // Get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();

            //Use the vendor-specific API to set the SIM card index
            Method method = SmsManager.class.getMethod("getSmsManagerForSubscriptionId", int.class);
            smsManager = (SmsManager) method.invoke(smsManager, Integer.parseInt(simSlotIndex));

            assert smsManager != null;
            // Send the SMS
            smsManager.sendTextMessage(phoneNumber, null, messageToSend, null, null);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentTime = dateFormat.format(new Date());

            // Add the new message to messagesList
            messagesList.add(new SentMessage(replyTitle, currentTime, phoneNumber, messageToSend));

            // Read JSON data from the file
            FileInputStream inputStream = context.openFileInput("messages.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            reader.close();

            // Parse JSON data into objects of SentMessage class and add previous messages to messagesList
            Gson gson = new Gson();
            SentMessage[] messagesArray = gson.fromJson(jsonData.toString(), SentMessage[].class);
            for (SentMessage message : messagesArray) {
                messagesList.add(new SentMessage(message.getTitle(), message.getTime(), message.getNumber(), message.getMessage()));
            }

            // Convert the updated data structure to JSON
            String updatedJson = gson.toJson(messagesList);

            // Write the updated JSON to the file
            FileOutputStream fos = context.openFileOutput("messages.json", context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(updatedJson);
            osw.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

