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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.ralphrouwen.locationawareapp.Adapters.ParkedAdapter;
import nl.ralphrouwen.locationawareapp.Fragments.DetailedParkedMapFragment;
import nl.ralphrouwen.locationawareapp.Fragments.HistoryFragment;
import nl.ralphrouwen.locationawareapp.Fragments.MapFragment;
import nl.ralphrouwen.locationawareapp.Helper.InputFilterMinMax;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    public static final String PARKEDLIST_URL = "parkedListURL";
    private static final int MY_PERMISSION_LOCATION = 99;

    public static final String PARKED_URL = "parkedURL";
    private ArrayList<Parked> parkeds;
    private Parked currentParked;

    private ImageButton parkbutton;
    boolean parkButtonPressed;

    private EditText editDays;
    private EditText editHours;
    private EditText editMinutes;
    private TextView textDays;
    private TextView textHours;
    private TextView textMinutes;

    private RecyclerView.LayoutManager mLayoutManager;
    private static final int REQUEST = 112;
    private static Geocoder geocoder;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        parkButtonPressed = false;
        generateParkeds();
        buildHistoryFragment();
        bindComponents();
        fillComponents();
        Log.d("length:", "count() " + parkeds.size());
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

    public static String
    getAddress(LatLng location) {
        List<Address> addressList;
        String addressStr = "";

        try {
            addressList = geocoder.getFromLocation(location.latitude, location.longitude, 2);
            if(addressList != null && addressList.size() != 0) {
                addressStr = addressList.get(0).getThoroughfare() + " ";
                addressStr += addressList.get(0).getSubThoroughfare() + ", ";
                addressStr += addressList.get(0).getLocality(); }
            else
            {
                Log.i("NULL or count() = 0", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressStr;
    }


    public void parkButtonPressed(View view) {
        if (!parkButtonPressed) {
            AlertDialog.Builder setLocationBuilder = new AlertDialog.Builder(this);
            setLocationBuilder.setTitle("Set your car location");
            setLocationBuilder.setMessage("Are you sure you wanna set your car location to your current location?");
            setLocationBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    Log.i("Dialog: ", "Clicked YES, closing dialog!");
                    parkbutton.setImageResource(R.drawable.parkbutton3);
                    parkButtonPressed = true;
                    int UniqueID = parkeds.size() + 1;
                    LatLng currentLocation = MapFragment.getMyLocation();
                    ArrayList<Integer> values = getValues();

                    DateTime begin = DateTime.now();
                    DateTime end = begin.plusDays(values.get(0));
                    end = end.plusHours(values.get(1));
                    end = end.plusMinutes(values.get(2));

                    currentParked = new Parked(UniqueID, (float)currentLocation.longitude, (float)currentLocation.latitude, begin, end, true, getAddress(currentLocation));
                    parkeds.add(0, currentParked);
                    editDays.setText("");
                    editHours.setText("");
                    editMinutes.setText("");

                    String info = getResources().getString(R.string.address) + " " + getAddress(currentLocation)
                            + "\r\n" + getResources().getString(R.string.payedTill) + currentParked.getEndTime().toString("hh:mm, MMM d yyyy");
                    MapFragment.setParkedMarker(currentLocation, info);

                    HistoryFragment.updateRecyclerView(currentParked, true);
                    dialog.dismiss();
                }
            });

            setLocationBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Dialog: ", "Clicked NO, closing dialog!");
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alertSet = setLocationBuilder.create();
            alertSet.show();


        } else {
            AlertDialog.Builder removeLocationBuilder = new AlertDialog.Builder(this);
            removeLocationBuilder.setTitle("Remove your car location");
            removeLocationBuilder.setMessage("Are you sure you wanna remove your car location from the map?\r\n" +
            "(Warning! This can not be this can not be undone!)");
            removeLocationBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    parkbutton.setImageResource(R.drawable.parkbutton5);
                    parkButtonPressed = false;
                    // Do nothing but close the dialog
                    Log.i("Dialog: ", "Clicked YES, closing dialog!");
                    parkeds.remove(0);
                    HistoryFragment.updateRecyclerView(parkeds.get(0), false);
                    MapFragment.removeParkedMarker();
                    //remove last parked object from list
                    dialog.dismiss();
                }
            });

            removeLocationBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Dialog: ", "Clicked NO, closing dialog!");
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alertSet = removeLocationBuilder.create();
            alertSet.show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void bindComponents() {
        parkbutton = findViewById(R.id.parkbutton);
        editDays = findViewById(R.id.editText_days);
        editHours = findViewById(R.id.editText_hours);
        editMinutes = findViewById(R.id.editText_minutes);
        textDays = findViewById(R.id.textView_days);
        textHours = findViewById(R.id.textView_hours);
        textMinutes = findViewById(R.id.textView_minutes);
    }

    public void fillComponents() {
        textDays.setText(getResources().getString(R.string.days));
        textHours.setText(getResources().getString(R.string.hours));
        textMinutes.setText(getResources().getString(R.string.minutes));
        editDays.setFilters(new InputFilter[]{new InputFilterMinMax(0, 7), new InputFilter.LengthFilter(1)});
        editHours.setFilters(new InputFilter[]{new InputFilterMinMax(0, 23), new InputFilter.LengthFilter(2)});
        editMinutes.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59), new InputFilter.LengthFilter(2)});
    }

    public ArrayList<Integer> getValues()
    {
        ArrayList<Integer> values = new ArrayList<>();

        values.add(getInt(editDays.getText().toString(), 0));
        values.add(getInt(editHours.getText().toString(), 0));
        values.add(getInt(editMinutes.getText().toString(), 0));
        return values;
    }

    public void generateParkeds()
    {
        parkeds = new ArrayList<>();
        parkeds.add(new Parked(1, 4.5788538f,  51.5480428f, new DateTime(2018, 10, 22, 0, 0),
                new DateTime(2018, 10, 27, 5, 10), false, "Gerbens huis"));
        parkeds.add(new Parked(2, 4.7927f, 51.5857f, new DateTime(2018, 10, 22, 0, 0),
                new DateTime(2018, 10, 23, 5, 10), false, "Gerbens school"));
        parkeds.add(new Parked(3, 4.6721458f, 51.86096769f, new DateTime(2018, 11, 10, 0, 12),
                new DateTime(2018, 11, 10, 10, 12), false, "Ralphs adres"));
    }

    private void buildHistoryFragment()
    {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        HistoryFragment hf = HistoryFragment.newInstance(parkeds);
        fragmentManager.beginTransaction().replace(R.id.fragment_history, hf).commit();
    }

    public int getInt(String edtValue, int defaultValue) {
        int value = defaultValue;

        if (edtValue != null) {
            try {
                value = Integer.parseInt(edtValue);
            } catch (NumberFormatException e) {
                value = defaultValue;
            }
        }

        return value;
    }
}
