package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Adapters.BridgeRecyclerAdapter;
import nl.ralphrouwen.hue.Adapters.LightRecyclerAdapter;
import nl.ralphrouwen.hue.Helper.LightManager;
import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;

public class BridgeActivity extends AppCompatActivity implements RequestListener {

    public Bridge bridge;
    VolleyHelper api;
    ArrayList<Light> lights;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LightRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        Intent intent = getIntent();
        bridge = intent.getParcelableExtra(BRIDGE_URL);

         api = VolleyHelper.getInstance(getApplicationContext());
         api.getLights(bridge, this);

//        api = new VolleyHelper(getApplicationContext());
//        api.getLights(bridge,this);

        Log.i("bridge", bridge.toString());
        Log.i("bridge", "hallo");

    }

    public void createCardView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.bridgeActivity_RecycleView);
        mRecyclerView.setHasFixedSize(true);

        //specify an adapter
        mAdapter = new LightRecyclerAdapter(this, lights);
        mRecyclerView.setAdapter(mAdapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }


    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETLICHTS:
                lights = LightManager.sortLights(response);
                createCardView();
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
