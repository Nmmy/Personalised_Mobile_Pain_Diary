package com.example.personalisedmobilepaindiary.model;

public class HumPain {
    private float humidity;
    private String pain_level;
    private String date;

    public HumPain(float humidity, String pain_level, String date){
        this.humidity = humidity;
        this.pain_level = pain_level;
        this.date = date;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
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
