package nl.ralphrouwen.locationawareapp.Helper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import nl.ralphrouwen.locationawareapp.Helper.listeners.RestProviderListener;

public class RestProvider {
    private static RestProvider Instance = null;
    private RequestQueue requestqueue;

    private RestProvider(Context context) {
        requestqueue = Volley.newRequestQueue(context);
    }

    public static RestProvider getInstance(Context context) {
        if (Instance == null)
            Instance = new RestProvider(context);
        return Instance;
    }

    public void getRequest(String url, final RestProviderListener listener, boolean isObject) {
        if (isObject) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url, null,
                    listener::onRequestObjectAvailible,
                    listener::onRequestError
            );
            requestqueue.add(jsonObjectRequest);
        } else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    url,
                    response -> {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("response", response);
                            listener.onRequestObjectAvailible(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    listener::onRequestError
            );
            requestqueue.add(jsonArrayRequest);
        }
    }
}
