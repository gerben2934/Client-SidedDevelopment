package nl.ralphrouwen.hue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;

import nl.ralphrouwen.hue.Helper.VolleyHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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