package com.example.personalisedmobilepaindiary.entity;


import android.content.Intent;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class PainRecord {
    @PrimaryKey(autoGenerate = true)
    public int pid;

    @ColumnInfo(name = "pain_level")
    @NonNull
    public String painLevel;
    @ColumnInfo(name = "pain_location")
    @NonNull
    public String painLocation;
    @ColumnInfo(name = "mood_level")
    @NonNull
    public String moodLevel;
    @ColumnInfo(name = "steps_today/goal")
    @NonNull
    public String steps;

    public String date;
    @ColumnInfo(name = "email")
    @NonNull
    public String email;

    public float temp;
    public float humidity;
    public float pressure;


    public PainRecord( @NonNull String painLevel, @NonNull String painLocation, String moodLevel,
                       String steps, String date, String email, float temp, float humidity, float pressure) {
        this.painLevel=painLevel;
        this.painLocation=painLocation;
        this.moodLevel = moodLevel;
        this.steps = steps;
        this.date = date;
        this.email = email;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}
