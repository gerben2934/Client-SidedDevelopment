package nl.ralphrouwen.locationawareapp.Activitys;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.util.ArrayList;

import nl.ralphrouwen.locationawareapp.Adapters.RecyclerViewAdapter;
import nl.ralphrouwen.locationawareapp.Fragments.HistoryFragment;
import nl.ralphrouwen.locationawareapp.Fragments.MapFragment;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    public static final String PARKED_URL = "parkedURL";
    ArrayList<Parked> parkeds = new ArrayList<Parked>();
    ImageButton parkbutton;
    boolean parkButtonPressed;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parkButtonPressed = false;
        //generateParkeds();
        Log.d("length:", "count() " + parkeds.size());

        parkbutton = findViewById(R.id.parkbutton);

/*        mRecyclerView = (RecyclerView) findViewById(R.id.);
        mRecyclerView.setHasFixedSize(true);*/

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter
       /* mAdapter = new RecyclerViewAdapter(this, parkeds);
        mRecyclerView.setAdapter(mAdapter);*/

/*        FragmentActivity MapFragment = new FragmentActivity();
        MapFragment.findViewById(R.id.)*/
    }

    public void parkButtonPressed(View view)
    {
        if(!parkButtonPressed)
        {
            parkbutton.setImageResource(R.drawable.parkbutton3);
            parkButtonPressed = true;
        }else{
            parkbutton.setImageResource(R.drawable.parkbutton5);
            parkButtonPressed = false;
        }
        Log.i("HALLO", "puttonpressed");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
