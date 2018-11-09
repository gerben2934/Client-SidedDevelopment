package nl.ralphrouwen.blindwalls;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Mural implements Parcelable {
    private String author;
    private String descriptionNL;
    private String descriptionENG;
    private String imageURL;
    private String year;
    private String address;
    private float longitude;
    private float latitude;
    private ArrayList images;

    public Mural(String author, String descriptionNL, String descriptionENG, String imageURL, String year, String address, float longitude, float latitude, ArrayList images) {
        this.author = author;
        this.descriptionNL = descriptionNL;
        this.descriptionENG = descriptionENG;
        this.imageURL = imageURL;
        this.year = year;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.images = images;
    }



    protected Mural(Parcel in)
    {
        author = in.readString();
        descriptionNL = in.readString();
        descriptionENG = in.readString();
        imageURL = in.readString();
        year = in.readString();
        address = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        images = in.readArrayList(null);
    }

    public static final Creator<Mural> CREATOR = new Creator<Mural>() {
        @Override
        public Mural createFromParcel(Parcel in) {
            return new Mural(in);
        }

        @Override
        public Mural[] newArray(int size) {
            return new Mural[size];
        }
    };

    public ArrayList getImages() {
        return images;
    }

    public void setImages(ArrayList images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescriptionNL() {
        return descriptionNL;
    }

    public void setDescriptionNL(String descriptionNL) {
        this.descriptionNL = descriptionNL;
    }

    public String getDescriptionENG() {
        return descriptionENG;
    }

    public void setDescriptionENG(String descriptionENG) {
        this.descriptionENG = descriptionENG;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    @Override
    public String toString() {
        return "Mural{" +
                "author='" + author + '\'' +
                ", descriptionNL='" + descriptionNL + '\'' +
                ", descriptionENG='" + descriptionENG + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", year='" + year + '\'' +
                ", address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", images=" + images +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(descriptionNL);
        parcel.writeString(descriptionENG);
        parcel.writeString(imageURL);
        parcel.writeString(year);
        parcel.writeString(address);
        parcel.writeFloat(longitude);
        parcel.writeFloat(latitude);
        parcel.writeList(images);
    }
}
