package com.reversecoder.googlemap.demo.activity;

import android.location.Location;
import android.os.Bundle;

import com.reversecoder.googlemap.demo.R;

public class MainActivity extends BaseMapActivity {
//    private static final int REQUEST_CHECK_SETTINGS = 0x1;
//    private static GoogleApiClient mGoogleApiClient;
//    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
//    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
//    private TextView gps_status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        gps_status = (TextView) findViewById(R.id.gps_status);
//        initGoogleAPIClient();//Init Google API Client
//        checkPermissions();//Check Permission
    }
//
//    /* Initiate Google API Client  */
//    private void initGoogleAPIClient() {
//        //Without Google API Client Auto Location Dialog will not work
//        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//    /* Check Location Permission for Marshmallow Devices */
//    private void checkPermissions() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(MainActivity.this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED)
//                requestLocationPermission();
//            else
//                showSettingDialog();
//        } else
//            showSettingDialog();
//
//    }
//
//    /*  Show Popup to access User Permission  */
//    private void requestLocationPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    ACCESS_FINE_LOCATION_INTENT_ID);
//
//        } else {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    ACCESS_FINE_LOCATION_INTENT_ID);
//        }
//    }
//
//    /* Show Location Access Dialog */
//    private void showSettingDialog() {
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
//        locationRequest.setInterval(30 * 1000);
//        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can initialize location
//                        // requests here.
//                        updateGPSStatus("GPS is Enabled in your device");
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            e.printStackTrace();
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        Log.e("Settings", "Result OK");
//                        updateGPSStatus("GPS is Enabled in your device");
//                        //startLocationUpdates();
//                        break;
//                    case RESULT_CANCELED:
//                        Log.e("Settings", "Result Cancel");
//                        updateGPSStatus("GPS is Disabled in your device");
//                        break;
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //Unregister receiver on destroy
//        if (gpsLocationReceiver != null)
//            unregisterReceiver(gpsLocationReceiver);
//    }
//
//    //Run on UI
//    private Runnable sendUpdatesToUI = new Runnable() {
//        public void run() {
//            showSettingDialog();
//        }
//    };
//
//    /* Broadcast receiver to check status of GPS */
//    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            //If Action is Location
//            if (intent.getAction().matches(BROADCAST_ACTION)) {
//                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//                //Check if GPS is turned ON or OFF
//                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    Log.e("About GPS", "GPS is Enabled in your device");
//                    updateGPSStatus("GPS is Enabled in your device");
//                } else {
//                    //If GPS turned OFF show Location Dialog
//                    new Handler().postDelayed(sendUpdatesToUI, 10);
//                    // showSettingDialog();
//                    updateGPSStatus("GPS is Disabled in your device");
//                    Log.e("About GPS", "GPS is Disabled in your device");
//                }
//
//            }
//        }
//    };
//
//    //Method to update GPS status text
//    private void updateGPSStatus(String status) {
//        gps_status.setText(status);
//    }
//
//
//    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case ACCESS_FINE_LOCATION_INTENT_ID: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    //If permission granted show location dialog if APIClient is not null
//                    if (mGoogleApiClient == null) {
//                        initGoogleAPIClient();
//                        showSettingDialog();
//                    } else
//                        showSettingDialog();
//
//
//                } else {
//                    updateGPSStatus("Location Permission denied.");
//                    Toast.makeText(MainActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//        }
//    }

    @Override
    public void onGoogleClientApiConnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
    }
}


