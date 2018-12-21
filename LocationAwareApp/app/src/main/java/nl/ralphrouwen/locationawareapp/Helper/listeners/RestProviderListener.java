package nl.ralphrouwen.locationawareapp.Helper.listeners;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RestProviderListener {
    void onRequestObjectAvailible(JSONObject response);
    void onRequestError(VolleyError error);
}
