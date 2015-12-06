package com.example.binqi.sunshine.web;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.binqi.sunshine.R;
import com.example.binqi.sunshine.fragment.MainActivityFragment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by binqi on 12/5/15.
 */
public class GetWeatherTask extends AsyncTask<Context, Void, String[]> {
    private final String LOG_CAT = GetWeatherTask.class.getSimpleName();
    private Context context;
    @Override
    protected String[] doInBackground(Context... params) {
        try {
            this.context = params[0];
            URL url = buildURL();
            String[] result = downloadURL(url);
            return result;
            //("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=3cd1fb7bb429a24860f198184c4332b9");
        }catch (IOException e) {
            Log.e(LOG_CAT, "url failed!");
            return new String[0];
        } catch (JSONException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null){
            MainActivityFragment.mForecastAdapter.clear();
            for (String res : result){
                MainActivityFragment.mForecastAdapter.add(res);
            }
        }
    }

    public URL buildURL() throws IOException {
        String format = "json";
        String appid = "3cd1fb7bb429a24860f198184c4332b9";
        int numDays = 7;
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String Q_PARAM = "q";
        final String MODE_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID = "APPID";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String zipcode = sharedPreferences.getString("location", String.valueOf(R.string.pref_location_default));
        String units = sharedPreferences.getString("temperUnit", String.valueOf(R.string.pref_temperUnit_default));
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(Q_PARAM,zipcode)
                .appendQueryParameter(MODE_PARAM,format)
                .appendQueryParameter(UNITS_PARAM,units)
                .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                .appendQueryParameter(APPID,appid)
                .build();
        URL url = new URL(buildUri.toString());
        Log.d(LOG_CAT,url.toString());
        return url;
    }

    public String[] downloadURL(URL url) throws IOException, JSONException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        int respenseCode = httpURLConnection.getResponseCode();
        Log.d(LOG_CAT,"response is " + respenseCode);
        String weather = readIT(httpURLConnection.getInputStream());
        String[] result = new ParseWeatherJson().parseJSON(weather);
        for(int i = 0;i < result.length;i++){
            Log.d(LOG_CAT,result[i]);
        }
        return result;
    }

    public String readIT(InputStream it) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(it));
        String line;
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }

}
