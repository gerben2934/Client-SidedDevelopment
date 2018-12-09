package nl.ralphrouwen.locationawareapp.Helper;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GPSManager {

    private static volatile GPSManager instance;
    private Application application;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng lastKnownLocation;
    private Geocoder geocoder;

    private GPSManager(Application application) {
        this.application = application;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public static GPSManager getInstance(Application application) {
        if (instance == null) {
            instance = new GPSManager(application);
        }
        return instance;
    }

    public LatLng getLastKnownLocation() {
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.i("TEST", "Komt hier");
                        lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                    //Log.i("TEST", "NULL!!");

                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.d("GPSManager: ", "Location ERROR!");
            return null;
        }
        return lastKnownLocation;
    }

    public void startCollecting() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    getLastKnownLocation();
                    if(lastKnownLocation == null){
                        Log.i("LATLNG", "NULL");
                    }
                    if (lastKnownLocation != null)
                        Log.i("LATLNG LOCATION", "lat: " + lastKnownLocation.latitude + " long:" + lastKnownLocation.longitude);
                    try {
                        //Thread.sleep(100); //for simulation
                        Thread.sleep(1000); //for testing IRL
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

/*    public void getAddress() //LatLng lastKnownLocation)
    {
        java.util.List<Address> addresses;
        geocoder = new Geocoder((Context)application,Locale.getDefault());

        lastKnownLocation = new LatLng(4.5788755f,  51.54810330000001f);
        try {
            addresses = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        Log.i("Last adres:", "Info:" + addresses.toString());
    }*/

    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {
        String fullAdd = "";
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context);
            //new Geocoder(context, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 5);
            if(geocoder.isPresent()) {
                if (addresses != null && addresses.size() > 0) {
                    fullAdd = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    Log.d("GetAdress()", "getAddress:  address" + fullAdd);
                    Log.d("GetAdress()", "getAddress:  city" + city);
                    Log.d("GetAdress()", "getAddress:  state" + state);
                    Log.d("GetAdress()", "getAddress:  postalCode" + postalCode);
                    Log.d("GetAdress()", "getAddress:  knownName" + knownName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAdd;
    }
}


/*import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;

public class GPSManager extends Service implements LocationListener {

    private final Context context;

    //Flags (GPS, Network etc.)
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    private Marker locationMarker;
    private Location location;
    private double latitude;
    private double longitude;

//    private static final long MIN_DISTANCE_FOR_UPDATE = 10; //in meters
//    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60; // in miliseconds

    private static final long MIN_DISTANCE_FOR_UPDATE = 0; //in meters
    private static final long MIN_TIME_FOR_UPDATE = 1000; // in miliseconds

    private LocationManager locationManager;
    private LocationListener listener;

    public GPSManager(Context contextm) {
        this.context = contextm;
        getLocation();
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("LOCATIONLISTENER", "Location changed!");
                    latitude = location.getLatitude();
                    longitude  = location.getLongitude();
                    LatLng coordinate = new LatLng(latitude, longitude);
                    //locationManager =
                    //moveMap();
                    Log.d("New location1234:", "Latitude: " + latitude + " Longitude: " + longitude);

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };


            //Getting GPS status:
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //Getting network status:
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.i("ISENABLED1", String.valueOf(isGPSEnabled) + isNetworkEnabled);
                Log.i("ISENABLED1", String.valueOf(getLongitude()));

                // no network provider is enabled
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;
                Log.i("ISENABLED2", String.valueOf(isGPSEnabled) + isNetworkEnabled);
                Log.i("ISENABLED2", String.valueOf(getLongitude()));

                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity)context, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        }, 10);
                    Log.i("ISENABLED3", String.valueOf(isGPSEnabled) + isNetworkEnabled);
                    Log.i("ISENABLED3", String.valueOf(getLongitude()));
                    }

                    Log.i("ISENABLED5", String.valueOf(isGPSEnabled) + isNetworkEnabled);
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATE,
                            MIN_DISTANCE_FOR_UPDATE,
                            this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                //if GPS Enabled get latitude and longitude using the GPS services:
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_FOR_UPDATE,
                                MIN_DISTANCE_FOR_UPDATE, this);
                        Log.d("GPS Enabled", "Your GPS is enabled!");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location changed!");
        if (location != null) {

            latitude = location.getLatitude();
            longitude  = location.getLongitude();
            LatLng coordinate = new LatLng(latitude, longitude);
            //locationManager =
            moveMap();
            Log.d("New location:", "Latitude: " + latitude + " Longitude: " + longitude);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Status", "Status changed!");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Provider!", "Provider is Enabled!");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Provider!", "Provider is Disabled!");
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    private void moveMap() {
        *//**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         *//*
        LatLng latLng = new LatLng(latitude, longitude);

*//*
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Marker in India"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
*//*
    }


    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}*/

