package nl.ralphrouwen.locationawareapp.Helper;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class LocationTracker {
    private static volatile LocationTracker instance;
    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng lastKnownLocation;

    private LocationTracker(Context context) {
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static LocationTracker getInstance(Context context) {
        if (instance == null) {
            instance = new LocationTracker(context);
        }
        return instance;
    }






}
