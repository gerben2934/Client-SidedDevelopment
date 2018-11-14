package nl.ralphrouwen.hue;

import android.os.Parcel;
import android.os.Parcelable;

public class Light implements Parcelable {

    public String name;
    public boolean on;
    public int brightness;
    public int hue;
    public int saturation;
    public String colorMode;

    //brightness hue saturation

    public Light(String name, boolean on, int bri, int hue, int sat, String colorMode, boolean reachable)
    {
        this.name = name;
        this.on = on;
        this.brightness = bri;
        this.hue = hue;
        this.saturation = sat;
        this.colorMode = colorMode;
    }

    protected Light(Parcel in) {
        name = in.readString();
        on = in.readByte() != 0;
        brightness = in.readInt();
        hue = in.readInt();
        saturation = in.readInt();
        colorMode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (on ? 1 : 0));
        dest.writeInt(brightness);
        dest.writeInt(hue);
        dest.writeInt(saturation);
        dest.writeString(colorMode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Light> CREATOR = new Creator<Light>() {
        @Override
        public Light createFromParcel(Parcel in) {
            return new Light(in);
        }

        @Override
        public Light[] newArray(int size) {
            return new Light[size];
        }
    };

    @Override
    public String toString() {
        return "Light{" +
                "name='" + name + '\'' +
                ", on=" + on +
                ", brightness=" + brightness +
                ", hue=" + hue +
                ", saturation=" + saturation +
                ", colorMode='" + colorMode + '\'' +
                '}';
    }

    //-------//
    //Getters//
    //-------//

    public String getName() {
        return name;
    }

    public int getBrightness() {
        return brightness;
    }

    public int getHue() {
        return hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public String getColorMode() {
        return colorMode;
    }

    public boolean isOn() {
        return on;
    }

    //-------//
    //Setters//
    //-------//
    public void setName(String name) {
        this.name = name;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public void setColorMode(String colorMode) {
        this.colorMode = colorMode;
    }
    //------//
}
