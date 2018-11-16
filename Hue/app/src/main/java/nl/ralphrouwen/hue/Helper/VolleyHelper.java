package nl.ralphrouwen.hue.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;

public class VolleyHelper {

    private static VolleyHelper Instance = null;
    private Context context;


    public VolleyHelper(Context context) {
        this.context = context;
    }

    public static VolleyHelper getInstance(Context context) {
        if (Instance == null) {
            Instance = new VolleyHelper(context);
        }
        return Instance;
    }

    public void getRequest(String url, final RequestListener listener, final nl.ralphrouwen.hue.Models.Response requestResponse)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        listener.onRequestObjectAvailible(response, requestResponse);
                        Log.i("HUE", "OK");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i("HUE","NOK");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    public void putArrayRequest(String url, final RequestListener listener, final nl.ralphrouwen.hue.Models.Response responserequest, JSONObject jsonObject)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                listener.onRequestArrayAvailible(response, responserequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }


    public void getLights(Bridge bridge, RequestListener listener)
    {
        String url = bridge.getIp() + "/api/" + bridge.getToken() + "/lights";
        getRequest(url, listener, nl.ralphrouwen.hue.Models.Response.GETLICHTS);
    }

    public void changeLight(Bridge bridge, Light light, RequestListener listener, int bri, int hue, int sat, boolean status)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("on",status);
            jsonObject.put("bri", bri);
            jsonObject.put("hue", hue);
            jsonObject.put("sat",sat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = bridge.getIp() + "/api/" + bridge.getToken() + "/lights/" + light.getId() + "/state";

        putArrayRequest(url, listener, nl.ralphrouwen.hue.Models.Response.SETLIGHT,jsonObject);
    }
}


