package nl.ralphrouwen.locationawareapp;

import java.util.List;

public class Car {

    private Parked parkedLocation;
    private boolean parked;
    private List<Parked> locationHistory;

    public Car(Parked parkedLocation, boolean parked, List<Parked> locationHistory) {
        this.parkedLocation = parkedLocation;
        this.parked = parked;
        this.locationHistory = locationHistory;
    }

    //---------------
    //----Getters----
    //---------------
    public Parked getParkedLocation() {
        return parkedLocation;
    }

    public boolean isParked() {
        return parked;
    }

    public List<Parked> getLocationHistory() {
        return locationHistory;
    }

    //---------------
    //----Setters----
    //---------------
    public void setParkedLocation(Parked parkedLocation) {
        this.parkedLocation = parkedLocation;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    public void setLocationHistory(List<Parked> locationHistory) {
        this.locationHistory = locationHistory;
    }
}
