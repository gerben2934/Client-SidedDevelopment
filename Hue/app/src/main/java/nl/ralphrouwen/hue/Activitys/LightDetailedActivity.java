package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.R;
import top.defaults.colorpicker.ColorPickerView;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;
import static nl.ralphrouwen.hue.Activitys.MainActivity.LIGHT_URL;

public class LightDetailedActivity extends AppCompatActivity implements RequestListener {

    public Light light;
    VolleyHelper api;

    Bridge bridge;

    RequestListener request;

    TextView statusTV;
    TextView brightnessTV;
    TextView colorTV;

    TextView lightname;
    Switch lightSwitch;
    SeekBar lightSeekbar;
    TextView lightHue;

    ColorPickerView colorPickerView;
    View pickedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_detailed);

        request = this;

        Intent intent = getIntent();
        light = intent.getParcelableExtra(LIGHT_URL);
        bridge = intent.getParcelableExtra(BRIDGE_URL);

        api = VolleyHelper.getInstance(getApplicationContext());

        bindComponents();
        setTextViews();

        if(!lightSwitch.isChecked())
        {
            lightSeekbar.setEnabled(false);
        }

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                api.changeLight(bridge, light, request, light.getBrightness(), light.getHue(), light.getSaturation(), isChecked);
                lightSeekbar.setEnabled(isChecked);
            }
        });

        lightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                api.changeLight(bridge, light, request, progress, light.getHue(), light.getSaturation(), true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        colorPickerView.subscribe((color, fromUser) -> {
            pickedColor.setBackgroundColor(color);

            // Hier komt color binnen die geselecteerd is!!!!!
            // Color waarde omrekenen naar iets wat hue api snapt en dan vervolgens die waarde bij kleurwaarde meegeven :

            //api.changeLight(bridge, light, request, light.brightness, kleurwaarde, light.getSaturation(), true);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(color));
            }
        });

    }

    private void bindComponents() {
        statusTV = findViewById(R.id.lightDetailedActivity_statusTV);
        brightnessTV = findViewById(R.id.lightDetailedActivity_brightnessTV);
        colorTV = findViewById(R.id.lightDetailedActivity_colorTV);

        lightname = findViewById(R.id.lightDetailedActivity_lightName);
        lightSwitch = findViewById(R.id.lightDetailedActivity_lightSwitch);
        lightSeekbar = findViewById(R.id.lightDetailedActivity_lightBrightness);

        pickedColor = findViewById(R.id.pickedColor);
        colorPickerView = findViewById(R.id.colorPicker);

    }

    private void setTextViews() {
        lightname.setText(light.getName());

        lightSwitch.setEnabled(true);
        lightSwitch.setChecked(light.isOn());

        lightSeekbar.setMax(254);
        lightSeekbar.setMin(0);
        lightSeekbar.setProgress(light.brightness);
    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {

    }

    @Override
    public void onRequestArrayAvailible(JSONArray response, Response responsetype) {

    }

    @Override
    public void onRequestError(Error error) {

    }
}
