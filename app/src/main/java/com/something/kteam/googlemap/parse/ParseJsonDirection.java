package com.something.kteam.googlemap.parse;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.something.kteam.googlemap.model.Distance;
import com.something.kteam.googlemap.model.Duration;
import com.something.kteam.googlemap.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nguyen Hung Son on 4/18/2017.
 */

public class ParseJsonDirection {
    private Context context;
    private String url;
    private RequestQueue requestQueue;

    public ParseJsonDirection(String URL, Context mcContext) {
        this.url = URL;
        this.context = mcContext;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getDirection(final Callback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Routes routesObj = new Routes();
                    Distance distanceObj = new Distance();
                    Duration durationObj = new Duration();
                    boolean bl = false;
                    JSONArray routes = (JSONArray) response.get("routes");
                    if (routes.length() > 0) {
                        bl = true;
                        JSONObject jsonObject = (JSONObject) routes.get(0);
                        JSONArray legs = jsonObject.getJSONArray("legs");
                        JSONObject obj = (JSONObject) legs.get(0);
                        JSONObject distance = obj.getJSONObject("distance");

                        distanceObj.setText(distance.getString("text"));
                        distanceObj.setValue(distance.getString("value"));
                        routesObj.setDistance(distanceObj);

                        JSONObject duration = obj.getJSONObject("duration");
                        durationObj.setText(duration.getString("text"));
                        durationObj.setValue(duration.getString("value"));
                        routesObj.setDuration(durationObj);

                        JSONObject overview_polyline = jsonObject.getJSONObject("overview_polyline");
                        String points = overview_polyline.getString("points");
                        routesObj.setListPoint(decodePolyLine(points));
                    } else bl = false;
                    callback.onSuccess(routesObj, bl);

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

    public interface Callback {
        void onSuccess(Routes routes, boolean bl);
    }

    private ArrayList<LatLng> decodePolyLine(final String poly) {
        Log.d("1213",""+poly);
        int len = poly.length();
        int index = 0;
        ArrayList<LatLng> decoded = new ArrayList<>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
