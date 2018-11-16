package nl.ralphrouwen.hue.Helper;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.ralphrouwen.hue.Models.Response;

public interface RequestListener {

    public void onRequestObjectAvailible(JSONObject response, Response responsetype);

    public void onRequestArrayAvailible(JSONArray response, Response responsetype);

    public void onRequestError(Error error);

}
