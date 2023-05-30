package com.example.autosms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MissedCallService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        String message = "Call not picked up. Please call back."; // Replace with your desired message

        Log.d("DADOS SERVICE", "NUMERO ENVIAR: "+phoneNumber);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

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
