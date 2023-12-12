package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;

public class LearningResource extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private int ISBN;
    private String author;
    private String organization;
    private String releaseDate;
    private String type;
    private int edition;

    // CONSTRUCTOR

    public LearningResource(String name,
                            String description,
                            float price,
                            int basePoints,
                            Seller seller,
                            int stock, int ISBN,
                            String author,
                            String organization,
                            String sellDate,
                            String releaseDate,
                            String type,
                            int edition) {
        super(name, description, Category.LEARNING_RESOURCES, price, basePoints, seller, stock, sellDate);
        this.ISBN = ISBN;
        this.author = author;
        this.organization = organization;
        this.releaseDate = releaseDate;
        this.type = type;
        this.edition = edition;
    }


    // GETTERS

    public int getISBN() {
        return ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public String getOrganization() {
        return organization;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }

    public int getEdition() {
        return edition;
    }

    // SETTERS

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }
}
