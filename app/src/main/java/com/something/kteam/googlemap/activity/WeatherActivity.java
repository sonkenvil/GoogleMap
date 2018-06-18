package com.something.kteam.googlemap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import com.something.kteam.googlemap.R;
import com.something.kteam.googlemap.adapter.CustomWeather;
import com.something.kteam.googlemap.model.WeatherDetail;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {
    TextView city, day, temp_weather, min_max, tp, sweather;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city = (TextView) findViewById(R.id.city);
        sweather = (TextView) findViewById(R.id.sweather);
        day = (TextView) findViewById(R.id.day);
        temp_weather = (TextView) findViewById(R.id.temp_weather);
        min_max = (TextView) findViewById(R.id.min_max);
        gridView = (GridView) findViewById(R.id.grid);
        tp = (TextView) findViewById(R.id.tp);
        xuly();
    }

    private void xuly() {

        Intent intent = getIntent();
        String city_weather = intent.getStringExtra("city");
        String day_weather = intent.getStringExtra("day");
        String tps = intent.getStringExtra("tp");
        String[] tach = day_weather.split(" ");
        String thu = null, thang = null;
        if (tach[0].equalsIgnoreCase("Tue,")) {
            thu = "T.3, " + tach[1];
        } else if (tach[0].equalsIgnoreCase("Mon,")) {
            thu = "T.2, " + tach[1];
        } else if (tach[0].equalsIgnoreCase("Wed,")) {
            thu = "T.4, " + tach[1];
        } else if (tach[0].equalsIgnoreCase("Thu,")) {
            thu = "T.5, " + tach[1];
        } else if (tach[0].equalsIgnoreCase("Fri,")) {
            thu = "T.6, " + tach[1];
        } else if (tach[0].equalsIgnoreCase("Sat,")) {
            thu = "T.7, " + tach[1];
        } else if (tach[0].equalsIgnoreCase("Sun,")) {
            thu = "CN, " + tach[1];
        }

        if (tach[2].equalsIgnoreCase("Jan")) {
            thang = " Th1";
        } else if (tach[2].equalsIgnoreCase("Feb")) {
            thang = " Th2";
        } else if (tach[2].equalsIgnoreCase("Mar")) {
            thang = " Th3";
        } else if (tach[2].equalsIgnoreCase("Apr")) {
            thang = " Th4";
        } else if (tach[2].equalsIgnoreCase("May")) {
            thang = " Th5";
        } else if (tach[2].equalsIgnoreCase("Jun")) {
            thang = " Th6";
        } else if (tach[2].equalsIgnoreCase("Jul")) {
            thang = " Th7";
        } else if (tach[2].equalsIgnoreCase("Aug")) {
            thang = " Th8";
        } else if (tach[2].equalsIgnoreCase("Sep")) {
            thang = " Th9";
        } else if (tach[2].equalsIgnoreCase("Oct")) {
            thang = " Th10";
        } else if (tach[2].equalsIgnoreCase("Nov")) {
            thang = " Th11";
        } else if (tach[2].equalsIgnoreCase("Dec")) {
            thang = " Th12";
        }

        String temp = intent.getStringExtra("temp");
        int tems = (int) ((Float.parseFloat(temp) - 32) / 1.8);
        if ((float) ((Float.parseFloat(temp) - 32) / 1.8) - (int) ((Float.parseFloat(temp) - 32) / 1.8) >= 0.5)
            tems += 1;

        String min = intent.getStringExtra("min");
        int low = (int) ((Float.parseFloat(min) - 32) / 1.8);
        if ((float) ((Float.parseFloat(min) - 32) / 1.8) - (int) ((Float.parseFloat(min) - 32) / 1.8) >= 0.5)
            low += 1;
        String max = intent.getStringExtra("max");
        int high = (int) ((Float.parseFloat(max) - 32) / 1.8);
        if ((float) ((Float.parseFloat(max) - 32) / 1.8) - (int) ((Float.parseFloat(max) - 32) / 1.8) >= 0.5)
            high += 1;
        city.setText(city_weather);
        day.setText(thu + thang);
        temp_weather.setText(tems + "°C");
        min_max.setText(high + "/" + low);
        String[] citycenter = tps.split(",");
        tp.setText(citycenter[0]);
        sweather.setText(intent.getStringExtra("weather"));
        ArrayList<WeatherDetail> list = intent.getParcelableArrayListExtra("list");
        CustomWeather customWeather = new CustomWeather(WeatherActivity.this, list);
        gridView.setAdapter(customWeather);
    }
}
