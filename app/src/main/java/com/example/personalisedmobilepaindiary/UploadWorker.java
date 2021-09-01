package com.example.personalisedmobilepaindiary;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.personalisedmobilepaindiary.painecordviewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadWorker extends Worker {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private PainRecordViewModel painRecordViewModel;

    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {

        // Do the work here
        uploadFirebase();

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void uploadFirebase(){
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance((Application) getApplicationContext())
                .create(PainRecordViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("PainRecord");
        painRecordViewModel.selectAll(email).thenApply(v->{
            Log.i("DataSavedToFirebase: ", v.toString());
            databaseReference.setValue(v);
            return v;
        });
    }
}
