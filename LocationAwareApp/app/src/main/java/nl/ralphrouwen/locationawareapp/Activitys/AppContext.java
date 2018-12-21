package nl.ralphrouwen.locationawareapp.Activitys;

import android.content.Context;

import nl.ralphrouwen.locationawareapp.business.RouteManager;

public class AppContext {
    private static AppContext instance;
    private RouteManager routeManager;

    public static AppContext getInstance(Context context)
    {
        if(instance == null)
            instance = new AppContext(context);
        return  instance;
    }

    private AppContext(Context context)
    {
        routeManager = new RouteManager(context);
    }

    public RouteManager getRouteManager() {
        return routeManager;
    }

    public void setRouteManager(RouteManager routeManager) {
        this.routeManager = routeManager;
    }
}
