package com.example.binqi.sunshine;

import android.app.Fragment;
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
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
        menuInflater.inflate(R.menu.menu_mainfragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id == R.id.refresh){
            new getWeatherTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
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

    public class getWeatherTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                downloadURL("http://api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=3cd1fb7bb429a24860f198184c4332b9");
                return null;
            }catch (IOException e) {
                Log.e(LOG_CAT, "url failed!");
                return null;
            }
        }
        public Void downloadURL(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int respenseCode = httpURLConnection.getResponseCode();
            Log.d(LOG_CAT,"response is" + respenseCode);
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
