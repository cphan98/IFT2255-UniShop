package products;

import Users.Seller;
import otherUtility.Category;

import java.util.PropertyResourceBundle;

public class LearningResource extends Product {
    private int ISBN;
    private String author;
    private String organization;
    private String releaseDate;
    private String type;
    private int edition;

    public LearningResource(String name, String description, float price, int basePoints, Seller seller, int stock, int ISBN, String author, String organization, String sellDate, String releaseDate, String type, int edition) {
        super(name, description, Category.LEARNING_RESOURCES, price, basePoints, seller, stock, sellDate);
        this.ISBN = ISBN;
        this.author = author;
        this.organization = organization;
        this.releaseDate = releaseDate;
        this.type = type;
        this.edition = edition;
    }

    public int getISBN() {
        return ISBN;
    }
    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }
}
