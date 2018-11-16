package nl.ralphrouwen.hue.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Bridge implements Parcelable {

    public int id;
    public String name;
    public String ip;
    public String token;


    public Bridge(int id, String name, String ip, String token) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.token = token;
    }

    protected Bridge(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ip = in.readString();
        token = in.readString();
    }

    public static final Creator<Bridge> CREATOR = new Creator<Bridge>() {
        @Override
        public Bridge createFromParcel(Parcel in) {
            return new Bridge(in);
        }

        @Override
        public Bridge[] newArray(int size) {
            return new Bridge[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(ip);
        parcel.writeString(token);
    }
}
