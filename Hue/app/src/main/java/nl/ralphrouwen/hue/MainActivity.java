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
    ArrayList<Bridge> bridges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new VolleyHelper(getApplicationContext());
        bridges.add(new Bridge("Emulator Ralph Thuis", "http://192.168.178.29", ""));
        bridges.add(new Bridge("Emulator Ralph school", "http://145.49.45.24", ""));


//        api.getLights(bridge, this);
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype){
            case GETLICHTS:
                lights = LightManager.sortLights(response);
//                api.changeLight(bridge,lights.get(0), this,20,200,200,true);
                break;
        }
    }

    @Override
    public void onRequestArrayAvailible(JSONArray response, Response responsetype) {
        switch(responsetype){
            case SETLIGHT:
                boolean test = LightManager.handleSetLights(response);
                Log.i("SETLIGHT", response.toString());
                break;
        }
    }

    @Override
    public void onRequestError(Error error) {

    }
}