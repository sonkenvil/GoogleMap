package com.something.kteam.googlemap.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Nguyen Hung Son on 4/18/2017.
 */

public class Routes {
    private Distance distance;
    private Duration duration;
    private ArrayList<LatLng> listPoint;

    public Routes(Distance distance, Duration duration, ArrayList<LatLng> listPoint) {
        this.distance = distance;
        this.duration = duration;
        this.listPoint = listPoint;
    }

    public Routes() {
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ArrayList<LatLng> getListPoint() {
        return listPoint;
    }

    public void setListPoint(ArrayList<LatLng> listPoint) {
        this.listPoint = listPoint;
    }
}
