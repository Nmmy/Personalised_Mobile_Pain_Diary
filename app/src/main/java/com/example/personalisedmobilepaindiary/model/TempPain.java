package com.example.personalisedmobilepaindiary.model;

import java.util.Date;

public class TempPain {
    private float temp;
    private String pain_level;
    private String date;

    public TempPain(float temp, String pain_level, String date){
        this.temp = temp;
        this.pain_level = pain_level;
        this.date = date;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public String getPainLevel() {
        return pain_level;
    }

    public void setPainLevel(String pain_level) {
        this.pain_level = pain_level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
