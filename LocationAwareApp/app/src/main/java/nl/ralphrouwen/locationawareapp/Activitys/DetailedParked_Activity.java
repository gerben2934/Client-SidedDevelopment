package nl.ralphrouwen.locationawareapp.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;

public class DetailedParked_Activity extends AppCompatActivity {

    public Parked parked;

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
        setContentView(R.layout.activity_detailed_parked);

        Intent intent = getIntent();
        parked = intent.getParcelableExtra(PARKED_URL);
        BindComponents();
        SetTextViews();
    }

    public void BindComponents()
    {
        mapView = findViewById(R.id.parkedDetailed_map);
        streetName = findViewById(R.id.parkedDetailed_streetName);
        startTime = findViewById(R.id.parkedDetailed_startTime);
        endTime = findViewById(R.id.parkedDetailed_endTime);
        deltaTime = findViewById(R.id.parkedDetailed_deltaTime);
    }

    public void SetTextViews() {
        streetName.setText(parked.getStreetName());
        date.setText(dateFormatter());
        startTime.setText(timeFormatter(true));
        endTime.setText(timeFormatter(false));
        deltaTime.setText(calculateTimeSpan());
    }

    public String calculateTimeSpan()
    {
        DateTime start = parked.getStartTime();
        DateTime end = parked.getEndTime();
        Period timeSpan = new Period(start, end);
        String elapsed = "";
        if (timeSpan.getDays() > 0) {
            elapsed += R.string.days + " " + timeSpan.getDays();
        }
        if (timeSpan.getHours() > 0)
        {
            elapsed += timeSpan.getHours();
        }
        if (timeSpan.getMinutes() > 0)
        {
            elapsed += ":" + timeSpan.getMinutes();
        }
        else {
            elapsed = "Auto geparkeerd";
        }
        elapsed = R.string.timeParked + ": " + elapsed;
        return elapsed;
    }

    public String dateFormatter()
    {
        DateTime dateDateTime = parked.getStartTime();
        String dateString = dateDateTime.toString("dd/MMM/yyyy");
        return dateString;
    }

    public String timeFormatter(boolean isStart)
    {
        String time = "";

        if(isStart) {
            DateTime dateDateTime1 = parked.getStartTime();
            time += R.string.start_time + " " + dateDateTime1.toString("mm/HH");
        }
        else
        {
            DateTime dateDateTime2 = parked.getEndTime();
            time += R.string.end_time + " " + dateDateTime2.toString("mm/HH");
        }
        return time;
    }
}
