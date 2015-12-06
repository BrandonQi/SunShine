package com.example.binqi.sunshine.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.binqi.sunshine.R;
import com.example.binqi.sunshine.activity.SettingActivity;
import com.example.binqi.sunshine.web.GetWeatherTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingActivityFragment extends PreferenceFragment {

    public SettingActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.OnSharedPreferenceChangeListener prefListenter = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("location")) {
                    ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if(networkInfo != null && networkInfo.isConnected()){
                        new GetWeatherTask().execute(getActivity());
                    }
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListenter);
    }
}
