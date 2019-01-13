package nl.ralphrouwen.locationawareapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.ralphrouwen.locationawareapp.Activitys.AppContext;
import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.LocationPermissionRequest;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;
import nl.ralphrouwen.locationawareapp.business.listeners.DirectionsListener;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private static Context context;
    private static LatLng myLocation;

    private OnFragmentInteractionListener mListener;
    private Location lastlocation;
    private Geocoder geocoder;
    boolean startup = true;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mlocationRequest;
    private LocationCallback mLocationCallback;
    private Location currentLocation;

    private static ArrayList<Marker> markers;
    private static Marker parkedMarker;
    private static String parkedInfo;

    private Polyline polyline;

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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
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
//        getRoute();
        if (LocationPermissionRequest.requestPermission(this))
        {
            try {
                mMap.setMyLocationEnabled(true);
            }
            catch (SecurityException sx) {sx.printStackTrace();}

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    LinearLayout info = new LinearLayout(context);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(context);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(context);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
            setupLocation();
        }
        else
            return;
    }

    public void setupLocation()
    {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(2000);
        mlocationRequest.setFastestInterval(2000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mlocationRequest, new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
//                startLocation(locationResult.getLastLocation());
                updateLocation(currentLocation);
                //Log.e("LOG!!!!!", String.valueOf(currentLocation));
            }
        }, Looper.myLooper());
    }

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
/*        if(startup)
        {
            //alle markers verwijderen.
            //Markers.get(0) (eigen locatie) updaten met location!
            //alle markers opnieuw tekenen

//            for (int i = 0; i < markers.size(); i++)
//            {
//                if(i == 0)
//                {
//                    Marker oldLocationMarker = markers.get(0);
//                    Marker newLocationMarker = new Marker()
//                    markers.get(0).get
//                }
//            }

            LatLng parkedLocation = parkedMarker.getPosition();
            mMap.clear();
            setParkedMarker(parkedLocation, parkedInfo);
        }*/

        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14.0f));
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

    public static LatLng getMyLocation() {
        return myLocation;
    }

    public static void setParkedMarker(Parked parked)
    {
        LatLng location = new LatLng(parked.getLatitude(), parked.getLongitude());
        String address = MainActivity.getAddress(location);
        String info = context.getResources().getString(R.string.address) + " " + MainActivity.getAddress(location)
                + "\r\n" + context.getResources().getString(R.string.payedTill) + " " + parked.getEndTime().toString("hh:mm, MMM d yyyy");
        parkedMarker = mMap.addMarker(new MarkerOptions().position(location).title(context.getResources().getString(R.string.carLocation))
                .snippet(info).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        CameraPosition parkedmarker = CameraPosition.builder().target(location).zoom(14).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(parkedmarker));
    }

    public static void setMarker(Parked parked)
    {
        LatLng location = new LatLng(parked.getLatitude(), parked.getLongitude());
        String title = context.getResources().getString(R.string.carlocation);
        String info = context.getResources().getString(R.string.address) + " " + MainActivity.getAddress(location);
        mMap.addMarker(new MarkerOptions().position(location).title(title)
                .snippet(info).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    public static void removeParkedMarker() {
        parkedMarker.remove();
    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLUE);
        polyOptions.width(5);
        polyOptions.addAll(list);

//        mMap.clear();
        polyline = mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list)
            builder.include(latLng);
    }

    public void getRoute()
    {
        LatLng origin = new LatLng(51.8583878, 4.677771099999973);
        LatLng destination = new LatLng(51.5719149, 4.768323000000009);

        AppContext.getInstance(context).getRouteManager().getDirections(origin, destination, new DirectionsListener() {
            @Override
            public void onReceivedDirections(List<LatLng> directionList) {
                Log.e("DATARECEIVEDDIRECTIONS1234567891", String.valueOf(directionList));
                drawPolyLineOnMap(directionList);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }
}
