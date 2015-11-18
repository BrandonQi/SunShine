package com.example.binqi.sunshine;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

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
    public class getWeatherTask extends AsyncTask<String, Void, Void> {
        public final String LOG_CAT = getWeatherTask.class.getSimpleName();
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = buildURL(params);
                downloadURL(url);
                //("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=3cd1fb7bb429a24860f198184c4332b9");
                return null;
            }catch (IOException e) {
                Log.e(LOG_CAT, "url failed!");
                return null;
            }
        }
        public URL buildURL(String... params) throws IOException {
            Log.d(LOG_CAT,"LENGTH IS "+ params.length);
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
        public Void downloadURL(URL url) throws IOException {
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int respenseCode = httpURLConnection.getResponseCode();
            Log.d(LOG_CAT,"response is " + respenseCode);
            String weather = readIT(httpURLConnection.getInputStream());
            return null;
        }
        public String readIT(InputStream it) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(it));
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }

}
