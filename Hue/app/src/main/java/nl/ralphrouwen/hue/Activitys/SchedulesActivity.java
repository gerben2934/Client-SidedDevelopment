package nl.ralphrouwen.hue.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Adapters.SchedulesRecylerAdapter;
import nl.ralphrouwen.hue.Helper.LightManager;
import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.Models.Schedule;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;
import static nl.ralphrouwen.hue.Activitys.MainActivity.LIGHT_URL;

public class SchedulesActivity extends AppCompatActivity implements RequestListener, ScheduleDetailled.OnFragmentInteractionListener {

    public Bridge bridge;
    VolleyHelper api;
    ArrayList<Light> lights;
    ArrayList<Schedule> schedules;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SchedulesRecylerAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;
    private RequestListener request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);
        request = this;

        Intent intent = getIntent();
        bridge = intent.getParcelableExtra(BRIDGE_URL);
        lights = intent.getParcelableArrayListExtra(LIGHT_URL);

        api = VolleyHelper.getInstance(getApplicationContext());
        api.getSchedules(bridge,request);
    }

    public void createCardView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.schedulesActivity_RecycleView);
        mRecyclerView.setHasFixedSize(true);

//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer_schedules);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter
        mAdapter = new SchedulesRecylerAdapter(this,schedules);
        mRecyclerView.setAdapter(mAdapter);


//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.i("Refresh", "refrsh");
////                api.getLights(bridge, request);
////                mAdapter.clear();
////                // ...the data has come back, add new items to your adapter...
////                mAdapter = new SchedulesRecylerAdapter(getApplicationContext(), schedules, lights, bridge);
////                // Now we call setRefreshing(false) to signal refresh has finished
////                swipeContainer.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {
        switch (responsetype) {
            case GETSCHEDULES:
                schedules = LightManager.sortSchedules(response);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
