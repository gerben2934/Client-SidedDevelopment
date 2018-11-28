package nl.ralphrouwen.hue.Activitys;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Data.DataUtil;
import nl.ralphrouwen.hue.Data.DatabaseHandler;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.R;
import nl.ralphrouwen.hue.Adapters.BridgeRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements AddBridgeActivity.BridgeFragmentListener {

    ArrayList<Bridge> bridges = new ArrayList<Bridge>();
    private RecyclerView mRecyclerView;
    private BridgeRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String BRIDGE_URL = "bridgeURL";
    public static final String LIGHT_URL = "lightURL";
    FloatingActionButton addBridgeButton;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        addBridgeButton = findViewById(R.id.floatingActionButton);

        deleteDatabase(DataUtil.DB_NAME);
//
        if(db.checkIfEmpty())
        {
            db.addBridge(new Bridge(1,"Emulator Ralph Thuis", "http://192.168.178.45", "ba78860f274f0060f319645406c561b"));
            db.addBridge(new Bridge(5, "Ralph thuis hue","http://192.168.178.90", "DmznyFSbvpdIpzCIB0cYAppyi18LJPsOog5A8CHD"));
            db.addBridge(new Bridge(2,"Emulator Ralph school", "http://145.49.45.24", "0296724fbea1bc84173967f322e34b7"));
            db.addBridge(new Bridge(3, "Emulator Gerben School", "http://145.49.2.189", "c8ccceb2a8224993626a649fef8b048"));
            db.addBridge(new Bridge(4, "Emulator Gerben Thuis", "http://192.168.101.1", "f01a104869921887ac45229fc2040ef"));
            db.addBridge(new Bridge(4,"Emulator Ralph iMac", "http://192.168.178.23", "6446fb88b5a083eb630138080276167"));
        }
        db.addBridge(new Bridge(4,"Emulator Ralph iMac", "http://192.168.178.23", "6446fb88b5a083eb630138080276167"));

        bridges = db.getAllBridges();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter
        mAdapter = new BridgeRecyclerAdapter(this, bridges);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.setOnItemClickListener(MainActivity.this);

        addBridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.addToBackStack(null);

//                DialogFragment fragment = AddBridgeActivity.newInstance();
//                fragment.show(ft,"dialog");

                AddBridgeActivity fragment2 = new AddBridgeActivity();

                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                fragment2.show(ft,"joejoe");
            }
        });
    }

    @Override
    public void sendInput(String name, String ip, String token) {
        int id = bridges.size() + 1;
        Bridge bridge = new Bridge(id,name,ip,token);
        db.addBridge(bridge);
        bridges.add(bridge);
        mAdapter.notifyDataSetChanged();
    }

    public void removeBridge(int id)
    {
        db.removeBridge(id);
        bridges.remove(id);
        mAdapter.notifyDataSetChanged();
    }
}