package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.ralphrouwen.hue.Helper.LightManager;
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
    public Light light1;
    VolleyHelper api;

    Bridge bridge;

    RequestListener request;

    TextView statusTV;
    TextView brightnessTV;
    TextView lightname;
    Switch lightSwitch;
    SeekBar lightSeekbar;

    ColorPickerView colorPickerView;
    int finalColor;

    Button testbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_detailed);

        request = this;

        Intent intent = getIntent();
        light1 = intent.getParcelableExtra(LIGHT_URL);
        bridge = intent.getParcelableExtra(BRIDGE_URL);
        api = VolleyHelper.getInstance(getApplicationContext());
        int id = light1.getId();
        System.out.println("ID: " + id);
        api.getLight(bridge, this, id);
        bindComponents();

        //setTextViews();

        if(!lightSwitch.isChecked())
        {
            lightSeekbar.setEnabled(false);
        }

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                light.setHue(finalColor);
                api.changeLight(bridge, light, request, light.getBrightness(), finalColor, light.getSaturation(), isChecked);
                lightSeekbar.setEnabled(isChecked);
                colorPickerView.setEnabled(isChecked);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(color));
            }

            int[] ints = colorHex(color);
            int r = ints[1];
            int g = ints[2];
            int b = ints[3];

            float[] hsb = new float[3];
            Color.RGBToHSV(r, g, b, hsb);
            float x = hsb[0];
            int y = (int)x;
            finalColor = y * (65535/360);
            //System.out.println("Light: " + light.toString());
            //System.out.println("Final color: " + finalColor);
            //api.changeLight(bridge, light, request, light.getBrightness(), finalColor, light.getSaturation(), lightSwitch.isChecked());
        });
    }

    private void bindComponents() {
        statusTV = findViewById(R.id.lightDetailedActivity_statusTV);
        brightnessTV = findViewById(R.id.lightDetailedActivity_brightnessTV);

        lightname = findViewById(R.id.lightDetailedActivity_lightName);
        lightSwitch = findViewById(R.id.lightDetailedActivity_lightSwitch);
        lightSeekbar = findViewById(R.id.lightDetailedActivity_lightBrightness);

        colorPickerView = findViewById(R.id.colorPicker);
    }

    private void setTextViews() {
        lightname.setText(light.getName());
        lightSwitch.setChecked(light.isOn());
        lightSeekbar.setThumbOffset(0);
        lightSeekbar.setMin(0);
        lightSeekbar.setMax(254);
        lightSeekbar.setProgress(light.getBrightness());
        System.out.println("Hue: " + light.getHue());
        float hue = (float)(light.getHue() / (65535/360));
        float saturation = (float)(light.getSaturation() / 254);
        float brightness = (float)(light.getBrightness() / 254);
        float[] a = {hue, brightness, saturation};
        System.out.println("Hue: " + hue + " brightness: " + brightness + ", saturation: " + saturation);
        //System.out.println(Color.);
        colorPickerView.setInitialColor(Color.HSVToColor(a));
    }

    private int[] colorHex(int color) {
        int a = Color.alpha(color); //niet nodig
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int[] integers = {a, r, g, b};
        return integers;
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETLIGHT:
                System.out.println("OnRequestAvailable!");
                light = LightManager.getLight(response, light1.getId());
                setTextViews();
                break;
        }
    }

    @Override
    public void onRequestArrayAvailible(JSONArray response, Response responsetype) {

    }

    @Override
    public void onRequestError(Error error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        request = this;
        //System.out.println("On resume!");

        Intent intent = getIntent();
        //light = intent.getParcelableExtra(LIGHT_URL);
        //bridge = intent.getParcelableExtra(BRIDGE_URL);
        api = VolleyHelper.getInstance(getApplicationContext());
        api.getLight(bridge, this, light1.getId());
    }
}
