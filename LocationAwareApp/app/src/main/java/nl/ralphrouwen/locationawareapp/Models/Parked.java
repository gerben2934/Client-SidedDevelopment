package nl.ralphrouwen.locationawareapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import org.joda.time.DateTime;

public class Parked implements Parcelable {

    private int id;
    private float longitude;
    private float latitude;
    private DateTime startTime;
    private DateTime endTime;
    private boolean valid;
    private String streetName;

    public Parked(int id, float longitude, float latitude, DateTime startTime, DateTime endTime, boolean valid, String streetName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.valid = valid;
        this.streetName = streetName;
    }

    protected Parked(Parcel in) {
        id = in.readInt();
        longitude = in.readFloat();
        latitude = in.readFloat();
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

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
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

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
