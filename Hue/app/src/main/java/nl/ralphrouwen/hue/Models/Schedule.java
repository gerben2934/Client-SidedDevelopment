package nl.ralphrouwen.hue.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Schedule implements Parcelable {

    public String name;
    public String description;
    public String time;
    public String lamp;

    public Schedule(String name, String description, String time, String lamp) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.lamp = lamp;
    }

    protected Schedule(Parcel in) {
        name = in.readString();
        description = in.readString();
        time = in.readString();
        lamp = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLamp() {
        return lamp;
    }

    public void setLamp(String lamp) {
        this.lamp = lamp;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", lamp='" + lamp + '\'' +
                '}';
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(time);
        dest.writeString(lamp);
    }
}
