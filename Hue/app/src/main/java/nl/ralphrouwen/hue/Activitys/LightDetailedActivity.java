package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.LIGHT_URL;

public class LightDetailedActivity extends AppCompatActivity {

    public Light light;
    VolleyHelper api;

    TextView statusTV;
    TextView brightnessTV;
    TextView colorTV;

    TextView lightname;
    Switch lightSwitch;
    SeekBar lightSeekbar;
    TextView lightHue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_detailed);

        Intent intent = getIntent();
        light = intent.getParcelableExtra(LIGHT_URL);

        api = VolleyHelper.getInstance(getApplicationContext());

        bindComponents();
        setTextViews();
    }

    private void bindComponents() {
        statusTV = findViewById(R.id.lightDetailedActivity_statusTV);
        brightnessTV = findViewById(R.id.lightDetailedActivity_brightnessTV);
        colorTV = findViewById(R.id.lightDetailedActivity_colorTV);

        lightname = findViewById(R.id.lightDetailedActivity_lightName);
        lightSwitch = findViewById(R.id.lightDetailedActivity_lightSwitch);
        lightSeekbar = findViewById(R.id.recycleViewItem_SeekBar);

        //Colorpicker here!
    }

    private void setTextViews() {
        lightname.setText(light.getName());
        lightSwitch.setEnabled(light.isOn());
    }
}
