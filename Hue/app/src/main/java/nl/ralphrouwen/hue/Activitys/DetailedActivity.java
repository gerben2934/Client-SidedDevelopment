package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.EXTRA_URL;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        Bridge bridge = intent.getParcelableExtra(EXTRA_URL);

        Log.i("bridge", bridge.toString());

    }
}
