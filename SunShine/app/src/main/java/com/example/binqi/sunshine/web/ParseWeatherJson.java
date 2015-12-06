package com.example.binqi.sunshine.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by binqi on 12/5/15.
 */
public class ParseWeatherJson{
    public String[] parseJSON(String weather) throws JSONException {
        final String ON_CNT = "cnt";
        final String ON_LIST = "list";
        final String ON_TEMP = "temp";
        final String ON_MAX = "max";
        final String ON_MIN = "min";
        final String ON_WEATHER = "weather";
        final String ON_MAIN = "main";

        double min;
        double max;
        String day;
        String maxMin;
        String main;
        JSONObject jsonObject = new JSONObject(weather);
        int count = jsonObject.getInt(ON_CNT);
        String[] result = new String[count];
        JSONArray jsonArray = jsonObject.getJSONArray(ON_LIST);
        for(int i = 0;i < count;i++){
            JSONObject jsonObjectDay = jsonArray.getJSONObject(i);
            JSONObject jsonObjectTemp = jsonObjectDay.getJSONObject(ON_TEMP);
            JSONArray jsonArrayWeather = jsonObjectDay.getJSONArray(ON_WEATHER);
            min = jsonObjectTemp.getDouble(ON_MIN);
            max = jsonObjectTemp.getDouble(ON_MAX);
            maxMin = buildMaxMin(max, min);
            main = jsonArrayWeather.getJSONObject(0).getString(ON_MAIN);
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(GregorianCalendar.DATE,i);
            Date time = gc.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd");
            day = simpleDateFormat.format(time);
            StringBuilder resultString = new StringBuilder();
            resultString.append(day + "-");
            resultString.append(main + "-");
            resultString.append(maxMin);
            result[i] = resultString.toString();
        }
        return result;
    }
    public String buildMaxMin(double max,double min){
        return Math.round(max) + "/" + Math.round(min);
    }
}