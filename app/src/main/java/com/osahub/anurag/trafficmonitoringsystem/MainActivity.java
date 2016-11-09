package com.osahub.anurag.trafficmonitoringsystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    double currentlatitude;
    double currentlongitude;
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://192.168.43.185:8068/tms/location.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";



    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
//    private static int DISPLACEMENT = 10; // 10 meters

    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First we need to check availability of play services
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();

//            displayLocation();
//        togglePeriodicLocationUpdates();
        }// Show location button click listener

        // Toggling the periodic location updates

    }
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

        }
//        displayLocation();
//        togglePeriodicLocationUpdates();


    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        initilizeMap();
        // Resuming the periodic location updates
//        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//        if (mGoogleApiClient.isConnected()) {
//            displayLocation();
//            startLocationUpdates();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    // Method to show map


    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
             currentlatitude = mLastLocation.getLatitude();
             currentlongitude = mLastLocation.getLongitude();
            new sendlocation().execute();
            LatLng latLng = new LatLng(currentlatitude,currentlongitude);
            googleMap.clear();
            MarkerOptions marker = new MarkerOptions().position(latLng).title(latLng.toString());
// adding marker
            googleMap.addMarker(marker);
            float zoomLevel;
            zoomLevel = 16.0f;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        } else {

            Toast.makeText(this,"(Couldn't get the location. Make sure location is enabled on the device)", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method to toggle periodic location updates
     * */
//    private void togglePeriodicLocationUpdates() {
//        if (!mRequestingLocationUpdates) {
//            // Changing the button text
////            btnStartLocationUpdates
////                    .setText(getString(R.string.btn_stop_location_updates));
//
//            mRequestingLocationUpdates = true;
//
//            // Starting the location updates
//            startLocationUpdates();
//
//            Log.d(TAG, "Periodic location updates started!");
//
//        } else {
//            // Changing the button text
////            btnStartLocationUpdates
////                    .setText(getString(R.string.btn_start_location_updates));
//
//            mRequestingLocationUpdates = false;
//
//            // Stopping the location updates
//            stopLocationUpdates();
//
//            Log.d(TAG, "Periodic location updates stopped!");
//        }
//    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

//        if (mRequestingLocationUpdates) {
        startLocationUpdates();
//        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

//        Toast.makeText(getApplicationContext(), "Location changed!",
//                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    class sendlocation extends AsyncTask<String, String, String> {
        int success;
        String a="anurag";
        int collision=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
            @Override
        protected String doInBackground(String... args) {
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username",a ));
                params.add(new BasicNameValuePair("latitude", String.valueOf(currentlatitude)));
                params.add(new BasicNameValuePair("longitude", String.valueOf(currentlongitude)));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Sending Coordinates", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1 || success==2) {
                    Log.d("Send Successful", json.toString());
                  //  finish();
                    if(success==2){
                        collision=1;
                    }
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Sending Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
//            if (file_url != null){
//                Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_SHORT).show();
//            }
//                if (collision==1)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Collision Detected");

                    // Setting Dialog Message
                    alertDialog.setMessage("Do you want to proceed?");

                    // Setting Icon to Dialog
                  //  alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            dialog.cancel();
                            startActivity(intent);
                            // Write your code here to invoke YES event
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            Intent intent = new Intent(MainActivity.this,DisplayActivity.class);
                            dialog.cancel();
                        startActivity(intent);
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
        }
    }


}
