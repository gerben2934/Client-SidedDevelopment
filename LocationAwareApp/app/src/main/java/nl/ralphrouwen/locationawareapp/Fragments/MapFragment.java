package nl.ralphrouwen.locationawareapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.Helper.GPSTracker;
import nl.ralphrouwen.locationawareapp.Helper.LocationListener;
import nl.ralphrouwen.locationawareapp.R;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private OnFragmentInteractionListener mListener;
    private Context context;
    GPSTracker gpsTracker;
    Location lastlocation;
    Geocoder geocoder;
    boolean startup = true;

    Location lastKownLocation;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mlocationRequest;
    LocationCallback mLocationCallback;
    Location currentLocation;


    public MapFragment() {
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
////        mFusedLocationClient.getLastLocation()
////                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
////                    @Override
////                    public void onSuccess(Location location) {
////                        // Got last known location. In some rare situations this can be null.
////                        if (location != null) {
////                            lastKownLocation = location;
////
////                        }
////                    }
////                });
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
//        gpsTracker = new GPSTracker(context, this::onLocationListener);
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(1);
        mlocationRequest.setFastestInterval(1);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                startLocation(locationResult.getLastLocation());
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mlocationRequest, mLocationCallback, Looper.myLooper());
    }



//    @Override
//    public void onLocationListener(Location location) {
//        if(location != null && lastlocation != null && mMap != null)
//        {
//            if(startup)
//            {
//                updateLocation(location);
//                startup = false;
//            }
//
//            if(distance(location.getLatitude(), location.getLongitude(), lastlocation.getLatitude(), lastlocation.getLongitude()) >= 0.07)
//            {
//                updateLocation(location);
//            }
//        }
//
//        lastlocation = location;
//    }

    public void startLocation(Location location)
    {
        if(location != null && lastlocation != null && mMap != null)
        {
            if(startup)
            {
                updateLocation(location);
                startup = false;
            }

            if(distance(location.getLatitude(), location.getLongitude(), lastlocation.getLatitude(), lastlocation.getLongitude()) >= 0.07)
            {
                updateLocation(location);
            }
        }

        lastlocation = location;
    }

    public void updateLocation(Location location)
    {
        mMap.clear();
        LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mylocation).title("Your Location!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
