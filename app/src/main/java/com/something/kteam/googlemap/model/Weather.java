package com.something.kteam.googlemap.model;

/**
 * Created by Nguyen Hung Son on 4/15/2017.
 */

public class Weather {
    private String temp;
    private String day;
    private String high;
    private String low;
    private String city;
    private String image;
    private String tp;
    private String description;
    public Weather() {
    }


    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTp() {
        return tp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String weather) {
        this.description = weather;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }
}
