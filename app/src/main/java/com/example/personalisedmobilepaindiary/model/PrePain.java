package com.example.personalisedmobilepaindiary.model;

public class PrePain {
    private float pressure;
    private String pain_level;
    private String date;

    public PrePain(float pressure, String pain_level, String date){
        this.pressure = pressure;
        this.pain_level = pain_level;
        this.date = date;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
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
