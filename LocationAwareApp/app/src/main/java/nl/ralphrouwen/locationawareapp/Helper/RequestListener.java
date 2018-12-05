package nl.ralphrouwen.locationawareapp.Helper;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RequestListener {

    public void onRequestObjectAvailible(JSONObject response, Response responseType);

    public void onRequestArrayAvailible(JSONArray response, Response responseType);

}
