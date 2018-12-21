package nl.ralphrouwen.locationawareapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

public class LocationPermissionRequest
{
    public static final int LOCATION_REQUEST_CODE = 11224;
    // Boolean geeft aan of de permissie gelijk is gelukt
    public static boolean requestPermission(Fragment fragment)
    {
        if (ContextCompat.checkSelfPermission(fragment.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else {
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            //ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        return false;
    }
}
