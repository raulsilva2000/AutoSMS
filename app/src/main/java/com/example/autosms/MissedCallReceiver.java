package com.example.autosms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MissedCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        Log.d("DADOS", "NUMERO LIGOU: " + phoneNumber);

        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            // Call not picked up, start the service to send SMS
            Intent serviceIntent = new Intent(context, MissedCallService.class);
            serviceIntent.putExtra("PHONE_NUMBER", phoneNumber);
            context.startService(serviceIntent);
        }
    }
}
