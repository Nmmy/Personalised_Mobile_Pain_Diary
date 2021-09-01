package com.example.personalisedmobilepaindiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals("NOTIFICATION")){
            Toast.makeText(context, "It's 2 minutes before entering your today's pain record.",
                    Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().equals("UPLOAD")){

            //Schedule periodic work
            PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(UploadWorker.class, 1, TimeUnit.DAYS)
                            // Constraints
                            .build();
            //Submit the WorkRequest to the system
            WorkManager
                    .getInstance(context)
                    .enqueue(saveRequest);


        }

    }
}
