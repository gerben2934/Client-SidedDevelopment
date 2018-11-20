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

public class MainActivity extends AppCompatActivity {

    ArrayList<Bridge> bridges = new ArrayList<Bridge>();
    private RecyclerView mRecyclerView;
    private BridgeRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String BRIDGE_URL = "bridgeURL";
    public static final String LIGHT_URL = "lightURL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bridges.add(new Bridge(1,"Emulator Ralph Thuis", "http://192.168.178.29", ""));
        bridges.add(new Bridge(2,"Emulator Ralph school", "http://145.49.45.24", "97432bff21e2641012f54eb62615bea"));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter
        mAdapter = new BridgeRecyclerAdapter(this, bridges);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.setOnItemClickListener(MainActivity.this);
    }
}