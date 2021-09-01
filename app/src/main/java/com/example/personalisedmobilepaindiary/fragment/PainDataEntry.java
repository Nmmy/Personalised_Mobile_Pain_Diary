package com.example.personalisedmobilepaindiary.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalisedmobilepaindiary.AlarmReceiver;
import com.example.personalisedmobilepaindiary.R;
import com.example.personalisedmobilepaindiary.Retrofit.RetrofitInterface;
import com.example.personalisedmobilepaindiary.databinding.PainDataEntryBinding;
import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.model.WeatherResponse;
import com.example.personalisedmobilepaindiary.painecordviewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.hsalf.smileyrating.SmileyRating;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PainDataEntry extends Fragment {
    private PainDataEntryBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private SmileyRating moodSelection;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private TimePicker timePicker;
    private FirebaseAuth mAuth;
    private int clicks;

    private float temp;
    private float humidity;
    private float pressure;

    public PainDataEntry(){}



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        binding = PainDataEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        /*timePicker = binding.timePicker;*/
        mAuth = FirebaseAuth.getInstance();


        showDialog();

        //Spinner
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                    String[] scale = getResources().getStringArray(R.array.numeric_rating_scale);

                Toast.makeText(getContext(), "You select :" + scale[pos], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "You must select one item", Toast.LENGTH_LONG).show();
            }
        });

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] loc = getResources().getStringArray(R.array.pain_location);

                Toast.makeText(getContext(), "You select :" + loc[pos], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "You must select one item", Toast.LENGTH_LONG).show();
            }
        });

        //stying smileyRating
        moodSelection = binding.mood;
        moodSelection.setTitle(SmileyRating.Type.TERRIBLE, "Very Low");
        moodSelection.setTitle(SmileyRating.Type.BAD, "Low");
        moodSelection.setTitle(SmileyRating.Type.OKAY, "Average");
        moodSelection.setTitle(SmileyRating.Type.GOOD, "Good");
        moodSelection.setTitle(SmileyRating.Type.GREAT, "Very Good");

        String email = mAuth.getCurrentUser().getEmail();

        //initiate view model
        painRecordViewModel =
                new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                String allPainRecords = "";

                for (PainRecord temp : painRecords) {
                    if(email.equalsIgnoreCase(temp.email)){
                        String painRecordDetails = (temp.pid + " " + temp.painLevel+ " " +
                                temp.painLocation + " " + temp.moodLevel + " " + temp.steps + " " +
                                temp.date);
                        allPainRecords = allPainRecords +
                                System.getProperty("line.separator") + painRecordDetails;
                    }
                }
                binding.textViewRead.setText("All pain record: " + allPainRecords);
            }
        });


        clicks = 0;
        //Save button (addButton) listener
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //set one save per day
                clicks++;

                if (clicks >=1){
                    binding.addButton.setEnabled(false);
                }
                //disable edit after click save button
                binding.spinner1.setEnabled(false);
                binding.spinner2.setEnabled(false);
                moodSelection.disallowSelection(true);
                binding.editTextSteps.setEnabled(false);


                //get email
                String email = mAuth.getCurrentUser().getEmail();
                //get date
                Date date = new Date();
                //get weather
                //weather APi
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://api.openweathermap.org/data/2.5/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                //call weather
                Call<WeatherResponse> weatherResponseCall = retrofitInterface.getWeather();

                weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        WeatherResponse weatherResponse = response.body();
                        temp = weatherResponse.getMain().temp;
                        humidity = weatherResponse.getMain().humidity;
                        pressure = weatherResponse.getMain().pressure;

                        //get user input
                        String painLevel = binding.spinner1.getSelectedItem().toString();
                        String painLocation = binding.spinner2.getSelectedItem().toString();
                        String steps = binding.editTextSteps.getText().toString();
                        String smiley = binding.mood.getSelectedSmiley().name();
                        String moodLevel = "";
                        //correspond smiley name to title
                        switch (smiley){
                            case "TERRIBLE":
                                moodLevel = "Very Low";
                                break;
                            case "BAD":
                                moodLevel = "Low";
                                break;
                            case "OKAY":
                                moodLevel = "Average";
                                break;
                            case "GOOD":
                                moodLevel = "Good";
                                break;
                            case "GREAT":
                                moodLevel = "Very Good";
                                break;
                        }

                       /* String painLevel = "6";
                        String painLocation = "back";
                        String moodLevel = "Very Low";
                        String steps = "2000/10000";
                        String date = "Fri May 14 10:30:40 GMT+10:00 2021";
                        String email = mAuth.getCurrentUser().getEmail();
                        temp = (float) 13.00;
                        humidity = 102;
                        pressure = 1006;*/
                        if ((!painLevel.isEmpty() && painLevel != null) && (!painLocation.isEmpty() &&
                                painLocation != null) && (!moodLevel.isEmpty() && moodLevel != null) &&
                                (!steps.isEmpty() && steps != null)) {
                            PainRecord painRecord = new PainRecord(painLevel, painLocation, moodLevel,
                                    steps, date.toString(), email, temp, humidity, pressure);
                            painRecordViewModel.insert(painRecord);

                            Toast.makeText(getContext(), "Save successfully", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Mood level is required!", Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    }
                });


            }});



        //edit button (update button)
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //enable edit after click save button
                binding.spinner1.setEnabled(true);
                binding.spinner2.setEnabled(true);
                moodSelection.disallowSelection(false);
                binding.editTextSteps.setEnabled(true);

                String strId
                        =binding.idTextField.getText().toString();
                int id=0;
                if (!strId.isEmpty() && strId!= null)
                    id=Integer.parseInt(strId);

                //get date
                Date date = new Date();
                //get user input
                String painLevel= binding.spinner1.getSelectedItem().toString();
                String painLocation=binding.spinner2.getSelectedItem().toString();
                String steps = binding.editTextSteps.getText().toString();
                String smiley = binding.mood.getSelectedSmiley().name();
                String moodLevel = "";
                //correspond smiley name to title
                switch (smiley){
                    case "TERRIBLE":
                        moodLevel = "Very Low";
                        break;
                    case "BAD":
                        moodLevel = "Low";
                        break;
                    case "OKAY":
                        moodLevel = "Average";
                        break;
                    case "GOOD":
                        moodLevel = "Good";
                        break;
                    case "GREAT":
                        moodLevel = "Very Good";
                        break;
                }


                if ((!painLevel.isEmpty() && painLevel != null) && (!painLocation.isEmpty() &&
                        painLocation != null) && (!moodLevel.isEmpty() && steps != null) &&
                        (!moodLevel.isEmpty() && steps != null)) {
                    //this deals with versioning issues
                    if (android.os.Build.VERSION.SDK_INT >=
                            android.os.Build.VERSION_CODES.N) {
                        CompletableFuture<PainRecord> painRecordCompletableFuture =
                                painRecordViewModel.findByIDFuture(id);
                        String finalMoodLevel = moodLevel;
                        painRecordCompletableFuture.thenApply(painRecord -> {
                        if (painRecord != null) {
                            painRecord.painLevel = painLevel;
                            painRecord.painLocation = painLocation;
                            painRecord.moodLevel = finalMoodLevel;
                            painRecord.steps = steps;
                            painRecord.date = date.toString();
                            painRecordViewModel.update(painRecord);
                            Toast.makeText(getContext(), "Save successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "ID Does Not Exist", Toast.LENGTH_LONG).show();
                        }
                        return painRecord;
                        });
                    }
                }
                else{
                    Toast.makeText(getContext(), "Mood level is required!", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    //ask user to select time
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please select notification time");
        timePicker = new TimePicker(getActivity());
        builder.setView(timePicker);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAlarm();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //set up alarm
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(){
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.setAction("NOTIFICATION");
        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);


        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        //transform time from hour and minutes to alarmLongTime
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute-2);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
