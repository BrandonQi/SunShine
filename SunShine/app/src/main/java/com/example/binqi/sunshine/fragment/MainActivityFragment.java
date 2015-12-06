package com.example.binqi.sunshine.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.binqi.sunshine.R;
import com.example.binqi.sunshine.activity.DetailActivity;
import com.example.binqi.sunshine.activity.SettingActivity;
import com.example.binqi.sunshine.web.GetWeatherTask;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public final String LOG_CAT = MainActivityFragment.class.getSimpleName();

    public static ArrayAdapter<String> mForecastAdapter;

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
                Intent detailIntent = new Intent(getActivity(),DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT,forecast);
                startActivity(detailIntent);
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
        switch(id){
            case R.id.refresh:
                ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    new GetWeatherTask().execute(getActivity());
                }
                return true;
            case R.id.setting:
                Intent settingIntent = new Intent(getActivity(),SettingActivity.class);
                startActivity(settingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
