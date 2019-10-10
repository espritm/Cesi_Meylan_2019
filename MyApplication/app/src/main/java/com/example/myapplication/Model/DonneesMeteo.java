package com.example.myapplication.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.FormatStyle;
import java.util.ArrayList;

public class DonneesMeteo {
    String jsonString;

    CityInfo city_info;

    ArrayList<ForcastDay> lsDays;

    public String gJsonString(){
        return jsonString;
    }

    public void setJsonString(String jSon){
        jsonString = jSon;
    }

    public CityInfo getCityInfo(){
        return  city_info;
    }

    public ArrayList<ForcastDay> getJoursSemaines(){
        return lsDays;
    }

    public DonneesMeteo(){

    }

    public DonneesMeteo(String json) throws JSONException
    {
        lsDays = new ArrayList<ForcastDay>();

        //Boucle foreach
        /*for(ForcastDay d : lsDays){
            d.getDate();
        }*/

        //Pour parser un Json contenant un tableau d'objets : [{...}, {...}]
        /*JSONArray a = new JSONArray(json);
        for(int i = 0; i < a.length(); i++){
            JSONObject o = a.getJSONObject(i);
            new Error(o);
        }*/

        JSONObject jObject = new JSONObject(json);

        int i = 0;
        while (jObject.has("fcst_day_" + i)){
            ForcastDay day = new ForcastDay(jObject.getString("fcst_day_" + i));
            lsDays.add(day);
            i++;
        }
    }
}
