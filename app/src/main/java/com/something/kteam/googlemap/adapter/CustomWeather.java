package com.something.kteam.googlemap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.something.kteam.googlemap.R;
import com.something.kteam.googlemap.model.WeatherDetail;

import java.util.ArrayList;

/**
 * Created by Nguyen Hung Son on 4/15/2017.
 */

public class CustomWeather extends BaseAdapter {
    private Context context;
    private ArrayList<WeatherDetail> list;
    public CustomWeather(Context context, ArrayList<WeatherDetail> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = convertView;

        if(root == null){
            root = inflater.inflate(R.layout.custom_weather,parent,false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.dayWeather = (TextView) root.findViewById(R.id.dayweather);
            viewHolder.day_monthweather = (TextView)root.findViewById(R.id.day_monthweather);
            viewHolder.temp_max_weather = (TextView) root.findViewById(R.id.temp_max_weather);
            viewHolder.temp_min_weather = (TextView) root.findViewById(R.id.temp_min_weather);
            viewHolder.imageweather = (ImageView) root.findViewById(R.id.imageweather);
            root.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) root.getTag();
        viewHolder.dayWeather.setText(list.get(position).getWeekday());
        viewHolder.day_monthweather.setText(list.get(position).getDay()+"/"+list.get(position).getMonth());
        viewHolder.temp_min_weather.setText(list.get(position).getLow());
        viewHolder.temp_max_weather.setText(list.get(position).getHight());
        Glide.with(context).load(list.get(position).getImageSun()).into(viewHolder.imageweather);
        return root;
    }

    public static class ViewHolder{
        TextView dayWeather,day_monthweather,temp_max_weather,temp_min_weather;
        ImageView imageweather;
    }
}
