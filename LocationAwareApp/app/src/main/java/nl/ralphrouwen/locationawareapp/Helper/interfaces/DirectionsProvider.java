package nl.ralphrouwen.locationawareapp.Helper.interfaces;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import nl.ralphrouwen.locationawareapp.Helper.listeners.DirectionsProviderListener;

public interface DirectionsProvider {
    void queueDirectionsRequest(LatLng origin, LatLng destination , DirectionsProviderListener listener);

}
