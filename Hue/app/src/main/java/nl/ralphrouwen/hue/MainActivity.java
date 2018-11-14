package nl.ralphrouwen.hue;

import android.service.autofill.Dataset;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import com.android.volley.Request;

import nl.ralphrouwen.hue.Helper.VolleyHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[] dataSet = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter
        mAdapter = new MyAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        VolleyHelper api = new VolleyHelper(getApplicationContext());

        //String requestString = "http://localhost/api/235ebf139caff745ab2ac6aba8e7153/lights/2/state";
        String requestString = "http://localhost/api/235ebf139caff745ab2ac6aba8e7153";
        //String stringbody = "{\"on\": true}";
        String stringbody ="";
        int requestMethod = Request.Method.GET;
        //String test = api.volleyRequest(requestString, stringbody, requestMethod);
        api.getProjects();
//        Log.d("TEST", test);

//        {
//            "on": false
//        }

    }
}