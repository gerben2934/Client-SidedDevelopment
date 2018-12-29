package nl.ralphrouwen.locationawareapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import nl.ralphrouwen.locationawareapp.Activitys.AppContext;
import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.Helper.listeners.LocationListener;
import nl.ralphrouwen.locationawareapp.LocationPermissionRequest;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;
import nl.ralphrouwen.locationawareapp.business.listeners.DirectionsListener;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;


public class DetailedParkedMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Context context;
    private Parked parked;
    private Marker marker;
    private Polyline polyline;


    public DetailedParkedMapFragment() {

    }

    public static DetailedParkedMapFragment newInstance(Parked parked) {
        DetailedParkedMapFragment fragment = new DetailedParkedMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARKED_URL, (Parcelable) parked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if(bundle != null)
        {
            parked = (Parked) bundle.getParcelable(PARKED_URL);
        }
        if (getArguments() != null)
        {
            Log.i("welll....", "getArguments() != null");
        }
        Log.i("PARKED OBJECT: ", "PARKED: " + parked.toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_detailed_map, container, false);
        context = container.getContext();
/*        Bundle args = new Bundle();
        args.putParcelable(TAG, (Parcelable) parked);
        this.setArguments(args);*/

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        if (LocationPermissionRequest.requestPermission(this))
        {
            try {
                googleMap.setMyLocationEnabled(true);
            }
            catch (SecurityException sx) {sx.printStackTrace();}
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            LatLng carLocation = new LatLng(parked.getLatitude(), parked.getLongitude());

            String address = MainActivity.getAddress(carLocation);
            googleMap.addMarker(new MarkerOptions().position(carLocation).title(context.getResources().getString(R.string.carLocation)).snippet(context.getResources().getString(R.string.address) + " " + address));
            CameraPosition osso = CameraPosition.builder().target(carLocation).zoom(14).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(osso));
        }
    }

    @Override
    public void onLocationListener(Location location) {

    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLUE);
        polyOptions.width(5);
        polyOptions.addAll(list);

//        mMap.clear();
        polyline = mGoogleMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list)
            builder.include(latLng);
    }

    public void getRoute(LatLng origin, LatLng destination)
    {
//        LatLng origin = new LatLng(51.8583878, 4.677771099999973);
//        LatLng destination = new LatLng(51.5719149, 4.768323000000009);

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