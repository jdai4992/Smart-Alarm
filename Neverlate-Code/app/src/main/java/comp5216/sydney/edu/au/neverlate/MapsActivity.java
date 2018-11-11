package comp5216.sydney.edu.au.neverlate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.SettingsSlicesContract.KEY_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private static final int DEFAULT_ZOOM = 13;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation = new Location("");
    private TextView locationTextView;
    private Timer timer;
    private ArrayList<LatLng> listPoints;
    private Object[] dataTransfer;
    public static String distanceResult="",durationResult="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationTextView = (TextView) this.findViewById(R.id.location);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        listPoints= new ArrayList<LatLng>();
        TextView dis = (TextView)findViewById(R.id.showrote);
        TextView tim = (TextView)findViewById(R.id.showtime);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //refreshLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getCurrentLocation();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                switch (listPoints.size()){
                    case 0:
                        listPoints.add(latLng);
                        MarkerOptions markerstart = new MarkerOptions().position(latLng).title("Outset");
                        mMap.addMarker(markerstart);
                        break;
                    case 1:
                        listPoints.add(latLng);

                        dataTransfer=new Object[2];
                        String url=getDirectionsUrl();
                        dataTransfer[0]=mMap;
                        dataTransfer[1]=url;
                        GetDirectionsData getDirectionsData=new GetDirectionsData();
                        getDirectionsData.execute(dataTransfer);
                        MarkerOptions markerstop = new MarkerOptions().position(latLng).title("Destination");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TextView dis = (TextView)findViewById(R.id.showrote);
                                TextView tim = (TextView)findViewById(R.id.showtime);
                                dis.setText(distanceResult);
                                tim.setText(durationResult);
                            }
                        },1000);
                        mMap.addMarker(markerstop);
                        break;
                    case 2:
                        listPoints.clear();
                        mMap.clear();
                        TextView dis = (TextView)findViewById(R.id.showrote);
                        TextView tim = (TextView)findViewById(R.id.showtime);
                        dis.setText("");
                        tim.setText("");
                        Log.e("123",distanceResult);
                        Log.e("123",durationResult);
                        break;

                }

            }

        });

    }



    private String getDirectionsUrl(){
        StringBuilder googleDirectionsUrl=new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+listPoints.get(0).latitude+","+listPoints.get(0).longitude);
        googleDirectionsUrl.append("&destination="+listPoints.get(1).latitude+","+listPoints.get(1).longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyDU_0crY5zMruA2d1i63rK2eg26KEpQxGc");
        return googleDirectionsUrl.toString();

    }






    private void refresh(){
        timer=new Timer();


        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView dis = (TextView)findViewById(R.id.showrote);
                        TextView tim = (TextView)findViewById(R.id.showtime);
                        dis.setText(distanceResult);
                        tim.setText(durationResult);
                    }
                });
            }
        };
        timer.schedule(task,2000,10000000);
    }


    private void getCurrentLocation(){
        try{
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {

                        mLastKnownLocation = task.getResult();
                        String currentOrDefault = "Current";


                        if (mLastKnownLocation != null) {

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }

                        // Show location details on the location TextView
                        String msg = currentOrDefault + " Location: " +
                                Double.toString(mLastKnownLocation.getLatitude()) + ", " +
                                Double.toString(mLastKnownLocation.getLongitude());
                        locationTextView.setText(msg);

                    }
                }
            });

        }catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true); // false to disable my location button
                mMap.getUiSettings().setZoomControlsEnabled(true); // false to disable zoom controls
                mMap.getUiSettings().setCompassEnabled(true); // false to disable compass
                mMap.getUiSettings().setRotateGesturesEnabled(true); // false to disable rotate gesture
                mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void toPrepare(View view){
        Intent intent = new Intent(MapsActivity.this, PrepareActivity.class);
        startActivity(intent);
    }

}
