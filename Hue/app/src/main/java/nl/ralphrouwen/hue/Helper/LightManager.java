package nl.ralphrouwen.hue.Helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Schedule;

public class LightManager {

    public static ArrayList<Light> sortLights(JSONObject jsonObject)
    {
        ArrayList<Light> lights = new ArrayList<>();
        for (int i = 1 ; i < jsonObject.length() + 1; i++) {
            try {
                String name = jsonObject.getJSONObject(String.valueOf(i)).getString("name");
                boolean status = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("state").getBoolean("on");
                int brightness = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("state").getInt("bri");
                int hue = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("state").getInt("hue");
                int sat = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("state").getInt("sat");
                String mode = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("state").getString("colormode");
                boolean reachable = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("state").getBoolean("reachable");
                Light light = new Light(i, name,status,brightness,hue,sat,mode, reachable);
                lights.add(light);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return lights;
    }

    public static ArrayList<Schedule> sortSchedules(JSONObject jsonObject)
    {
        ArrayList<Schedule> schedules = new ArrayList<>();
        for(int i = 1 ; i < jsonObject.length() + 1 ; i++)
        {
            try {
                String name = jsonObject.getJSONObject(String.valueOf(i)).getString("name");
                String description = jsonObject.getJSONObject(String.valueOf(i)).getString("description");
                String time = jsonObject.getJSONObject(String.valueOf(i)).getString("time");
//                String light = jsonObject.getJSONObject(String.valueOf(i)).getJSONObject("command").getString("address");
                String light = "12uur";
                Schedule schedule = new Schedule(name,description,time,light);
                schedules.add(schedule);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return schedules;
    }


    public static boolean handleSetLights(JSONArray array)
    {
        Log.i("test","test");
        try {
            String response = array.getJSONObject(0).getString("success");
            if(response != null)
            {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
