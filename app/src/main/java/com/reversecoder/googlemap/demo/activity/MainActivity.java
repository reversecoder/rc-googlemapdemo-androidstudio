package com.reversecoder.googlemap.demo.activity;

import android.location.Location;
import android.os.Bundle;

import com.reversecoder.googlemap.demo.R;

/**
 * @author Md. Rashadul Alam
 */
public class MainActivity extends BaseMapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onGoogleClientApiConnected() {
    }

    @Override
    public void onUserLocationChanged(Location location) {
    }
}


