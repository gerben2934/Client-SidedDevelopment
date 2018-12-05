package nl.ralphrouwen.locationawareapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;

public class Parked implements Parcelable {

    private int id;
    private float longitude;
    private float latitude;
    private DateFormat startTime;
    private DateFormat endTime;
    private int minutesParked;
    private boolean valid;
    private String streetName;

    public Parked(int id, float longitude, float latitude, DateFormat startTime, DateFormat endTime, int minutesParked, boolean valid, String streetName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minutesParked = minutesParked;
        this.valid = valid;
        this.streetName = streetName;
    }

    protected Parked(Parcel in) {
        id = in.readInt();
        longitude = in.readFloat();
        latitude = in.readFloat();
        minutesParked = in.readInt();
        valid = in.readByte() != 0;
        streetName = in.readString();
    }

    public static final Creator<Parked> CREATOR = new Creator<Parked>() {
        @Override
        public Parked createFromParcel(Parcel in) {
            return new Parked(in);
        }

        @Override
        public Parked[] newArray(int size) {
            return new Parked[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeFloat(longitude);
        parcel.writeFloat(latitude);
        parcel.writeInt(minutesParked);
        parcel.writeByte((byte) (valid ? 1 : 0));
        parcel.writeString(streetName);
    }

    //---------------
    //----Getters----
    //---------------
    public int getId() {
        return id;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public DateFormat getStartTime() {
        return startTime;
    }

    public DateFormat getEndTime() {
        return endTime;
    }

    public int getMinutesParked() {
        return minutesParked;
    }

    public boolean isValid() {
        return valid;
    }

    public String getStreetName() {
        return streetName;
    }

    public static Creator<Parked> getCREATOR() {
        return CREATOR;
    }

    //---------------
    //----Setters----
    //---------------
    public void setId(int id) {
        this.id = id;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setStartTime(DateFormat startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(DateFormat endTime) {
        this.endTime = endTime;
    }

    public void setMinutesParked(int minutesParked) {
        this.minutesParked = minutesParked;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
