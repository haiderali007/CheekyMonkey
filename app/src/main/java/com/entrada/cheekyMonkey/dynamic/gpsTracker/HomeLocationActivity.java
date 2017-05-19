package com.entrada.cheekyMonkey.dynamic.gpsTracker;

import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.ui.CustomTextview;

/**
 * Created by Rahul on 30/04/2016.
 */


public class HomeLocationActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    Context context;
    CustomTextview mLatitude, mLongitude, mAccuracy, mCity, mPincodee, mCountry;


    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_layout);
        context = this;
        mLatitude = (CustomTextview) findViewById(R.id. tv_lattitude);
        mLongitude = (CustomTextview) findViewById(R.id. tv_longitude);
        mAccuracy = (CustomTextview) findViewById(R.id.tv_accuracy);
        mCity = (CustomTextview) findViewById(R.id.tv_city);
        mPincodee = (CustomTextview) findViewById(R.id.tv_pincode);
        mCountry = (CustomTextview) findViewById(R.id.tv_country);


                mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                        //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

    }


    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            float accuracy = location.getAccuracy();

            mLatitude.setText(String.valueOf(currentLongitude));
            mLongitude.setText(String.valueOf(currentLatitude));
            //mAccuracy.setText(String.valueOf(accuracy));
            mAccuracy.setText(getAddressLine());
            mCity.setText(getLocality());
            mPincodee.setText(getPostalCode());
            mCountry.setText(getCountryName());
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }


    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getLocality(){

        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 1);
            if (addresses.size() > 0){

                String locality = addresses.get(0).getLocality();
                return locality;
            }

        }catch (IOException e){e.printStackTrace();}

        return null;
    }

    /**
     * Try to get AddressLine
     * @return null or addressLine
     */
    public String getAddressLine() {

        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);

                return addressLine;
            } else {
                return null;
            }
        }catch (IOException e){e.printStackTrace();}

        return null;
    }

    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    public String getPostalCode() {

        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String postalCode = address.getPostalCode();
                return postalCode;
            } else {
                return null;
            }
        }catch (IOException e){e.printStackTrace();}

        return null;
    }

    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    public String getCountryName() {


        try {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String countryName = address.getCountryName();

                return countryName;
            } else {
                return null;
            }
        }catch (IOException e){e.printStackTrace();}

        return null;
    }
}