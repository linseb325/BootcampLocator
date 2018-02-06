package com.example.linseb325.bootcamplocator.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.linseb325.bootcamplocator.Fragments.MainFragment;
import com.example.linseb325.bootcamplocator.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity {

    private final String TAG = "Brennan";
    private final int PERMISSION_LOCATION_COARSE = 1;
    private final int REQUEST_CHECK_SETTINGS = 2;

    private MainFragment mainFragment;

    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        this.locationClient = LocationServices.getFusedLocationProviderClient(this);

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "Got location result");

                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, String.format("A location: %.3f, %.3f", location.getLatitude(), location.getLongitude()));
                    mainFragment.setUserMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        };

        startLocationServices();

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, mainFragment).commit();
        }
    }


    // Checks for location permission and requests them if not already granted.
    // When permissions are granted, gets user's last location.
    public void startLocationServices() {
        Log.d(TAG, "startLocationServices called");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permission has NOT been granted.
            // Request permission to use location.
            Log.d(TAG, "Requesting permission to use location");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION_COARSE);
        } else {
            // Location permission has been granted.
            // Request location updates.
            Log.d(TAG, "Location permission has been granted");
            try {
                Log.d(TAG, "Got into try block in startLocationServices");

                // Create the location request.
                final LocationRequest request = this.createLocationRequest(5000, 1000, LocationRequest.PRIORITY_LOW_POWER);

                // Add the location request to the location settings.
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(request);

                SettingsClient client = LocationServices.getSettingsClient(this);
                Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

                // What to do when the settings request is successful.
                // This means that location settings are satisfied for the given location request.
                task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.d(TAG, "Location settings response succeeded.\nIs location usable? -> "
                                + locationSettingsResponse.getLocationSettingsStates().isLocationUsable());
                        if (locationSettingsResponse.getLocationSettingsStates().isLocationUsable()) {
                            Log.d(TAG, "Requesting location updates now");
                            locationClient.requestLocationUpdates(request, locationCallback, null);
                        }
                    }
                });

                // What to do when the settings request fails.
                // This means that location settings are NOT satisfied for the given location request.
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Exception in OnFailureListener: " + e.getLocalizedMessage());
                        if (e instanceof ResolvableApiException) {
                            try {
                                Log.d(TAG, "Trying to resolve the ResolvableApiException in OnFailureListener");
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendException) {
                                Log.d(TAG, "Couldn't resolve the exception: " + sendException.getLocalizedMessage());
                            }
                        }
                        else {
                            Log.d(TAG, "e isn't an instance of ResolvableApiException");
                        }
                    }
                });

                locationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d(TAG, "Got last location: " + location.getLatitude() + ", " + location.getLongitude());
                        } else {
                            Log.d(TAG, "location was null in onSuccess");
                        }
                    }
                });
            } catch (SecurityException exception) {
                Log.d(TAG, exception.getLocalizedMessage());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Resolved the API exception.");
                } else {
                    Log.d(TAG, "Couldn't resolve the API exception.");
                }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION_COARSE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission to use fine location was granted.
                    startLocationServices();
                } else {
                    // Permission was denied.
                    Log.d(TAG, "Permission denied");
                    startLocationServices();
                }
        }
    }

    private LocationRequest createLocationRequest(long interval, long fastestInterval, int priority) {
        // Create and configure a location request.
        LocationRequest request = new LocationRequest();
        request.setInterval(interval);
        request.setFastestInterval(fastestInterval);
        request.setPriority(priority);
        return request;
    }
}
