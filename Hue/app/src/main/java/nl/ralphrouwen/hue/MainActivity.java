package nl.ralphrouwen.hue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nl.ralphrouwen.hue.Helper.LightManager;
import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;



public class MainActivity extends AppCompatActivity implements RequestListener {

    ArrayList<Light> lights;
    VolleyHelper api;
    Bridge bridge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new VolleyHelper(getApplicationContext());
        bridge = new Bridge("Emulator", "http://192.168.178.29", "58cb70cf1396b156b820449aaf53e43");

        api.getLights(bridge, this);
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype){
            case GETLICHTS:
                Log.i("1234", response.toString());
                lights = LightManager.sortLights(response);
                api.changeLight(bridge,lights.get(0), this,20,200,200,true);

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