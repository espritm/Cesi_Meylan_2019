package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ForcastDay {

    String date;

    String day_long;

    int tmin;

    int tmax;

    String condition;

    public String getDate() {
        return date;
    }

    public String getDay_long() {
        return day_long;
    }

    public int getTmin() {
        return tmin;
    }

    public int getTmax() {
        return tmax;
    }

    public String getCondition() {
        return condition;
    }

    public String getIcon() {
        return icon;
    }

    String icon;

    ArrayList<HourlyData> lsHours;

    String Json;

    public String getJson() {
        return Json;
    }

    public ForcastDay(String json) throws JSONException {
        JSONObject jObject = new JSONObject(json);

        Json = json;

        date = jObject.getString("date");
        day_long = jObject.getString("day_long");
        tmin = jObject.getInt("tmin");
        tmax = jObject.getInt("tmax");
        condition = jObject.getString("condition");
        icon = jObject.getString("icon");

        JSONObject hourly_data = new JSONObject(jObject.getString("hourly_data"));

        lsHours = new ArrayList<HourlyData>();
        for (int i = 0; i < 24; i++){
            HourlyData hour = new HourlyData(hourly_data.getJSONObject(i + "H00"));
            lsHours.add(hour);
        }
    }
}
