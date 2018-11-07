package nl.ralphrouwen.blindwalls;

import java.io.Serializable;

public class Mural implements Serializable {
    private String author;
    private String descriptionNL;
    private String descriptionENG;
    private String imageURL;
    private String year;

    public Mural(String author, String descriptionNL, String descriptionENG, String imageURL, String year) {
        this.author = author;
        this.descriptionNL = descriptionNL;
        this.descriptionENG = descriptionENG;
        this.imageURL = imageURL;
        this.year = year;
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
                ", year=" + year +
                '}';
    }
}
