package com.example.binqi.sunshine;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public final String LOG_CAT = MainActivityFragment.class.getSimpleName();

    ArrayAdapter<String> mForecastAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] weatherData = {
                "Today-rainy-88/22",
                "Tomorrow-rainy-88/22",
                "Saturday-rainy-88/22",
                "Sunday-rainy-88/22",
                "Monday-rainy-88/22",
                "Tuesday-rainy-88/22",
                "Wednesday-rainy-88/22"};
        ArrayList<String> weekForcast = new ArrayList<String>(Arrays.asList(weatherData));
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forcast_textview,
                weekForcast
        );
        ListView listView = (ListView)rootView.findViewById(R.id.listview_forcast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
        menuInflater.inflate(R.menu.menu_mainfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id == R.id.refresh){
            ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                new getWeatherTask().execute("10025");
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class getWeatherTask extends AsyncTask<String, Void, String[]> {
        public final String LOG_CAT = getWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            try {
                URL url = buildURL(params);
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
                mForecastAdapter.clear();
                for (String res : result){
                    mForecastAdapter.add(res);
                }
            }
        }

        public URL buildURL(String... params) throws IOException {
            String format = "json";
            String units = "metric";
            String appid = "3cd1fb7bb429a24860f198184c4332b9";
            int numDays = 7;
            final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String Q_PARAM = "q";
            final String MODE_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID = "APPID";

            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(Q_PARAM,params[0])
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
            String[] result = new ParseWeatherJSON().parseJSON(weather);
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

    public class ParseWeatherJSON{
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

}
