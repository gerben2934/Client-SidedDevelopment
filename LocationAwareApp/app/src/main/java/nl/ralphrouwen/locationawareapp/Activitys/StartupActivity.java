package nl.ralphrouwen.locationawareapp.Activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nl.ralphrouwen.locationawareapp.R;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(StartupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
