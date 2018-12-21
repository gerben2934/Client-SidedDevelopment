package nl.ralphrouwen.locationawareapp.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import nl.ralphrouwen.locationawareapp.Helper.interfaces.DirectionsProvider;
import nl.ralphrouwen.locationawareapp.Helper.listeners.DirectionsProviderListener;
import nl.ralphrouwen.locationawareapp.Helper.listeners.RestProviderListener;

public class MovieCastDirectionsProvider implements DirectionsProvider {
    private static String BASE_URL = "https://maps.moviecast.io/directions";
    private RestProvider restProvider;
    private String apiKey;

    public MovieCastDirectionsProvider(Context context)
    {
        apiKey = "176d6aa5-ccb8-4774-a1c9-bd2bb89906d7";
        restProvider = RestProvider.getInstance(context);
    }

    private List<LatLng> getPolyLineCoordinates(JSONObject rootObject) throws JSONException {
        String encodedPolyLine = rootObject.getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("overview_polyline")
                .getString("points");
        return DecoderHelper.decodePolyLine(encodedPolyLine);
    }

    @Override
    public void queueDirectionsRequest(LatLng origin, LatLng destination, DirectionsProviderListener listener) {
        String request = DirectionBuilderHelper.buildStandaardRequest(BASE_URL,apiKey, origin, destination);
        Log.e("DATARECEIVEDDIRECTIONS", String.valueOf(request));
        restProvider.getRequest(request, new RestProviderListener() {
            @Override
            public void onRequestObjectAvailible(JSONObject response) {
                try {
                    List<LatLng> subDirectionList = getPolyLineCoordinates(response);
                    listener.onReceivedDirections(subDirectionList);
                    Log.e("DATARECEIVEDDIRECTIONS", String.valueOf(response));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response!!!", String.valueOf(response));
            }

            @Override
            public void onRequestError(VolleyError error) {

            }
        }, true);

    }
}
