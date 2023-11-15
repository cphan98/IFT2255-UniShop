package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;

public class Book extends Product {
    private int ISBN;
    private String author;
    private String publishingHouse;
    private String genre;
    private String releaseDate;
    private int edition;
    private int volume;

    public Book(String name, String description, float price, int basePoints, Seller seller, int stock, int ISBN, String author, String publishingHouse, String genre, String sellDate, String releaseDate, int edition, int volume) {
        super(name, description, Category.BOOKS, price, basePoints, seller, stock, sellDate);
        this.ISBN = ISBN;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.edition = edition;
        this.volume = volume;
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

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }
    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
