package com.something.kteam.googlemap.parse;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.something.kteam.googlemap.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nguyen Hung Son on 4/15/2017.
 */

public class ParseJsonWeather {
    private RequestQueue requestQueue;
    private Context context;
    private String URL;

    public ParseJsonWeather(Context mcontext, String url) {
        this.context = mcontext;
        this.URL = url;
        requestQueue = Volley.newRequestQueue(context);
    }
    public void getInfor(final Callback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Weather weather = new Weather();
                    JSONObject current_observation = response.getJSONObject("current_observation");
                    String time = current_observation.getString("observation_time_rfc822");
                    weather.setDay(time);

                    JSONObject display_location = current_observation.getJSONObject("display_location");
                    String full = display_location.getString("full");
                    weather.setCity(full);
                    String temp = current_observation.getString("temp_f");
                    weather.setTemp(temp);

                    weather.setDescription(current_observation.getString("weather"));
                    JSONObject observation_location = current_observation.getJSONObject("observation_location");
                    String city = observation_location.getString("city");
                    weather.setTp(city);
                    String temp_min = current_observation.getString("dewpoint_f");
                    weather.setLow(temp_min);

                    String temp_max = current_observation.getString("feelslike_f");
                    weather.setHigh(temp_max);

                    String url = current_observation.getString("icon_url");
                    weather.setImage(url);
                    callback.onSuccess(weather);
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

    public interface Callback{
        void onSuccess(Weather weather);
    }
}
