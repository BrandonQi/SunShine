package com.example.binqi.sunshine.activity;

import android.os.Bundle;
import android.app.Activity;

import com.example.binqi.sunshine.fragment.DetailActivityFragment;
import com.example.binqi.sunshine.R;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailActivityFragment())
                    .commit();
        }
    }
}
