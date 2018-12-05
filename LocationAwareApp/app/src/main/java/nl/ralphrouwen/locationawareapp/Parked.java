package nl.ralphrouwen.locationawareapp;

import java.text.DateFormat;

public class Parked {

    private float longitude;
    private float latitude;
    private DateFormat time;
    private boolean valid;

    public Parked(float longitude, float latitude, DateFormat time, boolean valid) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.valid = valid;
    }

    //---------------
    //----Getters----
    //---------------
    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public DateFormat getTime() {
        return time;
    }

    public boolean isValid() {
        return valid;
    }

    //---------------
    //----Setters----
    //---------------
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setTime(DateFormat time) {
        this.time = time;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
