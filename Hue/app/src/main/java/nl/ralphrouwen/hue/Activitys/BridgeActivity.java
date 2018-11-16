package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Helper.LightManager;
import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.EXTRA_URL;

public class BridgeActivity extends AppCompatActivity implements RequestListener {

    public Bridge bridge;
    VolleyHelper api;
    ArrayList<Light> lights;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        Intent intent = getIntent();
        bridge = intent.getParcelableExtra(EXTRA_URL);

        api = new VolleyHelper(getApplicationContext());
        api.getLights(bridge,this);

        Log.i("bridge", bridge.toString());
        Log.i("bridge", "hallo");
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETLICHTS:
                lights = LightManager.sortLights(response);
                break;
        }
    }

    @Override
    public void onRequestArrayAvailible(JSONArray response, Response responsetype) {

    }

    @Override
    public void onRequestError(Error error) {

    }
}
