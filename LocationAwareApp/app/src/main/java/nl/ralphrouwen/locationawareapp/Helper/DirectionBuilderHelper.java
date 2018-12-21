package nl.ralphrouwen.locationawareapp.Helper;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class DirectionBuilderHelper {

    private static String formatLatLng(LatLng latLng)
    {
        return latLng.latitude + "," + latLng.longitude;
    }

    public static String buildStandaardRequest(String url, String key, LatLng origin, LatLng destination)
    {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append(url);
        requestBuilder.append("?key=");
        requestBuilder.append(key);
        requestBuilder.append("&mode=walking");
        requestBuilder.append("&origin=");
        requestBuilder.append(formatLatLng(origin));
        requestBuilder.append("&destination=");
        requestBuilder.append(formatLatLng(destination));
        Log.e("DATARECEIVEDDIRECTIONS", requestBuilder.toString());
        return requestBuilder.toString();
    }
}
