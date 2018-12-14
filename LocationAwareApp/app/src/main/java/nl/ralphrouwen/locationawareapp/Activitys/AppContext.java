package nl.ralphrouwen.locationawareapp.Activitys;

import android.content.Context;

import nl.ralphrouwen.locationawareapp.Helper.GPSTracker;

public class AppContext {
    private static AppContext instance;
    private GPSTracker gpsTracker;

    public static AppContext getInstance(Context context)
    {
        if(instance == null)
            instance = new AppContext(context);
        return instance;
    }

    private AppContext(Context context)
    {
    }
}
