package com.example.myapplication.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class HourlyData {
    String condition;

    public HourlyData(JSONObject jObject) throws JSONException {
        condition = jObject.getString("CONDITION");
    }
}
