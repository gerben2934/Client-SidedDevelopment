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
    ArrayList<Light> lights;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LightRecyclerAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;
    private RequestListener request;
    private FloatingActionButton deleteButton;
    Switch alllightswitch;
    Button button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        intent = getIntent();
        bridge = intent.getParcelableExtra(BRIDGE_URL);
        api = VolleyHelper.getInstance(getApplicationContext());
        api.getLights(bridge, this);
        request = this;
    }

    public void createCardView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.bridgeActivity_RecycleView);
        mRecyclerView.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        //specify an adapter
        mAdapter = new LightRecyclerAdapter(this, lights, bridge);
        mRecyclerView.setAdapter(mAdapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        button = findViewById(R.id.schedulesButton);
        alllightswitch = findViewById(R.id.allLightSwitch);

        for (Light light : lights) {
            boolean check = light.isOn();
            if(check == true)
            {
                alllightswitch.setChecked(check);
            }
            break;
        }

        alllightswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Light light : lights) {
                    light.setOn(isChecked);
                    api.changeLight(bridge, light, request, light.getBrightness(), light.getHue(), light.getSaturation(), isChecked);
                }
//                api.getLights(bridge, request);
//                mAdapter.clear();
//                // ...the data has come back, add new items to your adapter...
//                mAdapter = new LightRecyclerAdapter(getApplicationContext(), lights, bridge);
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SchedulesActivity.class);
                intent.putExtra(BRIDGE_URL, (Parcelable) bridge);
                intent.putParcelableArrayListExtra(LIGHT_URL, lights);
                getApplicationContext().startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                api.getLights(bridge, request);
                // Now we call setRefreshing(false) to signal refresh has finished
            }
        });
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETLICHTS:
                lights = LightManager.sortLights(response);
                // ...the data has come back, add new items to your adapter...
//                mAdapter = new LightRecyclerAdapter(getApplicationContext(), lights, bridge);
                createCardView();
                swipeContainer.setRefreshing(false);
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
        api.getLights(bridge, this);
    }
}
