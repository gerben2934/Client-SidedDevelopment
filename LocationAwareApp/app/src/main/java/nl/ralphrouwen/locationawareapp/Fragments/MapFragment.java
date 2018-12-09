package nl.ralphrouwen.locationawareapp.Fragments;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDateTime;

import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.Activitys.MapsActivity;
import nl.ralphrouwen.locationawareapp.Helper.GPSManager;
import nl.ralphrouwen.locationawareapp.R;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationSource.OnLocationChangedListener, LocationSource {

    private GoogleMap mMap;
    private OnFragmentInteractionListener mListener;
    private OnLocationChangedListener locationChangedListener;

    private GPSManager gpsManager;
    private Context context;
    private LatLng location;
    private Application application;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);

        gpsManager = GPSManager.getInstance(getActivity().getApplication());
        gpsManager.startCollecting();
        if (location != null) {
            Log.i("MapFragment: ", String.valueOf(location));
            Log.i("MapFragment: ", "Latitude: " + String.valueOf(location.latitude));
            Log.i("MapFragment: ", "Longitude: " + String.valueOf(location.longitude));
        }

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
        super.onAttach(context);
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setLocationSource(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        if (gpsManager.getLastKnownLocation() != null) {
            LatLng mylocation = gpsManager.getLastKnownLocation();
            mMap.addMarker(new MarkerOptions().position(mylocation).title("Your Location!"));
            Log.d("New location MAPFRAGMENT!", "Location: LONG: " + mylocation.longitude + " LAT: " + mylocation.latitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17.0f));
        }

        gpsManager.getAddress(context, 4.5788755f,  51.54810330000001f);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (locationChangedListener != null) {
            locationChangedListener.onLocationChanged(location);
        }

        Log.i("Location changed", "onLocationChanged()");
        LatLng a = gpsManager.getLastKnownLocation();
        mMap.addMarker(new MarkerOptions().position(a).title("Updated location!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 17.0f));
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onLocationChanged(Location location) {
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//
//                locationManager.requestLocationUpdates(
//                        locationManager.NETWORK_PROVIDER,
//                        MIN_TIME,
//                        50,
//                        locationListener
//                );
//////                Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
//                Log.i("LOCATIONDEBUG", "Time: " + LocalDateTime.now() + "|" + location.toString());
//                mMap.clear();
//                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                Log.d("Provider", "LocationListener: OnProviderEnabled()");
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                Log.d("Provider", "LocationListener: OnProviderDisabled()");
//            }
//        };
//
//        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
////                mMap.clear();
////                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
////                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
////                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//        }