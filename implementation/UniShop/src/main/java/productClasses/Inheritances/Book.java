package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;

public class Book extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private int ISBN;
    private String author;
    private String publishingHouse;
    private String genre;
    private String releaseDate;
    private int edition;
    private int volume;

    // CONSTRUCTOR

    public Book(String name,
                String description,
                float price,
                int basePoints,
                Seller seller,
                int stock,
                int ISBN,
                String author,
                String publishingHouse,
                String genre,
                String sellDate,
                String releaseDate,
                int edition,
                int volume) {
        super(name, description, Category.BOOKS, price, basePoints, seller, stock, sellDate);
        this.ISBN = ISBN;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.edition = edition;
        this.volume = volume;
    }

    // GETTERS

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public String getGenre(){
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getEdition() {
        return edition;
    }

    public int getVolume() {
        return volume;
    }

    // SETTERS

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
