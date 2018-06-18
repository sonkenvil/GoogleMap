package com.something.kteam.googlemap.parse;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.something.kteam.googlemap.model.Atm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nguyen Hung Son on 4/16/2017.
 */

public class ParseJsonAtm {
    private RequestQueue requestQueue;
    private String url;
    private Context context;

    public ParseJsonAtm(String url, Context mcontext) {
        this.url = url;
        this.context = mcontext;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getATM(final CallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Atm> list = new ArrayList<>();
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Atm atm = new Atm();
                        JSONObject object = (JSONObject) results.get(i);
                        JSONObject geometry = object.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        atm.setLat(location.getDouble("lat"));
                        atm.setLng(location.getDouble("lng"));
                        atm.setName(object.getString("name"));
                        list.add(atm);
                    }
                    callBack.onSuccess(list);
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

    public interface CallBack {
        void onSuccess(ArrayList<Atm> listATM);
    }
}
