package nl.ralphrouwen.locationawareapp.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.Period;

import nl.ralphrouwen.locationawareapp.Fragments.DetailedParkedMapFragment;
import nl.ralphrouwen.locationawareapp.Fragments.MapFragment;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;

public class DetailedParked_Activity extends AppCompatActivity implements nl.ralphrouwen.locationawareapp.Fragments.MapFragment.OnFragmentInteractionListener {

    public Parked parked;
    private Context context;

    private TextView streetName;
    private TextView date; //dd:mm:yyyy (01-01-2018
    private TextView startTime; //startTime (hh:mm)
    private TextView endTime; //endTime (hh:mm)
    private TextView deltaTime;  // (end - start) (deltaTime (hh:mm);
    private Period timeParked;
    private View detailedMapFragment;
    private Button navigateButton;
    //TextView timesParked; (sort on streetName?)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_detailed_parked);

        Intent intent = getIntent();
        parked = intent.getParcelableExtra(PARKED_URL);
        BindComponents();
        SetTextViews();
        buildMapFragment();
    }

    private void buildMapFragment()
    {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        DetailedParkedMapFragment ff = DetailedParkedMapFragment.newInstance(parked);
        fragmentManager.beginTransaction().replace(R.id.mapdetailed_fragment, ff).commit();
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng destination = new LatLng(parked.getLatitude(), parked.getLongitude());
                LatLng orgin = MapFragment.getMyLocation();
                ff.getRoute(orgin, destination);
            }
        });
    }

    public void BindComponents()
    {
        //detailedMapFragment = findViewById(R.id.mapdetailed_fragment);
        streetName = findViewById(R.id.parkedDetailed_streetName);
        streetName.setBackground(getResources().getDrawable(R.drawable.textviewborder2));
        date = findViewById(R.id.parkedDetailed_date);
        date.setBackground(getResources().getDrawable(R.drawable.textviewborder2));
        startTime = findViewById(R.id.parkedDetailed_startTime);
        startTime.setBackground(getResources().getDrawable(R.drawable.textviewborder2));
        endTime = findViewById(R.id.parkedDetailed_endTime);
        endTime.setBackground(getResources().getDrawable(R.drawable.textviewborder2));
        deltaTime = findViewById(R.id.parkedDetailed_deltaTime);
        deltaTime.setBackground(getResources().getDrawable(R.drawable.textviewborder2));
        navigateButton = findViewById(R.id.detaillednavigateID);

        if(!parked.isValid())
        {
            navigateButton.setVisibility(View.INVISIBLE);
        }
        else {
            navigateButton.setVisibility(View.VISIBLE);
        }
    }

    public void SetTextViews() {
        //detailedMapFragment.inflate
        streetName.setText(context.getResources().getString(R.string.location) + " " + parked.getStreetName());
        date.setText(dateFormatter());
        startTime.setText(timeFormatter(true));
        endTime.setText(timeFormatter(false));
        String elapsed = context.getResources().getString(R.string.timeParked) + parked.getParkedTime(context);
        deltaTime.setText(elapsed);
    }

    public String dateFormatter()
    {
        String date = "";
        DateTime dateDateTime = parked.getStartTime();
        date += context.getResources().getString(R.string.date) + dateDateTime.toString(" dd/MMM/yyyy");
        return date;
    }

    public String timeFormatter(boolean isStart)
    {
        String time = "";

        if(isStart) {
            DateTime dateDateTime1 = parked.getStartTime();
            Log.i("Datetime: ", " Datetime: " + parked.getStartTime());
            time += context.getResources().getString(R.string.begin) + " " + dateDateTime1.toString("dd/MMM, HH:mm");
        }
        else
        {
            DateTime dateDateTime2 = parked.getEndTime();
            time += context.getResources().getString(R.string.end) + " " + dateDateTime2.toString("dd/MMM, HH:mm");
        }
        return time;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

/*    @Override
    public void onBackPressed() {
        Intent startMainActivity = new Intent(context, MainActivity.class);
        startMainActivity.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(startMainActivity);
    }*/
}
