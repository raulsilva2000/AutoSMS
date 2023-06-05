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
        if (phoneNumber != null && TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            // Call not picked up, start the service to send SMS
            sendingProcess();
            //sendSmsWithSimCard(phoneNumber, "ola", 0);
        }
    }

    //Aqui vamos ter percorrer cada resposta automática do data.json e enviar sms se a situacao corresponder a uma determinada resposta automatica
    private void sendingProcess(){
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

            SmsManager smsManager = SmsManager.getDefault();

            // GET SIM CARDS

            ArrayList<String> simCardsFromPhone = new ArrayList<>();

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (telephonyManager != null) {
                SubscriptionManager subscriptionManager = SubscriptionManager.from(context.getApplicationContext());
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
                                    sendMessage(smsManager, reply.getTitle(), phoneNumber, message);
                                }
                            }
                        }
                    } else if(reply.getNumbers().get(0).equals("anyNumber")){ //Any Number
                        if(reply.getDays()[dayOfWeek].equals(true)) {
                            sendMessage(smsManager, reply.getTitle(), phoneNumber, message);
                        }
                    } else { //Specific Contacts
                        if(reply.getDays()[dayOfWeek].equals(true)){ //se dia atual estiver selecionado
                            if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                sendMessage(smsManager, reply.getTitle(), phoneNumber, message);
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
                                            sendMessage(smsManager, reply.getTitle(), phoneNumber, message);
                                            break;
                                        }
                                    }
                                }
                            } else if(reply.getNumbers().get(0).equals("anyNumber")){ //Any Number
                                if(reply.getDays()[dayOfWeek].equals(true)) {
                                    if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                        sendMessage(smsManager, reply.getTitle(), phoneNumber, message);
                                        break;
                                    }
                                }
                            } else { //Specific Contacts
                                if(reply.getDays()[dayOfWeek].equals(true)) {
                                    if( (currentTime.after(startTime) && currentTime.before(endTime)) || currentTime.equals(startTime) || currentTime.equals(endTime) ){
                                        sendMessage(smsManager, reply.getTitle(), phoneNumber, message);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(SmsManager smsManager, String replyTitle, String phoneNumber, String messageToSend){
        //Register the sent message to messages.json
        try {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendSmsWithSimCard(String phoneNumber, String message, int simSlotIndex) {
        try {
            // Get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();

            //Use the vendor-specific API to set the SIM card index
            Method method = SmsManager.class.getMethod("getSmsManagerForSubscriptionId", int.class);
            smsManager = (SmsManager) method.invoke(smsManager, simSlotIndex);

            // Send the SMS
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

