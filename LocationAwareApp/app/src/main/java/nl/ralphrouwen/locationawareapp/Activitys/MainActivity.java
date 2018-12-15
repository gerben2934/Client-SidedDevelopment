package nl.ralphrouwen.locationawareapp.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.ralphrouwen.locationawareapp.Adapters.ParkedAdapter;
import nl.ralphrouwen.locationawareapp.Fragments.HistoryFragment;
import nl.ralphrouwen.locationawareapp.Fragments.MapFragment;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    public static final String PARKED_URL = "parkedURL";
    public static final String PARKEDLIST_URL = "parkedListURL";
    private static final int MY_PERMISSION_LOCATION = 99;
    ArrayList<Parked> parkeds = new ArrayList<Parked>();
    ImageButton parkbutton;
    boolean parkButtonPressed;
    private RecyclerView mRecyclerView;
    private ParkedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int REQUEST = 112;
    private Geocoder geocoder;
    private Context mContext;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        parkButtonPressed = false;
        //generateParkeds();
        Log.d("length:", "count() " + parkeds.size());

        parkbutton = findViewById(R.id.parkbutton);

        mLayoutManager = new LinearLayoutManager(this);
        geocoder = new Geocoder(mContext, Locale.getDefault());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getAddress() {
        List<Address> addressList;

        LatLng location = new LatLng(51.54808, 4.57885);
        LatLng location2 = new LatLng(51.86096769, 4.6721458);

        try {
            addressList = geocoder.getFromLocation(location2.latitude, location2.longitude, 2);
            if(addressList != null && addressList.size() != 0) {
                String addressStr = addressList.get(0).getAddressLine(0);
                Log.i("TEST", "AddressStreet: " + addressStr);
            }
            else
            {
                Log.i("NULL or count() = 0", "");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void parkButtonPressed(View view) {
        if (!parkButtonPressed) {
            parkbutton.setImageResource(R.drawable.parkbutton3);
            parkButtonPressed = true;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Set your car location");
            builder.setMessage("Are you sure you wanna set your car location to your current location?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    Log.i("Dialog: ", "Clicked YES, closing dialog!");
                    //get current location;
                    getAddress(); //put location in getAddress();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Dialog: ", "Clicked NO, closing dialog!");
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            parkbutton.setImageResource(R.drawable.parkbutton5);
            parkButtonPressed = false;
        }
        Log.i("HALLO", "puttonpressed");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
