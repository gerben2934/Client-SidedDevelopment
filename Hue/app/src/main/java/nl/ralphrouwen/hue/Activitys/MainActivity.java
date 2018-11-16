package nl.ralphrouwen.hue.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.ralphrouwen.hue.Helper.LightManager;
import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.R;
import nl.ralphrouwen.hue.Adapters.BridgeRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements RequestListener {

    ArrayList<Light> lights;
    VolleyHelper api;
    ArrayList<Bridge> bridges = new ArrayList<Bridge>();
    private RecyclerView mRecyclerView;
    private BridgeRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String EXTRA_URL = "url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new VolleyHelper(getApplicationContext());
        bridges.add(new Bridge(1, "Emulator Ralph Thuis", "http://192.168.178.29", ""));
        bridges.add(new Bridge(2, "Emulator Ralph school", "http://145.49.45.24", ""));
//        api = new VolleyHelper(getApplicationContext());
        bridges.add(new Bridge("Emulator Ralph Thuis", "http://192.168.178.29", ""));
        bridges.add(new Bridge("Emulator Ralph school", "http://145.49.45.24", "7e720a9ef0102f25221c56f91c7f43f"));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter
        mAdapter = new BridgeRecyclerAdapter(this, bridges);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.setOnItemClickListener(MainActivity.this);

//        VolleyHelper api = new VolleyHelper(getApplicationContext());
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETLICHTS:
                lights = LightManager.sortLights(response);
//                api.changeLight(bridge,lights.get(0), this,20,200,200,true);
                break;
        }
    }

/*    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailedActivity.class);
        Bridge clickedBridge = bridges.get(position);

        //use parcable --> from Mural object.
        detailIntent.putExtra(EXTRA_URL, (Parcelable)clickedBridge);
        startActivity(detailIntent);
    }*/

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