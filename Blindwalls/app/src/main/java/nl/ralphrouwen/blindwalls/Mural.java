package nl.ralphrouwen.blindwalls;

import java.io.Serializable;

public class Mural implements Serializable {
    private String author;
    private String descriptionNL;
    private String descriptionENG;
    private String imageURL;
    private String year;
    private String address;
    private float longitude;
    private float latitude;

    public Mural(String author, String descriptionNL, String descriptionENG, String imageURL, String year, String address, float longitude, float latitude) {
        this.author = author;
        this.descriptionNL = descriptionNL;
        this.descriptionENG = descriptionENG;
        this.imageURL = imageURL;
        this.year = year;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
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
                '}';
    }
}
