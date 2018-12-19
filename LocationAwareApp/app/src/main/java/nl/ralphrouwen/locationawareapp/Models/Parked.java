package nl.ralphrouwen.locationawareapp.Models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;

import nl.ralphrouwen.locationawareapp.R;

public class Parked implements Parcelable {

    private int id;
    private float longitude;
    private float latitude;
    private DateTime startTime;
    private DateTime endTime;
    private Period parkedTime;
    private boolean valid;
    private String streetName;
    private long starttimelong;
    private long endtimelong;


    public Parked(int id, float longitude, float latitude, long startTime, long endTime, boolean valid, String streetName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.starttimelong = startTime;
        this.endtimelong = endTime;
        this.valid = valid;
        this.streetName = streetName;
    }

    public Parked(int id, float longitude, float latitude, DateTime startTime, DateTime endTime, boolean valid, String streetName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.valid = valid;
        this.streetName = streetName;
    }

    public Parked() {
    }

    public void convertStartTime()
    {
        this.startTime = new DateTime(starttimelong);
    }

    public void convertEndTime()
    {
        this.endTime = new DateTime(endtimelong);
    }


    protected Parked(Parcel in) {
        id = in.readInt();
        longitude = in.readFloat();
        latitude = in.readFloat();
        valid = in.readByte() != 0;
        streetName = in.readString();
        startTime = DateTime.parse(in.readString());
        endTime = DateTime.parse(in.readString());
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

    public String getParkedTime(Context context)
    {
        //Format parking length in dd:hh:mm
        DateTime start = this.getStartTime();
        DateTime end = this.getEndTime();
        Period timeSpan = new Period(start, end);

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" " + context.getResources().getString(R.string.year) + ", ")
                .appendMonths().appendSuffix(" " + context.getResources().getString(R.string.month) + ", ")
                .appendWeeks().appendSuffix(" " + context.getResources().getString(R.string.week) + ", ")
                .appendDays().appendSuffix(" " + context.getResources().getString(R.string.days) + ", ")
                .appendHours().appendSuffix(" " + context.getResources().getString(R.string.hours) + ", ")
                .appendMinutes().appendSuffix(" " + context.getResources().getString(R.string.minutes))
                .printZeroNever()
                .toFormatter();

        String elapsed = formatter.print(timeSpan);
        return elapsed;
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

    public long getStarttimelong() {
        return starttimelong;
    }

    public void setStarttimelong(long starttimelong) {
        this.starttimelong = starttimelong;
    }

    public long getEndtimelong() {
        return endtimelong;
    }

    public void setEndtimelong(long endtimelong) {
        this.endtimelong = endtimelong;
    }

    @Override
    public String toString() {
        return "Parked{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", parkedTime=" + parkedTime +
                ", valid=" + valid +
                ", streetName='" + streetName + '\'' +
                ", starttimelong=" + starttimelong +
                ", endtimelong=" + endtimelong +
                '}';
    }

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
        parcel.writeString(startTime.toString());
        parcel.writeString(endTime.toString());
    }
}
