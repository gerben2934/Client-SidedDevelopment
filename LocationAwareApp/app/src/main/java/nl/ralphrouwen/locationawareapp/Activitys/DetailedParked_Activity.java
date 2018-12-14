package nl.ralphrouwen.locationawareapp.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;

public class DetailedParked_Activity extends AppCompatActivity {

    public Parked parked;
    private Context context;

    MapView mapView;
    TextView streetName;
    TextView date; //dd:mm:yyyy (01-01-2018
    TextView startTime; //startTime (hh:mm)
    TextView endTime; //endTime (hh:mm)
    TextView deltaTime;  // (end - start) (deltaTime (hh:mm);
    Period timeParked;
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

        //map toevoegen
        //pointer toevoegen op map van parked.getlocation();
        //map verbieden van alles behalve in en uitzoomen!
    }

    public void BindComponents()
    {
        mapView = findViewById(R.id.parkedDetailed_map);
        streetName = findViewById(R.id.parkedDetailed_streetName);
        date = findViewById(R.id.parkedDetailed_date);
        startTime = findViewById(R.id.parkedDetailed_startTime);
        endTime = findViewById(R.id.parkedDetailed_endTime);
        deltaTime = findViewById(R.id.parkedDetailed_deltaTime);
    }

    public void SetTextViews() {
        streetName.setText(parked.getStreetName());
        date.setText(dateFormatter());
        startTime.setText(timeFormatter(true));
        endTime.setText(timeFormatter(false));
        String elapsed = context.getResources().getString(R.string.timeParked) + parked.getParkedTime(context);;
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
}
