package nl.ralphrouwen.locationawareapp.Helper;


import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class GPSManager extends Service implements LocationListener {

    private final Context context;

    //Flags (GPS, Network etc.)
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    private Location location;
    private double latitude;
    private double longitude;

//    private static final long MIN_DISTANCE_FOR_UPDATE = 10; //in meters
//    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60; // in miliseconds

    private static final long MIN_DISTANCE_FOR_UPDATE = 0; //in meters
    private static final long MIN_TIME_FOR_UPDATE = 0; // in miliseconds

    private LocationManager locationManager;

    public GPSManager(Context contextm) {
        this.context = contextm;
        getLocation();
    }

    @SuppressLint("ServiceCast")
    private Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            //Getting GPS status:
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //Getting network status:
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
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

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location changed!");
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

        // return latitude
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
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

}

