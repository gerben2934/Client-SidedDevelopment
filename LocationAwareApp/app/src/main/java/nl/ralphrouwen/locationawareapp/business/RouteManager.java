package nl.ralphrouwen.locationawareapp.business;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import nl.ralphrouwen.locationawareapp.Helper.MovieCastDirectionsProvider;
import nl.ralphrouwen.locationawareapp.Helper.interfaces.DirectionsProvider;
import nl.ralphrouwen.locationawareapp.Helper.listeners.DirectionsProviderListener;
import nl.ralphrouwen.locationawareapp.business.listeners.DirectionsListener;

public class RouteManager {
    private DirectionsProvider directionsProvider;

    public RouteManager(Context context) {
        this.directionsProvider = new MovieCastDirectionsProvider(context);
    }

    public void getDirections(LatLng start, LatLng end, DirectionsListener listener)
    {
        Log.e("DATARECEIVEDDIRECTIONS", "Komt die her?");
        directionsProvider.queueDirectionsRequest(start, end, new DirectionsProviderListener() {
            @Override
            public void onReceivedDirections(List<LatLng> directionList) {
                listener.onReceivedDirections(directionList);
                Log.e("DATARECEIVEDDIRECTIONS", String.valueOf(directionList));

            }
            @Override
            public void onError(Error error) {

            }
        });
    }
}
