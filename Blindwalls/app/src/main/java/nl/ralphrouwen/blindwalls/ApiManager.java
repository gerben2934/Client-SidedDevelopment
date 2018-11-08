package nl.ralphrouwen.blindwalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class ApiManager {

    Context context;
    RequestQueue queue;
    ApiListener listener;

    public ApiManager(Context context, ApiListener listener)
    {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
        this.listener = listener;
    }

    public void getData()
    {
        final String url = "https://api.blindwalls.gallery/apiv2/murals";

        JsonArrayRequest request = new JsonArrayRequest (

                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEY_TAG", response.toString());

                        try {
                            // JSONArray jsonArray = new JSONArray(response);
                            for( int idx = 0; idx < response.length(); idx++) {
                                String author = response.getJSONObject(idx).getString("author");
                                String year = response.getJSONObject(idx).getString("year");
                                String descNL = response.getJSONObject(idx).getJSONObject("description").getString("nl");
                                String descENG = response.getJSONObject(idx).getJSONObject("description").getString("en");
                                String address = response.getJSONObject(idx).getString("address");
                                float latitude = (float) response.getJSONObject(idx).getDouble("latitude");
                                float longitude = (float) response.getJSONObject(idx).getDouble("longitude");

                                JSONArray images = response.getJSONObject(idx).getJSONArray("images");


                                int index = new Random().nextInt(images.length());

                                String imageUrl = "https://api.blindwalls.gallery/" +
                                        images.getJSONObject(index).getString("url");

                                Mural mural = new Mural(author, descNL, descENG, imageUrl, year, address, longitude, latitude);

                                listener.onMuralAvailable(mural);

                                Log.d("VOLLEY_TAG", mural.toString());
                            }
                        } catch (JSONException e) {

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY_TAG", error.toString());
                        listener.onMuralError( new Error("Fout bij ophalen murals") );
                    }
                }
        );

        queue.add(request);
    }


}
