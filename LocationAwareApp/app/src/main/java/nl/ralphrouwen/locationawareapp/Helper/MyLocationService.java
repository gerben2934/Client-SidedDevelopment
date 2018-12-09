package nl.ralphrouwen.locationawareapp.Helper;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyLocationService {
    private static volatile MyLocationService instance;
    private Application application;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng lastKnownLocation;

    private MyLocationService(Application application) {
        this.application = application;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public static MyLocationService getInstance(Application application) {
        if (instance == null) {
            instance = new MyLocationService(application);
        }
        return instance;
    }

    public void getLastKnownLocation() {
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public LatLng getLocation()
    {
        return lastKnownLocation;
    }
}