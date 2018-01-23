package com.example.linseb325.bootcamplocator.Activities;

import android.Manifest;
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
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapsActivity extends FragmentActivity {

    private final String TAG = "Brennan";
    private final int PERMISSION_LOCATION_COARSE = 1;

    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        this.locationClient = LocationServices.getFusedLocationProviderClient(this);

        startLocationServices();

        MainFragment mainFrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (mainFrag == null) {
            mainFrag = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, mainFrag).commit();
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
            // Get the last location and print it to the screen.
            Log.d(TAG, "Location permission has been granted");
            try {
                Log.d(TAG, "Got into try block in startLocationServices");
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

        Log.d(TAG, "End of startLocationServices call");
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
}
