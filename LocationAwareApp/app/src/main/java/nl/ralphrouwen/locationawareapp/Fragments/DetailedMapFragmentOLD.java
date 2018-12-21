package nl.ralphrouwen.locationawareapp.Fragments;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import nl.ralphrouwen.locationawareapp.Helper.listeners.LocationListener;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;


public class DetailedMapFragmentOLD extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnInfoWindowClickListener, LocationListener {

    private GoogleMap mMap;
    private Parked parked;
    private Marker marker;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private Location parkedLocation;
    private Geocoder geocoder;
    boolean startup = true;
    private static final String TAG = "PARKEDURL";


    public DetailedMapFragmentOLD() {
    }

    public static DetailedMapFragmentOLD newInstance(Parked parked) {
        DetailedMapFragmentOLD fragment = new DetailedMapFragmentOLD();
        Bundle args = new Bundle();
        args.putParcelable(TAG, (Parcelable) parked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if(bundle != null)
        {
            parked = (Parked) bundle.getParcelable(TAG);
        }

        if (getArguments() != null) {
        }

        Log.i("PARKED OBJECT: ", "PARKED: " + parked.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(context, Locale.getDefault());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
    }


    @Override
    public void onLocationListener(Location location) {
        if(location != null && parkedLocation != null && mMap != null)
        {
            if(startup)
            {
                updateLocation(location);
                startup = false;
            }

            if(distance(location.getLatitude(), location.getLongitude(), parkedLocation.getLatitude(), parkedLocation.getLongitude()) >= 0.07)
            {
                updateLocation(location);
            }
        }

        parkedLocation = location;
    }

    public void updateLocation(Location location)
    {
        mMap.clear();
        LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mylocation).title("Your parked your car here! ID: " + parked.getId()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
    }

    public void removeMarker()
    {
        Log.i("REMOVED MARKER", "aaa");
        marker.remove();
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
