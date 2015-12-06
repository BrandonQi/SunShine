package com.example.binqi.sunshine.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.binqi.sunshine.R;
import com.example.binqi.sunshine.activity.SettingActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent mainIntent = getActivity().getIntent();
        if(mainIntent != null && mainIntent.hasExtra(Intent.EXTRA_TEXT)){
            String forecastString = mainIntent.getStringExtra(Intent.EXTRA_TEXT);
            ( (TextView)rootView.findViewById(R.id.detailTextView)).setText(forecastString);
        }
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
            case R.id.setting:
                Intent settingIntent = new Intent(getActivity(),SettingActivity.class);
                startActivity(settingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
