package com.something.kteam.googlemap.parse;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.something.kteam.googlemap.model.WeatherDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nguyen Hung Son on 4/15/2017.
 */

public class ParseJsonWeatherDetail {
    private RequestQueue requestQueue;
    private Context context;
    private String URL;

    public ParseJsonWeatherDetail(Context mcontext, String URL) {
        this.context = mcontext;
        this.URL = URL;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getInfor(final CallBack callBack){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<WeatherDetail> weatherDetails = new ArrayList<>();
                try {
                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject simpleforecast = forecast.getJSONObject("simpleforecast");
                    JSONArray forecastdays = simpleforecast.getJSONArray("forecastday");
                    for (int i = 0; i < forecastdays.length(); i++) {
                        WeatherDetail weather = new WeatherDetail();
                        JSONObject objs = (JSONObject) forecastdays.get(i);
                        JSONObject date = objs.getJSONObject("date");

                        String weekday = date.getString("weekday");

                        if (weekday.equalsIgnoreCase("Tuesday")) {
                            weather.setWeekday("T.3");
                        } else if (weekday.equalsIgnoreCase("Monday")) {
                            weather.setWeekday("T.2");
                        } else if (weekday.equalsIgnoreCase("Wednesday")) {
                            weather.setWeekday("T.4");
                        } else if (weekday.equalsIgnoreCase("Thursday")) {
                            weather.setWeekday("T.5");
                        } else if (weekday.equalsIgnoreCase("Friday")) {
                            weather.setWeekday("T.6");
                        } else if (weekday.equalsIgnoreCase("Saturday")) {
                            weather.setWeekday("T.7");
                        } else if (weekday.equalsIgnoreCase("Sunday")) {
                            weather.setWeekday("CN");
                        }

                        String day = date.getString("day");
                        weather.setDay(day);

                        String month = date.getString("month");
                        weather.setMonth(month);

                        JSONObject high = objs.getJSONObject("high");
                        String celsius = high.getString("celsius");
                        weather.setHight(celsius);

                        JSONObject low = objs.getJSONObject("low");
                        String celsiusl = low.getString("celsius");
                        weather.setLow(celsiusl);
                        String icon_url = objs.getString("icon_url");
                        weather.setImageSun(icon_url);
                        weatherDetails.add(weather);
                    }
                    callBack.onSuccess(weatherDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public interface CallBack{
        void onSuccess(ArrayList<WeatherDetail> listWeather);
    }
}
