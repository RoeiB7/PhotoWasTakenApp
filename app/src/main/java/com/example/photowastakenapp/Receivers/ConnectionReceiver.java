package com.example.photowastakenapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.photowastakenapp.Services.ImageService;


public class ConnectionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent newIntent = new Intent(context, ImageService.class);
            newIntent.setAction(ImageService.START_IMAGE_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(newIntent);


            } else {
                context.startService(newIntent);

            }
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

//
//
//        }

        }
    }
}
