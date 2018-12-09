package nl.ralphrouwen.locationawareapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;

import nl.ralphrouwen.locationawareapp.Helper.GPSTracker;
import nl.ralphrouwen.locationawareapp.Helper.LocationListener;
import nl.ralphrouwen.locationawareapp.Helper.MyLocationService;
import nl.ralphrouwen.locationawareapp.R;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private OnFragmentInteractionListener mListener;
    Activity mactivity;
    private Context context;
    GPSTracker gpsTracker;

    private MyLocationService service;
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
        mactivity = getActivity();
        Application application = mactivity.getApplication();
        service.getInstance(application);
        gpsTracker = new GPSTracker(context, this::onLocationListener);
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
//        mMap.setLocationSource(this);
//        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        Location location = gpsTracker.getLocation();
        Double latitiude = gpsTracker.getLatitude();
        LatLng mylocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mylocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
//        service.getLastKnownLocation();
//        LatLng location = service.getLocation();
//        if(location != null)
//        {
//            mMap.addMarker(new MarkerOptions().position(location).title("My location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
//        }
    }

    public void updateLocation(Location location)
    {
        Double longitude = location.getLongitude();
        Double latitiude = location.getLatitude();

        LatLng mylocation = new LatLng(latitiude, longitude);
        mMap.addMarker(new MarkerOptions().position(mylocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
    }

//
//
//    @Override
//    public void onLocationChanged(Location location) {
//        if (locationChangedListener != null) {
//            locationChangedListener.onLocationChanged(location);
//        }
//
//        Log.i("Location changed", "onLocationChanged()");
//    }
//
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        locationChangedListener = onLocationChangedListener;
//    }
//
//    @Override
//    public void deactivate() {
//        locationChangedListener = null;
//    }

    @Override
    public void onLocationListener(Location location) {
//        updateLocation(location);
        Log.i("location changed!", String.valueOf(location));
        if(mMap != null)
        {
            mMap.clear();
                    LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mylocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
        }
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