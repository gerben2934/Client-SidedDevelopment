package nl.ralphrouwen.locationawareapp.Helper.listeners;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface DirectionsProviderListener {
    void onReceivedDirections(List<LatLng> directionList);
    void onError(Error error);
}
