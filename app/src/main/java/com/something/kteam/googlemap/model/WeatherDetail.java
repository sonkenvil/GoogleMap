package com.something.kteam.googlemap.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nguyen Hung Son on 4/15/2017.
 */

public class WeatherDetail implements Parcelable {
    private String weekday;
    private String day;
    private String month;
    private String imageSun;
    private String hight;
    private String low;

    public WeatherDetail() {
    }


    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getImageSun() {
        return imageSun;
    }

    public void setImageSun(String imageSun) {
        this.imageSun = imageSun;
    }

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    public WeatherDetail(Parcel parcel) {
        weekday = parcel.readString();
        day = parcel.readString();
        month = parcel.readString();
        imageSun = parcel.readString();
        hight = parcel.readString();
        low = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weekday);
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(imageSun);
        dest.writeString(hight);
        dest.writeString(low);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        @Override
        public WeatherDetail createFromParcel(Parcel parcel) {
            return new WeatherDetail(parcel);
        }

        public WeatherDetail[] newArray(int size) {
            return new WeatherDetail[size];
        }
    };
}
