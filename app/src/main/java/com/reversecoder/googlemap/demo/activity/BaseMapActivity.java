package com.reversecoder.googlemap.demo.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.reversecoder.googlemap.demo.util.ReceiverManager;

public abstract class BaseMapActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>,
        PlaceSelectionListener {

    public static final String TAG = BaseMapActivity.class.getSimpleName();
    private static final int SETTINGS_LOCATION = 0x1;
    private GoogleApiClient mGoogleApiClient; // The google services connection.
    private LocationRequest mLocationRequest; // Periodic location request object.
    private static final int PERMISSIONS_REQUEST_LOCATION = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    private boolean isRequestedForPlaceSearch = false;
    private static final int INTENT_PLACE_SEARCH = 1; // For places search
    ReceiverManager receiverManager;

    //abstract methods
    public abstract void onGoogleClientApiConnected();

    public abstract void onSearchedPlaceItemSelected(Place place);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if Google Play Services Available or not
        if (!checkGooglePlayServices()) {
            Log.d("onCreate", "Google Play Services are not available");
            Toast.makeText(BaseMapActivity.this, "Please install google play service.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        receiverManager = ReceiverManager.init(BaseMapActivity.this);
    }

    @Override
    public void onStart() {
        //Init Google API Client
        initGoogleAPIClient();
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isRequestedForPlaceSearch) {
            initGoogleAPIClient();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!isRequestedForPlaceSearch) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onStop() {
        //Unregister receiver on stop
        if (receiverManager.isReceiverRegistered(gpsLocationReceiver)) {
            receiverManager.unregisterReceiver(gpsLocationReceiver);
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onStop();

    }

    /**
     * Check if the user allows Google play services. Prerequisite for this app, bail if denied.
     *
     * @return True or False
     */
    public boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        1234).show();
            }
            return false;
        }
        return true;
    }

    /* Initiate Google API Client  */
    public void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        Log.d(TAG, "buildGoogleApiClient");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(BaseMapActivity.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public boolean checkLocationRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermissionSetting();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * Set up the location requests
     */
    private void createLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000); // ideal interval
            mLocationRequest.setFastestInterval(5000); // the fastest interval my app can handle
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // highest accuracy
        }
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermissionSetting() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(BaseMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(BaseMapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);

        } else {
            ActivityCompat.requestPermissions(BaseMapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }
    }

    /* Show Location Access Dialog */
    public void checkLocationSettings() {
        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }

    @Override
    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        final LocationSettingsStates state = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                if (!receiverManager.isReceiverRegistered(gpsLocationReceiver)) {
                    receiverManager.registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));
                }

                // All location settings are satisfied. The client can initialize location
                // requests here.
                updateGPSStatus("GPS is Enabled in your device.");
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. But could be fixed by showing the user
                // a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(BaseMapActivity.this, SETTINGS_LOCATION);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
                updateGPSStatus("Sorry! This service is not available.");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case SETTINGS_LOCATION:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        updateGPSStatus("GPS is Enabled in your device.");
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        updateGPSStatus("GPS is Disabled in your device.");
                        break;
                }
                break;
        }
    }

    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Check location settings
                    checkLocationSettings();

                } else {
                    updateGPSStatus("Location Permission denied.");
//                    Toast.makeText(BaseMapActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            checkLocationSettings();
        }
    };

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device.");
                    updateGPSStatus("GPS is Enabled in your device.");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showLocationSettingDialog();
                    updateGPSStatus("GPS is Disabled in your device.");
                    Log.e("About GPS", "GPS is Disabled in your device.");
                }

            }
        }
    };

    //Method to update GPS status text
    private void updateGPSStatus(String status) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }

    public void doPlaceSearch(GoogleMap googleMap) {
        try {
            isRequestedForPlaceSearch = true;
            Intent intent = new PlaceAutocomplete.IntentBuilder
                    (PlaceAutocomplete.MODE_OVERLAY)
                    .setBoundsBias(googleMap.getProjection().getVisibleRegion().latLngBounds)
                    .build(BaseMapActivity.this);
            startActivityForResult(intent, INTENT_PLACE_SEARCH);
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * GoogleApiClient.ConnectionCallbacks
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Check location request Permission
        if (checkLocationRequestPermission()) {
            checkLocationSettings();
        }
    }

    /**
     * GoogleApiClient.ConnectionCallbacks
     */
    @Override
    public void onConnectionSuspended(int i) {
    }

    /**
     * GoogleApiClient.OnConnectionFailedListener
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient onConnectionFailed");
    }

    /**
     * Places API
     * <p>
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.d(TAG, "Place Selected: " + place.getName());
        onSearchedPlaceItemSelected(place);
    }

    /**
     * Places API
     * <p>
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

}


