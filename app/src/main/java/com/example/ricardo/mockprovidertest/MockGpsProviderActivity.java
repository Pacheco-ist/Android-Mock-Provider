package com.example.ricardo.mockprovidertest;



import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MockGpsProviderActivity extends AppCompatActivity implements LocationListener {
    public static final String TAG = "MockGpsProviderActivity";

    public static final String GPS_MOCK_PROVIDER = "GpsMockProvider";


    int TAG_CODE_PERMISSION_LOCATION;

    private Integer mMockGpsProviderIndex = 0;

    Intent mServiceIntent;
    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_ENABLE_BT = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("fuck", "need permissions....");
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,},
                    TAG_CODE_PERMISSION_LOCATION);
        }

        if (BTAdapter == null || !BTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String mocLocationProvider = LocationManager.GPS_PROVIDER;
        locationManager.addTestProvider(mocLocationProvider, false, false,
                false, false, true, false, false, 0, 5);
        locationManager.setTestProviderEnabled(mocLocationProvider, true);
        locationManager.requestLocationUpdates(mocLocationProvider, 0, 0, this);

        mServiceIntent = new Intent(this, MockProviderService.class);
        this.startService(mServiceIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

      //TODO STOP MOCK SERVICE

        // remove it from the location manager
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeTestProvider(GPS_MOCK_PROVIDER);
        }
        catch (Exception e) {}
    }

    @Override
    public void onLocationChanged(Location location) {
        // show the received location in the view
        TextView view = (TextView) findViewById(R.id.text);
        view.setText( "index:" + mMockGpsProviderIndex
                + "\nlongitude:" + location.getLongitude()
                + "\nlatitude:" + location.getLatitude()
                + "\naltitude:" + location.getAltitude()
                + "\nroom:" + location.getExtras().getString("device")
        );
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


}