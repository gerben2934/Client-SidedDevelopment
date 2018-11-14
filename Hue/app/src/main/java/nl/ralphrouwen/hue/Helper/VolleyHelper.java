package nl.ralphrouwen.hue.Helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class VolleyHelper {

    private static VolleyHelper Instance = null;
    private Context context;
    private String requestResponse;
    private RequestQueue queue;


    public VolleyHelper(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);

    }

    public static VolleyHelper getInstance(Context context) {
        if (Instance == null) {
            Instance = new VolleyHelper(context);
        }
        return Instance;
    }

    public String volleyRequest(String requestURL, final String requestBody, int requestMethod) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(requestMethod, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
//                        Project project2 = new Project(projectName, projectYear, "@drawable/progress", "", projectDesc);
//                        listener.onProjectAvailable(project2);
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
        return requestResponse;
    }

    public void getProjects()
    {
        String url = "http://192.168.178.29/api/235ebf139caff745ab2ac6aba8e7153";
        //String url = "http://192.168.178.17:3000/tasks";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i("BWG", "OK");
                        for (int i = 0; i < response.length(); i++) {

                            JSONArray array = response;

//                                String projectname = response.getJSONObject(i).getString("projectname");
//                                String imagePath = response.getJSONObject(i).getString("image:");
//                                String projectYear = response.getJSONObject(i).getString("projectyear");
//                                String id = response.getJSONObject(i).getString("id");
//                                String descNl = response.getJSONObject(i).getJSONObject("description").getString("nl");
//                                String descEng = response.getJSONObject(i).getJSONObject("description").getString("en");
//
//                                Project project = new Project(projectname, projectYear, imagePath, descEng, descNl);
//                                listener.onProjectAvailable(project);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i("BWG", "NOT OK");
//                        listener.onProjectError(error.toString());
                    }
                }
        );
        this.queue.add(request);
    }
}


