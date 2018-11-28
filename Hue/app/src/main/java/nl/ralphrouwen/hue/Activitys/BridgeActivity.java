package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Adapters.BridgeRecyclerAdapter;
import nl.ralphrouwen.hue.Adapters.LightRecyclerAdapter;
import nl.ralphrouwen.hue.Helper.LightController;
import nl.ralphrouwen.hue.Helper.LightManager;
import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;
import static nl.ralphrouwen.hue.Activitys.MainActivity.LIGHT_URL;

public class BridgeActivity extends AppCompatActivity implements RequestListener {

    public Bridge bridge;
    VolleyHelper api;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LightRecyclerAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;
    private RequestListener request;
    private FloatingActionButton deleteButton;
    private LightController lightController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        Intent intent = getIntent();
        bridge = intent.getParcelableExtra(BRIDGE_URL);

        api = VolleyHelper.getInstance(getApplicationContext());
        api.getLights(bridge, this);
        request = this;
        lightController = LightController.getInstance();
        createCardView();
    }

    public void createCardView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.bridgeActivity_RecycleView);
        mRecyclerView.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //specify an adapter
        mAdapter = new LightRecyclerAdapter(this, bridge);
        mRecyclerView.setAdapter(mAdapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Button button = findViewById(R.id.schedulesButton);
        Switch alllightswitch = findViewById(R.id.allLightSwitch);

        alllightswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lightController.setLights(lightController.getLights());
                for (Light light : lightController.getLights()) {
                    api.changeLight(bridge, light, request, light.getBrightness(), light.getHue(), light.getSaturation(), isChecked);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SchedulesActivity.class);
                intent.putExtra(BRIDGE_URL, (Parcelable) bridge);
                intent.putParcelableArrayListExtra(LIGHT_URL, lightController.getLights());
                getApplicationContext().startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                api.getLights(bridge, request);
            }
        });
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETLICHTS:
                System.out.println("OnRequestAvailable!");
                lightController.setLights(LightManager.sortLights(response));
                //mAdapter.clear();
                //mAdapter.addAll(lights);
/*
                // ...the data has come back, add new items to your adapter...
                mAdapter = new LightRecyclerAdapter(getApplicationContext(), lights, bridge);
                mRecyclerView.setAdapter(mAdapter);

                // Now we call setRefreshing(false) to signal refresh has finished
*/
                swipeContainer.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
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
    public void onResume()
    {
        super.onResume();
        request = this;
        //System.out.println("On resume!");

        Intent intent = getIntent();
        //light = intent.getParcelableExtra(LIGHT_URL);
        //bridge = intent.getParcelableExtra(BRIDGE_URL);
        api = VolleyHelper.getInstance(getApplicationContext());
        api.getLights(bridge, this);
    }
}
