package com.example.personalisedmobilepaindiary.model;

public class PainCount {
    private String pain_location;
    private int count;

    public PainCount(String pain_location, int count){
        this.pain_location = pain_location;
        this.count = count;
    }

    public String getPain_location() {
        return pain_location;
    }

    public void setPain_location(String pain_location) {
        this.pain_location = pain_location;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
